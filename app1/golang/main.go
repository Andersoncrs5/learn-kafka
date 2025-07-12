package main

import (
	"context"
	"golang/config"
	"golang/handlers"
	"golang/kafka"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gofiber/fiber/v2"
)

func main() {
	cfg := config.LoadConfig()

	var consumers []*kafka.Consumer

	for _, consumerCfg := range cfg.Consumers {
		var handler kafka.MessageHandler

		switch consumerCfg.Topic {

			case "topic_connect_go":
				handler = &kafka.TopicConnectGo{}
			
			case "topic_send_obj":
				handler = &kafka.TopicSendObject{}

			default:
				log.Printf("Atenção: Nenhum handler específico encontrado para o tópico '%s'. Usando ConsoleMessageHandler padrão.", consumerCfg.Topic)
				handler = &kafka.ConsoleMessageHandler{}
				
		}

		consumer := kafka.NewConsumer(cfg.KafkaBroker, consumerCfg, handler)
		consumers = append(consumers, consumer);
		consumer.Start();
	}
 
	app := fiber.New()

	app.Get("/health", handlers.HealthCheck)
		
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM) 

	go func() {
		log.Printf("Servidor Fiber iniciado na porta %s", cfg.FiberPort)
		if err := app.Listen(cfg.FiberPort); err != nil {
			log.Fatalf("Erro ao iniciar o servidor Fiber: %v", err)
		}
	}()

	<-quit
	log.Println("Recebido sinal de desligamento. Iniciando shutdown graceful...")

	fiberCtx, cancelFiber := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancelFiber()

	if err := app.ShutdownWithContext(fiberCtx); err != nil {
		log.Fatalf("Erro durante o shutdown do Fiber: %v", err)
	}
	log.Println("Servidor Fiber desligado.")

	for _, consumer := range consumers {
		if err := consumer.Shutdown(); err != nil {
			log.Printf("Erro durante o shutdown de um consumer Kafka")
		}
	}
	
	log.Println("Todos os consumers Kafka desligados.")

	log.Println("Aplicação encerrada com sucesso.")
}

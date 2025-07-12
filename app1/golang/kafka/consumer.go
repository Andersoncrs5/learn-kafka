package kafka

import (
	"context"
	"golang/config"
	"log"
	"time"

	"github.com/segmentio/kafka-go"
)

type MessageHandler interface {
	Handle(ctx context.Context, msg kafka.Message) error
}

type Consumer struct {
	reader  *kafka.Reader
	handler MessageHandler
	ctx     context.Context
	cancel  context.CancelFunc
}

func NewConsumer(broker string, consumerCfg config.ConsumerConfig, handler MessageHandler) *Consumer {
	ctx, cancel := context.WithCancel(context.Background());

	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers: []string{broker},
		Topic: consumerCfg.Topic,
		GroupID: consumerCfg.Group,
		MinBytes: 10e3,
		MaxBytes: 10e6,
		MaxWait: 6 * time.Second,
	})

	log.Printf("Consumer Kafka configurado para Tópico: '%s', Grupo: '%s'\n", consumerCfg.Topic, consumerCfg.Group)

	return &Consumer{
		reader: reader,
		handler: handler,
		ctx: ctx,
		cancel: cancel,
	}
}

func (c *Consumer) Start() {
	go func() {
		log.Printf("Iniciando leitura para o Tópico: '%s', Grupo: '%s'\n", c.reader.Config().Topic, c.reader.Config().GroupID)
		for {
			select {
			case <-c.ctx.Done(): // Contexto cancelado, hora de parar
				log.Printf("Contexto do consumer para o Tópico '%s' cancelado. Parando o loop de leitura.", c.reader.Config().Topic)
				return
			default:
				m, err := c.reader.ReadMessage(c.ctx) // Passa o contexto para ReadMessage
				if err != nil {
					// kafka.ErrClosedConnection indica que a conexão foi fechada (no shutdown)
					if err == context.Canceled {
						log.Printf("Consumer Kafka para o Tópico '%s' desligado graciosamente.", c.reader.Config().Topic)
						return
					}
					log.Printf("Erro ao ler mensagem do Tópico '%s': %v", c.reader.Config().Topic, err)
					time.Sleep(1 * time.Second) // Pequena pausa antes de tentar novamente
					continue
				}

				// Processa a mensagem usando o handler injetado
				err = c.handler.Handle(c.ctx, m)
				if err != nil {
					log.Printf("Erro ao processar mensagem do Tópico %s | Partição: %d | Offset: %d | Chave: %s | Valor: %s | Erro: %v",
						m.Topic, m.Partition, m.Offset, string(m.Key), string(m.Value), err)
					// Dependendo da lógica de negócio, você pode querer registrar, enviar para uma fila de DLQ (Dead Letter Queue), etc.
				} else {
					// Opcional: Se auto-commit não for usado e você quiser commitar manualmente após o sucesso
					// c.reader.CommitMessages(c.ctx, m)
				}
			}
		}
	}()
}

func (c *Consumer) Shutdown() error {
	log.Printf("Iniciando o shutdown do consumer Kafka para o Tópico '%s'...", c.reader.Config().Topic)
	c.cancel()
	time.Sleep(2 * time.Second);
	return c.reader.Close()
}

type TopicConnectGo struct {}

func (h *TopicConnectGo) Handle(ctx context.Context, msg kafka.Message) error {
	log.Printf("GENÉRICO - Tópico: %s | Partição: %d | Offset: %d | Chave: %s | Valor: %s\n",msg.Topic, msg.Partition, msg.Offset, string(msg.Key), string(msg.Value))
	return nil;
}

type TopicSendObject struct{}

func (h *TopicSendObject) Handle(ctx context.Context, msg kafka.Message) error {
	log.Printf("PROCESSANDO PEDIDO - Tópico: %s | Chave: %s | Valor: %s\n",msg.Topic, string(msg.Key), string(msg.Value))
	return nil
}

type ConsoleMessageHandler struct{}

func (h *ConsoleMessageHandler) Handle(ctx context.Context, msg kafka.Message) error {
	log.Printf("PROCESSANDO PEDIDO - Tópico: %s | Chave: %s | Valor: %s\n",msg.Topic, string(msg.Key), string(msg.Value))
	return nil;
}
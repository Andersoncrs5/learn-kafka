package config

import (
	"fmt"
	"log"
	"os"

	"github.com/joho/godotenv"
)

type ConsumerConfig struct {
	Topic string
	Group string
}

type AppConfig struct {
	FiberPort   string
	KafkaBroker string
	Consumers   []ConsumerConfig
}

func LoadConfig() *AppConfig  {
	err := godotenv.Load();
	if err != nil && !os.IsNotExist(err) {
		log.Printf("Atenção: Não foi possível carregar o arquivo .env: %v", err)
	}

	cfg := &AppConfig{
		FiberPort:   getEnv("FIBER_PORT", ":3000"),
		KafkaBroker: getEnv("KAFKA_BROKER", "localhost:9092"),
	}

	numConsumers := 0
	for {
		topicKey := fmt.Sprintf("KAFKA_CONSUMER_%d_TOPIC", numConsumers+1)
		groupKey := fmt.Sprintf("KAFKA_CONSUMER_%d_GROUP", numConsumers+1)

		topic := getEnv(topicKey, "")
		group := getEnv(groupKey, "")

		if topic == "" || group == "" {
			break
		}

		cfg.Consumers = append(cfg.Consumers, ConsumerConfig{
			Topic: topic,
			Group: group,
		})

		numConsumers++;
	}

	if numConsumers == 0 {
		log.Println("Aviso: Nenhuma configuração de consumer Kafka encontrada nas variáveis de ambiente.")
	}

	return cfg
}


func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}

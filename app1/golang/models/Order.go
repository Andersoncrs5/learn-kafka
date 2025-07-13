// models/order.go
package models

import "time"

type Order struct {
	OrderID   string    `json:"orderId"`
	ProductID string    `json:"productId"`
	Quantity  int       `json:"quantity"`
	Status    string    `json:"status"`
	Timestamp time.Time `json:"timestamp"`
}
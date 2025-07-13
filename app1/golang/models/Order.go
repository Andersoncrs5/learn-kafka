// models/order.go
package models

type Order struct {
	OrderID   string    `json:"orderId"`
	ProductID string    `json:"productId"`
	Quantity  int       `json:"quantity"`
	Status    string    `json:"status"`
}
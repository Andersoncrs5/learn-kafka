package com.kafka.app1.entity;

import lombok.Data;

@Data
public class Order {

    private String OrderID;
    private String ProductID;
    private int Quantity;
    private String Status;

}

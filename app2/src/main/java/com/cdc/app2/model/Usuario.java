package com.cdc.app2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@Data
public class Usuario {

    @Id
    private String id;
    private Integer userId;

    public Usuario(Integer userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    private String name;
    private String email;

}

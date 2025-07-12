package com.cdc.app2.controller;

import com.cdc.app2.model.Usuario;
import com.cdc.app2.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    public Iterable<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }
}

package com.cdc.app2.service;

import com.cdc.app2.model.Usuario;
import com.cdc.app2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void salvarOuAtualizar(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByUserId(usuario.getUserId());

        if (existente.isPresent()) {
            existente.get().setName(usuario.getName());
            existente.get().setEmail(usuario.getEmail());
            usuarioRepository.save(existente.get());
        } else {
            usuarioRepository.save(usuario);
        }
    }

    public Iterable<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

}

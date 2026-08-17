package com.javanauta.revisaousuario.controller;

import com.javanauta.revisaousuario.business.UsuarioService;
import com.javanauta.revisaousuario.business.dtos.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(dto));
    }
}

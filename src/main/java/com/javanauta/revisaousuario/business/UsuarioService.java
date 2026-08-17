package com.javanauta.revisaousuario.business;

import com.javanauta.revisaousuario.business.converter.UsuarioConverter;
import com.javanauta.revisaousuario.business.dtos.UsuarioDTO;
import com.javanauta.revisaousuario.infrastructure.entity.Usuario;
import com.javanauta.revisaousuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO dto) {
        Usuario entity = usuarioConverter.paraUsuarioEntity(dto);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(entity));
    }
}

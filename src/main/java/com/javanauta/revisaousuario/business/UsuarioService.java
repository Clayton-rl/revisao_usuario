package com.javanauta.revisaousuario.business;

import com.javanauta.revisaousuario.business.converter.UsuarioConverter;
import com.javanauta.revisaousuario.business.dtos.UsuarioDTO;
import com.javanauta.revisaousuario.infrastructure.entity.Usuario;
import com.javanauta.revisaousuario.infrastructure.exceptions.ConflictException;
import com.javanauta.revisaousuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.revisaousuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO dto) {
        emailExiste(dto.getEmail());
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario entity = usuarioConverter.paraUsuarioEntity(dto);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(entity));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado " + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscaUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Email não encontrado " + email ));
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}

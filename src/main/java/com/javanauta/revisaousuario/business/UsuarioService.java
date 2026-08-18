package com.javanauta.revisaousuario.business;

import com.javanauta.revisaousuario.business.converter.UsuarioConverter;
import com.javanauta.revisaousuario.business.dtos.EnderecoDTO;
import com.javanauta.revisaousuario.business.dtos.TelefoneDTO;
import com.javanauta.revisaousuario.business.dtos.UsuarioDTO;
import com.javanauta.revisaousuario.infrastructure.entity.Endereco;
import com.javanauta.revisaousuario.infrastructure.entity.Telefone;
import com.javanauta.revisaousuario.infrastructure.entity.Usuario;
import com.javanauta.revisaousuario.infrastructure.exceptions.ConflictException;
import com.javanauta.revisaousuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.revisaousuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.revisaousuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.revisaousuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.revisaousuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

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

    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        Usuario entity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado"));
        Usuario usuarioEntity = usuarioConverter.updateUsuario(dto, entity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuarioEntity));
    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO dto) {
        Endereco entity = enderecoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + id));
        Endereco enderecoEntity = usuarioConverter.updateEndereco(dto, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoEntity));
    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + id));
        Telefone telefoneEntity = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneEntity));
    }
}

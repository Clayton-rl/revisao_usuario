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
import com.javanauta.revisaousuario.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.revisaousuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.revisaousuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.revisaousuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.revisaousuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authenticationManager;

    private final String EMAIL_NAO_EMCONTRADO = "Email não encontrado ";
    private final String ID_NAO_EMCONTRADO = "Id não encontrado ";
    private final String EMAIL_CADASTRADO = "Email já cadastrado ";

    public UsuarioDTO salvaUsuario(UsuarioDTO dto) {
        emailExiste(dto.getEmail());
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario entity = usuarioConverter.paraUsuarioEntity(dto);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(entity));
    }

    public String autenticaUsuario(UsuarioDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());

        } catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e) {
            throw new UnauthorizedException("Usuário ou senha inválidos", e.getCause());
        }
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException(EMAIL_CADASTRADO + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException(EMAIL_CADASTRADO, e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email).orElseThrow(
                            () -> new ResourceNotFoundException(EMAIL_NAO_EMCONTRADO + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(EMAIL_NAO_EMCONTRADO + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        Usuario entity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(EMAIL_NAO_EMCONTRADO));
        Usuario usuarioEntity = usuarioConverter.updateUsuario(dto, entity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuarioEntity));
    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO dto) {
        Endereco entity = enderecoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_EMCONTRADO + id));
        Endereco enderecoEntity = usuarioConverter.updateEndereco(dto, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoEntity));
    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(ID_NAO_EMCONTRADO + id));
        Telefone telefoneEntity = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneEntity));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(EMAIL_NAO_EMCONTRADO + email));
        Endereco endereco = usuarioConverter.paraEnderecoEntityId(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(EMAIL_NAO_EMCONTRADO + email));
        Telefone telefone = usuarioConverter.paraTelefoneEntityId(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}

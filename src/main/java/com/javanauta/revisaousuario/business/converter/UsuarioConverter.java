package com.javanauta.revisaousuario.business.converter;

import com.javanauta.revisaousuario.business.dtos.EnderecoDTO;
import com.javanauta.revisaousuario.business.dtos.TelefoneDTO;
import com.javanauta.revisaousuario.business.dtos.UsuarioDTO;
import com.javanauta.revisaousuario.infrastructure.entity.Endereco;
import com.javanauta.revisaousuario.infrastructure.entity.Telefone;
import com.javanauta.revisaousuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuarioEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .enderecos(paraListaEnderecoEntity(dto.getEnderecos()))
                .telefones(paraListaTelefoneEntity(dto.getTelefones()))
                .build();

    }

    public List<Endereco> paraListaEnderecoEntity(List<EnderecoDTO> dtos) {
        return dtos.stream().map(this::paraEnderecoEntity).toList();
    }

    public Endereco paraEnderecoEntity(EnderecoDTO dto) {
        return Endereco.builder()
                .numero(dto.getNumero())
                .rua(dto.getRua())
                .complemento(dto.getComplemento())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .build();
    }

    public List<Telefone> paraListaTelefoneEntity(List<TelefoneDTO> dtos) {
        return dtos.stream().map(this::paraTelefoneEntity).toList();
    }

    public Telefone paraTelefoneEntity(TelefoneDTO dto) {
        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario entity) {
        return UsuarioDTO.builder()
                .nome(entity.getNome())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .enderecos(paraListaEnderecoDTO(entity.getEnderecos()))
                .telefones(paraListaTelefoneDTO(entity.getTelefones()))
                .build();

    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> entity) {
        return entity.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco entity) {
        return EnderecoDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .rua(entity.getRua())
                .complemento(entity.getComplemento())
                .cidade(entity.getCidade())
                .estado(entity.getEstado())
                .cep(entity.getCep())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> entity) {
        return entity.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone entity) {
        return TelefoneDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .ddd(entity.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO dto, Usuario entity) {
        return Usuario.builder()
                .id(entity.getId())
                .nome(dto.getNome() != null ? dto.getNome() : entity.getNome())
                .email(dto.getEmail() != null ? dto.getEmail() : entity.getEmail())
                .senha(dto.getSenha() != null ? dto.getSenha() : entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco updateEndereco(EnderecoDTO dto, Endereco entity) {
        return Endereco.builder()
                .id(entity.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .rua(dto.getRua()!= null ? dto.getRua() : entity.getRua())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .build();
    }

    public Telefone updateTelefone(TelefoneDTO dto, Telefone entity) {
        return Telefone.builder()
                .id(entity.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .build();
    }

    public Endereco paraEnderecoEntityId(EnderecoDTO dto, Long idUsuario) {
        return Endereco.builder()
                .numero(dto.getNumero())
                .rua(dto.getRua())
                .complemento(dto.getComplemento())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .usuario_id(idUsuario)
                .build();
    }

    public Telefone paraTelefoneEntityId(TelefoneDTO dto, Long idUsuario) {
        return Telefone.builder()
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .usuario_id(idUsuario)
                .build();
    }
}

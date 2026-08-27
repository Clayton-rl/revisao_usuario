package com.javanauta.revisaousuario.business.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDTO {

    private Long id;
    private Long numero;
    private String rua;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
}

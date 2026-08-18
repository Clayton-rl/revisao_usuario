package com.javanauta.revisaousuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero", length = 10)
    private Long numero;
    @Column(name = "rua", length = 100)
    private String rua;
    @Column(name = "complemento", length = 100)
    private String complemento;
    @Column(name = "cidade", length = 100)
    private String cidade;
    @Column(name = "estado", length = 2)
    private String estado;
    @Column(name = "cep", length = 100)
    private String cep;
}

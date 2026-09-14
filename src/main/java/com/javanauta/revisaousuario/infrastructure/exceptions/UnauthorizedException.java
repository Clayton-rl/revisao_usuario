package com.javanauta.revisaousuario.infrastructure.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String mensagem) {
        super(mensagem);
    }

    public UnauthorizedException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}

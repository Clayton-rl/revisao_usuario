package com.javanauta.revisaousuario.infrastructure.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {

    @RequestMapping("/ws/{cep}/json/")
    ViaCepDTO buscaDadosEndereco(@PathVariable("cep") String cep);
}

package com.fiap.techchallenge3.client;

import com.fiap.techchallenge3.model.dto.LocalizacaoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCep", url = "https://viacep.com.br")
public interface ViaCepClient {

    @GetMapping(value = "/ws/{cep}/json/")
    LocalizacaoDTO getAddressByCep(@PathVariable(value = "cep") final String cep);

}

package com.fiap.techchallenge3.infrastructure.localizacao.client;

import com.fiap.techchallenge3.domain.localizacao.service.LocalizacaoAdapter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCep", url = "https://viacep.com.br")
public interface ViaCepClient extends LocalizacaoAdapter {

    @Override
    @GetMapping(value = "/ws/{cep}/json/")
    ViaCepResponse pegaLocalizacao(@PathVariable(value = "cep") final String cep);

}

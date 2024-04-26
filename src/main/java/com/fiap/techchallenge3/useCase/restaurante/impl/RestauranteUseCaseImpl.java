package com.fiap.techchallenge3.useCase.restaurante.impl;

import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteSpecification;
import com.fiap.techchallenge3.infrastructure.restaurante.model.TipoCozinhaEnum;
import com.fiap.techchallenge3.model.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.model.dto.ExibeBuscaRestauranteDTO;
import com.fiap.techchallenge3.useCase.restaurante.RestauranteUseCase;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RestauranteUseCaseImpl implements RestauranteUseCase {

    private final RestauranteRepository repositoryRestaurante;

    public RestauranteUseCaseImpl(final RestauranteRepository repositoryRestaurante) {
        this.repositoryRestaurante = repositoryRestaurante;
    }


    @Override
    public void cadastra(final CriaRestauranteDTO dadosRestaurante) {
        this.repositoryRestaurante.save(dadosRestaurante.converte());
    }

    @Override
    public List<ExibeBuscaRestauranteDTO> busca(String nome,
                                                String cep,
                                                String bairro,
                                                String cidade,
                                                String estado,
                                                TipoCozinhaEnum tipoCozinha) {
        var resultadosDaBusca = this.repositoryRestaurante.findAll(
                Specification
                        .where(RestauranteSpecification.nome(Objects.isNull(nome) ? "" : nome))
                        .and(RestauranteSpecification.cep(Objects.isNull(cep) ? "" : cep.replace("-", "")))
                        .and(RestauranteSpecification.bairro(Objects.isNull(bairro) ? "" : bairro))
                        .and(RestauranteSpecification.cidade(Objects.isNull(cidade) ? "" : cidade))
                        .and(RestauranteSpecification.estado(Objects.isNull(estado) ? "" : estado))
                        .and(RestauranteSpecification.tipoCozinha(Objects.isNull(tipoCozinha) ? "" : tipoCozinha.name())
                        )
        );
        return resultadosDaBusca
                .stream()
                .map(registro -> new ExibeBuscaRestauranteDTO(
                        registro.getNome(),
                        registro.getLogradouro(),
                        registro.getNumeroEndereco(),
                        registro.getCep(),
                        registro.getBairro(),
                        registro.getCidade(),
                        registro.getEstado(),
                        registro.getComplemento(),
                        registro.getTipoCozinha(),
                        registro.getDiasFuncionamento(),
                        registro.getHorarioFuncionamento(),
                        registro.getCapacidadeDePessoas())
                )
                .toList();
    }

}

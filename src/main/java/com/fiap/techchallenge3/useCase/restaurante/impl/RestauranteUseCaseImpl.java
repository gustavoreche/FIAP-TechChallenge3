package com.fiap.techchallenge3.useCase.restaurante.impl;

import com.fiap.techchallenge3.domain.localizacao.model.Cep;
import com.fiap.techchallenge3.domain.localizacao.model.Localizacao;
import com.fiap.techchallenge3.domain.restaurante.model.*;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.CriaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.controller.dto.ExibeBuscaRestauranteDTO;
import com.fiap.techchallenge3.infrastructure.restaurante.model.RestauranteEntity;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteSpecification;
import com.fiap.techchallenge3.useCase.restaurante.RestauranteUseCase;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RestauranteUseCaseImpl implements RestauranteUseCase {

    private final RestauranteRepository repository;

    public RestauranteUseCaseImpl(final RestauranteRepository repository) {
        this.repository = repository;
    }


    @Override
    public void cadastra(final CriaRestauranteDTO dadosRestaurante) {
        var cep = new Cep(
                Objects.isNull(dadosRestaurante.localizacao()) ? null : dadosRestaurante.localizacao().cep()
        );
        var localizacao = new Localizacao(
                dadosRestaurante.localizacao().logradouro(),
                cep,
                dadosRestaurante.localizacao().bairro(),
                dadosRestaurante.localizacao().cidade(),
                dadosRestaurante.localizacao().estado()
        );
        var localizacaoRestaurante = new LocalizacaoRestaurante(
                localizacao,
                dadosRestaurante.localizacao().numero(),
                dadosRestaurante.localizacao().complemento()
        );
        var horarioDeFuncionamento = new HorarioDeFuncionamento(
                Objects.isNull(dadosRestaurante.horarioFuncionamento()) ? null : dadosRestaurante.horarioFuncionamento().diasAbertos(),
                Objects.isNull(dadosRestaurante.horarioFuncionamento()) ? null : dadosRestaurante.horarioFuncionamento().horarioFuncionamento()
        );
        var restaurante = new Restaurante(
                dadosRestaurante.cnpj(),
                dadosRestaurante.nome(),
                localizacaoRestaurante,
                dadosRestaurante.tipoCozinha(),
                horarioDeFuncionamento,
                dadosRestaurante.capacidadeDePessoas()
        );

        var restauranteEntity = new RestauranteEntity(
                restaurante.cnpj()
                        .replace(".", "")
                        .replace("/", "")
                        .replace("-", ""),
                restaurante.nome(),
                restaurante.tipoCozinha(),
                restaurante.funcionamento().diasAbertos().toString(),
                restaurante.funcionamento().horarioFuncionamento(),
                restaurante.capacidadeDePessoas(),
                restaurante.localizacaoCompleta().localizacao().cep().numero()
                        .replace("-", ""),
                restaurante.localizacaoCompleta().localizacao().logradouro(),
                restaurante.localizacaoCompleta().numero(),
                restaurante.localizacaoCompleta().localizacao().bairro(),
                restaurante.localizacaoCompleta().localizacao().cidade(),
                restaurante.localizacaoCompleta().localizacao().estado(),
                restaurante.localizacaoCompleta().complemento()
        );

        this.repository.save(restauranteEntity);
    }

    @Override
    public List<ExibeBuscaRestauranteDTO> busca(String nome,
                                                String cep,
                                                String bairro,
                                                String cidade,
                                                String estado,
                                                TipoCozinhaEnum tipoCozinha) {
        var buscaRestaurante = new BuscaRestaurante(
                nome,
                cep,
                bairro,
                cidade,
                estado,
                tipoCozinha
        );
        var resultadosDaBusca = this.repository.findAll(
                Specification
                        .where(RestauranteSpecification.nome(Objects.isNull(buscaRestaurante.nome()) ? "" : buscaRestaurante.nome()))
                        .and(RestauranteSpecification.cep(Objects.isNull(buscaRestaurante.cep()) ? "" : buscaRestaurante.cep().replace("-", "")))
                        .and(RestauranteSpecification.bairro(Objects.isNull(buscaRestaurante.bairro()) ? "" : buscaRestaurante.bairro()))
                        .and(RestauranteSpecification.cidade(Objects.isNull(buscaRestaurante.cidade()) ? "" : buscaRestaurante.cidade()))
                        .and(RestauranteSpecification.estado(Objects.isNull(buscaRestaurante.estado()) ? "" : buscaRestaurante.estado()))
                        .and(RestauranteSpecification.tipoCozinha(Objects.isNull(buscaRestaurante.tipoCozinha()) ? "" : buscaRestaurante.tipoCozinha().name())
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

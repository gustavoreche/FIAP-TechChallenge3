package com.fiap.techchallenge3.useCase.avaliacao.impl;

import com.fiap.techchallenge3.domain.avaliacao.Avaliacao;
import com.fiap.techchallenge3.domain.reserva.CpfCnpj;
import com.fiap.techchallenge3.domain.restaurante.Cnpj;
import com.fiap.techchallenge3.infrastructure.avaliacao.controller.dto.AvaliacaoDTO;
import com.fiap.techchallenge3.infrastructure.avaliacao.model.AvaliacaoEntity;
import com.fiap.techchallenge3.infrastructure.avaliacao.repository.AvaliacaoRepository;
import com.fiap.techchallenge3.infrastructure.restaurante.repository.RestauranteRepository;
import com.fiap.techchallenge3.useCase.avaliacao.AvaliacaoUseCase;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoUseCaseImpl implements AvaliacaoUseCase {

    private final RestauranteRepository repositoryRestaurante;
    private final AvaliacaoRepository repositoryAvaliacao;

    public AvaliacaoUseCaseImpl(final RestauranteRepository repositoryRestaurante,
                                final AvaliacaoRepository repositoryAvaliacao) {
        this.repositoryRestaurante = repositoryRestaurante;
        this.repositoryAvaliacao = repositoryAvaliacao;
    }

    @Override
    public void avalia(String cnpjRestaurante,
                       String cpfCnpjCliente,
                       AvaliacaoDTO avaliacaoDTO) {
        var cnpjObject = new Cnpj(cnpjRestaurante);
        var cpfCnpjObject = new CpfCnpj(cpfCnpjCliente);
        var avaliacao = new Avaliacao(
                cnpjObject.numero(),
                cpfCnpjObject.numero(),
                avaliacaoDTO.comentario(),
                avaliacaoDTO.nota()
        );

        var restaurante = this.repositoryRestaurante.findById(cnpjObject.numero());
        if(restaurante.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado!");
        }

        var avaliacaoEntity = AvaliacaoEntity.builder()
                .cnpjRestaurante(cnpjObject.numero())
                .cpfCnpjCliente(cpfCnpjObject.numero())
                .comentario(avaliacao.comentario())
                .nota(avaliacao.nota())
                .build();
        this.repositoryAvaliacao.save(avaliacaoEntity);
    }

}

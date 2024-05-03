package com.fiap.techchallenge3.domain.avaliacao;

import java.util.Objects;

public record Avaliacao(
        String cnpjRestaurante,
        String cpfCnpjCliente,
        String comentario,
        Integer nota
) {

    public Avaliacao {
        if (Objects.isNull(comentario) || comentario.isEmpty()) {
            throw new IllegalArgumentException("COMENTÁRIO NAO PODE SER NULO OU VAZIO!");
        }
        if (comentario.length() < 3 || comentario.length() > 100) {
            throw new IllegalArgumentException("O comentário deve ter no mínimo 3 letras e no máximo 100 letras");
        }

        if (Objects.isNull(nota)) {
            throw new IllegalArgumentException("NOTA NAO PODE SER NULO!");
        }
        if (nota < 1 || nota > 10) {
            throw new IllegalArgumentException("A nota deve ter no mínimo 1 ou no máximo 10");
        }

    }

}

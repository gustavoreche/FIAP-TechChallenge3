# language: pt

Funcionalidade: Teste de avaliação da visita ao restaurante

  Cenário: Avalia o restaurante com sucesso
    Dado que um restaurante cadastrado no sistema
    E desejo realizar a avaliação da visita a esse restaurante
    Quando realizo a avaliação do restaurante
    Entao recebo uma resposta que a avaliação foi realizada com sucesso

  Cenário: Avalia o restaurante não cadastrado no sistema
    Dado que um restaurante cadastrado no sistema
    E desejo realizar a avaliação da visita de um restaurante não cadastrado no sistema
    Quando realizo a avaliação do restaurante
    Entao recebo uma resposta que a avaliação não foi realizada com sucesso

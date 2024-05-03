# language: pt

Funcionalidade: Teste de buscar reserva no restaurante

  Cenário: Busca reserva pendente do dia atual do restaurante
    Dado que existe determinado restaurante cadastrado no sistema
    E um cliente efetuou uma reserva nesse determinado restaurante
    E desejo buscar as reservas pendentes do dia
    Quando realizo a busca das reservas pendentes do dia atual
    Entao recebo uma resposta das reservas pendentes do dia atual

  Cenário: Não busca reserva pendente do dia atual do restaurante porque foi informado CNPJ inválido
    Dado que existe determinado restaurante cadastrado no sistema
    E um cliente efetuou uma reserva nesse determinado restaurante
    E desejo buscar as reservas pendentes do dia informando um CNPJ inválido
    Quando realizo a busca das reservas pendentes do dia atual
    Entao recebo uma resposta que o CNPJ esta inválido

  Cenário: Não encontra reservas pendente do dia atual do restaurante
    Dado que existe determinado restaurante cadastrado no sistema
    E um cliente efetuou uma reserva nesse determinado restaurante
    E desejo buscar as reservas pendentes do dia de outro restaurante
    Quando realizo a busca das reservas pendentes do dia atual
    Entao recebo uma resposta que não existe reserva no dia atual

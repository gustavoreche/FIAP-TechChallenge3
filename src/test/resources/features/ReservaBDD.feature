# language: pt

Funcionalidade: Teste de realizar reserva no restaurante

  Cenário: Realiza reserva no restaurante com sucesso
    Dado que existe restaurante cadastrado no sistema
    E informo os dados para realizar a reserva no restaurante
    Quando realizo a reserva
    Entao recebo uma resposta que a reserva será analisada

  Cenário: Não realiza reserva porque o restaurante não esta cadastrado no sistema
    Dado que existe restaurante cadastrado no sistema
    E informo os dados para realizar a reserva de um restaurante que não esta cadastrado no sistema
    Quando realizo a reserva
    Entao recebo uma resposta que a reserva não foi realizada

  Cenário: Não realiza reserva porque o restaurante não esta fechado
    Dado que existe restaurante cadastrado no sistema
    E informo os dados para realizar a reserva de um restaurante que esta fechado no horário informado
    Quando realizo a reserva
    Entao recebo uma resposta que a reserva não foi realizada

  Cenário: Não realiza reserva porque o restaurante não abriu ainda
    Dado que existe restaurante cadastrado no sistema
    E informo os dados para realizar a reserva de um restaurante que ainda não abriu no horário informado
    Quando realizo a reserva
    Entao recebo uma resposta que a reserva não foi realizada

  Cenário: Não realiza reserva porque o restaurante já esta lotado
    Dado que existe restaurante cadastrado no sistema
    E informo os dados para realizar a reserva de um restaurante que já esta lotado no horário informado
    Quando realizo a reserva
    Entao recebo uma resposta que a reserva não foi realizada

# language: pt

Funcionalidade: Teste de atualizar reserva no restaurante

  Cenário: Atualiza reserva no restaurante com sucesso
    Dado que existe um restaurante cadastrado no sistema
    E um cliente efetuou uma reserva nesse restaurante
    E desejo efetivar a reserva do cliente
    Quando realizo a atualização da reserva
    Entao recebo uma resposta que a reserva foi atualizada com sucesso

  Cenário: Não atualiza reserva porque não encontrou a reserva informada
    Dado que existe um restaurante cadastrado no sistema
    E desejo efetivar uma reserva que não existe
    Quando realizo a atualização da reserva
    Entao recebo uma resposta que a reserva não foi atualizada com sucesso

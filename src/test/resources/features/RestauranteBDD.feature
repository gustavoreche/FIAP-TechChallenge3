# language: pt

Funcionalidade: Teste de cadastro de restaurante

  Cenário: Cadastra restaurante com sucesso
    Dado que tenho dados validos de um restaurante
    Quando cadastro esse restaurante
    Entao recebo uma resposta que o restaurante foi cadastrado com sucesso

  Cenário: Cadastra restaurante com sucesso com funcionamento 24 horas
    Dado que tenho dados validos de um restaurante que funciona 24 horas
    Quando cadastro esse restaurante
    Entao recebo uma resposta que o restaurante foi cadastrado com sucesso

  Cenário: Cadastra restaurante com sucesso sem complemento no endereço
    Dado que tenho dados validos de um restaurante que não tem complemento no endereço
    Quando cadastro esse restaurante
    Entao recebo uma resposta que o restaurante foi cadastrado com sucesso

  Cenário: Cadastra restaurante com sucesso sem complemento no endereço
    Dado que tenho dados validos de um restaurante que não tem complemento no endereço
    Quando cadastro esse restaurante
    Entao recebo uma resposta que o restaurante foi cadastrado com sucesso

  Cenário: Cadastra restaurante com sucesso que funcione todos os dias
    Dado que tenho dados validos de um restaurante que funciona todos os dias
    Quando cadastro esse restaurante
    Entao recebo uma resposta que o restaurante foi cadastrado com sucesso

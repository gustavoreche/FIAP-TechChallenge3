# language: pt

Funcionalidade: Teste de busca de restaurante

  Cenário: Busca restaurante com sucesso
    Dado que tenho restaurantes cadastrados no sistema
    E utilizo todos os filtros de busca
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por nome
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por nome
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por cep
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por cep
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por bairro
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por bairro
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por cidade
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por cidade
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por estado
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por estado
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso filtrando por tipo cozinha
    Dado que tenho restaurantes cadastrados no sistema
    E filtro por tipo cozinha
    Quando busco por um restaurante
    Entao recebo as informações do restaurante

  Cenário: Busca restaurante com sucesso sem filtro
    Dado que tenho restaurantes cadastrados no sistema
    E não utilizo filtros
    Quando busco por um restaurante
    Entao recebo as informações de todos os restaurante

  Cenário: Busca restaurante sem sucesso
    Dado que tenho restaurantes cadastrados no sistema
    E os filtros não retornam resultados
    Quando busco por um restaurante
    Entao recebo as informações que não foi encontrado nenhum restaurante


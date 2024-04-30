# language: pt

Funcionalidade: Teste de localização

#  Cenário: Localização existe
#    Dado que tenho um CEP válido
#    Quando busco por este CEP
#    Entao recebo uma resposta com a localização correspondente
#
#  Cenário: Localização não existe
#    Dado que tenho um CEP inválido
#    Quando busco por este CEP
#    Entao recebo uma resposta que o endereço não existe

  Esquema do Cenário: Informando formato de CEPs inválidos
    Dado que informo um CEP inválido "<cep>"
    Quando busco por este CEP
    Entao recebo uma resposta que o CEP informado é inválido

    Exemplos:
      | cep        |
      | a          |
      | aaaaaaaaaa |
      | 1234       |
      | 1          |
      | 14025710   |
      | 14025-7101 |
      | 114025-710 |

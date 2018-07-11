### DownloadMail

Este é um simples projeto em Java que, dada uma conta de email, baixar
todos os anexos a partir de uma série de filtros. Atualmente, implementei
os filtros de "lidos/não-lidos" e por data.

Fiz esse algoritmo em virtude de uma necessidade de baixar uma quantidade
considerável de emails no trabalho (em torno de 90 mil). Embora este
código não seja o mesmo que utilizei, segue a mesma base.

Utilizei o padrão Builder para instancialização da classe, permitindo
uma flexibilidade maior, já que o uso de parâmetros no construtor seria
muito elevado.

Há aindas ajustes a fazer:

+ Fazer o download dos anexos serem feitos em threads separadas, para minimizar
o tempo gasto.
+ Criar testes unitários para as classes.
+ Criar diferentes modos de instanciar o arquivo ReadConfig, que
passa alguns parâmetros de conexão (porta e endereço IMAP, por exemplo)
+ Melhorar a documentação (hoje inexistente).

Os parâmetros usuário, senha e caminho para salvar os arquivos são
passados na linha de comando, ao executar o programa.
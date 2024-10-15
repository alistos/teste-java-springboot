# Microsserviço de Processos

Este projeto é um microsserviço desenvolvido com Spring Boot e PostgreSQL para gerenciar números de processos e réus.

## Requisitos

- Java 17 ou superior
- Maven 3.6.3 ou superior
- PostgreSQL 12 ou superior

## Configuração do Projeto

### 1. Clonar o Repositório

    git clone https://github.com/alistos/teste-java-springboot.git
    cd teste-java-springboot


### 2. Configurar as Propriedades da Aplicação

    Atualize o arquivo src/main/resources/application.properties com as credenciais do seu banco de dados:

    spring.datasource.url=jdbc:postgresql://localhost:5432/teste_tecnico
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.liquibase.enabled=true

### 3. Compilar e Executar a Aplicação
    
    Compile e execute a aplicação usando Maven:

    mvn clean install
    mvn spring-boot:run

### 4. Endpoints

    Você pode importar os endpoints no Insomnia através do arquivo `collection-processos` localizado na raiz do projeto.

### 5. Estrutura do Banco de Dados
    
    A estrutura do banco de dados é gerada automaticamente pelo Liquibase. O arquivo de configuração está localizado em src/main/resources/db/changelog/db.changelog-master.yaml.  

### 6. Contribuição
    
    Faça um fork do projeto
    Crie uma branch para sua feature (git checkout -b feature/nova-feature)
    Commit suas mudanças (git commit -am 'Adiciona nova feature')
    Faça um push para a branch (git push origin feature/nova-feature)
    Crie um novo Pull Request

### 7. Licença
    Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para mais detalhes.
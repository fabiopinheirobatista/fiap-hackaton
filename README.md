# fiap-hackaton
Desenvolvimento da 5ª fase do FIAP - Hackathon: Sistema de Agendamento de Consultas Médicas

## Sobre o Projeto

O projeto **fiap-hackaton** é uma API REST desenvolvida para o gerenciamento de agendamentos de consultas médicas e exames. O sistema permite que pacientes agendem, consultem, cancelem e reagendem suas consultas, além de fornecer recursos administrativos para gerenciamento da agenda médica.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação base
- **Spring Boot 3.5.5**: Framework para desenvolvimento da API REST
- **Spring Data JPA**: Para persistência de dados
- **Spring Security**: Para autenticação e autorização
- **Spring Validation**: Para validação de dados de entrada
- **Flyway**: Para gerenciamento e versionamento de migrações do banco de dados
- **MySQL**: Banco de dados relacional para ambiente de produção
- **H2 Database**: Banco de dados em memória para ambiente de desenvolvimento e testes
- **Lombok**: Para redução de código boilerplate
- **Maven**: Gerenciamento de dependências e build
- **JUnit e Spring Test**: Para testes unitários e de integração

## Arquitetura

O projeto segue a arquitetura MVC (Model-View-Controller) com as seguintes camadas:

- **Controller**: Recebe as requisições HTTP e delega ao serviço correspondente
- **Service**: Contém a lógica de negócio da aplicação
- **Repository**: Responsável pelo acesso aos dados
- **Entity**: Mapeamento das tabelas do banco de dados
- **DTO**: Objetos de transferência de dados entre camadas
- **Enum**: Tipos enumerados usados no sistema

## Modelo de Dados

A estrutura do banco de dados inclui as seguintes entidades principais:

1. **Pacientes**: Armazena informações dos pacientes
2. **Unidades**: Representa as unidades de atendimento médico
3. **Agendamentos**: Registra os agendamentos de consultas e exames
4. **Horários Disponíveis**: Controla os horários disponíveis para agendamento
5. **Triagens**: Armazena informações de triagem para alguns tipos de agendamento
6. **Bloqueios**: Registra bloqueios de horários (por férias de profissionais, etc.)
7. **Lista de Espera**: Controla pacientes em lista de espera para atendimento

## Dados de Teste

A aplicação utiliza o Flyway para gerar automaticamente dados de teste quando iniciada. **Não é necessário fazer requisições para inserir massas de dados**. Os dados de teste incluem:

### Pacientes
- **p1**: João Silva
- **p2**: Maria Oliveira 
- **p3**: Carlos Santos

### Unidades de Atendimento
- **u1**: Clínica Central (Centro)
  - Especialidades: CLINICO_GERAL, CARDIOLOGIA, ORTOPEDIA
- **u2**: Hospital Zona Sul (Zona Sul)
  - Especialidades: CLINICO_GERAL, EXAME_SANGUE, PEDIATRIA
- **u3**: Posto de Saúde Norte (Zona Norte)
  - Especialidades: CLINICO_GERAL, GINECOLOGIA

### Horários Disponíveis
- Unidade u1 (prof1): Dias 17, 18 e 25 de setembro de 2025, diversos horários
- Unidade u2 (prof2): Dias 17 e 18 de setembro de 2025, diversos horários
- Unidade u3 (prof3): Dias 17 e 18 de setembro de 2025, diversos horários

### Triagens
- **t1**: Paciente p1, urgência 5, sangramento intenso
- **t2**: Paciente p2, urgência 4, pré-operatório cirurgia cardíaca

Todos estes dados já estão disponíveis para testes na inicialização da aplicação através da migration V4__dados_teste.sql.

## Funcionalidades Implementadas

### História 1 e 2: Agendamentos Básicos
- Sugestão de agendamentos com base em preferências do paciente
- Confirmação de agendamentos
- Recusa de sugestões e solicitação de alternativas

### História 3: Visualização de Agenda por Unidade e Especialidade
- Visualização da agenda médica por unidade e especialidade
- Criação de bloqueios de horários

### História 4: Verificação de Consultas Agendadas
- Consulta de agendamentos ativos por paciente
- Visualização do histórico de consultas
- Listagem de todas as consultas de um paciente

### História 5: Cancelamento de Consulta Agendada
- Cancelamento de consultas com antecedência
- Liberação da vaga para outros pacientes
- Validação do prazo para cancelamento (24 horas antes do atendimento)

## Regras de Negócio Principais

1. **Agendamento de Consultas**:
   - Um paciente não pode ter dois agendamentos no mesmo horário
   - É necessário verificar a disponibilidade do horário escolhido
   - Alguns tipos de agendamento exigem triagem prévia

2. **Cancelamento de Consultas**:
   - Só é possível cancelar consultas com pelo menos 24 horas de antecedência
   - Ao cancelar, o status da consulta é atualizado para "CANCELADA"
   - O horário cancelado volta para a agenda do profissional

3. **Reagendamento**:
   - Não é possível reagendar consultas já canceladas
   - Um reagendamento altera o status da consulta para "REAGENDADA"

4. **Bloqueio de Horários**:
   - Administradores podem bloquear horários específicos
   - Bloqueios podem ser para um profissional específico ou para todos

## Validações Implementadas

O sistema utiliza Bean Validation para validar os dados de entrada:

- **@Valid**: Validação de objetos complexos
- **@NotNull**: Campos obrigatórios
- **@NotBlank**: Campos de texto obrigatórios e não vazios
- **@Size**: Limites de tamanho para campos de texto
- Validações personalizadas para regras de negócio específicas

## Tratamento de Erros

A API implementa tratamento de exceções para fornecer mensagens de erro claras:

- **IllegalArgumentException**: Para dados inválidos (HTTP 400)
- **IllegalStateException**: Para conflitos de estado (HTTP 409)
- **MethodArgumentNotValidException**: Para falhas de validação de dados
- **ConstraintViolationException**: Para violações de restrições de validação

## Cobertura de Testes

O projeto possui uma extensa cobertura de testes:

- **Testes Unitários**: Para serviços e DTOs
- **Testes de Integração**: Para controladores e repositórios
- **Testes de Validação**: Para validação de regras de negócio

Os testes utilizam banco H2 em memória para garantir execução rápida e isolada, e cobrem mais de 80% do código desenvolvido, validando:
- Funcionalidades de agendamento, cancelamento e consulta
- Validações de dados
- Regras de negócio específicas

## Como Executar o Projeto

### Pré-requisitos
- Java 17+ instalado
- Maven 3.6+ instalado
- MySQL instalado e acessível em localhost (porta 3306)

### Configurar banco MySQL
- Acesse o MySQL com um usuário administrativo e execute **apenas** o comando de criação do banco de dados:
  ```sql
  CREATE DATABASE IF NOT EXISTS hackaton CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- As credenciais esperadas pela aplicação (arquivo `src/main/resources/application-mysql.properties`) são:
  - usuário: `root`
  - senha: `root`
- Se quiser usar credenciais diferentes, atualize `src/main/resources/application-mysql.properties` ou forneça variáveis de ambiente apropriadas.

### Aplicar migrações e iniciar a aplicação (perfil mysql)
- As migrações Flyway estão em `src/main/resources/db/migration` (V1, V2, V3, V4, etc.).
- O Flyway executará automaticamente todas essas migrações quando a aplicação for iniciada, incluindo a inserção dos dados de teste.
- Para iniciar a aplicação apontando para o MySQL:
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=mysql -DskipTests
  ```

### Rodar a aplicação em modo desenvolvimento (H2)
- Para usar o perfil `dev` que roda com H2 em memória e aplica as migrações Flyway em memória:
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=dev -DskipTests
  ```

### Executar testes
- Os testes de unidade e integração usam H2 e são executáveis com:
  ```
  mvn test
  ```
- Executar apenas testes específicos:
  ```
  mvn -Dtest=AppointmentIntegrationTest test
  mvn -Dtest=ConcurrentBookingTest test
  ```

## Postman

A coleção Postman foi significativamente expandida e agora oferece uma cobertura abrangente de testes para a API:

### Arquivo de Importação
- **Coleção pronta para importação**: `postman/fiap-hackaton.postman_collection.json`
- **URL base**: `http://localhost:8080`
- **Importe no Postman** e execute as requisições organizadas por cenários

### Estrutura da Coleção

#### 1. **Configuração Inicial**
- Reset de dados administrativo
- Configuração do ambiente de testes

#### 2. **História 1 e 2 - Agendamentos Básicos**
- Sugestão de agendamentos válidos
- Recusa de sugestões e solicitação de próximas opções
- Confirmação de agendamentos
- Testes de conflitos de horários
- Validações de dados obrigatórios

#### 3. **História 3 - Visualizar Agenda por Unidade e Especialidade**
- Visualização de agendas por especialidade
- Criação de bloqueios de horários
- Validações de unidades e dados obrigatórios

#### 4. **Exames com Triagem**
- Sugestão de exames com triagem urgente
- Exames pré-operatórios
- Confirmação de exames com triagem
- Reagendamento de exames
- Validações específicas para triagem

#### 5. **História 4 - Verificar Consulta Agendada**
- Consultas ativas por paciente
- Histórico de consultas
- Listagem completa de consultas
- Testes para múltiplos pacientes

#### 6. **História 5 - Cancelar Consulta Agendada**
- Cancelamentos válidos
- Validação de prazos (24h de antecedência)
- Testes de agendamentos inexistentes
- Validação de pacientes incorretos
- Testes de dados obrigatórios

#### 7. **Administração**
- Histórico de migrações Flyway
- Verificação dos dados de teste

#### 8. **Cenários Avançados - Múltiplas Especialidades**
- **Cardiologia**: Consultas cardíacas com sintomas específicos
- **Ortopedia**: Consultas ortopédicas para problemas musculares
- **Pediatria**: Atendimento infantil com urgências
- **Ginecologia**: Exames preventivos e consultas especializadas
- Confirmação de agendamentos para cada especialidade

#### 9. **Testes de Carga e Volume**
- Múltiplas sugestões sequenciais para diferentes pacientes
- Simulação de agendas lotadas
- Testes de concorrência de horários
- Validação de capacidade do sistema

#### 10. **Cenários de Validação e Erro**
- **Validação de urgência**: Testes com valores inválidos (0, 6+)
- **Tipos inexistentes**: Especialidades não cadastradas
- **Pacientes inexistentes**: Validação de IDs inválidos
- **Localizações inexistentes**: Testes de regiões não atendidas
- **JSON malformado**: Testes de formato inválido

#### 11. **Cenários de Diferentes Níveis de Urgência**
- **Urgência 1**: Consultas de rotina e exames anuais
- **Urgência 2**: Acompanhamentos pós-tratamento
- **Urgência 3**: Sintomas moderados (febre persistente)
- **Urgência 4**: Sintomas graves (dor no peito)
- **Urgência 5**: Emergências (sangramento ativo)

#### 12. **Testes de Diferentes Unidades**
- **Unidade u1**: Clínica Central (Centro)
- **Unidade u2**: Hospital Zona Sul
- **Unidade u3**: Posto de Saúde Norte
- Visualização de agendas específicas por unidade
- Confirmação de agendamentos em diferentes locais

#### 13. **Testes de Consultas para Todos os Pacientes**
- Consultas ativas, históricas e completas para cada paciente
- Validação com pacientes inexistentes
- Cobertura completa dos 3 pacientes de teste

### Funcionalidades da Coleção

#### **Variáveis Dinâmicas**
A coleção utiliza variáveis para gerenciar dinamicamente:
- IDs de agendamentos criados
- Dados de unidades e profissionais
- Horários sugeridos pelo sistema
- IDs específicos por especialidade

#### **Testes Automatizados**
Cada requisição inclui:
- **Validação de status HTTP** apropriado
- **Verificação de resposta** para dados essenciais
- **Captura de variáveis** para uso em requisições subsequentes
- **Testes de conteúdo** específicos do domínio

#### **Cenários Realísticos**
- **Sintomas específicos** para cada especialidade
- **Dados demográficos** variados (diferentes idades, regiões)
- **Níveis de urgência** contextualmente apropriados
- **Fluxos completos** de agendamento a cancelamento

### Como Usar a Coleção

1. **Importe** o arquivo `postman/fiap-hackaton.postman_collection.json` no Postman
2. **Inicie a aplicação** com `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
3. **Execute "Admin - Resetar Dados"** se necessário
4. **Execute as requisições** na ordem sugerida ou conforme necessário
5. **Observe as variáveis** sendo populadas automaticamente

### Notas Importantes

- **Dados de teste automáticos**: Não é necessário executar requisições para criar dados, pois eles são inseridos automaticamente pelo Flyway na inicialização
- **Ordem de execução**: Algumas requisições dependem de variáveis definidas por requisições anteriores
- **Reset de dados**: Use a requisição de reset quando necessário para limpar o estado
- **Cobertura completa**: A coleção cobre todos os endpoints e cenários principais da API

## Observações Importantes
- A configuração de produção não deve manter credenciais em texto; utilize variáveis de ambiente ou um secret manager.
- Em perfil `mysql` o Flyway gerencia o schema (propriedade `spring.jpa.hibernate.ddl-auto=none`).
- Se seu MySQL estiver em outra porta (não 3306), ajuste `src/main/resources/application-mysql.properties` ou use variáveis de ambiente.
- Recomendado: criar um usuário específico para a aplicação em vez de usar `root`.

Pronto. Siga os passos acima para rodar a aplicação localmente sem Docker.

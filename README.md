# FIAP Hackaton - Medical Appointments API

Sistema de Agendamento de Consultas Médicas desenvolvido para a 5ª fase do FIAP - Hackathon.

## Sobre o Projeto

O projeto **fiap-hackaton** é uma API REST desenvolvida para o gerenciamento de agendamentos de consultas médicas e exames. O sistema permite que pacientes agendem, consultem, cancelem e reagendem suas consultas, além de fornecer recursos administrativos para gerenciamento da agenda médica.

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação base
- **Spring Boot 3.5.5**: Framework para desenvolvimento da API REST
- **Spring Data JPA**: Para persistência de dados
- **Spring Security**: Para autenticação e autorização
- **Spring Validation**: Para validação de dados de entrada
- **Flyway**: Para gerenciamento e versionamento de migrações do banco de dados
- **MySQL**: Banco de dados relacional para desenvolvimento e produção
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

## Como Executar

1. **Clonar o repositório**
   ```bash
   git clone <repository-url>
   cd fiap-hackaton
   ```

2. **Configurar o banco de dados MySQL**
- Não é necessário criar manualmente o schema: a aplicação cria o schema `hackaton` automaticamente ao inicializar (a URL de conexão em `src/main/resources/application.properties` já contém `createDatabaseIfNotExist=true`).
- Configure as credenciais no arquivo `src/main/resources/application.properties` (padrão: root / root) se necessário.
- As migrações do Flyway em `src/main/resources/db/migration` serão aplicadas automaticamente na inicialização.

3. **Executar a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar a aplicação**
   - URL base: `http://localhost:8080`

## Test Data (Massa de Dados)

A aplicação utiliza o Flyway para gerar automaticamente dados de teste quando iniciada. **Não é necessário fazer requisições para inserir test data**. Os dados de teste incluem:

### Pacientes (Patients)
- **p1**: João Silva - General patient for basic consultations
- **p2**: Maria Oliveira - Patient with multiple appointments
- **p3**: Carlos Santos - Patient for conflict testing
- **p4**: Ana Costa - Patient for urgent triage cases
- **p5**: Pedro Lima - Patient for pre-operative procedures

### Unidades (Medical Units)
- **u1**: Hospital Central - Main hospital unit (Centro)
- **u2**: Clínica Norte - Laboratory for blood tests (Zona Norte)
- **u3**: UBS Sul - Basic health unit (Zona Sul)

### Profissionais (Healthcare Professionals)
- **prof1**: Dr. Silva - General practitioner
- **prof2**: Dr. Santos - Laboratory technician
- **prof3**: Dra. Lima - Specialist

### Tipos de Atendimento (Service Types)
- **CLINICO_GERAL**: General practice consultations
- **EXAME_SANGUE**: Blood tests
- **CARDIOLOGIA**: Cardiology consultations
- **DERMATOLOGIA**: Dermatology consultations

## Coleção do Postman (Postman Collection)

O projeto inclui uma coleção completa do Postman para testes da API localizada em:

```
postman/fiap-hackaton.postman_collection.json
```

### Como Importar no Postman

1. Abra o Postman
2. Clique em "Import"
3. Selecione o arquivo `postman/fiap-hackaton.postman_collection.json`
4. A coleção será importada com todas as requisições organizadas por funcionalidade

### Estrutura da Coleção

A coleção está organizada nas seguintes pastas:

1. **Initial Setup**
   - Admin - Reset Data: Redefine dados do sistema (requer autenticação)

2. **Story 1 & 2 - Basic Appointments**
   - Suggest Appointment - Valid Request
   - Reject Suggestion - Request Next
   - Confirm Appointment - Valid
   - Confirm Appointment - Conflict (Same Time)
   - Suggest Appointment - Missing Patient ID (Validation)

3. **Story 3 - View Schedule by Unit and Specialty**
   - View Schedule - General Practice
   - View Schedule - Blood Test
   - Create Block - Professional on Vacation

4. **Tests with Triage**
   - Suggest Test with Triage - Urgent
   - Suggest Pre-operative Test

5. **Patient Consultation History**
   - Get Active Appointments by Patient
   - Get Patient Medical History

6. **Appointment Management**
   - Cancel Appointment
   - Reschedule Appointment

### Variáveis da Coleção

A coleção utiliza as seguintes variáveis:
- `base_url`: URL base da API (padrão: http://localhost:8080)
- `unit_id`, `professional_id`, `date_time`: Valores obtidos dinamicamente
- `appointment_id`, `exam_id`: IDs gerados durante os testes

## Principais Endpoints da API

### Agendamentos
- `POST /api/agendamentos/sugerir` - Solicitar sugestão de agendamento
- `POST /api/agendamentos/confirmar` - Confirmar agendamento
- `POST /api/agendamentos/recusar` - Recusar sugestão e solicitar próxima
- `POST /api/agendamentos/cancelar` - Cancelar agendamento
- `POST /api/agendamentos/reagendar` - Reagendar consulta

### Agenda
- `GET /api/agenda/visualizar` - Visualizar agenda por unidade e especialidade
- `POST /api/agenda/bloquear` - Criar bloqueio de horário

### Consultas de Pacientes
- `GET /api/consultas/paciente/{id}/agendamentos` - Agendamentos ativos do paciente
- `GET /api/consultas/paciente/{id}/historico` - Histórico médico do paciente

### Administração
- `POST /admin/reset` - Resetar dados do sistema (Basic Auth: admin/admin123)

## Testes

Para executar os testes:

```bash
./mvnw test
```

Os testes incluem:
- Testes unitários de serviços
- Testes de integração de controllers
- Testes de repositório com @DataJpaTest

## Tradução de Termos

**"Massa de dados"** em inglês americano é **"Test Data"** ou **"Sample Data"**. Outros termos equivalentes:
- **Mock Data**: Dados simulados
- **Seed Data**: Dados iniciais
- **Test Dataset**: Conjunto de dados de teste
- **Sample Dataset**: Conjunto de dados de exemplo

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/fiap/hackaton/
│   │   ├── controller/     # Controllers REST
│   │   ├── service/        # Lógica de negócio
│   │   ├── repository/     # Acesso a dados
│   │   ├── entity/         # Entidades JPA
│   │   ├── dto/           # Data Transfer Objects
│   │   └── enums/         # Enumerações
│   └── resources/
│       ├── db/migration/   # Scripts Flyway
│       └── application.properties
└── test/                   # Testes unitários e integração

postman/
└── fiap-hackaton.postman_collection.json  # Coleção Postman
```

## Resolução de Problemas de Testes

Se houver falhas nos testes relacionadas ao ApplicationContext, verifique:

1. **Configuração do banco**: Certifique que o MySQL está configurado corretamente e que o arquivo `src/main/resources/application.properties` aponta para o schema correto (`hackaton`).
2. **Perfis ativos**: O projeto foi consolidado para usar um único profile; remova perfis adicionais ou garanta que não sobrescrevam as configurações de conexão.
3. **Dependências**: Execute `./mvnw clean install` para atualizar dependências
4. **Portas**: Certifique-se que a porta 8080 não está em uso

## Merge para Branch Developer

Para fazer merge das alterações na branch developer:

```bash
# Certifique-se de estar na branch fix/json-readme-update
git add .
git commit -m "Fix: Update Postman collection format and README documentation"

# Mude para a branch developer
git checkout developer

# Faça o merge
git merge fix/json-readme-update

# Push das alterações
git push origin developer
```

## Contribuição

1. Faça um fork do projeto
2. Crie uma feature branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

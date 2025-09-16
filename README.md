# fiap-hackaton
Development of the 5th phase of FIAP - Hackathon

Instruções para rodar localmente (MySQL + aplicação + testes)

1) Pré-requisitos
- Java 17+ instalado
- Maven 3.6+ instalado
- MySQL instalado e acessível em localhost (porta 3306)

2) Configurar banco MySQL
- Acesse o MySQL com um usuário administrativo e execute:
  CREATE DATABASE IF NOT EXISTS hackaton CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
- As credenciais esperadas pela aplicação (arquivo `src/main/resources/application-mysql.properties`) são:
  - usuário: `root`
  - senha: `root`
- Se quiser usar credenciais diferentes, atualize `src/main/resources/application-mysql.properties` ou forneça variáveis de ambiente apropriadas.

3) Aplicar migrações e iniciar a aplicação (perfil mysql)
- As migrações Flyway estão em `src/main/resources/db/migration` (arquivo inicial `V1__init.sql`).
- Para iniciar a aplicação apontando para o MySQL e aplicar as migrações automaticamente:
  mvn spring-boot:run -Dspring-boot.run.profiles=mysql -DskipTests

4) Rodar a aplicação em modo desenvolvimento (H2, útil para desenvolver e testes locais rápidos)
- Para usar o perfil `dev` que roda com H2 em memória e aplica as migrações Flyway em memória:
  mvn spring-boot:run -Dspring-boot.run.profiles=dev -DskipTests

5) Executar testes
- Os testes de unidade e integração usam H2 e são executáveis com:
  mvn test
- Executar apenas testes específicos:
  mvn -Dtest=AppointmentIntegrationTest test
  mvn -Dtest=ConcurrentBookingTest test

6) Postman
- Coleção pronta para importação: `postman/fiap-hackaton.postman_collection.json`.
- Importe no Postman e execute as requisições contra `http://localhost:8080`.

7) Observações importantes
- A configuração de produção não deve manter credenciais em texto; utilize variáveis de ambiente ou um secret manager.
- Em perfil `mysql` o Flyway gerencia o schema (propriedade `spring.jpa.hibernate.ddl-auto=none`).
- Se seu MySQL estiver em outra porta (não 3306), ajuste `src/main/resources/application-mysql.properties` ou use variáveis de ambiente.
- Recomendado: criar um usuário específico para a aplicação em vez de usar `root`.

Pronto. Siga os passos acima para rodar a aplicação localmente sem Docker.

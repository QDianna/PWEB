# MobyLabWebProgramming

This is an example application to teach students the basics of web programming.

To start working with the backend install docker and docker compose from https://docs.docker.com/engine/install/ and enter the command below to launch the Postgresql database while in the Deployment folder:

```shell showLineNumbers
docker-compose -f .\docker-compose.yml -p mobylab-app-db up -d
```

You can use PGAdmin (https://www.pgadmin.org/) or DBeaver (https://dbeaver.io/download/) to access the database on localhost:5432 with database/user/password "postgres".

In order to run the application you need to have maven installed (https://maven.apache.org/install.html) and run the following commands:

```shell showLineNumbers
mvn clean install
mvn spring-boot:run
```
# Define a imagem base
FROM openjdk:17-jdk-slim

# Instalação do Maven
RUN apt-get update && apt-get install -y maven

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos do projeto para o contêiner
COPY . /app

# Define o comando a ser executado quando o contêiner for iniciado
CMD ["mvn", "spring-boot:run"]

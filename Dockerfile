FROM adoptopenjdk:11-jre-hotspot
COPY *.jar /rvc-website.jar


ARG SERVER_PORT
ARG ACTIVE
ARG UNIQUE_ID

ARG MYSQL_ADDR
ARG MYSQL_USERNAME
ARG MYSQL_PASSWORD

ARG REDIS_ADDR
ARG REDIS_PORT
ARG REDIS_PWD

ARG NACOS_SERVER_ADDR
ARG NACOS_SERVER_USER
ARG NACOS_SERVER_PASSWORD

ARG MQ_ADDR
ARG MQ_PORT
ARG MQ_USERNAME
ARG MQ_PWD

ENV SERVER_PORT=${SERVER_PORT}
ENV ACTIVE=${ACTIVE}
ENV UNIQUE_ID=${UNIQUE_ID}
ENV JASYPT_PWD=${JASYPT_PWD}



#ENV MYSQL_ADDR=${MYSQL_ADDR}
#ENV MYSQL_USERNAME=${MYSQL_USERNAME}
#ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}
#
#ENV REDIS_ADDR=${REDIS_ADDR}
#ENV REDIS_PORT=${REDIS_PORT}
#ENV REDIS_PWD=${REDIS_PWD}
#
#ENV NACOS_SERVER_ADDR=${NACOS_SERVER_ADDR}
#ENV NACOS_SERVER_USER=${NACOS_SERVER_USER}
#ENV NACOS_SERVER_PASSWORD=${NACOS_SERVER_PASSWORD}

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java","-jar","/rvc-website.jar"]

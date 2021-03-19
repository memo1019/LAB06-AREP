FROM openjdk:8

WORKDIR /usrapp/bin

ENV PORT 8000

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency
RUN mkdir keyscerts
COPY keyscerts/* /usrapp/bin/keyscerts/

CMD ["java","-cp","./classes:./dependency/*","edu.escuelaing.co.demosecurityservice.service"]

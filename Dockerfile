FROM openjdk:8-jre

ADD target/redispoc-0.0.1.jar redispoc-0.0.1.jar

ENTRYPOINT ["java","-jar","redispoc-0.0.1.jar"]

EXPOSE 8085

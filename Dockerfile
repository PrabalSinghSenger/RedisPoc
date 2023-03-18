FROM registry.tools.3stripes.net/pe-baseimages/openjdk:8-jre

ADD target/delivery-promise-engine-0.0.1.jar delivery-promise-engine-0.0.1.jar

ENTRYPOINT ["java","-jar","delivery-promise-engine-0.0.1.jar"]

EXPOSE 8085

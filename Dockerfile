FROM arm32v7/eclipse-temurin:11

COPY . .

RUN ./gradlew build -x test

ARG GOOGLE_API_KEY_ARG
ENV GOOGLE_API_KEY=$GOOGLE_API_KEY_ARG

ARG GOOGLE_API_CX_ARG
ENV GOOGLE_API_CX=$GOOGLE_API_CX_ARG

ENTRYPOINT java -Dgoogle.api.key=$(echo ${GOOGLE_API_KEY}) -Dgoogle.api.cx=$(echo ${GOOGLE_API_CX}) -jar build/libs/seo-serps-*-SNAPSHOT.jar

EXPOSE 8080
# YANDEX

## Описание

Проект был написан на Java 8 c использованием сборщика Maven. В проекте использованы фреймворки: Spring Boot, JOOQ, Lombok.            
Для хранения всех записей использована лекговесная база данных SQLITE (файл - *identifier.sqlite*).                
Бд уже приготовлена, в ней созданы соответсвующие таблицы. Схема бд описана в файле *init.sql*.                  
Параметр, отвечающий, сколько символов поля content нужно возвращать при поиске, называется *yandex.content.cut.limit* и содержится в файле *src/main/resources/application.yaml*.             
Порт приложения - *8080*.
Поддреживается Swagger/OpenAPI по адресу http://localhost:8080/swagger-ui.html
## Как запускать

0. Должны быть установлены Java 8 и Maven 3
1. mvn clean install в корне проекта
2. java -jar target/rest-0.0.1-SNAPSHOT.jar




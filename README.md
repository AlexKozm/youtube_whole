# youtube_whole

## To run
* systemctl start docker # to run docker
* docker-compose up mariadb1 rabbitmq # to run database and rabbitmq
* ./gradlew dockerBuild # to build files for docker
* docker-compose up youtube3 youtube_transcoder --build # to build images and run microservices 

## TODO
* Use rabbitmq from micronaut https://micronaut-projects.github.io/micronaut-rabbitmq/latest/guide/
* Handle microservices or rabbit crashes https://habr.com/ru/company/lamoda/blog/678932/
* Handle exceptions

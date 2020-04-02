name=spring-boot-api-project-seed
version=latest
mvn clean compile package -Dmaven.test.skip=true && docker build -t ${name} . && docker tag ${name}:${version} pingxin/${name}:${version}
#docker push pingxin/${name}:${version}
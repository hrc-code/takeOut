# 指定基础镜像
FROM maven:3.8.1-jdk-8-slim as builder

#标签
LABEL authors="hrc"

# 设置代码存储目录
WORKDIR /app
COPY pom.xml .
COPY src ./src

#声明容器运行时监听的端口
EXPOSE 8080

# 镜像构建时执行的命令
RUN mvn  clean  package -DskipTests  -Puse-aliyun-maven

# 容器启动时执行的命令
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","/app/target/takeOut-1.0-SNAPSHOT.jar"]
#CMD
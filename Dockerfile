# 指定基础镜像
FROM openjdk:8-jdk-alpine as builder

#标签
LABEL authors="hrc"

# 设置代码存储目录
WORKDIR /app
COPY jar ./jar

#声明容器运行时监听的端口
EXPOSE 8080

# 容器启动时执行的命令
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","/app/jar/takeOut-1.0-SNAPSHOT.jar"]
#CMD
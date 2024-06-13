# takeOut

#### 介绍
写一个业务项目，锻炼自己的业务能力
 
####  表

https://www.yuque.com/hrcblog/mg9973/hh0r8s8agqceonmn?singleDoc# 《表》

#### 部署
> docker
1. 构建镜像
```docker
docker build -t takeout:latest .
```
或者使用我构建好的镜像
```docker
docker pull registry.cn-guangzhou.aliyuncs.com/huthrc/test
```
2. 运行容器
```docker
docker run -d -p 8080:8080  --name my-takeout takeout:latest
```

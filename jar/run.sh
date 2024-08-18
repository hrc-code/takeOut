## 杀死java进程
jps | grep 'jar' | awk '{system("kill -9 " $1)}'

## 后台运行，输出到 log文件
nohup java -Dspring.profiles.active=prod -Dserver.port=8080 -jar takeOut-1.0-SNAPSHOT.jar > nohup.log 2>&1 &
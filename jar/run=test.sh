## 杀死java进程
jps | grep 'jar' | awk '{system("kill -9 " $1)}'
## 开启项目 后台运行，关闭输出
java -Dspring.profiles.active=prod -Dserver.port=81  -jar takeOut-1.0-SNAPSHOT.jar &

## 后台运行，输出到 log文件
nohup java -Dspring.profiles.active=prod -Dserver.port=81 -jar takeOut-1.0-SNAPSHOT.jar > log 2>&1 &

## 查看日志 前台一直监控
tail -f log

SET ACTIVE="-Dspring.profiles.active=prod"
SET PORT="-Dserver.port=8080"

java  %ACTIVE%  %PORT% -jar ../jar/takeOut-1.0-SNAPSHOT.jar
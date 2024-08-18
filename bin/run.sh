
ACTIVE="-Dspring.profiles.active=prod"

PORT="-Dserver.port=8080"

java  $ACTIVE  $PORT -jar ../jar/takeOut-1.0-SNAPSHOT.jar
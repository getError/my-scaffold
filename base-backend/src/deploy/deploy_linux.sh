if ! command -v mvn > /dev/null 2>&1; then
  echo 'mvn 不存在'
  exit
fi
if ! command -v java > /dev/null 2>&1; then
  echo 'java 不存在'
  exit
fi

mvn package -f ../../pom.xml
java -jar ../../target/base-backend-1.0-SNAPSHOT.jar

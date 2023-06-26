echo "Building Supportly Backend"
./gradlew clean build dokkaHtml

echo "Building Supportly Backend Docker Image"
docker build --platform linux/amd64 -t jerrybraun17/supportly-backend .

echo "Pushing Supportly Backend Docker Image"
docker push jerrybraun17/supportly-backend
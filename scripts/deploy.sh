CI_BUILD_REF_NAME=$1
echo "deploying on" $CI_BUILD_REF_NAME

# 打包主工程
echo 'install backend...'
mvn clean
if [[ $? -ne '0' ]]; then echo 'build failed'; exit 1; fi;

mvn package -B -Dmaven.test.skip=true;
if [[ $? -ne '0' ]]; then echo 'build failed'; exit 1; fi;

if [[ $CI_BUILD_REF_NAME =~ dev ]]; then env=dev;
elif [[ $CI_BUILD_REF_NAME =~ master ]]; then env=dev;
else exit 1; fi;

# 重新 build 并启动主工程容器
echo 'docker stop-build-run...'
sudo docker-compose rm -f
sudo docker-compose down --rmi all
sudo docker-compose up -d --env-file config/deploy-${env}.env

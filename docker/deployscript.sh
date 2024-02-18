cd /opt/edox-authorization
cd ./images
docker load < authorization-server.module-rest-authorization-endpoint.tar
cd ./..
docker  compose -f edox-authorization.yaml -p edox-authorization up -d
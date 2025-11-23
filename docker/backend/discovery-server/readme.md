1. Build Product Service Image
   docker build -t rupesh1997/discovery-server:1.0.0 \
   -f ../docker/backend/discovery-server/Dockerfile .

2. Docker Network Commands
   - List networks
     docker network ls

   - Create network
     docker network create purely-goods-nw --driver bridge

   - Inspect a network
     docker network inspect <network_id_or_name>
     E.g. docker network inspect purely-goods-nw

3. Run Product Service Container
   docker run -d -p 8761:8761 --name discovery-server --network=purely-goods-nw -e SPRING_APPLICATION_NAME=discovery-server rupesh1997/discovery-server:1.0.0



1. Build Product Service Image
   docker build -t rupesh1997/config-server:1.0.0 \
   -f ../docker/backend/config-server/Dockerfile .

2. Docker Network Commands
   - List networks
     docker network ls

   - Create network
     docker network create purely-goods-nw --driver bridge

   - Inspect a network
     docker network inspect <network_id_or_name>
     E.g. docker network inspect purely-goods-nw

3. Run Product Service Container
   docker run -d -p 8071:8071 \
   --name config-server \
   --network=purely-goods-nw \
   rupesh1997/config-server:1.0.0



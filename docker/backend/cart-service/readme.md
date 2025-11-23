1. Build Product Service Image
   docker build -t rupesh1997/gateway-server:1.0.0 -f ../docker/backend/gateway-server/Dockerfile .

2. Run Product Service Container
   docker run -d -p 8083:8083 --name gateway-server --network=purely-goods-nw rupesh1997/gateway-server:1.0.0

3. Docker Network Commands
    - List networks
      docker network ls

    - Create network
      docker network create purely-goods-nw --driver bridge

    - Inspect a network
      docker network inspect <network_id_or_name>
      E.g. docker network inspect purely-goods-nw

4. Check Product Service Health Endpoint
    - http://localhost:8072/actuator/health

   
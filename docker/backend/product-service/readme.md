1. Build Product Service Image
   docker build -t rupesh1997/product-service:1.0.0 \
   -f ../docker/backend/product-service/Dockerfile .

2. Run Product Service Container
   docker run -d -p 8081:8081 \
   --name product-service \
   --network=purely-goods-nw \
   -e SPRING_DATASOURCE_URL=jdbc:mysql://product-db:3306/products \
   -e SPRING_DATASOURCE_USERNAME=root \
   -e SPRING_DATASOURCE_PASSWORD=root \
   rupesh1997/product-service:1.0.0

3. Run MySQL Container
   docker run -d \
   --name product-db \
   --network=purely-goods-nw \
   -p 3306:3306 \
   -v mysql-data:/var/lib/mysql \
   -e MYSQL_ROOT_PASSWORD=root \
   -e MYSQL_DATABASE=products \
   mysql:8.0

   OR
   ---
   docker run -d \
   --name product-db \
   -p 3306:3306 \
   -v mysql-data:/var/lib/mysql \
   -e MYSQL_ROOT_PASSWORD=root \
   -e MYSQL_DATABASE=products \
   mysql:8.0

4. Enter MySQL Container
    - Use bash if available:
      docker exec -it mysql bash
    - Or fallback to sh:
      docker exec -it mysql sh
    - Login to MySQL:
      mysql -u root -p

5. Docker Network Commands
    - List networks
      docker network ls

    - Create network
      docker network create purely-goods-nw --driver bridge

    - Inspect a network
      docker network inspect <network_id_or_name>
      E.g. docker network inspect purely-goods-nw

6. Docker Volume Commands
    - docker volume ls
    - docker volume inspect mysql-data
    - docker volume rm mysql-data
    - docker create volume mysql-data

7. Check Product Service Health Endpoint
    - http://localhost:8081/api/v1.0.0/actuator/health


---
docker run -d -p 8081:8081 \
--name product-service \
rupesh1997/product-service:1.0.0
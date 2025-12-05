# docker system prune -f
# docker compose -f docker/docker-compose/default/docker-compose.yaml down
# docker compose -f docker/docker-compose/default/docker-compose.yaml up -d
# docker logs <container-id>
# cat ~/.ssh/id_rsa

# docker exec -it product-service sh -c "printenv | grep EUREKA"
# EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-server:8070/eureka/
# http://localhost:8070/eureka/apps
# docker exec -it order-service sh -c "curl -s http://localhost:8082/actuator/eureka | grep registry"
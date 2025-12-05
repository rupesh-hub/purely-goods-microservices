FROM node:20 AS build
WORKDIR /app

ARG CONFIGURATION=production

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build -- --configuration $CONFIGURATION

FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 8080

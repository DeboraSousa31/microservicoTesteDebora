# # Etapa 1 -- BUILD
# FROM node:24.0.2 AS builder

# WORKDIR /app

# COPY package*.json ./
# RUN npm install

# COPY . .
# RUN npm run build

# # Copia o .env com as variáveis VITE_
# COPY .env .env

# # Aceita o argumento passado pelo docker-compose
# ARG VITE_API_BASE_URL

# # Cria o .env com o valor recebido
# RUN echo "VITE_API_BASE_URL=$VITE_API_BASE_URL" > .env

# # Etapa 2 -- SERVIDOR WEB (NGINX)
# FROM nginx:stable-alpine

# RUN rm -rf /usr/share/nginx/html/*

# COPY --from=builder /app/dist /usr/share/nginx/html

# EXPOSE 80

# CMD ["nginx", "-g", "daemon off;"]


FROM node:24.0.2

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

EXPOSE 4181

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
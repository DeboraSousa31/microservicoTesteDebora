FROM node:24.0.2

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

EXPOSE 5174

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
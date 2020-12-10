FROM golang:latest

WORKDIR api
COPY . .
RUN go build -o api ./src
CMD ./api

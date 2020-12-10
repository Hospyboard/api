FROM golang:latest

WORKDIR api
COPY . .
RUN go mod vendor -v
RUN go build -o api ./src
CMD ./api

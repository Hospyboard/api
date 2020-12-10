package main

import (
	"fmt"
	"github.com/siddontang/go-mysql/client"
	"log"
	"os"
)

func main() {
	_, err := client.Connect("mysql:3306", os.Getenv("MYSQL_USER"),
		os.Getenv("MYSQL_PASSWORD"), os.Getenv("MYSQL_DATABASE"))
	if err != nil {
		log.Fatalf("Unable to connect to mysql database: %v\n", err.Error())
	}
	fmt.Println("Successfully connected to the database")
}

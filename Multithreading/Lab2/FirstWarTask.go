package main

import (
	"math/rand"
	"fmt"
	"time"
)

func main() {
	storage := make(chan int)
	channel := make(chan int)

	go generate(storage)
	go getFrom(storage, 20, channel)
	go putIntoCar(channel)

	var intput string
	fmt.Scan(&intput)
}

func generate(storage chan int)  {
	for i := 0; i < 20 ; i++ {
		storage <- rand.Intn(100)
	}
}


func getFrom (storage chan int, capacity int, c chan int) {
	for capacity > 0 {
		capacity--
		val := <- storage
		c <- val
		amt := time.Duration(rand.Intn(1000))
		time.Sleep(time.Millisecond * amt)
	}
	c <- -1
}

func putIntoCar(c chan int) {
	for true {
		value := <- c
		if value == -1 {
			return
		}
		fmt.Println(value)
		amt := time.Duration(rand.Intn(1000))
		time.Sleep(time.Millisecond * amt)
	}
}
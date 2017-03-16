package main

import (
	"time"
	"math/rand"
	"fmt"
)


func main()  {
	channelToHairdresser := make(chan int)
	channelToClient := make(chan bool)
	go hairdresser(channelToHairdresser, channelToClient)
	for true {
		time.Sleep(time.Millisecond * time.Duration(rand.Intn(10000)))
		go client(channelToHairdresser, channelToClient)
	}
}

func client( c1 chan int, c2 chan bool)  {
	//time.Sleep(time.Millisecond * time.Duration(rand.Intn(1000) * difficulty))
	c1 <- rand.Intn(10)
	res := <- c2
	for res != true {
		c2 <- res
	}
}

func hairdresser(c1 chan int, c2 chan bool)  {
	for true {
		fmt.Print("Hairdresser is falling asleep...\n")
		res := <- c1
		fmt.Print("Hairdresser has been awoken!\n")
		time.Sleep(time.Millisecond * time.Duration(1000))
		counts := 1000 * res / 80
		//fmt.Println(counts)
		for i := 0; i < counts; i++{
			time.Sleep(time.Millisecond * time.Duration(50))
			fmt.Print(".")
		}
		c2 <- true
		fmt.Println()
	}

}

package main

import (
	"fmt"
	"time"
)

func main() {
	channel := make(chan []bool)
	go bear(channel)
	for i := 0; i < 10; i++ {
		go bee(channel, make([]bool, 0))
	}

	var input string
	fmt.Scanln(&input)
}

func bear(channel chan []bool)  {
	for true {
		fmt.Println("Bees are working...")
		jug := <- channel
		fmt.Println("Bear has eaten ", len(jug), " honey!\n" )
		channel <- make([]bool, 0)
	}
}

func bee(channel chan []bool, jug []bool)  {
	for true {
		if len(jug) >= 10 {
			channel <- jug
			jug = <- channel
		} else {
			jug = append(jug, true)
		}

		time.Sleep(time.Second)
	}
}

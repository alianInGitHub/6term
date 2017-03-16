package main

import (
	"math/rand"
	"fmt"
	"time"
)

const tobacco =  0
const paper  =  1
const match  =  2

func main() {
	var smokerChannels [3]chan string
	for i := 0; i < 3; i++ {
		smokerChannels[i]= make(chan string)
	}

	barmanToSmokerChanel := make(chan [2]string)

	go smoker("tobacco", smokerChannels[tobacco], barmanToSmokerChanel)
	go smoker("paper", smokerChannels[paper], barmanToSmokerChanel)
	go smoker("match", smokerChannels[match], barmanToSmokerChanel)

	go barman(barmanToSmokerChanel, smokerChannels)

	var input string
	fmt.Scanln(&input)
}

func barman(barmanToSmokerChanel chan [2]string, smokerChannels [3]chan string) {

	for true {

		firstChosenSmoker := rand.Intn(3)
		secondChosenSmoker := rand.Intn(3)
		for secondChosenSmoker == firstChosenSmoker {
			secondChosenSmoker = rand.Intn(3)
		}

		firstComponent := <- smokerChannels[firstChosenSmoker]
		secondComponent := <- smokerChannels[secondChosenSmoker]

		array :=[2]string{firstComponent, secondComponent}
		barmanToSmokerChanel <- array

		val := <- barmanToSmokerChanel
		for val[0] != "Done" {
			for i := 0; i < 3; i++ {
				if (i != firstChosenSmoker) && (i != secondChosenSmoker) {
					<- smokerChannels[i]
				}
			}
			barmanToSmokerChanel <- val

			time.Sleep(time.Millisecond * time.Duration(100))
			val = <- barmanToSmokerChanel
		}
		time.Sleep(time.Second * 2)
	}
}

func smoker(nameOfComponent string, channel chan string, barmanToSmokerChanel chan [2]string) {

	for true {
		channel <- nameOfComponent
		newComponent := <- barmanToSmokerChanel

		if (newComponent[0] != nameOfComponent) && (newComponent[1] != nameOfComponent) {
			fmt.Println("Smoker with " + nameOfComponent + " has done a cigarette")
			barmanToSmokerChanel <- [2]string {"Done", ""}
		} else {
			barmanToSmokerChanel <- newComponent
		}
		time.Sleep(time.Second * 2)
	}

}

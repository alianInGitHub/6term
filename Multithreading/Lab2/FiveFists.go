package main

import (
	"fmt"
	"bufio"
	"os"
	"strconv"
	"log"
	"math/rand"
)

func main() {
	fmt.Print("Enter amount of buddists >>\t")
	reader := bufio.NewReader(os.Stdin)
	s, err := reader.ReadString('\n')
	s = s[:len(s) - 1]
	if err != nil {
		print(err)
	}
	amount, err := strconv.Atoi(s)
	if err != nil {
		log.Fatal(err)
	}

	buddistTsiCapasityList := make([]int, amount)
	for i := 0; i < amount; i++ {
		buddistTsiCapasityList[i] = rand.Intn(1000)
	}

	

}



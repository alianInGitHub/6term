package main

import (
	"math/rand"
	"time"
	"fmt"
	"sync"
	"bufio"
	"os"
)

func main() {

	quit := make(chan bool)
	channelCommander := make(chan bool)

	N := 5
	mutex := &sync.RWMutex{}
	garden := make([][]int, N, N)
	initialize(N, garden)

	go gardener(N, garden, mutex)
	go nature(N, garden, mutex)
	go harvestKeeper(N, garden, mutex)
	go commander(channelCommander, quit)
	go showStatus(N, garden, mutex, channelCommander)

	<- quit
}

func initialize(N int, garden [][]int)  {
	for i := 0; i < N; i++ {
		garden[i] = make([]int, N, N)
		for j := 0; j < N; j++ {
			garden[i][j] = rand.Intn(5)
		}
	}
}

func commander(channel chan bool, quit chan bool)  {
	 for {
		 var str string
		 reader := bufio.NewReader(os.Stdin)
		 str,_ = reader.ReadString('\n')
		 //fmt.Println(str)
		 if str == "e" {
			 quit <- true
		 } else {
			 channel <- true
		 }
	 }
}

func nature(N int, garden [][]int, mutex *sync.RWMutex)  {
	for  {
		time.Sleep(time.Second * time.Duration(rand.Intn(10)))
		for i := 0; i < 3; i++ {
			iPos := rand.Intn(N)
			jPos := rand.Intn(N)

			mutex.Lock()
			garden[iPos][jPos] = rand.Intn(5)
			mutex.Unlock()
		}
	}
}

func gardener(N int, garden [][]int, mutex *sync.RWMutex)  {
	for  {
		time.Sleep(time.Second * time.Duration(rand.Intn(4)))

		for i := 0; i < N; i++ {
			for j := 0; j < N; j++ {
				garden[i][j]++
			}
		}
	}
}

func harvestKeeper(N int, garden [][]int, mutex *sync.RWMutex)  {
	for  {
		time.Sleep(time.Second * time.Duration(rand.Intn(5)))
		for i := 0; i < N; i++ {
			for j := 0; j < N; j++ {
				mutex.Lock()
				if garden[i][j] >= 4 {
					garden[i][j] = 0
				}
				mutex.Unlock()

				time.Sleep(time.Millisecond * time.Duration(rand.Intn(10)))
			}
		}
	}
}

func showStatus(N int, garden [][]int, mutex *sync.RWMutex, channel chan bool)  {
	for  {
		mutex.RLock()
		for i := 0; i < N; i++ {
			for j := 0; j < N; j++ {
				fmt.Print(garden[i][j], " ")
			}
			fmt.Println()
		}
		mutex.RUnlock()
		fmt.Println()
		<- channel
	}

}


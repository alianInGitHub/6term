package main

import (
	"math/rand"
	"fmt"
	"sync"
	"time"
	"math"
)

type Edge struct {
	A int
	B int
	price int
}

func main() {
	N := rand.Intn(10) + 5
	graph := generateGraph(N)
	//fmt.Println(N, int(math.Sqrt(float64(1 + 8 * len(graph))) - 1) / 2 + 1)
	mutex := sync.RWMutex{}
	quit := make(chan bool)

	go changePrice(graph, mutex)
	go changeRides(graph, mutex)
	go changeTowns(graph, mutex)
	<- quit
}

func generateGraph(N int) []Edge {
	graph := make([]Edge, 0)
	for i := 0; i < N; i++ {
		for j := i + 1; j < N; j++ {
			graph = append(graph, Edge{i, j, ridePrice()})
		}
	}
	return graph
}

func ridePrice() int {
	return rand.Intn(10)
}

func getNumberOfTowns(graphLength int) int {
	return int(math.Sqrt(float64(1 + 8 * graphLength)) - 1) / 2 + 1
}

func changePrice(graph []Edge, mutex sync.RWMutex) {
	for {
		mutex.RLock()
		N := len(graph)
		if N != 0 {
			i := rand.Intn(N)

			fmt.Print("Price at [", graph[i].A, ", ", graph[i].B, "] has changed from ", graph[i].price, " to ")
			graph[i].price = ridePrice()
			fmt.Println(graph[i].price)
		}
		mutex.RUnlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10) + 1))
	}
}

func changeRides(graph []Edge, mutex sync.RWMutex)  {
	for {
		mutex.Lock()
		N := len(graph)
		if rand.Intn(2) == 0 {
			graph = addRide(graph, N)
		} else {
			graph = removeRide(graph, N)
		}
		mutex.Unlock()
		time.Sleep(time.Second *time.Duration(rand.Intn(10) + 1))
	}
}

func addRide(graph []Edge, N int) []Edge {
	for i := 0; i < N; i++ {
		if graph[i].price == 0 {
			graph[i].price = ridePrice()
			fmt.Println("New path was created from ", graph[i].A, " to ", graph[i].B, " with price ", graph[i].price)
			break
		}
	}
	return graph
}

func removeRide(graph []Edge, N int) []Edge {
	if N != 0 {
		i := rand.Intn(N)
		graph[i].price = 0
		fmt.Println("Path was removed from ", graph[i].A, " to ", graph[i].B)
	}
	return graph
}

func changeTowns(graph []Edge, mutex sync.RWMutex) {
	for  {
		mutex.Lock()
		if rand.Intn(2) == 0 {
			graph = addTown(graph)
		} else {
			graph = removeTown(graph)
		}
		mutex.Unlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10) + 1))
	}
}

func addTown(graph []Edge) []Edge {
	N := len(graph)
	maxTown := 0
	for i := 0; i < N; i++ {
		if graph[i].B > maxTown {
			maxTown = graph[i].B
		}
		if graph[i].A > maxTown {
			maxTown = graph[i].A
		}
	}
	N = int(math.Max(float64(maxTown), float64(N))) + 1
	used := make([]bool, N)
	for i := 0; i < N; i++ {
		used[i] = false
	}
	for i := 0; i < len(graph); i++ {
		if !used[graph[i].A] {
			used[graph[i].A] = true
		}
		if !used[graph[i].B] {
			used[graph[i].B] = true
		}
	}

	v := N
	for i := 0; i < N; i++ {
		if !used[i] {
			v = i
			break
		}
	}

	N = getNumberOfTowns(N)
	for i := 0; i < N; i++ {
		if i != v {
			graph = append(graph, Edge{i, v, 0})
		}
	}
	fmt.Println("New town ", v, " was added")
	return graph
}

func removeTown(graph []Edge) []Edge {
	N := getNumberOfTowns(len(graph))
	town := rand.Intn(N)
	N = len(graph)
	i := 0
	for i != len(graph) {
		if (graph[i].A == town) || (graph[i].B == town) {
			graph = append(graph[:i], graph[i + 1:]...)
		} else {
			i++
		}
	}
	if N != len(graph) {
		fmt.Println("Town ", town, " was deleted")
		fmt.Println(graph)
	}
	return graph
}

func printGraph(graph []Edge)  {
	N := len(graph)
	for i := 0; i < N; i++ {
		fmt.Println(graph[i])
	}
}


package main

import (
	"math/rand"
	"fmt"
	"sync"
	"time"
	"math"
)

type Edge struct {
	to int
	price int
}

func main() {
	N := rand.Intn(15) + 5
	graph := generateGraph(N)
	mutex := sync.RWMutex{}
	quit := make(chan bool)

	go changePrice(graph, mutex)
	go changeRides(graph, mutex)
	go changeTowns(graph, mutex)
	go showPathCost(graph, mutex)
	<- quit
}

func generateGraph(N int) [][]Edge {
	graph := make([][]Edge, N)
	visited := make([][]bool, N)
	for i := 0; i < N; i++ {
		graph[i] = make([]Edge, 0)
		visited[i] = make([]bool, N)
		for j := 0; j < N; j++ {
			visited[i][j] = false
			if i == j {
				visited[i][j] = true
			}
		}
	}
	for i := 0; i < N; i++ {
		for j := 0; j < N; j++ {
			price := ridePrice()
			if (rand.Intn(2) == 0) && (price != 0) && !visited[i][j] {
				graph[i] = append(graph[i], Edge{j, price})
				graph[j] = append(graph[j], Edge{i, price})
				visited[i][j] = true
				visited[j][i] = true
			}
		}
	}
	for i := 0; i < N; i++ {
		fmt.Println(graph[i])
	}
	return graph
}

func ridePrice() int {
	return rand.Intn(10)
}

func getNumberOfTowns(graphLength int) int {
	return int(math.Sqrt(float64(1 + 8 * graphLength)) - 1) / 2 + 1
}

func changePrice(graph [][]Edge, mutex sync.RWMutex) {
	for {
		mutex.RLock()
		N := len(graph)
		if N != 0 {
			i := rand.Intn(N)
			if len(graph[i]) != 0 {
				j := rand.Intn(len(graph[i]))

				fmt.Print("Price at [", i, ", ", graph[i][j].to, "] has changed from ", graph[i][j].price, " to ")
				// if price is 0, then there is no path, so we make it always
				// nonzero in order to prevent path deleting, such function
				// is done by another thread
				graph[i][j].price = ridePrice() + 1
				fmt.Println(graph[i][j].price)
			}
		}
		mutex.RUnlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10) + 1))
	}
}

func changeRides(graph [][]Edge, mutex sync.RWMutex)  {
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

func addRide(graph [][]Edge, N int) [][]Edge {
	if N == 0 {
		return graph
	}
	visited := make([]bool, N)
	for i := 0; i < N; i++ {
		visited[i] = false
	}
	i := rand.Intn(N)
	for j := 0; j < len(graph[i]); j++ {
		if !visited[graph[i][j].to] {
			visited[graph[i][j].to] = true
		}
	}
	for j := 0; j < N; j++ {
		if !visited[j] {
			price := ridePrice() + 1
			graph[i] = append(graph[i], Edge{j, price})
			fmt.Println("New path was created from ", i, " to ", j, " with price ", price)
			break
		}
	}

	return graph
}

func removeRide(graph [][]Edge, N int) [][]Edge {
	if N != 0 {
		i := rand.Intn(N)
		if len(graph[i]) != 0 {
			j := rand.Intn(len(graph[i]))
			fmt.Println("Path was removed from ", i, " to ", graph[i][j].to)
			graph[i] = append(graph[i][:j], graph[i][j+1:]...)
		}
	}
	return graph
}

func changeTowns(graph [][]Edge, mutex sync.RWMutex) {
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

func addTown(graph [][]Edge) [][]Edge {
	newTown := make([]Edge, 0)
	graph = append(graph, newTown)
	fmt.Println("New town ", len(graph) - 1, " was added")
	return graph
}

func removeTown(graph [][]Edge) [][]Edge {
	N := len(graph)
	town := rand.Intn(N)
	graph = append(graph[:town], graph[town + 1:]...)
	for i := 0; i < N - 1; i++ {
		for j := 0; j < len(graph[i]); j++ {
			if graph[i][j].to == town {
				graph[i] = append(graph[i][:j], graph[i][j + 1:]...)
			} else if graph[i][j].to > town {
				graph[i][j].to--
			}
		}
	}
	fmt.Println("Town ", town, " was deleted")
	fmt.Println(graph)
	return graph
}

func showPathCost(graph [][]Edge, mutex sync.RWMutex) {
	for  {
		mutex.RLock()
		N := getNumberOfTowns(len(graph))
		i := rand.Intn(N)
		j := rand.Intn(N)
		if i != j {
			cost := findPath(graph, i, j)
			if cost == 0 {
				fmt.Println("Where is no path from ", i, " to ", j)
			} else {
				fmt.Println("Ride price from town ", i, " to ", j, " is ", cost)
			}
		}
		mutex.RUnlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10) + 1))
	}
}

func findPath(graph [][]Edge, from int, to int) int {
	INF := math.MaxInt64
	N := len(graph)
	visited := make([] bool, N)
	distances := make([]int, N)
	for i := 0; i < N; i++ {
		visited[i] = false
		distances[i] = INF
	}

	distances[from] = 0

	for i := 0; i < N; i++ {
		v := -1
		for j := 0; j < N; j++ {
			if !visited[j] && (v == -1 || distances[j] < distances[v]) {
				v = j
			}
		}
		if distances[v] == INF {
			break
		}
		visited[v] = true
		for j := 0; j < len(graph[i]); j++ {
			to := graph[i][j].to
			len := graph[i][j].price
			if distances[v] + len <distances[to] {
				distances[to] = distances[v] + len
			}
		}
	}
	if distances[to] == INF {
		return 0
	}
	return distances[to]
}


func printGraph(graph [][]Edge)  {
	N := len(graph)
	for i := 0; i < N; i++ {
		fmt.Println(graph[i])
	}
}


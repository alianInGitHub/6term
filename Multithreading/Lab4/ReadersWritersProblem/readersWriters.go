package main

import (
	"bufio"
	"os"
	"log"
	"sync"
	"io"
	"fmt"
	"time"
	"math/rand"
)

func main() {

	quit := make(chan bool)
	path := "/home/anastasia/Documents/GoglandProjects/lab4/ReadersWritersProblem/"

	commandSNFile := openFile(path + "command_sn.txt")
	commandSNFileReader := bufio.NewReader(commandSNFile)

	commandTelFile := openFile(path + "command_tel.txt")
	commandTelFileReader := bufio.NewReader(commandTelFile)

	commandNewDataFile := openFile(path + "new_data.txt")
	commandNewDataFileReader := bufio.NewReader(commandNewDataFile)

	f, err := os.OpenFile(path + "data.txt", os.O_APPEND, 0666)
	checkErr(err)
	//f_copy := openFile(path + "data.txt")
	reader := io.NewSectionReader(f, 0, 100 * 39)
	//writer := bufio.NewWriter(f)


	defer f.Close()
	defer commandSNFile.Close()
	defer commandTelFile.Close()
	defer commandNewDataFile.Close()

	var mutex = &sync.RWMutex{}

	go surnameReader(reader, mutex, commandSNFileReader)
	go surnameReader(reader, mutex, commandSNFileReader)

	go telephoneReader(reader, mutex, commandTelFileReader)
	go telephoneReader(reader, mutex, commandTelFileReader)

	go writerOfNewData(f, mutex, commandNewDataFileReader)

	<- quit
}

func openFile(path string) *os.File {
	file, err := os.Open(path)
	if err != nil {
		log.Fatal(err)
	}
	return file
}

func surnameReader(f *io.SectionReader, mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {
	for {
		isFound := false

		surname := read(commandsFileReader, '\n')

		//erase spaces
		if surname != ""  {
			surname = surname[:len(surname) - 1]
		} else {
			break
		}

		mutex.RLock()
		f.Seek(0, 0)
		reader := bufio.NewReader(f)

		for {
			curr := read(reader, ' ')
			if curr == "" { break }

			curr = curr[:len(curr) - 1]
			if curr == surname {
				str := read(reader, '\n')
				if str == "" { break }
				fmt.Println("[snreader] :\t", curr, str[:len(str) - 1])
				isFound = true
				break
			} else {
				read(reader, '\n')
			}
		}
		mutex.RUnlock()
		if !isFound {
			fmt.Println("[snreader] :\t Not Founnd! : ", surname)
		}
		time.Sleep(time.Second * time.Duration(rand.Intn(5) + 2))
	}
	fmt.Println("end")
}

func read(reader *bufio.Reader, delimiter byte) string  {
	str, err := reader.ReadString(delimiter)
	if err == io.EOF {
		return  ""
	}
	if !checkRWErr(err) {
		fmt.Println("Error")
		log.Fatal(err)
	}
	str = str[:len(str) - 1]
	return str
}

func telephoneReader(f *io.SectionReader, mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {
	for {
		phone := read(commandsFileReader, '\n')
		if phone == "" {  fmt.Println("eof 1"); break }

		isFound := false

		mutex.RLock()
		f.Seek(0, 0)
		reader := bufio.NewReader(f)

		for {
			info := ""
			for i := 0; i < 3; i++ {
				s := read(reader, ' ')
				info += s + " "
			}

			curr := read(reader, '\n')

			if curr == phone {
				info += curr
				fmt.Println("[telreader] :\t", info)
				isFound = true
				break
			}
		}
		mutex.RUnlock()
		if !isFound {
			fmt.Println("[telreader] :\t Not Found : ", phone)
		}
		time.Sleep(time.Second * time.Duration(rand.Intn(5)))
	}
	fmt.Println("tel end")
}

func writerOfNewData(writer *os.File,  mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {
	for  {
		time.Sleep(time.Second *time.Duration(rand.Intn(7)))

		fmt.Print("3")
		data := read(commandsFileReader, '\n')
		if data == "" {
			break
		}

		fmt.Print("3")
		mutex.Lock()
		fmt.Print("3")
		writer.WriteString(data)
		fmt.Print("3")
		mutex.Unlock()

		fmt.Println("[writer] :\t", data)
	}
	fmt.Println("wr end")
}

func checkErr(err error)  {
	if err != nil {
		log.Fatal(err)
	}
}

func checkRWErr(err error) bool {
	if err == io.EOF {
		return false
	} else {
		checkErr(err)
		return true
	}
}
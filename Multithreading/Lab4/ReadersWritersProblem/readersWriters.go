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

var path = "/home/anastasia/Documents/GoglandProjects/lab4/ReadersWritersProblem/"

func main() {

	quit := make(chan bool)

	commandSNFile := openFile(path + "command_sn.txt")
	commandSNFileReader := bufio.NewReader(commandSNFile)

	commandTelFile := openFile(path + "command_tel.txt")
	commandTelFileReader := bufio.NewReader(commandTelFile)

	commandNewDataFile := openFile(path + "new_data.txt")
	commandNewDataFileReader := bufio.NewReader(commandNewDataFile)

	f, err := os.OpenFile(path + "data", os.O_SYNC | os.O_APPEND, 0666)
	checkErr(err)


	defer f.Close()
	defer commandSNFile.Close()
	defer commandTelFile.Close()
	defer commandNewDataFile.Close()

	var mutex = &sync.RWMutex{}

	go surnameReader(f, mutex, commandSNFileReader)
	go surnameReader(f, mutex, commandSNFileReader)

	go telephoneReader(f, mutex, commandTelFileReader)
	go telephoneReader(f, mutex, commandTelFileReader)

	go writer(mutex, commandNewDataFileReader)

	<- quit
}

func openFile(path string) *os.File {
	file, err := os.Open(path)
	if err != nil {
		log.Fatal(err)
	}
	return file
}

func surnameReader(f *os.File, mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {

	for {
		surname := read(commandsFileReader, '\n')
		if surname != ""  {
			surname = surname[:len(surname) - 1]
		} else {
			break
		}

		mutex.RLock()
		info := findInfoBySurname(f, surname)
		mutex.RUnlock()

		if info != "" {
			fmt.Println("[snreader] : \t", info)
		} else {

			fmt.Println("[snreader] :\t Not Founnd! : ", surname)
		}

		time.Sleep(time.Second * time.Duration(rand.Intn(8) + 2))
	}
	fmt.Println("end")
}

func telephoneReader(f *os.File, mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {
	for {
		phone := read(commandsFileReader, '\n')
		if phone == "" {  fmt.Println("eof 1"); break }

		mutex.RLock()
		info := findInfoByPhoneNumber(f, phone)
		mutex.RUnlock()

		if info != "" {
			fmt.Println("[telreader] :\t", info)
		} else {
			fmt.Println("[telreader] :\t Not Found : ", phone)
		}
		time.Sleep(time.Second * time.Duration(rand.Intn(10)))
	}
	fmt.Println("tel end")
}

func writer(mutex *sync.RWMutex, commandsFileReader *bufio.Reader)  {
	for  {
		time.Sleep(time.Second *time.Duration(rand.Intn(7) + 2))

		data := read(commandsFileReader, '\n')
		if data == "" {
			break
		}

		mutex.Lock()
		f, err := os.OpenFile(path + "data" ,os.O_RDWR | os.O_APPEND,0666)
		checkErr(err)

		writeDataIntoFile(f, data)

		f,err = os.OpenFile(path + "data",os.O_SYNC,0666)
		checkErr(err)

		mutex.Unlock()


		fmt.Println("[writer] :\t", data)
	}
	fmt.Println("wr end")
}

func findInfoBySurname(f *os.File, surname string) string {
	f.Seek(0, 0)
	reader := bufio.NewReader(f)

	for {
		curr := read(reader, ' ')
		if curr == "" { break }

		if curr == surname {
			str := read(reader, '\n')
			if str == "" { break }
			return surname + " " + str[:len(str) - 1]
		} else {
			read(reader, '\n')
		}
	}

	return ""
}

func checkErr(err error)  {
	if err != nil {
		log.Fatal(err)
	}
}


func findInfoByPhoneNumber(f *os.File, phoneNumber string) string {
	f.Seek(0, 0)
	reader := bufio.NewReader(f)

	for {
		info := ""
		for i := 0; i < 3; i++ {
			s := read(reader, ' ')
			if s == "" { break }
			info += s + " "
		}

		curr := read(reader, '\n')
		if curr == "" { break }

		if curr == phoneNumber {
			info += curr
			return info
		}
	}
	return ""
}


func writeDataIntoFile(f *os.File, data string)  {
	w:= bufio.NewWriter(f)
	fmt.Fprintln(w, data)
	w.Flush()
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

func checkRWErr(err error) bool {
	if err == io.EOF {
		return false
	} else {
		checkErr(err)
		return true
	}
}
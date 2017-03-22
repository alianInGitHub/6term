package main

import (
	"os"
	"bufio"
	"log"
	"math/rand"
	"io"
	"fmt"
	"strconv"
)

func main() {
	generateData()
}

func genNewNum() string  {
	var num string
	num = "380"
	for i := 0; i < 9; i++ {
		num += strconv.Itoa(rand.Intn(10))
	}

	num += "\n"
	//fmt.Println(num)
	return num
}

func generateData()  {
	path := "/home/anastasia/Documents/GoglandProjects/lab4/ReadersWritersProblem/"

	f, err := os.Open(path + "surnames.txt")
	//checkErr(err)
	if err != nil {
		log.Fatal(err)
	}

	nf, err := os.Create(path + "data.txt")
	if err != nil {
		log.Fatal(err)
	}

	r := bufio.NewReader(f)
	w := bufio.NewWriter(nf)

	defer f.Close()
	defer nf.Close()

	for {
		str, err := r.ReadString(' ')
		if err == io.EOF {
			break
		} else {
			if err != nil {
				log.Fatal(err)
			}
		}
		for str == " " {
			str, err = r.ReadString(' ')
			if err == io.EOF {
				break
			} else {
				if err != nil {
					log.Fatal(err)
				}
			}
		}
		dstr := str
		str, err = r.ReadString('\n')
		if err == io.EOF {
			break
		} else {
			if err != nil {
				log.Fatal(err)
			}
		}
		dstr += str[: len(str) - 1] + " "
		tel := "380"
		for i := 0; i < 9; i++ {
			tel += strconv.Itoa(rand.Intn(10))
		}

		dstr += tel + "\n"
		fmt.Println(dstr)
		_, err = w.WriteString(dstr)
		if err != nil {
			log.Fatal(err)
		}
		w.Flush()
	}
}

func generateNewData()  {
	nf, err := os.Open("/home/anastasia/Documents/GoglandProjects/lab4/new_data_variance.txt")
	if err != nil {
		log.Fatal(err)
	}

	f, err := os.Create("/home/anastasia/Documents/GoglandProjects/lab4/new_data.txt")
	if err != nil {
		log.Fatal(err)
	}

	defer f.Close()
	defer nf.Close()

	reader := bufio.NewReader(nf)

	writer := bufio.NewWriter(f)

	for i := 0; i < 20; i++ {
		name, err := reader.ReadString('\n')
		if err == io.EOF {
			break
		} else if err != nil {
			panic(err)
		}
		name = name[4:len(name) - 1]
		phone := genNewNum()

		name += " " + phone

		writer.WriteString(name)
	}

	writer.Flush()
}

func generateCommandSN(f *os.File)  {
	nf, err := os.Open("/home/anastasia/Documents/GoglandProjects/lab4/new_data_variance.txt")
	//checkErr(err)
	if err != nil {
		log.Fatal(err)
	}

	wf, err := os.Create("/home/anastasia/Documents/GoglandProjects/lab4/command_sn.txt")
	if err != nil {
		log.Fatal(err)
	}

	defer f.Close()
	defer nf.Close()
	defer wf.Close()

	writer := bufio.NewWriter(wf)

	ioR := io.NewSectionReader(f, 0, 100 * 38)
	ioRn := io.NewSectionReader(nf, 0,  20 * 22)
	for i := 0; i < 20; i++ {

		var reader *bufio.Reader
		p := rand.Intn(3)
		if p == 0 {
			ioRn.Seek(int64(rand.Intn(19 * 22)), 0)
			reader = bufio.NewReader(ioRn)

		} else {

			ioR.Seek(int64(rand.Intn(99*38)), 0)
			reader = bufio.NewReader(ioR)
		}
		str, err := reader.ReadString('\n')
		if err == io.EOF {
			break
		} else if err != nil {
			panic(err)
		}
		//fmt.Println(str)
		str = " "
		for str == " " {
			str, err = reader.ReadString(' ')
			if err == io.EOF {
				break
			} else if err != nil {
				panic(err)
			}
		}
		str += "\n"
		writer.WriteString(str)
		fmt.Println(str)
	}
	writer.Flush()
}

func generateCommandTel()  {
	f, err := os.Open("/home/anastasia/Documents/GoglandProjects/lab4/data1.txt")
	//checkErr(err)
	if err != nil {
		log.Fatal(err)
	}
	//generateCommandSN(f)
	wf, err := os.Create("/home/anastasia/Documents/GoglandProjects/lab4/command_tel.txt")
	if err != nil {
		log.Fatal(err)
	}

	defer f.Close()
	defer wf.Close()

	ioR := io.NewSectionReader(f, 0, 100 * 38)
	r := bufio.NewReader(f)
	w := bufio.NewWriter(wf)

	for i := 0; i < 20; i++ {
		p := rand.Intn(3)
		if p == 0 {
			w.WriteString(genNewNum())
		} else {
			n := rand.Intn(99 *38)
			ioR.Seek(int64(n), 0)
			str, err := r.ReadString('\n')
			if err == io.EOF {
				break
			} else {
				if err != nil {
					log.Fatal(err)
				}
			}
			str, err = r.ReadString(' ')
			fmt.Println(1, str)
			str, err = r.ReadString(' ')
			fmt.Println(2, str)
			str, err = r.ReadString(' ')
			fmt.Println(3, str)
			str, err = r.ReadString('\n')
			fmt.Println(4, str)
			if err == io.EOF {
				break
			} else {
				if err != nil {
					log.Fatal(err)
				}
			}

			fmt.Println(str)
			w.WriteString(str)
		}
	}

	w.Flush()
}

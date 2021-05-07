package main

import (
	"fmt"

	"github.com/PaulioRandall/cookies/go/pkg/stack"
)

func main() {

	// Write play code here...

	fmt.Println()
	fmt.Println("All work and no play makes Jack a dull boy,")
	fmt.Println("All play and no work makes Jack a mere toy.")
	fmt.Println()

	s := stack.Stack{}

	s.Push("Thing 1")
	s.Push("Thing 2")
	s.Push("Thing 3")

	fmt.Println(s.JoinString("\n"))
	fmt.Println()

	thing3, exists := s.Pop()
	fmt.Println(thing3, "exists:", exists)
	fmt.Println()

	fmt.Println(s.JoinString("\n"))
	fmt.Println()

	for exists {
		_, exists = s.Pop()
	}

	fmt.Println("Empty:", s.Empty())
}

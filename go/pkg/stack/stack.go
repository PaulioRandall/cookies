package stack

import (
	"fmt"
	"strings"
)

type (
	Thing string

	Node struct {
		Data Thing
		Next *Node
	}

	Stack struct {
		Top  *Node
		Size int
	}
)

func (n Node) String() string {
	return fmt.Sprintf("%+v", n.Data)
}

func (s *Stack) Empty() bool {
	return s.Size == 0
}

func (s *Stack) Push(t Thing) {
	s.Top = &Node{Data: t, Next: s.Top}
	s.Size++
}

func (s *Stack) Peek() (Thing, bool) {
	if s.Size == 0 {
		return Thing(""), false
	}
	return s.Top.Data, true
}

func (s *Stack) Pop() (t Thing, ok bool) {
	if t, ok = s.Peek(); ok {
		s.Top = s.Top.Next
		s.Size--
	}
	return
}

func (s *Stack) String() string {
	return s.JoinString(", ")
}

func (s *Stack) JoinString(delim string) string {

	sb := strings.Builder{}
	f := func(n *Node) {
		sb.WriteString(n.String())
		if n.Next != nil {
			sb.WriteString(delim)
		}
	}

	s.foreachNode(f)
	return sb.String()
}

func (s *Stack) foreachNode(f func(n *Node)) {
	for n := s.Top; n != nil; n = n.Next {
		f(n)
	}
}

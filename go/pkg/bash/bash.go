package bash

import (
	"os"
)

var dirStack []string

func Pushd(path string) error {

	currDir, e := os.Getwd()
	if e != nil {
		return e
	}

	if e := os.Chdir(path); e != nil {
		return e
	}

	pushItem(currDir)
	return nil
}

func Popd() error {

	dir := lastItem()
	if e := os.Chdir(dir); e != nil {
		return e
	}

	popItem()
	return nil
}

func lastItem() string {
	i := len(dirStack) - 1
	return dirStack[i]
}

func pushItem(dir string) {
	dirStack = append(dirStack, dir)
}

func popItem() {
	i := len(dirStack) - 1
	dirStack = dirStack[:i]
}

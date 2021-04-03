#!/bin/bash

# Build script to save me 5 seconds on building, formatting, & testing.
# I do these actions a lot, sometimes as frequently as every 30 seconds!

# If you want to do less of something, make it hard & slow
# If you want to do more of it, make it easy & quick

set -e
tabs -2

printUsage() {
  println "Usage:"
  println "\t" "./godo [-noclear] [help]" "\t" "Show usage"
  println "\t" "./godo [-noclear] fmt   " "\t" "Format"
  println "\t" "./godo [-noclear] test  " "\t" "Format -> test"
  println "\t" "./godo [-noclear] play  " "\t" "Format -> test -> run playground"
  println "Option:"
  println "\t" "'-noclear'" "\t" "skips screen clearing"
}

# println prints the arguments then a line feed.
# $@: List of text strings to print
println() {
  for s in "$@"
  do
    printf "$s"
  done

  printf "\n"
}

goFmt() {
  println "Formatting..."
  go fmt ./...
}

goTest() {
  println "Testing..."
  go test ./... -timeout "2s"
}

goVet() {
  println "Vetting..."
  go vet ./...
}

goPlay() {
  println "Playing..."
  go run "playground/playground.go"
}

if [[ "$1" == "-noclear" ]]; then
  shift 1
else
  clear
fi

if [[ "$1" == "help" ]]; then
  printUsage
  exit 0
fi

if [[ "$1" == "" ]]; then
  printUsage
  exit 0
fi

if [[ "$1" == "fmt" ]]; then
  goFmt
  exit 0
fi

if [[ "$1" == "test" ]]; then
  goFmt
  goTest
  exit 0
fi

if [[ "$1" == "vet" ]]; then
  goFmt
  goTest
  goVet
  exit 0
fi

if [[ "$1" == "play" ]]; then
  goFmt
  goTest
  goVet

  shift 1
  goPlay $@

  println
  exit 0
fi

println "I don't understand the option '$1'."
printUsage
exit 1

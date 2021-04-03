#!/bin/bash

# Build script to save me 5 seconds on building, formatting, & testing.
# I do these actions a lot, sometimes as frequently as every 30 seconds!

# If you want to do less of something, make it hard & slow
# If you want to do more of it, make it easy & quick

set -e
tabs -2

BUILD_DIR="build"
EXE_NAME="my-go-app"
GO_MAIN="path/to/main/file.go"
GO_PLAY="playground/playground.go"
BUILD_FLAGS=""
#BUILD_FLAGS=-gcflags -m -ldflags "-s -w"
TEST_TIMEOUT="2s"

printUsage() {
  println "Usage:"
  println "\t" "./godo [-noclear] [help]" "\t" "Show usage"
  println "\t" "./godo [-noclear] clean " "\t" "Delete build directory"
  println "\t" "./godo [-noclear] build " "\t" "Build -> format"
  println "\t" "./godo [-noclear] test  " "\t" "Build -> format -> test"
  println "\t" "./godo [-noclear] vet   " "\t" "Build -> format -> test -> vet"
  println "\t" "./godo [-noclear] run   " "\t" "Build -> format -> test -> vet -> run program"
  println "\t" "./godo [-noclear] shrink" "\t" "Build -> format -> test -> vet -> Shrink binary if 'upx' is installed"
  println "\t" "./godo [-noclear] play  " "\t" "Run around the playground"
  println "Option:"
  println "\t" "'-noclear'" "\t" "skips screen clearing"
}

println() {
  for s in "$@"
  do
    printf "$s"
  done

  printf "\n"
}

clean() {
  println "Cleaning..."
  rm -r -f "$BUILD_DIR"
}

setup() {
  println "Setup..."
  clean
  mkdir -p "$BUILD_DIR"
}

goBuild() {
  println "Building..."
  go build -o "$BUILD_DIR/$EXE_NAME" $BUILD_FLAGS "$GO_MAIN"
}

goFmt() {
  println "Formatting..."
  go fmt ./...
}

goTest() {
  println "Testing..."
  go test ./... -timeout "$TEST_TIMEOUT"
}

goVet() {
  println "Vetting..."
  go vet ./...
}

runExe() {
  println "Running..."
  cd $BUILD_DIR
  ./"$EXE_NAME" $@
  cd $ROOT_DIR
}

upxShrink() {
  println Shrinking...
  cd $BUILD_DIR

  if [ "command -v upx" ]; then
    upx --ultra-brute $EXE_NAME
  else
    println "Shrinking requires 'upx' be installed"
  fi
    
  cd $ROOT_DIR
}

goPlay() {
  println "Playing..."
  go run "$GO_PLAY" $@
}

if [[ "$1" == "-noclear" ]]; then
  shift 1
else
  clear
fi

if [[ "$1" == "" || "$1" == "help" ]]; then
  printUsage
  exit 0
fi

if [[ "$1" == "clean" ]]; then
  clean
  exit 0
fi

if [[ "$1" == "build" ]]; then
  setup
  goBuild
  goFmt
  exit 0
fi

if [[ "$1" == "test" ]]; then
  setup
  goBuild
  goFmt
  goTest
  exit 0
fi

if [[ "$1" == "vet" ]]; then
  setup
  goBuild
  goFmt
  goTest
  goVet
  exit 0
fi

if [[ "$1" == "run" ]]; then
  setup
  goBuild
  goFmt
  goTest
  goVet

  shift 1
  runExe $@

  println
  exit 0
fi

if [[ "$1" == "shrink" ]]; then
  setup
  goBuild
  goFmt
  goTest
  goVet

  upxShrink  
  exit 0
fi

if [[ "$1" == "play" ]]; then
  shift 1
  goPlay $@

  println
  exit 0
fi

println "I don't understand the option '$1'."
printUsage
exit 1

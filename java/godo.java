//usr/bin/javac godo.java && clear ; java -cp ./ godo "$@" ; rm -f *.class ; exit
// Java 8

import java.io.*;
import java.util.function.*;
import java.util.concurrent.*;
import java.util.*;
import java.io.FileWriter;
import java.nio.file.*;

// ############################################################################
// Main script class.
public class godo {

  // **************************************************************************
  static final Command[] CMDS = {
    new Clean(),
    new Build(),
    new Run()
  };

  // **************************************************************************
  // Main script entry point.
  public static void main(String[] args) throws Exception {

    Platform os = Platform.identify();
    Command.println("Platform: " + os.getClass().getName());

    if (args.length < 1) {
      printUsage();
      System.exit(1);
    }

    Command[] cmds = parseCommands(args);

    for (Command c : cmds) {
      c.exe(os);
    }
  }

  // **************************************************************************
  // Parses the list of user commands.
  static Command[] parseCommands(String[] args) {

    Command[] cmds = new Command[args.length];

    for (int i = 0; i < args.length; i++) {

      Command c = identifyCommand(args[i]);
      if (c == null) {
        Command.println("Unknown command '" + args[i] + "'");
        printUsage();
        System.exit(1);
      }

      cmds[i] = c;
    }

    return cmds;
  }

  // **************************************************************************
  // Identifies and returns the specified command.
  static Command identifyCommand(String args) {

    args = args.toLowerCase();

    for (Command c : CMDS) {
      String name = c.getClass().getName().toLowerCase();

      if (args.equals(name)) {
        return c;
      }
    }

    return null;
  }

  // **************************************************************************
  // Prints a the script's usage instructions.
  static void printUsage() {

    String s = "Usage:\n";
    s += "\t" + "./godo.java 'command' [command...]\n";
    s += "Commands:\n";

    for (int i = 0; i < CMDS.length; i++) {
      s += i == 0 ? "\t" : ", ";

      String name = CMDS[i].getClass().getName().toLowerCase();
      s += name;
    }

    s += "\nExamples:\n";
    s += "\t" + "./godo.java run\n";
    s += "\t" + "./godo.java clean build\n";

    System.err.println(s);
  }
}

// ############################################################################
// Const contains the global constants.
class Const {
  static Path ROOT_DIR = Paths.get(".").toAbsolutePath().normalize();
  static Path SRC_DIR = ROOT_DIR.resolve("src");
  static Path BUILD_DIR = ROOT_DIR.resolve("build");
  static Path CLASS_DIR = BUILD_DIR.resolve("classes");
  static Path TEMP_SRC_FILE = BUILD_DIR.resolve("srcs.txt");
}

// ############################################################################
// Abstract representation of an OS.
abstract class Platform {

  // **************************************************************************
  // Identifies and returns an instance for the current platform.
  static Platform identify() throws Exception {
    
    String name = System.getProperty("os.name").toLowerCase();

    // Original: https://mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
    if (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0) {
      return new Unix();
    }

    throw new RuntimeException("OS not supported");    
  }

  // **************************************************************************
  // Runs a command.
  static int run(final String cmd) throws Exception {

    // 1x out stream, 1x err stream
    ExecutorService exe = Executors.newFixedThreadPool(2);

    try {      
      Process process = Runtime.getRuntime().exec(cmd);

      asyncPrintStream(exe, process.getInputStream(), System.out);
      asyncPrintStream(exe, process.getErrorStream(), System.err);
    
      int exitCode = process.waitFor();
      exe.shutdown();

      return exitCode;

    } finally {
      exe.awaitTermination(200, TimeUnit.MILLISECONDS);
    }
  }

  // **************************************************************************
  // Async pipes the text from the input stream into print stream.
  static void asyncPrintStream(
    ExecutorService exe,
    InputStream in,
    PrintStream out
  ) {

    final InputStreamReader isr = new InputStreamReader(in);
    final BufferedReader br = new BufferedReader(isr);

    exe.submit(new Runnable() {
      public void run() {
        br.lines().forEach(out::println);
      }
    });
  }

  // **************************************************************************
  // Compiles the Java source files listed in the specified file.
  int javac() throws Exception {
    return run("javac @" + Const.TEMP_SRC_FILE + " -d " + Const.CLASS_DIR);
  }
}

// ############################################################################
// Implements Unix specific platform operations.
class Unix extends Platform {
}

// ############################################################################
// Represents a command or unit of processing.
abstract class Command {

  // **************************************************************************
  // Shorthand for `System.out.println(s)`.
  static <T> void println(T s) {
    System.out.println(s);
  }

  // **************************************************************************  
  // Executes the function.
  abstract void exe(Platform os) throws Exception;
}

// ############################################################################
// Cleans the project by removing build files.
class Clean extends Command {

  // **************************************************************************
  void exe(Platform os) throws Exception {

    println("Cleaning...");

    // https://www.baeldung.com/java-delete-directory
    if (Files.exists(Const.BUILD_DIR)) {
      Files.walk(Const.BUILD_DIR)
        .filter(p -> p.toFile().exists())
        .sorted(Comparator.reverseOrder())
        .forEach(p -> p.toFile().delete());
    }
  }
}

// ############################################################################
// Builds the project by compiling java classes.
class Build extends Command {

  // **************************************************************************
  void exe(Platform os) throws Exception {

    println("Building...");

    List<Path> srcFiles = findSrcFiles();

    mkdirs();
    writePathList(srcFiles, Const.TEMP_SRC_FILE);
    
    if (os.javac() != 0) {
      throw new RuntimeException("javac failed: check err output");
    }
  }

  // **************************************************************************
  // mkdirs the build directory for a new build.
  void mkdirs() throws Exception {

    if (!Files.exists(Const.BUILD_DIR)) {
      println("mkdir: " + Const.BUILD_DIR);
      Files.createDirectory(Const.BUILD_DIR);
    }

    if (!Files.exists(Const.CLASS_DIR)) {
      println("mkdir: " + Const.CLASS_DIR);
      Files.createDirectory(Const.CLASS_DIR);
    }
  }

  // **************************************************************************
  // Returns all java files in the project.
  List<Path> findSrcFiles() throws Exception {

    List<Path> r = new ArrayList<>();

    Files.walk(Const.SRC_DIR).forEach(p -> {
      if (p.toString().endsWith(".java")) {
        r.add(p);
      }
    });

    return r;
  }

  // **************************************************************************
  // Writes out the paths to a file as a line separated list.
  void writePathList(List<Path> files, Path dst) throws Exception {
    
    String b = "";

    for(Path p : files) {
      b += p + System.lineSeparator();
    }

    Files.write(dst, b.getBytes());
  }
}

// ############################################################################
// Runs the built program.
class Run extends Command {

  // **************************************************************************
  void exe(Platform os) throws Exception {
    println("Running...");
    println("'run' not implemented yet!");
  }
}

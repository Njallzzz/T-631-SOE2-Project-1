import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

class Program {
  public static void main(String[] args) {
    String filename;
    Integer T_SUPPORT = 3;
    Integer T_CONFIDENCE = 65;

    if (args.length == 0) {
      System.out.println("Must supply arguments.");
      System.exit(0);
    }

    filename = args[0];
    if (args.length == 3) {
      T_SUPPORT = Integer.parseInt(args[1]);
      T_CONFIDENCE = Integer.parseInt(args[2]);
    }

    CallGraph callGraph = Parser.parse(filename);
    BugDetector detector = new BugDetector(callGraph);
    detector.scan(T_SUPPORT, T_CONFIDENCE);
  }
}

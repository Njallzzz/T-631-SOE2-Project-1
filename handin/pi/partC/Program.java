import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

class Program {
  public static void main(String[] args) {
    Integer T_SUPPORT = 3;
    Integer T_CONFIDENCE = 65;
    Integer T_DEPTH = 0;

    if (args.length == 2) {
      T_SUPPORT = Integer.parseInt(args[0]);
      T_CONFIDENCE = Integer.parseInt(args[1]);
    } else if(args.length == 3) {
      T_SUPPORT = Integer.parseInt(args[0]);
      T_CONFIDENCE = Integer.parseInt(args[1]);
      T_DEPTH = Integer.parseInt(args[2]); 
    }

    CallGraph callGraph = Parser.parse();
    BugDetector detector = new BugDetector(callGraph);
    detector.scan(T_SUPPORT, T_CONFIDENCE, T_DEPTH);
  }
}

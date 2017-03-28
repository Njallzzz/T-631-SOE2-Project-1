// To generate callgraph: opt-3.3 -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.io.*;

class Parser {
  private static CallGraph callGraph;

  // Parses a callgraph into a CallGraph class and returns it
  public static CallGraph parse(String filepath) {
    CallGraph callGraph = new CallGraph();

    try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
      String currentLine;
      while((currentLine = br.readLine()) != null) {
        if (currentLine.trim().startsWith("Call graph node for")) {
          // New caller node. Extract the caller name and start parsing the edges
          String caller = currentLine.split("'")[1];
          while ((currentLine = br.readLine()) != null) {
            if (!currentLine.trim().startsWith("CS")) {
              // No longer parsing edges, fall back to node parsing
              break;
            }
            if (currentLine.contains("external")) {
              // Don't care about external nodes
              continue;
            }
            // Line is now a "CS" line, find the callee name and add it to the call graph
            String callee = currentLine.split("'")[1];
            callGraph.addEdge(caller, callee);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return callGraph;
  }
}

// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.*;

class Parser {
	private static String root;
  private static CallGraph callGraph;
	private static Map<String, Set<String>> callgraph;
	private static Map<String, Integer> onesupport;

  // A structure containing all the pairs in the program.
  // The keys are the pairs themselves, the value is that pair's support
  private static Map<FuncPair, Integer> supportMap;
  private static Set<FuncPair> pairs;

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

	public static void main (String [] args) throws java.io.IOException {
		// Initialize data structures used for storage
		callgraph = new HashMap<String, Set<String>>();
		onesupport = new HashMap<String, Integer>();
    supportMap = new HashMap<FuncPair, Integer>();
    callGraph = new CallGraph();

		// Redirect stdin to BufferedReader
		InputStreamReader instream = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(instream);

		// While loop variables
		String line = null, nodeName = null;
		Set<String> currNode = null;

    while((line = buffer.readLine()) != null) {
      if (line.trim().startsWith("Call graph node for")) {
        // New caller node. Extract the caller name and start parsing the edges
        String caller = line.split("'")[1];
        while ((line = buffer.readLine()) != null) {
          if (!line.trim().startsWith("CS") || line.contains("external")) {
            // No longer parsing edges, fall back to node parsing
            break;
          }
          // Line is now a "CS" line, find the callee name and add it to the call graph
          String callee = line.split("'")[1];
          callGraph.addEdge(caller, callee);
        }
      }
    }

		// Parser main while loop
		// while((line = buffer.readLine()) != null) {
		// 	if( line.length() == 0 ) {
		// 		if( currNode != null || nodeName != null ) {
		// 			callgraph.put(nodeName, currNode);
		// 		}
		// 	} else if( line.contains("Root is:") ) {
		// 		root = line.substring(19);
		// 	} else if(line.contains("node for f")) {
		// 		currNode = new HashSet<String>();
		// 		line = line.substring(31);
		// 		nodeName = line.substring(0, line.indexOf('\''));
		// 	} else if(line.contains("calls function")) {
		// 		if(currNode != null) {
		// 			line = line.substring(line.indexOf('\'')+1, line.length()-1);
		// 			currNode.add(line);
		// 		}
		// 	}
		// }

		// Generate support for individual functions
		// for(String key : callgraph.keySet()) {
		// 	onesupport.put(key, support(key));
		// }

		// Debugging viewer of function
		/*System.out.println("Root: " + root);
		for (Map.Entry<String, Set<String>> entry : callgraph.entrySet()) {
			String key = entry.getKey();
			Set<String> value = entry.getValue();
			System.out.println("\nKey: " + key);

			Iterator iterator = value.iterator();
			while (iterator.hasNext()){
				System.out.println("Entry: " + iterator.next());
			}
		}	// */

    // Generate all pairs of callees from our call graph, calculate the support
    // for each pair and store it.
    // getCallees() returns a Set, we need to convert it to a List.
	}
}

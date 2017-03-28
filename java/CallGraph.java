import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.StringBuilder;

// A call graph implemented as a directed adjacency matrix.
// Terminology: Nodes and edges, as per any graph.
// Caller: A node that has an edge pointing away from it toward a callee
// Callee: A node that has an edge pointing into it from a caller
class CallGraph {
  Map<String, Map<String, Boolean>> graph;
  Set<String> nodes;

  public CallGraph() {
    graph = new HashMap<String, Map<String, Boolean>>();
    nodes = new HashSet<String>();
  }

  // Adds an edge between caller and callee, creating the nodes if they don't exist
  public void addEdge(String caller, String callee) {
    if (!graph.containsKey(caller)) {
      graph.put(caller, new HashMap<String, Boolean>());
      nodes.add(caller);
    }

    Map<String, Boolean> calls = graph.get(caller);

    calls.put(callee, true);
    nodes.add(callee);
  }

  // Returns true if there is an edge from node1 to node2, false otherwise
  public Boolean hasEdge(String node1, String node2) {
    if (!graph.containsKey(node1)) {
      return false;
    }

    Map<String, Boolean> callees = graph.get(node1);

    if (!callees.containsKey(node2)) {
      return false;
    }

    return callees.get(node2);
  }

  public Boolean hasNode(String node) {
    return nodes.contains(node);
  }

  // Get a set of all nodes that have edges to callee
  public Set<String> getCallersTo(String callee) {
    Set<String> result = new HashSet<>();

    for (String caller : graph.keySet()) {
      Set<String> callees = getCalleesFrom(caller);
      if (callees.contains(callee)) result.add(caller);
    }
    return result;
  }

  // Get a set of all nodes that caller has edges to
  public Set<String> getCalleesFrom(String caller) {
    if (!graph.containsKey(caller)) return null;

    Map<String, Boolean> callees = graph.get(caller);
    return callees.keySet();
  }

  // Calculate how many times this pair of edges leaves the same node
  public int edgePairs(String f1, String f2) {
    int result = 0;
    for(Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      if (callees.containsKey(f1) && callees.containsKey(f2)) {
        result++;
      }
    }
    return result;
  }

  // Calculate how many edges go to the given node
  public int edgesTo(String node) {
    int result = 0;
    for(String caller : graph.keySet()) {
      Map<String, Boolean> callees = graph.get(caller);
      if (callees.containsKey(node)) {
        result++;
      }
    }
    return result;
  }

  // Returns a set of the names of all nodes in the graph
  public Set<String> getNodes() {
    return nodes;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for(Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      builder.append(entry.getKey() + " -> ");
      Map<String, Boolean> callees = entry.getValue();
      for (String callee : callees.keySet()) {
        builder.append(callee + ", ");
      }
      builder.append('\n');
		}

    return builder.toString();
  }

  public static void main(String[] args) {
    CallGraph graph = new CallGraph();

    /*
      Function scope1 calls functions A, B, C, D
      Function scope2 calls functions A, C, D
      Function scope3 calls functions A, B
      Function scope4 calls functions B, D, scope1,
      Function scope5 calls functions A, B, D
      Function scope6 calls functions B, D
    */
    graph.addEdge("scope1", "A");
    graph.addEdge("scope1", "B");
    graph.addEdge("scope1", "C");
    graph.addEdge("scope1", "D");

    graph.addEdge("scope2", "A");
    graph.addEdge("scope2", "C");
    graph.addEdge("scope2", "D");

    graph.addEdge("scope3", "A");
    graph.addEdge("scope3", "B");
    graph.addEdge("scope3", "B");
    graph.addEdge("scope3", "B");
    graph.addEdge("scope3", "B");

    graph.addEdge("scope4", "B");
    graph.addEdge("scope4", "D");
    graph.addEdge("scope4", "scope1");

    graph.addEdge("scope5", "A");
    graph.addEdge("scope5", "B");
    graph.addEdge("scope5", "D");

    graph.addEdge("scope6", "B");
    graph.addEdge("scope6", "D");

    System.out.println(graph);
    System.out.println(graph.edgePairs("A", "C"));
  }
}

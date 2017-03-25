import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.StringBuilder;

class CallGraph {
  Map<String, Map<String, Boolean>> graph;

  public CallGraph() {
    graph = new HashMap<String, Map<String, Boolean>>();
  }

  public void addEdge(String caller, String callee) {
    if (!graph.containsKey(caller)) {
      graph.put(caller, new HashMap<String, Boolean>());
    }

    Map<String, Boolean> calls = graph.get(caller);

    calls.put(callee, true);
  }

  public Set<String> getCallees() {
    Set<String> nodes = new HashSet<String>();

    for (Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      nodes.addAll(callees.keySet());
    }

    return nodes;
  }

  public Boolean calls(String caller, String callee) {
    if (!graph.containsKey(caller)) {
      return false;
    }

    Map<String, Boolean> callees = graph.get(caller);

    if (!callees.containsKey(callee)) {
      return false;
    }

    return callees.get(callee);
  }

  public int support(String f1, String f2) {
    int result = 0;
    for(Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      if (callees.containsKey(f1) && callees.containsKey(f2)) {
        result++;
      }
    }
    return result;
  }

  public int support(String func) {
    int result = 0;
    for(Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      if (callees.containsKey(func)) {
        result++;
      }
    }
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
    System.out.println(graph.support("A", "C"));
    System.out.println(graph.getCallees());
    List<String> c = new ArrayList<>(graph.getCallees());
    Collections.reverse(c);
    System.out.println(c);
  }
}

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
  Map<FuncPair, Integer> pairSupport;
  Map<String, Integer> funcSupport;

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

  // Returns true if caller calls the callee, false otherwise
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

  // Returns a set of all callers (essentially each scope)
  public Set<String> getCallers() {
    return graph.keySet();
  }

  public Set<String> getCallersContaining(String callee) {
    Set<String> result = new HashSet<>();

    for (String caller : getCallers()) {
      Set<String> callees = getCalleesFor(caller);
      if (callees.contains(callee)) result.add(caller);
    }
    return result;
  }

  // Returns a set of all callees
  public Set<String> getCallees() {
    Set<String> nodes = new HashSet<String>();

    for (Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      nodes.addAll(callees.keySet());
    }

    return nodes;
  }

  // Gets all functions the given caller calls.
  public Set<String> getCalleesFor(String caller) {
    if (!graph.containsKey(caller)) return null;

    Map<String, Boolean> callees = graph.get(caller);
    return callees.keySet();
  }

  // Can we memoize this so lookups are faster?
  public Set<FuncPair> getPairsContaining(String func) {
    if (pairSupport == null) return null;

    Set<FuncPair> allPairs = pairSupport.keySet();
    Set<FuncPair> containingPairs = new HashSet<>();

    for (FuncPair pair : allPairs) {
      if (pair.contains(func)) containingPairs.add(pair);
    }

    return containingPairs;
  }

  // Will get all pairs in a given scope (the caller).
  // Returns null if the caller does not exist.
  public Set<FuncPair> getPairsFor(String caller) {
    if (!graph.containsKey(caller)) return null;
    Set<FuncPair> pairs = new HashSet<FuncPair>();

    Map<String, Boolean> callees = graph.get(caller);
    List<String> calleeList = new ArrayList<>(callees.keySet());

    // Generate all the pairs
    for (int i = 0; i < calleeList.size(); i++) {
      for (int j = i+1; j < calleeList.size(); j++) {
        pairs.add(new FuncPair(calleeList.get(i), calleeList.get(j)));
      }
    }

    return pairs;
  }

  // Calculates how many times a pair of functions appear together.
  // Pairs appearing more than one time in a single scope (a single
  // caller calls the pair multiple times) will be counted only once.
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

  // Calculates in how many scopes a function is called.
  // A function that is called multiple times in a single scope
  // is only counted once.
  public int support(String func) {
    int result = 0;
    for(Map.Entry<String, Map<String, Boolean>> entry : graph.entrySet()) {
      Map<String, Boolean> callees = entry.getValue();
      if (callees.containsKey(func)) {
        result++;
      }
    }
    return result;
  }

  // Calculates and memoizes the support for each pair of callees.
  // We don't need to create pairs from callers if they are not
  // callees themselves.
  public Map<FuncPair, Integer> allPairsSupport() {
    if (this.pairSupport != null) {
      return this.pairSupport;
    }

    Map<FuncPair, Integer> supportMap = new HashMap<>();

    // Generate each pair of functions from our call graph callees and
    // calculate the support of that pair.
    List<String> callees = new ArrayList<>(this.getCallees());
    for (int i = 0; i < callees.size(); i++) {
      for (int j = i+1; j < callees.size(); j++) {
        FuncPair pair = new FuncPair(callees.get(i), callees.get(j));
        int support = this.support(pair.first, pair.second);
        supportMap.put(pair, support);
      }
    }
    this.pairSupport = supportMap;
    return supportMap;
  }

  // Calculates and memoizes the support for each callee.
  public Map<String, Integer> allFunctionsSupport() {
    if (this.funcSupport != null) {
      return this.funcSupport;
    }

    Map<String, Integer> supportMap = new HashMap<>();

    for (String s : this.getCallees()) {
      // Calculate the support of each callee and add it to our map
      int support = this.support(s);
      supportMap.put(s, support);
    }
    this.funcSupport = supportMap;
    return supportMap;
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

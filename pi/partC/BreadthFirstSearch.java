import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;

class BreadthFirstSearch {
  private CallGraph G;
  private Map<String, Boolean> marked;
  private Map<String, String> edgeTo;
  private String source;
  private String target;
  private String other;

  public BreadthFirstSearch(CallGraph G, String source, String target, String other) {
    marked = new HashMap<>();
    this.G = G;
    this.source = source;
    this.target = target;
    this.other = other;

    for (String node : G.getNodes()) {
      marked.put(node, false);
    }
  }

  public String bfs(Integer maxDepth) {
    if (maxDepth < 0) {
      return null;
    }

    Queue<String> queue = new LinkedList<>();
    marked.put(source, true);
    queue.add(source);

    Integer currentDepth = 0,
      elementsToDepthIncrease = 1,
      nextElementsToDepthIncrease = 0;

    while (queue.size() != 0) {
      String current = queue.poll();
      Set<String> childrenOfCurrent = G.getCalleesFrom(current);
      Boolean containsMoreOfTarget = G.getTimesCalled(current, target) > G.getTimesCalled(current, other);

      if (containsMoreOfTarget) {
        // Not bug, stop searching, return the scope
        return current;
      }

      nextElementsToDepthIncrease += childrenOfCurrent.size();
      if (--elementsToDepthIncrease == 0) {
        // Reached maxDepth and node not found, probably a bug
        if (++currentDepth > maxDepth) return null;
        elementsToDepthIncrease = nextElementsToDepthIncrease;
        nextElementsToDepthIncrease = 0;
      }

      for (String child : childrenOfCurrent) {
        if (!marked.get(child)) {
          marked.put(child, true);
          queue.add(child);
        }
      }
    }
    return null;
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
  }
}

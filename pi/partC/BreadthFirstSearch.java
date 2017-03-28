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
}

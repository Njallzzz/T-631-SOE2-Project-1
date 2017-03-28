import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

class BugDetector {
  private CallGraph graph;
  private Map<String, Integer> functionSupport;
  private Map<FuncPair, Integer> pairSupport;
  private String OUTPUT_FORMAT = "bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%\r\n";

  public BugDetector(CallGraph graph) {
    this.graph = graph;
  }

  public void scan(Integer support, Integer confidence) {
    calculateFunctionSupport(support);
    calculatePairSupport(support);

    for (FuncPair pair : pairSupport.keySet()) {
      int pSup = pairSupport.get(pair);
      if (pSup < support) continue; // Ignore pairs that are under the support threshold

      // Iterate each variable in the pair
      for (int i = 0; i < 2; i++) {
        // Calculate the confidence for each variable in the pair
        double conf = pSup * 100 / (double)functionSupport.get(pair.get(i));

        if (conf >= confidence) {
          // Check all scopes for the reported variable
          Set<String> scopes = graph.getCallersTo(pair.get(i));
          for (String scope : scopes) {
            Set<String> callees = graph.getCalleesFrom(scope);
            // If the scope calls the first function but not the second, we can
            // assume that the bug lies in this scope
            if (callees.contains(pair.get(i)) && !callees.contains(pair.get(i+1))) {
              String output = String.format(
                OUTPUT_FORMAT,
                pair.get(i),
                scope,
                pair.first,
                pair.second,
                pSup,
                conf
              );

              System.out.print(output);
            }
          }
        }
      }

      // double confidenceFirst = pSup * 100 / (double)functionSupport.get(pair.first);
      // double confidenceSecond = pSup * 100 / (double)functionSupport.get(pair.second);
      //
      // if (confidenceFirst >= confidence) {
      //   // Check all scopes for the reported variable
      //   Set<String> scopes = graph.getCallersTo(pair.first);
      //   for (String scope : scopes) {
      //     Set<String> callees = graph.getCalleesFrom(scope);
      //     // If the scope calls the first function but not the second, we can
      //     // assume that the bug lies in this scope
      //     if (callees.contains(pair.first) && !callees.contains(pair.second)) {
      //       String output = String.format(
      //         OUTPUT_FORMAT,
      //         pair.first,
      //         scope,
      //         pair.first,
      //         pair.second,
      //         pSup,
      //         confidenceFirst
      //       );
      //
      //       System.out.print(output);
      //     }
      //   }
      // }
      //
      // if (confidenceSecond >= confidence) {
      //   Set<String> scopes = graph.getCallersTo(pair.second);
      //   for (String scope : scopes) {
      //     Set<String> callees = graph.getCalleesFrom(scope);
      //     if (!callees.contains(pair.first) && callees.contains(pair.second)){
      //       String output = String.format(
      //         OUTPUT_FORMAT,
      //         pair.second,
      //         scope,
      //         pair.first,
      //         pair.second,
      //         pSup,
      //         confidenceSecond
      //       );
      //
      //       System.out.print(output);
      //     }
      //   }
      // }
    }

    functionSupport = null;
    pairSupport = null;
  }

  private void calculateFunctionSupport(Integer supportThreshold) {
    Map<String, Integer> supportMap = new HashMap<>();
    for (String node : graph.getNodes()) {
      // Calculate the support of each node and add it to our map if
      // it meets the threshold
      int support = graph.edgesTo(node);
      if (support >= supportThreshold) supportMap.put(node, support);
    }
    functionSupport = supportMap;
  }

  private void calculatePairSupport(Integer supportThreshold) {
    // We always want to first calculate the support for every standalone
    // function, because then we can limit the amount of pairs we need
    // to calculate support for.
    if (functionSupport == null) {
      calculateFunctionSupport(supportThreshold);
    }

    Map<FuncPair, Integer> supportMap = new HashMap<>();
    // Generate all the pairs required for support calculations.
    // Note that we only need to generate pairs that include functions
    // that themselves have support greater or equal than the threshold.
    // If a function overall is called fewer times than the threshold,
    // it's definitely not called as part of a pair any more often.
    List<String> callees = new ArrayList<>(functionSupport.keySet());
    for (int i = 0; i < callees.size(); i++) {
      for (int j = i+1; j < callees.size(); j++) {
        FuncPair pair = new FuncPair(callees.get(i), callees.get(j));
        int support = graph.edgePairs(pair.first, pair.second);
        if (support >= supportThreshold) supportMap.put(pair, support);
      }
    }
    pairSupport = supportMap;
  }
}

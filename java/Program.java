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
    String OUTPUT_FORMAT = "bug: %s in %s, pair: (%s, %s), support: %d, confidence: %.2f%%\n";

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
    Map<FuncPair, Integer> pairSupport = callGraph.allPairsSupport();
    Set<FuncPair> allPairs = pairSupport.keySet();
    Map<String, Integer> funcSupport = callGraph.allFunctionsSupport();

    // for each pair, calculate the confidence.
    for (FuncPair pair : allPairs) {
      // Get the support for this pair
      int pSup = pairSupport.get(pair);

      // We know the pair that is bugged, and which function in the pair is responsible,
      // so I need to check every scope and see if the trouble variable is there
      // without the other
      double conf1 = pSup * 100 / (double)funcSupport.get(pair.first);
      double conf2 = pSup * 100 / (double)funcSupport.get(pair.second);

      if (pSup >= T_SUPPORT && conf1 > T_CONFIDENCE) {
        // Check all scopes for the reported variable
        Set<String> scopes = callGraph.getCallersContaining(pair.first);
        for (String scope : scopes) {
          Set<String> callees = callGraph.getCalleesFor(scope);
          if (callees.contains(pair.first) && !callees.contains(pair.second)) {
            String output = String.format(
              OUTPUT_FORMAT,
              pair.first,
              scope,
              pair.first,
              pair.second,
              pSup,
              conf1
            );

            System.out.print(output);
          }
        }
      }

      if (pSup >= T_SUPPORT && conf2 > T_CONFIDENCE) {
        // Check all scopes for the reported variable
        Set<String> scopes = callGraph.getCallersContaining(pair.second);
        for (String scope : scopes) {
          Set<String> callees = callGraph.getCalleesFor(scope);
          if (!callees.contains(pair.first) && callees.contains(pair.second)){
            String output = String.format(
              OUTPUT_FORMAT,
              pair.second,
              scope,
              pair.first,
              pair.second,
              pSup,
              conf2
            );

            System.out.print(output);
          }
        }
      }
    }
  }
}

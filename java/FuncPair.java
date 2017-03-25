package project.soe2;

import java.util.Map;
import java.util.HashMap;

class FuncPair {
  public final String first;
  public final String second;

  public FuncPair() {
    this.first = "";
    this.second = "";
  }

  public FuncPair(String first, String second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof FuncPair)) {
      return false;
    }
    FuncPair that = (FuncPair) o;
    return that.first.equals(first) && that.second.equals(second);
  }

  @Override
  public int hashCode() {
    return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
  }

  @Override
  public String toString() {
    return first + ":" + second;
  }

  public static void main (String [] args) {
    Map<FuncPair, Integer> map = new HashMap<>();

    FuncPair pair1 = new FuncPair("Hello", "World");
    FuncPair pair2 = new FuncPair("Yo", "Man");
    FuncPair pair3 = new FuncPair("FuMan", "Chu");

    map.put(pair1, pair1.hashCode());
    map.put(pair2, pair2.hashCode());
    map.put(pair3, pair3.hashCode());

    for (Map.Entry<FuncPair, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
  }
}

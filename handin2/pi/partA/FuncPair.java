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
    // Pairs need to be sorted lexicographically according to project PDF
    if (first.compareTo(second) <= 0) {
      this.first = first;
      this.second = second;
    } else {
      this.first = second;
      this.second = first;
    }
  }

  public String get(Integer i) {
    if (i % 2 == 0) {
      return first;
    } else {
      return second;
    }
  }

  public Boolean contains(String func) {
    return first.equals(func) || second.equals(func);
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
    return "(" + first + ", " + second + ")";
  }

  public static void main (String [] args) {
    Map<FuncPair, Integer> map = new HashMap<>();

    FuncPair pair1 = new FuncPair("Hello", "World");
    FuncPair pair2 = new FuncPair("Yo", "Man");
    FuncPair pair3 = new FuncPair("FuMan", "Chu");

    map.put(pair1, pair1.hashCode());
    map.put(pair2, pair2.hashCode());
    map.put(pair3, pair3.hashCode());

    System.out.println(pair1.get(0));
    System.out.println(pair1.get(1));
    System.out.println(pair1.get(2));
    System.out.println(pair1.get(3));
  }
}

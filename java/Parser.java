// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.*;

class Parser {
	private static String root;
	private static Map<String, Set<String>> callgraph;
	private static Map<String, Integer> onesupport;

	private static int support(Set<String> hset) {
		int num = 0;
		for (Map.Entry<String, Set<String>> entry : callgraph.entrySet()) {
			if(entry.getValue().containsAll(hset)) {
				num++;
			}
		}
		return num;
	}

	public static void main (String [] args) throws java.io.IOException {
		// Initialize data structures used for storage
		callgraph = new HashMap<String, Set<String>>();
		onesupport = new HashMap<String, Integer>();

		// Redirect stdin to BufferedReader
		InputStreamReader instream = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(instream);

		// While loop variables
		String line = null, nodeName = null;
		Set<String> currNode = null;

		// Parser main while loop
		while((line = buffer.readLine()) != null) {
			if( line.length() == 0 ) {
				if( currNode != null || nodeName != null ) { 
					callgraph.put(nodeName, currNode); 
				}
			} else if( line.contains("Root is:") ) {
				root = line.substring(19);
			} else if(line.contains("node for f")) {
				currNode = new HashSet<String>();
				line = line.substring(31);
				nodeName = line.substring(0, line.indexOf('\''));
			} else if(line.contains("calls function")) {
				if(currNode != null) {
					line = line.substring(line.indexOf('\'')+1, line.length()-1);
					currNode.add(line);
				}
			}
		}

		// Generate support for individual functions
		for(String key : callgraph.keySet()) {
			Set<String> st = new HashSet<String>();
			st.add(key);
			Integer num = support(st);
			onesupport.put(key, num);
			System.out.println("{" + key + "}: " + num);
		}

		// Debugging viewer of function
		System.out.println("Root: " + root);
		for (Map.Entry<String, Set<String>> entry : callgraph.entrySet()) {
			String key = entry.getKey();
			Set<String> value = entry.getValue();
			System.out.println("\nKey: " + key);

			Iterator iterator = value.iterator();      
			while (iterator.hasNext()){
				System.out.println("Entry: " + iterator.next());
			}
		}

	}
}

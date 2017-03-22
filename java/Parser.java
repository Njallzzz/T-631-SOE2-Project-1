// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

class Parser {
	private static String root;
	private static Map<String, List<String>> callgraph;

	public static void main (String [] args) throws java.io.IOException {
		// Initialize data structures used for storage
		callgraph = new HashMap<String, List<String>>();

		// Redirect stdin to BufferedReader
		InputStreamReader instream = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(instream);

		// While loop variables
		String line = null, nodeName = null;
		List currNode = null;

		// Parser main while loop
		while((line = buffer.readLine()) != null) {
			if( line.length() == 0 ) {
				if( currNode != null || nodeName != null ) { 
					callgraph.put(nodeName, currNode); 
				}
			} else if( line.contains("CallGraph Root is:") ) {
				root = line.substring(19);
			} else if(line.contains("Call graph node for function:")) {
				currNode = new ArrayList<String>();
				nodeName = line.substring(31, line.indexOf('\''));
			} else if(line.contains(" calls function ")) {
				if(currNode != null) {
					line = line.substring(line.indexOf('\'')+1, line.length()-1);
					currNode.add(line);
				}
			}
		}

		// Debugging viewer of function
		for (Map.Entry<String, List<String>> entry : callgraph.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			System.out.println("\nKey: " + key);
			for(int i = 0; i < value.size(); i++) {
				System.out.println("Entry: " + value.get(i));
			}
		}
	}
}

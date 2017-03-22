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
		int length = 0;
		String line = null, nodeName = null;
		List currNode = null;
		while((line = buffer.readLine()) != null) {
			length = length + line.length();
			System.out.println(line.length() + ": " + line);

			if( line.length() == 0 ) {
				if( currNode != null || nodeName != null ) { 
					callgraph.put(nodeName, currNode); 
				}
			} else if( line.contains("CallGraph Root is:") ) {
				root = line.substring(19);
			} else if(line.contains("Call graph node for function:")) {
				currNode = new ArrayList<String>();
				line = line.substring(31);
				nodeName = line.substring(0, line.indexOf('\''));
			} else if(line.contains(" calls function ")) {
				if(currNode != null) {
					line = line.substring(line.indexOf('\'')+1, line.length()-1);
					System.out.println("function call: " + line);
					currNode.add(line);
				} else
					continue;
			}

		}

		for (Map.Entry<String, List<String>> entry : callgraph.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			System.out.println(key);

			for(int i = 0; i < value.size(); i++) {
				System.out.println("entry: " + value.get(i));
			}
		}

	}
}

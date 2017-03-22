// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
		String line;

		while((line = buffer.readLine()) != null) {
			if( line.contains("CallGraph Root is:") )
				root = line.substring(19);

			length = length + line.length();

			System.out.println(line);
		}

		System.out.println("root: " + root);
		System.out.println("Length: " + length);
	}
}

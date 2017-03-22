// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.*;

class Parser {
	private static Map<String, List<String>> callgraph;

	public static void main (String [] args) throws java.io.IOException {
		callgraph = new HashMap<String, List<String>>();

		InputStreamReader instream = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(instream);

		int length = 0;
		String line;	

		while((line = buffer.readLine()) != null) {
			length = length + line.length();

			System.out.println(line);
		}

		System.out.println("Length: " + length);
	}
}

// To generate callgraph: opt -print-callgraph <.bc file here> 2>&1 1> /dev/null

class Parser {
	public static void main (String [] args) throws java.io.IOException {
		int ch;
		System.out.print ("Enter some text: ");
		while ((ch = System.in.read ()) != '\n')
			System.out.print ((char) ch);
	}
}

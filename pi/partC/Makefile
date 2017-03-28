JC = javac
CLASSES = \
	Program.java \
	Parser.java \
	FuncPair.java \
	CallGraph.java \
	BugDetector.java \
	BreadthFirstSearch.java 

all: $(CLASSES)
	$(JC) -Xlint:unchecked $(CLASSES)

clean:
	$(RM) *.class

test1:
	cat callgraph_test1 | java -classpath . Program test1

test2:
	cat callgraph_test2 | java -classpath . Program test2

test3:
	cat callgraph_test3 | java -classpath . Program test3

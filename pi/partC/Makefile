JC = javac
CLASSES = \
	Program.java \
	Parser.java \
	FuncPair.java \
	CallGraph.java \
	BugDetector.java\
	BreadthFirstSearch.java \

all: $(CLASSES)
	$(JC) -Xlint:unchecked $(CLASSES)

clean:
	$(RM) *.class

test1:
	java -classpath . Program callgraph_test1

test2:
	java -classpath . Program callgraph_test2

test3:
	java -classpath . Program callgraph_test3

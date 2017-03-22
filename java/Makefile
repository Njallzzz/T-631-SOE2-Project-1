JC = javac
CLASSES = \
	Parser.java \

all: $(CLASSES)
	$(JC) -Xlint:unchecked $(CLASSES)

clean:
	$(RM) *.class

runtest1:
	java -classpath . Parser < callgraph_test1

runtest2:
	java -classpath . Parser < callgraph_test2

runtest3:
	java -classpath . Parser < callgraph_test3

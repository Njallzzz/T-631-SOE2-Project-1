JC = javac
CLASSES = \
	Parser.java \

all: $(CLASSES)
	$(JC) -Xlint:unchecked $(CLASSES)

clean:
	$(RM) *.class

test1:
	java -classpath . Parser < callgraph_test1

test2:
	java -classpath . Parser < callgraph_test2

test3:
	java -classpath . Parser < callgraph_test3

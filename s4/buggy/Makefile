all:
	javac -encoding UTF-8 Hello.java -classpath "../.."
	javac -encoding UTF-8 Frequencer.java -classpath "../.."
	javac -encoding UTF-8 InformationEstimator.java -classpath "../.."
	javac -encoding UTF-8 TestCase.java -classpath "../.."
run:
	java -classpath "../.." s4.buggy/Hello
	java -classpath "../.." s4.buggy/Frequencer
	java -classpath "../.." s4.buggy/InformationEstimator
	java -ea -classpath "../.." s4.buggy/TestCase
clean:
	rm -f *.class *~



package s4.B201818; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    boolean targetReady = false;
    boolean spaceReady = false;
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { myTarget = target; if(target.length>0) targetReady = true;}
    public void setSpace(byte []space) { 
	myFrequencer = new Frequencer();
	mySpace = space; myFrequencer.setSpace(space); 
	spaceReady = true;
    }

    public double estimation(){
	if(targetReady == false) return (double) 0.0;
	if(spaceReady == false) return Double.MAX_VALUE;
	boolean [] partition = new boolean[myTarget.length+1];
	int np;


	np = 1<<(myTarget.length-1);
	// System.out.println("np="+np+" length="+myTarget.length);
	double value = Double.MAX_VALUE; // value = mininimum of each "value1".

	for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
	    // binary representation of p forms partition.
	    // for partition {"ab" "cde" "fg"}
	    // a b c d e f g   : myTarget
	    // T F T F F T F T : partition:
	    partition[0] = true; // I know that this is not needed, but..
	    for(int i=0; i<myTarget.length -1;i++) {
		partition[i+1] = (0 !=((1<<i) & p));
	    }
	    partition[myTarget.length] = true;

	    // Compute Information Quantity for the partition, in "value1"
	    // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            double value1 = (double) 0.0;
	    int end = 0;;
	    int start = end;
	    while(start<myTarget.length) {
		// System.out.write(myTarget[end]);
		end++;;
		while(partition[end] == false) { 
		    // System.out.write(myTarget[end]);
		    end++;
		}
		// System.out.print("("+start+","+end+")");
		myFrequencer.setTarget(subBytes(myTarget, start, end));
		int freq = myFrequencer.frequency();
		if(freq == 0) {
		    value1 = Double.MAX_VALUE;
		    break;
		}
		if(freq < 0) return (double) 0.0;
		value1 = value1 + iq(freq);
		start = end;
	    }
	    // System.out.println(" "+ value1);

	    // Get the minimal value in "value"
	    if(value1 < value) value = value1;
	}
	return value;
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}		  
			       

	























/*package s4.B201818; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;
*/






/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface {
    void setTarget(byte target[]);  // set the data for computing the information quantities
    void setSpace(byte space[]);  // set data for sample space to computer probability
    double estimation();  // It returns 0.0 when the target is not set or Target's length is zero;
    // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
    // The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
    // Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
    // Otherwise, estimation of information quantity,
}
*/



/*
public class InformationEstimator implements InformationEstimatorInterface {
    static boolean debugMode = false;
    // Code to test, *warning: This code is slow, and it lacks the required test
    byte[] myTarget; // data to compute its information quantity
    byte[] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    private void showVariables() {
	for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	System.out.write(' ');
	for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[] requires copy.
        byte[] result = new byte[end - start];
        for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
        return result;
    }

    // f: information quantity for a count, -log2(count/sizeof(space))
    double f(int freq) {
	if(freq ==0){
		return Double.MAX_VALUE;
	}
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
	if(myTarget.length == 0){
		return 0.0;
	}
	if(mySpace.length == 0){
		return Double.MAX_VALUE;
	}
        boolean [] partition = new boolean[myTarget.length+1];
        int np = 1<<(myTarget.length-1);
        double value = Double.MAX_VALUE; // value = mininimum of each "value1".
	if(debugMode) { showVariables(); }
        if(debugMode) { System.out.printf("np=%d length=%d ", np, +myTarget.length); }

        for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
            // binary representation of p forms partition.
            // for partition {"ab" "cde" "fg"}
            // a b c d e f g   : myTarget
            // T F T F F T F T : partition:
            partition[0] = true; // I know that this is not needed, but..
            for(int i=0; i<myTarget.length -1;i++) {
                partition[i+1] = (0 !=((1<<i) & p));
            }
            partition[myTarget.length] = true;

            // Compute Information Quantity for the partition, in "value1"
            // value1 = f(#"ab")+f(#"cde")+f(#"fg") for the above example
            double value1 = (double) 0.0;
            int end = 0;
            int start = end;
            while(start<myTarget.length) {
                // System.out.write(myTarget[end]);
                end++;;
                while(partition[end] == false) {
                    // System.out.write(myTarget[end]);
                    end++;
                }
                // System.out.print("("+start+","+end+")");
                myFrequencer.setTarget(subBytes(myTarget, start, end));
                value1 = value1 + f(myFrequencer.frequency());
		// it should  -->   value1 = value1 + f(myFrequencer.subByteFrequency(start, end)
		// note that subByteFrequency is not work for B201818 version.
                start = end;
            }
            // System.out.println(" "+ value1);

            // Get the minimal value in "value"
            if(value1 < value) value = value1;
        }
	if(debugMode) { System.out.printf("%10.5f\n", value); }
        return value;
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
	debugMode = true;
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
    }
}*/


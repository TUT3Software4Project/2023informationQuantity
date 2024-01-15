package s4.umemura; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    static boolean debugMode = false;
    // Code to tet, *warning: This code condtains intentional problem*
    boolean targetReady = false;
    boolean spaceReady = false;
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    /*

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    */

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
	if(myTarget.length == 0) return (double) 0.0; // Is it needed?

	myFrequencer.setTarget(myTarget);

	double [] suffixEstimation = new double[myTarget.length+1];

	for(int i=0;i<myTarget.length;i++) { suffixEstimation[i] = Double.MAX_VALUE; }
	    
	suffixEstimation[myTarget.length] = (double) 0.0; //IE("") = 0.0; shortest suffix of target

	for(int n=myTarget.length-1;n>=0;n--) {
            // target = "abcdef..", n = 4 for example, subByte(0, 4) = "abcd",
            // IE("abcd") = min( iq(#a)+IE("bcd"),
	    //                   iq(#ab)+IE("cd"),
	    //                   iq(#abc)+IE("d"),
	    //                   iq(#abcd))+IE("") )
            // suffixEstimation[4] = IE(""), subByte(0,4) = "abcd", 
            // suffixEstimation[3] = IE("d");  subByte(0,3)= "abc",
            // suffixEstimation[2] = IE("cd");  subByte(0,2)= "ab",
            // suffixEstimation[1] = IE("bcd");  subByte(0,1)= "a",	
	    // suffixEstimation[0] = IE("abcd");                     
	    //
	    double value = Double.MAX_VALUE; // for suffixEstimation[n]
	    double value1 = Double.MAX_VALUE; // for candidate of suffixEstimation[n]
	    int start = n;
	    for(int end = n+1; end<= myTarget.length;end++) {
		int freq = myFrequencer.subByteFrequency(start, end);
		// You should compute value1 here, (example is iq(#ab)+IE("cd") above),
		// using this freq and apropriate SuffixEstimation[somewhere].

		if(value>value1) value = value1; // compute minimum of value1,
	    }
	    if(debugMode) {
		// System.out.print("suffixEstimation["+n+"] = "+value);
	    }
	    suffixEstimation[n]=value;
	}
	return suffixEstimation[0];

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
				  
			       

	
    

package s4.B233367; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

/*
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


public class TestCase {
    static boolean success = true;

    public static void main(String[] args) {
	try {
	    FrequencerInterface  myObject;
	    int freq;
	    System.out.println("checking Frequencer");

	    // This is smoke test
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    assert freq == 4: "Hi Ho Hi Ho, H: " + freq;

	    // Write your testCase here

		// Test case to detect the issue with frequency() method
		myObject.setSpace("ABC".getBytes());
		myObject.setTarget("ABCD".getBytes());  // Target length is greater than space length
		try {
		    freq = myObject.frequency();
		    // If no exception is thrown, the test has failed
		    assert false: "Expected ArrayIndexOutOfBoundsException, but got frequency: " + freq;
		} catch (ArrayIndexOutOfBoundsException e) {
		    // If exception is caught, the test has passed
		    System.out.println("Test passed: " + e.getMessage());
		}

		// Test Case 1: Target is not set
		myObject = new Frequencer();
		myObject.setSpace("Hi Ho Hi Ho".getBytes());
		freq = myObject.frequency();
		assert freq == -1 : "Target not set, expected -1, but got " + freq;
		
		// Test Case 2: Target's length is zero
		myObject = new Frequencer();
		myObject.setSpace("Hi Ho Hi Ho".getBytes());
		myObject.setTarget(new byte[0]);
		freq = myObject.frequency();
		assert freq == -1 : "Target's length is zero, expected -1, but got " + freq;
		
		// Test Case 3: Space is not set
		myObject = new Frequencer();
		myObject.setTarget("H".getBytes());
		freq = myObject.frequency();
		assert freq == 0 : "Space not set, expected 0, but got " + freq;
		
		// Test Case 4: Space's length is zero
		myObject = new Frequencer();
		myObject.setSpace(new byte[0]);
		myObject.setTarget("H".getBytes());
		freq = myObject.frequency();
		assert freq == 0 : "Space's length is zero, expected 0, but got " + freq;
		
		// Test Case 5: Normal case - Target present in Space
		myObject = new Frequencer();
		myObject.setSpace("Hi Ho Hi Ho".getBytes());
		myObject.setTarget("H".getBytes());
		freq = myObject.frequency();
		assert freq == 4 : "Hi Ho Hi Ho, H: expected 4, but got " + freq;
		
		// Test Case 6: Normal case - Target not present in Space
		myObject = new Frequencer();
		myObject.setSpace("Hi Ho Hi Ho".getBytes());
		myObject.setTarget("X".getBytes());
		freq = myObject.frequency();
		assert freq == 0 : "Hi Ho Hi Ho, X: expected 0, but got " + freq;

	}
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
	    success = false;
	}

	try {
	    InformationEstimatorInterface myObject;
	    double value;
	    System.out.println("checking InformationEstimator");
	    myObject = new InformationEstimator();
	    myObject.setSpace("3210321001230123".getBytes());
	    myObject.setTarget("0".getBytes());
	    value = myObject.estimation();
	    assert (value > 1.9999) && (2.0001 >value): "IQ for 0 in 3210321001230123 should be 2.0. But it returns "+value;
	    myObject.setTarget("01".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 01 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("0123".getBytes());
	    value = myObject.estimation();
	    assert (value > 2.9999) && (3.0001 >value): "IQ for 0123 in 3210321001230123 should be 3.0. But it returns "+value;
	    myObject.setTarget("00".getBytes());
	    value = myObject.estimation();
	    assert (value > 3.9999) && (4.0001 >value): "IQ for 00 in 3210321001230123 should be 3.0. But it returns "+value;
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in InformationEstimator Object");
	    success = false;
	}
        if(success) { System.out.println("TestCase OK"); } 
    }
}	    
	    

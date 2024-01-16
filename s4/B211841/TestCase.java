package s4.B211841; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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

		myObject.setSpace("Hiii".getBytes()); // space: 4 bytes
		myObject.setTarget("Hiiii".getBytes()); // target: 5 bytes
		freq = myObject.frequency();
		assert freq == 0: "Hiii, Hiiii: " + freq;


		myObject.setSpace("".getBytes());
		myObject.setTarget("Hiiii".getBytes());
		freq = myObject.frequency();
		assert freq == 0 : "NotSet,Hiiii" + freq;

		myObject.setSpace("Hiii".getBytes());
		myObject.setTarget("".getBytes());
		freq = myObject.frequency();
		assert freq == -1 : "Hiii, NotSet" + freq;

		myObject.setSpace("AAAA".getBytes());
		myObject.setTarget("AA".getBytes());
		freq = myObject.frequency();
		assert freq == 3: "AAAA,AA: " + freq;


// //Week1-STEP13
//             myObject = new Frequencer();
//             myObject = new Frequencer();
//             myObject.setSpace("Hi".getBytes());
//             myObject.setTarget("HiH".getBytes());
//             freq = myObject.frequency();
//             assert freq == 0: "HiH: " + freq;
// //Week1-STEP14
//             myObject = new Frequencer();
//             freq = myObject.frequency();
//             assert freq == -1: "TARGET is not set or TARGET's length is zero";
//             myObject = new Frequencer();
//             freq = myObject.frequency();
//             assert freq == 0: "SPACE is not set or Space's length is zero";
//             // Write your testCase here



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
	    

package s4.B233322; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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
       	    // Test case 1: target is not set
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            freq = myObject.frequency();
            assert freq == -1 : "test failed: when target not set, return value must be -1. but return value is " + freq + ".";
        
            // Test case 2: target length is zero
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("".getBytes());
            freq = myObject.frequency();
            assert freq == -1 : "test failed: when target length is zero, return value must be -1. but return value is " + freq + ".";
        
            // Test case 3: space is not set
            myObject = new Frequencer();
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
            assert freq == 0 : "test failed: when space is not set, return value must be 0. but return value is " + freq + ".";
        
            // Test case 4: space length is zero
            myObject = new Frequencer();
            myObject.setSpace("".getBytes());
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
            assert freq == 0 : "test failed: when space length is zero, return value must be 0. but return value is " + freq + ".";
        
            // Test case 5: valid case
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
            assert freq == 4 : "test failed: space is \"Hi Ho Hi Ho\", target is \"H\", return value must be 4, but it is " + freq + ".";
        
            // Test case 6: valid case
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("X".getBytes());
            freq = myObject.frequency();
            assert freq == 0 : "test failed: space is \"Hi Ho Hi Ho\", target is \"X\", return value must be 0, but it is " + freq + ".";
        

            //detect the problem(week2 - step6)
            try
            {
                // Test case 7: valid case
                myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());
                myObject.setTarget("Hi".getBytes());
                freq = myObject.frequency();
                System.out.println("test case 7: success!");
                assert freq == 2 : "test failed: space is \"Hi Ho Hi Ho\", target is \"Hi\", return value must be 2, but it is " + freq + ".";
            }
            catch (Exception e)
            {
                System.out.println("test case 7: problem remain!");
                e.printStackTrace();
            }
	}
	catch(Exception e) {
	    System.out.println("Exception occurred in Frequencer Object");
            e.printStackTrace();
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
            e.printStackTrace();
	    success = false;
	}
        if(success) { System.out.println("TestCase OK"); } 
    }
}	    
	    

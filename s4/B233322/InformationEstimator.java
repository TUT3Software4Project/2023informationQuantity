package s4.B233322; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

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
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        if (myFrequencer == null) { myFrequencer = new Frequencer(); }
        myTarget = target;
	myFrequencer.setTarget(target);
    }

    @Override
    public void setSpace(byte[] space) {
        if (myFrequencer == null) { myFrequencer = new Frequencer(); }
        mySpace = space;
	myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
	if (this.myTarget == null || this.myTarget.length == 0) { return 0.0; }
	if (this.mySpace == null || this.mySpace.length == 0) { return Double.MAX_VALUE; }
	
        double value;
	double f_result;
	if(debugMode) { showVariables(); }
        if(debugMode) { System.out.printf("length=%d ", +myTarget.length); }

	double [][] values = new double[myTarget.length][myTarget.length];  //first subs for start/begin(), second subs for end()-1
	for (int gap=1; gap <= myTarget.length; ++gap)
	{
	    for (int i=0; i+gap <= myTarget.length; ++i)
	    {
		value = Double.MAX_VALUE;
		for (int j=i+1; j < i+gap; ++j)  //j is for divide pos
		{
		    f_result = values[i][j-1] + values[j][i+gap-1];
		    value = ((f_result < value) ? f_result : value);
		}
		f_result = f(myFrequencer.subByteFrequency(i, i+gap));
		values[i][i+gap-1] = ((f_result < value) ? f_result : value);
	    }
	}
	if(debugMode) { System.out.printf("%10.5f\n", values[0][myTarget.length-1]); }
        return (values[0][myTarget.length-1] == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : values[0][myTarget.length-1];
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
}


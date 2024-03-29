package s4.B233367; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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
        for(int i = 0; i < end - start; i++) {
            result[i] = x[start + i];
        };
        return result;
    }

    // f: information quantity for a count, -log2(count/sizeof(space))
    double f(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
        if(myFrequencer != null) myFrequencer.setTarget(target);
    }

    @Override
    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
    }

    @Override
    public double estimation(){
        // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
        if(myTarget == null || myTarget.length == 0) return 0.0;
        if(mySpace == null) return Double.MAX_VALUE;

        double[] values = new double[myTarget.length];
        for(int i = 0; i < values.length; ++i) {
            values[i] = Double.MAX_VALUE;
        }

        double value = iq(0, myTarget.length, values);

	if(debugMode) { System.out.printf("%10.5f\n", value); }

        return value;
    }


    private double iq(int start, int end, double[] values) {
        /*
            下例のようにメモをするDPで実装
            例 : target = "abcd", start = 0, end = 4
            values.length = myTarget.length
            values[0] = "abcd"のiq
            values[1] = "abc" のiq
            values[2] = "ab"  のiq
            values[3] = "a"   のiq
        */
        if(end - start <= 0) return 0.0;
        int length = end - start;
        int index = myTarget.length - length;
        if(values[index] != Double.MAX_VALUE) return values[index];

        double minValue = Double.MAX_VALUE;
        for(int i = 0; i < length; ++i) {
            double value = iq(start, start + i, values);
            int freq = myFrequencer.subByteFrequency(start + i, end);
            if(freq < 0)       return 0.0;
            else if(freq == 0) continue;
            
            value += f(freq);
            if(value < minValue) minValue = value;
        }
        values[index] = minValue;

        return minValue;
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


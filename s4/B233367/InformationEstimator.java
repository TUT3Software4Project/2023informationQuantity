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
        return estimation2();
    }
    public double estimation1(){
        // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
        if(myTarget == null || myTarget.length == 0) return 0.0;
        if(mySpace == null) return Double.MAX_VALUE;

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
                int freq = myFrequencer.subByteFrequency(start, end);
                if(freq == 0){
                    value1 = Double.MAX_VALUE;
                    break;
                }
                if(freq < 0) return (double) 0.0;
                value1 = value1 + f(freq);
		// it should  -->   value1 = value1 + f(myFrequencer.subByteFrequency(start, end)
		// note that subByteFrequency is not work for B233367 version.
                start = end;
            }
            // System.out.println(" "+ value1);

            // Get the minimal value in "value"
            if(value1 < value) value = value1;
        }
	if(debugMode) { System.out.printf("%10.5f\n", value); }
        return value;
    }

    public double estimation2(){
        // It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
        if(myTarget == null || myTarget.length == 0) return 0.0;
        if(mySpace == null) return Double.MAX_VALUE;

        double[] values = new double[valueNum()];
        for(int i = 0; i < values.length; ++i) {
            values[i] = Double.MAX_VALUE;
        }

        double value = iq(0, myTarget.length, values);

	if(debugMode) { System.out.printf("%10.5f\n", value); }
        return value;
    }

    private int valueNum() {
        int n = 0;
        for(int i = 1; i <= myTarget.length; ++i) {
            n += i;
        }
        return n;
    }

    private double iq(int start, int end, double[] values) {
        if(start == end) return 0.0;

        int length = end - start;
        int l = myTarget.length - length;
        int index = l * (l + 1) / 2 + start;
        if(values[index] != Double.MAX_VALUE) return values[index];

        double minValue = Double.MAX_VALUE;
        for(int i = 0; i < length; ++i) {
            double value = iq(start, start + i, values);
            int freq = myFrequencer.subByteFrequency(start + i, end);
            if(freq == 0){
                value = Double.MAX_VALUE;
                break;
            }
            if(freq < 0) return 0.0;
            value += f(freq);

            if(value < minValue) minValue = value;
        }
        values[index] = minValue;
/*
for(int i = 0; i < values.length; ++i) {
    if(values[i] == Double.MAX_VALUE) System.out.print("MAX ");
    else System.out.printf("%.3f ", values[i]);
}
System.out.println();
*/
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


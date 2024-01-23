package s4.B233375; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.util.Objects;
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
    // Code to tet, *warning: This code condtains intentional problem*
    boolean targetReady = false;
    boolean spaceReady = false;
    byte[] myTarget; // data to compute its information quantity
    byte[] mySpace; // Sample space to compute the probability
    FrequencerInterface myFrequencer; // Object for counting frequency

    private void showVariables() {
        for (int i = 0; i < mySpace.length; i++) {
            System.out.write(mySpace[i]);
        }
        System.out.write(' ');
        for (int i = 0; i < myTarget.length; i++) {
            System.out.write(myTarget[i]);
        }
        System.out.write(' ');
    }

    byte[] subBytes(byte[] x, int start, int end) {
        // corresponding to substring of String for byte[],
        // It is not implement in class library because internal structure of byte[]
        // requires copy.
        byte[] result = new byte[end - start];
        for (int i = 0; i < end - start; i++) {
            result[i] = x[start + i];
        }
        ;
        return result;
    }

    // f: information quantity for a count, -log2(count/sizeof(space))
    double iq(int freq) {
        return -Math.log10((double) freq / (double) mySpace.length) / Math.log10((double) 2.0);
    }

    public void setTarget(byte[] target) {
        myTarget = target;
        if (target.length > 0)
            targetReady = true;
    }

    public void setSpace(byte[] space) {
        myFrequencer = new Frequencer();
        mySpace = space;
        myFrequencer.setSpace(space);
        spaceReady = true;
    }

    public double estimation() {

        if (targetReady == false)
            return (double) 0.0;
        if (spaceReady == false)
            return Double.MAX_VALUE;
        if (myTarget.length == 0) {
            System.err.println("reach length = 0");
            return (double) 0.0; // Is it needed?

        }
        myFrequencer.setTarget(myTarget);

        double[] suffixEstimation = new double[myTarget.length + 1];
        // suffixEstimation[i] -> myTarget[i,length)をtargetとしたときに情報量を最小限に分割したときの最小値
        // init : suffixEstimation[length]= 0 (targetが空のときは情報量が0となる)
        // trans: suffixEstimation[i] = min(j=i to length)(suffixEstimation[j] + iq[i,j)
        // )
        // find : suffixEstimation[0]

        for (int i = 0; i < myTarget.length; i++) {
            suffixEstimation[i] = Double.MAX_VALUE;
        }

        suffixEstimation[myTarget.length] = (double) 0.0; // IE("") = 0.0; shortest suffix of target

        for (int n = myTarget.length - 1; n >= 0; n--) {
            // target = "abcdef..", n = 4 for example, subByte(0, 4) = "abcd",
            // IE("abcd") = min( iq(#a)+IE("bcd"),
            // iq(#ab)+IE("cd"),
            // iq(#abc)+IE("d"),
            // iq(#abcd))+IE("") )
            // suffixEstimation[4] = IE(""), subByte(0,4) = "abcd",
            // suffixEstimation[3] = IE("d"); subByte(0,3)= "abc",
            // suffixEstimation[2] = IE("cd"); subByte(0,2)= "ab",
            // suffixEstimation[1] = IE("bcd"); subByte(0,1)= "a",
            // suffixEstimation[0] = IE("abcd");
            //
            double value = Double.MAX_VALUE; // for suffixEstimation[n]
            double value1 = Double.MAX_VALUE; // for candidate of suffixEstimation[n]
            int start = n;
            for (int end = n + 1; end <= myTarget.length; end++) {
                int freq = myFrequencer.subByteFrequency(start, end);
                if (freq == 0) {
                    return Double.MAX_VALUE;
                }

                // You should compute value1 here, (example is iq(#ab)+IE("cd") above),
                // using this freq and apropriate SuffixEstimation[somewhere].
                value1 = iq(freq) + suffixEstimation[end];
                if (value > value1)
                    value = value1; // compute minimum of value1,
            }
            if (debugMode) {
                System.out.println("suffixEstimation[" + n + "] = " + value);
            }
            suffixEstimation[n] = value;
        }
        return suffixEstimation[0];

    }
    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        debugMode = true;
        double eps = 0.0001;
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        if (Math.abs(value - 2) <= eps) {
            System.out.println("AC");
        } else {
            System.err.println("WA value: " + value);
        }

        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        if (Math.abs(value - 3) <= eps) {
            System.out.println("AC");
        } else {
            System.err.println("WA value: " + value);
        }
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        if (Math.abs(value - 3) <= eps) {
            System.out.println("AC");
        } else {
            System.err.println("WA value: " + value);
        }
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
        if (Math.abs(value - 4) <= eps) {
            System.out.println("AC");
        } else {
            System.err.println("WA value: " + value);
        }
    }
}

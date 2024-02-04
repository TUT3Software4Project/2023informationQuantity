package s4.B233375; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 


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

public class InformationEstimatorRefactor implements InformationEstimatorInterface {
	static boolean debugMode = false;
	boolean targetReady = false;
	boolean spaceReady = false;
	private int targetLength;
	byte[] target; // data to compute its information quantity
	byte[] space; // Sample space to compute the probability
	FrequencerInterface myFrequencer; // Object for counting frequency

	private void showVariables() {
		for (int i = 0; i < space.length; i++) {
			System.out.write(space[i]);
		}
		System.out.write(' ');
		for (int i = 0; i < targetLength; i++) {
			System.out.write(target[i]);
		}
		System.out.write(' ');
	}

	/**
	 * 情報量を求める
	 * 
	 * @param freq space中に対象の文字列が現れた回数
	 * @return 情報量 = -log2(freq / space.length)
	 */
	double iq(int freq) {
		return -Math.log10((double) freq / (double) space.length) / Math.log10((double) 2.0);
	}

	public void setTarget(byte[] target) {
		this.target = target;
		targetLength = target.length;
		if (targetLength > 0)
			targetReady = true;
	}

	public void setSpace(byte[] space) {
		myFrequencer = new Frequencer();
		this.space = space;
		myFrequencer.setSpace(space);
		spaceReady = true;
	}

	public double estimation() {

		if (targetReady == false)
			return (double) 0.0;
		if (spaceReady == false)
			return Double.MAX_VALUE;
		if (targetLength == 0) {
			return (double) 0.0; // Is it needed?
		}
		myFrequencer.setTarget(target);

		// suffixEstimation[i] -> myTarget[i,length)をtargetとしたときに情報量を最小限に分割したときの最小値
		// init : suffixEstimation[length]= 0 (targetが空のときは情報量が0となる)
		// trans: suffixEstimation[i] = min(j=i to length)(suffixEstimation[j] +iq[i,j))
		// find : suffixEstimation[0]
		double[] suffixEstimation = new double[targetLength + 1];

		for (int i = 0; i < targetLength; i++) {
			suffixEstimation[i] = Double.MAX_VALUE;
		}

		suffixEstimation[targetLength] = (double) 0.0;// 空文字列の情報量

		for (int n = targetLength - 1; n >= 0; n--) {
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
			double value_min = Double.MAX_VALUE; // suffixEstimation[n] の暫定の値
			double value_candidate = Double.MAX_VALUE; // suffixEstimation[n]の候補値
			int start = n;
			for (int end = n + 1; end <= targetLength; end++) {
				int freq = myFrequencer.subByteFrequency(start, end);
				if (freq == 0) {// この時点で他の値もMAX_VALUEになるはず
					return Double.MAX_VALUE;
				}
				value_candidate = iq(freq) + suffixEstimation[end];
				if (value_min > value_candidate)
					value_min = value_candidate; // compute minimum of value1,
			}
			if (debugMode) {
				System.out.println("suffixEstimation[" + n + "] = " + value_min);
			}
			suffixEstimation[n] = value_min;
		}
		return suffixEstimation[0];

	}

	/**
	 * InformationEstimatorのテストを行う
	 * 
	 * @param space  入力するspace
	 * @param target 入力するtarget
	 * @param except 予測する値
	 */
	private static void testInformationEstimation(String space, String target, double except) {
		double eps = 0.0001;
		InformationEstimatorInterface tester = new InformationEstimatorRefactor();
		tester.setSpace(space.getBytes());
		tester.setTarget(target.getBytes());

		double value = tester.estimation();
		if (Math.abs(value - except) <= eps) {
			System.out.println("AC");
		} else {
			System.err.println("WA value: " + value);
		}
	}

	public static void main(String[] args) {

		debugMode = true;
		testInformationEstimation("3210321001230123", "0", 2.0);
		testInformationEstimation("3210321001230123", "01", 3.0);
		testInformationEstimation("3210321001230123", "0123", 3.0);
		testInformationEstimation("3210321001230123", "00", 4.0);

	}
}

package s4.B233375; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
	static String str = "";

	public static void main(String[] args) {

		// ファイルの読み込み
		String space1 = "", spacer1 = "", target10 = "", target16 = "", target22 = "";
		try {
			space1 = GetStringFromFile("../data/space_1k.txt");
			spacer1 = GetStringFromFile("../data/rand_1k.txt");
			target10 = GetStringFromFile("../data/target_10b.txt");
			target16 = GetStringFromFile("../data/target_16b.txt");
			target22 = GetStringFromFile("../data/target_22b.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FrequencerInterface myObject;
			int freq;
			System.out.println("checking Frequencer");

			// This is smoke test
			myObject = new Frequencer();
			myObject.setSpace("Hi Ho Hi Ho".getBytes());
			myObject.setTarget("H".getBytes());
			freq = myObject.frequency();
			assert freq == 4 : "Hi Ho Hi Ho, H: " + freq;
			// Write your testCase here
			testFrequencer("abc", "a", 1);
			testFrequencer("ssss", "ss", 3);
			testFrequencer("apple", "le", 1);
			testFrequencer("apple", "apple", 1);
			testFrequencer("play", "player", 0);
			testFrequencer("", "abc", 0);
			testFrequencer("abc", "", -1);
			testFrequencer("aaa", "a", 3);
			// Test Case 5: Target not set
			{
				Frequencer noTarget = new Frequencer();
				noTarget.setSpace("Hi Ho Hi Ho".getBytes());
				freq = noTarget.frequency();
				assert freq == -1 : "Test Case 5 Failed: Target not set";
			}
			{
				// Test Case 6: Space not set
				Frequencer noSpace = new Frequencer();
				noSpace.setTarget("H".getBytes());
				freq = noSpace.frequency();
				assert freq == 0 : "Test Case 6 Failed: Space not set";
			}
			// sub byteに対するテスト
			testFrequencerWithRange("3210321001230123", "3210321001230123", 0, 16, 1);// 全範囲
			testFrequencerWithRange("3210321001230123", "3210321001230123", 3, 4, 4);// 範囲が1
			testFrequencerWithRange("3210321001230123", "3210321001230123", 3, 5, 1);// 範囲が2
			testFrequencerWithRange("3210321001230123", "3210321001230123", 1, 3, 2);// 範囲が2
			// slow/TestCaseから拝借
			testFrequencer("AAA", "", -1);
			testFrequencer("", "A", 0);
			testFrequencer("AAA", "A", 3);
			testFrequencer("AAA", "AA", 2);
			testFrequencer("AAA", "AAA", 1);
			testFrequencer("AAA", "AAAA", 0);
			testFrequencerWithRange("AAAB", "AAAAB", 0, 1, 3);
			testFrequencerWithRange("AAAB", "AAAAB", 1, 2, 3);
			testFrequencerWithRange("AAAB", "AAAAB", 1, 3, 2);
			testFrequencerWithRange("AAAB", "AAAAB", 4, 5, 1);
			// 愚直ケースとの比較
			testFrequencerWithDiff("AAA", "");
			testFrequencerWithDiff("", "A");
			testFrequencerWithDiff("AAA", "A");
			testFrequencerWithDiff("AAA", "AA");
			testFrequencerWithDiff("AAA", "AAA");
			testFrequencerWithDiff("AAA", "AAAA");

			// ファイルとの比較
			// space1="",spacer1,target10,target16,target22;
			
			testFrequencerWithDiff(space1, target10);
			testFrequencerWithDiff(space1, target16);
			testFrequencerWithDiff(space1, target22);
	

			testFrequencerWithDiff(spacer1, target10);
			testFrequencerWithDiff(spacer1, target16);
			testFrequencerWithDiff(spacer1, target22);
			testFrequencerWithDiff(space1, space1);
		} catch (Exception e) {
			e.printStackTrace();
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
			assert (value > 1.9999) && (2.0001 > value)
					: "IQ for 0 in 3210321001230123 should be 2.0. But it returns " + value;
			myObject.setTarget("01".getBytes());

			value = myObject.estimation();
			assert (value > 2.9999) && (3.0001 > value)
					: "IQ for 01 in 3210321001230123 should be 3.0. But it returns " + value;
			myObject.setTarget("0123".getBytes());

			value = myObject.estimation();
			assert (value > 2.9999) && (3.0001 > value)
					: "IQ for 0123 in 3210321001230123 should be 3.0. But it returns " + value;
			myObject.setTarget("00".getBytes());

			value = myObject.estimation();

			assert (value > 3.9999) && (4.0001 > value)
					: "IQ for 00 in 3210321001230123 should be 3.0. But it returns " + value;
			// spaceが未設定時
			{
				InformationEstimatorInterface noSpace = new InformationEstimator();
				// myObject.setSpace("3210321001230123".getBytes());
				noSpace.setTarget("0".getBytes());
				double iq = noSpace.estimation();
				assert (iq == Double.MAX_VALUE) : "Test Case Failed: Space not set";
			}
			// space長が0
			{
				InformationEstimatorInterface zeroSpace = new InformationEstimator();
				zeroSpace.setSpace("".getBytes());
				zeroSpace.setTarget("0".getBytes());
				double iq = zeroSpace.estimation();
			}
			// targetが未設定時
			{
				InformationEstimatorInterface noTarget = new InformationEstimator();
				noTarget.setSpace("3210321001230123".getBytes());
				double iq = noTarget.estimation();
				assert (iq > -0.0001) && (0.0001 > iq) : "Test Case Failed: target not set";
			}
			testEstimation("a", "", 0.0);// target length ==0
			testEstimation("abc", "def", Double.MAX_VALUE);

			// ファイルとの比較
			// space1="",spacer1,target10,target16,target22;
			testEstimationWithDiff(space1, target10);
			testEstimationWithDiff(space1, target16);

			testEstimationWithDiff(spacer1, target10);
			testEstimationWithDiff(spacer1, target16);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occurred in InformationEstimator Object");
			success = false;
		}
		if (success) {
			System.out.println("TestCase OK");
		}
	}

	private static String GetStringFromFile(String path) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
		String str = reader.readLine();
		reader.close();
		return str;
	}

	public static void testFrequencer(String space, String target, int excepted)
			throws Exception {
		FrequencerInterface executer = new Frequencer();
		executer.setSpace(space.getBytes());
		executer.setTarget(target.getBytes());
		int freq = executer.frequency();
		assert freq == excepted : space + ", " + target + ": " + freq;

	}

	public static void testFrequencerWithRange(String space, String target, int start, int end, int excepted)
			throws Exception {
		FrequencerInterface executer = new Frequencer();
		executer.setSpace(space.getBytes());
		executer.setTarget(target.getBytes());
		int freq = executer.subByteFrequency(start, end);
		assert freq == excepted : space + ", with [" + start + "," + end + ") , " + target + " : " + freq;

	}

	public static void testEstimation(String space, String target, double excepted)
			throws Exception {
		double esp = 0.0001;// 許容誤差
		InformationEstimatorInterface estimator = new InformationEstimator();
		estimator.setSpace(space.getBytes());
		estimator.setTarget(target.getBytes());
		double iq = estimator.estimation();
		assert (excepted - esp <= iq && iq <= excepted + esp)
				: "IQ for " + target + " in " + space + " should be " + excepted + ". But it returns " + iq;
	}

	public static void testFrequencerWithDiff(String space, String target)
			throws Exception {
		FrequencerInterface executerBase = new FrequencerNaive();
		FrequencerInterface test = new Frequencer();
		executerBase.setSpace(space.getBytes());
		executerBase.setTarget(target.getBytes());
		test.setSpace(space.getBytes());
		test.setTarget(target.getBytes());
		int freq = executerBase.frequency();
		int res = test.frequency();
		assert freq == res : space + ", " + target + ": " + res;

	}

	public static void testEstimationWithDiff(String space, String target)
			throws Exception {
		double esp = 0.0001;// 許容誤差
		InformationEstimatorInterface estimatorBase = new InformationEstimatorNaive();
		InformationEstimatorInterface test = new InformationEstimator();
		estimatorBase.setSpace(space.getBytes());
		estimatorBase.setTarget(target.getBytes());
		test.setSpace(space.getBytes());
		test.setTarget(target.getBytes());
		double iq = estimatorBase.estimation();
		double res = test.estimation();
		assert (res - esp <= iq && iq <= res + esp)
				: "IQ for " + target + " in " + space + " should be " + res + ". But it returns " + iq;
	}
}

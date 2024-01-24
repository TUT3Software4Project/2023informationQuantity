package s4.B233375; // ここは、かならず、自分の名前に変えよ。

import java.lang.*;
import s4.specification.*;

/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/

public class FrequencerWithMarge implements FrequencerInterface {
	// Code to start with: This code is not working, but good start point to work.
	byte[] myTarget;
	byte[] mySpace;
	boolean targetReady = false;
	boolean spaceReady = false;
	private int spaceLength = 0;
	int[] suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。
	// 辞書順の早いものを最初に持っていく

	// The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
	// Each suffix is expressed by a integer, which is the starting position in
	// mySpace.

	// The following is the code to print the contents of suffixArray.
	// This code could be used on debugging.

	// この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
	// リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
	//
	private void printSuffixArray() {
		if (spaceReady) {
			for (int i = 0; i < mySpace.length; i++) {
				int s = suffixArray[i];
				System.out.printf("suffixArray[%2d]=%2d:", i, s);
				for (int j = s; j < mySpace.length; j++) {
					System.out.write(mySpace[j]);
				}
				System.out.write('\n');
			}
		}
	}

	/**
	 * 
	 * comparing two suffixes by dictionary order.
	 * suffix_i is a string starting with the position i in "byte [] mySpace".
	 * When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
	 * suffix_2 is "CD", and sufffix_3 is "D".
	 * Each i and j denote suffix_i, and suffix_j.
	 * Example of dictionary order
	 * "i" < "o" : compare by code
	 * "Hi" < "Ho" ; if head is same, compare the next element
	 * "Ho" < "Ho " ; if the prefix is identical, longer string is big
	 * 
	 * // The return value of "int suffixCompare" is as follows.
	 * 
	 * @return 1 if suffix_i > suffix_j
	 *         -1 if suffix_i < suffix_j
	 *         0 if suffix_i = suffix_j
	 */
	private int suffixCompare(int i, int j) {

		// suffixCompareはソートのための比較メソッドである。
		// 次のように定義せよ。
		//
		// comparing two suffixes by dictionary order.
		// suffix_i is a string starting with the position i in "byte [] mySpace".
		// When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
		// suffix_2 is "CD", and sufffix_3 is "D".
		// Each i and j denote suffix_i, and suffix_j.
		// Example of dictionary order
		// "i" < "o" : compare by code
		// "Hi" < "Ho" ; if head is same, compare the next element
		// "Ho" < "Ho " ; if the prefix is identical, longer string is big
		//
		// The return value of "int suffixCompare" is as follows.
		// if suffix_i > suffix_j, it returns 1
		// if suffix_i < suffix_j, it returns -1
		// if suffix_i = suffix_j, it returns 0;

		// ここにコードを記述せよ
		//
		int short_letter = i < j ? j : i;// 文字数が短い文字
		int res = 0;// 比較結果
		int in_current = 0;// 現在見ている文字

		while (res == 0) {
			// 範囲かどうか見る
			if (short_letter + in_current >= spaceLength) {// 表示範囲を超える
				res = j - i;
				break;
			}
			// 大小比較
			res = mySpace[i + in_current] - mySpace[j + in_current];
			++in_current;
		}

		if (res > 0) {
			// System.err.printf("%d > %d\n", i, j);
			return 1;
		} else if (res < 0) {
			// System.err.printf("%d < %d\n", i, j);
			return -1;
		}
		// System.err.printf("%d = %d\n", i, j);
		return 0; // この行は変更しなければいけない。
	}

	public void setSpace(byte[] space) {
		// suffixArrayの前処理は、setSpaceで定義せよ。
		mySpace = space;
		spaceLength = mySpace.length;
		if (spaceLength > 0)
			spaceReady = true;
		// First, create unsorted suffix array.
		suffixArray = new int[spaceLength];
		// put all suffixes in suffixArray.
		for (int i = 0; i < spaceLength; i++) {
			suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
		}
		//
		// ここに、int suffixArrayをソートするコードを書け。
		// もし、mySpace が"ABC"ならば、
		// suffixArray = { 0, 1, 2} となること求められる。
		// このとき、printSuffixArrayを実行すると
		// suffixArray[ 0]= 0:ABC
		// suffixArray[ 1]= 1:BC
		// suffixArray[ 2]= 2:C
		// のようになるべきである。
		// もし、mySpace が"CBA"ならば
		// suffixArray = { 2, 1, 0} となることが求めらる。
		// このとき、printSuffixArrayを実行すると
		// suffixArray[ 0]= 2:A
		// suffixArray[ 1]= 1:BA
		// suffixArray[ 2]= 0:CBA
		// のようになるべきである。

		// sort by マージソート
		/// 辞書順は広義単調増加(1が出ないようにする)
		MargeSort(0, spaceLength);
	}

	/**
	 * 半開区間[left,right)をマージソートでマージを行う
	 */
	private void MargeSort(int left, int right) {
		if (left + 1 >= right) {// 幅が1以下
			return;
		}
		if (left + 2 == right) {// 幅が2
			if (suffixCompare(left, left + 1) == 1) {// swap
				int tmpValue1 = suffixArray[left];
				int tmpValue2 = suffixArray[left + 1];
				suffixArray[left] = tmpValue2;
				suffixArray[left + 1] = tmpValue1;
			}
			return;
		}
		int length = right - left;
		int halfLength = length / 2;
		int middle = left + halfLength;

		MargeSort(left, middle);// 前半
		MargeSort(middle, right);// 後半
	
		// 一旦退避
		int tmp[] = new int[length];
		int count = 0;
		for (int i = left; i < right; ++i) {
			tmp[count++] = this.suffixArray[i];
		}

		int leftAt = 0, rightAt = halfLength;
		count = left;

		while (leftAt < halfLength && rightAt < length) {
		
			if (suffixCompare(tmp[leftAt], tmp[rightAt]) == 1) {
				suffixArray[count] = tmp[rightAt++];
				tmp[rightAt - 1] = -1;
			} else {
				suffixArray[count] = tmp[leftAt++];
				tmp[leftAt - 1] = -1;
			}
			++count;
		}
		while (leftAt < halfLength) {

			suffixArray[count] = tmp[leftAt++];

			tmp[leftAt - 1] = -1;
			++count;
		}
		while (rightAt < length) {
			suffixArray[count] = tmp[rightAt++];

			++count;
			tmp[rightAt - 1] = -1;
		}

	}
	// ここから始まり、指定する範囲までは変更してはならないコードである。

	public void setTarget(byte[] target) {
		myTarget = target;
		if (myTarget.length > 0)
			targetReady = true;
	}

	public int frequency() {
		if (targetReady == false)
			return -1;
		if (spaceReady == false)
			return 0;
		return subByteFrequency(0, myTarget.length);
	}

	public int subByteFrequency(int start, int end) {
		// start, and end specify a string to search in myTarget,
		// if myTarget is "ABCD",
		// start=0, and end=1 means string "A".
		// start=1, and end=3 means string "BC".
		// This method returns how many the string appears in my Space.
		//
		/*
		 * This method should be work as follows, but much more efficient.
		 * int spaceLength = mySpace.length;
		 * int count = 0;
		 * for(int offset = 0; offset< spaceLength - (end - start); offset++) {
		 * boolean abort = false;
		 * for(int i = 0; i< (end - start); i++) {
		 * if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
		 * }
		 * if(abort == false) { count++; }
		 * }
		 */
		// The following the counting method using suffix array.
		// 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
		int first = subByteStartIndex(start, end);
		int last1 = subByteEndIndex(start, end);
		return last1 - first;// 半開区間で定義すれば良さそう
	}
	// 変更してはいけないコードはここまで。

	private int targetCompare(int i, int j, int k) {
		// subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
		// 次のように定義せよ。
		// suffix_i is a string starting with the position i in "byte [] mySpace".:
		// mySpace[i,mySpace.length)
		// When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
		// suffix_2 is "CD", and sufffix_3 is "D".
		// target_j_k is a string in myTarget start at j-th postion ending k-th
		// position. : myTarget[j,k)
		// if myTarget is "ABCD",
		// j=0, and k=1 means that target_j_k is "A".
		// j=1, and k=3 means that target_j_k is "BC".
		// This method compares suffix_i and target_j_k.
		// if the beginning of suffix_i matches target_j_k, it return 0.
		// if suffix_i > target_j_k it return 1;
		// if suffix_i < target_j_k it return -1;
		// if first part of suffix_i is equal to target_j_k, it returns 0;
		//
		// Example of search
		// suffix target
		// "o" > "i"
		// "o" < "z"
		// "o" = "o"
		// "o" < "oo"
		// "Ho" > "Hi"
		// "Ho" < "Hz"
		// "Ho" = "Ho"
		// "Ho" < "Ho " : "Ho " is not in the head of suffix "Ho"
		// "Ho" = "H" : "H" is in the head of suffix "Ho"
		// The behavior is different from suffixCompare on this case.
		// For example,
		// if suffix_i is "Ho Hi Ho", and target_j_k is "Ho",
		// targetCompare should return 0;
		// if suffix_i is "Ho Hi Ho", and suffix_j is "Ho",
		// suffixCompare should return 1. (It was written -1 before 2021/12/21)
		//
		// ここに比較のコードを書け
		//
		// サイズをtargetの長さに制限して、文字列比較?
		int comp_targetLength = k - j;
		int comp_spaceLength = spaceLength - suffixArray[i];
		int res = 0;// 比較結果
		int in_current = 0;// 現在見ている文字
		int short_letter_length = comp_targetLength < comp_spaceLength ? comp_targetLength : comp_spaceLength;// 文字数が短い方の文字数
		while (res == 0) {
			if (in_current >= short_letter_length) {// どちらかの最終文字を超えた時
				if (comp_spaceLength < comp_targetLength) {
					return -1;
				}
				return 0;
			}
			res = mySpace[suffixArray[i] + in_current] - myTarget[j + in_current];

			++in_current;
		}
		if (res > 0) {
			return 1;
		}
		return -1; // この行は変更しなければならない。
	}

	private int subByteStartIndex(int start, int end) {
		// suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド=lowerbound
		// 以下のように定義せよ。
		// The meaning of start and end is the same as subByteFrequency.
		/*
		 * Example of suffix created from "Hi Ho Hi Ho"
		 * 0: Hi Ho
		 * 1: Ho
		 * 2: Ho Hi Ho
		 * 3:Hi Ho
		 * 4:Hi Ho Hi Ho
		 * 5:Ho
		 * 6:Ho Hi Ho
		 * 7:i Ho
		 * 8:i Ho Hi Ho
		 * 9:o
		 * 10:o Hi Ho
		 */

		// It returns the index of the first suffix
		// which is equal or greater than target_start_end.
		// Suppose target is set "Ho Ho Ho Ho"
		// if start = 0, and end = 2, target_start_end is "Ho".
		// if start = 0, and end = 3, target_start_end is "Ho ".
		// Assuming the suffix array is created from "Hi Ho Hi Ho",
		// if target_start_end is "Ho", it will return 5.
		// Assuming the suffix array is created from "Hi Ho Hi Ho",
		// if target_start_end is "Ho ", it will return 6.
		//
		// ここにコードを記述せよ。
		//
		int res;
		for (res = 0; res < spaceLength && (targetCompare(res, start, end) == -1); ++res) {

		}
		return res; // このコードは変更しなければならない。
	}

	private int subByteEndIndex(int start, int end) {
		// suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド=upper bound
		// 以下のように定義せよ。
		// The meaning of start and end is the same as subByteFrequency.
		/*
		 * Example of suffix created from "Hi Ho Hi Ho"
		 * 0: Hi Ho
		 * 1: Ho
		 * 2: Ho Hi Ho
		 * 3:Hi Ho
		 * 4:Hi Ho Hi Ho
		 * 5:Ho
		 * 6:Ho Hi Ho
		 * 7:i Ho
		 * 8:i Ho Hi Ho
		 * 9:o
		 * 10:o Hi Ho
		 */
		// It returns the index of the first suffix
		// which is greater than target_start_end; (and not equal to target_start_end)
		// Suppose target is set "High_and_Low",
		// if start = 0, and end = 2, target_start_end is "Hi".
		// if start = 1, and end = 2, target_start_end is "i".
		// Assuming the suffix array is created from "Hi Ho Hi Ho",
		// if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
		// Assuming the suffix array is created from "Hi Ho Hi Ho",
		// if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
		//
		int res;
		for (res = spaceLength - 1; res >= 0 && (targetCompare(res, start, end) == 1); --res) {

		}
		return res + 1; // このコードは変更しなければならない。
	}

	// Suffix Arrayを使ったプログラムのホワイトテストは、
	// privateなメソッドとフィールドをアクセスすることが必要なので、
	// クラスに属するstatic mainに書く方法もある。
	// static mainがあっても、呼びださなければよい。
	// 以下は、自由に変更して実験すること。
	// 注意：標準出力、エラー出力にメッセージを出すことは、
	// static mainからの実行のときだけに許される。
	// 外部からFrequencerを使うときにメッセージを出力してはならない。
	// 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
	// 減点の対象である。
	public static void main(String[] args) {
		FrequencerWithMarge frequencerObject;
		try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
			frequencerObject = new FrequencerWithMarge();
			frequencerObject.setSpace("ABC".getBytes());
			frequencerObject.printSuffixArray();
			// test for suffix array of ABC (ABC , BC ,A -> 0,1,2)
			int[] expect_suffix_abc = { 0, 1, 2 };
			for (int i = 0; i < frequencerObject.suffixArray.length; ++i) {
				System.err.printf("suffix[%d] : (except %d , actually %d) :", i, expect_suffix_abc[i],
						frequencerObject.suffixArray[i]);
				if (expect_suffix_abc[i] != frequencerObject.suffixArray[i]) {
					System.err.println("ng");
				} else {
					System.err.println("ok");
				}
			}
			frequencerObject = new FrequencerWithMarge();
			frequencerObject.setSpace("CBA".getBytes());
			frequencerObject.printSuffixArray();
			int[] expect_suffix_cba = { 2, 1, 0 };// A BA CBA
			for (int i = 0; i < frequencerObject.suffixArray.length; ++i) {
				System.err.printf("suffix[%d] : (except %d , actually %d) :", i, expect_suffix_cba[i],
						frequencerObject.suffixArray[i]);
				if (expect_suffix_cba[i] != frequencerObject.suffixArray[i]) {
					System.err.println("ng");
				} else {
					System.err.println("ok");
				}
			}
			frequencerObject = new FrequencerWithMarge();
			frequencerObject.setSpace("HHH".getBytes());
			frequencerObject.printSuffixArray();
			int[] expect_suffix_hhh = { 2, 1, 0 };// H HH HHH
			for (int i = 0; i < frequencerObject.suffixArray.length; ++i) {
				System.err.printf("suffix[%d] : (except %d , actually %d) :", i, expect_suffix_hhh[i],
						frequencerObject.suffixArray[i]);
				if (expect_suffix_hhh[i] != frequencerObject.suffixArray[i]) {
					System.err.println("ng");
				} else {
					System.err.println("ok");
				}
			}
			System.out.println();
			frequencerObject = new FrequencerWithMarge();
			frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
			frequencerObject.printSuffixArray();
			/*
			 * Example from "Hi Ho Hi Ho"
			 * 0: Hi Ho
			 * 1: Ho
			 * 2: Ho Hi Ho
			 * 3:Hi Ho
			 * 4:Hi Ho Hi Ho
			 * 5:Ho
			 * 6:Ho Hi Ho
			 * 7:i Ho
			 * 8:i Ho Hi Ho
			 * 9:o
			 * 10:o Hi Ho
			 */ int[] expect_suffix_long = { 5, 8, 2, 6, 0, 9, 3, 7, 1, 10, 4 };// H HH HHH
			for (int i = 0; i < frequencerObject.suffixArray.length; ++i) {
				System.err.printf("suffix[%d] : (except %d , actually %d) :", i, expect_suffix_long[i],
						frequencerObject.suffixArray[i]);
				if (expect_suffix_long[i] != frequencerObject.suffixArray[i]) {
					System.err.println("ng");
				} else {
					System.err.println("ok");
				}
			}
			// 1文字でのテスト
			frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
			frequencerObject.printSuffixArray();
			frequencerObject.setTarget("H".getBytes());
			int result = frequencerObject.frequency();
			int except_value = 4;
			System.out.print(frequencerObject.myTarget.toString() + " in " + frequencerObject.mySpace.toString()
					+ " : Freq = " + result + " ");
			if (except_value == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			// 2文字でのテスト
			frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
			System.out.println("space length:" + frequencerObject.mySpace.length);
			frequencerObject.printSuffixArray();
			frequencerObject.setTarget("Hi".getBytes());
			result = frequencerObject.frequency();
			except_value = 2;
			System.out.print("Freq = " + result + " ");
			if (except_value == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG ,value=" + result);
			}
			// 探索地点が終点を超えるときのテスト
			frequencerObject.setTarget("z".getBytes());
			result = frequencerObject.frequency();
			except_value = 0;
			System.out.print("Freq = " + result + " ");
			if ((except_value == result) && (frequencerObject.subByteEndIndex(0,
					frequencerObject.mySpace.length) == frequencerObject.mySpace.length)) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG value : " + result + " end pos : " + frequencerObject.subByteEndIndex(0,
						frequencerObject.mySpace.length));
			}
			// 探索位置が始点のときのテスト
			frequencerObject.setSpace("hello".getBytes());
			frequencerObject.printSuffixArray();
			frequencerObject.setTarget("a".getBytes());
			result = frequencerObject.frequency();
			except_value = 0;
			System.out.print("Freq = " + result + " ");
			if ((except_value == result) && (frequencerObject.subByteStartIndex(0,
					frequencerObject.mySpace.length) == 0)) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("STOP");
		}
	}
}

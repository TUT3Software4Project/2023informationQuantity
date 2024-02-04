package s4.B233375; // ここは、かならず、自分の名前に変えよ。

import java.lang.*;
import s4.specification.*;

/*package s4.specification;
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

public class FrequencerRefactor implements FrequencerInterface {
	byte[] target;
	byte[] space;
	boolean targetReady = false;
	boolean spaceReady = false;
	private int spaceLength = 0;
	/// spaceの接尾語を辞書順が早い順に並べた配列(要素は開始場所のインデックス)
	int[] suffixArray;

	/**
	 * SuffixArrayの内容を標準出力に出力する
	 */
	private void printSuffixArray() {
		if (spaceReady) {
			for (int i = 0; i < spaceLength; i++) {
				int s = suffixArray[i];
				System.out.printf("suffixArray[%2d]=%2d:", i, s);
				for (int j = s; j < spaceLength; j++) {
					System.out.write(this.space[j]);
				}
				System.out.write('\n');
			}
		}
	}

	/**
	 * 2つの接尾文字列 S_i , S_jを辞書順に比較する
	 * ここで、S_iとはSpace Sのi番目(0-index)から始まる連続した文字列のことである
	 * e.g. S = "ABCD"のとき、 S_0 = "ABCD" , S_1 = "BCD" , S_2 = "CD" S_3 = "D" となる
	 * 文字列の比較は以下の順で決まる
	 * <ol>
	 * <li>先頭のアルファベット順 e.g. "a" < "b"
	 * <li>先頭のアルファベットと同じの時は次の文字列 e.g. "aa" < "ab"
	 * <li>一方の文字列がもう一方の接頭辞のときは文字列が長い方 e.g. "aa" < "aab" , "ab" = "ab"
	 * </ol>
	 * 
	 * @param i 比較する文字列1 (S="abcd", i=0 のとき、 S_i = "abcd")
	 * @param j 比較する文字列2 (S="abcd", j=2 のとき、 S_j = "cd")
	 * @return 大小比較の結果, S_i > S_j のとき、1以上の値を返し、S_i < S_j のとき、-1以下の値を返す S_i = S_j
	 *         のとき、0を返す
	 */
	private int suffixCompare(int i, int j) {

		int short_letter = i < j ? j : i;// 文字数が短い文字
		int comp = 0;// 比較結果
		int in_current = 0;// 現在見ている文字

		while (comp == 0) {// アルファベットが異なるまで繰り返す
			// 範囲内かどうか見る
			if (short_letter + in_current >= spaceLength) {// 表示範囲を超える
				comp = j - i;
				return comp;// 文字が長い方=開始インデックスが早い方を1とする
			}
			// 大小比較
			comp = this.space[i + in_current] - this.space[j + in_current];
			++in_current;
		}
		return comp;
	}

	public void setSpace(byte[] space) {
		// spaceに関する変数の定義
		this.space = space;
		spaceLength = this.space.length;
		if (spaceLength > 0)
			spaceReady = true;

		// suffixArrayの作成
		suffixArray = new int[spaceLength];
		// suffixArrayの初期化
		for (int i = 0; i < spaceLength; i++) {
			suffixArray[i] = i;
		}
		// suffixArrayをソートする
		MargeSort(0, spaceLength);
	}

	/**
	 * suffixArrayの半開区間[left, right)をマージソートによってソートする
	 * left = 4 , right = 7 の時, [4,7) = 4,5,6の要素についてソートを行う(終点は引数-1になることに注意)
	 * 
	 * @param left  区間の始点
	 * @param right 区間の終点
	 */
	private void MargeSort(int left, int right) {
		if (left + 1 >= right) {// 幅が1以下
			return;
		}
		if (left + 2 == right) {// 幅が2
			if (suffixCompare(left, left + 1) > 0) {// swap
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

		MargeSort(left, middle);// 前半をソートする
		MargeSort(middle, right);// 後半をソートする

		// 一旦退避
		int tmp[] = new int[length];
		int count = 0;
		for (int i = left; i < right; ++i) {
			tmp[count++] = this.suffixArray[i];
		}

		int leftAt = 0, rightAt = halfLength;
		count = left;

		while (leftAt < halfLength && rightAt < length) {// どちらかが行き切るまで繰り返す
			if (suffixCompare(tmp[leftAt], tmp[rightAt]) > 0) {
				suffixArray[count] = tmp[rightAt++];
			} else {
				suffixArray[count] = tmp[leftAt++];
			}
			++count;
		}
		// この時点で左右どちらかが行き切るはず
		while (leftAt < halfLength) {// 左側をすべて行き切るまで
			suffixArray[count] = tmp[leftAt++];
			++count;
		}
		while (rightAt < length) {// 右側を行き切るまで
			suffixArray[count] = tmp[rightAt++];
			++count;
		}

	}
	// ここから始まり、指定する範囲までは変更してはならないコードである。

	public void setTarget(byte[] target) {
		this.target = target;
		if (this.target.length > 0)
			targetReady = true;
	}

	public int frequency() {
		if (targetReady == false)
			return -1;
		if (spaceReady == false)
			return 0;
		return subByteFrequency(0, this.target.length);
	}

	public int subByteFrequency(int start, int end) {

		return upperBound(start, end) - lowerBound(start, end);
	}
	// 変更してはいけないコードはここまで。

	/**
	 * 文字列 Space_i target[j ,k) を辞書順に比較を行う ,
	 * ここで Space_i はSpaceのi番目(0始まり)から始まる接尾語 (space = "abcd", i= 1 ならば space_i =
	 * "bcd" , spaceは前と同じでi=0 ならば space_i = "abcd")
	 * target[j,k)はtargetのj番目から始まり、k-1番目に終わる(境界を含む)の連続部分文字列を指す(値はいずれも0-indexed)
	 * (target = "efghi" , j=1 , k= 4 ならば、 target[j,k] = "fgh" , targetは同じで j=3 k=4
	 * ならば target[j,k) = "h")
	 * 
	 * 比較は次の順序で行われる
	 * <ol>
	 * <li>先頭の文字のアルファベット順 e.g. "a" < "b"
	 * <li>先頭の文字が同じならば次の文字のアルファベット順 e.g. "aa" < "ab"
	 * <li>一方の文字列がもう一方の接頭辞のとき
	 * <ul>
	 * <li>space_i の長さ < target[j,k)の長さ のとき、 space_i < target[j,k) e.g. "a" < "ab"
	 * <li>space_i の長さ >= target[j,k)の長さ のとき、 space_i = target[j,k) e.g. "ab" = "a"
	 * </ul>
	 * </ol>
	 * 
	 * @param i space の接尾詞の開始地点
	 * @param j targetの連続部分文字列の始点
	 * @param k targetの連続部分文字列の終点
	 * @return 大小比較の結果, space_i > target[j,k) のとき、1以上の値を返し、space_i > target[j,k)
	 *         のとき、-1以下の値を返す space_i > target[j,k) のとき、0を返す
	 */
	private int targetCompare(int i, int j, int k) {

		int comp_targetLength = k - j;
		int comp_spaceLength = spaceLength - suffixArray[i];
		int comp = 0;// 比較結果
		int in_current = 0;// 現在見ている文字
		int short_letter_length = comp_targetLength < comp_spaceLength ? comp_targetLength : comp_spaceLength;// 文字数が短い方の文字数
		while (comp == 0) {
			if (in_current >= short_letter_length) {// どちらかの最終文字を超えた時
				if (comp_spaceLength < comp_targetLength) {
					return -1;
				}
				return 0;
			}
			comp = this.space[suffixArray[i] + in_current] - this.target[j + in_current];

			++in_current;
		}
		return comp;
	}

	/**
	 * suffix arrayの中で、辞書順でspace[start, end)と同じか、それよりも前に出るインデックスのうち、最も小さな値を返す
	 * 
	 * @param start spaceの連続部分文字列の開始地点
	 * @param end   spaceの連続部分文字列の終点
	 * @return suffix arrayの中で、辞書順でspace[start, end)と同じか、それよりも前に出るインデックス(0-index)
	 */
	private int lowerBound(int start, int end) {

		int left = 0, right = spaceLength, middle;
		if (right - left <= 0) { // 範囲が1以下のときは左端を返す
			return left;
		}
		if (targetCompare(left, start, end) >= 0) {// 右端は明らかに対象よりも後に来るので、左端が対象よりも先に来ることを確認
			return left;// もし、対象と同じか、その後に来るなら、全部対象の後に来ることが分かるので、左端を返す
		}

		int comp;
		do {
			// 左端は対象よりも前に、右端は対象と同じか後ろに来るように持つ
			middle = left + (right - left) / 2;
			comp = targetCompare(middle, start, end);
			if (comp < 0) {// 中央が対象より先に来るか、後に来るかで場合分け
				left = middle;
			} else {
				right = middle;
			}

		} while (right - left >= 2);// 幅が最小になるまで繰り返す
		return right; // 右端は対象と同じかその後に来る
	}

	/**
	 * suffix arrayの中で、辞書順でspace[start, end)よりも後ろに出るインデックスのうち、最も小さな値を返す
	 * 
	 * @param start spaceの連続部分文字列の開始地点
	 * @param end   spaceの連続部分文字列の終点
	 * @return suffix arrayの中で、辞書順でspace[start, end)よりも後ろに出る、インデックス(0-index)
	 */
	private int upperBound(int start, int end) {
		int left = 0, right = spaceLength, middle;
		if (right - left <= 0) { // 範囲が1以下のときは左端を返す
			return left;
		}
		if (targetCompare(left, start, end) > 0) {// 右端は明らかに対象の後に来るので、左端が対象と同じかその前に来ることを確認する
			return left;// もし対象の後に後にくるならば、すべてが対象の後ろに来ることが明らかなので左端を返す
		}
		int comp;
		do {
			// 左端は対象と同じかより前に来るように、右端は対象の後ろに来るように持っておく
			middle = left + (right - left) / 2;
			comp = targetCompare(middle, start, end);
			if (comp > 0) {
				right = middle;
			} else {
				left = middle;
			}
		} while (right - left >= 2);// 幅が最小になるまで繰り返す
		return right; // 右端は対象よりも後に来るはず
	}

	private static void testSuffix(String space, int[] except_array) {
		FrequencerRefactor tester = new FrequencerRefactor();
		tester.setSpace(space.getBytes());
		tester.printSuffixArray();
		for (int i = 0; i < tester.suffixArray.length; ++i) {
			System.err.printf("suffix[%d] : (except %d , actually %d) :", i,
					except_array[i],
					tester.suffixArray[i]);
			if (except_array[i] != tester.suffixArray[i]) {
				System.err.println("WA");
			} else {
				System.err.println("AC");
			}
		}
	}

	public static void main(String[] args) {
		FrequencerRefactor tester;
		try {
			tester = new FrequencerRefactor();
			// test for suffix array of ABC (ABC , BC ,A -> 0,1,2)
			int[] expect_suffix_abc = { 0, 1, 2 };
			testSuffix("ABC", expect_suffix_abc);

			int[] expect_suffix_cba = { 2, 1, 0 };// A BA CBA
			testSuffix("CBA", expect_suffix_cba);

			int[] expect_suffix_hhh = { 2, 1, 0 };// H HH HHH
			testSuffix("HHH", expect_suffix_hhh);

			tester = new FrequencerRefactor();
			tester.setSpace("Hi Ho Hi Ho".getBytes());
			tester.printSuffixArray();
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
			 */
			int[] expect_suffix_long = { 5, 8, 2, 6, 0, 9, 3, 7, 1, 10, 4 };
			testSuffix("Hi Ho Hi Ho", expect_suffix_long);

			// 1文字でのテスト

			tester.setSpace("Hi Ho Hi Ho".getBytes());
			tester.printSuffixArray();
			tester.setTarget("H".getBytes());
			int result = tester.frequency();
			int except_value = 4;
			System.out.print(tester.target.toString() + " in " + tester.space.toString()
					+ " : Freq = " + result + " ");
			if (except_value == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG");
			}
			// 2文字でのテスト
			tester.setSpace("Hi Ho Hi Ho".getBytes());
			System.out.println("space length:" + tester.space.length);
			tester.printSuffixArray();
			tester.setTarget("Hi".getBytes());
			result = tester.frequency();
			except_value = 2;
			System.out.print("Freq = " + result + " ");
			if (except_value == result) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG ,value=" + result);
			}
			// 探索地点が終点を超えるときのテスト
			tester.setTarget("z".getBytes());
			result = tester.frequency();
			except_value = 0;
			System.out.print("Freq = " + result + " ");
			if ((except_value == result) && (tester.upperBound(0,
					tester.space.length) == tester.space.length)) {
				System.out.println("OK");
			} else {
				System.out.println("WRONG value : " + result + " end pos : " + tester.upperBound(0,
						tester.space.length));
			}
			// 探索位置が始点のときのテスト
			tester.setSpace("hello".getBytes());
			tester.printSuffixArray();
			tester.setTarget("a".getBytes());
			result = tester.frequency();
			except_value = 0;
			System.out.print("Freq = " + result + " ");
			if ((except_value == result) && (tester.lowerBound(0,
					tester.space.length) == 0)) {
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

package s4.B233316; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. ここは、かならず、自分の名前に変えよ。
import java.lang.*;
import s4.specification.*;

import java.util.Random;

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



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。

    Random rand = new Random();


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
    //
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                System.out.printf("suffixArray[%2d]=%2d:", i, s);
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

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
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ 
        // i == j のとき suffix=i == suffix_j
        if (i == j) {
            return 0;
        }

        int minIndex = i <= j ? i : j;
        int maxIndex = i + j - minIndex;
        int spaceLength = mySpace.length;

        // i != j のとき とりあえず mySpace の終端まで比較
        for (int offset = 0; maxIndex + offset < spaceLength; offset++) {
            if(mySpace[i + offset] > mySpace[j + offset]) {
                return 1;
            } else if (mySpace[i + offset] < mySpace[j + offset]) {
                return -1;
            }
        }

        // mySpace.subString(min,hoge) と suffix_max が等しい場合、suffix.length に合わせてreturn
        if (i < j) {
            return 1;
        } else {
            return -1;
        }
        // return 0; // この行は変更しなければいけない。 
    }

    public void setSpace(byte []space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。
        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 0:ABC
        //   suffixArray[ 1]= 1:BC
        //   suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 2:A
        //   suffixArray[ 1]= 1:BA
        //   suffixArray[ 2]= 0:CBA
        // のようになるべきである。
        // 
        // TODO:QUICK SORTを実装
        suffixArrayQuickSort(0, suffixArray.length);
    }

    // suffix array内の部分配列をクイックソート
    // begin <= index < end
    private void suffixArrayQuickSort (int begin, int end) {
        if (begin < end) {
            int pivot = suffixArrayPartition(begin, end);
            suffixArrayQuickSort(begin, pivot);
            suffixArrayQuickSort(pivot + 1, end);
        }
    }

    // quick sortのパーティション
    // begin <= index < end
    // パーティション後pivotのindexをreturn
    private int suffixArrayPartition (int begin, int end) {
        // pivotの決定
        int r = rand.nextInt(end - begin) + begin;
        // rと部分配列の最終要素をswap
        suffixArraySwap(r, end - 1);
        int i = begin;
        int pivot = suffixArray[end - 1];
        for (int j = begin; j < end - 1; j++) {
            // pivotとjを比較
            int result = suffixCompare(suffixArray[j], pivot);
            // pivot以下ならiの示すindexとswap
            if (result <= 0) {
                suffixArraySwap(i, j);
                i++;
            }
        }
        // pivotを大小の境界に配置
        suffixArraySwap(i, end - 1);
        return i;
    }

    // suffix_array内のswap
    private void suffixArraySwap (int i, int j) {
        int temp = suffixArray[i];
        suffixArray[i] = suffixArray[j];
        suffixArray[j] = temp;
    }

    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        // start, and end specify a string to search in myTarget,
        // if myTarget is "ABCD", 
        //     start=0, and end=1 means string "A".
        //     start=1, and end=3 means string "BC".
        // This method returns how many the string appears in my Space.
        // 
        /* This method should be work as follows, but much more efficient.
           int spaceLength = mySpace.length;                      
           int count = 0;                                        
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false; 
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        // The following the counting method using suffix array.
        // 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
        // 次のように定義せよ。
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD", 
        // suffix_2 is "CD", and sufffix_3 is "D".
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // if myTarget is "ABCD", 
        //     j=0, and k=1 means that target_j_k is "A".
        //     j=1, and k=3 means that target_j_k is "BC".
        // This method compares suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // if suffix_i > target_j_k it return 1; 
        // if suffix_i < target_j_k it return -1;
        // if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search 
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        // The behavior is different from suffixCompare on this case.
        // For example,
        //    if suffix_i is "Ho Hi Ho", and target_j_k is "Ho", 
        //            targetCompare should return 0;
        //    if suffix_i is "Ho Hi Ho", and suffix_j is "Ho", 
        //            suffixCompare should return 1. (It was written -1 before 2021/12/21)
        //
        // ここに比較のコードを書け 
        //

        // 文字列を比較
        int spaceLength = mySpace.length;
        for (int offset = 0; i + offset < spaceLength && j + offset < k; offset++) {
            if (mySpace[i + offset] > myTarget[j + offset]) { 
                return 1;
            } else if (mySpace[i + offset] < myTarget[j + offset]) {
                return -1;
            }
        }

        // suffix_i が target_j_k の接頭語である場合をreturn 
        if (spaceLength - i < k - j) {
            return -1;
        }

        // ここまでで引っかからない場合、suffix_iの先頭 k - j 文字は target_j_kに等しい
        return 0;
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
          10:o Hi Ho
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
        // binary_searchを実装
        int l = 0;
        int r = suffixArray.length - 1;
        int mid = (l + r) / 2;
        while (l <= r) {
            mid = (l + r) / 2;
            // まず現在のmidを比較
            int result = targetCompare(suffixArray[mid], start, end);
            // targetより小さいならlを更新して次
            if (result < 0) {
                l = mid + 1;
            } else {
                // ひとつ前のsuffixを見て
                int before = -1;
                if (mid > 0) {
                    before = targetCompare(suffixArray[mid - 1], start, end);
                }
                // それがtargetよりも小さい
                // すなわち今指しているmidは境界だから
                if (before < 0) {
                    return mid;
                } else {
                    // そうでなければrを更新
                    r = mid - 1;
                }
            }
        }
        return mid + 1;
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho                                    
           1: Ho                                       
           2: Ho Hi Ho                                 
           3:Hi Ho                                     
           4:Hi Ho Hi Ho                              
           5:Ho                                      
           6:Ho Hi Ho                                
           7:i Ho                                    
           8:i Ho Hi Ho                              
           9:o                                       
          10:o Hi Ho                                 
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
        //　ここにコードを記述せよ
        // binary_searchを実装
        int len = suffixArray.length;
        int l = 0;
        int r = len - 1;
        int mid = (l + r) / 2;
        while (l <= r) {
            mid = (l + r) / 2;
            // まず現在のmidを比較
            int result = targetCompare(suffixArray[mid], start, end);
            // targetより大きいならrを更新して次
            if (result > 0) {
                r = mid - 1;
            } else {
                // ひとつ後のsuffixを見て
                int next = 1;
                if (mid < len - 1) {
                    next = targetCompare(suffixArray[mid + 1], start, end);
                }
                // それがtargetよりも大きい
                // すなわち今指しているmidは境界だから
                if (next > 0) {
                    return mid + 1;
                } else {
                    // そうでなければlを更新
                    l = mid + 1;
                }
            }
        }
        return mid;
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
        Frequencer frequencerObject;
        try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            // frequencerObject.setSpace("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes());
            // frequencerObject.setTarget("B".getBytes());
            // System.out.println(frequencerObject.frequency());
            // System.out.println(frequencerObject.subByteStartIndex(0, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".length()));
            // System.out.println(frequencerObject.subByteEndIndex(0, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".length()));
            // frequencerObject.setTarget("0".getBytes());
            // System.out.println(frequencerObject.subByteStartIndex(0, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".length()));
            // System.out.println(frequencerObject.subByteEndIndex(0, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".length()));
            // frequencerObject.printSuffixArray();
            // frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
              10:o Hi Ho                     
            */

            frequencerObject.setTarget("H".getBytes());
            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

package s4.B233375; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 

import java.lang.*;
import java.util.Objects;

import s4.specification.*;

/*
interface FrequencerInterface {  // This interface provides the design for frequency counter.
    void setTarget(byte[] target);  // set the data to search.
    void setSpace(byte[] space);  // set the data to be searched target from.
    int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
                     // Otherwise, it return 0, when SPACE is not set or Space's length is zero
                     // Otherwise, get the frequency of TAGET in SPACE
                     //e.g. myTarget : aaaa , mySpace : aa -> frequency :3 ([0,1],[1,2],[2,3]の3つ)
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e. target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

public class Frequencer implements FrequencerInterface {
    // Code to Test, *warning: This code contains intentional problem*
    static boolean debugMode = false;
    byte[] myTarget;
    byte[] mySpace;

    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    @Override
    public void setSpace(byte[] space) {
        mySpace = space;
    }

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

    @Override
    public int frequency() {
        // targetが設定されているか確認する
        if (Objects.isNull(myTarget)) {
            return -1;

        }
        // spaceが設定されているか確認する
        if (Objects.isNull(mySpace)) {
            return 0;
        }
        int targetLength = myTarget.length;// not set の場合は0になる?->null pointerになった
        int spaceLength = mySpace.length;// not set の場合は0になる?-> null pointer になった
        // TARGETの長さが0でないかを確認する
        if (targetLength == 0) {
            return -1;
        }
        // space の長さが0出ないかを確認する
        if (spaceLength == 0) {
            return 0;
        }
        int count = 0;
        if (debugMode) {
            showVariables();
        }
        // Otherwise, get the frequency of TAGET in SPACE

        // space abcd(4) -> 0,1,2 -> i=0; i<= space length - target length
        // target ab(2)

        // space ab(2) -> 0 : i=0 : i< space length - target length
        // target ab(2)
        for (int start = 0; start <= spaceLength - targetLength; start++) { // Is it OK?
                                                                            // //開始地点(0文字目からtargetの最後がspaceの最後に一致するまで)
            boolean abort = false;// 先頭からみて文字が一致していているか
            for (int i = 0; i < targetLength; i++) {
                if (myTarget[i] != mySpace[start + i]) {// 文字が一致していなかったら
                    abort = true;// 中断+先頭を次に移す
                    break;
                }
            }
            if (abort == false) {// 最後まで一致したら
                count++;// カウントを増やす
            }
        }
        if (debugMode) {
            System.out.printf("%10d\n", count);
        }
        return count;
    }

    // I know that here is a potential problem in the declaration.
    @Override
    public int subByteFrequency(int start, int end) {
        // dev note : Frequencyの内容を利用
        // Not yet implemented, but it should be defined as specified.
        // 半開区間での出現数を求める
        // 始点・終点が不正なものは未定義でよい
        // targetが設定されているか確認する
        if (Objects.isNull(myTarget)) {
            System.err.println("target not set");
            return -1;

        }
        // spaceが設定されているか確認する
        if (Objects.isNull(mySpace)) {
            System.err.println("space not set");
            return 0;
        }
        int targetLength = end-start;// not set の場合はnull pointerに
        int spaceLength = mySpace.length;// not set の場合は null pointer に
        // TARGETの長さが0でないかを確認する
        if (end-start == 0) {
            System.err.println("target length is 0");
            return -1;
        }
        // space の長さが0でないかを確認する
        if (spaceLength == 0) {
            System.err.println("space length is 0");
            return 0;
        }
        // 区間が正当なものか確認する
        if (start < 0 || start > end) {
            System.err.println("range invalid");
            return -2;
        }
        if (end > myTarget.length) {
            System.err.println("end value is invalid");
            return -2;
        }
        int count = 0;
        if (debugMode) {
            showVariables();
        }
        // Otherwise, get the frequency of TAGET in SPACE

        // space abcd(4) -> 0,1,2 -> i=0; i<= space length - target length
        // target ab(2)

        // space ab(2) -> 0 : i=0 : i< space length - target length
        // target ab(2)
        for (int in_start =0; in_start <= spaceLength - targetLength; in_start++) { // Is it OK?
                                                                            // //開始地点(0文字目からtargetの最後がspaceの最後に一致するまで)
            boolean abort = false;// 先頭からみて文字が一致していているか
            for (int i = start; i < end; i++) {
                if (myTarget[i] != mySpace[in_start + i-start]) {// 文字が一致していなかったら
                    abort = true;// 中断+先頭を次に移す
                    break;
                }
            }
            if (abort == false) {// 最後まで一致したら
                count++;// カウントを増やす
            }
        }
        if (debugMode) {
            System.out.printf("%10d\n", count);
        }
        return count;
        //return -1;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
        // White box test, here.
        debugMode = true;
        try {
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("H".getBytes());
            freq = myObject.frequency();
            myObject.setSpace("3210321001230123".getBytes());
            myObject.setTarget("3210321001230123".getBytes());
            myObject.subByteFrequency(0, 16);
        } catch (Exception e) {
            System.out.println("Exception occurred: STOP");
            e.printStackTrace();
        }
    }
}

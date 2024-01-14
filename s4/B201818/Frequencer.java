package s4.B201818; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {  // This interface provides the design for frequency counter.
    void setTarget(byte[] target);  // set the data to search.
    void setSpace(byte[] space);  // set the data to be searched target from.
    int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
                     // Otherwise, it return 0, when SPACE is not set or Space's length is zero
                     // Otherwise, get the frequency of TAGET in SPACE
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
	for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	System.out.write(' ');
	for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	System.out.write(' ');
    }

    @Override
    public int frequency() {
	int targetLength = myTarget.length;
	int spaceLength = mySpace.length;
	if(myTarget.length == 0){//ターゲットが不正な時にreturn -1
		return -1;
	}
	if(mySpace.length == 0){//スペースが不正な時にreturn 0
		return 0;
	}
        int count = 0;
	if(debugMode) { showVariables(); }
        for(int start = 0; start<(spaceLength-(targetLength-1)); start++) { //spaceの開始地点を進める
            boolean abort = false;
            for(int i = 0; i<targetLength; i++) {//開始地点からターゲットと一致しているか一文字ずつ調べる。
                if(myTarget[i] != mySpace[start+i]) { abort = true; break; }//一文字でも一致していなければbreak
            }
            if(abort == false) { count++; }//全文字一致だった時にカウント
        }
	if(debugMode) { System.out.printf("%10d\n", count); }
        return count;
    }

    // I know that here is a potential problem in the declaration.
    @Override
    public int subByteFrequency(int start, int length) {
        // Not yet implemented, but it should be defined as specified.
	if(start>myTarget.length-1 || length>myTarget.length-1 || start>length){//正しくない因数の場合はreturn -1
        	return -1;
	}
	List<byte> list = new ArrayList<byte>();//リストを作る
	for(int i=start;i<length;i++){//作ったリストに切り出すべきところを入れていく
		 list.add(myTarget[i]);
	}
	byte[] subTarget = list.toArray(new byte[list.size()]);//リストを配列に変える
	//以下は大体frequency()と同じ
	int subtargetLength = subTarget.length;
	int spaceLength = mySpace.length;
	if(subTarget.length == 0){
		return -1;
	}
	if(mySpace.length == 0){
		return 0;
	}
        int count = 0;
	if(debugMode) { showVariables(); }
        for(start = 0; start<(spaceLength-(subtargetLength-1)); start++) { // Is it OK? => OK!
            boolean abort = false;
            for(int i = 0; i<subtargetLength; i++) {
                if(subTarget[i] != mySpace[start+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
        }
	if(debugMode) { System.out.printf("%10d\n", count); }
        return count;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
	// White box test, here.
	debugMode = true;
        try {
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
            myObject.setTarget("H".getBytes());//探す単語をセット
            freq = myObject.frequency();
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP");
        }
    }
}

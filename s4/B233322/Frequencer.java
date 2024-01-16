package s4.B233322; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import java.util.Arrays;
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
	if (this.myTarget == null) { return -1; }
	if (this.mySpace == null) { return 0; }
		
        int targetLength = myTarget.length;
	if (targetLength == 0) { return -1; }
        int spaceLength = mySpace.length;
	if (spaceLength == 0) { return 0; }
	
        int count = 0;
	if(debugMode) { showVariables(); }
        for(int start = 0; start + targetLength - 1 < spaceLength; start++) { // Is it OK?
            boolean abort = false;
            for(int i = 0; i<targetLength; i++) {
                if(myTarget[i] != mySpace[start+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
        }
	if(debugMode) { System.out.printf("%10d\n", count); }
        return count;
    }

    @Override
    public int subByteFrequency(int start, int end) {
	Frequencer freq = new Frequencer();
	freq.setSpace(this.mySpace);
	freq.setTarget(Arrays.copyOfRange(this.myTarget, start, end));
	return freq.frequency();
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
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP");
        }
    }
}

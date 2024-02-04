package s4.B211302; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
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
    boolean targetready = false;
    boolean spacereday = false;

    @Override
    public void setTarget(byte[] target) {
        if(target.length > 0){
            myTarget = target;
            targetready = true;
        }
    }
    @Override
    public void setSpace(byte[] space) {
        if(space.length > 0){
            mySpace = space;
            spacereday = true;
        }
    }

    private void showVariables() {
	for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	System.out.write(' ');
	for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	System.out.write(' ');
    }

    @Override
    public int frequency() {
        if(targetready == false) return -1;
        if(spacereday == false) return 0;
        return subByteFrequency(0,myTarget.length);
    }

    // I know that here is a potential problem in the declaration.
    @Override
    public int subByteFrequency(int start, int length) {
        // Not yet implemented, but it should be defined as specified.
	    
        int spaceLength = mySpace.length;
        int count = 0;
	for(int offset = 0; offset<= spaceLength - (length - start); offset++) {
	    boolean abort = false;
	    for(int i = 0; i< (length - start); i++) {
		if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
	    }
	    if(abort == false) { count++; }
	}
        return count;
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

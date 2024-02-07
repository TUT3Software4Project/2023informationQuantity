package s4.B211302;
import java.lang.*;
import s4.specification.*;
import java.util.*;


public class Frequencer implements FrequencerInterface{
    
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; 

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
        String str1 = Arrays.toString(mySpace);
        String suffix_i = str1.substring(i,str1.length()-1);
        String suffix_j = str1.substring(j,str1.length()-1);
        if (suffix_i.compareTo(suffix_j) < 0) return 1;
        if (suffix_i.compareTo(suffix_j) > 0) return -1;
        return 0;
    }

    public void setSpace(byte []space) {
        
        mySpace = space; if(mySpace.length>0) spaceReady = true;
       
        suffixArray = new int[space.length];
        
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; 
        }

        int tmp;
        String str1 = Arrays.toString(mySpace);
        for(int i=0;i<mySpace.length-1;i++){
            for(int j=i+1;j<mySpace.length-1;j++){
                String mySpace_i = str1.substring(i,str1.length()-1);
                String mySpace_j = str1.substring(j,str1.length()-1);
                if(mySpace_i.compareTo(mySpace_j) > 0){
                    tmp = suffixArray[i];
                    suffixArray[i] = suffixArray[j];
                    suffixArray[j] = tmp;
                }
            }
        }
    }

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }

    private int targetCompare(int i, int j, int k) {
        String str1 = Arrays.toString(mySpace);
        String str2 = Arrays.toString(myTarget);
        String suffix_i = str1.substring(i,str1.length()-1);
        String target_j_k = str2.substring(j,k);
        if (suffix_i.compareTo(target_j_k) < 0) return 1;
        if (suffix_i.compareTo(target_j_k) > 0) return -1;
        return 0;
    }


    private int subByteStartIndex(int start, int end) {;
        String str1 = Arrays.toString(mySpace);
        String str2 = Arrays.toString(myTarget);
        String target_start_end = str2.substring(start,end);

        int result = str1.indexOf(target_start_end);
        return result;
    }

    private int subByteEndIndex(int start, int end) {
                String str1 = Arrays.toString(mySpace);
        String str2 = Arrays.toString(myTarget);
        String target_start_end = str2.substring(start,end);

        int result = str1.lastIndexOf(target_start_end);
        return result;

    }

    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
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
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
        
            frequencerObject.setTarget("H".getBytes());
        
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

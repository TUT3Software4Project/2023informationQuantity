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
    Random rand;
    
    //suffixArrayの値を表示する
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
    
    private void MultiKeyQuickSort(int left, int right, int depth) {
        if (right - left <= 1) {
            return;
        }

        int pivotIndex = rand.nextInt(right - left) + left;
        int pivotValue = this.suffixArray[pivotIndex];
        int i = left;
        int j = right - 1;
        while (i <= j) {
            while (i < right && suffixCompare(this.suffixArray[i], pivotValue, depth) < 0) {
                i++;
            }
            while (j >= left && suffixCompare(this.suffixArray[j], pivotValue, depth) > 0) {
                j--;
            }
            if (i <= j) {
                Swap(i, j);
                i++;
                j--;
            }
        }

        if (left < j) {
            MultiKeyQuickSort(left, j + 1, depth);
        }
        if (i < right) {
            MultiKeyQuickSort(i, right, depth);
        }
    }

    private void Swap(int i, int j) {
        int tmp = this.suffixArray[i];
        this.suffixArray[i] = this.suffixArray[j];
        this.suffixArray[j] = tmp;
    }

    private int suffixCompare(int i, int j, int depth) {
        if (i == j) {
            return 0;
        }

        int spaceLength = mySpace.length;
        int offset = depth;
        while (true) {
            if (i + offset >= spaceLength) {
                return -1;
            }
            if (j + offset >= spaceLength) {
                return 1;
            }
            if (this.mySpace[i + offset] > this.mySpace[j + offset]) {
                return 1;
            }
            if (this.mySpace[i + offset] < this.mySpace[j + offset]) {
                return -1;
            }
            offset++;
        }
    }

    public void setSpace(byte[] space) {

        mySpace = space;
        if (mySpace.length > 0) spaceReady = true;

        suffixArray = new int[space.length];

        for (int i = 0; i < space.length; i++) {
            suffixArray[i] = i;
        }
        if (this.mySpace.length < 2) {
            return;
        }
        rand = new Random();
        MultiKeyQuickSort(0, this.mySpace.length, 0);
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
	for (int offset=0; offset < k-j; ++offset)
	{
	    if (i+offset >= this.mySpace.length) { return -1; }
	    if (this.mySpace[i+offset] > this.myTarget[j+offset]) { return 1; }
	    if (this.mySpace[i+offset] < this.myTarget[j+offset]) { return -1; }
	}
        return 0;
    }


    private int subByteStartIndex(int start, int end) {
	int left = -1;
        int right = suffixArray.length;
	int mid;

        while(right - left > 1) {
            mid = (left + right) / 2;
            if(targetCompare(suffixArray[mid], start, end) >= 0) {
                right = mid;
            }
            else {
                left = mid;
            }
        }

        return right;
    }

    private int subByteEndIndex(int start, int end) {
	int left = -1;
        int right = suffixArray.length;
	int mid;

        while(right - left > 1) {
            mid = (left + right) / 2;
            if(targetCompare(suffixArray[mid], start, end) > 0) {
                right = mid;
            }
            else {
                left = mid;
            }
        }

        return right;
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

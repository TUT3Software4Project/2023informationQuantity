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
    static boolean debugMode = false;
    byte[] myTarget;
    byte[] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int []  suffixArray;

    private void printSuffixArray() {
        if(mySpace.length != 0) {
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
	
    @Override
    public void setTarget(byte[] target) {
        myTarget = target;
	targetReady = true;
    }
    @Override
    public void setSpace(byte[] space) {
        mySpace = space;
	spaceReady = true;
        if(space.length != 0){
            suffixArray = new int[space.length];
            for(int i = 0;i<space.length;i++) {
            suffixArray[i] = i;//suffixArrayの中身は0,1,2,3...space.length-1になる
            }
	
	    //マージソート
	    suffixArray = msort(suffixArray);

	    //辞書順になるようにバブルソート
	    /*for(int r = 0;r<(space.length-1);r++){
   	         for(int i = (space.length-1);i>r;i--){
			if(suffixCompare(suffixArray[i],suffixArray[i-1]) == -1){
				int a = suffixArray[i];
				suffixArray[i] = suffixArray[i-1];
				suffixArray[i-1] = a;
			}
		}
	    }*/
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

        //リターン0になるのはiとjが同一な時のみ
	if(i == j){
            return 0;// if suffix_i = suffix_j, it returns 0;   
        }
	for(int q = 0;q < mySpace.length;q++){
		//文字数上限に基づく比較
		if(i+q > mySpace.length-1){
			return -1;// if suffix_i < suffix_j, it returns -1
		}
		if(j+q > mySpace.length-1){
			return 1;// if suffix_i > suffix_j, it returns 1
		}

		//数値比較
		if(mySpace[i+q] == mySpace[j+q]){
			//次の周回へ
		}else if(mySpace[i+q] > mySpace[j+q]){
			return 1;// if suffix_i > suffix_j, it returns 1   
		}else if(mySpace[i+q] < mySpace[j+q]){
			return -1;// if suffix_i < suffix_j, it returns -1  
		}
	}
	return 0;
    }

    private int[] msort(int[] suffixArray){
	int[] a;
	int[] b;
	if(suffixArray.length % 2 == 0){
	    if(suffixArray.length==2){
		if(suffixCompare(suffixArray[0],suffixArray[1]) == 1){//辞書順に入れ替え
			int x = suffixArray[0];
			suffixArray[0] = suffixArray[1];
			suffixArray[1] = x;
		}
		return suffixArray;
	    }
            //a = msort(suffixArray.slice(0,(suffixArray.length / 2)));
	    //b = msort(suffixArray.slice(suffixArray.length / 2),suffixArray.length);
	    a = msort(slice(suffixArray,0,(int)(suffixArray.length / 2)));
            b = msort(slice(suffixArray,(int)(suffixArray.length / 2),suffixArray.length));
	}else{
	    if(suffixArray.length==1){
		return suffixArray;
	    }
	    //a = msort(suffixArray.slice(0,(suffixArray.length / 2)));
            //b = msort(suffixArray.slice(suffixArray.length / 2),suffixArray.length);
	    a = msort(slice(suffixArray,0,(int)(suffixArray.length / 2)));
            b = msort(slice(suffixArray,(int)(suffixArray.length / 2),suffixArray.length));
	}

	//配列aとbを辞書順にマージしリターン
	int aLength = a.length;
	int bLength = b.length;
	int[] ret = new int[aLength+bLength];
        int ac = 0;
	int bc = 0;

	while((ac < aLength) && (bc < bLength)){
	    if(suffixCompare(a[ac],b[bc]) == 1){
                ret[ac+bc] = b[bc];
	        bc++;
	    }else{
                ret[ac+bc] = a[ac];
	        ac++;
	    }
	}
        if((ac < aLength)){
            for(int i = ac;i<aLength;i++){
		ret[i+bc] = a[i];
	    }
	}
	if((bc < bLength)){
            for(int j = bc;j<bLength;j++){
		ret[ac+j] = b[j];
	    }
	}

	return ret;
    }

     private int[] slice(int[] suffixArray,int x,int y){
	int[] array = new int[y-x];
        for(int i=0;i<(y-x);i++){
            array[i] = suffixArray[i+x];
	}
        return array;
     }
	
    private void showVariables() {
	for(int i=0; i< mySpace.length; i++) { System.out.write(mySpace[i]); }
	System.out.write(' ');
	for(int i=0; i< myTarget.length; i++) { System.out.write(myTarget[i]); }
	System.out.write(' ');
    }

    @Override
    public int frequency() {
	if(targetReady == false){//ターゲットが未セットな時にreturn -1
		return -1;
	}
	if(spaceReady == false){//スペースが未セットな時にreturn 0
		return 0;
	}
	int targetLength = myTarget.length;
	int spaceLength = mySpace.length;
	if(myTarget.length == 0){//ターゲットが不正な時にreturn -1
		return -1;
	}
	if(mySpace.length == 0){//スペースが不正な時にreturn 0
		return 0;
	}
	if(mySpace.length < myTarget.length){//スペースよりターゲットの方が大きい時にreturn 0
		return 0;
	}
	/*
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
	*/
	return subByteFrequency(0, myTarget.length);
    }

    // I know that here is a potential problem in the declaration.
    @Override
    public int subByteFrequency(int start, int end) {
	if((start > myTarget.length-1) || (end > myTarget.length) || (start >= end)){//正しくない引数の場合はreturn -1
        	return -1;
	}
	/*int targetLength = myTarget.length;
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
            for(int i = 0; i<(e-s); i++) {//開始地点からターゲットと一致しているか一文字ずつ調べる。
                if(myTarget[i+s] != mySpace[start+i]) { abort = true; break; }//一文字でも一致していなければbreak
            }
            if(abort == false) { count++; }//全文字一致だった時にカウント
        }
	if(debugMode) { System.out.printf("%10d\n", count); }
        return count;*/

	int first = subByteStartIndex(start, end);//いくつ目のサフィックスアレイから一致しているか
	if(first == -1) return 0;
        int last1 = subByteEndIndex(start, end, first);//いくつ目のサフィックスアレイから不一致になるか
        return last1 - first;

        /*int sStart = 0;
	int sEnd = mySpace.length;//suffixarray上の探す領域
	for (int n = 0; n < (end - start); n++){
            //n文字目が一致する場所を探索していく、だんだん領域が狭まっていく
            sStart = startSearch(start,end,sStart,sEnd,n);
	    if(sStart == -1){return 0;}
	    sEnd = endSearch(start,end,sStart,sEnd,n);
	}

        return (sEnd - sStart);*/
	    
    }

/*private int startSearch(int start, int end, int sStart, int sEnd,int n){
        if(sStart == 0 && sEnd == 1){
	    //suffixArray[0]が一致する場合はここで判定する
            if((suffixArray[0] + (end-start)) <= mySpace.length){//文字数を超過していなければ
                 if(myTarget[n+start] == mySpace[suffixArray[0] + n]) { return 0;//一致ならstart位置は0 }
	    }
	return -1;//みつからなかったとき
	}

	int center;
	//領域の中心を決定
        if((sEnd-sStart)%2 == 0){
            center = (sEnd-sStart)/2;
	}else{
            center = ((sEnd-sStart)-1)/2;
	}

        //中心が、一致する一個前(startの位置)であるかを判定し、中心ならリターン、そうでなければ次の再帰へ
	if((mySpace.length - suffixArray[center]) > n+start){
	    if(myTarget[n+start] > mySpace[suffixArray[center]+n]){
                if(myTarget[n+start] == mySpace[suffixArray[center+1]+n]){return center;}
		return startSearch(start,end,center,sEnd,n);
	    }
	    if(myTarget[n] <= mySpace[suffixArray[center]+n]){
                return startSearch(start,end,sStart,center,n);
	    }
	}
    }
    return -1;
}

private int endSearch(int start, int end, int sStart, int sEnd,int n){
        if(sStart == 0 && sEnd == 1){
	    //最後まで一致する場合はここで判定する
            if((suffixArray[0] + (end-start)) <= mySpace.length){//文字数を超過していなければ
                 if(myTarget[n+start] == mySpace[suffixArray[0] + n]) { return mySpace.length;//一致ならend位置は最後 }
	    }
	return mySpace.length-1;//不一致ならend位置は最後-1
	}
	
	//領域の中心を決定
	int center;
        if((sEnd-sStart)%2 == 0){
            center = (sEnd-sStart)/2;
	}else{
            center = ((sEnd-sStart)-1)/2;
	}

        //中心が、不一致する位置(endの位置)であるかを判定し、中心ならリターン、そうでなければ次の再帰へ
	if((mySpace.length - suffixArray[center]) > n+start){
	    if(myTarget[n+start] >= mySpace[suffixArray[center]+n]){
                if(myTarget[n+start] != mySpace[suffixArray[center+1]+n]){return center;}
		return endSearch(start,end,center,sEnd,n);
	    }
	    if(myTarget[n] < mySpace[suffixArray[center]+n]){
                return endSearch(start,end,sStart,center,n);
	    }
	}
    }
    return -1;
}*/

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
	    
	int i,j;
	for (i = 0;i<mySpace.length;i++){
	    boolean abort = false;
            if((suffixArray[i] + (end-start)) <= mySpace.length){//文字数を超過していれば
            for(j = 0; j<(end-start); j++) {//開始地点からターゲットと一致しているか一文字ずつ調べる。
                if(myTarget[start+j] != mySpace[suffixArray[i] + j]) { abort = true; break; }//一文字でも一致していなければbreak
            	}
            if(abort == false) {
		return i;
	    }//全文字一致だった時にカウント
	  }
        }
	    
        return -1;
    }

    private int subByteEndIndex(int start, int end, int first) {
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
	    
	//カウントが進んだ状態からスタート
	int i,j;
	for (i = first;i<mySpace.length;i++){
	    boolean abort = true;
            if((suffixArray[i] + (end-start)) > mySpace.length){return i;}//文字数を超過していれば
            for(j = 0; j<(end-start); j++) {//開始地点からターゲットと一致しているか一文字ずつ調べる。
                if(myTarget[start+j] != mySpace[suffixArray[i]+j]) { abort = false; break; }//一文字でも一致していなければbreak
            }
            if(abort == false) { 
		return i;
	    }//不一致だった時にリターン
            }
	if(i == mySpace.length) return i;//ターゲット文字数が1かつ最後のアレイまで一致していてfor文を超過した場合
        return -1;
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
		assert freq == 4 : "test1:" + freq;
		}
        catch(Exception e) {
            System.out.println("Exception occurred: STOP1");
        }
        try {
		myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                myObject.setTarget("Hi Ho Hi Ho Hi Ho".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 0 : "test2:" + freq;
		}
        catch(Exception e) {
            System.out.println("Exception occurred: STOP2");
        }
	
       try {
                myObject = new Frequencer();
                myObject.setTarget("Hi Ho Hi Ho Hi Ho".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 0 : "test3:" + freq;
	       }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP3");
        }
	
       try {
		myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                freq = myObject.frequency();
		assert freq == -1 : "test4:" + freq;
	}
        catch(Exception e) {
            System.out.println("Exception occurred: STOP4:");
        }
	
       try {
		myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                myObject.setTarget("Hi Ho Hi Ho".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 1 : "test5:" + freq;
	}
        catch(Exception e) {
            System.out.println("Exception occurred: STOP5");
        }
        try {
                myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                myObject.setTarget("i".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 2 : "test6:" + freq;
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP6");
        }
	try {
                myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                myObject.setTarget("H".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 4 : "test6:" + freq;
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP9");
        }

	try {
                myObject = new Frequencer();
                myObject.setSpace("H".getBytes());//探される文をセット
                myObject.setTarget("i".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 0 : "test6:" + freq;
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP7");
        }

        try {
                myObject = new Frequencer();
                myObject.setSpace("Hi Ho Hi Ho".getBytes());//探される文をセット
                myObject.setTarget("H".getBytes());//探す単語をセット
                freq = myObject.frequency();
		assert freq == 4 : "test6:" + freq;
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP8");
        }
	    
	/*myObject = new Frequencer();
        myObject.setSpace("ABC".getBytes());
        myObject.printSuffixArray();
        myObject = new Frequencer();
        myObject.setSpace("CBA".getBytes());
        myObject.printSuffixArray();
        myObject = new Frequencer();
        myObject.setSpace("H".getBytes());
        myObject.printSuffixArray();
        myObject = new Frequencer();
        myObject.setSpace("Hi Ho Hi Ho".getBytes());
        myObject.printSuffixArray();*/
    }
}

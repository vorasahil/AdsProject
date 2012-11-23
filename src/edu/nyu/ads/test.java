package edu.nyu.ads;
import java.util.*;

public class test {
	public static void main(String arg[]){
		HashMap<Integer,Boolean> t=new HashMap<Integer,Boolean>();
		for(int i=0;i<10;i++)
		t.put(i, true);
		System.out.println(t);
		
		for(Integer i:t.keySet()){
			t.put(i, false);
		}
		
		System.out.println(t);
	}
}

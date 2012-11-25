package edu.nyu.ads;
import java.util.*;

public class test {
	public static void main(String arg[]){
		HashMap<Integer,Boolean> t=new HashMap<Integer,Boolean>();
		for(int i=0;i<10;i++)
		t.put(i, true);
		System.out.println(t);
		
		List<List<Integer>> list=new ArrayList<List<Integer>>();
		list.add(null);
		list.add(null);
		FOR: for(List<Integer> sit:list){
			if(sit==null){
				Variable v=null;
				try{
					System.out.println("inside try");

					sit.size();
					System.out.println("inside try2");
				}
				catch(NullPointerException e){
					System.out.println("inside cathc");
				continue;
				}
				System.out.println("tp");
				break FOR;
			}
		}
		System.out.println(t);
	}
}

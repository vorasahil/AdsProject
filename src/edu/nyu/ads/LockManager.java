package edu.nyu.ads;
import java.util.*;
public class LockManager {
	List<HashMap<String, Transaction>> locks;
	
	LockManager(){
		locks=new ArrayList<HashMap<String,Transaction>>();
	}
	
	void addLocktable(HashMap<String,Transaction> lockTable){
		locks.add(lockTable);
	}
	
	String getLock(Transaction t,String variable){
		return "";
		
	}
}

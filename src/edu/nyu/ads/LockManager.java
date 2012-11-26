package edu.nyu.ads;
import java.util.*;
public class LockManager {
	List<Map<String, Transaction>> locks;
	
	LockManager(){
		locks=new ArrayList<Map<String,Transaction>>();
	}
	
	void addLocktable(Map<String,Transaction> lockTable){
		locks.add(lockTable);
	}
	
	Status getLock(Transaction t,String variable){
		
		for(Map<String,Transaction> lockTable : locks){
			if(lockTable.containsKey(variable)){
				Transaction temp = lockTable.get(variable);
				if(temp.equals(t)){
					return Status.GetLock;
				}
				if(temp.getTimestamp() > t.getTimestamp()){
					return Status.Block;
				}

				else{
					return Status.Abort;
				}
			}
			
		}
		
		return Status.GetLock;
		
		
	}
}

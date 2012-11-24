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
	
	Status getLock(Transaction t,String variable){
		
		for(HashMap<String,Transaction> lockTable : locks){
			if(lockTable.containsKey(variable)){
				Transaction temp = lockTable.get(variable);
				if(temp.getTimestamp() > t.getTimestamp()){
					return Status.Block;
				}
			}
			
			else{
				return Status.Abort;
			}
		}
		
		return Status.GetLock;
		
		
	}
}

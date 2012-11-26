package edu.nyu.ads;
import java.util.*;
public class LockManager {
	List<Map<String, Transaction>> locks;
	List<Map<String,List<Transaction>>> readLocks;
	
	LockManager(){
		locks=new ArrayList<Map<String,Transaction>>();
		readLocks=new ArrayList<Map<String,List<Transaction>>>();
	}
	
	void addLocktable(Map<String,Transaction> lockTable){
		locks.add(lockTable);
	}
	
	Status getLock(Transaction t,String variable, boolean read){
		
		
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
		
		
		if(read){
			return Status.GetLock;
		}
		
		else{
			Set<Transaction> hasReadLock=new HashSet<Transaction>();
			for(Map<String,List<Transaction>> maps:readLocks){
				if(maps.containsKey(variable)){
					hasReadLock.addAll(maps.get(variable));
				}
			}
			if(hasReadLock.size()==0)
				return Status.GetLock;
			if(hasReadLock.size()==1&& hasReadLock.contains(t))
				return Status.GetLock;
			
			for(Transaction trans:hasReadLock){
				if(trans.getTimestamp()<t.getTimestamp()){
					return Status.Abort;
				}
				
			}
			
			return Status.Block;
		}
		
	}

	public void addReadLocktable(Map<String, List<Transaction>> readLockTable) {
		readLocks.add(readLockTable);
		
	}
}

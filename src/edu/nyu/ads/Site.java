
package edu.nyu.ads;

import java.util.*;

public class Site {
	private int siteNumber;
	private Map<String,Variable> variables;
	private Map<String,Variable> variablesBackup;	
    private Map<String, Transaction> lockTable;
    private Map<String,List<Transaction>> readLockTable;
    private boolean fail;
    private Map<String,Boolean> isReadable;
    /**
     * 
     * @param siteNumber
     * @param variables
     */
    public Site(int siteNumber,Map<String,Variable> variables,Map<String,Transaction> lockTable, Map<String,List<Transaction>> readLockTable){
    		this.siteNumber=siteNumber;
    		this.variables=new HashMap<String,Variable>(variables);
    		this.variablesBackup=new HashMap<String,Variable>(variables);
    		this.fail=false;
    		this.lockTable=lockTable;//shallow copy, also passed to the lockManager..
    		isReadable=new HashMap<String,Boolean>();
    		this.readLockTable=readLockTable;
    		for(String variable:variables.keySet()){
    			isReadable.put(variable, true);
    		}
    }
    
    /**
     * 
     * @param variable
     * @param t
     * @param read : If true, read lock else write lock
     * @return
     */
    boolean lock(String variable, Transaction t, boolean read){
    	if(read){
    		if(readLockTable.containsKey(variable)){
    			if(!readLockTable.get(variable).contains(t))
    				readLockTable.get(variable).add(t);
    		}
    		else{
    			List<Transaction> lst=new ArrayList<Transaction>();
    			lst.add(t);
    			readLockTable.put(variable, lst);
    		}
    		return true;
    	}
    	else{
    	
         	if(!variable.contains(variable))
         		throw new IllegalStateException("Does not contain that variable");
         
         	//updating readLockTable
         	if(readLockTable.containsKey(variable)){
         		if(readLockTable.get(variable).contains(t)){
         			readLockTable.get(variable).remove(t);
         		}
         		else{
         			throw new IllegalStateException("LOCK MANAGER IS MALFUNCTIONING!");
         		}
         	}
         	
         	if(lockTable.containsKey(variable)){
    			if(lockTable.get(variable).equals(t))
    				return true;
         		return false;
         	}
         	else{
         		lockTable.put(variable,t);
         		return true;
         	}
    	}
    }
    
    boolean releaseLock(String variable, Transaction t){
    	if(readLockTable.containsKey(variable)){
    		readLockTable.get(variable).remove(t);
    		return true;
    	}
    	if(lockTable.containsKey(variable)){
    		if(!lockTable.get(variable).equals(t)){
    			throw new IllegalStateException("LOCK NOT with "+t+"!");
    		}
    		else{
    			lockTable.remove(variable);
    			return true;
    		}	
    	
    	}
    	return true;
    }
    
    boolean isUp(){
      		return !fail;
    }
    
    int getSiteNumber(){
    	return this.siteNumber;
    }
    
    void fail(){
    	fail=true;
    	lockTable.clear();
    	//variables=new HashMap<String,Variable>(variablesBackup);    	
    	for(String variable:isReadable.keySet()){
    		Variable v=variables.get(variable);
    		if(v.isVariableReplicated()){
    			isReadable.put(variable, false); 
    		}
    	}
    }
    
    void recover(){
    	fail=false;
    }
    
	Map<String,Integer> dump(){
		Map<String,Integer> map=new HashMap<String,Integer>();
		for(String var:variablesBackup.keySet()){
			map.put(var,variablesBackup.get(var).getValue());
		}
		return Collections.unmodifiableMap(map);
	}
    
    
    Variable readVariableBackup(String variable){
		if(!variablesBackup.containsKey(variable)){
			throw new IllegalStateException("Does not contain variable");
		}
		else{
			if(!isReadable.get(variable)){
				throw new IllegalStateException("Cannot Read, this is Stale");
			}
			return variablesBackup.get(variable);
		}
	}
	
	Variable readVariable(String variable){
		if(!variables.containsKey(variable)){
			throw new IllegalStateException("Does not contain variable");
		}
		else{
			if(!isReadable.get(variable)){
				throw new IllegalStateException("Cannot Read, this is Stale");
			}
			return variables.get(variable);
		}
	}
	
	boolean writeVariable(String variable, int i){
		if(!variables.containsKey(variable)){
			throw new IllegalStateException("Does not contain variable");
		}
		else{
			Variable v=new Variable(variables.get(variable));
			v.setValue(i);
			variables.put(variable, v);
			isReadable.put(variable,true);
			return true;
		}
	}
	
	boolean commit(Transaction t){
		//check if commitable.
		ArrayList<String> names=new ArrayList<String>();
		
		for(String variable:lockTable.keySet()){
			if(lockTable.get(variable).equals(t)){
				names.add(variable);
			}
		}
		
		for(String variable:names){
			variablesBackup.get(variable).setValue(variables.get(variable).getValue());
			variables.put(variable,variablesBackup.get(variable));//Restore the original variable object in variables map.
			lockTable.remove(variable);
		}
		
		for(String var:readLockTable.keySet()){
			readLockTable.get(var).remove(t);
		}
		
		return true;
	}
	
	void abort(Transaction t){
		ArrayList<String> vars=new ArrayList<String>();
		for(String var:lockTable.keySet()){
			if(lockTable.get(var).equals(t)){
				vars.add(var);
				variables.put(var,variablesBackup.get(var));
			}
		}
		for(String var:vars){
			lockTable.remove(var);
		}
		
		for(String var:readLockTable.keySet()){
			readLockTable.get(var).remove(t);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fail ? 1231 : 1237);
		result = prime * result + siteNumber;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (fail != other.fail)
			return false;
		if (siteNumber != other.siteNumber)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Site [siteNumber=" + siteNumber + ", variables=" + variables
				+ ", variablesBackup=" + variablesBackup + ", lockTable="
				+ lockTable + ", fail=" + fail + ", isReadable=" + isReadable
				+ "]";
	}

}

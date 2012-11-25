
package edu.nyu.ads;

import java.util.*;

public class Site {
	private int siteNumber;
	private Map<String,Variable> variables;
	private Map<String,Variable> variablesBackup;	
    private Map<String, Transaction> lockTable;
    private boolean fail;
    private Map<String,Boolean> isReadable;
    /**
     * 
     * @param siteNumber
     * @param variables
     */
    public Site(int siteNumber,Map<String,Variable> variables,HashMap<String,Transaction> lockTable){
    		this.siteNumber=siteNumber;
    		this.variables=new HashMap<String,Variable>(variables);
    		this.variablesBackup=new HashMap<String,Variable>(variables);
    		this.fail=false;
    		this.lockTable=lockTable;//shallow copy, also passed to the lockManager..
    		isReadable=new HashMap<String,Boolean>();
    		for(String variable:variables.keySet()){
    			isReadable.put(variable, true);
    		}
    }
    
    /**
     * 
     * @param variable
     * @param t
     * @return
     */
    boolean lock(String variable, Transaction t){
         	if(!variable.contains(variable))
         		throw new IllegalStateException("Does not contain that variable");
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
    
    boolean releaseLock(String variable, Transaction t){
    	if(!lockTable.containsKey(variable))
    		throw new IllegalStateException("Does not contain that lock");
    	else{
    		lockTable.remove(variable);
    		return true;
    	}	
    }
    
    boolean isUp(){
      		return !fail;
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
			lockTable.remove(variable);
		}
		
		return true;
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

}

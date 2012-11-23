
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
    public Site(int siteNumber,Map<String,Variable> variables){
    		this.siteNumber=siteNumber;
    		this.variables=new HashMap<String,Variable>(variables);
    		this.variablesBackup=new HashMap<String,Variable>(variables);
    		this.fail=false;
    		this.lockTable= new HashMap<String,Transaction>();
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
    		if(lockTable.containsKey(variable))
         		return false;
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

}

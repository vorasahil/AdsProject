package edu.nyu.ads;
import java.util.*;
public class ReadOnlyTransaction extends Transaction {

	Map<String,Variable> variables;
	ReadOnlyTransaction(String name, int timestamp,Map<String,Variable>variables) {
		super(name, timestamp, TransactionType.ReadOnly);
		this.variables=new HashMap<String,Variable>(variables);	
	}
	
	@Override
	Integer read(String variable) {
		if(variables.get(variable)==null)
			return null;
		return variables.get(variable).getValue();
	}

	@Override
	void write(String variable, int value) {
		throw new UnsupportedOperationException("Read Only Transaction Cannot Write!");
	}

	void addVar(String variable,Variable v){
		variables.put(variable, v);
	}

	@Override
	Map<String, String> end(TransactionState s) {
		this.state = s;
		return null;
	}

}

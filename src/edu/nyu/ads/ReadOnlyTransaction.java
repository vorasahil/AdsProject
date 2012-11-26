package edu.nyu.ads;
import java.util.*;
public class ReadOnlyTransaction extends Transaction {

	Map<String,Variable> variables;
	ReadOnlyTransaction(String name, int timestamp,Map<String,Variable>variables) {
		super(name, timestamp, TransactionType.ReadOnly);
		this.variables=new HashMap<String,Variable>(variables);	
	}
	
	@Override
	int read(String variable) {
		return variables.get(variable).getValue();
	}

	@Override
	void write(String variable, int value) {
		throw new UnsupportedOperationException("Read Only Transaction Cannot Write!");
	}


	@Override
	Map<String, String> end(TransactionState s) {
		return null;
	}

}

package edu.nyu.ads;
import java.util.*;
public class ReadWriteTransaction extends Transaction {

	Integer value;
	Boolean read;
	
	Map<String,String> touchedVariables;
	ReadWriteTransaction(String name,  int timestamp) {
		super(name,timestamp, TransactionType.ReadWrite);
		touchedVariables=new HashMap<String,String>();
		value=null;
		read=null;
	}

	@Override
	int read(String variable) {
		if(!touchedVariables.containsKey(variable))
		{
			touchedVariables.put(variable,"Read");
		}
		return 0;
	}

	@Override
	void write(String variable, int value) {
		touchedVariables.put(variable,"Write");

	}

	
	
	@Override
	Map<String, String> end(TransactionState s) {
		this.state=s;
		return Collections.unmodifiableMap(this.touchedVariables);
	}

}

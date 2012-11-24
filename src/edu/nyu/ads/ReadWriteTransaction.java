package edu.nyu.ads;
import java.util.*;
public class ReadWriteTransaction extends Transaction {

	Map<String,String> touchedVariables;
	ReadWriteTransaction(String name,  int timestamp) {
		super(name,timestamp, TransactionType.ReadWrite);
		touchedVariables=new HashMap<String,String>();
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
	void block() {
		// TODO Auto-generated method stub
		this.state=TransactionState.Blocked;
	}

	@Override
	Map<String, String> end() {
		return Collections.unmodifiableMap(this.touchedVariables);
	}

}

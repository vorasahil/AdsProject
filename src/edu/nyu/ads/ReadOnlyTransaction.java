package edu.nyu.ads;

public class ReadOnlyTransaction extends Transaction {

	ReadOnlyTransaction(String name, int timestamp) {
		super(name, timestamp, TransactionType.ReadOnly);
		// TODO Auto-generated constructor stub
	}

	@Override
	int read(Variable v, Site site) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	boolean write(Variable variable, int value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean abort() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean commit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void block() {
		// TODO Auto-generated method stub
		
	}

}

package edu.nyu.ads;

public class ReadOnlyTransaction extends Transaction {

	ReadOnlyTransaction(String name, String id, int timestamp, String type) {
		super(name, id, timestamp, type);
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

}

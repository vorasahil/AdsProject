/**
 * 
 */
package edu.nyu.ads;

import java.util.*;
/**
 * @author Sahil Vora
 *
 */
public abstract class Transaction {
	String name;
	int timestamp;
	TransactionType type;
	TransactionState state;
	
	Transaction(String name,int timestamp,TransactionType type){
		this.name=name;
		this.timestamp=timestamp;
		this.type=type;
		this.state=TransactionState.Active;
	}
	
	abstract void block();
	abstract int  read (Variable v,Site site);
	abstract boolean write(Variable variable, int value);
	abstract boolean abort ();
	abstract boolean commit ();
	
}

/**
 * 
 */
package edu.nyu.ads;

import java.util.*;
/**
 * @author Rameez
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
	abstract int  read (String variable);
	abstract void write(String variable, int value);
	abstract Map<String,String> end();
	
}

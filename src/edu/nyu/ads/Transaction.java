/**
 * 
 */
package edu.nyu.ads;

/**
 * @author Sahil Vora
 *
 */
public abstract class Transaction {
	String name;
	String id;
	int timestamp;
	String type;
	Transaction(String name,String id,int timestamp,String type){
		this.name=name;
		this.id=id;
		this.timestamp=timestamp;
		this.type=type;
	}
	abstract int  read (Variable v,Site site);
	abstract boolean write(Variable variable, int value);
	abstract boolean abort ();
	abstract boolean commit ();

}

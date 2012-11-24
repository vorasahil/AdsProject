package edu.nyu.ads;
import java.util.*;
import java.io.*;
public class TransactionManager {

	Map<Variable,Site> varToSite;
	Map<Integer,Site> sites;
	Map<String,Transaction> transactions;
	Set<Transaction> activeTransactions;
	Map<Variable,List<Transaction>> blockedTransactions;
	LockManager lockManager;
	File outputfile;
	static int timestamp;
                  
	boolean lock(String s,Variable v){
		
		return false;
	}
	
	boolean begin(String s){
		System.out.println("begin "+s);
		return false;
	}
	
	boolean beginRO(String s){
		System.out.println("beginRO"+s);
		return false;
	}
	
	boolean end(String s){
		System.out.println("end "+s);
		return false;
	}
	
	boolean read(String s, String v){
		System.out.println("read" +" "+s+" "+v);
		return false;
	}
	
	
	boolean write(String s, String v, int value)
	{
		System.out.println("write "+s+" "+" "+v+" "+value);
		return false;
	}
	
	void dump(){
		System.out.println("dump");
	}
	
	void dump(int site){
		System.out.println("dumpsite"+site);
	}
	
	void dump(String variable){
		System.out.println("dumpvariable"+variable);
	}
	
	void fail(int site){
		System.out.println("fail"+site);
	}
	
	void recover(int site){
		System.out.println("recover"+site);
	}
	
	void queryState(){
		
	}

	
}

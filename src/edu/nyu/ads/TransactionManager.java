package edu.nyu.ads;
import java.util.*;
import java.io.*;
public class TransactionManager {
	PrintWriter out;
	Map<String,List<Site>> varToSite;
	Map<Integer,Site> sites;
	//Map<String,Transaction> transactions;
	Map<String,Transaction> transactions;
	Map<Variable,List<Transaction>> blockedTransactions;
	LockManager lockManager;
	String outputFile;
	static int timestamp;
                  
	TransactionManager(Map<String,List<Site>> varToSite,Map<Integer,Site> sites, LockManager lockManager,String outputFile) throws IOException{
		varToSite = new HashMap<String,List<Site>>(varToSite);
		sites = new HashMap<Integer,Site>(sites);
		this.outputFile=outputFile;
		out = new PrintWriter(new FileWriter(outputFile));
		this.lockManager=lockManager;
		//transactions=new HashMap<String,Transaction>();
		transactions=new HashMap<String,Transaction>();
		blockedTransactions=new HashMap<Variable,List<Transaction>>();
		
	}
	
	boolean lock(String s,Variable v){
		
		return false;
	}
	
	void begin(String s,int timestamp){
		Transaction temp=new ReadWriteTransaction(s, timestamp);
		transactions.put(s,temp);
	}
	
	void beginRO(String s,int timestamp){
		Map<String,Variable> variables=new HashMap<String,Variable>();
		for(String var:varToSite.keySet()){
			
			List<Site> siteList=varToSite.get(var);
			
			FOR: for(Site sit:siteList){
				if(sit.isUp()){
					Variable v=null;
					try{
						v=sit.readVariable(var);	
					}
					catch(IllegalStateException e){
					continue;
					}
					variables.put(v.getName(),v);
					break FOR;
				}
				
			}
			
			//check variables map for the entry of this variable
			//if not found, do what prof says...(doubt)
		}
		Transaction temp=new ReadOnlyTransaction(s,timestamp,variables);
		transactions.put(s, temp);
	}
	
	boolean end(String s,int timestamp){
		System.out.println("end "+s);
		return false;
	}
	
	boolean read(String s, String v,int timestamp){
		System.out.println("read" +" "+s+" "+v);
		return false;
	}
	
	
	boolean write(String s, String v, int value,int timestamp)
	{
		System.out.println("write "+s+" "+" "+v+" "+value);
		return false;
	}
	
	void dump(int timestamp){
		out.println("COMPLETE DUMP AT TIMESTAMP= "+timestamp);
		for(Integer siteNum:sites.keySet()){
			dump(siteNum);
		}
		
	}
	
	
	void dump(int siteNum,int timestamp){
		out.println("DUMPing Site= "+siteNum+" at TimeStamp "+timestamp);
		
			Site site=sites.get(siteNum);
			if(site.isUp()){
				out.println(siteNum+" variables are as follows:");
				out.println(site.dump());
			}
			else{
				out.println(siteNum+" is DOWN.");
			}
			
	}
	
	void dump(String variable,int timestamp){
		out.println("Dumping Variable "+variable+" at timestamp"+timestamp);
		List<Site> siteList=varToSite.get(variable);
			
		for(Site sit:siteList){
			if(sit.isUp()){
				try{
					Variable v=sit.readVariable(variable);	
					out.println("At Site "+sit+": "+v.getValue());
				}
				catch(IllegalStateException e){
					out.println("At Site "+sit+": CANNOT READ, STALE COPY.");
					continue;
				}
			}
			else{
				out.println("Site "+sit+" IS DOWN.");
			}
		}	
		
	}
	
	void fail(int site,int timestamp){
		System.out.println("fail"+site);
	}
	
	void recover(int site,int timestamp){
		System.out.println("recover"+site);
	}
	
	void queryState(){
		
	}

	
}

package edu.nyu.ads;
import java.io.IOException;
import java.util.*;
public class Architecture {
	LockManager lockManager=new LockManager();
	
	void initialize() throws IOException{
		Map<String,List<Site>> varToSite=new HashMap<String,List<Site>>();
		Map<Integer,Site> sites=new HashMap<Integer,Site>();
		Map<String,Variable> variables=new HashMap<String,Variable>();
		
		for(int i=1;i<=20;i++){
			String variableName="x"+i;
			Variable v=new Variable(variableName,(i*10));
			variables.put(variableName,v);
			List<Site>list=new ArrayList<Site>();
			varToSite.put(variableName, list);
		}
		
		for(int i=1;i<=10;i++){
			ArrayList<String> arr=new ArrayList<String>();
			Map<String,Variable> map=new HashMap<String,Variable>();
			Map<String,Transaction> lockTable=new HashMap<String,Transaction>();
			Map<String,List<Transaction>> readLockTable=new HashMap<String,List<Transaction>>();

				if(i%2==1){
					for(int j=2;j<=20;j+=2){
						String x="x"+j;
						map.put(x, variables.get(x));
						arr.add(x);
						
					}		
				}
				else{
					for(int j=1;j<=20;j++){
						if(j%2==0){
							String x="x"+j;
							map.put(x, variables.get(x));
							arr.add(x);
						}
						if(1+(j%10)==i){
							String x="x"+j;
							map.put(x, variables.get(x));
							arr.add(x);
						}
					}
					
				}
				
			Site s=new Site(i, map,lockTable,readLockTable);
			sites.put(i,s);
			lockManager.addLocktable(lockTable);
			lockManager.addReadLocktable(readLockTable);
			for(String str:arr){
				varToSite.get(str).add(s);
			}
		}
		
		Runner r=new Runner("input.txt",varToSite,sites,lockManager,"outputFile.txt");
		r.run();
		
	}
	
	public static void main(String args[])throws Exception{
		new Architecture().initialize();

	}
}


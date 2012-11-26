package edu.nyu.ads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetUp {

	void initialize(){
		Site sites[] = new Site[11];
		ArrayList<HashMap<String, Variable>> valuesAtSites = new ArrayList<HashMap<String, Variable>>();
		ArrayList<HashMap<String,Transaction>> sitesLockTable = new ArrayList<HashMap<String,Transaction>>();
		HashMap<String,List<Site>> varToSite = new 	HashMap<String,List<Site>>();
		HashMap<Integer,Site> sitesMap  = new HashMap<Integer,Site>();
		HashMap<String, Variable> valueAtSite;
		HashMap<String,Transaction> siteLockTable;
		ArrayList<Site> siteVarList;
		LockManager lockManager = new LockManager();
		String varName;
		String outputFile = "outputFile";
		String inputFile = "inputFile";
		
		int  varValue;
		for(int  i =1 ;i<=10;i++){
			valueAtSite = new HashMap<String,Variable>();
			valuesAtSites.add(i,valueAtSite);	
		}
		for(int  i =1 ;i<=10;i++){
			siteLockTable = new HashMap<String,Transaction>();
			sitesLockTable.add(siteLockTable);
			lockManager.addLocktable(siteLockTable);
		}
		
		for(int i = 1;i<=20;i++){
			siteVarList = new ArrayList<Site>();
			varToSite.put("x"+i,siteVarList);
			
		}
		for(int i = 2;i<=20;i = i+2){
			varName = "x"+i;
			varValue = 10*i;
			Variable variable = new Variable(varName,varValue);
			variable.setIsReplicated(true);
			for(int j = 1;j<=10;j++){
				valuesAtSites.get(j).put(varName,variable);	
				
			}
		}
		
		for(int i = 1;i<=19;i = i+2){
			varName = "x"+i;
			varValue = 10*i;
			Variable variable = new Variable(varName,varValue);
			valuesAtSites.get(1+i%10).put(varName,variable);	
			
		}
		for(int  i = 2;i<=20;i = i+2){
			varName = "x"+i;
			for(int j = 1;j<=10;j++){
				varToSite.get(varName).add(sites[j]);
			}
		}
		
		for(int i = 1;i<=19;i = i+2){
			varName = "x"+i;
			varToSite.get(varName).add(sites[1+i%10]);
		}
		
		for(int i = 1;i<=10;i++){
			sites[i] = new Site(i,valuesAtSites.get(i),sitesLockTable.get(i));
			sitesMap.put(i,sites[i]);
		}
		
		
		try {
			Runner r  = new Runner(inputFile,varToSite,sitesMap,lockManager,outputFile);
			r.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

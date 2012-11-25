package edu.nyu.ads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUp {

	void initialize(){
		Site sites[] = new Site[11];
		ArrayList<HashMap<String, Variable>> valuesAtSites = new ArrayList<HashMap<String, Variable>>();
		ArrayList<HashMap<String,Transaction>> sitesLockTable = new ArrayList<HashMap<String,Transaction>>();
		HashMap<String, Variable> valueAtSite;
		HashMap<String,Transaction> siteLockTable;
		String varName;
		
		int  varValue;
		for(int  i =1 ;i<=10;i++){
			valueAtSite = new HashMap<String,Variable>();
			valuesAtSites.add(i,valueAtSite);	
		}
		for(int  i =1 ;i<=10;i++){
			siteLockTable = new HashMap<String,Transaction>();
			sitesLockTable.add(siteLockTable);
		}
		
		for(int i = 2;i<=20;i = i+2){
			varName = "x"+i;
			varValue = 10*i;
			Variable variable = new Variable(varName,varValue);
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
		
		for(int i = 1;i<=10;i++){
			sites[i] = new Site(i,valuesAtSites.get(i),sitesLockTable.get(i));
		}
		
	}
	
}

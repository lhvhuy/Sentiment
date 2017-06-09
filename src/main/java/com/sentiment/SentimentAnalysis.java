package com.sentiment;

import java.io.BufferedReader;    //  read file
import java.io.FileReader;        //  
import java.io.IOException;       // 
import java.io.File;			  // check exists file
import java.util.*;				  // Map

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class SentimentAnalysis {

	public final static String FILENAME = "D:\\Content.txt";
	public final static String DICTIONARY = "D:\\DictionaryFeeling.txt";
	
	public static void main(String[] args) {
		Sentiment senti = new Sentiment(FILENAME, DICTIONARY);
		//case Content file isn't exists
		if (senti.getContent() == null){
			System.out.println("File content is not exists"); 
			return;
		}
		//case Dictionary file isn't exists
		if (senti.getDictionary() == null){
			System.out.println("File dictionary is not exists"); 
			return;
		}
		
		//print Content
		System.out.println("---------------Content-----------------");
		List<String> listStr = senti.getContent();
		for (int count = 0; count < listStr.size(); count++)
			System.out.println(listStr.get(count));
		
		//print Dictionary
		System.out.println("---------------Dictionary-----------------");
		Map<String, Integer> mapTmp = new HashMap<String, Integer>();
		mapTmp = senti.getDictionary();
		for (String key : mapTmp.keySet())
			System.out.println(key + " " + mapTmp.get(key));
		
		// load up the knowledge base
	    KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        
        //fire all rules
        kSession.insert(senti);
        kSession.fireAllRules(); 
	}
	
	/* 
	 * 		SENTIMENT CLASS
	 * */
	
	public static class Sentiment{
		private List<String> content;
		private Map<String, Integer> dictionary;
		private int positive = 0;
		private int negative = 0;
		
		/*	    GETTER AND SETTER
		 * 
		 */
		public void setContent(List<String> listStr){
			content = listStr;
		}
		
		public List<String> getContent(){
			return content;
		}
		
		public void setDictionaryLv(Map<String, Integer> map){
			dictionary = map;
		}
		
		public Map<String, Integer> getDictionary(){
			return dictionary;
		}
		
		public void setPositive(int posi){
			positive += posi;
		}
		
		public int getPositive(){
			return positive;
		}
		
		public void setNegative(int nega){
			negative += nega;
		}

		public int getNegative(){
			return negative;
		}
		
		/*		CONSTRUCTOR
		 * */
		Sentiment(String nameFile, String dictionaryFile) {
			content = readFile(nameFile);
			dictionary = readFileDictionary(dictionaryFile);
		}
		
		/*		PROCESS FUNCTION
		 * 
		 * */
		
		/* Read dictionary file with format WORD WEIGHTED
		 * For example:  Good 5
		 *               Bad -5
		 */ 
		public Map<String, Integer> readFileDictionary(String fileName){
			BufferedReader br = null;
			FileReader fr = null;
			Map<String, Integer> dictionary = new HashMap<String, Integer>();
			
			try {
				/*   Declare Variables
				 * 
				 * */
				File f = new File(fileName);
				if (!f.exists())
					return null;
				fr = new FileReader(fileName);
				
				br = new BufferedReader(fr);
				String sCurrentLine;
			
				/* Read file
				 * */
				String[] strs;
				while ((sCurrentLine = br.readLine()) != null) {
					strs = sCurrentLine.trim().split("\\s+");
					dictionary.put(strs[0], Integer.parseInt(strs[1]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
					if (fr != null)
						fr.close();	
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return dictionary;
		}
		
		//Check specific word
		public void sentimentCheckword(String keyword){
			List<String> listStr = this.getContent();
			for (int count = 0; count < listStr.size(); count++)
				if (listStr.get(count).contains(" " + keyword + " "))
					System.out.println("Keyword: \"" + keyword + "\" met at line " + (count + 1)
							+ " position " + listStr.get(count).indexOf(" " + keyword + " "));
		}

		//get position of keyword, return map<String, Integer>
		public Map<String, Integer> getPosition(String keyword){
			Map<String, Integer> pos = new HashMap<String, Integer>();
			List<String> listStr = this.getContent();
			for (int count = 0; count < listStr.size(); count++)
				for (int i = -1, times = 1; (i = listStr.get(count).indexOf(" " + keyword + " ", i + 1)) != -1; times++){
					 pos.put(Integer.toString(count + 1) + "(" + times + ")", i);
				}
			return pos;
		}
		
		/*	Read file content 
		 * 
		 */
		public static List<String> readFile(String fileName){
			BufferedReader br = null;
			FileReader fr = null;
			List<String> text = new Vector<String>();
			
			try {
				/*   Declare Variables
				 * 
				 * */
				File f = new File(fileName);
				if (!f.exists())
					return null;
				fr = new FileReader(fileName);
				
				br = new BufferedReader(fr);
				String sCurrentLine;
			
				/* Read file
				 * */
				while ((sCurrentLine = br.readLine()) != null) {
					text.add(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
					if (fr != null)
						fr.close();	
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return text;
		}
		
	}
}

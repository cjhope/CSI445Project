import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;


public class SemanticTest {
	static Scanner sc = new Scanner(System.in);
	static Hashtable<String, pair> words = new Hashtable<String, pair>();
	
	@SuppressWarnings("resource")
	public static void main(String [] args){
		String[] result;
		String line;
		int printCount;
		String printPOS;
		//String outputFileString = "results";
		String outputFilePath = "/Users/cjh/SUNY/CSI445_DMS/text_analysis/results/result";
		
		/**
		 * Try block handles all the operations, opens a file to read and write
		 */
		try {
			
			//Get all files in the folder
			File folder = new File("/Users/cjh/SUNY/CSI445_DMS/text_analysis/data/formattedData/");
			File[] listOfFiles = folder.listFiles();
			
			for(int i = 0; i < listOfFiles.length; i++){
				//System.out.println(listOfFiles[i]);
				
					
					BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]));
					FileWriter writer = new FileWriter(outputFilePath+i+".txt");
					BufferedWriter bw = new BufferedWriter(writer);
					
					//Primes the buffered reader
					line = br.readLine();
					while(line != null){
					result = line.split("\t"); //Splits the input line on a tab
					line = br.readLine();
					
					
					//Checks if the word already has an entry in the hash table
					//if so, adds one to the count, otherwise makes a new entry with count 1
					if(words.containsKey(result[2])){
						pair p = (pair)words.get(result[2]);
						p.count = p.count + 1;
						words.put(result[2], p);
					}
					else{
						pair p = new pair(result[1], 1);
						words.put(result[2], p);
					}
				}//end while - no longer reads input
					
				//returns an iterator over the hash table, writes the key-value mappings to the output file	
				Iterator<Map.Entry<String, pair>> it = words.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, pair> value = it.next();
					pair p = value.getValue();
					String s = String.format("Word: %-18s\tPart Of Speech: %5s\tCount: %4d", value.getKey(), p.partOfSpeech, p.count);
					//System.out.println(s);
					bw.write(s + '\n');
				}//end while
				
				bw.close();
				br.close();
			}//end for loop
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Didn't opent the file right");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end main
	
	
}//end class

class pair{
	
	int count; //count of how many times the word has appeared
	String partOfSpeech; //describes the part of speech
	
	public pair(String pos, int x){
		this.partOfSpeech = pos;
		this.count = x;
	}
}//end pair class
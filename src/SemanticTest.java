import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;

/**
 * The SemanticTest class reads all formatted text files in a directory (generated using the TreeTagger program) and
 * creates a hashtable that stores a count of each word that appears in the files along with the part of speech of the word
 * 
 * Once the hashtable is created, a txt file is created for each input file and placed in the results directory
 * 
 * @throws IOException
 * 
 * @author cjh
 *
 */
public class SemanticTest {
	Hashtable<String, Pair> wordList = new Hashtable<String, Pair>();
	Hashtable<String, ProbIdent> probability = new Hashtable<String, ProbIdent>();
	static HashSet<String> dictionary = new HashSet<String>();
	

	public static void main(String [] args) throws IOException{
		String inputFilePath = "/Users/cjh/SUNY/CSI445_DMS/text_analysis/data/formattedData/"; //set up to use absolute path name
		String outputFilePath = "/Users/cjh/SUNY/CSI445_DMS/text_analysis/results/resultFile"; //set up to use absolute path name
		String probDiffFilePath = "/Users/cjh/SUNY/CSI445_DMS/text_analysis/results/probDifference"; //set up to use absolute path name
		File folder = new File(inputFilePath);  //gets the folder name
		File[] listOfFiles = folder.listFiles();  //gets all files in the specified folder
		SemanticTest aggregate = new SemanticTest(); //creates the aggregated SemanticTest object
		
		
		//Create array the size of the number of input files
		SemanticTest[] collection = new SemanticTest[listOfFiles.length];
		
		System.out.println("Beginning collation of word frequency data...");
		
		//Iterate over all the files in the folder, create a SemanticTest object for each file
		//Passes the input file to createHashTable, which sets the hashTable for each SemanticTest object
		for(int i = 0; i < listOfFiles.length; i++){
			collection[i] = new SemanticTest();
			try{
			collection[i].createHashtable(listOfFiles[i]);
			} catch (Exception e){
				System.err.println("Error in main function on iteration " + (i+1));
				e.printStackTrace();
			}//end try-catch
		}//end for - all individual SemanticTest objects created and populated
		
		System.out.println("Updating raw probability data for writing to individual files...");
		
		//Handles writing the wordLists to file
		for(int i = 0; i < collection.length; i++){
			NaiveBayesClassifier.updateProbability(collection[i]);
			File fout = new File(outputFilePath + (i+1) + ".txt");
			collection[i].writeWordListToFile(fout);
		}//end for - probabilities set in individual SemanticTest objects and data written to individual files
		
		System.out.println("Aggregating data...");
		
		//Combines the computed files into an aggregated hashtable
		aggregate.wordList = Combiner.combineWordLists(collection);
		//Updates the raw probability for the aggregated hashtable
		NaiveBayesClassifier.updateProbability(aggregate);
		
		System.out.println("Writing aggregated data to file...");
		//Writes aggregated data to file
		aggregate.writeWordListToFile(new File(outputFilePath + "Aggregation.txt"));
		
		
		//Computes probability to data and writes to file
		//The probability Hashtable for each SemanticTest object is a subset of the wordList Hashtable
		//that only contains words that occur with a higher probability in the individual SemanticTest object
		//than in the aggregated dataset, with the threshold being set by the passed epsilonValue in the 
		//NaiveBayesClassifier.findProbabilityDiscrepencies method
		System.out.println("Writing probability differences to file...");
		for(int i = 0; i < collection.length; i++){
			File pointer = new File(probDiffFilePath + (i+1) + ".txt");
			collection[i].probability = NaiveBayesClassifier.findProbabilityDiscrepencies(aggregate, collection[i], 0.01);
			collection[i].writeProbDiffToFile(pointer);
		}//end for
		
		//Build the dictionary
		Combiner.buildDictionary(collection);
		
		//Write the dictionary to file
		writeDictionary(new File("/Users/cjh/SUNY/CSI445_DMS/text_analysis/results/Dictionary.txt"));
		
		System.out.println("Task complete. Exiting...");
		return;
		
	}//end main
	
	
	/**
	 * createHashTable sets the value of the SemanticTest object's hash table based on passed files
	 * @param fin
	 * @return
	 * @throws IOException 
	 */
	void createHashtable(File fin) throws IOException{
		String line; //holds each line of the input
		String[] lineElements; //holds the parsed values of line
		Hashtable<String, Pair> hTable = new Hashtable<String, Pair>(); //the Hashtable that will populate this.wordList
		BufferedReader bReader = new BufferedReader(new FileReader(fin));

		//Prime buffered reader
		try{
		line = bReader.readLine();
		
		while( line != null ) { //While there is valid input
			lineElements = line.split("\t"); //split the line on tabs
			
			//Checks if the word is already in the hash table, if so adds one to the count
			if(hTable.containsKey(lineElements[2])){
				Pair p = hTable.get(lineElements[2]);
				p.count++;
				hTable.put(lineElements[2], p);
			}else{
				//If the current word is not in the hash table, add it
				Pair p = new Pair(lineElements[1], 1);
				hTable.put(lineElements[2], p);
			}
			line = bReader.readLine();
		}//end while - done reading input, hashTable fully formulated
		
		this.wordList = hTable; //set wordList to equal the constructed Hashtable
		
		bReader.close(); //close the reader
		}//end try
		
		catch(Exception e){
			System.err.println("Error with createHashTable");
			e.printStackTrace();
			return;
		}//end catch

	}//end SemanticTest method
	
	/**
	 * Writes contents of the wordList to a specified output file
	 * 
	 * @param fout
	 * @throws IOException
	 */
	void writeWordListToFile(File fout) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(fout));
		Iterator<Map.Entry<String, Pair>> it = this.wordList.entrySet().iterator();
		Map.Entry<String, Pair> value;
		String s = String.format("%-20s\t%5s\t%7s\t%9s", "WordID", "POS", "Count", "Prob");
		bWriter.write(s + "\n");
		while(it.hasNext()){
			value = it.next();
			s = String.format("%-20s\t%5s\t%7d\t%1.8f", value.getKey(), value.getValue().partOfSpeech, value.getValue().count, value.getValue().rawProbabilty);
			bWriter.write(s + "\n");
		}
		bWriter.flush();
		bWriter.close();
		
	}//end writeWordListToFile method
	
	void writeProbDiffToFile(File fout) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(fout));
		Iterator<Map.Entry<String, ProbIdent>> it = this.probability.entrySet().iterator();
		Map.Entry<String, ProbIdent> value;
		String s = String.format("%-20s\t%5s\t%10s", "WordID", "POS", "Prob Diff");
		bWriter.write(s + "\n");
		while(it.hasNext()){
			value = it.next();
			s = String.format("%-20s\t%5s%1.9f", value.getKey(), value.getValue().partOfSpeech, value.getValue().probDifferential);
			bWriter.write(s + "\n");
		}//end while
		bWriter.flush();
		bWriter.close();
	}//end writeProbDiffToFile method
	
	
	/**
	 * Writes the contents of the calculated dictionary to the file passed by fout parameter
	 * @param fout
	 * @throws IOException
	 */
	static void writeDictionary(File fout) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(fout));
		System.out.println(dictionary.size());
		for( String s : dictionary){
			bWriter.write(s + "\n");
		}
		bWriter.flush();
		bWriter.close();
	}//end writeDictionary method
	
	/**
	 * Constructs the SemanticTest object
	 * @param hTable Sets the value of wordList Hashtable
	 */
	public SemanticTest(Hashtable<String, Pair> hTable){
		this.wordList = hTable;
	}//end constructor
	
	/**
	 * Default constructor sets the value of wordList to null
	 */
	public SemanticTest(){
		this.wordList = null;
	}
}//end SemanticTest class

/**
 * Sets up a custom class to populate the value side of the Hashtable
 * count field denotes how many times a given word has occurred
 * partOfSpeech identifies the part of speech of the word
 * 
 * @author cjh
 *
 */
class Pair{
	
	int count; //count of how many times the word has appeared
	String partOfSpeech; //describes the part of speech
	double rawProbabilty = 0.0;
	
	/**
	 * Builds a pair object, sets the values based on passed parameters
	 * 
	 * @param partOfSpeech
	 * @param x
	 */
	public Pair(String partOfSpeech, int x){
		this.partOfSpeech = partOfSpeech;
		this.count = x;
	}//end constructor
	
}//end pair class

class ProbIdent{
	String partOfSpeech;
	double probDifferential;
	
	public ProbIdent(String partOfSpeech, double probDiff){
		this.partOfSpeech = partOfSpeech;
		this.probDifferential= probDiff;
	}//end constructor
	
}//end class

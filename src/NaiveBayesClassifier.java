import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides methods to implement the Naive Bayes Classifier Methods on the data set
 * 
 * @author cjh
 *
 */
public class NaiveBayesClassifier {

	/**
	 * Updates the raw probability field in each Pair kept in a passed Hashtable
	 * 
	 * @param semTest - the SemanticTest object whose probabilities are being updated
	 */
	static void updateProbability(SemanticTest semTest){
		int totalWordCount = 0; //initiate total word count
		Iterator<Map.Entry<String, Pair>> it = semTest.wordList.entrySet().iterator();
		//While the Hashtable has remaining values, count the total number of words
		while(it.hasNext()){
			Map.Entry<String, Pair> value = it.next();
			totalWordCount+=value.getValue().count;
		}//end while - total number of words in all text files counted
		
		//Now computes the random probability that a given word appears in the data set
		it = semTest.wordList.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Pair> value = it.next();
			Pair p = value.getValue();
			p.rawProbabilty = ((double)p.count/totalWordCount);
			value.setValue(p);
		}//end while - probabilities set
	}//end updateProbability method
	
	
	/**
	 * Populates and returns a Hashtable that contains only words that have a probability differential greater than specified by the user.
	 * The goal is to find words that are more likely to appear in a given subset of the data than the dataset as a whole
	 * 
	 * @param masterSet - the set of data
	 * @param checkSet - the subset of the data to be examined
	 * @param epsilonValue - the maximum bound of the probability differential (Condition: 0 < epsilonValue < 1);
	 * @return
	 */
	static Hashtable<String, ProbIdent> findProbabilityDiscrepencies(SemanticTest masterSet, SemanticTest checkSet, double epsilonValue){
		Hashtable<String, ProbIdent> hTable = new Hashtable<String, ProbIdent>(); //the Hashtable to be returned
		Iterator<Map.Entry<String, Pair>> checkSetIter = checkSet.wordList.entrySet().iterator();
		
		if(epsilonValue > 1 || epsilonValue < 0){
			System.err.println("Error: cannot check for a probability differential of: " + epsilonValue);
			return null;
		}
		
		//Loop runs over all elements in the checkSet, which is a subset of the masterSet
		//and checks the difference between the probability of a word appearing in a particular Hashtable
		//against the probability of the word appearing in the entire dataset
		while(checkSetIter.hasNext()){
			Map.Entry<String, Pair> checkSetValue = checkSetIter.next();
			//Just a check to make sure no errors in the passing of the masterSet and checkSet
			if(masterSet.wordList.containsKey(checkSetValue.getKey())){
				Pair checkSetPair = checkSetValue.getValue();
				Pair masterSetPair = masterSet.wordList.get(checkSetValue.getKey()); //gets the Pair from the masterSet that corresponds to the key from the checkSet
				if(checkSetPair.rawProbabilty - masterSetPair.rawProbabilty >= epsilonValue){
					ProbIdent elem = new ProbIdent(masterSetPair.partOfSpeech, (checkSetPair.rawProbabilty - masterSetPair.rawProbabilty));
					hTable.put(checkSetValue.getKey(), elem);
				}//end if - checks if the probability differential is greater than the passed epsilonValue, creates
				//a new ProbIdent element and populates the hTable with it
			}//basically a check to avoid null values, this should always evaluate to true based on construction of the hashtables
		}//end while
		
		return hTable;
	}//end findProbabilityDiscrepencies method
	
}//end class

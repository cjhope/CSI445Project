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
	 * @param semTest - 
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
	
}//end class

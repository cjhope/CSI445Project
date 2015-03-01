import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;

/**
 * Designed to aggregate the set of SemanticTest objects created in the SemanticTest program
 * to a single hashtable, with the total frequency of each word stored in Pair
 * 
 * @author cjh
 *
 */
public class Combiner{

	/**
	 * Reads the hashtables from a set of SemanticTest objects and creates one
	 * and combines them into one hashtable
	 * 
	 * @param wordLists - An array of SemanticTest objects
	 * @return hTable - the hashtable that contains the aggregated statistics of all hashtables in the set of 
	 * 					SemanticTest objects
	 */
	static Hashtable<String, Pair> combineWordLists(SemanticTest[] wordLists){
		Hashtable<String, Pair> aggregate = new Hashtable<String, Pair>();
		Iterator<Map.Entry<String, Pair>> it;
		
		//cover all the wordLists in wordLists
		for(int i = 0; i < wordLists.length; i++){
			//Iterate over all values in each wordList
			it = wordLists[i].wordList.entrySet().iterator();
			
			//Checks if the current wordList has additional values, while it does, continues updating
			while(it.hasNext()){
				Map.Entry<String, Pair> value = it.next();
				if(aggregate.containsKey(value.getKey())){
					Pair p = value.getValue();
					p.count+= aggregate.get(value.getKey()).count;
					aggregate.put(value.getKey(), p);
				} else {
					Pair p = new Pair(value.getValue().partOfSpeech, value.getValue().count);
					aggregate.put(value.getKey(), p);
				}//end else
			}//end while
			
		}//end for
		
		return aggregate;
	}//end combineWordLists method
	
	
	/**
	 * Takes the words with outlier probabilities in each dataset and uses them to
	 * formulate a dictionary of target words
	 * 
	 * @param probLists - the collection of SemanticTest objects to be evaluated
	 */
	static void buildDictionary(SemanticTest[] probLists){
		for(int i = 0; i < probLists.length; i++){
			Iterator<Map.Entry<String, ProbIdent>> it = probLists[i].probability.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, ProbIdent> value = it.next();
				if(!SemanticTest.dictionary.contains(value.getKey()))
					SemanticTest.dictionary.add(value.getKey());
			}
		}//end for
	}//end buildDictionary method
	
}//end class
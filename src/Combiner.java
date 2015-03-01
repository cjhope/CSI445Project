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
					p.count++;
					aggregate.put(value.getKey(), p);
				} else {
					Pair p = new Pair(value.getValue().partOfSpeech, value.getValue().count);
					aggregate.put(value.getKey(), p);
				}//end else
			}//end while
			
		}//end for
		
		return aggregate;
	}//end combineWordLists method
	
}//end class
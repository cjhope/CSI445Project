import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;


public class Combiner{

	static Hashtable<String, Pair> combineWordLists(SemanticTest[] wordLists){
		Hashtable<String, Pair> aggregate = new Hashtable<String, Pair>();
		Iterator<Map.Entry<String, Pair>> it;
		//cover all the wordLists in wordLists
		for(int i = 0; i < wordLists.length; i++){
			it = wordLists[i].wordList.entrySet().iterator();
			
			
			//Checks if the aggregate function has 
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
			
		}
		
		
		return aggregate;
	}

	
}

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RepMapTest {
	public static void main(String[] args) {
		ReputationReader rr = new ReputationReader();
		Map<String, Double> repMap = rr.getRepMap("tmp\\reputations.json");
		
		Iterator<Entry<String, Double>> iter = repMap.entrySet().iterator();
		
		while(iter.hasNext()) {
			Entry<String, Double> entry = iter.next();
			
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}

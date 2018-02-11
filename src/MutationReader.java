import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MutationReader {
	public List<Mutation> getMutations(String filename) {
		JSONParser jsonParser = new JSONParser();
		List<Mutation> mutations = new ArrayList<Mutation>();
		
		try {
			Object obj = jsonParser.parse(new FileReader(filename));
			JSONObject jo = (JSONObject) obj;
			
			
			Iterator iter = jo.keySet().iterator();
			
			while(iter.hasNext()) {
				Object key = iter.next();
				JSONObject current = (JSONObject) jo.get(key);
				String author = (String) current.get("a");
				long time = (Long) current.get("t");
				
				JSONObject o = (JSONObject) current.get("o");
				Edit edit = this.createEdit(o);
				
				mutations.add(new Mutation(edit, author, time));
			}
			
			mutations.sort(new MutationComparator());
			
			return mutations;
		} catch(IOException | ParseException e) {
			return null;
		}
	}
	
	public Edit createEdit(JSONObject o) {
		List<Operation> operations = new ArrayList<Operation>();
		
		Iterator iter = o.keySet().iterator();
		
		while(iter.hasNext()) {
			
			Object op = o.get(iter.next());
			
			if(op instanceof String) {
				operations.add(new InsertOperation((String) op));
			}
			
			else {
				int num = ((Long) op).intValue();
				
				if(num < 0) {
					operations.add(new DeleteOperation(num*-1));
				}
				
				else {
					operations.add(new CursorOperation(num));
				}
			}
		}
		
		return new Edit(operations);
	}
	
	class MutationComparator implements Comparator<Mutation> {

		@Override
		public int compare(Mutation mut1, Mutation mut2) {			
			return (int) (mut1.getT() - mut2.getT());
		}
		
	}
}

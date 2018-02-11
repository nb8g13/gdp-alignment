import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReputationReader {
	
	public Map<String, Double> getRepMap(String filename) {
		
		Map<String, Double> repMap = new HashMap<String, Double>();
		
		JSONParser jp = new JSONParser();
		
		try {
			JSONObject jo = (JSONObject) jp.parse(new FileReader(filename));
			
			Iterator iter = jo.keySet().iterator();
			
			while(iter.hasNext()) {
				String uuid = (String) iter.next();
				if(!uuid.equals("admin")) {
					double rep = ((Long) jo.get(uuid)).doubleValue();
					repMap.put(uuid, rep);
				}
			}
			
			return repMap;
		} catch (IOException | ParseException e) {
			return null;
		}
		
	}
	
}

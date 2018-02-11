import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONObject;

public class ReputationWriter {
	public void writeReputations(String filename, Map<String, Double> reputations) {
		try {
			FileWriter fw = new FileWriter(filename);
			fw.write(JSONObject.toJSONString(reputations));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

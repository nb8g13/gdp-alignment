import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class WinnerWriter {
	public void writeWinner(String filename, String winner) {
		JSONObject jo = new JSONObject();
		jo.put("winner", winner);
		
		try {
			FileWriter fw = new FileWriter(filename);
			fw.write(jo.toJSONString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

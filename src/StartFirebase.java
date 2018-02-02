import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

//Utility class that connects to the firebase backend
public class StartFirebase {
	
	public static void connect() {
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(
					"tmp\\test-project-96190-firebase-adminsdk-f5lx8-3d82f9bd49.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://test-project-96190.firebaseio.com/")
					.build();
			FirebaseApp.initializeApp(options);
			System.out.println("Done connecting");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

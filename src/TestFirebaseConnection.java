import org.apache.log4j.BasicConfigurator;

public class TestFirebaseConnection {
	public static void main(String[] args) {
		StartFirebase.connect();
		BasicConfigurator.configure();
		System.out.println("firebase loaded");
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
		
		FirebaseDataReader fbdr = new FirebaseDataReader(new PluralityConsensus(
				SubstitutionMatrix.LEVENSHTEIN(alphabet),
				new CapsNoPunctuation(),
				new PluralityPicker()
				));
		
		fbdr.update("-L4wW17hQ7OiQjo3J51l", "-L4wW1941-3ihAs7Jdll");
	}

}

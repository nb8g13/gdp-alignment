
public class TestFirebaseConnection {
	public static void main(String[] args) {
		StartFirebase.connect();
		System.out.println("firebase loaded");
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
		
		FirebaseDataReader fbdr = new FirebaseDataReader(new PluralityConsensus(
				SubstitutionMatrix.LEVENSHTEIN(alphabet),
				new CapsNoPunctuation(),
				new PluralityPicker()
				));
		
		fbdr.getMutationList("-L47J9-WHNEkGwjgbkSR", "-L47J916ZiWXlDjvb7qN");
		
		while(!fbdr.done) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

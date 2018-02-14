import org.apache.commons.text.similarity.LevenshteinDistance;

public class TestTreeBuilder {
	
	public static void main(String[] args) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
		double[][] subMatrix = new double[27][27];
		
		
		for (int i = 0; i < subMatrix.length; i++) {
			for (int j = 0; j < subMatrix.length; j++) {
				if (i == j) {
					subMatrix[i][j] = 0.0;
				}
				
				else {
					subMatrix[i][j] = -1.0;
				}
			}
		}
		
		SubstitutionMatrix subs = new SubstitutionMatrix(alphabet, subMatrix);
		Stage1Aligner aligner = new Stage1Aligner(subs);
		//String[] sequences = {"HERE-ANNOUNCE-ANOTHER-LIGHT-CHEESE", "FROM-HERE-ON-IN-I-WILL-TURN-OFF-THE-LIGHTS-AND-CUT-TO-THE-CHASE", "FROM-HERE-ON-IN-I-WILL-TURN-OFF-THE-LIGHTS-AND-UP-TO-THE-CHEESE"};
		String[] sequences = {"FROM HERE ON IN I WILL TURN OFF THE LIGHTS AND CUT TO THE CHASE", "FROM HERE ON IN I WILL TURN OFF THE LIGHTS AND UP TO THE CHEESE"};
		double[] reps = {0, 0, 0};
		
		String[] alignments = aligner.align(sequences, reps);
		
		String consensus = new FrequencyVoter().vote(alignments);
		
		System.out.println("alignments");
		for(int i = 0; i < alignments.length; i++) {
			System.out.println(alignments[i]);
		}
		
		LevenshteinDistance ld = new LevenshteinDistance();
		
		System.out.println(ld.apply("FROM HERE ON IN I WILL TURN OFF THE LIGHTS AND CUT TO THE CHASE", "FROM HERE ON IN I WILL TURN OFF THE LIGHTS AND UP TO THE CHEESE"));
		System.out.println(ld.apply(alignments[0], alignments[1]));
		
		System.out.println("Consensus \n" + consensus);
	}

}

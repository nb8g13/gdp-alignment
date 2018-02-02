
public class TestTreeBuilder {
	
	public static void main(String[] args) {
		System.out.println("Hello World");
		String alphabet = "ATCG";
		double[][] subMatrix = new double[4][4];
		
		
		for (int i = 0; i < subMatrix.length; i++) {
			for (int j = 0; j < subMatrix.length; j++) {
				if (i == j) {
					subMatrix[i][j] = 5.0;
				}
				
				else {
					subMatrix[i][j] = -10.0;
				}
			}
		}
		
		SubstitutionMatrix subs = new SubstitutionMatrix(alphabet, subMatrix, 8.0);
		Stage1Aligner aligner = new Stage1Aligner(subs);
		String[] sequences = {"ATATCCCGG", "AAATCGC", "TGAATCCCGC", "AAAAAACATATGATA"};
		
		String[] alignments = aligner.align(sequences);
		
		for(int i = 0; i < alignments.length; i++) {
			System.out.println(alignments[i]);
		}
		
		
	}

}

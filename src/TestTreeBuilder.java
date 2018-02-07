
public class TestTreeBuilder {
	
	public static void main(String[] args) {
		System.out.println("Hell World");
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
		
		SubstitutionMatrix subs = new SubstitutionMatrix(alphabet, subMatrix);
		Stage1Aligner aligner = new Stage1Aligner(subs);
		String[] sequences = {"ATATCCCGG", "AAATCGC", "TGAATCCCGC", "AAAAAACATATGATA", "AAATCGC"};
		
		String[] alignments = aligner.align(sequences);
		
		System.out.println("alignments");
		for(int i = 0; i < alignments.length; i++) {
			System.out.println(alignments[i]);
		}
		
		
		
	}

}

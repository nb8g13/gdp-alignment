import java.util.ArrayList;
import java.util.List;

public class TestAlignment {
	public static void main(String[] args) {
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
		
		ScoringSystem psp = new PSP();
		
		GapPenalty penaliser = new MUSCLEPenaliser(8, 1);
		
		ProfileMerger merger = new MUSCLEMerge(psp, subs, penaliser);
		
		Profile prof1 = new Profile(new Sequence("ATAT-CCGG"));
		Profile prof2 = new Profile(new Sequence("ATATCCCGG"));
		List<Sequence> alignments = new ArrayList<Sequence>(prof1.getSequences());
		alignments.addAll(prof2.getSequences());
		Profile prof3 = new Profile(prof1, prof2, alignments);
		Profile prof4 = new Profile(new Sequence("ACTAGCGG"));
		
		Profile output = merger.merge(prof3,  prof4);
		
		System.out.println("Left child: " + prof3);
		System.out.println("Right child: " + prof4);
		System.out.println("Alignment: " + output);
	}
}

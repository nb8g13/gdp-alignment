import java.util.ArrayList;
import java.util.List;

public class TestAlignment {
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
		
		ScoringSystem psp = new PSP();
		
		GapPenalty penaliser = new MUSCLEPenaliser(-8, 1);
		
		ProfileMerger merger = new MUSCLEMerge(psp, subs, penaliser);
		
		Profile prof1 = new Profile(new Sequence("HELLO WOLRD"));
		Profile profSame = new Profile(new Sequence("HELLO WALD"));
		Profile prof2 = new Profile(new Sequence("HELL NO WALD"));
		List<Sequence> alignments = new ArrayList<Sequence>(prof1.getSequences());
		alignments.addAll(prof2.getSequences());
		Profile prof3 = new Profile(prof1, prof2, alignments);
		Profile prof4 = new Profile(new Sequence("YO WALD ITS YA BOI YUNJIA COMIN AT YA LIVE"));
		Profile prof5 = new Profile(new Sequence("YO WALD ITS YA BOI THE YAKUZA"));
		
		
		Profile output = merger.merge(prof1,  prof2);
		
		System.out.println("Left child: " + prof3);
		System.out.println("Right child: " + prof4);
		System.out.println("Alignment: " + output);
		
		
	}
}

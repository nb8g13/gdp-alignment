import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Need to refactor and retain author history - or keep originals
/**
 * Merges profiles together, includes the main function, merge, and a couple others that it needs
 * @author lhs2g14
 *
 */
public class MUSCLEMerge implements ProfileMerger {
	
	ScoringSystem scoring;
	SubstitutionMatrix subs;
	GapPenalty gp;
	/**
	 * Constructor. 
	 * @param scoring ScoringSystem object. Decides how the substitution matrix will be used.
	 * @param subs SubstitionMatrix object. Used by the scoring system. Has the alphabet and matrix of sub costs in
	 * @param gp GapPenalty object. Encapsulates the system of scoring and penalising gaps.
	 * @return a MUSCLEMerge object
	 */
	public MUSCLEMerge(ScoringSystem scoring, SubstitutionMatrix subs, GapPenalty gp) {
		this.scoring = scoring;
		this.subs = subs;
		this.subs.adjustByExtension(gp.extenstionCost());
		this.gp = gp;
	}
	/**
	 * Main Merge function. Merges together two profiles using NeedleMan Wunsh and the given scoring, gap, and substitution methods
	 * @param left One of the profiles to be merged
	 * @param right The other profiles to be merged
	 * @return a single profile containing all the input profile's sequences but aligned to be the same length
	 */
	public Profile merge(Profile left, Profile right) {
		
		/*
		System.out.println("Start of Merge");
		System.out.println("Left");
		for (int i=0;i<left.getSequences().size();i++) {
			System.out.println(left.getSequences().get(i).getIndex());
		}
		System.out.println("Right");
		for (int i=0;i<right.getSequences().size();i++) {
			System.out.println(right.getSequences().get(i).getIndex());
		}*/
		
		int rows = left.getSequences().get(0).getSeq().length();
		int cols = right.getSequences().get(0).getSeq().length();
		
		List<Double> leftOpens = this.gp.openCost(left);
		//System.out.println("left opens");
		//this.printLists(leftOpens);
		List<Double> rightOpens = this.gp.openCost(right);
		//System.out.println("right opens");
		//this.printLists(rightOpens);
		List<Double> leftCloses = this.gp.closeCost(left);
		//System.out.println("left closes");
		//this.printLists(leftCloses);
		List<Double> rightCloses = this.gp.closeCost(right);
		//System.out.println("right closes");
		//this.printLists(rightCloses);
		
		// Stops error thrown at x = 1 and y = 1
		leftCloses.add(0,0.0);
		rightCloses.add(0, 0.0);
		
		//LX by LY
		double[][] M = new double[rows+1][cols+1];
		double[][] D = new double[rows+1][cols+1];
		double[][] I = new double[rows+1][cols+1];
		
		//Initial conditions
		
		for(int i = 1; i < rows+1; i++) {
			D[i][0] = 0.0;
		}
		
		for(int i = 1; i < cols+1; i++) {
			I[0][i] = 0.0;
		}
		
		
		I[0][0] = -Double.MAX_VALUE;
		
		D[0][0] = -Double.MAX_VALUE;
		
		M[0][0] = 0.0;
		
		for(int i = 1; i < rows+1; i++) {
			for (int j = 1; j < cols+1; j++) {
				
				// Pick new M value
				double sxy = scoring.score(left.getSequences(), right.getSequences(), this.subs, i-1, j-1);

				// Will cause index out of bounds right now
				double[] mOptions = {D[i-1][j-1] + leftCloses.get(i-1), I[i-1][j-1] + rightCloses.get(j-1), M[i-1][j-1]};
				M[i][j] = this.maxValue(mOptions) + sxy;
				
				//Pick new D value
				double dOptions[] = {M[i-1][j] + leftOpens.get(i-1), D[i-1][j]};
				D[i][j] = this.maxValue(dOptions);
				
				//Pick new I value
				double iOptions[] = {M[i][j-1] + rightOpens.get(j-1), I[i][j-1]};
				I[i][j] = this.maxValue(iOptions);
			}
		}
		
		String[] leftAlignments = new String[left.getSequences().size()];
		
		for(int i = 0; i < leftAlignments.length; i++) {
			leftAlignments[i] = "";
		}
		
		String[] rightAlignments = new String[right.getSequences().size()];
		
		for(int i = 0; i < rightAlignments.length; i++) {
			rightAlignments[i] = "";
		}
		
		/*
		System.out.println("M:");
		System.out.println(Arrays.deepToString(M));
		System.out.println("D:");
		System.out.println(Arrays.deepToString(D));
		System.out.println("I:");
		System.out.println(Arrays.deepToString(I));
		*/
		
		int x = rows;
		int y = cols;
		
		while(x != 0 && y != 0) {
			double[] options = {M[x][y], D[x][y], I[x][y]};
			double max = this.maxValue(options);
			
			//System.out.println("max value:" + max);
			
			if(max == M[x][y]) {
				this.prependLetters(leftAlignments, left, x-1);
				this.prependLetters(rightAlignments, right, y-1);
				x--;
				y--;
			}
			
			else if(max == D[x][y]) {
				this.prependLetters(leftAlignments, left, x-1);
				this.prependSpace(rightAlignments);
				x--;
			}
			
			else if (max == I[x][y]) {
				this.prependSpace(leftAlignments);
				this.prependLetters(rightAlignments, right, y-1);
				y--;
			}
		}
		
		// Fill in remaining spaces
		if (x != 0) {
			while(x > 0) {
				this.prependLetters(leftAlignments, left, x-1);
				this.prependSpace(rightAlignments);
				x--;
			}
		}
		
		else if (y != 0) {
			while(y > 0) {
				this.prependSpace(leftAlignments);
				this.prependLetters(rightAlignments, right, y-1);
				y--;
			}
		}
		
		
		
		for(int i = 0; i < leftAlignments.length; i++) {
			System.out.println("Left alignment " + i + ": " + leftAlignments[i]);
		}
		
		
		for(int i = 0; i < rightAlignments.length; i++) {
			System.out.println("right alignment " + i + ": " + rightAlignments[i]);
		}
		
		//Create all aligned sequences in one list
		List<Sequence> alignments = toSequenceList(rightAlignments, toSequenceList(leftAlignments, new ArrayList<Sequence>(), left),right);
		
		Profile parent = new Profile(left, right, alignments);
		
		return parent;
	
	}
	
	/**
	 * Returns the largest value in a list of doubles
	 * @param arr a list of doubles
	 * @return the largest value
	 */
	public double maxValue(double[] arr) {
		double max = arr[0];
		
		for(int i = 1; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];			
			}
		}
		
		return max;
	}
	/**
	 * Prepends a profile's sequences' characters to some semi built aligned strings. 
	 * @param arr an array of semi built aligned strings
	 * @param prof the profile those strings belong to
	 * @param idx the index of the character in the profile's strings that needs prepending
	 */
	public void prependLetters(String[] arr, Profile prof, int idx) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = prof.getSequences().get(i).getSeq().charAt(idx) + arr[i];
		}
	}
	/**
	 * Puts a space on the front of a list of strings
	 * @param arr an array of semi built aligned strings
	 */
	public void prependSpace(String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = '-' + arr[i];
		}
	}
	/**
	 * Wraps up an array of strings in sequence objects and adds them to a sequence List. preserves their index and reputation
	 * @param arr an array of strings
	 * @param sequences list of sequences that the strings will be added to
	 * @param profile the profile containing the strings and their index and rep values
	 * @return the given sequence list with all the strings in arr added to it
	 */
	public List<Sequence> toSequenceList(String[] arr, List<Sequence> sequences, Profile profile) {
		for(int i = 0; i < arr.length; i++) {
			sequences.add(new Sequence(arr[i],profile.getSequences().get(i).getIndex(), profile.getSequences().get(i).getRep()));
		}
		return sequences;
	}
	/**
	 * Prints each element in a list	 
	 * @param list the list of items to be printed
	 */
	public void printLists(List<Double> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
}

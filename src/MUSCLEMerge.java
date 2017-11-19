import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MUSCLEMerge implements ProfileMerger {
	
	ScoringSystem scoring;
	SubstitutionMatrix subs;
	GapPenalty gp;
	
	public MUSCLEMerge(ScoringSystem scoring, SubstitutionMatrix subs, GapPenalty gp) {
		this.scoring = scoring;
		this.subs = subs;
		this.subs.adjustByExtension(gp.extenstionCost());
		this.gp = gp;
	}
	
	public Profile merge(Profile left, Profile right) {
		
		int rows = left.getSequences().get(0).getSeq().length();
		int cols = right.getSequences().get(0).getSeq().length();
		
		List<Double> leftOpens = this.gp.openCost(left);
		List<Double> rightOpens = this.gp.openCost(right);
		List<Double> leftCloses = this.gp.closeCost(left);
		List<Double> rightCloses = this.gp.closeCost(right);
		
		//LX by LY
		double[][] scores = new double[rows][cols];
		
		
		double[] dPrev = new double[cols];
		double[] dCurr = new double[cols];
		
		double[] mPrev = new double[cols];
		double[] mCurr = new double[cols];
		
		double[] iPrev = new double[cols];
		double[] iCurr = new double[cols];
		
		
		for(int x = 0; x < scores.length; x++) {
			
			//Need to check the by0value, by-reference passing here
			dPrev = dCurr;
			mPrev = mCurr;
			iPrev = iCurr;
			
			for (int y = 0; y < scores[0].length; y++) {
				
				double sxy = this.scoring.score(left.getSequences(), right.getSequences(), this.subs, x, y);
				
				double[] mOptions; 
				
				if (x > 0 && y > 0) {
					mOptions = new double[3];
					mOptions[0] = mPrev[y-1];
					mOptions[1] = dPrev[y-1] + leftCloses.get(x-1);
					mOptions[2] = iPrev[y-1] + rightCloses.get(y-1);
					mCurr[y] = sxy + this.maxValue(mOptions);
				}
				
				else {
					mCurr[y] = sxy;
				}
				
				
				double[] dOptions = new double[2];
				
				dOptions[0] = dPrev[y];
				dOptions[1] = mPrev[y] + leftOpens.get(x);
				dCurr[y] = this.maxValue(dOptions);
				
				double[] iOptions = new double[2];
				
				if (y > 0) {
					iOptions[0] = iCurr[y-1];
					iOptions[1] = mPrev[y] + rightOpens.get(y);
					iCurr[y] = this.maxValue(iOptions);
				}
				
				else {
					iCurr[y] = 0;
				}
				
				
				double[] options = {iCurr[y], dCurr[y], mCurr[y]};
				scores[x][y] = this.maxValue(options);
			}
			
		}
		
		String[] leftAlignments = new String[left.getSequences().size()];
		String[] rightAlignments = new String[right.getSequences().size()];
		
		//Starting trace-back
		int x = rows;
		int y = cols;
		
		
				
		// Need the vectors for traceback I think
		
		
		return null;
	}
	
	
	public double maxValue(double[] arr) {
		double max = arr[0];
		
		for(int i = 1; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];			
			}
		}
		
		return max;
	}
	
	public void prependLetters(String[] arr, Profile prof, int idx) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = prof.getSequences().get(i).getSeq().charAt(idx) + arr[i];
		}
	}
	
	public void prependSpace(String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = '-' + arr[i];
		}
	}
	
}

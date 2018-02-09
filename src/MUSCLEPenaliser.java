import java.util.ArrayList;
import java.util.List;
/**
 * Class for calculating gap costs with muscle algorithm
 * @author LukeStacey
 *
 */
public class MUSCLEPenaliser implements GapPenalty {
	
	double g;
	double e;
	/**
	 * Constructor
	 * @param g gap cost
	 * @param e extension cost
	 */
	public MUSCLEPenaliser(double g, double e) {
		this.g= g;
		this.e = e;
	}

	/**
	 * Calculates the gap open costs for the given profile
	 * @param prof profile
	 * @return a list of doubles, the gap open cost at that point
	 */
	public List<Double> openCost(Profile prof) {
		List<Double> opens = new ArrayList<Double>();
		boolean[] inGap = new boolean[prof.getSequences().size()];
		
		//Check for opening gap
		for(int i = 0; i < prof.getSequences().size(); i++) {
			String seq = prof.getSequences().get(i).getSeq();
			
			if(seq.charAt(0) == '-') {
				inGap[i] = true;
			}
		}
		
		//Assign no cost for opening gap at start of sequence
		opens.add(0.0);

		for (int i = 1; i < prof.getSequences().get(0).getSeq().length(); i++) {
			double count = 0;
			for (int j = 0; j < prof.getSequences().size(); j++) {
				String seq = prof.getSequences().get(j).getSeq();
				if (seq.charAt(i) == '-' && !inGap[j]) {
					inGap[j] = true;
					count++;
				}
				
				else if (seq.charAt(i) != '-') {
					inGap[j] = false;
				}
			}
			
			double freq = count / prof.getSequences().size();
			
			opens.add(this.g/2.0 * (1-freq));
				
		}
		return opens;
	}

	/**
	 * Calculates the gap close costs for the given profile
	 * @param prof profile
	 * @return a list of doubles, the gap close cost at that point
	 */
	public List<Double> closeCost(Profile prof) {
		List<Double> closes = new ArrayList<Double>();
		boolean[] inGap = new boolean[prof.getSequences().size()];
		
		for(int i = 0; i < prof.getSequences().size(); i++) {
			String seq = prof.getSequences().get(i).getSeq();
			
			if(seq.charAt(i) == '-') {
				inGap[i] = true;
			}
		}
		
		
		for (int i = 1; i < prof.getSequences().get(0).getSeq().length(); i++) {
			double count = 0;
			for (int j = 0; j < prof.getSequences().size(); j++) {
				String seq = prof.getSequences().get(j).getSeq();
				
				if(seq.charAt(i) != '-' && inGap[j]) {
					count++;
					inGap[j] = false;
					
				}
				
				else if(seq.charAt(i) == '-' && !inGap[j]) {
					inGap[j] = true;
				}
			}
			
			double freq = count / prof.getSequences().size();
			
			closes.add(this.g/2.0 * (1-freq));
				
		}
		
		//Assign no cost for finishing sequence with a gap
		closes.add(0.0);
		return closes;
	}
	
	@Override
	/**
	 * getter for extension
	 * @return extension
	 */
	public double extenstionCost() {
		return this.e;
	}
	
	
}

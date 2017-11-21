import java.util.ArrayList;
import java.util.List;

public class MUSCLEPenaliser implements GapPenalty {
	
	double g;
	double e;
	
	public MUSCLEPenaliser(double g, double e) {
		this.g= g;
		this.e = e;
	}

	@Override
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

	@Override
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
	public double extenstionCost() {
		return this.e;
	}
	
	
}

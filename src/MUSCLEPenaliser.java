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
		opens.add(0.0);

		for (int i = 1; i < prof.getSequences().get(0).getSeq().length(); i++) {
			double count = 0;
			for (int j = 0; j < prof.getSequences().size(); j++) {
				String seq = prof.getSequences().get(j).getSeq();
				if (seq.charAt(i) == '-') {
					count++;
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
		
		for (int i = 0; i < prof.getSequences().get(0).getSeq().length() - 1; i++) {
			double count = 0;
			for (int j = 0; j < prof.getSequences().size(); j++) {
				String seq = prof.getSequences().get(j).getSeq();
				if (seq.charAt(i) == '-') {
					count++;
				}
			}
			
			double freq = count / prof.getSequences().size();
			
			closes.add(this.g/2.0 * (1-freq));
				
		}
		
		closes.add(0.0);
		return closes;
	}
	
	@Override
	public double extenstionCost() {
		return this.e;
	}
	
	
}

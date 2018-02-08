public class Sequence {
	
	String seq;
	int index;
	double rep = 0.0;
	
	public Sequence(String seq) {
		this.seq = seq;
		index = -1;
	}
	public Sequence(String seq,int index) {
		this.seq = seq;
		this.index=index;
	}
	public Sequence(String seq,int index,double rep) {
		this.seq = seq;
		this.index=index;
		this.rep=rep;
	}
	
	public String getSeq() {
		return seq;
	}
	
	public void prependSeq(char c) {
		this.seq = c + seq;
	}
	
	public String toString() {
		return this.getSeq();
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public double getRep() {
		return rep;
	}
}

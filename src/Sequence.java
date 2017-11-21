public class Sequence {
	
	String seq;
	
	public Sequence(String seq) {
		this.seq = seq;
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
}

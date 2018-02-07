public class Sequence {
	
	String seq;
	int index;
	public Sequence(String seq) {
		this.seq = seq;
		index=99;
	}
	public Sequence(String seq,int index) {
		this.seq = seq;
		this.index=index;
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
}

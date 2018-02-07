import java.util.Comparator;

public class SequenceCompare implements Comparator<Sequence> {

	@Override
	public int compare(Sequence seq1, Sequence seq2) {
		return seq1.getIndex() - seq2.getIndex();
	}

}

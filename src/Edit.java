import java.util.Iterator;
import java.util.List;

public class Edit {
	
	List<Operation> operations;
	
	public Edit(List<Operation> ops) {
		this.operations = ops;
	}
	
	public String toString() {
		String str = "";
		for (Operation op : operations) {
			str = str + op.toString() + "\n";
		}
		
		return str;
	}
	
	public void applyEdit(Caption caption) {
		Iterator<Operation> iter = this.operations.iterator();
		while(iter.hasNext()) {
			Operation op = iter.next();
			op.performOperation(caption);
		}
		
		caption.setPos(0);	
	}
}

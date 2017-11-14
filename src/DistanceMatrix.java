import java.util.Comparator;
import java.util.List;

//TODO: Rewrite so only the upper half of the matrix is stored
// This will require changes to most for loops and removeRow/cleanDM
public class DistanceMatrix<T extends Comparable<T>> {
	
	List<List<T>> matrix;
	T selfValue;
	
	public DistanceMatrix(List<List<T>> matrix) {
		this.matrix = matrix;
	}
	
	public DistanceMatrix(List<List<T>> matrix, T selfValue) {
		this(matrix);
		this.selfValue = selfValue;
		cleanDM();
	}
	
	private void cleanDM() {
		for (int i = 0; i < this.matrix.size(); i++) {
			this.matrix.get(i).set(i, this.selfValue);
		}
	}
	
	public List<List<T>> getMatrix() {
		return this.matrix;
	}
	
	
	public Value findMax() {
		Value mv = new Value(1,1, matrix.get(0).get(0));
		
		for (int i = 0; i < matrix.size(); i++) {
			for(int j = i; j < matrix.size(); j++) {
				if (mv.getValue().compareTo(matrix.get(i).get(j)) < 0) {
					mv = new Value(i, j, matrix.get(i).get(j));
				}
			}
		}
		
		return mv;
	}
	
	public void removeIndex(int i) {
		
		matrix.remove(i);
		
		for (int j = 0; j < matrix.size(); j++) {
			List<T> row = matrix.get(j);
			row.remove(i);
		}
	}
	
	public void addElement(List<T> row) {
		if (row.size() == this.matrix.size()) {
			row.add(0, this.selfValue);
		}
		
		for(int i = 0; i < this.matrix.size(); i++) {
			List<T> current = matrix.get(i);
			current.add(0, row.get(i+1));
		}
		
		this.matrix.add(row);
	}
	
	class Value implements Comparator<Value> {
		int row;
		int col;
		T value;
		
		public Value(int row, int col, T value) {
			this.row = row;
			this.col = col;
			this.value = value;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
		
		public T getValue() {
			return value;
		}
		
		@Override
		public int compare(Value arg0, Value arg1) {
			
			int compVal = arg0.getValue().compareTo(arg1.getValue());
			
			if (compVal < 0) {
				return -1;
			}
			
			else if (compVal == 0) {
				return 0;
			}
			
			else {
				return -1;
			}
		}
		
		public boolean equals(Object o) {
			
			if (this == o) {
				return true;
			}
			
			if (!(o instanceof DistanceMatrix.Value)) {
				return false;
			}
			
			@SuppressWarnings("unchecked")
			DistanceMatrix<T>.Value mv = (DistanceMatrix<T>.Value) o;
			
			return mv.compare(mv, this) == 0;
		}
	}
}

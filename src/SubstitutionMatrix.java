import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
// May be better practice to interface this?
public class SubstitutionMatrix {
	
	Map<Character, Integer> indexMap = new HashMap<Character, Integer>();
	double[][] subMatrix;
	double delete;
	
	public SubstitutionMatrix(String str, double[][] subMatrix, double del) {
		this.populateIndexMap(str);
		this.subMatrix = subMatrix;
		this.delete = del;
	}
	
	//TODO: Change encoding to UTF-8
	// Method for reading substitution matrix from two configuration files, one containing the alphabet
	// and the other containing the substitution matrix itself 
	public SubstitutionMatrix(String alphabetFile, String matrixFile) throws IOException {
		Path apath = Paths.get(alphabetFile);
		
		//Change to UTF
		Charset charset = Charset.forName("CP1252");
		
		// Maybe change this to something more obvious / maintainable for large alphabets
		List<String> strings = Files.readAllLines(apath, charset);
		this.populateIndexMap(strings.get(0));
		
		this.subMatrix = new double[strings.get(0).length()][strings.get(0).length()];
		
		Path mpath = Paths.get(matrixFile);
		
		CSVReader reader = new CSVReader(new FileReader(mpath.toFile()));
		List<String[]> lines = reader.readAll();
		Iterator<String[]> iter = lines.iterator();
		
		int i = 0;
		
		// May throw an index out of bounds error if matrix dimensions do not match up with alphabet size
		//TODO: Error check index out of bounds on sub matrix read
		while(iter.hasNext()) {
			String[] line = iter.next();
			
			for(int j = 0; j < strings.get(0).length(); j++) {
				this.subMatrix[i][j] = Double.parseDouble(line[j]);
			}
			
			i++;
		}
		
		reader.close();
	}
	
	private void populateIndexMap(String str) {
		
		for (int i=0; i < str.length(); i++) {
			char curr = str.charAt(i);
			indexMap.put(curr, i);
		}
	}
	
	public double getSubScore(char i, char j) {
		return this.subMatrix[charToIndex(i)][charToIndex(j)];
	}
	
	public double getSubScore(int i, int j) {
		return this.subMatrix[i][j];
	}
	
	public double deletionCost() {
		return this.delete;
	}
	
	public int charToIndex(char c) {
		return indexMap.get(c);
	}
	
	public int alphabetSize() {
		return indexMap.size();
	}
	
}

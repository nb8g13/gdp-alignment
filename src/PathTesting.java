import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;

public class PathTesting {
	public static void main(String[] args) {
		/*System.out.println(Paths.get("tmp\\config.txt"));
		
		Path path = Paths.get("tmp\\config.txt");
		Path root = Paths.get(".").normalize();
		
		System.out.println(root.toString());
		
		Charset charset = Charset.forName("CP1252");
		
		try {
			List<String> strings = Files.readAllLines(path, charset);
			System.out.println(strings.get(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*FileSystem fs = FileSystems.getDefault();
		Iterator<Path> iter = fs.getRootDirectories().iterator();
		
		System.out.println("Printing root directories");
		while(iter.hasNext()) {
			Path next = iter.next();
			System.out.println(next);
		}*/
		
		Path mpath = Paths.get("tmp\\matrix.csv");
		
		System.out.println(mpath);
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(mpath.toFile()));
			List<String[]> lines = reader.readAll();
			Iterator<String[]> iter = lines.iterator();
			while(iter.hasNext()) {
				String[] line = iter.next();
				for (int i = 0; i < line.length; i++) {
					System.out.print(line[i] + " ");
				}
				System.out.println();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Pro on 09/02/2018.
 */
public class FileReader {

    public FileReader () {

    }

    //Reads the text file and stores as a single line string

    public String /*StringBuilder*/ readFile(String filepath)  {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // erasing all eggcorn data and filt_corns data
            if (!filepath.contains("egg_corns.txt")) {
                while (line != null) {
                    sb.append(line);
                    if (!(line.equals(""))) {
                        sb.insert(sb.length(), " ");
                    }
                    try {
                        line = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    try {
                        line = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String everything = sb.toString();
            return everything;

        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return sb;
    }

}

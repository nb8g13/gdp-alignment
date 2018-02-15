import java.util.HashMap;
import java.util.Random;

/**
 * Created by Pro on 08/02/2018.
 */
public class Eggcorn {

    private String correctCorn;
    private String wrongCorn;
    private String correctMispelledCorn;
    private String wrongMispelledCorn;
    private HashMap<Character, Character> errorMap;
    private String checkString;

    public Eggcorn(String correctCorn, String wrongCorn) {
        this.correctCorn = correctCorn;
        this.wrongCorn = wrongCorn;
        generateErrorMap();
        correctMispelledCorn = generateErrorCorn(correctCorn);
        wrongMispelledCorn = generateErrorCorn(wrongCorn);
        buildCheckString();
    }

    public String getCheckString() {
        return checkString;
    }

    public void buildCheckString() {
            checkString = "";
            checkString += correctCorn + " ";
            checkString += correctMispelledCorn + " ";
            checkString += wrongCorn + " ";
            checkString += wrongMispelledCorn;
    }

    public void generateErrorMap() {
        HashMap<Character, Character> errMap = new HashMap<>();
      //  errMap.put('f' , 'd');
      //  errMap.put('v' , 'c');
      //  errMap.put('h' , 'g');
      //  errMap.put('z' , 'x');
      //  errMap.put('w' , 'q');
      //  errMap.put('y' , 't');
      //  errMap.put('b' , 'n');
      //  errMap.put('a' , 's');
       // errMap.put('e' , 'r');
      //  errMap.put('i' , 'k');
      //  errMap.put('o' , 'p');
      //  errMap.put('u' , 'j');
       // errMap.put('m' , 'n');
      //  errMap.put('n' , 'm');
        errMap.put('a' , 'u');
     //   errMap.put('e' , 'i');
     //   errMap.put('u' , 'i');
     //   errMap.put('i' , 'e');
        errMap.put('o' , 'u');
     //   errMap.put('k' , 'c');

        this.errorMap = errMap;
    }

    public void getMisstypings() {
  //      FileDealer d = new FileDealer();
  //      d.eggToData()d.readFile("mistypings.txt");
    }

    public String getCorrectCorn() {
        return correctCorn;
    }

    public String getWrongCorn() {
        return wrongCorn;
    }

    public String getCorrectMispelledCorn() {
        return correctMispelledCorn;
    }

    public String getWrongMispelledCorn() {
        return wrongMispelledCorn;
    }

    public String generateErrorCorn(String word) {
        Integer randInd = generateRandomInRange(0, word.length() - 1);
        char[] wa = word.toCharArray();
        for (Integer i = 0; i < wa.length; i++) {
            if (errorMap.containsKey(wa[i])) {
              //  if (Math.random() < 0.7) {
                    wa[i] = errorMap.get(wa[i]);
              //  }
            }
        }
        return new String(wa);
    }



    public int generateRandomInRange(int min, int max) {
        Random r = new Random();
        int ra = r.nextInt((max - min) + 1) + min;
        return ra;
    }

}

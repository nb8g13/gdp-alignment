import java.util.ArrayList;

/**
 * Created by Pro on 09/02/2018.
 */
public class ReadFilesHandler {

    public ReadFilesHandler() {

    }

    public ArrayList<Caption> transcriptToList(String str) {
        ArrayList<Caption> trData = new ArrayList<>();
        int startIndex = 0;
      // int random;
     //   int count = 0;
     //   Integer wordsPerLine = 8;
        String transcript = new String(str);
        for (int i = 0; i < transcript.length(); i++){
            if (transcript.charAt(i) == '.' || transcript.charAt(i) == ',') {
                trData.add(new Caption(transcript.substring(startIndex, i + 1), 1));
                startIndex = i + 2;
         //       count = - 1;
            }
         //   if (transcript.charAt(i) == ' ') {
         //       count++;
         //       if (count == wordsPerLine) {
          //          trData.add(new Caption(transcript.substring(startIndex, i), 1));
         //           startIndex = i + 1;
         //           count = 0;
        //        }
         //   }
        }
        return trData;
    }

    public ArrayList<Eggcorn> eggsToList(String sb) {
        String[] ting;
        int startIndex = 0;
        ArrayList egData = new ArrayList();
        String eggcorns = new String(sb);
        int count = 0;
        for (int i = 0; i < eggcorns.length(); i++) {
            if(eggcorns.charAt(i) == ('\r')) {
                ting = eggcorns.substring(startIndex, i).split(" ! ");
                egData.add(new Eggcorn(ting[0], ting[1]));
                startIndex = i + 2;
                count ++;
            }
        }
        return egData;
    }

    public ArrayList<Eggcorn> scanCorns(String trueTransString, String eggsString) {
        Integer startIndex = 0;
        String[] ting;
        String[] transWords = trueTransString.split(" ");
        ArrayList<Eggcorn> tSpecEggs = new ArrayList<>();
        for (String word: transWords) {
            if(word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == ',') {
                word = word.substring(0, word.length() - 1);
            }
            for (int i = 0; i < eggsString.length(); i++) {
                if(eggsString.charAt(i) == ('\r')) {
                    ting = eggsString.substring(startIndex, i).split(" ! ");
                    if (word.equals(ting[0])) {
                        tSpecEggs.add(new Eggcorn(ting[0], ting[1]));
                    }
                    startIndex = i + 2;
                }
            }
            startIndex = 0;
        }
        return tSpecEggs;
    }

    public String dataStructToString(ArrayList<Caption> trData) {
        String output = "";
        Integer count = 0;
        for (Caption caption: trData) {
            count++;
            if ( count <= trData.size()  ) {
                if ( count != trData.size() - 1 ) {
                    output = output + caption.getText().toString() + " ";
                }
                else {
                    output = output + caption.getText().toString();
                }
            }
        }
        return output;
    }

    public void remPuncFromCaptionList(ArrayList<Caption> trData) {
        String caption;
        char[] ch;
        for (Caption cap: trData) {
            cap.setText(new String(getNakedString(cap.getText())));
        }
    }

    public String remPuncFromString(String string) {
        return getNakedString(string);
    }
/*
    public String getNakedString(String toMutate) {
        char[] ch;
        StringBuilder b = new StringBuilder(toMutate);
        ch = toMutate.toCharArray();
        for (Integer i = 0; i < toMutate.length(); i++) {
            if (ch[i] == ('.') || ch[i] == (',') || ch[i] == ('\'')) {
                ch[i] = ' ';
            }
            else if (ch[i] > 64 && ch[i] < 91) {
                ch[i] += 32;
            }
        }
        return new String(ch);
    }
*/
    public String getNakedString(String toMutate) {
        StringBuilder b = new StringBuilder(toMutate);
        Character ch;
        for (Integer i = 0; i < b.length(); i++) {
            ch = b.charAt(i);
            if (ch == ('.') || ch == (',') || ch == ('\'') || ch == ('-')) {
                b.deleteCharAt(i);
            }
            else if (Character.isUpperCase(ch)) {
                b.setCharAt(i, Character.toLowerCase(ch));
            }
        }
        return new String(b);
    }

    public String setToLowerCase(String toMutate) {
        String lower = toMutate.toLowerCase();
        //Replace all sequences of whitespace with a single space
        lower = lower.replaceAll("\\s+", " ");
        lower = lower.replaceAll("[^a-z0-9\\s]", "");
        return lower;
    }
}

import java.util.ArrayList;

/**
 * Created by Pro on 02/02/2018.
 */
public class Printer {

    public Printer() {

    }

    public void printTrans(ArrayList<Caption> trData) {
        for (Caption sentence: trData) {
            System.out.println(sentence.getText());
        }
    }

    public void printMutations(ArrayList<ArrayList<Mutation>> allMutations) {
        int count = 0;
        int othercount = 0;
        for (ArrayList<Mutation> captionMuts: allMutations) {
            count ++;
            System.out.println("CAPTION " + count + " MUTATIONS");
            for (Mutation mut: captionMuts) {
                othercount++;
                System.out.print("MUTATION " + othercount + "    ");
                String s = " ";
                for (Operation op: mut.getO().operations) {
                    s = s + op.toString() + " ";
                }
                System.out.println(s + " " + mut.getA() + " " + mut.getT());
            }
            othercount = 0;
        }
    }
}

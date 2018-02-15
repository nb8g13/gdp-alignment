import java.util.ArrayList;

/**
 * Created by Pro on 04/02/2018.
 */
//Reserves parts of the caption as well as splitting delete and insert operations for the bot into small parts
    // These small parts for deletes are the original word length split into different parts (and stored in an array) so that the whole delete can take place at different points throughout the simulation
    // These small parts for inserts are the original word split into different strings (and stored in an array) so that the whole insert can take place at different points throughout the simulation

    // Reservation allows operations for bots to occur at different points in the simulation in order to replicate the type of edits that can be produced through collaberative editing.

public class Reservation {

    String OriginalWord;
    String newWord;
    ArrayList<Integer> deleteNumberList;
    ArrayList<String> stringInsertList;
    private Integer reservationIndex;
    private Integer cursorIndex;
    String reservationHolder;
    Bot bot;

    public Reservation(Integer index, String bot, String O, String N, Bot inst) {
        this.reservationIndex = index;
        this.cursorIndex = index;
        this.reservationHolder = bot;
        this.OriginalWord = O;
        this.newWord = N;
        this.bot = inst;
        this.deleteNumberList = createDeleteNums(O);
        this.stringInsertList = createInsertStrings(N);

    }

    public ArrayList<Integer> createDeleteNums(String deleteWord) {
        ArrayList<Integer> deleteNums = new ArrayList<>();
        if (Math.random() < 0.7 || deleteWord.length() == 1) {
            deleteNums.add(deleteWord.length());
        }
        else {
            if (deleteWord.length() % 2 == 0) {
                for (Integer i = 0 ; i < 2; i++) {
                    deleteNums.add(deleteWord.length() / 2);
                }
            }
            else {
                for (Integer i = 0 ; i < 2; i++) {
                    deleteNums.add((deleteWord.length() - 1) / 2);
                }
                deleteNums.add(deleteWord.length() % 2);
            }
        }
        return deleteNums;
    }

    // the issue with these are that we would need to make sure that all users finish their last edit
    // It shouldnt matter too much given that the candidate selection works the way we need to

    public ArrayList<String> createInsertStrings(String newWord) {

        ArrayList<String> insertStrings = new ArrayList<>();
        if (Math.random() < 0.7 || newWord.length() == 1) {
            insertStrings.add(newWord);
        }
        else {
            Integer index = 0;
            if (newWord.length() % 2 == 0) {
                Integer end = newWord.length() / 2;
                for (Integer i = 0 ; i < 2; i++) {
                    insertStrings.add(newWord.substring(index, end));
                    index = newWord.length() / 2;
                    end = newWord.length();
                }
            }
            else {
                Integer end = (newWord.length() - 1) / 2;
                for (Integer i = 0 ; i < 2; i++) {
                    insertStrings.add(newWord.substring(index, end));
                    index = end;
                    end = (newWord.length() - 1);

                }
                insertStrings.add(newWord.substring(newWord.length() - 1));
            }
        }
        return insertStrings;
    }

    public Boolean updateDeleteNumberList() {
        this.deleteNumberList.remove(0);
        if (deleteNumberList.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateStringInsertList() {
        this.stringInsertList.remove(0);
        if (stringInsertList.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    public void updateReservationIndex(int length) {
        this.reservationIndex = reservationIndex + length;
    }

    public void updateCursorIndex(int length) {
        this.cursorIndex = cursorIndex + length;
    }

    public ArrayList<Integer> getDeleteNumberList() {
        return deleteNumberList;
    }

    public ArrayList<String> getStringInsertList() {
        return stringInsertList;
    }

    public Integer getReservationIndex() {
        return reservationIndex;
    }

    public Integer getCursorIndex() {
        return cursorIndex;
    }

    public String getNewWord() {
        return newWord;
    }

    public String getOriginalWord() {
        return OriginalWord;
    }

    public String getReservationHolder() {
        return reservationHolder;
    }

    public Bot getBot() {
        return bot;
    }
}

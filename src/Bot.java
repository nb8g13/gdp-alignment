import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Pro on 30/01/2018.
 */
public class Bot {

    private double cornCompetance;
    private double spellCompetance;
    double wrongFlip;
    String name;
    ArrayList<Mutation> mutations;
    CursorOperation cursorController;
    DeleteOperation deleter;
    InsertOperation inserter;

    Boolean inReservation;
    String state;
    Boolean deleteState;
    Boolean insertState;
    Boolean mtFlipState;

    double probProduced;
    int updaterValue;
    //ReservationUpdate updateArgs;
    ArrayList<ReservationUpdate> updateArgs;

    Integer noOfScans;
    Integer noOfScanMinB;
    Integer noOfScanMaxB;
    double reservationProbability;
    int concessionFactor;
    Boolean isActive;

    Eggcorn currentCorn;
    Random r;



    public Bot(String name) {
        mutations = new ArrayList<>();
        this.name = name;
        inReservation = false;
        deleteState = false;
        insertState = false;
        r = new Random();
     //   cornCompetance = setCompetance(4);
     //   spellCompetance = setCompetance(0.5);
    }

    public Bot(String name, Double cornVariance, Double spellVariance, int cornMean, int spellMean) {
        mutations = new ArrayList<>();
        updateArgs = new ArrayList<>();
        this.name = name;
        inReservation = false;
        deleteState = false;
        insertState = false;
        r = new Random();
        isActive = true;
      //  noOfScanMinB = 2;
      //  noOfScanMaxB = 4;
      //  noOfScans = generateRandomInRange(noOfScanMinB, noOfScanMaxB);
        noOfScans = 0;
        setReservationProbability(1);
        cornCompetance = setCompetance(cornVariance, cornMean);
        spellCompetance = setCompetance(spellVariance, spellMean);
        concessionFactor = generateRandomInRange(1, 5);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void resetScans(Caption caption, ArrayList<Eggcorn> eggData) {
        Integer count = numOfCorns(eggData, caption);
        this.noOfScans = count*3/*generateRandomInRange(count * 2, count * 3)*/;
    }

    public Integer getNoOfScans() {
        return noOfScans;
    }

    public void incrementNoOfScans() {
        this.noOfScans = noOfScans + 2;
    }

    public void decrementNoOfScans() {
        if (noOfScans != 0 ) {
            this.noOfScans--;
        }
    }

    public double setCompetance(double variance, int mean) {
        double ra = r.nextGaussian();
        ra = ra*variance+mean;

        if (mean == 1) {
            if (ra > 1) {
                ra = 1 - (ra - 1);
            }
            if (ra < 0 && ra > -1) {
                ra = 1 + ra;
            }
            if (ra < 0) {
                ra = 0.01;
            }
        }
        else {
            if (ra < 0) {
                ra = ra * -1;
            }
            if (ra > 1 && ra < 2) {
                ra = ra - 1;
            }
            if (ra > 1) {
                ra = 0.99;
            }
        }



        return ra;
    }

    public void setReservationProbability(double reservationProbability) {
        this.reservationProbability = reservationProbability;
    }

    public double getReservationProbability() {
        return reservationProbability;
    }

    public Integer numOfCorns(ArrayList<Eggcorn> eggData, Caption caption) {
        String[] words = caption.getText().split(" ");
        Integer count = 0;
        for (String oWord: words) {
            if (oWord.length() > 0 && oWord.charAt(oWord.length() - 1) == ',') {
                StringBuilder sbWord = new StringBuilder(oWord);
                oWord = sbWord.substring(0, (oWord.length() - 1));
            }
            if (getEgg(eggData ,oWord) != null) {
                count++;
            }
        }
        return count;
    }

    public void reduceReservationProb(ArrayList<Eggcorn> eggData, Caption caption) {

        Integer numCorn = numOfCorns(eggData, caption);
      //  double reduceFactor = /* 1.0/(numCorn*10.0) */ numCorn * 0.008 * concessionFactor;
        double reduceFactor = /* 1.0/(numCorn*concessionFactor)*/  numCorn * 0.008 * concessionFactor;

        this.reservationProbability = reservationProbability - reduceFactor;
        if (reservationProbability < 0 || numCorn == 0) {
            reservationProbability = 0;
        }
    }

    public void increaseReservationProb(ArrayList<Eggcorn> eggData, Caption caption) {

        Integer numCorn = numOfCorns(eggData, caption);
        double increaseFactor =  1.0/(numCorn*4.0);

       this.reservationProbability = reservationProbability + increaseFactor;

       // this.reservationProbability = reservationProbability + (reservationProbability/4);
        if (reservationProbability > 1) {
            reservationProbability = 1;
        }
    }

    public Boolean getInReservation() {
        return inReservation;
    }


    public double getCompetance() {
        return cornCompetance;
    }

    public String getName() {
        return name;
    }

    public void moveAndDelete(int move, int del, Caption caption) {
        ArrayList<Operation> ops = new ArrayList<>();
        cursorController = new CursorOperation(move);
        deleter = new DeleteOperation(del);
        ops.add(cursorController);
        ops.add(deleter);
        Edit e = new Edit(ops);
        caption.setText(e.applyEdit(caption, 1).getText());
    }

    public void moveAndInsert(int move, String replacer, Caption caption) {
        ArrayList<Operation> ops = new ArrayList<>();
        cursorController = new CursorOperation(move);
        inserter = new InsertOperation(replacer);
        ops.add(cursorController);
        ops.add(inserter);
        Edit e = new Edit(ops);
        caption.setText(e.applyEdit(caption, 1).getText());
    }
/*
    public void moveCursor(int move, Caption caption) {
        cursorController = new CursorOperation(move);
        cursorController.performOperation(caption);
    }

    public void deleteSection(int del, Caption caption) {
        deleter = new DeleteOperation(del);
        deleter.performOperation(caption);
    }

    public void insertSection(String replacer, Caption caption) {
        inserter = new InsertOperation(replacer);
        inserter.performOperation(caption);
    }
*/
    public int calculateStartIndex(String[] words, int indexOfCorn, Caption oldCaption) {
      //  System.out.println("oldCaption: " + oldCaption.getText() + "Index of corn: " + indexOfCorn + " word: " + words[indexOfCorn]);
        int count = 0;
        for (Integer i = 0; i < indexOfCorn; i++) {
            if (words[i].equals(words[indexOfCorn])) {
                count++;
            }
        }
        count++;
        String str = oldCaption.getText();
        String substr;
        int corrector = 0;
        if (!(indexOfCorn == 0)) {
            substr = " " + words[indexOfCorn];
            corrector = 1;
        }
        else {
            substr = words[indexOfCorn];
        }
        int pos = oldCaption.getText().indexOf(substr);
        while (--count > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);

        pos = pos + corrector;
        return pos;
    }

    public int calculateCursorMove(Caption caption, int startIndex) {
        int move = 0;
        move = startIndex - caption.getPos();
        return move;
    }
/*
    public void flip(Caption caption, int startIndex, int deleteLength, String newCornReplacer) {
        int move = calculateCursorMove(caption, startIndex);
        cursorController = new CursorOperation(move);
        cursorController.performOperation(caption);

        deleter = new DeleteOperation(deleteLength);
        deleter.performOperation(caption);

        inserter = new InsertOperation(newCornReplacer);
        inserter.performOperation(caption);

        mutations.add(generateMutation(System.nanoTime()));
        resetOperations();
    }
*/
    public void resetOperations() {
        cursorController = null;
        deleter = null;
        inserter = null;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setInReservation(Boolean inReservation) {
        this.inReservation = inReservation;
    }



    public int getUpdaterValue() {
        return updaterValue;
    }
/*
    public ReservationUpdate getUpdate() {
        return this.updateArgs;
    }

    public void setUpdateArgs(ReservationUpdate updateArgs) {
        this.updateArgs = updateArgs;
    }
*/
    public ArrayList<ReservationUpdate> getUpdate() {
        return this.updateArgs;
    }

    public void clearUpdateArgs() {
        this.updateArgs.clear();
    }


    public ArrayList<Mutation>/*Mutation*/ editCaptionFour(Caption caption, ArrayList<Eggcorn> eggData, HashMap<String, Reservation> reservations) {

        String[] words = caption.getText().split(" ");
        //Mutation mut = null;
        ArrayList<Mutation> mut = new ArrayList<>();

        if (isActive) {
            if (inReservation) {
                if (state.equals("delete")) {
                   // mut = performReservationDelete(reservations, caption);
                    mut.add(performReservationDelete(reservations, caption));
                } if (state.equals("insert")) {
                   // mut = performReservationMove(reservations, caption);
                    mut.add(performReservationMove(reservations, caption));
                }
            } else {
                for (Integer i = 0; i < words.length; i++) {
                    decideMoveMakeRes(eggData, words[i], calculateStartIndex(words, i, caption), reservations);
                   // if (!inReservation) {
                    /*
                        decrementNoOfScans();
                        if (noOfScans == 0) {
                            isActive = false;
                        }
                        */
                 //   }
                 //   else {
                    reduceReservationProb(eggData, caption);
                    if(inReservation) {
                        //reduceReservationProb(eggData, caption);
                        setState("delete");
                    }
                 //   }
                }
            }
        }
        return mut;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Mutation performReservationDelete(HashMap<String, Reservation> reservations, Caption caption) {
        Mutation mut = null;
        Reservation ourRes = reservations.get(name);
        ourRes.getOriginalWord();
        if (!ourRes.getDeleteNumberList().isEmpty()) {

         //   moveCursor(calculateCursorMove(caption, ourRes.getCursorIndex()), caption);
         //   deleteSection(ourRes.getDeleteNumberList().get(0), caption);
       //     System.out.println("Caption: " + caption.getText() + " Caption length: " + caption.getText().length()+ " Cursor move " + calculateCursorMove(caption, ourRes.getCursorIndex()));
            moveAndDelete(calculateCursorMove(caption, ourRes.getCursorIndex()), ourRes.getDeleteNumberList().get(0), caption);


           // mutations.add(generateMutation(System.nanoTime()));
            mut = generateMutation(System.nanoTime());
            resetOperations();
          //  updateArgs = new ReservationUpdate(ourRes.getCursorIndex(), -ourRes.getDeleteNumberList().get(0), state);
            updateArgs.add(new ReservationUpdate(ourRes.getCursorIndex(), -ourRes.getDeleteNumberList().get(0), state));
            if(!ourRes.updateDeleteNumberList()) {
                setState("insert");
            }
        }
        return mut;
    }

    public Mutation performReservationMove(HashMap<String, Reservation> reservations, Caption caption) {
        Mutation mut = null;
        Reservation ourRes = reservations.get(name);
        String NewWord = ourRes.getNewWord();
        if (!ourRes.getStringInsertList().isEmpty()) {

           // moveCursor(calculateCursorMove(caption, ourRes.getCursorIndex()), caption);
           // insertSection(ourRes.getStringInsertList().get(0), caption);
            String tobeinserted = ourRes.getStringInsertList().get(0);
       //     System.out.println("Caption: " + caption.getText() + " Caption length: " + caption.getText().length() + " Cursor move " + calculateCursorMove(caption, ourRes.getCursorIndex()) + " Required cursor index: " + ourRes.getCursorIndex() + " to be inserted: " + tobeinserted );
            moveAndInsert(calculateCursorMove(caption, ourRes.getCursorIndex()), tobeinserted, caption);

          //  mutations.add(generateMutation(System.nanoTime()));
            mut = generateMutation(System.nanoTime());
            resetOperations();



        //    updateArgs = new ReservationUpdate(ourRes.getCursorIndex(), ourRes.getStringInsertList().get(0).length(),state);
            updateArgs.add(new ReservationUpdate(ourRes.getCursorIndex(), ourRes.getStringInsertList().get(0).length(),state));
            if (!ourRes.updateStringInsertList()) {
                setState(null);
                setInReservation(false);
                reservations.remove(name);
               // updateArgs.setComplete(true);
                updateArgs.get(updateArgs.size() - 1).setComplete(true);
            }
        }
        return mut;
    }

    public Boolean decideMoveMakeRes(ArrayList<Eggcorn> eggData, String oWord, Integer startIndex, HashMap<String, Reservation> reservations) {
/*
        if (oWord.equals("approach")) {
            System.out.println("approach found");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/

        if (oWord.length() > 0 && oWord.charAt(oWord.length() - 1) == ',') {
            StringBuilder sbWord = new StringBuilder(oWord);
            oWord = sbWord.substring(0, (oWord.length() - 1));
        }

        Eggcorn corn = getEgg(eggData, oWord);
        Integer code = getEggInd(corn, oWord);
        double cornRandom = generateRandom();
        double spellRandom = generateRandom();
        Boolean bool = false;



        //Switch case for different corn types
        //case 1 - Its a correct corn .... we can either mispell it, flip it to wrong corn or flip it to mispelled wrong corn
        //case 2 - Mispelled correct corn .. we can either spell it correct, flip it to wrong corn or flip it to mispelled wrong corn
        //case 3 - Its a wrong corn .. we can either mispell it, flip it to correct corn or flip it to mispelled correct corn
        //case 4 - Mispelled wrong corn .. we can either spell it correct, flip it to correct corn or flip it to mispelled correct corn

        switch (code) {
            case 1:  //word is a 100% correct corn
                //if probality of making a correct flip occur occurs
                if (cornRandom < cornCompetance) {
                    if (spellRandom > spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectMispelledCorn());
                    }
                } else {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongCorn());
                    } else {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongMispelledCorn());
                    }
                }
                break;
            case 2: //word is correct eggcorn but mispelled
                if (cornRandom < cornCompetance) {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectCorn());
                    }
                } else {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongCorn());
                    } else {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongMispelledCorn());
                    }
                }
                break;
            case 3: //word is wrong eggcorn
                if (cornRandom > cornCompetance) {
                    if (spellRandom > spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongMispelledCorn());
                    }
                } else {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectCorn());
                    } else {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectMispelledCorn());
                    }
                }
                break;
            case 4: //word is wrong eggcorn and mispelled
                if (cornRandom > cornCompetance) {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getWrongCorn());
                    }
                } else {
                    if (spellRandom < spellCompetance) {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectCorn());
                    } else {
                        bool = createReservation(reservations, startIndex, oWord, corn.getCorrectMispelledCorn());
                    }
                }
                break;
        }
        return bool;
    }


    public Boolean createReservation(HashMap<String, Reservation> reservations, int startIndex, String oWord, String nWord) {
        if(!isReserved(reservations, startIndex) && (Math.random() < reservationProbability)) {
            reservations.put(name, new Reservation(startIndex, name, oWord, nWord, this));
            inReservation = true;
            //reduceReservationProb();
            //setState("delete");
            return true;
        }
        return false;
    }

    public Eggcorn getEgg(ArrayList<Eggcorn> eggData, String word) {

        for (Eggcorn corn: eggData) {
            String[] sp = corn.getCheckString().split(" ");
            for (Integer i = 0; i < sp.length; i++) {
                if (word.equals(sp[i])) {
                    return corn;
                }
            }
        }
        return null;
    }

    public int getEggInd(Eggcorn corn, String word) {
        if (corn != null) {
            String[] sp = corn.getCheckString().split(" ");
            for (Integer i = 0; i < 4; i++) {
                if (word.equals(sp[i])) {
                    return i + 1;
                }
            }
        }
        return 5;
    }

    public double generateRandom() {
        return Math.random();
    }

    public int generateRandomInRange(int min, int max) {
        Random r = new Random();
      //  System.out.print("  min: " + min + " max: " + max );
        int ra = r.nextInt((max - min) + 1) + min;
      //  System.out.print("  completed with: " + ra);
        return ra;
    }

    public Boolean isReserved(HashMap<String, Reservation> reservations, int startIndex) {
        for (String key: reservations.keySet()) {
            if (reservations.get(key).getReservationIndex() == startIndex) {
                return true;
            }
        }
        return false;
    }


    public Mutation generateMutation(long time) {
        List<Operation> ops = new ArrayList<>();
        if (cursorController != null ) {
            ops.add(cursorController);
        }
        if (deleter != null) {
            ops.add(deleter);
        }
        if (inserter != null) {
            ops.add(inserter);
        }
        Edit edit = new Edit(ops);
        Mutation mutation = new Mutation(edit, name , time);
        return mutation;
    }
}

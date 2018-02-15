import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Pro on 01/02/2018.
 */
public class Simulator {

    //ArrayList<StringBuilder> trData;
    ArrayList<Caption> trData;
    ArrayList<Eggcorn> eggData;

    HashMap<String, String> correctCorns;
    HashMap<String, String> wrongCorns;

    double variance;
    ArrayList<Bot> bots;
    Printer printer;
    double captionEditDuration;

    int noOfBots;
    ArrayList<Mutation> captionMutations;
    ArrayList<ArrayList<Mutation>> allMutations;
    HashMap<String, Reservation> reservations;

    public Simulator() {

    }

    public void reinitialiseSimulator(String trueTransName, String transcriptFileName, String eggFileName) {
        FileReader reader = new FileReader();
        ReadFilesHandler rfh = new ReadFilesHandler();

        String trueTransString = reader.readFile(trueTransName);
        String transString = reader.readFile(transcriptFileName);
        String eggsString = reader.readFile(eggFileName);

        ArrayList<Caption> transList = rfh.transcriptToList(transString);
       // ArrayList<Eggcorn> eggsList = rfh.scanCorns(transString, eggsString);
        ArrayList<Eggcorn> eggsList = rfh.scanCorns(trueTransString, eggsString);

        trData = transList;
        eggData = eggsList;

        printer = new Printer();
        allMutations = new ArrayList<>();
        captionMutations = new ArrayList<>();
        reservations = new HashMap<>();
        setNumberOfBots(15);
   //     botInitialiserTwo(cornVariance, spellVariance);
        System.out.println("done");
    }

    public ArrayList<ArrayList<Mutation>> getAllMutations() {
        return allMutations;
    }

    public ArrayList<Caption> getTrData() {
        return trData;
    }

    public void setTrData(ArrayList<Caption> trData) {
        this.trData = trData;
    }

    public void setNumberOfBots(int noOfBots) {
        this.noOfBots = noOfBots;
    }

    public void botInitialiser() {
        bots = new ArrayList<>();
        for (Integer i = 0; i < noOfBots; i++) {
            bots.add(new Bot("Bot " + i));
            System.out.println("name: " + bots.get(i).getName() + " Competance: " + bots.get(i).getCompetance());
        }
    }

    public void botInitialiserTwo(double cornVariance, double spellVariance, int cornMean, int spellMean) {
        this.variance = cornVariance;
        bots = new ArrayList<>();
        for (Integer i = 0; i < noOfBots; i++) {
            bots.add(new Bot("Bot " + i, cornVariance, spellVariance, cornMean, spellMean));
      //      System.out.println("name: " + bots.get(i).getName() + " Competance: " + bots.get(i).getCompetance());
        }
    }

    public Mutation generateFirstMutation(String captionInsert) {
    //    CursorOperation c = new CursorOperation();
        InsertOperation i = new InsertOperation(captionInsert);
        ArrayList<Operation> ops = new ArrayList<>();
        ops.add(i);
        Edit e = new Edit(ops);
        return new Mutation(e, "admin", System.nanoTime());
    }

    public ArrayList<ArrayList<Mutation>> Simulate() {
        Integer timeStep = 0;

        for (Integer i = 0; i < trData.size(); i++) {
            Caption caption = trData.get(i);
            allMutations.add(new ArrayList<>());

            allMutations.get(i).add(generateFirstMutation(caption.getText()));

            reinitialiseBotStates(caption);

            ArrayList<Bot> botsForCaption = new ArrayList<>();
            Integer botsPerCaption = randomInRange(2, 3);

            for (Integer h = 0; h < botsPerCaption; h++) {
                botsForCaption.add(bots.get(randomInRange(0, bots.size() - 1)));
            }

            while (checkReservationProbs(botsForCaption)) {
         //   while (isBotsFullyScanned(botsForCaption)) {
         //   for (Integer o = 0; o < noOfSteps; o++ ) {
                for (Bot bot : botsForCaption) {
                  //  Mutation mut = bot.editCaptionFour(caption, eggData, reservations);
            //        while (bot.getReservationProbability() > 0) {
                        ArrayList<Mutation> mut = bot.editCaptionFour(caption, eggData, reservations);
                        //  if (mut != null) {
                        if (!mut.isEmpty()) {
                            allMutations.get(i).addAll(mut);
                            updateReservationsIndexes(bot.getUpdate());
                            //if (bot.getUpdate().getComplete()) {
                            if (bot.getUpdate().get(bot.getUpdate().size() - 1).getComplete()) {
                                //  incrementBotChecks(botsForCaption, bot);
                                spikeReservationProbs(botsForCaption, bot, caption);
                            }
                            // bot.setUpdateArgs(null);
                            bot.clearUpdateArgs();
                        }
                //    }
                }
            }
          //  allMutations.add(new ArrayList<>(captionMutations));
            flushBotOperations(caption, botsForCaption);
            reservations.clear();
            reinitialiseBotStates(caption);
          //  captionMutations.clear();
            //printer.printMutations(allMutations);
            //f.printCorrectCorns();
            //f.printWrongCorns();
        }
        printer.printMutations(allMutations);
        printer.printTrans(trData);
        return allMutations;
        //allMutations.clear();
    }

    public ArrayList<Bot> getBots() {
        return bots;
    }

    public Boolean checkReservationProbs(ArrayList<Bot> botsPerCaption){
        double i = 0;
        for (Bot b: botsPerCaption) {
            i += b.getReservationProbability();
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    public Boolean isBotsFullyScanned(ArrayList<Bot> botsPerCaption) {
        Integer i = 0;
        for (Bot b: botsPerCaption) {
            i += b.getNoOfScans();
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    public void spikeReservationProbs(ArrayList<Bot> botsPerCaption, Bot currentBot, Caption caption) {
        for (Bot b: botsPerCaption) {
            if (b != currentBot && b.getActive()) {
              //  b.incrementNoOfScans();
                b.increaseReservationProb(eggData, caption);
            }
        }
    }

    public void incrementBotChecks(ArrayList<Bot> botsPerCaption, Bot currentBot) {
        for (Bot b: botsPerCaption) {
            if (b != currentBot && b.getActive()) {
                b.incrementNoOfScans();
            }
        }
    }

    public void flushBotOperations(Caption capt, ArrayList<Bot> bpc) {
        for (Bot bot : bpc) {
            bot.setActive(true);
            if (reservations.containsKey(bot.name)) {
                while (bot.getInReservation()) {

                 //   allMutations.get(trData.indexOf(capt)).add(bot.editCaptionFour(/*trData,*/ capt, eggData, reservations));
                    allMutations.get(trData.indexOf(capt)).addAll(bot.editCaptionFour(/*trData,*/ capt, eggData, reservations));

                    if (bot.getUpdate() != null) {
                        updateReservationsIndexes(bot.getUpdate());
                    //    bot.setUpdateArgs(null);
                        bot.clearUpdateArgs();
                    }
                }
            }
        }
    }

    public void reinitialiseBotStates(Caption caption) {
        for (Bot bot: bots) {
            bot.setState(null);
            bot.setInReservation(false);
            bot.resetOperations();
            bot.setActive(true);
            bot.resetScans(caption, eggData);
            bot.setReservationProbability(1);
        }
    }


    public void updateReservationsIndexes(ArrayList<ReservationUpdate> resUp/*ReservationUpdate r*/) {
        for (ReservationUpdate r: resUp) {
            if (r.getAction().equals("delete")) {
                for (String key : reservations.keySet()) {
                    if (reservations.get(key).getReservationIndex() > r.getLocation()) {
                        reservations.get(key).updateReservationIndex(r.getUpdateValue());
                        reservations.get(key).updateCursorIndex(r.getUpdateValue());
                    }
                }
            } else {
                for (String key : reservations.keySet()) {
                    if (reservations.get(key).getReservationIndex() > r.getLocation()) {
                        reservations.get(key).updateReservationIndex(r.getUpdateValue());
                    }
                    if (reservations.get(key).getCursorIndex() >= r.getLocation()) {
                        reservations.get(key).updateCursorIndex(r.getUpdateValue());
                    }
                }
            }
        }
    }

    public int randomInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}

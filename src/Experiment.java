import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pro on 06/02/2018.
 */
public class Experiment {

  FileReader reader;
  ReadFilesHandler rfh;
  CapsNoPunctuation remPunc;
  ArrayList<meanVariancePair> botDistributions;
  ArrayList<String> srTranscripts;
  String trueTranscriptFilePath;
  String eggcornsFilePath;

  ArrayList<ArrayList<Mutation>> returnedMutations;
  ArrayList<Result> results;

  ArrayList<String> botEditedScripts;
  //HashMap<Double, Integer> results;

  public Experiment() {
    reader = new FileReader();
    rfh = new ReadFilesHandler();
    srTranscripts = new ArrayList<>();
    results = new ArrayList<>();
    botEditedScripts = new ArrayList<>();
    returnedMutations = new ArrayList<>();
    remPunc = new CapsNoPunctuation();
  }


  // Change the distribution of probabilities of Bot competences. I.e. changing variance of the probability distribution
  // For each probability distribution. Apply to each of the speech recognition transcripts.
  //        - For each

  // 1) Create instance of simulator
  //    a) Update BotInitialiser method to take a variance for distribution from which we want to take our bots competance level from
  //    b)

  // 1) Create instance of simulator
  // 2)
  // 3) read in 100% correct transcript
  // 4) read in SR transcript i
  // 5) quantify distance between the 100% correct transcript and SR transcript i
  // 6) for ( number of different bot variance distributions )
  // 7)   initialise bots with weights (n variance of bot probs)
  // 8)   for (different transcripts)
  // 9)         read in SR transcript i
  //10)         quantify distance between the 100% correct transcript and SR transcript i
  //11)         for (Run the simulation 10 times)
  //                   for each run take the resulting transcript and quantify distance between itself and 100% correct transcript
  //            average distances from 'corrected' transcript i to 100% correct
  //            add to csv file
  //
    //
  // how to calculate distance ?
  //

  public void runExperiment() {

      LevenshteinDistance distance = new LevenshteinDistance();

      getDataPaths();
      String trueTranscript = reader.readFile(trueTranscriptFilePath);
    //  String trueTranscript = reader.read(trueTranscriptFilePath);

      //String trueTransName = "original_transcript.txt";


      meanVarianceList(0.01, 1 , 0);

      Simulator sim;

      for (Integer t = 0; t < srTranscripts.size(); t++) {
          String currentFileAsString = reader.readFile(srTranscripts.get(t));
          Integer origDist = distance.apply(rfh.remPuncFromString(reader.readFile(srTranscripts.get(t))), rfh.remPuncFromString(trueTranscript));
          results.add(new Result(new meanVariancePair(0,0), origDist, origDist));
          sim = new Simulator();
          sim.reinitialiseSimulator(trueTranscriptFilePath ,srTranscripts.get(t), eggcornsFilePath);
          for ( meanVariancePair pair: botDistributions) {
              sim.botInitialiserTwo(pair.getVariance(), /*pair.getVariance()*/1, pair.getMean(), pair.getMean());
              ArrayList<Result> bufferResults = new ArrayList<>();
              for (Integer i = 0; i < 10; i++) {
               //   Simulator sim = new Simulator(srTranscripts.get(t-1), ef, variance);
                  returnedMutations = sim.Simulate();
                  String alignScript = getAlignAndConsensusScript(returnedMutations, sim);
                  ArrayList<Caption> botEditedData = sim.getTrData();
                  String botEditedScript = convertDataToString(botEditedData);
                  botEditedScripts.add(botEditedScript);
                  Integer editedDistTrue = distance.apply(rfh.remPuncFromString(botEditedScript), rfh.remPuncFromString(trueTranscript));
                  Integer alignDistTrue = distance.apply(alignScript, rfh.remPuncFromString(trueTranscript));
                  bufferResults.add(new Result(pair, editedDistTrue, alignDistTrue));
               //   sim.setTrData(new ArrayList<>(srTranscripts.get(t-1)));
                  sim.reinitialiseSimulator(trueTranscriptFilePath, srTranscripts.get(t), eggcornsFilePath);
              }
              results.add(new Result(pair, calcBotDistMean(bufferResults), calcAlignDistMean(bufferResults)));
              bufferResults.clear();
          }
          produceTestCSV("Transcript_" + (t + 1) + "_Results_SpComRand.csv");
          results.clear();
      }
  }

  public String getAlignAndConsensusScript(ArrayList<ArrayList<Mutation>> allMutations, Simulator sim) {

      StringBuilder finalScriptAsSB = new StringBuilder("");
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
      double[][] subMatrix = new double[27][27];


      for (int i = 0; i < subMatrix.length; i++) {
          for (int j = 0; j < subMatrix.length; j++) {
              if (i == j) {
                  subMatrix[i][j] = 0.0;
              }

              else {
                  subMatrix[i][j] = -1.0;
              }
          }
      }

      SubstitutionMatrix subs = new SubstitutionMatrix(alphabet, subMatrix);
      Stage1Aligner aligner = new Stage1Aligner(subs);
      FinishedCaptionFilter getCandidates = new FinishedCaptionFilter();


      HashMap<String, Double> reputations = new HashMap<>();
      ArrayList<Bot> bots = sim.getBots();
      for (Bot bot: bots) {
          reputations.put(bot.getName(), 1.0);
      }
      List<Caption> rebuiltCaptions;
      List<Caption> candidateCaptions;
      AverageReputation arep = new AverageReputation(reputations, 1.0);
      CaptionHistoryGenerator gen = new CaptionHistoryGenerator(arep);
      for (ArrayList<Mutation> captionMuts: allMutations) {
          rebuiltCaptions = gen.getHistory(captionMuts, reputations);
          candidateCaptions = getCandidates.filter(captionMuts, rebuiltCaptions);
          if (candidateCaptions.size() > 2) {
              System.out.println("GREATER THAN TWO");
          }
          String[] candCapAsArray = new String[candidateCaptions.size()];
          double[] captionReps = new double[candidateCaptions.size()];


          for (Integer i = 0; i < candidateCaptions.size(); i++) {
              candidateCaptions.get(i).setText(remPunc.clean(candidateCaptions.get(i)));
              captionReps[i] = candidateCaptions.get(i).getReputation();
              candCapAsArray[i] = candidateCaptions.get(i).getText();
          }
          String[] alignments = aligner.align(candCapAsArray, captionReps);
          String consensus = new FrequencyVoter().vote(alignments);
          System.out.println("CONSENSUS: " + consensus);
          if (allMutations.indexOf(captionMuts) == allMutations.size() - 1) {
              finalScriptAsSB.insert(finalScriptAsSB.length(), consensus);
          }
          else {
              finalScriptAsSB.insert(finalScriptAsSB.length(), consensus + " ");
          }
      }
      String finalScript = new String(finalScriptAsSB);
      finalScript = rfh.setToLowerCase(finalScript);
      return finalScript;
  }

  public Integer calcBotDistMean(ArrayList<Result> results) {
      Integer avg = 0;
      for (Result res: results) {
          avg += res.getBotEditLeveDist();
      }
      avg = avg / results.size();
      return avg;
  }

    public Integer calcAlignDistMean(ArrayList<Result> results) {
        Integer avg = 0;
        for (Result res: results) {
            avg += res.getAlignAlgLeveDist();
        }
        avg = avg / results.size();
        return avg;
    }

  public String getProjectPath() {
      File currentDirFile = new File(".");
      String helper = null;
      try {
          helper = currentDirFile.getCanonicalPath();
      } catch (IOException e) {
          e.printStackTrace();
      }
      return helper;
  }

  public void getDataPaths() {
      String projectPath = getProjectPath();
      File transDir = new File(projectPath + "\\transcripts");
      File eggDir = new File(projectPath + "\\eggcorns");
      for (File f: transDir.listFiles()) {
          if (f.getName().contains("SpeechRec")) {
              srTranscripts.add(f.getAbsolutePath());
          }
          else if (f.getName().equals("original_transcript.txt")){
              trueTranscriptFilePath = f.getAbsolutePath();
          }
      }
      eggcornsFilePath = eggDir.listFiles()[0].getAbsolutePath();
  }

  public String convertDataToString(ArrayList<Caption> transcriptAsData) {
    String dataAsString = rfh.dataStructToString(transcriptAsData);
    return dataAsString;
  }

  public void produceTestCSV(String filename) {
    String projectPath = getProjectPath();
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new File(projectPath + "\\csv_outputs\\" + filename));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    StringBuilder sb = new StringBuilder();
      sb.append("Mean of Bot Competance Dstbn");
      sb.append(',');
    sb.append("Variance of Bot Competance Dstbn");
    sb.append(',');
    sb.append("Bot Only Levenshtein Distance");
      sb.append(',');
      sb.append("Align Consensus Levenshtein Distance");
    sb.append('\n');


    for(Result t : results) {
      sb.append(t.getPair().getMean());
      sb.append(',');
      sb.append(t.getPair().getVariance());
      sb.append(',');
      sb.append(t.getBotEditLeveDist());
      sb.append(',');
      sb.append(t.getAlignAlgLeveDist());
      sb.append('\n');
    }
    pw.write(sb.toString());
    pw.close();
  }

  /*
  public void createVarianceList(double min) {
      double adder = 0;
      ArrayList<Double> vars = new ArrayList<>();
      for (Integer i = 0 ; i < 100; i++) {
          adder = adder + min;
          vars.add(adder);
      }
      for (Integer i = 0 ; i < 100; i++) {
          adder = adder - min;
          if ( i != 99) {
              vars.add(adder * -1);
          }
          else {
              vars.add(-0.01);
          }
      }
      this.botSuccessVariances = vars;
  }
*/
    public void meanVarianceList(double min, int meanOne, int meanZero) {
        double adder = 0;
        ArrayList<meanVariancePair> vars = new ArrayList<>();
        for (Integer i = 0 ; i < 100; i++) {
            adder = adder + min;
            vars.add(new meanVariancePair(meanOne, adder));
        }
        adder = adder + min;
        for (Integer i = 0 ; i < 100; i++) {
            adder = adder - min;
            vars.add(new meanVariancePair(0 ,(adder)));
        }
        this.botDistributions = vars;
    }
}

//make bot variances constant per transcript!!
//claculate dist between trans before editing and true transcript

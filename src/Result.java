/**
 * Created by Pro on 07/02/2018.
 */
public class Result {

    //double variance;
    meanVariancePair pair;
    Integer botEditLeveDist;
    Integer alignAlgLeveDist;

    public Result(/*double variance*/ meanVariancePair pair, Integer botEditLeveDist, Integer alignAlgLeveDist) {
        this.pair = pair;
        this.botEditLeveDist = botEditLeveDist;
        this.alignAlgLeveDist = alignAlgLeveDist;
    }

    public meanVariancePair getPair() {
        return pair;
    }

    public Integer getBotEditLeveDist() {
        return botEditLeveDist;
    }

    public Integer getAlignAlgLeveDist() {
        return alignAlgLeveDist;
    }
}

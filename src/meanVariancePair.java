/**
 * Created by Pro on 12/02/2018.
 */
public class meanVariancePair {

    Integer mean;
    double variance;
    String sRepresentation;

    public meanVariancePair(Integer mean, double variance){
        this.mean = mean;
        this.variance = variance;
        sRepresentation = "m: " + mean + " v: " + variance;
    }

    public double getVariance() {
        return variance;
    }

    public Integer getMean() {
        return mean;
    }

    public String getsRepresentation() {
        return sRepresentation;
    }
}

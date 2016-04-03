package org.movsim;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class ResultCore {
    private String path;
    private int[] individus;
    private double result;

    public ResultCore(String path) {
        this.path = path;
    }

    public ResultCore(int[] individus, double result) {
        this.individus = individus;
        this.result = result;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int[] getIndividus() {
        return individus;
    }

    public void setIndividus(int[] individus) {
        this.individus = individus;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

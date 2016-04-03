package org.movsim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class ResultCore {
    private String path;
    private List<Integer> genes;
    private double result;

    public ResultCore() {
        genes = new ArrayList<>();
    }

    public ResultCore(String path) {
        this.path = path;
        genes = new ArrayList<>();
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public void setGenes(List<Integer> genes) {
        this.genes = genes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

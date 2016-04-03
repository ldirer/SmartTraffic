package org.movsim;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class ResultCore implements Comparable<ResultCore> {
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

    @Override
    public int compareTo(ResultCore o) {
        return (int) Math.signum(this.getResult() - o.getResult());
    }


    public void updateScore() {
        if (path == null) {
            path = "D:\\workspaces\\SmartTraffic\\sim\\generated\\" + 0 + ".xprj";
        }
        try {
            Executor.run(this);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}

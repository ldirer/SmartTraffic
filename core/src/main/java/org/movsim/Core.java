package org.movsim;

import org.movsim.autogen.Movsim;
import org.movsim.input.MovsimCommandLine;
import org.movsim.input.ProjectMetaData;
import org.movsim.logging.Logger;
import org.movsim.simulator.Simulator;
import org.movsim.xml.InputLoader;

import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class Core implements Callable<ResultCore> {
    private ResultCore result;

    public Core( ResultCore result) {
        this.result = result;
    }

    @Override
    public ResultCore call() throws Exception {
        Locale.setDefault(Locale.US);

        Logger.initializeLogger();

        MovsimCommandLine.parse(new String[]{"-f", result.getPath()});

        ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        if (!projectMetaData.hasProjectName()) {
            throw new IllegalArgumentException("no xml simulation configuration path provided.");
        }

        // unmarshall movsim configuration path
        Movsim movsimInput = InputLoader.unmarshallMovsim(projectMetaData.getInputFile());
        Simulator simulator = new Simulator(movsimInput);
        simulator.initialize();
        // simulator.runToCompletion();
        double cost = simulator.runToCompletionWithCost();
        System.out.println(String.format("HACK: cost is %s", cost));
        result.setResult(cost);
        return result;
    }
}

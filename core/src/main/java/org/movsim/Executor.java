package org.movsim;

import org.movsim.autogen.Movsim;
import org.movsim.input.MovsimCommandLine;
import org.movsim.input.ProjectMetaData;
import org.movsim.logging.Logger;
import org.movsim.simulator.Simulator;
import org.movsim.xml.InputLoader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.Locale;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public abstract class Executor {

    public static ResultCore run(ResultCore core) throws JAXBException, SAXException {
        Locale.setDefault(Locale.US);

        Logger.initializeLogger();

        MovsimCommandLine.parse(new String[]{"-f", core.getPath()});//fichier !

        ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        if (!projectMetaData.hasProjectName()) {
            throw new IllegalArgumentException("no xml simulation configuration file provided.");
        }

        // unmarshall movsim configuration file
        Movsim movsimInput = InputLoader.unmarshallMovsim(projectMetaData.getInputFile());
        Simulator simulator = new Simulator(movsimInput);
        simulator.initialize();

        double cost = simulator.runToCompletionWithCost();
        System.out.println(String.format("CISCO - cost is %s", cost));
        System.out.println(String.format("CISCO - fitness is %s", -cost));
        core.setResult(-cost);
        return core;
    }
}

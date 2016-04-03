import org.movsim.Executor;
import org.movsim.ResultCore;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class Generator {

    public static void main(String[] argv) {

        List<ResultCore> resultats = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30; i++) {
            try {
                ResultCore core = new ResultCore("D:\\workspaces\\SmartTraffic\\sim\\generated\\" + 0 + ".xprj");
                resultats.add(core);
                int[] individus = new int[12];

                for (int j = 0; j < individus.length; j++) {
                    individus[j] = (int) (Math.random() * 90);
                }
                core.setGenes(individus);

                //Create File
                createFile(core);

                try {
                    //Run ! core is modified inplace.
                    Executor.run(core);
                } catch (JAXBException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                try {
                    Executor.run(core);
                } catch (JAXBException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("END " + (System.currentTimeMillis() - startTime));
    }

    private static void createFile(ResultCore core) throws IOException {
        BufferedWriter bf = new BufferedWriter(new FileWriter(core.getPath()));
        bf.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Movsim>\n" +
                "    <VehiclePrototypes>\n" +
                "        <VehiclePrototypeConfiguration label=\"IDM1\" length=\"6\" maximum_deceleration=\"9\">\n" +
                "            <AccelerationModelType>\n" +
                "                <ModelParameterIDM v0=\"20\" T=\"1.2\" s0=\"3\" s1=\"2\" delta=\"4\" a=\"1.2\" b=\"2.0\" />\n" +
                "            </AccelerationModelType>\n" +
                "            <LaneChangeModelType european_rules=\"true\" crit_speed_eur=\"20\">\n" +
                "                <ModelParameterMOBIL safe_deceleration=\"5.0\" minimum_gap=\"2.0\" threshold_acceleration=\"0.1\" right_bias_acceleration=\"0.05\" politeness=\"0.1\" />\n" +
                "            </LaneChangeModelType>\n" +
                "        </VehiclePrototypeConfiguration>\n" +
                "        <VehiclePrototypeConfiguration label=\"ACC1\" length=\"6\" maximum_deceleration=\"9\">\n" +
                "            <AccelerationModelType>\n" +
                "                <ModelParameterACC v0=\"20\" T=\"1.2\" s0=\"3\" s1=\"2\" delta=\"4\" a=\"1.2\" b=\"2.0\" coolness=\"1.0\" />\n" +
                "            </AccelerationModelType>\n" +
                "            <LaneChangeModelType european_rules=\"true\" crit_speed_eur=\"20\">\n" +
                "                <ModelParameterMOBIL safe_deceleration=\"5.0\" minimum_gap=\"2.0\" threshold_acceleration=\"0.1\" right_bias_acceleration=\"0.05\" politeness=\"0.1\" />\n" +
                "            </LaneChangeModelType>\n" +
                "        </VehiclePrototypeConfiguration>\n" +
                "        <!-- trucks -->\n" +
                "        <VehiclePrototypeConfiguration label=\"IDM2\" length=\"16\" maximum_deceleration=\"9\">\n" +
                "            <AccelerationModelType>\n" +
                "                <ModelParameterIDM v0=\"18\" T=\"1.5\" s0=\"4\" s1=\"4\" delta=\"4\" a=\"0.8\" b=\"2.0\" />\n" +
                "            </AccelerationModelType>\n" +
                "            <LaneChangeModelType european_rules=\"true\" crit_speed_eur=\"20\">\n" +
                "                <ModelParameterMOBIL safe_deceleration=\"4.0\" minimum_gap=\"2.0\" threshold_acceleration=\"0.2\" right_bias_acceleration=\"0.3\" politeness=\"0.1\" />\n" +
                "            </LaneChangeModelType>\n" +
                "        </VehiclePrototypeConfiguration>\n" +
                "    </VehiclePrototypes>\n" +
                "    <Scenario network_filename=\"trafficlight.xodr\">\n" +
                "        <Simulation timestep=\"0.2\" crash_exit=\"false\">\n" +
                "            <TrafficComposition>\n" +
                "                <VehicleType label=\"IDM1\" fraction=\"0.4\" relative_v0_randomization=\"0.2\" />\n" +
                "                <VehicleType label=\"ACC1\" fraction=\"0.4\" relative_v0_randomization=\"0.2\" />\n" +
                "                <VehicleType label=\"IDM2\" fraction=\"0.2\" relative_v0_randomization=\"0.2\" />\n" +
                "            </TrafficComposition>\n" +
                "\n" +
                "            <Road id=\"1\">\n" +
                "                <TrafficSource logging=\"false\">\n" +
                "                    <Inflow t=\"0\" q_per_hour=\"500\" />\n" +
                "                </TrafficSource>\n" +
                "                <Detectors />\n" +
                "            </Road>\n" +
                "            <Road id=\"2\">\n" +
                "                <TrafficSource logging=\"false\">\n" +
                "                    <Inflow t=\"0\" q_per_hour=\"500\" />\n" +
                "                </TrafficSource>\n" +
                "                <Detectors />\n" +
                "            </Road>\n" +
                "            <Road id=\"3\">\n" +
                "                <TrafficSource logging=\"false\">\n" +
                "                    <Inflow t=\"0\" q_per_hour=\"500\" />\n" +
                "                </TrafficSource>\n" +
                "                <Detectors />\n" +
                "            </Road>\n" +
                "            <Road id=\"4\">\n" +
                "                <TrafficSource logging=\"false\">\n" +
                "                    <Inflow t=\"0\" q_per_hour=\"500\" />\n" +
                "                </TrafficSource>\n" +
                "                <Detectors />\n" +
                "            </Road>\n" +
                "\n" +
                "        </Simulation>\n" +
                "        <TrafficLights>\n" +
                "            <ControllerGroup id=\"plan1\">\n" +
                "                <Phase duration=\"" + core.getGenes()[0] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Green\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[1] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"GreenRed\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[2] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Red\" />\n" +
                "                </Phase>\n" +
                "            </ControllerGroup>\n" +
                "\n" +
                "            <ControllerGroup id=\"plan2\">\n" +
                "                <Phase duration=\"" + core.getGenes()[3] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Green\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[4] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"GreenRed\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[5] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Red\" />\n" +
                "                </Phase>\n" +
                "            </ControllerGroup>\n" +
                "\n" +
                "            <ControllerGroup id=\"plan3\">\n" +
                "                <Phase duration=\"" + core.getGenes()[6] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Red\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[7] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"RedGreen\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[8] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Green\" />\n" +
                "                </Phase>\n" +
                "            </ControllerGroup>\n" +
                "\n" +
                "            <ControllerGroup id=\"plan4\">\n" +
                "                <Phase duration=\"" + core.getGenes()[9] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Red\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[10] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"RedGreen\" />\n" +
                "                </Phase>\n" +
                "                <Phase duration=\"" + core.getGenes()[11] + "\">\n" +
                "                    <TrafficLightState type=\"firstLight\" status=\"Green\" />\n" +
                "                </Phase>\n" +
                "            </ControllerGroup>\n" +
                "        </TrafficLights>\n" +
                "\n" +
                "        <Routes>\n" +
                "            <Route label=\"R1\">\n" +
                "                <Road id=\"1\"/>\n" +
                "            </Route>\n" +
                "            <Route label=\"R2\">\n" +
                "                <Road id=\"2\"/>\n" +
                "            </Route>\n" +
                "            <Route label=\"R3\">\n" +
                "                <Road id=\"3\"/>\n" +
                "            </Route>\n" +
                "            <Route label=\"R4\">\n" +
                "                <Road id=\"4\"/>\n" +
                "            </Route>\n" +
                "        </Routes>\n" +
                "        <OutputConfiguration>\n" +
                "            <TravelTimes dt=\"1\" route=\"R4\"/>\n" +
                "            <TravelTimes dt=\"1\" route=\"R3\"/>\n" +
                "            <TravelTimes dt=\"1\" route=\"R2\"/>\n" +
                "            <TravelTimes dt=\"1\" route=\"R1\"/>\n" +
                "        </OutputConfiguration>\n" +
                "    </Scenario>\n" +
                "</Movsim>\n");
        bf.close();
    }

}

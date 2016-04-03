import org.movsim.Executor;
import org.movsim.ResultCore;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Timothy Nibeaudeau on 03/04/2016 for Movsim.
 */
public class Launcher {

    public static void main(String[] args) {
        ResultCore core = new ResultCore("D:\\workspaces\\SmartTraffic\\sim\\test\\trafficlight.xprj");
        try {
            Executor.run(core);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}

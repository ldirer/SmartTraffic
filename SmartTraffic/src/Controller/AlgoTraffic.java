package Controller;

import Model.TrafficActuel;
import Model.Voiture;

/**
 * Created by Mathieu on 02/04/2016.
 */
public class AlgoTraffic {


    private TrafficActuel trafficActuel = new TrafficActuel();

    public void ajouterVoiture(Voiture voiture, String route){
        if(voiture != null && route.toLowerCase().compareTo("verticale") == 0){
            trafficActuel.ajouterVerticale(voiture);
        }else if(voiture != null && route.toLowerCase().compareTo("horizontale") == 0){
            trafficActuel.ajouterHorizontale(voiture);
        }
    }






}

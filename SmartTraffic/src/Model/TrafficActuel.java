package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathieu on 02/04/2016.
 */
public class TrafficActuel {

    private int idVoiture=0;
    private List<Voiture> fileHorizontale = new ArrayList<>();
    private List<Voiture> fileVerticale = new ArrayList<>();
    private double tempSystem=System.currentTimeMillis();
    private double TempsMoyenHorizontale;
    private double TempsMoyenVerticale;

    public List<Voiture> getFileHorizontale(){
        return fileHorizontale;
    }

    public List<Voiture> getFileVerticale(){
        return fileVerticale;
    }


    public void ajouterHorizontale(){
        Voiture voiture = new Voiture(idVoiture);
        idVoiture++;
        fileHorizontale.add(voiture);
    }
    public void ajouterVerticale(){
        Voiture voiture = new Voiture(idVoiture);
        idVoiture++;
        fileVerticale.add(voiture);
    }

}

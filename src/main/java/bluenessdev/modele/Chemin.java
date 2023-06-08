package bluenessdev.modele;

import java.util.ArrayList;

public class Chemin {
    private ArrayList<Integer> numQuete;
    private int nbPas;
    private int temps;
    private int nbQuete;
    private int xp;

    public Chemin(ArrayList<Quete> parchemin, Scenario scenario){
        Niveau2 niveau2 = new Niveau2(scenario);

        nbPas = niveau2.calulerNombrePas(parchemin);
        temps = niveau2.calulerTempsChemin(parchemin);
        nbQuete = niveau2.calulerNombreQuetes(parchemin);
        xp = niveau2.calulerXPChemin(parchemin);
        numQuete = new ArrayList<>();
       for (Quete quete : parchemin){
           numQuete.add(quete.getNumero());
       }


    }

    public ArrayList<Integer> getNumQuete(){
        return numQuete;
    }

    public int getNbPas() {
        return nbPas;
    }

    public int getTemps() {
        return temps;
    }

    public int getNbQuete() {
        return nbQuete;
    }

    public int getXp() {
        return xp;
    }
}

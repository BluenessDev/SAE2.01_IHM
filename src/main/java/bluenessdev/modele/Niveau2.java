package bluenessdev.modele;

import java.util.ArrayList;
import java.util.Comparator;

public class Niveau2 {

    private Scenario scenario;
    private final Joueur joueur;
    private ArrayList<ArrayList<Quete>> liste_chemins = new ArrayList<>();
    private ArrayList<Quete> cheminMin;
    private final Quete quete0;
    private static int tempsmin = 1000000; //valeur arbitraire
    private static int nbQuetes = 1000000; //valeur arbitraire
    private static int nbPas = 1000000; //valeur arbitraire
    private int counter = 0;

    /**
     * Constructeur de la classe Niveau2
     * @param parScenario Scenario, le scenario du jeu
     */
    public Niveau2(Scenario parScenario) {
        scenario = parScenario;
        joueur = new Joueur(scenario);
        quete0 = joueur.getQueteFinale(scenario);
    }

    /**
     * Methode qui renvoie la liste des chemins
     * @return ArrayList ArrayList, la liste des chemins
     */
    public ArrayList<ArrayList<Quete>> getListe_chemins() {
        return liste_chemins;
    }


    /**
     * Methodes qui verifie les quetes
     * @param parListe ArrayList, la liste des quetes
     * @return boolean, true si le chemin est valide, false sinon
     */
    public boolean estValide(ArrayList<Quete> parListe) {
        Joueur joueur1 = new Joueur(scenario);
        for (Quete quete : parListe) {
            joueur1.getRealisables();
            if (joueur1.queteFinaleRealisable()) {
                joueur1.realiserQuete(quete0);
                return true;
            }
            if (joueur1.estRealisable(quete)) {
                joueur1.realiserQuete(quete);
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Methode qui verifie si un chemin est exhaustif
     * @param chemin ArrayList, le chemin actuel
     * @param parScenario Scenario, le scenario actuel
     * @return boolean, true si le chemin est exhaustif, false sinon
     */
    public boolean estExhaustif(ArrayList<Quete> chemin, Scenario parScenario) {
        return chemin.containsAll(parScenario.getStaticProvQuetes());
    }

    //==========================================================================
    //================== Methodes de parcours à N solutions ====================
    //==========================================================================

    /**
     * Methode qui initalise le parcours en profondeur à N solutions
     */
    public void intialisation(){
        int cap = 125000; //valeur pour eviter que le programme ne tourne trop longtemps
        ArrayList<Quete> liste_quetes = new ArrayList<>();
        boolean[] quetesConnues = new boolean[scenario.getStaticProvQuetes().size()];
        for (Quete quete : scenario.getStaticProvQuetes()) {
            if (quete.aucunePrecond()) {
                joueur.realiserQuete(quete);
                parcoursEnProfondeur(quete, liste_quetes, quetesConnues, cap);
                joueur.reinitialiserJoueur();
            }
        }
    }

    /**
     * Methode qui parcourt en profondeur à N solutions
     * @param quete Quete, la quete actuelle
     * @param chemin ArrayList, le chemin actuel
     * @param quetesConnues boolean[], les quetes connues
     * @param parCap int, le maximum de solutions pour eviter que le programme ne tourne trop longtemps
     */
    public void parcoursEnProfondeur(Quete quete, ArrayList<Quete> chemin, boolean[] quetesConnues, int parCap) {
        if (parCap == counter) {
            return;
        }

        joueur.getRealisables();
        ArrayList<Quete> listeDisponibles = new ArrayList<>(joueur.getQuetesRealisables());
        quetesConnues[quete.getNumero()] = true;
        chemin.add(quete);

        if (quete.equals(quete0)) {
            counter++;
            if (!liste_chemins.contains(chemin) && estValide(chemin)) {
                liste_chemins.add(new ArrayList<>(chemin));
            }
        } else {
            for (Quete prochaineQuete : listeDisponibles) {
                if (!quetesConnues[prochaineQuete.getNumero()]) {
                    joueur.realiserQuete(prochaineQuete);
                    parcoursEnProfondeur(prochaineQuete, chemin, quetesConnues, parCap);
                    joueur.annulerRealisationQuete(prochaineQuete);
                }
            }
        }
        quetesConnues[quete.getNumero()] = false;
        chemin.remove(chemin.size() - 1);
    }

    //==========================================================================
    //================ Methodes d'initialisation à N solutions =================
    //==========================================================================

    /**
     * Methode qui renvoie les N meilleurs chemins efficace en terme de temps
     * @param ParN nombre de chemins à retourner
     * @return ArrayList, la liste des chemins les plus efficace en terme de temps
     */
    public ArrayList<ArrayList<Quete>> tempsPlusCourtEfficaceMeilleur(int ParN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsTemps = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsTemps)) {
            int temps = calulerTempsChemin(listeQuetes);
            if (estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsTemps.remove(listeQuetes);
            }
            if (temps < tempsmin) {
                tempsmin = temps;
            }
        }
        trierCheminsParTempsMeilleur(cheminsMoinsTemps);
        while (cheminsMoinsTemps.size() > ParN) {
            cheminsMoinsTemps.remove(cheminsMoinsTemps.size() - 1);
        }
        return cheminsMoinsTemps;
    }

    /**
     * Methode qui renvoie les N meilleurs chemins exhaustifs en terme de temps
     * @param parN nombre de chemins à retourner
     * @return liste des chemins les plus courts
     */
    public ArrayList<ArrayList<Quete>> tempsPlusCourtExhaustifMeilleur(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsTemps = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsTemps)) {
            if (!estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsTemps.remove(listeQuetes);
            }
            int temps = calulerTempsChemin(listeQuetes);
            if (temps < tempsmin) {
                tempsmin = temps;
            }
        }
        trierCheminsParTempsMeilleur(cheminsMoinsTemps);
        while (cheminsMoinsTemps.size() > parN) {
            cheminsMoinsTemps.remove(cheminsMoinsTemps.size() - 1);
        }
        return cheminsMoinsTemps;
    }

    /**
     * Retourne les N meilleurs chemins en fonction du nombre de quetes
     * @param parN nombre de solutions à retourner
     * @return les N meilleurs chemins en fonction du nombre de quetes
     */
    public ArrayList<ArrayList<Quete>> moinsDeQuetesEfficaceMeilleur(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsQuetes = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsQuetes)) {
            int nbQuetesActuel = calulerNombreQuetes(listeQuetes);
            if (estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsQuetes.remove(listeQuetes);
            }
            if (nbQuetesActuel < nbQuetes) {
                nbQuetes = nbQuetesActuel;
            }
        }
        trierCheminsParQuetesMeilleur(cheminsMoinsQuetes);
        while (cheminsMoinsQuetes.size() > parN) {
            cheminsMoinsQuetes.remove(cheminsMoinsQuetes.size() - 1);
        }
        return cheminsMoinsQuetes;
    }

    /**
     * Retourne les N meilleurs chemins en fonction du nombre de deplacements
     * @param parN nombre de solutions à retourner
     * @return liste des N meilleurs chemins qui prennent le moins de deplacements
     */
    public ArrayList<ArrayList<Quete>> moinsDeDeplacementsEfficaceMeilleur(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsPas = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsPas)) {
            int nbPasAcutuel = calulerNombrePas(listeQuetes);
            if (nbPasAcutuel < nbPas) {
                nbPas = nbPasAcutuel;
            }
        }
        trierCheminsParDistanceMeilleur(cheminsMoinsPas);
        while (cheminsMoinsPas.size() > parN) {
            cheminsMoinsPas.remove(cheminsMoinsPas.size() - 1);
        }
        return cheminsMoinsPas;
    }

    /**
     * Retourne les N meilleurs chemins en fonction du nombre de quetes
     * @param parN nombre de solutions à retourner
     * @return liste des N meilleurs chemins
     */
    public ArrayList<ArrayList<Quete>> moinsDeDeplacementsExhaustifMeilleur(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsPas = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsPas)) {
            if (!estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsPas.remove(listeQuetes);
            }
            int nbPasAcutuel = calulerNombrePas(listeQuetes);
            if (nbPasAcutuel < nbPas) {
                nbPas = nbPasAcutuel;
            }
        }
        trierCheminsParDistanceMeilleur(cheminsMoinsPas);
        while (cheminsMoinsPas.size() > parN) {
            cheminsMoinsPas.remove(cheminsMoinsPas.size() - 1);
        }
        return cheminsMoinsPas;
    }

    /**
     * Retourne les n chemins qui prennent le plus de temps
     * @param parN nombre de solution à retourner
     * @return les chemins qui prennent le plus de temps
     */
    public ArrayList<ArrayList<Quete>> tempsPlusCourtEfficacePire(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsPlusCourt = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsPlusCourt)) {
            int temps = calulerTempsChemin(listeQuetes);
            if (temps < tempsmin) {
                tempsmin = temps;
            }
        }
        trierCheminsParTempsPire(cheminsPlusCourt);
        while (cheminsPlusCourt.size() > parN) {
            cheminsPlusCourt.remove(cheminsPlusCourt.size() - 1);
        }
        return cheminsPlusCourt;
    }

    /**
     * Retourne les N chemins qui prennent le moins de temps
     * @param parN nombre de solution à retourner
     * @return liste des chemins exhaustifs qui prennent les moins de temps
     */
    public ArrayList<ArrayList<Quete>> tempsPlusCourtExhaustifPire(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsPlusCourt = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsPlusCourt)) {
            if (!estExhaustif(listeQuetes, scenario)) {
                cheminsPlusCourt.remove(listeQuetes);
            }
            int temps = calulerTempsChemin(listeQuetes);
            if (temps < tempsmin) {
                tempsmin = temps;
            }
        }
        trierCheminsParTempsPire(cheminsPlusCourt);
        while (cheminsPlusCourt.size() > parN) {
            cheminsPlusCourt.remove(cheminsPlusCourt.size() - 1);
        }
        return cheminsPlusCourt;
    }

    /**
     * Retourne les N chemins les plus courts en terme de quetes
     * @param parN nombre de chemins à retourner
     * @return les chemins efficaces les plus courts en terme de quetes
     */
    public ArrayList<ArrayList<Quete>> moinsDeQuetesEfficacePire(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsQuetes = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsQuetes)) {
            if (estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsQuetes.remove(listeQuetes);
            }
            int nbQuetesActuel = calulerNombreQuetes(listeQuetes);
            if (nbQuetesActuel < nbQuetes) {
                nbQuetes = nbQuetesActuel;
            }
        }
        trierCheminsParQuetesPire(cheminsMoinsQuetes);
        while (cheminsMoinsQuetes.size() > parN) {
            cheminsMoinsQuetes.remove(cheminsMoinsQuetes.size() - 1);
        }
        return cheminsMoinsQuetes;
    }

    /**
     * Retourne les N chemins les plus courts en terme de distance
     * @param parN nombre de chemins à retourner
     * @return les chemins efficaces les plus courts en terme de distance
     */
    public ArrayList<ArrayList<Quete>> moinsDeDeplacementsEfficacePire(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsMoinsPas = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsMoinsPas)) {
            if (estExhaustif(listeQuetes, scenario)) {
                cheminsMoinsPas.remove(listeQuetes);
            }
            int nbPasAcutuel = calulerNombrePas(listeQuetes);
            if (nbPasAcutuel < nbPas) {
                nbPas = nbPasAcutuel;
            }
        }
        trierCheminsParDistancePire(cheminsMoinsPas);
        while (cheminsMoinsPas.size() > parN) {
            cheminsMoinsPas.remove(cheminsMoinsPas.size() - 1);
        }
        return cheminsMoinsPas;
    }

    /**
     * Retourne les N chemins les plus courts en terme de distance
     * @param parN nombre de chemins à retourner
     * @return les chemins exhaustif les plus courts en terme de distance
     */
    public ArrayList<ArrayList<Quete>> moinsDeDeplacementsExhaustifPire(int parN) {
        intialisation();
        ArrayList<ArrayList<Quete>> cheminsPlusPas = new ArrayList<>(liste_chemins);
        for (ArrayList<Quete> listeQuetes : new ArrayList<>(cheminsPlusPas)) {
            if (!estExhaustif(listeQuetes, scenario)) {
                cheminsPlusPas.remove(listeQuetes);
            }
            int nbPasAcutuel = calulerNombrePas(listeQuetes);
            if (nbPasAcutuel < nbPas) {
                nbPas = nbPasAcutuel;
            }
        }
        trierCheminsParDistancePire(cheminsPlusPas);
        while (cheminsPlusPas.size() > parN) {
            cheminsPlusPas.remove(cheminsPlusPas.size() - 1);
        }
        return cheminsPlusPas;
    }

    //==========================================================================
    //================= Methodes de Tri des Meilleurs / Pires ==================
    //==========================================================================

    /**
     * Trie une liste de chemins par distance de manière croissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParTempsMeilleur(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerTempsChemin(chemin1);
                int temps2 = calulerTempsChemin(chemin2);
                return Integer.compare(temps1, temps2);
            }
        });
    }

    /**
     * Trie une liste de chemins par nombre de quêtes de manière croissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParQuetesMeilleur(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerNombreQuetes(chemin1);
                int temps2 = calulerNombreQuetes(chemin2);
                return Integer.compare(temps1, temps2);
            }
        });
    }

    /**
     * Trie une liste de chemins par nombre de déplacements de manière croissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParDistanceMeilleur(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerNombrePas(chemin1);
                int temps2 = calulerNombrePas(chemin2);
                return Integer.compare(temps1, temps2);
            }
        });
    }

    /**
     * Trie une liste de chemins par nombre de déplacements de manière croissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParTempsPire(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerTempsChemin(chemin1);
                int temps2 = calulerTempsChemin(chemin2);
                return Integer.compare(temps2, temps1);
            }
        });
    }

    /**
     * Trie une liste de chemins par nombre de quêtes de manière décroissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParQuetesPire(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerNombreQuetes(chemin1);
                int temps2 = calulerNombreQuetes(chemin2);
                return Integer.compare(temps2, temps1);
            }
        });
    }

    /**
     * Trie une liste de chemins par nombre de pas de manière décroissante
     * @param parListe liste de chemins
     */
    public void trierCheminsParDistancePire(ArrayList<ArrayList<Quete>> parListe) {
        parListe.sort(new Comparator<ArrayList<Quete>>() {
            @Override
            public int compare(ArrayList<Quete> chemin1, ArrayList<Quete> chemin2) {
                int temps1 = calulerNombrePas(chemin1);
                int temps2 = calulerNombrePas(chemin2);
                return Integer.compare(temps2, temps1);
            }
        });
    }

    //==========================================================================
    //========================= Fonctions de calculs ===========================
    //==========================================================================

    /**
     * Calcul le nombre de quetes dans une liste de quetes
     * @param list liste de quetes
     * @return le temps mis pour réaliser les quetes dans la liste
     */
    public int calulerTempsChemin(ArrayList<Quete> list){
        Joueur joueur2 = new Joueur(scenario);
        for (Quete quete : list){
            joueur2.realiserQuete(quete);
        }
        return joueur2.getTemps();
    }

    /**
     * Calcul le nombre de pas effectué pour réaliser les quetes dans une liste de quetes
     * @param list liste de quetes
     * @return le nombre de pas effectué pour réaliser les quetes de la liste
     */
    public int calulerNombrePas(ArrayList<Quete> list){
        Joueur joueur2 = new Joueur(scenario);
        for (Quete quete : list){
            joueur2.realiserQuete(quete);
        }
        return joueur2.getNombrePas();
    }

    /**
     * Calcul le nombre de quetes dans une liste de quetes
     * @param list liste de quetes
     * @return le nombre de quetes dans la liste
     */
    public int calulerNombreQuetes(ArrayList<Quete> list){
        return list.size();
    }

    /**
     * Calcul l'expérience gagnée en réalisant les quetes dans une liste de quetes
     * @param list liste de quetes
     * @return l'expérience gagnée en réalisant les quetes de la liste
     */
    public int calulerXPChemin(ArrayList<Quete> list){
        Joueur joueur2 = new Joueur(scenario);
        for (Quete quete : list){
            joueur2.realiserQuete(quete);
        }
        return joueur2.getExperience();
    }
}
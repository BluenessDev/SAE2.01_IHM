package bluenessdev.controleur;

import bluenessdev.vue.VBoxRoot;
import bluenessdev.vue.VBox_Selection_Parcours;
import bluenessdev.vue.VBox_Tableau_N_Chemin;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import bluenessdev.modele.LectureFichierTexte;
import bluenessdev.modele.Quete;
import bluenessdev.modele.Scenario;
import bluenessdev.modele.Niveau1;
import bluenessdev.modele.Niveau2;

import java.io.File;
import java.util.ArrayList;

public class Controleur implements EventHandler {

    /**
     * Méthode qui permet de gérer les évènements
     * @param event Event, évènement
     */
    @Override
    public void handle(Event event) {

         VBox_Selection_Parcours vBoxSelectionParcours = VBoxRoot.getvBoxSelectionParcours();
        VBox_Tableau_N_Chemin vBoxTableauNChemin = VBoxRoot.getvBoxTableauNChemin();

        if (event.getSource() instanceof Button){
            String nomScenar = vBoxSelectionParcours.getNomScenario();
            String niveau = vBoxSelectionParcours.getNiveau();
            String qualiteChemin = vBoxSelectionParcours.getQualiteChemin();
            String qualiteEtudiees = vBoxSelectionParcours.getQualiteEtudiee();
            String typeChemins = vBoxSelectionParcours.getTypeChemin();
            int nbChemins = vBoxSelectionParcours.getNbChemins();

            Scenario scenario = LectureFichierTexte.lecture(new File("scenario" + File.separator + (nomScenar.toLowerCase()+".txt")));

            ArrayList<ArrayList<Quete>> listChemin = new ArrayList<>();

            if (niveau == "Glouton"){
                ArrayList<Quete> chemin;

                Niveau1 niveau1 = new Niveau1(scenario);

                if (typeChemins == "Efficace"){
                    chemin = niveau1.cheminEfficace();
                }
                else {
                    chemin = niveau1.cheminExhaustif();
                }

                listChemin.add(chemin);
            }

            else {

                Niveau2 niveau2 = new Niveau2(scenario);

                if (typeChemins == "Efficace"){

                    if (qualiteEtudiees == "Temps"){

                        if(qualiteChemin == "Meilleur"){
                            listChemin = niveau2.tempsPlusCourtEfficaceMeilleur(nbChemins);
                        }

                        else {
                            listChemin = niveau2.tempsPlusCourtEfficacePire(nbChemins);
                        }

                    } else if (qualiteEtudiees == "Nombres de pas") {

                        if(qualiteChemin == "Meilleur"){
                            listChemin = niveau2.moinsDeDeplacementsEfficaceMeilleur(nbChemins);
                        }

                        else {
                            listChemin = niveau2.moinsDeDeplacementsEfficacePire(nbChemins);
                        }

                    }
                    else {
                        if(qualiteChemin == "Meilleur"){
                            listChemin = niveau2.moinsDeQuetesEfficaceMeilleur(nbChemins);
                        }

                        else {
                            listChemin = niveau2.moinsDeQuetesEfficacePire(nbChemins);
                        }
                    }

                }
                else {
                    if (qualiteEtudiees == "Temps"){

                        if(qualiteChemin == "Meilleur"){
                            listChemin = niveau2.tempsPlusCourtExhaustifMeilleur(nbChemins);
                        }

                        else {
                            listChemin = niveau2.tempsPlusCourtExhaustifMeilleur(nbChemins);
                        }

                    } else if (qualiteEtudiees == "Nombres de pas") {

                        if(qualiteChemin == "Meilleur"){
                            listChemin = niveau2.moinsDeDeplacementsExhaustifMeilleur(nbChemins);
                        }

                        else {
                            listChemin = niveau2.moinsDeDeplacementsExhaustifPire(nbChemins);
                        }

                    }
                }
            }
            vBoxTableauNChemin.update(nomScenar,listChemin,scenario);

        }
    }
}

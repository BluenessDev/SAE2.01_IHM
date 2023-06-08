package bluenessdev.vue;

import bluenessdev.controleur.Controleur;
import javafx.scene.layout.VBox;

public class VBoxRoot extends VBox {

    private static VBox_Selection_Parcours vBoxSelectionParcours;
    private static VBox_Tableau_N_Chemin vBoxTableauNChemin;
    private static Controleur controleur;

    /**
     * Constructeur de la classe VBoxRoot
     */
    public VBoxRoot(){
        super();
        controleur = new Controleur();
        vBoxTableauNChemin = new VBox_Tableau_N_Chemin();
        vBoxSelectionParcours = new VBox_Selection_Parcours();


        this.getChildren().addAll(vBoxSelectionParcours,vBoxTableauNChemin);
    }

    /**
     * Méthode qui permet de récupérer la VBox_Selection_Parcours
     * @return VBox_Selection_Parcours, la VBox_Selection_Parcours
     */
    public static VBox_Selection_Parcours getvBoxSelectionParcours() {
        return vBoxSelectionParcours;
    }

    /**
     * Méthode qui permet de récupérer le Controleur
     * @return Controleur, le Controleur
     */
    public static Controleur getControleur(){
        return controleur;
    }

    /**
     * Méthode qui permet de récupérer la VBox_Tableau_N_Chemin
     * @return VBox_Tableau_N_Chemin, la VBox_Tableau_N_Chemin
     */
    public static VBox_Tableau_N_Chemin getvBoxTableauNChemin(){
        return vBoxTableauNChemin;
    }
}

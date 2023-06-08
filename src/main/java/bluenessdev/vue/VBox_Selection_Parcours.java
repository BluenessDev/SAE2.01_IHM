package bluenessdev.vue;

import bluenessdev.controleur.Controleur;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import bluenessdev.modele.ConstanteChoix;

public class VBox_Selection_Parcours extends VBox implements ConstanteChoix {


    private static ComboBox<String> comboBox_nomScenario = new ComboBox<>();
    private static ComboBox<String> comboBox_niveaux = new ComboBox<>();
    private static ComboBox<String> comboBox_typeChemin = new ComboBox<>();
    private static Spinner<Integer> spinnerNbChemins = new Spinner<>(1,100,1);
    private static ComboBox<String> comboBox_qualiteChemin = new ComboBox<>();
    private static ComboBox<String> comboBox_qualiteEtudiees = new ComboBox<>();

    /**
     * Constructeur de la classe VBox_Selection_Parcours
     */
    public VBox_Selection_Parcours(){
        super(20);

        Controleur controleur = VBoxRoot.getControleur();


        ToolBar toolBar = new ToolBar();

        comboBox_nomScenario.getItems().addAll(Scenario);
        comboBox_nomScenario.setValue(Scenario[0]);


        comboBox_niveaux.getItems().addAll(Niveaux);
        comboBox_niveaux.setValue(Niveaux[0]);




        comboBox_typeChemin.getItems().addAll(TypeChemins);
        comboBox_typeChemin.setValue(TypeChemins[0]);


        comboBox_qualiteChemin.getItems().addAll(QualiteChemins);
        comboBox_qualiteChemin.setValue(QualiteChemins[0]);


        comboBox_qualiteEtudiees.getItems().addAll(QualiteEtudiees);
        comboBox_qualiteEtudiees.setValue(QualiteEtudiees[0]);

        toolBar.getItems().addAll(comboBox_nomScenario,comboBox_niveaux,comboBox_typeChemin,comboBox_qualiteChemin,comboBox_qualiteEtudiees);

        Button boutonValide = new Button("Valider");

        boutonValide.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {

            }
        }
        );
        boutonValide.addEventHandler(ActionEvent.ACTION,controleur);

        Label label = new Label("Nombre de solution(s) :");
        this.getChildren().addAll(toolBar,label,spinnerNbChemins,boutonValide);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Méthode qui retourne le nom du scénario choisi
     * @return le nom du scénario choisi (Scenario_...)
     */
    public String getNomScenario() {
        return comboBox_nomScenario.getValue();
    }

    /**
     * Méthode qui retourne le niveau choisi
     * @return le niveau choisi (En profondeur/Glouton)
     */
    public String getNiveau() {
        return comboBox_niveaux.getValue();
    }

    /**
     * Méthode qui retourne le nombre de chemins à afficher
     * @return le nombre de chemins à afficher
     */
    public int getNbChemins() {
        return spinnerNbChemins.getValue();
    }

    /**
     * Méthode qui retourne le type de chemin choisi
     * @return le type de chemin choisi
     */
    public String getTypeChemin() {
        return comboBox_typeChemin.getValue();
    }

    /**
     * Méthode qui retourne la qualité du chemin choisi
     * @return la qualité du chemin choisi (Meilleur/Pire)
     */
    public String getQualiteChemin() {
        return comboBox_qualiteChemin.getValue();
    }

    /**
     * Méthode qui retourne la qualité étudiée
     * @return la qualité étudiée (Temps/Distance/Quetes)
     */
    public String getQualiteEtudiee() {
        return comboBox_qualiteEtudiees.getValue();
    }
}

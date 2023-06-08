package bluenessdev.vue;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import bluenessdev.modele.Chemin;
import bluenessdev.modele.Quete;
import bluenessdev.modele.Scenario;

import java.util.ArrayList;

public class VBox_Tableau_N_Chemin extends VBox {


        Label labelScenario;
        TableView<Chemin> tableDesChemins;
        Label labelNbChemin;
        Label label = new Label();

    /**
     * Constructeur de la classe VBox_Tableau_N_Chemin
     */
        public VBox_Tableau_N_Chemin(){
            super();
            labelScenario = new Label();
            labelNbChemin = new Label();
            tableDesChemins = new TableView();

            TableColumn<Chemin, ArrayList<Integer>> cheminColumn = new TableColumn<>("Liste des quêtes");
            cheminColumn.setCellValueFactory(new PropertyValueFactory<>("NumQuete"));

            TableColumn<Chemin, Integer> distanceColumn = new TableColumn<>("Nombre de pas");
            distanceColumn.setCellValueFactory(new PropertyValueFactory<>("NbPas"));

            TableColumn<Chemin, Integer> tempsColumn = new TableColumn<>("Temps");
            tempsColumn.setCellValueFactory(new PropertyValueFactory<>("Temps"));

            TableColumn<Chemin, Integer> nbQuetesColumn = new TableColumn<>("Nombre de quêtes");
            nbQuetesColumn.setCellValueFactory(new PropertyValueFactory<>("NbQuete"));

            TableColumn<Chemin, Integer> XpColumn = new TableColumn<>("Expérience");
            XpColumn.setCellValueFactory(new PropertyValueFactory<>("Xp"));

            tableDesChemins.getColumns().addAll(cheminColumn,distanceColumn,tempsColumn,nbQuetesColumn,XpColumn);
            tableDesChemins.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            HBox hBox1 = new HBox(20);
            HBox hBox2 = new HBox(20);
            HBox hBox = new HBox(40);
            hBox2.getChildren().addAll(label,labelNbChemin);
            hBox2.setAlignment(Pos.CENTER_RIGHT);
            hBox1.getChildren().addAll(labelScenario);
            hBox1.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(hBox1,hBox2);
            hBox.setSpacing(500);
            this.getChildren().addAll(hBox,tableDesChemins);
        }

    /**
     * Met à jour le tableau des chemins
     * @param scenario nom du scenario
     * @param listchemins liste des chemins
     * @param scenar scenario
     */
        public void update(String scenario, ArrayList<ArrayList<Quete>> listchemins, Scenario scenar){

            tableDesChemins.getItems().clear();

            labelScenario.setText(scenario);
            label.setText("nombre de solutions: ");

            labelNbChemin.setText(String.valueOf(listchemins.size()));

            for (ArrayList<Quete> list : listchemins){

                Chemin chemin = new Chemin(list,scenar);

                tableDesChemins.getItems().add(chemin);

            }
        }
}

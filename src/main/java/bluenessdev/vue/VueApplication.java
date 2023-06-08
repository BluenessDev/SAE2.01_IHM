package bluenessdev.vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class VueApplication extends Application {

    /**
     * Méthode qui permet d'afficher la scène
     * @param stage Stage, la scène
     */
    @Override
    public void start(Stage stage){
        VBox root = new VBoxRoot();
        Scene scene = new Scene(root,770,450);
        File [] fichierCss = new File("css").listFiles();
        for (File fichier : fichierCss){
            scene.getStylesheets().add(fichier.toURI().toString());
        }
        Stage stage1 = new Stage();
        stage1.setResizable(false);
        stage1.setScene(scene);
        stage1.setTitle("SAE2.01 - Cyril(BluenessDev) & Pierre(octogenarian78)");
        stage1.show();
    }

    /**
     * Méthode qui permet de lancer l'application
     * @param args Arguments, les arguments java
     */
    public static void main(String[] args) {
       Application.launch(args);
    }
}

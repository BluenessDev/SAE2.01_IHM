module bluenessdev.sae201_ihm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens bluenessdev.sae201_ihm to javafx.fxml;
    exports bluenessdev.modele;
    exports bluenessdev.vue;
    exports bluenessdev.controleur;
}
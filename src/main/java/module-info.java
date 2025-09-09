module com.proyecto.reciclatech {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Librerías adicionales
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires jdk.compiler;


    // Abrir paquetes para FXML
    opens com.proyecto.reciclatech.app to javafx.fxml;
    opens com.proyecto.reciclatech.db to javafx.fxml;

    // Exportar paquetes
    exports com.proyecto.reciclatech.db;
}

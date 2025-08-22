package com.example.Proiect_in_Java;

import com.example.repository.SQLPiesaRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        SQLPiesaRepository repository = new SQLPiesaRepository("piese.bin");
//        try {
//            repository.add(new Piesa(100, "ColdPlay", "muzica","happy","03:30"));
//            repository.add(new Piesa(101, "DuaLipa", "melodie", "happy","04:00"));
//            repository.add(new Piesa(102, "Metalica", "chitaracv","rock","05:30"));
//            repository.add(new Piesa(103, "Eminem", "abracadabra", "rap","08:00"));
//        } catch (RepositoryException e) {
//            throw new RuntimeException(e);
//        }


        HelloController controller = fxmlLoader.getController();
        controller.initialize(repository);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.example.lab4map;

import domain.*;
import exceptions.RepositoryException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.MemoryRepository;
import service.ComandaService;
import service.TortService;
import ui.Console;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws RepositoryException {
        EntityConverter<Tort> tortConverter = new TortConverter();
        EntityConverter<Comanda> comandaConverter = new ComandaConverter();
        MemoryRepository<Tort> tortRepository = new MemoryRepository<>();
        MemoryRepository<Comanda> comandaRepository = new MemoryRepository<>();
        TortService tortService = new TortService(tortRepository, comandaRepository);
        ComandaService comandaService = new ComandaService(comandaRepository);
        Console console = new Console(comandaService, tortService);
        console.generate100Comenzi();
        console.generate100Torturi();
        console.startConsole();
        launch();
    }
}
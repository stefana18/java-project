package com.example.lab4map;

import domain.Comanda;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import repository.MemoryRepository;
import service.ComandaService;
import service.TortService;

import java.util.ArrayList;
import java.util.Collection;

public class HelloController {
    @FXML
    public ListView lstMessage;

    @FXML
    public Button afiseazaComenzi;
    public Button addTort;

    public Button deleteTort;
    public TextField id_tort;

    public TextField id_tort_sters;

    public TextField tip_tort;

    public TextField id_tort_modif;

    public TextField tip_modif;

    public Button updtTort;
    @FXML
    ObservableList<String> list = FXCollections.observableList(new ArrayList<>());
    @FXML
    public Button afiseazaTorturi;

    MemoryRepository<Tort> tortRepository = new MemoryRepository<Tort>();
    MemoryRepository<Comanda> comandaRepository = new MemoryRepository<Comanda>();
    TortService tortService = new TortService(tortRepository, comandaRepository);
    ComandaService comandaService = new ComandaService(comandaRepository);

    @FXML
    public void adaugaTort(ActionEvent actionEvent) throws RepositoryException {
        String tip = null;
        int id = 0;
        try{
            id = Integer.parseInt(id_tort.getText());
            tip = tip_tort.getText();
            Tort t = new Tort(id, tip);
            tortService.add(id,tip);
        }catch(NumberFormatException e)
        {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Enter a valid, positive integer");
            hello.show();
            return;
        }catch(DuplicateObjectException de)
        {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Enter a valid, positive integer");
            hello.show();
            return;
        }

    }

    @FXML
    public void stergeTort(ActionEvent actionEvent) throws RepositoryException{
        int id = 0;
        try{
            id = Integer.parseInt(id_tort_sters.getText());
        }
        catch(NumberFormatException e)
        {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Enter a valid, positive integer");
            hello.show();
            return;
        }
        tortService.remove(id);
    }

    @FXML
    public void updateTort(ActionEvent actionEvent) throws RepositoryException{
        int id =0;
        String tip_nou = null;
        try{
            id = Integer.parseInt(id_tort_modif.getText());
            tip_nou = tip_modif.getText();
            Tort t = new Tort(id, tip_nou);
            tortService.update(id, id, ".", tip_modif.getText());
        }catch(RepositoryException e)
        {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Enter a valid, positive integer");
            hello.show();
            return;
        }catch (NumberFormatException ne)
        {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Enter a valid, positive integer");
            hello.show();
            return;
        }
    }
    @FXML
    public void getAllTorturi(ActionEvent actionEvent) throws RepositoryException {
        list.clear();
        Collection<Tort> lista =tortService.getAll();
        for(Tort t:lista) {
            list.add(t.getID()+" "+t.getTip_tort());
        }
        lstMessage.setItems(list);
    }
    @FXML
    public void getAllComenzi(ActionEvent actionEvent) throws RepositoryException{
        list.clear();
        Collection<Comanda> lista = comandaService.getAll();
        for (Comanda c : lista){
            String s="";
            ArrayList<Tort> torturi = c.getTorturi()
                    ;
            for(Tort t:torturi){
                s+=t.getID()+","+t.getTip_tort()+";";
            }
            list.add(Integer.parseInt("id comanda:"+c.getID()+"   lista torturi: "+s), "data: " + c.getData());
        }
        lstMessage.setItems(list);
    }
}
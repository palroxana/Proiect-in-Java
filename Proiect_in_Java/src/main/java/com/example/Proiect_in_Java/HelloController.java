package com.example.Proiect_in_Java;


import com.example.domain.Piesa;
import com.example.repository.RepositoryException;
import com.example.repository.SQLPiesaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class HelloController {
    public ListView<Piesa> listViewPiese;
    public TextField textFieldPieseId;
    public TextField textFieldPieseFormatie;
    public TextField textFieldPieseTitlu;
    public TextField textFieldPieseGen;
    public TextField textFieldPieseDurata;
    public TextField textFieldSearch;

    public Button buttonAdd;

    private SQLPiesaRepository repository;

    ObservableList<Piesa> pieseList;

    public void initialize(SQLPiesaRepository repository) {
        this.repository = repository;
        pieseList = FXCollections.observableList(new ArrayList<Piesa>(repository.getAll()));
        listViewPiese.setItems(pieseList);
    }

    @FXML
    protected void onHelloButtonClick() {

    }

    public void onAddButtonClicked(ActionEvent actionEvent) {
        try {
            int piesaId = Integer.parseInt(textFieldPieseId.getText());
            String piesaFormatie = textFieldPieseFormatie.getText();
            String piesaTitlu = textFieldPieseTitlu.getText();
            String piesaGen = textFieldPieseGen.getText();
            String piesaDurata = textFieldPieseDurata.getText();

            var piesa = new Piesa(piesaId,piesaFormatie,piesaTitlu,piesaGen,piesaDurata);
            // daca la adaugarea in repository apare o exceptie, artistul nu trebuie
            // sa apara in interfata grafica
            repository.add(piesa);
            pieseList.add(piesa);
        } catch (NumberFormatException e) {
            Alert hopa = new Alert(Alert.AlertType.ERROR, "Id-ul piesei trebuie sa fie numar intreg pozitiv!", ButtonType.OK);
            hopa.show();
        } catch (RepositoryException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        } finally {
            textFieldPieseId.clear();
            textFieldPieseFormatie.clear();
            textFieldPieseTitlu.clear();
            textFieldPieseGen.clear();
            textFieldPieseDurata.clear();
        }
    }

    public void onUpdateButtonClicked(ActionEvent actionEvent) {
        try {
            int piesaId = Integer.parseInt(textFieldPieseId.getText());
            String piesaFormatie = textFieldPieseFormatie.getText();
            String piesaTitlu = textFieldPieseTitlu.getText();
            String piesaGen = textFieldPieseGen.getText();
            String piesaDurata = textFieldPieseDurata.getText();
            var piesa = new Piesa(piesaId,piesaFormatie,piesaTitlu,piesaGen,piesaDurata);

            // sterg artistul existent cu id-ul dat
            repository.remove(piesaId);
            pieseList.remove(repository.findById(piesaId));

            repository.add(piesa);
            pieseList.add(piesa);
        } catch (RepositoryException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
    }

    public void onReset(ActionEvent actionEvent) {initialize(repository);
    }
    public void onFiltru(ActionEvent actionevent) throws RepositoryException {
            String query = textFieldSearch.getText();
            pieseList.setAll(repository.filtrupiese(query));
        }
}
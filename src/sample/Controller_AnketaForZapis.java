package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller_AnketaForZapis implements Initializable {
    public TextField fio_pols;
    public ChoiceBox choice_time;
    public ChoiceBox choice_specialist;
    public Label l_name_note;
    public Button btn_podtverzd_zapis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        l_name_note.setText(Controller_Main.proff);
        try{
            loadFio();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            specForRec();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
//        try {
//            timeForRec();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        choice_specialist.setOnAction(event -> {
            try {
                timeForRec();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void btn_podtverzd_zapis_Click(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!fio_pols.getText().isEmpty() && choice_specialist.getValue() != null && choice_time.getValue() != null) {
            makeRecord();
            Stage stage = (Stage) btn_podtverzd_zapis.getScene().getWindow();
            stage.close();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label("Запись прошла успешно! " + fio_pols.getText()+"!, вы были записаны на " + choice_time.getValue() + " к специалисту "
            + choice_specialist.getValue());
            VBox layout = new VBox(label);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 700, 100);
            stage2.setTitle("Запись прошла успешно");
            stage2.setScene(scene);
            stage2.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля/заполнены не корректно");
            alert.showAndWait();
        }
    }

    public void timeForRec() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = null;
        if (choice_specialist.getValue() != null){
            res = dbHandler.getRecordings((String) choice_specialist.getValue());
            System.out.println(choice_specialist.getValue());
        }
        else res = dbHandler.getRecordings();
        ArrayList dates = new ArrayList();
        ArrayList times = new ArrayList();
        ArrayList status = new ArrayList();
        int counter = 0;
        while(res.next()){
            dates.add(res.getDate(1));
            times.add(res.getTime(2));
            status.add(res.getInt(3));
            counter++;
        }
        String[] asd = new String[counter];
        Format fd = new SimpleDateFormat("yyyy-MM-dd");
        Format ft = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < counter; i++) {
            asd[i] = fd.format(dates.get(i)) + " " + ft.format(times.get(i));
        }
        choice_time.setItems(FXCollections.observableArrayList(asd));
    }

    public void specForRec() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.getSpecialists(Controller_Main.proff);
        ArrayList surname = new ArrayList();
        ArrayList name = new ArrayList();
        ArrayList patr = new ArrayList();
        int counter = 0;
        while(res.next()){
            name.add(res.getString(2));
            surname.add(res.getString(3));
            patr.add(res.getString(4));
            counter++;
        }
        String[] asd = new String[counter];
        for (int i = 0; i < counter; i++) {
            asd[i] = surname.get(i) + " " + name.get(i) + " " + patr.get((i));
        }
        choice_specialist.setItems(FXCollections.observableArrayList(asd));
    }

    public void loadFio() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.dataInPA();
        ArrayList surname = new ArrayList();
        ArrayList name = new ArrayList();
        ArrayList patr = new ArrayList();
        int counter = 0;
        while(res.next()){
            name.add(res.getString(3));
            surname.add(res.getString(2));
            patr.add(res.getString(4));
            counter++;
        }
        String[] asd = new String[counter];
        for (int i = 0; i < counter; i++) {
            asd[i] = surname.get(i) + " " + name.get(i) + " " + patr.get((i));
        }
        fio_pols.setText(asd[0]);
    }

    public void makeRecord() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.insertRecord((String) choice_specialist.getValue(), DBconst.CURRENT_USER, (String) choice_time.getValue());
    }
}

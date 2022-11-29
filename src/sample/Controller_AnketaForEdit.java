package sample;

import javafx.collections.FXCollections;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller_AnketaForEdit implements Initializable {
    public Button btn_edit_zapis;
    public TextField fio_pols;
    public ChoiceBox choice_time;
    public ChoiceBox choice_specialist;
    public Label l_name_note;
    public Button btn_otmena_zapis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        l_name_note.setText(Controller_PersonalAccount.proff);
        try {
            loadFio();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            specForRec();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            defaultSpecRec();
            timeForRec();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        choice_specialist.setOnAction(event -> {
            try {
                timeForRec();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void btn_edit_zapis_Click(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!fio_pols.getText().isEmpty()) {
            changeRecord();
            makeRecord();
            Stage stage = (Stage) btn_edit_zapis.getScene().getWindow();
            stage.close();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label("Запись была успешно изменена!");
            VBox layout = new VBox(label);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 400, 100);
            stage2.setTitle("Запись изменена успешно");
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

    public void btn_otmena_zapis_Click(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_otmena_zapis.getScene().getWindow();
        stage.close();
    }


    public void btn_delete_zapis_Click(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Вы действительно хотите удалить запись?");
        ButtonType btn_yes = new ButtonType("Да");
        ButtonType btn_no = new ButtonType("Нет");
        // Remove default ButtonTypes
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(btn_yes, btn_no);
        // option != null.
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
//            this.label.setText("No selection!");
        } else if (option.get() == btn_yes) {
            System.out.println("File deleted!");
            deleteRecord();
            //           this.label.setText("File deleted!");
        } else if (option.get() ==btn_no) {
            System.out.println("Cancelled!");
            //           this.label.setText("Cancelled!");
        } else {
            //         this.label.setText("-");
        }
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

    public void timeForRec() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = null;
        if (choice_specialist.getValue() != null){
            res = dbHandler.getRecordings((String) choice_specialist.getValue());
//            System.out.println(choice_specialist.getValue());
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
//        System.out.println(counter);
        String[] asd = new String[counter + 1];
        Format fd = new SimpleDateFormat("yyyy-MM-dd");
        Format ft = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < counter; i++) {
            asd[i] = fd.format(dates.get(i)) + " " + ft.format(times.get(i));
        }
        asd[counter] = Controller_PersonalAccount.date+ " " + Controller_PersonalAccount.time;
        choice_time.setItems(FXCollections.observableArrayList(asd));
    }

    public void specForRec() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.getSpecialists(Controller_PersonalAccount.proff);
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

    public void defaultSpecRec() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.recInEdit();
        ArrayList data = new ArrayList();
        ArrayList time = new ArrayList();
        ArrayList surname = new ArrayList();
        ArrayList name = new ArrayList();
        ArrayList patr = new ArrayList();
        while(res.next()){
            data.add(res.getString(1));
            time.add(res.getString(2));
            surname.add(res.getString(3));
            name.add(res.getString(4));
            patr.add(res.getString(5));
        }
        String dt = data.get(0) + " " + time.get(0);
        String fio = surname.get(0) + " " + name.get(0) + " " + patr.get((0));
        choice_specialist.setValue(fio);
        choice_time.setValue(dt);
    }

    public void makeRecord() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.insertRecord((String) choice_specialist.getValue(), DBconst.CURRENT_USER, (String) choice_time.getValue());
    }

    public void deleteRecord() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.removeRecord((String) choice_specialist.getValue(), (String) choice_time.getValue());
    }

    public void changeRecord() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.recInEdit();
        ArrayList data = new ArrayList();
        ArrayList time = new ArrayList();
        ArrayList surname = new ArrayList();
        ArrayList name = new ArrayList();
        ArrayList patr = new ArrayList();
        while(res.next()){
            data.add(res.getString(1));
            time.add(res.getString(2));
            surname.add(res.getString(3));
            name.add(res.getString(4));
            patr.add(res.getString(5));
        }
        String dt = data.get(0) + " " + time.get(0);
        String fio = surname.get(0) + " " + name.get(0) + " " + patr.get((0));
//        System.out.println(dt);
        dbHandler.removeRecord(fio, dt);
    }
}

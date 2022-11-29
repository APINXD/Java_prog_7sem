package sample;

import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller_Settings implements Initializable {
    public TextField l_fam;
    public TextField l_name;
    public TextField l_otch;
    public TextField l_mail;
    public TextField l_reg_password;
    public Button btn_delete;
    public Button btn_save;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            databp();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btn_delete_Click(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление профиля");
        alert.setHeaderText("Вы действительно хотите удалить профиль?");
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
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.deleteUser();
            System.out.println("File deleted!");
            Stage stage = (Stage) btn_delete.getScene().getWindow();
            Platform.runLater( () -> {
                try
                {
                    stage.close();
                    new Main().start( new Stage() );
                }
                catch (Exception e1)
                {
// TODO:
                    e1.printStackTrace();
                }
            } );
 //           this.label.setText("File deleted!");
        } else if (option.get() == btn_no) {
            System.out.println("Cancelled!");
 //           this.label.setText("Cancelled!");
        } else {
   //         this.label.setText("-");
        }
    }


    public void btn_save_Click(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!l_name.getText().isEmpty() && !l_otch.getText().isEmpty() && !l_fam.getText().isEmpty() && !l_mail.getText().isEmpty()&& !l_reg_password.getText().isEmpty()) {
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.updateUser(l_fam.getText(), l_name.getText(), l_otch.getText(), l_mail.getText(), l_reg_password.getText());
            Stage stage = (Stage) btn_save.getScene().getWindow();
            stage.close();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label("Новые личные данные были успешно сохранены!");
            VBox layout = new VBox(label);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 400, 100);
            stage2.setTitle("Изменение данных прошло успешно");
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

    public void databp() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.dataInPA();
        ArrayList surname = new ArrayList();
        ArrayList name = new ArrayList();
        ArrayList patr = new ArrayList();
        ArrayList mail = new ArrayList();
        ArrayList pass = new ArrayList();
        int counter = 0;
        while(res.next()){
            surname.add(res.getString(2));
            name.add(res.getString(3));
            patr.add(res.getString(4));
            mail.add(res.getString(5));
            pass.add(res.getString(6));
            counter++;
        }
        l_fam.setText((String) surname.get(0));
        l_name.setText((String) name.get(0));
        l_otch.setText((String) patr.get(0));
        l_mail.setText((String) mail.get(0));
        l_reg_password.setText((String) pass.get(0));
    }
}

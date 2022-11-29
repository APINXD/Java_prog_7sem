package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_Registracia implements Initializable {

    public TextField l_name;
    public TextField l_otch;
    public TextField l_mail;
    public TextField l_reg_password;
    public TextField l_fam;
    public Button zareg_btn;

    private void signUpNewUser() {
        String firstname = l_fam.getText();
        String name = l_name.getText();
        String middlename = l_otch.getText();
        String email = l_mail.getText();
        String password = l_reg_password.getText();
        User user = new User(firstname, name, middlename, email, password);
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.signUpUser(user);
    }


    public void zareg_Click(ActionEvent actionEvent) {
        if (!l_name.getText().isEmpty() && !l_otch.getText().isEmpty() && !l_fam.getText().isEmpty() && !l_mail.getText().isEmpty() && !l_reg_password.getText().isEmpty()) {
            signUpNewUser();
            Stage stage = (Stage) zareg_btn.getScene().getWindow();
            stage.close();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.APPLICATION_MODAL);

            Label label = new Label("Поздравляем, " + l_name.getText() + "! Вы были успешно зарегистрированы!");

            VBox layout = new VBox(label);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout, 400, 100);

            stage2.setTitle("Регистрация прошла успешно");
            stage2.setScene(scene);
            stage2.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля/заполнены не корректно");

            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

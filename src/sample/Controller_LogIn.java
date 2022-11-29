package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller_LogIn implements Initializable {
    public TextField l_password;
    public TextField l_label;
    public Button btn_login;
    @FXML
    private Hyperlink reg_hyperlink;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }


    public void ne_zaregistr_Click(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) reg_hyperlink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("registracia.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        newWindow.setTitle("Регистрация");
        newWindow.setScene(new Scene(fxmlLoader.load(), 446, 548));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }


    public void log_in_Click(ActionEvent actionEvent)  throws IOException {
        if (loginUser(l_label.getText(),l_password.getText()) > 0) {
            Stage stage = (Stage) btn_login.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("main.fxml"));
            Stage newWindow = new Stage();
            stage.close();
            newWindow.setResizable(false);
            newWindow.getIcons().add(new Image("./media/notes.png"));
            newWindow.setTitle("Главная");
            newWindow.setScene(new Scene(fxmlLoader.load(), 1000, 600));
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.show();
//            stage.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Логин или пароль введены не верно");
            alert.showAndWait();
        }
    }

    private int loginUser(String loginText,String loginPassword){
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setEmail(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);
        int counter = 0;
        ArrayList id = new ArrayList();
        try{
            while(result.next()){
                counter++;
                id.add(result.getString(counter));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(counter >= 1)
            DBconst.CURRENT_USER = (String) id.get(0);
        return counter;
    }
}

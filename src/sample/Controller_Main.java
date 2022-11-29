package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller_Main implements Initializable {
    public static String name_selected_usluga = "";
    public TextField t_search;
    public Button btn_search;
    public ListView<String> search_ListView;
    public Hyperlink hyper_lichnii_kabinet;
    public String[] uslugi;
    public static String proff = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(DBconst.CURRENT_USER);
        try {
            uslugi = listProfessions();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        search_ListView.setItems(FXCollections.observableArrayList(uslugi));
        t_search.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    btn_search_Click(new ActionEvent());
                }
            }
        });
//        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public void searchItem_handleMouseClick(MouseEvent mouseEvent) throws IOException {
        name_selected_usluga = search_ListView.getSelectionModel().getSelectedItem();
//        System.out.println("clicked on " + search_ListView.getSelectionModel().getSelectedItem());
        proff = search_ListView.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) search_ListView.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("description.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        newWindow.setTitle("Запись " + search_ListView.getSelectionModel().getSelectedItem());
        newWindow.setScene(new Scene(fxmlLoader.load(), 600, 400));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }

    public void btn_search_Click(ActionEvent actionEvent) {
        if (!t_search.getText().isEmpty()) {
            List<String> search_list = new ArrayList<String>();
            String search = t_search.getText().toLowerCase();
            for (int i = 0; i < uslugi.length; i++) {
                if (uslugi[i].toLowerCase().contains(search)) {
                    search_list.add(uslugi[i]);
                }
            }
            search_ListView.setItems(FXCollections.observableArrayList(search_list));
        } else search_ListView.setItems(FXCollections.observableArrayList(uslugi));
    }

    public void hyper_lichnii_kabinet_Click(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) hyper_lichnii_kabinet.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("personal_account.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        newWindow.setTitle("Личный кабинет");
        newWindow.setScene(new Scene(fxmlLoader.load(), 1000, 600));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }

    private String[] listProfessions() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.getProfessions();
        int counter = 0;
        ArrayList prof = new ArrayList();
        while (res.next()) {
            prof.add(res.getString(2));
            counter++;
        }
        String[] result = new String[counter];
        for (int i = 0; i < counter; i++) {
            result[i] = (String) prof.get(i);
        }
        return result;
    }
}

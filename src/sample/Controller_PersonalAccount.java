package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
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
import java.util.ResourceBundle;

public class Controller_PersonalAccount implements Initializable {
    public static String name_selected_usluga = "";
    public Hyperlink hyperlink_main;
    public Hyperlink hyperlink_settings;
    public Label l_fio;
    public ListView<String> my_notes_ListView;
    public String [] notes = new String[]{};
    public static String proff;
    public static String date;
    public static String time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadFio();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        try {
            notes = getMyRecs();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        my_notes_ListView.setItems(FXCollections.observableArrayList(notes));
    }

    public void NoteItem_handleMouseClick(MouseEvent mouseEvent) throws IOException {
        name_selected_usluga = my_notes_ListView.getSelectionModel().getSelectedItem();
//        System.out.println("clicked on " + my_notes_ListView.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) my_notes_ListView.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("anketa_for_edit.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        newWindow.setTitle("Запись " + my_notes_ListView.getSelectionModel().getSelectedItem());
        date = my_notes_ListView.getSelectionModel().getSelectedItem().substring(0,10);
        time = my_notes_ListView.getSelectionModel().getSelectedItem().substring(14,22);
        proff = my_notes_ListView.getSelectionModel().getSelectedItem().substring(26);
        newWindow.setScene(new Scene(fxmlLoader.load(), 600, 400));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }

    public void hyperlink_settings_Click(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) hyperlink_settings.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("settings.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        newWindow.setTitle("Настройки личных данных");
        newWindow.setScene(new Scene(fxmlLoader.load(), 446, 548));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }

    public void hyperlink_main_Click(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) l_fio.getScene().getWindow();
        stage.close();
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
        l_fio.setText(asd[0]);
    }

    public String[] getMyRecs() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.myRec();
        ArrayList dates = new ArrayList();
        ArrayList times = new ArrayList();
        ArrayList proffname = new ArrayList();
        int counter = 0;
        while(res.next()){
            dates.add(res.getDate(1));
            times.add(res.getTime(2));
            proffname.add(res.getString(3));
            counter++;
        }
        String[] result = new String[counter];
        Format fd = new SimpleDateFormat("yyyy-MM-dd");
        Format ft = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < counter; i++) {
            result[i] = fd.format(dates.get(i)) + "    " + ft.format(times.get(i)) + "    " + proffname.get(i);
        }
        return result;
    }
}

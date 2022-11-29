package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller_Description  implements Initializable {
    public Text text_discription_note;
    public Button btn_zapis;
    public Label l_name_note;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println(Controller_Main.proff + " HAHA ITS WORKING");
        l_name_note.setText(Controller_Main.proff);
        try {
            writeDescription();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Stage getStage() {
        return null;
    }

    public void btn_zapis_Click(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btn_zapis.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("anketa_for_zapis.fxml"));
        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.getIcons().add(new Image("./media/notes.png"));
        stage.close();
        newWindow.setTitle(stage.getTitle());
        newWindow.setScene(new Scene(fxmlLoader.load(), 600, 400));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.show();
    }

    public void writeDescription() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet res = dbHandler.getDescription(Controller_Main.proff);
        ArrayList desc = new ArrayList();
        while(res.next()){
            desc.add(res.getString(3));
        }
        text_discription_note.setText((String) desc.get(0));
    }
}

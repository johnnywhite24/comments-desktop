package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        Parameters parameters = getParameters();
        List<String> raws = parameters.getRaw();
        Controller controller = loader.getController();
        controller.setParameters(raws);

        primaryStage.setTitle("设置备注");
        primaryStage.setScene(new Scene(root, 400, 160));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

package apps.helpApp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class HelpApp extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        // 获取新窗口的fxml文件
        URL location = getClass().getResource("/apps/helpApp/HelpApp.fxml");
        if(location==null)
        {
            System.out.println("null");
            return ;
        }
        //实例化fxml加载器
        FXMLLoader fxmlLoader = new FXMLLoader();
        //设置加载位置
        fxmlLoader.setLocation(location);
        //加载
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("帮助");

        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);

        Scene scene = primaryStage.getScene();
        HelpAppController helpAppController = fxmlLoader.getController();

        //设置窗口的图标.
        location=getClass().getResource("/imgs/help.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);

//        primaryStage.initStyle(StageStyle.TRANSPARENT);

        helpAppController.init(primaryStage);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();



        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        helpAppController.adaptWindow();
    }
}

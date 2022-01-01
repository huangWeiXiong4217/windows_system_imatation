package apps.systemFileApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class SystemFileApp extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        // 获取新窗口的fxml文件
        URL location = getClass().getResource("/apps/systemFileApp/SystemFileApp.fxml");
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
        //设置标题
        primaryStage.setTitle("可执行文件表");
        //舞台设置
        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);
        Scene scene = primaryStage.getScene();
        SystemFileAppController systemFileAppController = fxmlLoader.getController();
        //设置窗口的图标.
        location=getClass().getResource("/imgs/task.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
        //标题栏隐藏,以方便自定义标题栏
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();

        //控制器初始化
        systemFileAppController.init(primaryStage);
        primaryStage.setResizable(false);
        systemFileAppController.adaptWindow();
    }
}

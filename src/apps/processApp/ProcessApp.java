package apps.processApp;

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

//进程管理器
public class ProcessApp extends Application
{



    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        // 获取新窗口的fxml文件
        URL location = getClass().getResource("/apps/processApp/ProcessApp.fxml");
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
        //标题设置
        primaryStage.setTitle("进程管理器");
        //舞台设置
        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);
        Scene scene = primaryStage.getScene();
        //控制器设置
        ProcessAppController processAppController = fxmlLoader.getController();
        //设置窗口的图标.
        location=getClass().getResource("/imgs/process.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
        primaryStage.setResizable(false);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        //控制器初始化
        processAppController.init(primaryStage);
        //关闭整个程序
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        processAppController.adaptWindow();
    }
}

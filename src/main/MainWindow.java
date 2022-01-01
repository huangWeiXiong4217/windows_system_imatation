package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainWindow extends Application
{


    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException
    {
        // 获取新窗口的fxml文件
        URL location = getClass().getResource("/main/MainWindow.fxml");
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

        primaryStage.setTitle("os");

        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);

        Scene scene = primaryStage.getScene();
        MainController mainController = fxmlLoader.getController();
        // 监听窗口面板宽度变化
        primaryStage.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                // 更新GUI组件
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mainController.adaptWindow();
                    }
                });
            }
        });
        // 监听窗口面板高度变化
        primaryStage.heightProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                // 更新GUI组件
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mainController.adaptWindow();
                    }
                });
            }
        });
        //窗口最大化
        primaryStage.setMaximized(true);
        //设置窗口的图标.
        location=getClass().getResource("/imgs/Windows.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
        //隐藏顶部状态
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        mainController.init(scene,primaryStage);
        mainController.adaptWindow();

    }


    public static void main(String[] args)
    {
        launch(args);
    }
}

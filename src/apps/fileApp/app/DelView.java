package apps.fileApp.app;

import apps.fileApp.Controller.DelViewCtl;
import apps.fileApp.com.Disk;
import apps.fileApp.com.File;
import apps.fileApp.com.Folder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class DelView extends Application
{

    private Disk block;
    private String tipString;
    private MainUI mainView;

    public DelView(MainUI mainView,Disk block)
    {
        this.mainView=mainView;
        this.block = block;
        String mesg = "";
        if (block.getObject() instanceof Folder)
        {
            Folder folder = (Folder) block.getObject();
            mesg = "名称: " + folder.getFolderName()
                    + "\n类型: " + folder.getType()
                    + "\n大小: " + folder.getSize()
                    + "\n创建时间: " + folder.getCreateTime();
        }
        else
        {
            File file = (File) block.getObject();
            mesg = "名称: " + file.getFileName()
                    + "\n类型: " + file.getType()
                    + "\n大小: " + file.getSize() + "KB"
                    + "\n创建时间: " + file.getCreateTime();
        }
        tipString=mesg;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // 获取新窗口的fxml文件
        URL location = getClass().getResource("/apps/fileApp/fxmls/delView.fxml");
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
        primaryStage.setTitle("删除");
        //舞台设置
        Scene MainScene = new Scene(root);
        primaryStage.setScene(MainScene);
        Scene scene = primaryStage.getScene();
        DelViewCtl delViewCtl = fxmlLoader.getController();
        //设置窗口的图标.
        location=getClass().getResource("/apps/fileApp/res/tip.png");
        primaryStage.getIcons().add(new Image(String.valueOf(location)));
        //标题栏隐藏,以方便自定义标题栏
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();

        //控制器初始化
        delViewCtl.init(primaryStage,mainView,tipString,block);
        primaryStage.setResizable(false);
    }
}

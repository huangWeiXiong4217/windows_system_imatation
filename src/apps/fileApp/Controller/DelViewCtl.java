package apps.fileApp.Controller;

import apps.fileApp.app.MainUI;
import apps.fileApp.app.TipWindow;
import apps.fileApp.com.Disk;
import apps.fileApp.com.Folder;
import apps.fileApp.com.Path;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DelViewCtl
{

    @FXML
    private Button yes;

    @FXML
    private Button no;

    @FXML
    private Text text;

    ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////
    double xOffset = 0, yOffset = 0;
    Stage stage;
    //关闭窗口
    @FXML
    void closeStage(MouseEvent event)
    {
        stage.close();
    }
    //最小化窗口
    @FXML
    void minimizeStage(MouseEvent event)
    {
        stage.setIconified(true);

    }
    //按下窗口的时候：获取当前鼠标在窗口中的位置
    @FXML
    void pressBar(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    //拖动窗口的时候：更新当前窗口的位置，设置半透明
    @FXML
    void dragBar(MouseEvent event) {

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
        stage.setOpacity(0.8f);
    }
    //拖动窗口完毕，设置不透明
    @FXML
    void dragBarDone(DragEvent event) {
        stage.setOpacity(1.0f);
    }
    //释放窗口完毕，设置不透明
    @FXML
    void releaseBar(MouseEvent event) {
        stage.setOpacity(1.0f);
    }
    ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////


    private Disk block;
    private String tipString;
    public void init(Stage stage, MainUI mainView, String tipString, Disk block)
    {
        this.stage=stage;
        text.setText(tipString);
        this.block=block;

        yes.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                stage.close();
                Path thisPath = null;
                if (block.getObject() instanceof Folder) {
                    thisPath = ((Folder)block.getObject()).getPath();
                }
                int res = mainView.fat.delete(block);
                if (res == 0) {//删除文件夹成功
                    mainView.removeNode(mainView.getRecentNode(), thisPath);
                    try
                    {
                        tipOpen("删除文件夹成功");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                } else if (res == 1) {
                    try
                    {
                        tipOpen("删除文件成功");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                } else if (res == 2) {//文件夹不为空

                    try
                    {
                        tipOpen("文件夹不为空");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }

                } else {//文件未关闭
                    try
                    {
                        tipOpen("文件未关闭");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                mainView.flowPane.getChildren().removeAll(mainView.flowPane.getChildren());
                mainView.addIcon(mainView.fat.getBlockList(mainView.recentPath), mainView.recentPath);
            }
            });

            no.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    stage.close();
                }
            });

    }



    //提示窗口创建
    public void tipOpen(String tipString) throws Exception
    {
        Stage stage = new Stage();
        TipWindow tipWindow = new TipWindow(tipString);
        tipWindow.start(stage);
        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
    }


}

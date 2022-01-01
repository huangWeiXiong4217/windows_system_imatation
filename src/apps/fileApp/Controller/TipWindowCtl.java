package apps.fileApp.Controller;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TipWindowCtl
{
    @FXML
    private HBox toolBar;
    @FXML
    private Text tipText;

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
    public void  init(Stage stage,String tipString)
    {
        this.stage=stage;
        tipText.setText(tipString);
    }



}

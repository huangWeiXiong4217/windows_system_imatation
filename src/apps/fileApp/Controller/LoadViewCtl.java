package apps.fileApp.Controller;

import apps.fileApp.app.MainUI;
import apps.fileApp.app.TipWindow;
import apps.fileApp.com.FAT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoadViewCtl
{

    @FXML
    private Button yes;

    @FXML
    private Button no;

    ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////
    double xOffset = 0, yOffset = 0;
Stage stage;
    //关闭窗口
    @FXML
    void closeStage(MouseEvent event)
    {
      File log = new File("./data");
      FileWriter fileWriter = null;
      try {
        fileWriter = new FileWriter(String.valueOf(log));
        fileWriter.write("");System.out.println("clear");
        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      new FAT();
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

public void  init(Stage stage){
      this.stage = stage;
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

  public void yesbtn(ActionEvent actionEvent) {
    stage.close();
  }
  public void nobtn(ActionEvent actionEvent) {
    MainUI.clearFlag = true;
    stage.close();
    }

}

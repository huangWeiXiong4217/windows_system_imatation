package apps.fileApp.Controller;

import apps.fileApp.app.FileView;
import apps.fileApp.com.Disk;
import apps.fileApp.com.FAT;
import apps.fileApp.com.File;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FileViewCtl {

  ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////
  Stage stage;
  Disk block;
  File file;
  double xOffset;
  double yOffset;
  public void init(File file, Stage stage, Disk block)
  {
    this.file=file;
    this.stage=stage;
    this.block = block;
  }
  //关闭窗口
  @FXML
  void closeStage(MouseEvent event)
  {

    FileView.maps.remove(file);
    FAT.removeOpenedFile(block);
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

}

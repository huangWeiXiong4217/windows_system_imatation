package apps.fileApp.Controller;

import apps.fileApp.app.MainUI;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainCtl {
  @FXML
  private HBox toolBar;

  @FXML
  private TabPane TabP;

  @FXML
  private Label currentPath;

  @FXML
  private TreeView<?> treeView;

  @FXML
  private FlowPane flowPane;

  @FXML
  private Tab chartTab;

  @FXML
  private PieChart pie;

  @FXML
  private TableView<?> diskTable;

  @FXML
  private TableView<?> openFile;

  @FXML
  private TableColumn<?, ?> filePath;


  ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////
  Stage stage;
  double xOffset;
  double yOffset;
  public void init(Stage stage)
  {
    this.stage=stage;
  }

  //关闭窗口
  @FXML
  void closeStage(MouseEvent event)
  {
    MainUI.saveData();
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

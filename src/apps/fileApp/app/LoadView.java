package apps.fileApp.app;

import apps.fileApp.Controller.LoadViewCtl;
import apps.fileApp.com.FAT;
import apps.fileApp.com.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.MainController;
import utils.StageRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class LoadView {
  LoadView() throws IOException {
    Stage stage = MainController.checkStage("load");

    if (stage != null && stage.isShowing() == false)
    {
      MainController.removeStage("load");
    }

    stage = MainController.checkStage("load");
    if (stage == null)
    {
      URL location = getClass().getResource("/apps/fileApp/fxmls/loadView.fxml");
      if(location==null)
      {
        System.out.println("null");
        return ;
      }
      FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/apps/fileApp/fxmls/loadView.fxml"));
      Parent root1 = fxmlLoader1.load();
      stage =new Stage();

      stage.setAlwaysOnTop(true);
      stage.setIconified(false);
      stage.toFront();
      Scene scene = new Scene(root1,300,300);
      stage.setScene(scene);

      MainController.stageList.add(new StageRecord("load", stage));
      MainController.updateStageList("load");
      //设置窗口的图标.
      location=getClass().getResource("/apps/fileApp/res/tip.png");
      stage.getIcons().add(new Image(String.valueOf(location)));
      //标题栏隐藏,以方便自定义标题栏
      scene.setFill(Color.TRANSPARENT);
      stage.initStyle(StageStyle.TRANSPARENT);
      LoadViewCtl loadViewCtl = fxmlLoader1.getController();
      loadViewCtl.init(stage);
      stage.setResizable(false);
      stage.toFront();//置顶操作--蔡棱添加
      stage.showAndWait();
    }
    else if (stage != null && stage.isShowing() == true)
    {
      stage.toFront();//置顶操作--蔡棱添加
      stage.showAndWait();
    }

    if(MainUI.clearFlag){
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
      MainUI.fat = new FAT();
      System.out.println("clear1");
    }

  }
}

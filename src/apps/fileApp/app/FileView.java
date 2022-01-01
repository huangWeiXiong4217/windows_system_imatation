package apps.fileApp.app;

import apps.fileApp.Controller.FileViewCtl;
import apps.fileApp.com.Disk;
import apps.fileApp.com.FAT;
import apps.fileApp.com.File;
import apps.fileApp.com.Folder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileView  extends Application
{
  private File file;
  private Disk block;
  private String newContent, oldContent;
  private Stage stage;
  private TextArea contentField;
  private Label title;
  private MenuItem saveItem, closeItem,save_close,type1,type2,type3,type4,size1,size2,size3,size4,color1,color2,color3,color4;
  private  String color="-fx-text-fill:black;",size="-fx-font-size: 16px;",font="-fx-font-family: 'System';";
  public static Map<File, Stage> maps = new HashMap<>();
  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/apps/fileApp/fxmls/FileView.fxml"));
  private Parent root = fxmlLoader.load();
  public FileView(Stage stage,File file, Disk block) throws IOException {
    this.file = file;
    this.block = block;
    this.stage=stage;

    saveContent(file.getContent());
  }

  private void showView() {
    title = (Label) root.lookup("#fileIcon");
    title.setText(file.getLocation()+"\\"+file.getFileName());
    contentField = (TextArea) root.lookup("#contentField");
    contentField.setText(file.getContent());
    if (file.getFlag() == 0) { //读
      contentField.setDisable(true);
    }

    MenuBar Bar =(MenuBar) root.lookup("#Bar");
    Menu fileMenu =Bar.getMenus().get(0);
    Menu wordMenu = Bar.getMenus().get(1);

    saveItem = (MenuItem) fileMenu.getItems().get(0);
    saveItem.setGraphic(new ImageView("apps/fileApp/res/save.png"));
    ((ImageView)(saveItem.getGraphic())).setFitWidth(15);
    ((ImageView)(saveItem.getGraphic())).setFitHeight(15);

    saveItem.setOnAction(ActionEvent -> {
      newContent = contentField.getText();
      oldContent = file.getContent();
      if (newContent == null) {
        newContent = "";
      }
      if (!newContent.equals(oldContent)) {
        saveContent(newContent);
      }
    });

    save_close =  (MenuItem) fileMenu.getItems().get(1);
    save_close.setGraphic(new ImageView("apps/fileApp/res/save.png"));
    ((ImageView)(save_close.getGraphic())).setFitWidth(15);
    ((ImageView)(save_close.getGraphic())).setFitHeight(15);
    save_close.setOnAction(ActionEvent -> {

      newContent = contentField.getText();
      saveContent(newContent);
      MainUI.fat.removeOpenedFile(block);
      stage.close();
    });

    closeItem = (MenuItem) fileMenu.getItems().get(2);
    closeItem.setGraphic(new ImageView("apps/fileApp/res/close.png"));
    ((ImageView)(closeItem.getGraphic())).setFitWidth(15);
    ((ImageView)(closeItem.getGraphic())).setFitHeight(15);
    closeItem.setOnAction(ActionEvent -> {
      MainUI.fat.removeOpenedFile(block);
      stage.close();
    });

    Menu typeMenu = (Menu) wordMenu.getItems().get(0);
    Menu sizeMenu = (Menu) wordMenu.getItems().get(1);
    Menu colorMenu = (Menu) wordMenu.getItems().get(2);

    color1 = colorMenu.getItems().get(0);
    color2 = colorMenu.getItems().get(1);
    color3 = colorMenu.getItems().get(2);
    color4 = colorMenu.getItems().get(3);

    color1.setOnAction(ActionEvent -> {

      color = "-fx-text-fill:#7f7f7f;";
      contentField.setStyle( color + size + font);

    });
    color2.setOnAction(ActionEvent -> {
      color = "-fx-text-fill:black;";
      contentField.setStyle( color + size + font);    });
    color3.setOnAction(ActionEvent -> {
      color = "-fx-text-fill:#f91717;";
      contentField.setStyle( color + size + font);    });
    color4.setOnAction(ActionEvent -> {
      color = "-fx-text-fill:#00a2e8;";
      contentField.setStyle( color + size + font);    });

    size1 = sizeMenu.getItems().get(0);
    size2 = sizeMenu.getItems().get(1);
    size3 = sizeMenu.getItems().get(2);
    size4 = sizeMenu.getItems().get(3);

    size1.setOnAction(ActionEvent -> {
       size = "-fx-font-size: 12px;";
      contentField.setStyle( color + size + font);
    });
    size2.setOnAction(ActionEvent -> {
       size = "-fx-font-size: 16px;";
      contentField.setStyle( color + size + font);
    });
    size3.setOnAction(ActionEvent -> {
       size = "-fx-font-size: 20px;";
      contentField.setStyle( color + size + font);
    });
    size4.setOnAction(ActionEvent -> {
       size = "-fx-font-size: 24px;";
      contentField.setStyle( color + size + font);

    });

    type1 = typeMenu.getItems().get(0);
    type2 = typeMenu.getItems().get(1);
    type3 = typeMenu.getItems().get(2);
    type4 = typeMenu.getItems().get(3);

    type1.setOnAction(ActionEvent -> {
      font = "-fx-font-family: 'SimHei';";
      contentField.setStyle( color + size + font);
    });
    type2.setOnAction(ActionEvent -> {
      font = "-fx-font-family: 'NSimSun';";
      contentField.setStyle( color + size + font);

    });
    type3.setOnAction(ActionEvent -> {
      font = "-fx-font-family: 'KaiTi';";
      contentField.setStyle( color + size + font);
    });
    type4.setOnAction(ActionEvent -> {
      font = "-fx-font-family: 'Microsoft YaHei';";
      contentField.setStyle( color + size + font);
    });
    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.setTitle(file.getFileName());
    stage.titleProperty().bind(file.fileNamePProperty());
    stage.getIcons().add(new Image("apps/fileApp/res/file.png"));

    /***********************面板设置-蔡棱************************************/
    MainUI.fileAppAdditionStageList.add(stage);
    scene.setFill(Color.TRANSPARENT);
    stage.initStyle(StageStyle.TRANSPARENT);
    FileViewCtl fileViewCtl = fxmlLoader.getController();
    fileViewCtl.init(file,stage,block);
    /***********************面板设置-蔡棱************************************/
    stage.show();
    maps.put(file,stage);
    System.out.println(maps.get(file));
  }

//保存文件新内容
  private void saveContent(String newContent) {
    int newLength = newContent.length();
    int blockCount = FAT.blocksCount(newLength);
    file.setLength(blockCount);
    file.setContent(newContent);
    file.setSize(FAT.getSize(newLength));
    if (file.hasParent()) {
      Folder parent = (Folder) file.getParent();
      parent.setSize(FAT.getFolderSize(parent));
      while (parent.hasParent()) {
        parent = (Folder) parent.getParent();
        parent.setSize(FAT.getFolderSize(parent));
      }
    }
    MainUI.fat.reallocBlocks(blockCount, block);
  }


  @Override
  public void start(Stage primaryStage) throws Exception
  {
    showView();
  }

}

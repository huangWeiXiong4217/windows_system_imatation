package apps.fileApp.app;

import apps.fileApp.Controller.PropertyCtl;
import apps.fileApp.com.*;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Kit
 * @version: 2018年9月29日 上午12:07:57
 */
public class PropertyView extends Application
{

    private Disk block;
    private Label icon;
    private Map<Path, TreeItem<String>> pathMap;
    private String oldName, location;
    private Stage stage;
    private TextField nameField;
    private Label typeField, locField, spaceField, timeField;
    private Button okBtn, cancelBtn, applyBtn;
    private RadioButton checkRead, checkWrite;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Image ico;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/apps/fileApp/fxmls/PropertyView.fxml"));
    private Parent root = fxmlLoader.load();

    public PropertyView(Disk block, Label icon, Map<Path, TreeItem<String>> pathMap,Stage stage) throws IOException
    {
        this.block = block;
        this.icon = icon;
        this.pathMap = pathMap;
        this.stage=stage;
    }

    private void showView()
    {
        nameField = (TextField) root.lookup("#na");
        typeField = (Label) root.lookup("#type");
        locField = (Label) root.lookup("#loc");
        spaceField = (Label) root.lookup("#space");
        timeField = (Label) root.lookup("#time");
        okBtn = (Button) root.lookup("#yes");
        cancelBtn = (Button) root.lookup("#no");
        applyBtn = (Button) root.lookup("#apply");

        checkRead = (RadioButton) root.lookup("#read");
        checkRead.setToggleGroup(toggleGroup);
        checkRead.setUserData(0);//读
        checkWrite = (RadioButton) root.lookup("#write");
        checkWrite.setToggleGroup(toggleGroup);
        checkWrite.setUserData(1);//写
        toggleGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) ->
                {
                    applyBtn.setDisable(false);
                });
        Label icon= (Label) root.lookup("#propertyIcon");
        if (block.getObject() instanceof Folder)
        {
            Folder folder = (Folder) block.getObject();
            nameField.setText(folder.getFolderName());
            typeField.setText(folder.getType());
            locField.setText(folder.getLocation());
            spaceField.setText(folder.getSpace());
            timeField.setText(folder.getCreateTime());
            oldName = folder.getFolderName();
            location = folder.getLocation();
            checkRead.setDisable(true);
            checkWrite.setDisable(true);
            ico = new Image("apps/fileApp/res/folder.png");
            ImageView imageView=new ImageView(ico);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            ((Label) icon).setGraphic(imageView);
        }
        else
        {
            File file = (File) block.getObject();
            nameField.setText(file.getFileName());

            typeField.setText(file.getType());
            locField.setText(file.getLocation());
            spaceField.setText(file.getSpace());
            timeField.setText(file.getCreateTime());
            oldName = file.getFileName();
            location = file.getLocation();
            toggleGroup.selectToggle(file.getFlag() == 0 ? checkRead : checkWrite);
            ico = new Image("apps/fileApp/res/file.png");
            ImageView imageView=new ImageView(ico);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            ((Label) icon).setGraphic(imageView);
        }

        okBtn.setOnMouseEntered(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                okBtn.setStyle("-fx-background-color: #808080;");
            }
        });
        okBtn.setOnMouseExited(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                okBtn.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        cancelBtn.setOnMouseEntered(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                cancelBtn.setStyle("-fx-background-color: #808080;");
            }
        });
        cancelBtn.setOnMouseExited(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                cancelBtn.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        applyBtn.setOnMouseEntered(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                applyBtn.setStyle("-fx-background-color: #808080;");
            }
        });
        applyBtn.setOnMouseExited(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent event)
            {
                applyBtn.setStyle("-fx-background-color: #d3d3d3;");
            }
        });
        nameField.addEventFilter(MouseDragEvent.MOUSE_PRESSED, event ->
        {
            applyBtn.setDisable(false);
        });
        buttonOnAction();
        Scene scene = new Scene(root );
        stage = new Stage();
        /***********************面板设置-蔡棱************************************/
        MainUI.fileAppAdditionStageList.add(stage);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        PropertyCtl propertyCtl = fxmlLoader.getController();
        propertyCtl.init(stage);

        /***********************面板设置-蔡棱************************************/

        stage.setScene(scene);
        stage.setTitle("属性");
        stage.setResizable(false);
        stage.getIcons().add(ico);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    private void buttonOnAction()
    {
        applyBtn.setOnAction(ActionEvent ->
        {
          String newName = nameField.getText();
          String regEx = "[$./]";
          Pattern p = Pattern.compile(regEx);
          boolean m = p.matcher(newName).find();
            if (!oldName.equals(newName))
            {
              if(m){
                try {
                  System.out.println(m);
                  MainUI.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                  return;
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
              if (MainUI.fat.hasName(location, newName))
                {
                    try
                    {
                        MainUI.tipOpen("此位置已包含同名文件/文件夹");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                else
                {
                    if (block.getObject() instanceof Folder)
                    {
                        Folder thisFolder = (Folder) block.getObject();
                        thisFolder.setFolderName(newName);
                        pathMap.get(thisFolder.getPath()).setValue(newName);
                        reLoc(location, location, oldName, newName, thisFolder);
                    }
                    else
                    {
                        ((File) block.getObject()).setFileName(newName);
                    }
                    oldName = newName;
                    icon.setText(newName);
                }
            }
            if (block.getObject() instanceof File)
            {
                File thisFile = ((File) block.getObject());
                int newFlag = toggleGroup.getSelectedToggle().getUserData().hashCode();
                System.out.println(toggleGroup.getSelectedToggle().getUserData());
                thisFile.setFlag(newFlag);
            }
            applyBtn.setDisable(true);
        });
        cancelBtn.setOnAction(ActionEvent ->
        {
            stage.close();
        });
        okBtn.setOnAction(ActionEvent ->
        {
            String newName = nameField.getText();
            String regEx = "[$./]";
          Pattern p = Pattern.compile(regEx);
          boolean m = p.matcher(newName).find();
            if (!oldName.equals(newName))
            {
              if(m){
                try {
                  System.out.println(m);
                  MainUI.tipOpen("合法目录名仅可以使用字母、数字和除“$”、“.”、“/”以外的字符");
                  return;

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
                if (MainUI.fat.hasName(location, newName))
                {
                    try
                    {
                        MainUI.tipOpen("此位置已包含同名文件/文件夹");
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                else
                {
                    if (block.getObject() instanceof Folder)
                    {
                        Folder thisFolder = (Folder) block.getObject();
                        thisFolder.setFolderName(newName);
                        pathMap.get(thisFolder.getPath()).setValue(newName);
                        reLoc(location, location, oldName, newName, thisFolder);
                    }
                    else
                    {
                        ((File) block.getObject()).setFileName(newName);
                    }
                    icon.setText(newName);
                }
            }
            if (block.getObject() instanceof File)
            {
                File thisFile = ((File) block.getObject());
                int newFlag = toggleGroup.getSelectedToggle().getUserData().hashCode();
                thisFile.setFlag(newFlag);
            }
            stage.close();
        });
    }

    //更改名称
    private void reLoc(String oldP, String newP, String oldN, String newN, Folder folder)
    {
        String oldLoc = oldP + "\\" + oldN;
        String newLoc = newP + "\\" + newN;
        Path oldPath = MainUI.fat.getPath(oldLoc);
      MainUI.fat.replacePath(oldPath, newLoc);
        for (Object child : folder.getChildren())
        {
            if (child instanceof File)
            {//对象为文件
                ((File) child).setLocation(newLoc);
            }
            else
            {//对象为文件夹
                Folder nextFolder = (Folder) child;
                nextFolder.setLocation(newLoc);
                if (nextFolder.hasChild())
                {
                    reLoc(oldLoc, newLoc, nextFolder.getFolderName(),
                            nextFolder.getFolderName(), nextFolder);
                }
                else
                {
                    Path nextPath = MainUI.fat.getPath(oldLoc + "\\" +
                            nextFolder.getFolderName());
                    String newNext = newLoc + "\\" + nextFolder.getFolderName();
                  MainUI.fat.replacePath(nextPath, newNext);
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        showView();
    }
}

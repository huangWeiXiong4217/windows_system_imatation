package apps.systemFileApp;

import datas.ExecutableFileData;
import datas.InstructionData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.DataLoader;
import utils.DrawUtil;
import utils.ExecutableFile;
import utils.ProcessManager;

import java.util.ArrayList;
import java.util.Vector;

//可执行文件表控制器
public class SystemFileAppController
{
    //标题栏
    @FXML
    private BorderPane titlebar;
    //顶层面板
    @FXML
    private AnchorPane topMainPane;
    //可执行文件表格
    @FXML
    private  TableView<ExecutableFileData> executableFileTable;
    //文件名栏
    @FXML
    private  TableColumn<?, ?> fileName;
    //文件信息表格
    @FXML
    private  TableView<InstructionData> fileDetailTable;
    //文件信息栏
    @FXML
    private  TableColumn<?, ?> fileDetail;
    //选择第一个表格的文件，就更新第一个表格
    @FXML
    void selectFile(MouseEvent event)
    {
        //获取对应编号
        String s=executableFileTable.getSelectionModel().getSelectedItem().toString();
        int i=Integer.valueOf(s);
        updateFileDetailTable(i);
    }



    ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////

    boolean haveResize=true;

    boolean isMax=false;

    @FXML
    void resizeStage(MouseEvent event)
    {
        if(!haveResize)
        {
            return;
        }
        haveResize=false;

        if(!isMax)
        {
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.setX(0);
            stage.setY(0);
            adaptWindow();
        }
        else {


            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());
            stage.setX(0);
            stage.setY(0);
            adaptWindow();

        }
        isMax=!isMax;
        haveResize=true;
    }
    public void adaptWindow()
    {
        double sw=stage.getWidth();
        double sh=stage.getHeight();
        double w = 50 *sw/1920;

        titlebar.setPrefWidth(sw);
        titlebar.setMaxWidth(sw);
        titlebar.setMinWidth(sw);

        executableFileTable.setPrefWidth(sw/3);
        executableFileTable.setMaxWidth(sw/3);
        executableFileTable.setMinWidth(sw/3);

        fileDetailTable.setPrefWidth(sw/3);
        fileDetailTable.setMaxWidth(sw/3);
        fileDetailTable.setMinWidth(sw/3);

    }

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
    //可执行文件队列
    public static Vector<ExecutableFile> executableFileList;
    //可执行文件数据队列
    public  static ArrayList<ExecutableFileData> executableFileDataList = new ArrayList<>();
    //对应文件的指令队列
    public  ArrayList<InstructionData> fileDetailDataList = new ArrayList<>();



    public SystemFileAppController()
    {

    }



    //初始化
    public void init(Stage stage)
    {
        this.stage=stage;
        //得到进程管理器中的可执行文件表
        executableFileList= ProcessManager.executableFileList;
        //文件表初始化
        fileName.setCellValueFactory(new PropertyValueFactory("fileName"));
        //文件内容初始化
        fileDetail.setCellValueFactory(new PropertyValueFactory("instruction"));
        updateFileTable(executableFileTable);
        // 拖动监听器
        DrawUtil drawUtil=new DrawUtil();
        drawUtil.addDrawFunc(stage, topMainPane);
//        FileAppController.tableViews=new TableView[]{executableFileTable};
        // 监听窗口面板高度变化
        stage.widthProperty().addListener(new ChangeListener<Number>()
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
                        adaptWindow();
                    }
                });
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>()
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
                        adaptWindow();
                    }
                });
            }
        });

    }



    //更新第一个表格（文件列表）
    public  static void updateFileTable(TableView<ExecutableFileData> executableFileTable)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                DataLoader.fileDataLoad(executableFileDataList, executableFileList);
                executableFileTable.setItems(FXCollections.observableArrayList(executableFileDataList));
            }
        });
    }

    //更新第二个表格（对应文件指令）
    public void updateFileDetailTable(int i)
    {

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                DataLoader.fileDetailDataLoad(fileDetailDataList, executableFileList.get(i));
                fileDetail.setText("对应编号："+i);
                fileDetailTable.setItems(FXCollections.observableArrayList(fileDetailDataList));

            }
        });
    }
}

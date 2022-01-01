package apps.occupancyApp;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.DrawUtil;
import utils.UIThread;

//占用管理 控制器
public class OccupancyAppController
{
    @FXML
    private AnchorPane topMainPane;

    @FXML
    private BorderPane titlebar;
    //内存占比文本
    @FXML
    private Button memoryText;
    //磁盘占比文本
    @FXML
    private Button diskText;
    //pcb占比文本
    @FXML
    private Button pcbText;
    //设备占比文本
    @FXML
    private Button deviceText;
    //磁盘占用的第一个矩形框
    @FXML
    private VBox diskBox1;
    //内存占用的第一个矩形框
    @FXML
    private VBox memoryBox1;
    //设置占用的第一个矩形框
    @FXML
    private VBox deviceBox1;
    //pcb占用的第一个矩形框
    @FXML
    private VBox pcbBox1;
    //内存占用的第二个矩形框
    @FXML
    private HBox memoryBox2;
    //设备占用的第二个矩形框
    @FXML
    private HBox deviceBox2;
    //磁盘占用的第二个矩形框
    @FXML
    private HBox diskBox2;
    //pcb占用的第二个矩形框
    @FXML
    private HBox pcbBox2;

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

    }

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



    double xOffset = 0, yOffset = 0;
    Stage stage;
    ///////////////////自定义标题栏的拖动效果实现/////////////////////////////////////////////////




    //初始化
    public void init(Stage stage)
    {
        this.stage=stage;

        //以数组方式存起来
        VBox[] boxes1={memoryBox1,diskBox1,deviceBox1,pcbBox1};
        Button[] textbuttons={memoryText,diskText,deviceText,pcbText};
        HBox[] boxes2={memoryBox2,diskBox2,deviceBox2,pcbBox2};


        //内存的第二个矩形加入512个内置矩形，用于后面的涂色。
        for(int i=0;i<512;i++)
        {
            double hight = boxes2[0].getHeight() ;
            double width = boxes2[0].getWidth() ;
            //乘0.95是试出来的，这样显示效果最好，能够充满整个框，这个数字可以忽略。
            double percent=1.0/512*0.95;
            Region region=new Region();
            //设置尺寸
            region.setPrefSize(percent*width,hight);
            region.setMinSize(percent*width, hight);
            region.setMaxSize(percent*width, hight);
            memoryBox2.getChildren().add(region);
        }

        //磁盘的第二个矩形加入256个内置矩形，用于后面的涂色
        for(int i=0;i<256;i++)
        {
            double hight = boxes2[1].getHeight() ;
            double width = boxes2[1].getWidth() ;
            //乘0.95是试出来的，这样显示效果最好，能够充满整个框，这个数字可以忽略。
            double percent=1.0/256*0.95;
            Region region=new Region();
            //设置尺寸
            region.setPrefSize(percent*width,hight);
            region.setMinSize(percent*width, hight);
            region.setMaxSize(percent*width, hight);
            diskBox2.getChildren().add(region);
        }


        //设备的第二个矩形加入8个内置矩形，用于后面的涂色。
        for(int i=0;i<8;i++)
        {
            double hight = boxes2[2].getHeight() ;
            double width = boxes2[3].getWidth() ;
            //乘0.99是试出来的，这样显示效果最好，能够充满整个框，这个数字可以忽略。
            double percent=1.0/8*0.99;
            Label region=new Label();
            //设置css的id为emptyBox
            region.setId("emptyBox");
            //设置文本，用于区分是哪个设备
            if(i>=0&&i<=1)
            {
                region.setText("A");
            }
            else if(i>=2&&i<=4)
            {
                region.setText("B");
            }
            else
            {
                region.setText("C");
            }
            //设置尺寸
            region.setPrefSize(percent*width,0.9*hight);
            region.setMinSize(percent*width, 0.9*hight);
            region.setMaxSize(percent*width, 0.9*hight);
            region.setAlignment(Pos.CENTER);
            deviceBox2.getChildren().add(region);
        }


        //pcb的第二个矩形加入10个内置矩形，用于后面的涂色。
        for(int i=0;i<10;i++)
        {
            double hight = boxes2[2].getHeight() ;
            double width = boxes2[3].getWidth() ;
            //乘0.99是试出来的，这样显示效果最好，能够充满整个框，这个数字可以忽略。
            double percent=1.0/10*0.99;
            Label region=new Label();

            //设置css的id为emptyBox
            region.setId("emptyBox");
            //显示是那个框
            region.setText(i+"");

            //设置尺寸
            region.setPrefSize(percent*width,0.9*hight);
            region.setMinSize(percent*width, 0.9*hight);
            region.setMaxSize(percent*width, 0.9*hight);
            region.setAlignment(Pos.CENTER);
            pcbBox2.getChildren().add(region);
        }
        // 拖动监听器
        DrawUtil drawUtil=new DrawUtil();
        drawUtil.addDrawFunc(stage, topMainPane);
        //初始化UIThread中的三个变量，以方便界面线程直接调用这些组件。
        UIThread.boxes1=boxes1;
        UIThread.textbuttons=textbuttons;
        UIThread.boxes2=boxes2;

    }

}

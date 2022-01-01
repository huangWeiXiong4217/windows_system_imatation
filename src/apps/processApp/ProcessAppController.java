package apps.processApp;

import datas.InstructionData;
import datas.ProcessDetailData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.DrawUtil;
import utils.ProcessManager;
import utils.ProcessScheduleThread;
import utils.UIThread;


//进程管理器控制器
public class ProcessAppController
{
    //标题栏
    @FXML
    private BorderPane titlebar;
    //顶层面板
    @FXML
    private AnchorPane topMainPane;
    //进程表
    @FXML
    private TableView<?> processTable;
    //进程名栏
    @FXML
    private TableColumn<?, ?> processName;
    //进程状态栏
    @FXML
    private TableColumn<?, ?> processState;
    //执行的文件对应编号栏
    @FXML
    private TableColumn<?, ?> whichFile;
    //占用设备栏
    @FXML
    private TableColumn<?, ?> havedDevice;
    //占用内存栏
    @FXML
    private TableColumn<?, ?> havedMemory;
    //占用pcb栏
    @FXML
    private TableColumn<?, ?> havedPid;
    //执行结果栏
    @FXML
    private TableColumn<?, ?> result;
    //进度条栏
    @FXML
    private TableColumn<?, ?> progressBar;
    //当前执行进程
    @FXML
    private Button nowProcessName;
    //当前执行结果
    @FXML
    private Button nowResult;
    //当前执行指令
    @FXML
    private Button nowInstruction;
    //当前执行进程的指令表
    @FXML
    private TableView<?> nowProcessTable;
    //指令栏
    @FXML
    private TableColumn<?, ?> instruction;
    //显示当前选项
    @FXML
    private CheckBox showNow;
    //显示新建进程选项
    @FXML
    private CheckBox showCreating;
    //显示就绪进程选项
    @FXML
    private CheckBox showWaiting;
    //显示阻塞进程选项
    @FXML
    private CheckBox showBlocked;
    //显示销毁进程选项
    @FXML
    private CheckBox showEnded;
    //显示所有进程选项
    @FXML
    private CheckBox showAll;
    //高亮销毁进程选项
    @FXML
    private CheckBox signEnded;
    //高亮新建进程选项
    @FXML
    private CheckBox signCreating;
    //高亮就绪进程选秀
    @FXML
    private CheckBox signWaiting;
    //高亮阻塞进程选项
    @FXML
    private CheckBox signBlocked;
    //高亮运行进程选项
    @FXML
    private CheckBox signRunning;
    //继续新建进程选项
    @FXML
    private CheckBox continueButton;
    //暂停新建进程选项
    @FXML
    private CheckBox suspendButton;
    //剩余时间片
    @FXML
    private Button residueSlice;
    @FXML
    private RadioButton speed1Button;

    @FXML
    private RadioButton speed2Button;

    @FXML
    private RadioButton speed4Button;

    @FXML
    private RadioButton speed8Button;

    boolean haveResize=true;
    boolean isMax=false;
    @FXML
    void resizeStage(MouseEvent event)
    {
        if (!haveResize)
        {
            return;
        }
        haveResize = false;

        if (!isMax)
        {
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.setX(0);
            stage.setY(0);
        }
        else
        {
            stage.setWidth(stage.getMinWidth());
            stage.setHeight(stage.getMinHeight());

            stage.setX(0);
            stage.setY(0);
        }
        isMax = !isMax;
        haveResize = true;
    }


    //进程设置相关按钮的互斥
    @FXML
    void createSelectByMouse(MouseEvent event)
    {

        createSelect((CheckBox) event.getSource());
    }


    void createSelect(CheckBox checkBox)
    {
        CheckBox[] array = {continueButton,suspendButton};
        for (int i = 0; i < 2; i++)
        {
            if (checkBox == array[i])
            {
                array[i].setSelected(true);
            } else
            {
                array[i].setSelected(false);
            }
        }
        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    //显示设置相关按钮的互斥
    @FXML
    void showSelectByMouse(MouseEvent event)
    {
        showSelect((CheckBox) event.getSource());
    }

    void showSelect(CheckBox checkBox) {
        CheckBox[] array = {showNow, showCreating, showWaiting, showBlocked,showEnded,showAll};
        for (int i = 0; i < 6; i++)
        {
            if (checkBox == array[i])
            {
                array[i].setSelected(true);
            } else
            {
                array[i].setSelected(false);
            }
        }
        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    //标记设置相关按钮的互斥
    @FXML
    void signSelectByMouse(MouseEvent event) {
        signSelect((CheckBox) event.getSource());
    }

    void signSelect(CheckBox checkBox)
    {
        CheckBox[] array = {signCreating, signWaiting,signRunning, signBlocked, signEnded};
        for (int i = 0; i < 5; i++)
        {
            if (checkBox == array[i])
            {
                array[i].setSelected(true);
            } else
            {
                array[i].setSelected(false);
            }
        }
        System.out.println(checkBox.getId() + "被选中");
        checkBox.setSelected(true);
    }

    //倍速设置

    //显示设置相关按钮的互斥
    @FXML
    void speedSelectByMouse(MouseEvent event)
    {
        speedSelect((RadioButton) event.getSource());
    }

    void speedSelect(RadioButton radioButton) {
        RadioButton[] array = {speed1Button, speed2Button, speed4Button, speed8Button};
        for (int i = 0; i < 4; i++)
        {
            if (radioButton == array[i])
            {
                array[i].setSelected(true);
            } else
            {
                array[i].setSelected(false);
            }
        }
        if(speed1Button.isSelected())
        {
            ProcessManager.speed=1;
        }
        else if (speed2Button.isSelected())
        {
            ProcessManager.speed=2;
        }
        else if (speed4Button.isSelected())
        {
            ProcessManager.speed=4;
        }
        else if (speed8Button.isSelected())
        {
            ProcessManager.speed=8;
        }
        System.out.println(radioButton.getId() + "被选中");
        radioButton.setSelected(true);
    }






    public ProcessAppController()
    {

    }

    //关闭
    @FXML
    void closeStage(MouseEvent event)
    {
        stage.close();
    }
    //最小化
    @FXML
    void minimizeStage(MouseEvent event)
    {
        stage.setIconified(true);
    }

    //
    @FXML
    void dragBar(MouseEvent event) {
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
        stage.setOpacity(0.8f);
    }

    @FXML
    void dragBarDone(DragEvent event) {
        stage.setOpacity(1.0f);
    }

    @FXML
    void pressBar(MouseEvent event) {

        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void releaseBar(MouseEvent event) {
        stage.setOpacity(1.0f);
    }

    double xOffset = 0, yOffset = 0;
    Stage stage;


    public void init(Stage stage)
    {
        this.stage=stage;
        //按钮选择默认
        showSelect(showNow);
        signSelect(signRunning);
        createSelect(continueButton);
        speedSelect(speed1Button);
        //表格初始化
        processName.setCellValueFactory(new PropertyValueFactory("processName"));
        processState.setCellValueFactory(new PropertyValueFactory("processState"));
        whichFile.setCellValueFactory(new PropertyValueFactory("whichFile"));
        havedDevice.setCellValueFactory(new PropertyValueFactory("havedDevice"));
        havedMemory.setCellValueFactory(new PropertyValueFactory("havedMemory"));
        havedPid.setCellValueFactory(new PropertyValueFactory("havedPid"));
        result.setCellValueFactory(new PropertyValueFactory("result"));
        progressBar.setCellValueFactory(new PropertyValueFactory("progressBar"));

        //表格初始化
        instruction.setCellValueFactory(new PropertyValueFactory("instruction"));

        //UI线程初始化
        UIThread.processTable= (TableView<ProcessDetailData>) this.processTable;
        UIThread.nowProcessTable= (TableView<InstructionData>) this.nowProcessTable;
        UIThread.nowProcessName=this.nowProcessName;
        UIThread.nowResult=this.nowResult;
        UIThread.nowInstruction=this.nowInstruction;
        UIThread.residueSlice=this.residueSlice;

        UIThread.checkBoxes1=new CheckBox[]{showNow, showCreating, showWaiting, showBlocked,showEnded,showAll};
        UIThread.checkBoxes2=new CheckBox[]{signCreating, signWaiting ,signRunning,signBlocked, signEnded};
//
//        DataLoader.checkBoxes1=new CheckBox[]{showNow, showCreating, showWaiting, showBlocked,showEnded,showAll};
//        DataLoader.checkBoxes2=new CheckBox[]{signCreating,signWaiting, signRunning, signBlocked, signEnded};


        //调度线程初始化
        ProcessScheduleThread.controlButton=new CheckBox[]{continueButton,suspendButton};
        // 拖动监听器
        DrawUtil drawUtil=new DrawUtil();
        drawUtil.addDrawFunc(stage, topMainPane);
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
    //适应窗口
    public void adaptWindow()
    {
        double sw=stage.getWidth();
        double sh=stage.getHeight();
        double w = 50 *sw/1920;

        titlebar.setPrefWidth(sw);
        titlebar.setMaxWidth(sw);
        titlebar.setMinWidth(sw);



    }



}

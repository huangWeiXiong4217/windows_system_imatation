package main;

import apps.browserApp.BrowserApp;
import apps.fileApp.app.MainUI;
import apps.fileApp.com.FAT;
import apps.helpApp.HelpApp;
import apps.occupancyApp.OccupancyApp;
import apps.processApp.ProcessApp;
import apps.systemFileApp.SystemFileApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


public class MainController
{
    final Label menu = new Label();
    public ProcessScheduleThread processScheduleThread = new ProcessScheduleThread();

    public UIThread uiThread = new UIThread();

    ArrayList<Button> buttonList = new ArrayList<>();

    Scene scene = null;

    Stage primaryStage = null;

    Button temporaryButton = null;

    public  static Vector<StageRecord> stageList = new Vector<>();

    //app图标参数
    double appWidth;
    double distance;
    //窗口大小
    double sceneWidth;
    double sceneHight;


    @FXML
    private Pane MainWindow;

    @FXML
    private ImageView background;

    @FXML
    private Pane buttomBar;

    @FXML
    private HBox appBox;

    @FXML
    private Button broswerButton;

    @FXML
    private Button executableFileButton;

    @FXML
    private Button processButton;

    @FXML
    private Button occupancyButton;

    @FXML
    private HBox tipBox;

    @FXML
    private Separator separator1;

    @FXML
    private Button minimizeButton;

    @FXML
    private Separator separator2;

    @FXML
    private Button closeButton;

    @FXML
    private VBox timeBox;

    @FXML
    private Button timeButton1;

    @FXML
    private Button timeButton2;

    @FXML
    private Button deskButton;

    @FXML
    private Button helpButton;
    @FXML
    private Button fileManagerButton;



    public MainController()
    {
    }

    @FXML
    void closeWindow(MouseEvent event)
    {
      FAT.closeAll();
      if(MainUI.fat!=null)
        MainUI.saveData();//关闭时保存数据
        System.exit(0);
        Platform.exit();
        primaryStage.close();

    }

    @FXML
    void minimizeWindow(MouseEvent event)
    {
        for(int i=0;i<stageList.size();i++)
        {
            StageRecord stageRecord=stageList.get(i);
            if(stageRecord.name.contains("apps/"))
            {
                Stage stage=stageRecord.stage;
                if(stage!=null)
                {
                    stage.setIconified(true);
                }
            }
        }
        //文件管理器 附属文件窗口最小化
        if(MainUI.fileAppAdditionStageList!=null)
        {
            for(int i=0;i<MainUI.fileAppAdditionStageList.size();i++)
            {
                Stage stage1=MainUI.fileAppAdditionStageList.get(i);
                if(stage1!=null&&stage1.isShowing()==false)
                {
                    MainUI.fileAppAdditionStageList.remove(stage1);
                }
                else
                {
                    stage1.setIconified(true);
                }
            }
        }
        primaryStage.setIconified(true);

    }

    @FXML
    void updateTimeButton1(ActionEvent event)
    {

    }

    @FXML
    void updateTimeButton2(ActionEvent event)
    {

    }



    public void iconInit()
    {
        appWidth = scene.getHeight() / 15;
//        distance = scene.getWidth() / 20;
        sceneWidth = scene.getWidth();
        sceneHight = scene.getHeight();
        //背景图片初始化
        background.setLayoutX(0);
        background.setLayoutY(0);
        background.fitWidthProperty().bind(scene.widthProperty());
        background.fitHeightProperty().bind(scene.heightProperty());
        background.setPreserveRatio(false);
        background.setVisible(true);
//        final Delta dragDelta = new Delta();
        //浏览器按钮初始化
        broswerButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {

                    if (event.getClickCount() == 1)
                    {
                        System.out.println("browser open success");
                        broswerAppOpen();
                    }
                }
            }
        });
        //可执行文件按钮初始化
        executableFileButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
//                    if(event.getClickCount()==1)
//                    {
//                        Stage stage=checkStage("apps/systemFileApp");
//
//                        if (stage != null && stage.isShowing() == true)
//                        {
//                            System.out.println("executableFileApp open success");
//                            executableFileAppOpen();
//                        }
//                    }
                    if (event.getClickCount() == 1)
                    {
                        System.out.println("executableFileApp open success");
                        executableFileAppOpen();
                    }
                }
            }
        });
        //进程按钮初始化
        processButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
                    if (event.getClickCount() == 1)
                    {
                        System.out.println("processApp open success");
                        processAppOpen();
                    }
                }

            }
        });
        //占用按钮初始化
        occupancyButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
                    if (event.getClickCount() == 1)
                    {
                        System.out.println("occupancyApp open success");
                        occupancyAppOpen();
                    }
                }
            }
        });

        //帮助按钮初始化
        helpButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
                    if (event.getClickCount() == 1)
                    {
                        System.out.println("helpApp open success");
                        helpAppOpen();
                    }
                }
            }
        });
        //桌面按钮初始化
        deskButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {
//                    if (event.getClickCount() == 1)
//                    {
////                        System.out.println("helpApp open success");

//                        primaryStage.setAlwaysOnTop(false);
                        toDesk();
//                    }
                }
            }
        });

        //文件管理器
        fileManagerButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton().equals(MouseButton.PRIMARY))
                {

                    if (event.getClickCount() == 1)
                    {
                        System.out.println("fileApp open success");
                        try
                        {
                            fileAppOpen();
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });


        //分割线1
        separator1.setMaxWidth(appWidth * 0.8);
        separator1.setMinWidth(appWidth * 0.8);


        //图标
        Button[] buttons={broswerButton,executableFileButton,fileManagerButton,processButton,occupancyButton,helpButton};

        Tooltip tooltip=new Tooltip("简易浏览器");
        tooltip.setStyle("-fx-font-size: 12");
        broswerButton.setTooltip(tooltip);

        tooltip=new Tooltip("系统文件表");
        tooltip.setStyle("-fx-font-size: 12");
        executableFileButton.setTooltip(tooltip);

        tooltip=new Tooltip("文件管理器");
        tooltip.setStyle("-fx-font-size: 12");
        fileManagerButton.setTooltip(tooltip);

        tooltip=new Tooltip("进程管理器");
        tooltip.setStyle("-fx-font-size: 12");
        processButton.setTooltip(tooltip);

        tooltip=new Tooltip("占用管理器");
        tooltip.setStyle("-fx-font-size: 12");
        occupancyButton.setTooltip(tooltip);

        tooltip=new Tooltip("帮助");
        tooltip.setStyle("-fx-font-size: 12");
        helpButton.setTooltip(tooltip);

        tooltip=new Tooltip("最小化");
        tooltip.setStyle("-fx-font-size:12");
        minimizeButton.setTooltip(tooltip);

        tooltip=new Tooltip("关闭");
        tooltip.setStyle("-fx-font-size: 12");
        closeButton.setTooltip(tooltip);

        tooltip=new Tooltip("显示桌面");
        tooltip.setStyle("-fx-font-size: 12");
        deskButton.setTooltip(tooltip);


        UIThread.mainButtons=buttons;
        //StageMap
        UIThread.stageList=stageList;

    }


    boolean isTop=false;
    boolean haveChanged=true;

    public void toDesk()
    {
        if(haveChanged)
        {
            haveChanged=false;
            if(!isTop)
            {
                primaryStage.toFront();
                for(int i=0;i<stageList.size();i++)
                {
                    Stage stage=stageList.get(i).stage;
                    if(stage!=null&&stage.isShowing()==true)
                {
                    stage.setIconified(true);
                }
                }
                //文件管理器 附属文件窗口最小化
                if(MainUI.fileAppAdditionStageList!=null)
                {
                    for(int i=0;i<MainUI.fileAppAdditionStageList.size();i++)
                    {
                        Stage stage1=MainUI.fileAppAdditionStageList.get(i);
                        if(stage1!=null&&stage1.isShowing()==false)
                        {
                            MainUI.fileAppAdditionStageList.remove(stage1);
                        }
                        else
                        {
                            stage1.setIconified(true);
                        }
                    }
                }
            }
            else
            {
                Button[] mainButtons={broswerButton,executableFileButton,fileManagerButton,processButton,occupancyButton,helpButton};
                for(int i=0;i<stageList.size();i++)
                {
                    Stage stage=stageList.get(i).stage;
                    if(stage!=null&&stage.isShowing()==true)
                    {
                        stage.setIconified(false);
                    }
                }
                if(MainUI.fileAppAdditionStageList!=null)
                {
                    for(int i=0;i<MainUI.fileAppAdditionStageList.size();i++)
                    {
                        Stage stage1=MainUI.fileAppAdditionStageList.get(i);
                        if(stage1!=null&&stage1.isShowing()==false)
                        {
                            MainUI.fileAppAdditionStageList.remove(stage1);
                        }
                        else
                        {
                            stage1.setIconified(false);
                        }
                    }
                }

            }

            isTop=!isTop;
            haveChanged=!haveChanged;
        }


    }
    public void init(Scene scene, Stage stage) throws URISyntaxException
    {
        this.scene = scene;
        this.primaryStage = stage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {

                System.out.println("结束");
                System.exit(0);
            }
        });
        iconInit();
        timeInit();
        OccupancyManager.init();
        proceessThreadInit();
        uiThreadInit();

    }

    //适应窗口
    public void adaptWindow()
    {
        //主窗口
        MainWindow.setPrefSize(scene.getWidth(), scene.getHeight());
        System.out.println("MainWindow:" + MainWindow.getWidth() + "," + MainWindow.getHeight());
        //底部栏
        buttomBar.setMaxHeight(1.5 * appWidth);
        buttomBar.setMinHeight(1.5 * appWidth);
        buttomBar.setPrefHeight(1.5 * appWidth);
        buttomBar.setMaxWidth(sceneWidth - 1 * appWidth);
        buttomBar.setMinWidth(sceneWidth - 1 * appWidth);
        buttomBar.setPrefWidth(sceneWidth - 1 * appWidth);
        buttomBar.setLayoutX(0.5 * appWidth);
        buttomBar.setLayoutY(sceneHight - 1.7 * appWidth);
        //桌面图标
        broswerButton.setPrefSize(1.2 * appWidth, 1.2 * appWidth);
        broswerButton.setMinSize(1.2 * appWidth, 1.2 * appWidth);
        broswerButton.setMaxSize(1.2 * appWidth, 1.2 * appWidth);
        broswerButton.resize(1.2 * appWidth, 1.2 * appWidth);
        ((ImageView) broswerButton.getGraphic()).setFitWidth(appWidth*0.9);
        ((ImageView) broswerButton.getGraphic()).setFitHeight(appWidth*0.9);

        executableFileButton.setPrefSize(1.2 * appWidth, 1.2 * appWidth);
        executableFileButton.setMinSize(1.2 * appWidth, 1.2 * appWidth);
        executableFileButton.setMaxSize(1.2 * appWidth, 1.2 * appWidth);
        executableFileButton.resize(1.2 * appWidth, 1.2 * appWidth);
        ((ImageView) executableFileButton.getGraphic()).setFitWidth(appWidth*0.9);
        ((ImageView) executableFileButton.getGraphic()).setFitHeight(appWidth*0.9);


        fileManagerButton.setPrefSize(1.2 * appWidth, 1.2 * appWidth);
        fileManagerButton.setMinSize(1.2 * appWidth, 1.2 * appWidth);
        fileManagerButton.setMaxSize(1.2 * appWidth, 1.2 * appWidth);
        fileManagerButton.resize(1.2 * appWidth, 1.2 * appWidth);
        ((ImageView) fileManagerButton.getGraphic()).setFitWidth(appWidth*0.9);
        ((ImageView) fileManagerButton.getGraphic()).setFitHeight(appWidth*0.9);


        processButton.setPrefSize(1.2 * appWidth, 1.2 * appWidth);
        processButton.setMinSize(1.2 * appWidth, 1.2 * appWidth);
        processButton.setMaxSize(1.2 * appWidth, 1.2 * appWidth);
        processButton.resize(1.2 * appWidth, 1.2 * appWidth);
        ((ImageView) processButton.getGraphic()).setFitWidth(appWidth*0.9);
        ((ImageView) processButton.getGraphic()).setFitHeight(appWidth*0.9);


        occupancyButton.setPrefSize(1.2 * appWidth, 1.2 * appWidth);
        occupancyButton.setMinSize(1.2 * appWidth, 1.2 * appWidth);
        occupancyButton.setMaxSize(1.2 * appWidth, 1.2 * appWidth);
        occupancyButton.resize(1.2 * appWidth, 1.2 * appWidth);
        ((ImageView) occupancyButton.getGraphic()).setFitWidth(appWidth*0.9);
        ((ImageView) occupancyButton.getGraphic()).setFitHeight(appWidth*0.9);


        //帮助按钮
        helpButton.setPrefSize(0.8 * appWidth, 0.8 * appWidth);
        helpButton.setMinSize(0.8 * appWidth, 0.8 * appWidth);
        helpButton.setMaxSize(0.8 * appWidth, 0.8 * appWidth);
        helpButton.resize(0.8 * appWidth, 0.8 * appWidth);
        ((ImageView) helpButton.getGraphic()).setFitWidth(0.8 * appWidth);
        ((ImageView) helpButton.getGraphic()).setFitHeight(0.8 * appWidth);

        //缩小按钮
        minimizeButton.setPrefSize(0.8 * appWidth, 0.8 * appWidth);
        minimizeButton.setMinSize(0.8 * appWidth, 0.8 * appWidth);
        minimizeButton.setMaxSize(0.8 * appWidth, 0.8 * appWidth);
        minimizeButton.resize(0.8 * appWidth, 0.8 * appWidth);
        ((ImageView) minimizeButton.getGraphic()).setFitWidth(0.8 * appWidth);
        ((ImageView) minimizeButton.getGraphic()).setFitHeight(0.8 * appWidth);

        //关闭按钮
        closeButton.setPrefSize(0.8 * appWidth, 0.8 * appWidth);
        closeButton.setMinSize(0.8 * appWidth, 0.8 * appWidth);
        closeButton.setMaxSize(0.8 * appWidth, 0.8 * appWidth);
        closeButton.resize(0.8 * appWidth, 0.8 * appWidth);
        ((ImageView) closeButton.getGraphic()).setFitWidth(0.8 * appWidth);
        ((ImageView) closeButton.getGraphic()).setFitHeight(0.8 * appWidth);
        //垃圾桶按钮
        deskButton.setPrefSize(0.8 * appWidth, 0.8 * appWidth);
        deskButton.setMinSize(0.8 * appWidth, 0.8 * appWidth);
        deskButton.setMaxSize(0.8 * appWidth, 0.8 * appWidth);
        deskButton.resize(0.8 * appWidth, 0.8 * appWidth);
        ((ImageView) deskButton.getGraphic()).setFitWidth(0.7 * appWidth);
        ((ImageView) deskButton.getGraphic()).setFitHeight(0.7 * appWidth);
        //appBox
        appBox.setMinHeight(1.5 * appWidth);
        appBox.setMaxHeight(1.5 * appWidth);
        appBox.setPrefHeight(1.5 * appWidth);
        appBox.setPrefWidth(appWidth * 10);
        appBox.setMaxWidth(appWidth * 10);
        appBox.setMinWidth(appWidth * 10);

        appBox.setLayoutX(sceneWidth / 2 - appBox.getWidth() / 2);
        appBox.setLayoutY(0);
        System.out.println(appBox.getWidth() + " " + appBox.getHeight());
        //tipBox
        tipBox.setMinHeight(1.5 * appWidth);
        tipBox.setMaxHeight(1.5 * appWidth);
        tipBox.setPrefHeight(1.5 * appWidth);
        tipBox.setMaxWidth(closeButton.getWidth() * 6);
        tipBox.setMinWidth(closeButton.getWidth() * 6);
        tipBox.setPrefWidth(closeButton.getWidth() * 6);
        tipBox.setLayoutX(sceneWidth - tipBox.getWidth() - 2 * appWidth);
        tipBox.setLayoutY(0);
        //timeBox
        timeBox.setMinHeight(1.5 * appWidth);
        tipBox.setMaxHeight(1.5 * appWidth);

        System.out.println(tipBox.getWidth() + " " + tipBox.getHeight());


    }


    public static void removeStage(String name)
    {
        for(int i=0;i<stageList.size();i++)
        {
            if(stageList.get(i).name.equals(name))
            {
                stageList.remove(i);
            }
        }
    }
    public void fileAppOpen() throws Exception
    {
        Stage stage = checkStage("apps/fileApp");

        if (stage != null && stage.isShowing() == false)
        {
            removeStage("apps/fileApp");

        }

//        FileApp fileApp = new FileApp();

        stage = checkStage("apps/fileApp");
        if (stage == null)
        {
            stage = new Stage();

            new MainUI(stage);
            stageList.add(new StageRecord("apps/fileApp", stage));
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("apps/fileApp");

        fileManagerButton.setUnderline(true);
        fileManagerButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius: 20;\n"+
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }





    public void helpAppOpen()
    {
        Stage stage = checkStage("apps/helpApp");

        if (stage != null && stage.isShowing() == false)
        {
            removeStage("apps/helpApp");

        }

        HelpApp helpApp = new HelpApp();

        stage = checkStage("apps/helpApp");
        if (stage == null)
        {
            try
            {
                stage = new Stage();

                double ratioW=primaryStage.getWidth()/1920;
                double ratioH=primaryStage.getHeight()/1080;

                stage.setMinHeight(500);
                stage.setMaxHeight(primaryStage.getHeight() - 2 * appWidth);

                stage.setMinWidth(800);
                stage.setMaxWidth(primaryStage.getWidth());


                stage.setWidth(800);
                stage.setHeight(500);

                helpApp.start(stage);
                stageList.add(new StageRecord("apps/helpApp", stage));

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }


        stage.setAlwaysOnTop(true);
        stage.setIconified(false);

        stage.toFront();
        updateStageList("apps/helpApp");


        helpButton.setUnderline(true);
        helpButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius:20;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }




    public void  occupancyAppOpen()
    {
        Stage stage = checkStage("apps/occupancyApp");

        if (stage != null && stage.isShowing() == false)
        {
            removeStage("apps/occupancyApp");
        }

        OccupancyApp occupancyApp = new OccupancyApp();
        stage = checkStage("apps/occupancyApp");
        if (stage == null)
        {
            try
            {
                stage = new Stage();


                double ratioW=primaryStage.getWidth()/1920;
                double ratioH=primaryStage.getHeight()/1080;

                stage.setMinHeight(530);
                stage.setMaxHeight(primaryStage.getHeight() - 2 * appWidth);

                stage.setMinWidth(1000);
                stage.setMaxWidth(primaryStage.getWidth());

                stage.setWidth(1000);
                stage.setHeight(530);


                occupancyApp.start(stage);
                stageList.add(new StageRecord("apps/occupancyApp", stage));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {


                occupancyButton.setUnderline(false);
                occupancyButton.setStyle("");
            }
        });


        stage.setAlwaysOnTop(true);
        stage.setIconified(false);
        stage.toFront();
        updateStageList("apps/occupancyApp");

        occupancyButton.setUnderline(true);
        occupancyButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius: 20;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }




    public void processAppOpen()
    {
        Stage stage = checkStage("apps/processApp");

        if (stage != null && stage.isShowing() == false)
        {
            removeStage("apps/processApp");
        }

        ProcessApp processApp = new ProcessApp();

        stage = checkStage("apps/processApp");
        if (stage == null)
        {
            try
            {
                stage = new Stage();

                double ratioW=primaryStage.getWidth()/1920;
                double ratioH=primaryStage.getHeight()/1080;

                stage.setMinHeight(500);
                stage.setMaxHeight(primaryStage.getHeight() - 2 * appWidth);


                stage.setMinWidth(1100);
                stage.setMaxWidth(primaryStage.getWidth());

                stage.setWidth(1100);
                stage.setHeight(500);


                processApp.start(stage);
                stageList.add(new StageRecord("apps/processApp", stage));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);

        stage.toFront();
        updateStageList("apps/processApp");
        processButton.setUnderline(true);
        processButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius: 20;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }


    public void executableFileAppOpen()
    {
        Stage executableFileStage = checkStage("apps/systemFileApp");

        if (executableFileStage != null && executableFileStage.isShowing() == false)
        {
            removeStage("apps/systemFileApp");

        }

        SystemFileApp systemFileApp = new SystemFileApp();

        Stage stage = checkStage("apps/systemFileApp");
        if (stage == null)
        {
            try
            {
                stage = new Stage();

                double ratioW=primaryStage.getWidth()/1920;
                double ratioH=primaryStage.getHeight()/1080;

                stage.setMinHeight(500);
                stage.setMaxHeight(primaryStage.getHeight() - 2 * appWidth);

                stage.setMinWidth(500);
                stage.setMaxWidth(primaryStage.getWidth());


                stage.setWidth(500);
                stage.setHeight(500);



                systemFileApp.start(stage);
                stageList.add(new StageRecord("apps/systemFileApp", stage));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }


        stage.setAlwaysOnTop(true);
        stage.setIconified(false);

        stage.toFront();
        updateStageList("apps/systemFileApp");

        executableFileButton.setUnderline(true);
        executableFileButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius: 20;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
    }

    public  static Stage checkStage(String name)
    {
        for(int i=0;i<stageList.size();i++)
        {
            if(stageList.get(i).name.equals(name)==true)
            {
                return stageList.get(i).stage;
            }
        }
        return  null;
    }
    public static void updateStageList(String name)
    {
        for(int i=0;i<stageList.size();i++)
        {
            if(stageList.get(i).name.equals(name)==true)
            {
                StageRecord stageRecord=stageList.get(i);
                stageList.remove(stageRecord);
                stageList.add(stageRecord);
                return;
            }
        }
        return ;
    }
    public void broswerAppOpen()
    {
        Stage browserStage = checkStage("apps/browserApp");
        if (browserStage != null && browserStage.isShowing() == false)
        {
            removeStage("apps/browserApp");

        }

        BrowserApp browserApp = new BrowserApp();
        Stage stage = checkStage("apps/browserApp");
        if (stage == null)
        {
            try
            {
                stage = new Stage();

                stageList.add(new StageRecord("apps/browserApp",stage));


                stage.setMinHeight(primaryStage.getHeight()/2 );
                stage.setMaxHeight(primaryStage.getHeight() - 2 * appWidth);

                stage.setMinWidth(primaryStage.getWidth()*0.7);
                stage.setMaxWidth(primaryStage.getWidth());


                stage.setHeight(primaryStage.getHeight()/2);
                stage.setWidth(primaryStage.getWidth()*0.7);

                stage.setX(primaryStage.getWidth()*0.1);
                stage.setY(primaryStage.getHeight()*0.25);

                browserApp.start(stage);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (stage != null && stage.isShowing() == true)
        {
            stage.show();
        }

        stage.setAlwaysOnTop(true);
        stage.setIconified(false);

        stage.toFront();
        updateStageList("apps/browserApp");
        broswerButton.setUnderline(true);
        broswerButton.setStyle("    -fx-background-color: transparent,     aliceblue;\n" +
                "    -fx-background-radius: 20;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");

    }

    public void timeInit()
    {
        Date date = new Date();//创建Date对象
        String hour = String.format("%tH", date);//格式化输出时间
        String minute = String.format("%tM", date);
        String second = String.format("%tS", date);
        String year = String.format("%ty", date);
        String month = String.format("%tm", date);
        String day = String.format("%td", date);
//                System.out.println("现在是：" + hour + "时" +minute+"分" + second +"秒");
        timeButton1.setMinWidth(1.5 * appWidth);
        timeButton1.setMaxWidth(1.5 * appWidth);

        timeButton2.setMinWidth(1.5 * appWidth);
        timeButton2.setMaxWidth(1.5 * appWidth);

        timeButton1.setText(hour + ":" + minute);
        timeButton2.setText("20" + year + "/" + month + "/" + day);


    }

    public void uiThreadInit()
    {
        uiThread.timeButton1=timeButton1;
        uiThread.timeButton2=timeButton2;
        uiThread.init();
        uiThread.start();
    }


    public void proceessThreadInit()
    {
        ProcessManager.init();
        processScheduleThread.init();
        processScheduleThread.start();
    }

}

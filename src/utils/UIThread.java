package utils;

import apps.fileApp.app.MainUI;
import datas.InstructionData;
import datas.ProcessDetailData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.MainController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class UIThread extends Thread
{

    //主界面
    public static Button timeButton1 = null;
    public static Button timeButton2 = null;

    //进程管理器
    public static ArrayList<ProcessDetailData> processDetailDataArrayList = new ArrayList<>();
    public static TableView<ProcessDetailData> processTable = null;
    public static TableView<InstructionData> nowProcessTable = null;
    public static ArrayList<InstructionData> instructionDataArrayList = new ArrayList<>();
    public static Button nowProcessName = null;
    public static Button nowResult = null;
    public static Button residueSlice = null;

    public Vector<Process> runProcessList;
    public Process runProcess = null;
    public Vector<Process> creatingProcessList;

    public Vector<Process> waitProcessList;
    public Vector<Process> blockProcessList;


    public static Button nowInstruction = null;
    public static CheckBox[] checkBoxes1 = null;
    public static CheckBox[] checkBoxes2 = null;

    //占用管理
    public static VBox[] boxes1 = null;
    public static HBox[] boxes2 = null;

    public static Button[] textbuttons = null;

    public static Button[] mainButtons = null;
    public static Vector<StageRecord> stageList = null;

    public void init()
    {

    }

    public void mainButtonsUpdate()
    {

        if (mainButtons == null || stageList == null)
        {
            return;
        }
        //        Button[] buttons={broswerButton,executableFileButton,fileManagerButton,processButton,occupancyButton,helpButton};
        Stage stage = MainController.checkStage("apps/browserApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[0].setStyle("");
        }
        stage =  MainController.checkStage("apps/systemFileApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[1].setStyle("");
        }
        stage =  MainController.checkStage("apps/fileApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[2].setStyle("");
        }
        stage =  MainController.checkStage("apps/processApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[3].setStyle("");
        }
        stage =  MainController.checkStage("apps/occupancyApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[4].setStyle("");
        }
        stage =  MainController.checkStage("apps/helpApp");
        if (stage != null && stage.isShowing() == false)
        {
            mainButtons[5].setStyle("");
        }

    }


    //////////////////////////////////占用管理器的界面更新////////////////////////////////////////
    public void occupancyAppUpdate()
    {
        occupancyTextUpdate();
        occupancyBoxes1Update();
        occupancyBoxes2Update();
    }

    //占用管理器界面的文本更新
    public void occupancyTextUpdate()
    {
        //如果文本按钮数组还没有初始化，先不更新了。
        if (textbuttons == null)
            return;

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                //内存占用比
                int num = 0;
                int all = OccupancyManager.allMemory.length;
                for (int i = 0; i < all; i++)
                {
                    if (OccupancyManager.allMemory[i] == 1)
                    {
                        num++;
                    }
                }

                // 更新文本
                double percent = num * 1.0 / all * 100;
                String result = String.format("%.2f", percent);
                textbuttons[0].setText(num + "B/" + all + "B" + "(" + result + "%)");


                //磁盘占用比
                if (MainUI.fat != null)
                {
                    num = 0;
                    all = 256;
                    for (int i = 0; i < 256; i++)
                    {
                        if (MainUI.fat.getDiskBlocks()[i].getIndex() != 0)
                            num++;
                    }

                    // 更新文本
                    percent = num * 1.0 / all * 100;
                    result = String.format("%.2f", percent);
                    textbuttons[1].setText(num + "/" + all + "" + "(" + result + "%)");
                }
                //设备占用比
                num = 0;
                all = OccupancyManager.aDevice.length + OccupancyManager.bDevice.length + OccupancyManager.cDevice.length;

                for (int i = 0; i < OccupancyManager.aDevice.length; i++)
                {
                    if (OccupancyManager.aDevice[i] == 1)
                    {
                        num++;
                    }
                }
                for (int i = 0; i < OccupancyManager.bDevice.length; i++)
                {
                    if (OccupancyManager.bDevice[i] == 1)
                    {
                        num++;
                    }
                }
                for (int i = 0; i < OccupancyManager.cDevice.length; i++)
                {
                    if (OccupancyManager.cDevice[i] == 1)
                    {
                        num++;
                    }
                }


                // 更新文本
                percent = num * 1.0 / all * 100;
                result = String.format("%.2f", percent);
                textbuttons[2].setText(num + "/" + all + "" + "(" + result + "%)");

                //PCB占用比
                num = 10 - OccupancyManager.freePcbList.size();
                all = 10;

                // 更新文本
                percent = num * 1.0 / all * 100;
                result = String.format("%.2f", percent);
                textbuttons[3].setText(num + "/" + all + "" + "(" + result + "%)");
            }
        });

    }

    //占用管理器第一个矩形框更新
    public void occupancyBoxes1Update()
    {
        //如果第一个矩形框数组还没有初始化，先不更新了。
        if (boxes1 == null)
        {
            return;
        }

        //内存占用比
        int num = 0;
        int all = OccupancyManager.allMemory.length;
        for (int i = 0; i < all; i++)
        {
            if (OccupancyManager.allMemory[i] == 1)
            {
                num++;

            }
        }
        // 矩形框中有一个Region区域,是实心的，通过调整大小即可。
        double percent = num * 1.0 / all;
        double hight = boxes1[0].getHeight() - 2;
        double width = boxes1[0].getWidth() - 2;
        Region region = (Region) boxes1[0].getChildren().get(0);
        percent = num * 1.0 / all;
        region.setPrefSize(width, percent * hight);
        region.setMinSize(width, percent * hight);
        region.setMaxSize(width, percent * hight);

        //磁盘占用比
        if (MainUI.fat != null)
        {
            num = 0;
            for (int i = 0; i < 256; i++)
            {
                if (MainUI.fat.getDiskBlocks()[i].getIndex() != 0)
                    num++;
            }

            percent = num * 1.0 / 256;
            System.out.println("usedNum:" + num);
            hight = boxes1[1].getHeight() - 2;
            width = boxes1[1].getWidth() - 2;
            region = (Region) boxes1[1].getChildren().get(0);
            percent = num * 1.0 / 256;
            region.setPrefSize(width, percent * hight);
            region.setMinSize(width, percent * hight);
            region.setMaxSize(width, percent * hight);
        }
        //设备占用比
        num = 0;
        all = OccupancyManager.aDevice.length + OccupancyManager.bDevice.length + OccupancyManager.cDevice.length;

        for (int i = 0; i < OccupancyManager.aDevice.length; i++)
        {
            if (OccupancyManager.aDevice[i] == 1)
            {
                num++;
            }
        }
        for (int i = 0; i < OccupancyManager.bDevice.length; i++)
        {
            if (OccupancyManager.bDevice[i] == 1)
            {
                num++;
            }
        }
        for (int i = 0; i < OccupancyManager.cDevice.length; i++)
        {
            if (OccupancyManager.cDevice[i] == 1)
            {
                num++;
            }
        }


        // 矩形框中有一个Region区域,是实心的，通过调整大小即可。
        percent = num * 1.0 / all;
        hight = boxes1[2].getHeight() - 2;
        width = boxes1[2].getWidth() - 2;
        region = (Region) boxes1[2].getChildren().get(0);
        region.setPrefSize(width, percent * hight);
        region.setMinSize(width, percent * hight);
        region.setMaxSize(width, percent * hight);


        //PCB占用比
        num = 10 - OccupancyManager.freePcbList.size();
        all = 10;
        // 矩形框中有一个Region区域,是实心的，通过调整大小即可。
        percent = num * 1.0 / all;
        hight = boxes1[3].getHeight() - 2;
        width = boxes1[3].getWidth() - 2;
        region = (Region) boxes1[3].getChildren().get(0);
        region.setPrefSize(width, percent * hight);
        region.setMinSize(width, percent * hight);
        region.setMaxSize(width, percent * hight);

    }

    //占用管理器的第二个矩形框更新
    public void occupancyBoxes2Update()
    {
        //如果第二个矩形框数组还没有初始化，先不更新了。
        if (boxes2 == null)
        {
            return;
        }

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                //内存
                //css中已经设置了样式，设置id，即可更新界面显示，具体可看源文件
                //emptyBox：白色盒子
                //memoryInBox1：涂色盒子
                int all = OccupancyManager.allMemory.length;
                for (int i = 0; i < all; i++)
                {
                    if (OccupancyManager.allMemory[i] == 0)
                    {

                        boxes2[0].getChildren().get(i).setId("emptyBox");
                    }
                    else
                    {
                        boxes2[0].getChildren().get(i).setId("memoryInBox1");
                    }
                }


                //磁盘
                //磁盘占用比
                if (MainUI.fat != null)
                {
                    for (int i = 0; i < 256; i++)
                    {
                        if (MainUI.fat.getDiskBlocks()[i].getIndex() != 0)
                        {
                            boxes2[1].getChildren().get(i).setId("diskInBox1");
                        }
                        else
                        {
                            boxes2[1].getChildren().get(i).setId("emptyBox");
                        }
                    }
                }


            //设备
            //css中已经设置了样式，设置id，即可更新界面显示，具体可看源文件
            //emptyBox：白色盒子
            //deviceInBox1：涂色盒子
            for(int i = 0;i<OccupancyManager.aDevice.length;i++)
            {
                if (OccupancyManager.aDevice[i] == 0)
                {
                    boxes2[2].getChildren().get(i).setId("emptyBox");
                }
                else
                {
                    boxes2[2].getChildren().get(i).setId("deviceInBox1");
                }
            }
                for(
            int i = 0;
            i<OccupancyManager.bDevice.length;i++)

            {
                if (OccupancyManager.bDevice[i] == 0)
                {
                    boxes2[2].getChildren().get(2 + i).setId("emptyBox");
                }
                else
                {
                    boxes2[2].getChildren().get(2 + i).setId("deviceInBox1");
                }
            }
                for(
            int i = 0;
            i<OccupancyManager.cDevice.length;i++)

            {
                if (OccupancyManager.cDevice[i] == 0)
                {
                    boxes2[2].getChildren().get(5 + i).setId("emptyBox");
                }
                else
                {
                    boxes2[2].getChildren().get(5 + i).setId("deviceInBox1");
                }
            }

            //pcb
            //css中已经设置了样式，设置id，即可更新界面显示，具体可看源文件
            //emptyBox：白色盒子
            //pcbInBox0：涂色盒子0
            //pcbInBox1：涂色盒子1
            //pcbInBox2：涂色盒子2
            //pcbInBox3：涂色盒子3
            int pcbId;
            //初始化
                for(
            int i = 0;
            i< 10;i++)

            {
                boxes2[3].getChildren().get(i).setId("emptyBox");
            }
            //就绪
                for(
            int i = 0; i<creatingProcessList.size();i++)

            {
                pcbId = creatingProcessList.get(i).pcbID;
                if (pcbId >= 0 && pcbId <= 9)
                {
                    boxes2[3].getChildren().get(pcbId).setId("pcbInBox0");
                }
            }

                for(
            int i = 0; i<waitProcessList.size();i++)

            {
                pcbId = waitProcessList.get(i).pcbID;
                if (pcbId >= 0 && pcbId <= 9)
                {
                    boxes2[3].getChildren().get(pcbId).setId("pcbInBox1");

                }
            }
            //运行
                if(runProcess !=null)

            {
                pcbId = runProcess.pcbID;
                if (pcbId >= 0 && pcbId <= 9)
                {
                    boxes2[3].getChildren().get(pcbId).setId("pcbInBox2");

                }
            }
            //阻塞
                for(
            int i = 0; i<blockProcessList.size();i++)

            {

                pcbId = blockProcessList.get(i).pcbID;
                if (pcbId >= 0 && pcbId <= 9)
                {
                    boxes2[3].getChildren().get(pcbId).setId("pcbInBox3");

                }
            }


        }
    });
}

//////////////////////////////////占用管理器的界面更新////////////////////////////////////////


    public void nowProcessUpdate()
    {
        if (nowProcessName == null || nowResult == null || nowInstruction == null || residueSlice == null)
        {
            return;
        }
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Update UI here.
                if (runProcess == null)
                {
                    nowProcessName.setText("");
                    nowResult.setText("");
                    nowInstruction.setText("");
                    residueSlice.setText("");
                }
                else
                {
                    nowProcessName.setText(runProcess.name + "");
                    nowResult.setText(runProcess.AX + "");
                    int counter = runProcess.counter;
                    if (counter >= runProcess.executableFile.instructionArray.size())
                    {
                        counter--;
                    }
                    nowInstruction.setText(runProcess.executableFile.getInstructionArray().get(counter) + "");
                    residueSlice.setText(ProcessScheduleThread.residueSlice + "");

                }

            }
        });

    }

    public void processTableUpdate()
    {
        if (processTable == null)
        {
            return;
        }
        // UIThread.checkBoxes1=new CheckBox[]{showNow, showCreating, showWaiting, showBlocked,showEnded,showAll};
        Vector<Process> updateList = null;
        String selectString = "";
        if (checkBoxes1[0].isSelected())//显示当前
        {
            selectString = checkBoxes1[0].getText();
            updateList = (Vector<Process>) ProcessManager.allProcessList.clone();
        }
        else if (checkBoxes1[1].isSelected())//新建进程
        {
            selectString = checkBoxes1[1].getText();
            updateList = (Vector<Process>) ProcessManager.creatingProcessList.clone();
        }
        else if (checkBoxes1[2].isSelected())//就绪进程
        {
            selectString = checkBoxes1[2].getText();
            updateList = (Vector<Process>) ProcessManager.waitProcessList.clone();
        }
        else if (checkBoxes1[3].isSelected())//阻塞进程
        {
            selectString = checkBoxes1[3].getText();
            updateList = (Vector<Process>) ProcessManager.blockProcessList.clone();
        }
        else if (checkBoxes1[4].isSelected())//销毁进程
        {
            selectString = checkBoxes1[4].getText();
            updateList = (Vector<Process>) ProcessManager.allProcessList.clone();
        }
        else if (checkBoxes1[5].isSelected())//显示所有
        {
            selectString = checkBoxes1[5].getText();
            updateList = (Vector<Process>) ProcessManager.allProcessList.clone();
        }

        DataLoader.processDetailDataLoad(processDetailDataArrayList, updateList, selectString);


        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                processTable.setItems(FXCollections.observableArrayList(processDetailDataArrayList));
            }
        });


        processTable.setRowFactory(row -> new TableRow<ProcessDetailData>()
        {
            @Override
            public void updateItem(ProcessDetailData item, boolean empty)
            {

                super.updateItem(item, empty);
                String s = "运行态";
                if (checkBoxes2 != null)
                {
                    for (int i = 0; i < checkBoxes2.length; i++)
                    {
                        if (checkBoxes2[i].isSelected())
                        {
                            if (i == 0)
                            {
                                s = "新建态";
                            }
                            else if (i == 1)
                            {
                                s = "就绪态";
                            }
                            else if (i == 2)
                            {
                                s = "运行态";
                            }
                            else if (i == 3)
                            {
                                s = "阻塞态";
                            }
                            else if (i == 4)
                            {
                                s = "已销毁";
                            }
                            break;
                        }
                    }
                }

                if (item == null)
                {
                    this.setId("not-right");
                }
                else if (item.getProcessState() == s)
                {
                    this.setId("right");
                }
                else
                {
                    this.setId("not-right");
                }
            }
        });
    }

    public void nowProcessTableUpdate()
    {
        if (nowProcessTable == null)
        {
            return;
        }
        if (runProcess != null)
        {
            DataLoader.fileDetailDataLoad(instructionDataArrayList, runProcess.executableFile);
//            nowProcessTable.get

            nowProcessTable.setRowFactory(row -> new TableRow<InstructionData>()
            {
                @Override
                public void updateItem(InstructionData item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (item == null)
                    {
                        this.setId("not-right");
                    }
                    else if (item.which == runProcess.counter)
                    {
                        this.setId("right");
                    }
                    else
                    {
                        this.setId("not-right");
                    }
                }
            });

        }
        else
        {
            instructionDataArrayList.clear();
        }

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                nowProcessTable.setItems(FXCollections.observableArrayList(instructionDataArrayList));

            }
        });


    }


    public void timeUpdate()
    {
        if (timeButton1 == null || timeButton2 == null)
        {
            return;
        }
        Date date = new Date();//创建Date对象
        String hour = String.format("%tH", date);//格式化输出时间
        String minute = String.format("%tM", date);
        String second = String.format("%tS", date);
        String year = String.format("%ty", date);
        String month = String.format("%tm", date);
        String day = String.format("%td", date);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Update UI here.
                timeButton1.setText(hour + ":" + minute);
                timeButton2.setText("20" + year + "/" + month + "/" + day);
            }
        });

    }

    public void run()
    {
        while (true)
        {
            try
            {
                sleep(ProcessManager.slice / ProcessManager.speed);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            mainButtonsUpdate();

            runProcessList = (Vector<Process>) ProcessManager.runProcessList.clone();
            runProcess = null;

            creatingProcessList = (Vector<Process>) ProcessManager.creatingProcessList.clone();

            waitProcessList = (Vector<Process>) ProcessManager.waitProcessList.clone();

            blockProcessList = (Vector<Process>) ProcessManager.blockProcessList.clone();

            if (runProcessList.size() != 0)
            {
                runProcess = runProcessList.get(0);
            }
            else
            {
                Instruction instruction = new Instruction(-1);
                Vector<Instruction> instructionArrayList = new Vector<>();
                instructionArrayList.add(instruction);
                runProcess = new Process(-1, new ExecutableFile(-1, instructionArrayList), -1);
                runProcess.counter = 0;

            }

            timeUpdate();

            processTableUpdate();
            nowProcessTableUpdate();
            nowProcessUpdate();

            occupancyAppUpdate();
        }
    }
}

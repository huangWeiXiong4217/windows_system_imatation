package utils;

import javafx.scene.control.CheckBox;

import java.util.*;

import static utils.ProcessManager.*;

public class ProcessScheduleThread extends Thread
{
    //可执行文件队列
    public static Vector<ExecutableFile> executableFileList;
    //空闲的pcb编号队列
    public static Vector<Integer> freePcbList;
    //所有进程表
    public static Vector<Process> allProcessList;
    //创建中的进程
    public static Vector<Process> creatingProcessList;
    //正在运行的进程
    public static Vector<Process> runProcessList;
    //就绪进程
    public static Vector<Process> waitProcessList;
    //阻塞进程
    public static Vector<Process> blockProcessList;
    //倍速
    public static int speed;
    //已创建进程数
    public static int processNum;
    //时间片:0.1秒
    public static int slice;
//    总时间片:时间片长度为 6
    public static int sliceLength;


    //用户区内存条
    public static int[] allMemory;
    //设备：A--0、1
    public static int[] aDevice;
    //设备：B--0、1、2
    public static int[] bDevice;
    //设备：C--0、1、2
    public static int[] cDevice;
    public static  int residueSlice=0;

    //创建控制按钮
    public static CheckBox[] controlButton=null;



    public void init()
    {
        //可执行文件队列
        executableFileList = ProcessManager.executableFileList;
        //空闲的pcb编号队列
        freePcbList = OccupancyManager.freePcbList;
        //所有进程表
        allProcessList = ProcessManager.allProcessList;
        //创建中的进程
        creatingProcessList = ProcessManager.creatingProcessList;
        //正在运行的进程
        runProcessList = ProcessManager.runProcessList;
        //就绪进程
        waitProcessList = ProcessManager.waitProcessList;
        //阻塞进程
        blockProcessList = ProcessManager.blockProcessList;

        //用户区内存条
        allMemory = OccupancyManager.allMemory;
        //设备：A--0、1
        aDevice = OccupancyManager.aDevice;
        //设备：B--0、1、2
        bDevice = OccupancyManager.bDevice;
        //设备：C--0、1、2
        cDevice = OccupancyManager.cDevice;

        //时间片:0.1秒
        slice = ProcessManager.slice;
        //倍速
        speed = ProcessManager.speed;
        //总时间片:时间片长度为 6
        sliceLength = 6;
        //已创建进程数
        processNum = ProcessManager.processNum;
    }




    //创建新进程
    public  void createProcess()
    {
        //暂停创建
        if(controlButton!=null&&controlButton.length>=2&&controlButton[1].isSelected())
        {
            return;
        }
        if(executableFileList.size()==0)
        {
            return;
        }

        //新建态进程达到峰值，不再新建
        if(creatingProcessList.size()>=3)
        {
            return;
        }

        Random random=new Random();
        //随机选择一个执行文件
        Vector<ExecutableFile> executableFiles= (Vector<ExecutableFile>) executableFileList.clone();
        int num=random.nextInt(executableFiles.size());//0-9
       ExecutableFile executableFile=executableFiles.get(num);
        int processId=processNum;
        //新建进程
        Process newProcess=new Process(processNum,executableFileList.get(num),executableFile.id);
        //加入新建进程队列
        creatingProcessList.add(newProcess);
        allProcessList.add(newProcess);
        processNum++;

        //进行新建工作：分配pcb、分配内存
        newProcess.create();
    }


    //以RR调度：时间片轮转算法调度
    public void updateByRR()
    {
       while(true)
       {
           createProcess();

           if(runProcessList.size()==0&&waitProcessList!=null&&!waitProcessList.isEmpty())
           {
               System.out.println("就绪队列个数："+waitProcessList.size());
               try{
                   Process nowProcess = waitProcessList.remove(0);
                   nowProcess.state = 2;//转运行
                   runProcessList.add(nowProcess);
               }
               catch (Exception exception)
               {
                   System.out.println("就绪队列个数："+0);

               }

           }
           if(runProcessList.size()==0)
           {
                continue;
           }
           Process nowProcess=runProcessList.get(0);
           for(int i=0;i<sliceLength;i++)
           {
               residueSlice=sliceLength-i;
               try
               {
                   sleep(ProcessManager.slice/ProcessManager.speed);
               }
               catch (InterruptedException e)
               {
                   e.printStackTrace();
               }
               //程序结束
               if(nowProcess.counter==nowProcess.executableFile.instructionArray.size())
               {
                   nowProcess.destroy();
                   break;
               }
               nowProcess.cpu();
               if(nowProcess.state==3)
               {
                   break;
               }
           }

           if(nowProcess!=null)
           {

               runProcessList.remove(nowProcess);

               if(nowProcess.state!=3&&nowProcess.state!=-1)
               {
                   //转就绪
                   nowProcess.state=1;
                   waitProcessList.add(nowProcess);
               }
           }



       }



    }
    public void run()
    {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkCreatingProcessList();
                checkBlockProcessList();
            }
        }, 0, 1000);// 0ms之后开始执行，每隔1000ms执行一次

        updateByRR();
    }


    public static  void main(String[] args)
    {
//        ProcessScheduleThread processScheduleThread=new ProcessScheduleThread();
//        processScheduleThread.init();
//        //新建进程
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                processScheduleThread.createProcess();
//            }
//        },0,1000);
//        //进程调度
//        processScheduleThread.start();
    }
}

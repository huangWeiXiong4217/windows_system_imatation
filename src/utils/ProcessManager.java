package utils;


import javafx.stage.Stage;

import java.util.*;


//进程管理器
public class ProcessManager
{
    //可执行文件队列
    public static  Vector<ExecutableFile> executableFileList = new Vector<>();
    //所有进程表
    public static  Vector<Process> allProcessList = new Vector<>();
    //新建进程
    public static  Vector<Process> creatingProcessList = new Vector<>();
    //正在运行的进程
    public static  Vector<Process> runProcessList = new Vector<>();
    //就绪进程
    public static  Vector<Process> waitProcessList = new Vector<>();
    //阻塞进程
    public static  Vector<Process> blockProcessList = new Vector<>();



    //时间片:1秒
    public static int slice = 1000;
    //倍速
    public static int speed = 1;
    //已创建进程数
    public static int processNum = 0;

    public static void init()
    {
        //随机生成可执行文件
        ProcessManager.createRandomExecuteFile(executableFileList, 10);

    }

    public static void createRandomExecuteFile(Vector<ExecutableFile> executeFileList, int num)
    {
        for (int i = 0; i < num; i++)
        {
            ExecutableFile executableFile = new ExecutableFile(i);
            executeFileList.add(executableFile);

        }
    }

    public static void checkCreatingProcessList()
    {
        for (int i = 0; i < allProcessList.size(); i++)
        {
            if(allProcessList.get(i).state!=0)
                continue;
            Process nowProcess = allProcessList.get(i);
            nowProcess.create();
        }
    }

    //检测阻塞进程
    public static void checkBlockProcessList()
    {

        for (int i = 0; i < allProcessList.size(); i++)
        {
            if (allProcessList.get(i).state != 3)
            {
                continue;
            }
            Process nowProcess = allProcessList.get(i);
            //因无设备而阻塞
            if (nowProcess.deviceId == -1)
            {
                //重新申请设备
                int deviceId = OccupancyManager.applyDevice(nowProcess.device);
                //申请失败
                if (deviceId == -1)
                {
                    continue;
                }
                //申请成功
                else
                {
                    nowProcess.deviceId = deviceId;
                }
            }
            else
            {
                ProcessManager.useDevice(nowProcess);
            }

        }


    }

    //使用设备
    public static void useDevice(Process process)
    {

        if (process.deviceRemainTime == 0)
        {
            OccupancyManager.retrieveDevice(process.device, process.deviceId);

            process.awake();
            return;
        }
        process.deviceRemainTime--;

    }


}

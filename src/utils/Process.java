package utils;

import java.util.Timer;

public class Process
{
    public int name;//进程标识符
    public int state;//进程状态:0-新建、1-就绪、2-运行、3-阻塞、-1：已销毁
    public int counter;//程序计数器
    public int AX;//主要寄存器
    public int pcbID;//对应进程控制块的ID,-1则不占用
    public ExecutableFile executableFile;//执行文件绑定
    public int memory;//占用内存数
    public MemoryArea memoryArea;//占用内存区
    public int device;//占用设备:-1-无、0-A、1-B、2-C
    public int deviceId;//占用设备第几个
    public int deviceRemainTime;//设备占用剩余时间
    public int whichFile=-1;
    public Process(int name, ExecutableFile executableFile, int whichFile)
    {
        //初始化
        this.name = name;
        this.state = 0;
        this.counter = 0;
        this.AX = 0;
        this.pcbID = -1;
        this.executableFile = executableFile;
        this.memory = executableFile.getInstructionArray().size();
        this.memoryArea = null;
        this.device = -1;
        this.deviceId = -1;
        this.deviceRemainTime = 0;
        this.whichFile=whichFile;
    }

    //进程创建
    public void create()
    {
        if (this.pcbID == -1)
        {
            int newPcb = -1;

            //第一步，申请free进程控制块；
            newPcb = OccupancyManager.applyFreePcb();
            if (newPcb == -1)
            {
                return;
            }
            this.pcbID=newPcb;
        }

        if(this.memoryArea==null)
        {
            MemoryArea newMemoryArea = null;
            //第二步，申请主存空间，申请成功，装入主存；
            newMemoryArea = OccupancyManager.applyMemory(memory);
            if (newMemoryArea == null)
            {
                return;

            }
            this.memoryArea=newMemoryArea;
        }


//        第三步，初始化；
        this.state = 1;
        //移出新建队列
        ProcessManager.creatingProcessList.remove(this);
        //加入就绪队列
        ProcessManager.waitProcessList.add(this);


    }

    //进程撤销
    public boolean destroy()
    {
        this.state = -1;
//        第一步，回收进程所占内存；
        boolean isSuccessed = OccupancyManager.retrieveMemory(this.memoryArea);
        System.out.println(memoryArea.start+" "+memoryArea.end);
        if (!isSuccessed)
        {
            System.out.println("进程销毁失败，进程编号为："+this.name);
            return false;
        }
//        第二步，回收进程控制块；
        OccupancyManager.retrievePcb(pcbID);

//        第三步，在屏幕上显示进程执行结果，进程撤销
        //移除运行队列
        isSuccessed = ProcessManager.runProcessList.remove(this);
        if (!isSuccessed)
        {
            return false;
        }
        return true;
    }

    //进程阻塞
    public void block()
    {
//        第一步，保存运行进程的 CPU 现场；
        //不用保存，跳过
//        第二步，修改进程状态；
        this.state = 3;
//        第三步，将进程链入对应的阻塞队列，然后转向进程调度。
        ProcessManager.runProcessList.remove(this);
        ProcessManager.blockProcessList.add(this);

    }

    //进程唤醒
    public void awake()
    {

//       进程唤醒的主要工作是将进程由阻塞队列中摘下，修改进程 。状态为就绪，然后链入就绪队列。
        this.state = 1;
        this.device=-1;
        this.deviceId=-1;
        ProcessManager.blockProcessList.remove(this);
        ProcessManager.waitProcessList.add(this);


    }


    //进程执行
    public int cpu()
    {
        this.state=2;
        Instruction instruction = executableFile.instructionArray.get(counter);
        counter++;
        if (instruction.category == 0)
        {
            int additional = instruction.additional;
            this.AX = additional;
            return 0;
        }
        else if (instruction.category == 1)
        {
            this.AX++;
            return 0;
        }
        else if (instruction.category == 2)
        {
            this.AX--;
            return 0;
        }
        else if (instruction.category == 3)
        {
            this.device = instruction.additional;
            this.deviceRemainTime = instruction.additional1;
            //申请设备
            int deviceId = OccupancyManager.applyDevice(device);
            //申请失败
            if (deviceId == -1)
            {
                block();//阻塞
            }
            //申请成功
            else
            {
                this.deviceId = deviceId;
                block();
                ProcessManager.useDevice(this);
            }
            return 1;
        }
        else if (instruction.category == 4)
        {
//            checkInterrupt();
        }
        return 0;

    }

}

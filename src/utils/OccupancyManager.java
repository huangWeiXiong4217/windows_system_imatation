package utils;

import java.util.Vector;

//占用管理类
public class OccupancyManager
{
    //用户区内存条
    public static  int[] allMemory=new int[512];
    //设备：A--0、1
    public static  int[] aDevice=new int[2];
    //设备：B--0、1、2
    public static  int[] bDevice=new int[3];
    //设备：C--0、1、2
    public static  int[] cDevice=new int[3];

    //空闲的pcb编号队列
    public static Vector<Integer> freePcbList=new Vector<>();

    public static void init()
    {
        //内存初始化
        for(int i=0;i<512;i++)
        {
            allMemory[i]=0;
        }
        //设备初始化
        for(int i=0;i<2;i++)
        {
            aDevice[i]=0;
        }
        for(int i=0;i<2;i++)
        {
            bDevice[i]=0;
        }
        for(int i=0;i<2;i++)
        {
            cDevice[i]=0;
        }
        //空闲pcb队列初始化
        for(int i=0;i<10;i++)
        {
            freePcbList.add(i);
        }
    }

    //申请空闲pcb：返回空闲pcb编号
    public static int applyFreePcb()
    {

        if(freePcbList!=null&&!freePcbList.isEmpty())
        {
            System.out.println("pcb剩余个数："+freePcbList.size());
            try{
                int pcb=freePcbList.remove(0);
                return pcb;

            }
            catch (Exception exception)
            {
                System.out.println("pcb剩余个数："+0);
                return -1;
            }
        }
        return -1;
    }

    //申请内存：首次适配，返回内存区
    public static MemoryArea applyMemory(int num)
    {
        int series=0;
        for(int i=0;i<allMemory.length;i++)
        {
            if(allMemory[i]==1)
            {
                series=0;
            }
            else
            {
                series++;
            }
            if(series==num)
            {
                int start=i-series+1;
                int end=i;
                for(int j=start;j<=end;j++)
                {
                    allMemory[j]=1;
                }
                System.out.println("分配内存:"+start+" "+end);
                MemoryArea memoryArea=new MemoryArea(start,end);
                return  memoryArea;
            }
        }
        return null;
    }

    //回收内存
    public static boolean retrieveMemory(MemoryArea memoryArea)
    {
        if(memoryArea==null)
        {
            return false;
        }
        int start=memoryArea.start;
        int end =memoryArea.end;

        for(int i=start;i<=end;i++)
        {
            allMemory[i]=0;
        }

        return true;
    }

    //回收pcb
    public static void retrievePcb(int num)
    {
        for(int i=0;i<freePcbList.size();i++)
        {
            if(num==freePcbList.get(i))
            {
//                return;
            }
        }
        freePcbList.add(num);
    }

    //请求设备:0-A、1-B、2-C，返回对应设备的id，返回-1即请求不成功
    public static int applyDevice(int device)
    {
        if(device==0)
        {
            int i;
            for(i=0;i<2;i++)
            {
                if(aDevice[i]==0)
                {
                    break;
                }
            }
            if(i==2)
            {
                return -1;
            }
            aDevice[i]=1;
            return i;
        }
        else if(device==1)
        {
            int i;
            for(i=0;i<3;i++)
            {
                if(bDevice[i]==0)
                {
                    break;
                }
            }
            if(i==3)
            {
                return -1;
            }
            bDevice[i]=1;
            return i;
        }
        else if(device==2)
        {
            int i;
            for(i=0;i<3;i++)
            {
                if(cDevice[i]==0)
                {
                    break;
                }
            }
            if(i==3)
            {
                return -1;
            }
            cDevice[i]=1;
            return i;
        }
        return -1;
    }
    //回收设备
    public static void retrieveDevice(int device,int deviceId)
    {
        if(device==0)
        {
            aDevice[deviceId]=0;
        }
        else if(device==1)
        {
            bDevice[deviceId]=0;

        }
        else if(device==2)
        {
            cDevice[deviceId]=0;
        }
    }


}

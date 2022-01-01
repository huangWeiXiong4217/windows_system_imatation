package utils;

import datas.ExecutableFileData;
import datas.InstructionData;
import datas.ProcessDetailData;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

//数据加载
public class DataLoader
{


    //文件数据加载：原数据清空，得到新数据
    public static ArrayList<ExecutableFileData> fileDataLoad(ArrayList<ExecutableFileData> data, Vector<ExecutableFile> executableFileList)
    {
        data.clear();
        for(int i=0;i<executableFileList.size();i++)
        {


            data.add(new ExecutableFileData(executableFileList.get(i)));

        }
        return data;
    }
    //指令数据加载：原数据清空，得到新数据
    public static ArrayList<InstructionData> fileDetailDataLoad(ArrayList<InstructionData> data, ExecutableFile executableFile)
    {
        data.clear();
        for(int i=0;i<executableFile.instructionArray.size();i++)
        {

            data.add(new InstructionData(executableFile.instructionArray.get(i),i));

        }
        return data;
    }


    public static ArrayList<ProcessDetailData> processDetailDataLoad(ArrayList<ProcessDetailData> data, Vector<Process> processList,String select)
    {
        data.clear();
        if(processList==null)
        {
            return data;
        }

        //新建、就绪、阻塞进程均有一个单独的进程队列
        //其他进程都需要从所有进程队列中进行查找
        //当选择显示 新建、 就绪 、进程 ，将传入其对应的队列
        //其他选择，传入 所有进程队列，再进行筛选
        for(int i=0;i<processList.size();i++)
        {
            if(select.equals("当前进程"))
            {
                //非新建，加入
                if(processList.get(i).state!=-1)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }
            else if(select.equals("显示所有"))
            {
                data.add(new ProcessDetailData(processList.get(i)));
            }
            else if(select.equals("销毁进程"))
            {
                //加入销毁进程
                if(processList.get(i).state==-1)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }
            else if(select.equals("新建进程"))
            {
                if(processList.get(i).state==0)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }
            else if (select.equals("就绪进程"))
            {
                if(processList.get(i).state==1)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }
                else if(select.equals("阻塞进程"))
            {
                if(processList.get(i).state==3)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }
            else if(select.equals("运行进程"))
            {
                //选择运行进程
                if(processList.get(i).state==2)
                {
                    data.add(new ProcessDetailData(processList.get(i)));
                }
            }

        }

        return data;
    }
}

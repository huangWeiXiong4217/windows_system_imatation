package datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;
import utils.Process;

public class ProcessDetailData
{
    public String getProcessName()
    {
        return processName.get();
    }

    public StringProperty processNameProperty()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName.set(processName);
    }

    public String getProcessState()
    {
        return processState.get();
    }

    public StringProperty processStateProperty()
    {
        return processState;
    }

    public void setProcessState(String processState)
    {
        this.processState.set(processState);
    }

    public String getWhichFile()
    {
        return whichFile.get();
    }

    public StringProperty whichFileProperty()
    {
        return whichFile;
    }

    public void setWhichFile(String whichFile)
    {
        this.whichFile.set(whichFile);
    }

    public String getHavedDevice()
    {
        return havedDevice.get();
    }

    public StringProperty havedDeviceProperty()
    {
        return havedDevice;
    }

    public void setHavedDevice(String havedDevice)
    {
        this.havedDevice.set(havedDevice);
    }

    public String getHavedPid()
    {
        return havedPid.get();
    }

    public StringProperty havedPidProperty()
    {
        return havedPid;
    }

    public void setHavedPid(String havedPid)
    {
        this.havedPid.set(havedPid);
    }

    public String getResult()
    {
        return result.get();
    }

    public StringProperty resultProperty()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result.set(result);
    }

    public ProgressBar getProgressBar()
    {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    private StringProperty processName=new SimpleStringProperty();
    private StringProperty processState=new SimpleStringProperty();
    private StringProperty whichFile=new SimpleStringProperty();
    private StringProperty havedDevice=new SimpleStringProperty();

    public String getHavedMemory()
    {
        return havedMemory.get();
    }

    public StringProperty havedMemoryProperty()
    {
        return havedMemory;
    }

    public void setHavedMemory(String havedMemory)
    {
        this.havedMemory.set(havedMemory);
    }

    private StringProperty havedMemory=new SimpleStringProperty();

    private StringProperty havedPid=new SimpleStringProperty();
    private StringProperty result=new SimpleStringProperty();
    private ProgressBar progressBar=new ProgressBar();


    public ProcessDetailData(Process process)
    {
        setProcessName(process.name+"");
        if(process.state==0)
        {
            setProcessState("新建态");
        }
        else if(process.state==1)
        {
            setProcessState("就绪态");
        }
        else if(process.state==2)
        {
            setProcessState("运行态");
        }
        else if(process.state==3)
        {
            setProcessState("阻塞态");
        }
        else if(process.state==-1)
        {
            setProcessState("已销毁");
        }
        setWhichFile("可执行文件"+ process.whichFile);


        if(process.state!=3)
        {
            setHavedDevice("");
        }
        else
        {
            String s="";
            if(process.device==0)
            {
                s="A";
            }
            else if(process.device==1)
            {
                s="B";
            }
            else
            {
                s="C";
            }
            if(process.deviceId==-1)
            {
                setHavedDevice("等待设备"+s);

            }
            else
            {
                setHavedDevice("占用设备"+s);
            }
        }



        if( process.state==-1|| process.state==0)
        {
            setHavedPid("");
        }
        else
        {
            setHavedPid(process.pcbID+"");
        }


        if(process.state!=-1)
        {
            if(process.memoryArea==null)
            {
                setHavedMemory("待分配");
            }
            else
            {
                setHavedMemory("["+process.memoryArea.start+","+process.memoryArea.end+"]");

            }
        }
        else
        {
            setHavedMemory("");

        }







        setResult(process.AX+"");
        double progress= process.counter*1.0/ process.executableFile.instructionArray.size();

        progressBar.setProgress(progress);
        setProgressBar(progressBar);


    }

}

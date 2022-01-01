package datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.ExecutableFile;

//可执行文件的表格数据
public class ExecutableFileData
{

   //文件名
    private StringProperty fileName=new SimpleStringProperty();
    //文件
    ExecutableFile executableFile =null;
    //文件id
    private int id;
    public ExecutableFileData(ExecutableFile executableFile)
    {
        this.id=executableFile.id;
        this.executableFile=executableFile;
        setFileName(executableFile.name+executableFile.addName);
    }

    public String toString()
    {
        return this.id+"";
    }


    public String getFileName()
    {
        return fileName.get();
    }

    public StringProperty fileNameProperty()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName.set(fileName);
    }

}

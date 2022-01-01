package utils;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

//可执行文件
public class ExecutableFile implements Serializable
{

    public int id;
    public Vector<Instruction> instructionArray = new Vector<>();
    public String name;
    public String addName = ".exe";

    // hwx
    public int size;
    private static final long serialVersionUID = 1L;
    //根目录
    public String department = "root";

    public ExecutableFile(int id)
    {
        this.id = id;
        this.name = id + "";
        //随机生成执行指令
        Random random = new Random();
        int instructionNum = 5 + random.nextInt(20);//5-25
        size = instructionNum;
        for (int i = 0; i < instructionNum; i++)
        {
            int which = random.nextInt(4);//0-3
            if (which == 0)//x=?
            {
                int num = random.nextInt(10);//0-9
                instructionArray.add(new Instruction(0, num));
            }
            else if (which == 1)//x++
            {
                instructionArray.add(new Instruction(1));
            }
            else if (which == 2)//x--
            {
                instructionArray.add(new Instruction(2));
            }
            else if (which == 3)//!??
            {
                int num = random.nextInt(3);//0-2---A、B、C
                int num1 = 1 + random.nextInt(5);//1-5
                instructionArray.add(new Instruction(3, num, num1));
            }
        }
        instructionArray.add(new Instruction(4));
    }


    public ExecutableFile(int id, Vector<Instruction> instructionArray)
    {
        this.id = id;
        this.instructionArray = instructionArray;
    }

    public int getId()
    {
        return id;
    }

    public Vector<Instruction> getInstructionArray()
    {
        return instructionArray;
    }

    public String toString()
    {
        String s = "";
        for (int i = 0; i < instructionArray.size(); i++)
        {
            s += instructionArray.get(i).toString();
            s += "\n";
        }
        return s;
    }

    public String getName()
    {
        return name + addName;
    }
}

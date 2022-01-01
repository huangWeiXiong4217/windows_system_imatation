package datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.Instruction;

//指令数据
public class InstructionData
{
    public int which=-1;
    public String getInstruction()
    {
        return instruction.get();
    }

    public StringProperty instructionProperty()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction.set(instruction);
    }

    private StringProperty instruction=new SimpleStringProperty();


    public InstructionData(Instruction instruction, int which)
    {
        setInstruction(instruction.toString());
        this.which=which;
    }
}

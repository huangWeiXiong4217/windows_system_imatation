package utils;

import java.io.Serializable;

public class Instruction implements Serializable
{
    int category;//指令类别--0：x=? 、1：x++、2：x--、3：！？？、4、end   -1 :闲逛
    int additional;//指令中需要的值：用于赋值第一位置
    int additional1;//指令中需要的值：用于赋值第二个位置
  private static final long serialVersionUID = 1L;

    public Instruction (int category)
    {
        this.category=category;
        this.additional=0;
        this.additional1=0;
    }
    public Instruction (int category,int additional)
    {
        this.category=category;
        this.additional=additional;
        this.additional1=0;

    }
    public Instruction (int category,int additional,int additional1)
    {
        this.category=category;
        this.additional=additional;
        this.additional1=additional1;
    }
    public String toString()
    {
        String decode="";


        if(this.category==0)
        {
            decode="x="+this.additional;
        }
        else if(this.category==1)
        {
            decode="x++";
        }
        else if(this.category==2)
        {
            decode="x--";
        }
        else if(this.category==3)
        {
            decode="!";
            if(additional==0)
            {
                decode+="A";
            }
            else if(additional==1)
            {
                decode+="B";
            }
            else if(additional==2)
            {
                decode+="C";
            }
            decode+=additional1;
        }
        else if(this.category==4)
        {
            decode="end";
        }

        if(this.category==-1)
        {
            decode="闲逛";
        }
        return decode;
    }

}

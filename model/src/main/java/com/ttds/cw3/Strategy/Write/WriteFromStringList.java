package com.ttds.cw3.Strategy.Write;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public final class WriteFromStringList extends WriteStrategy<ArrayList<String>>
{
    @Override
    public boolean write(String file, ArrayList<String> list,String encoding)
    {
        boolean success = true;

        try
        {
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));

            for(int i=0;i<list.size();i++)
            {
                bufferedWriter.write(list.get(i)+"\n");
            }

            outputStream.flush();
            bufferedWriter.flush();
            outputStream.close();
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            success = false;
            System.out.printf("写入txt文件 %s 出现问题！\n",file);
            e.printStackTrace();
        }

        return success;
    }
}

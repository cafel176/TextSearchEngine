package com.ttds.cw3.Strategy.Read;

import com.ttds.cw3.Data.JSONAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public final class ReadJSON extends ReadStrategy<JSONAdapter>
{
    @Override
    public JSONAdapter read(String file, String encoding)
    {
        JSONAdapter object = null;

        try
        {
            File f = new File(file);
            Long filelength = f.length();
            byte[] filecontent = new byte[filelength.intValue()];
            FileInputStream in = new FileInputStream(f);
            in.read(filecontent);
            in.close();

            String jsonString = new String(filecontent, encoding);
            object = new JSONAdapter(jsonString);

            return object;
        }
        catch (FileNotFoundException e)
        {
            System.out.printf("文件 %s 不存在！\n",file);
            return null;
        }
        catch (IOException e)
        {
            System.out.printf("读取json文件 %s 出现问题！\n",file);
            e.printStackTrace();

            return null;
        }
    }
}

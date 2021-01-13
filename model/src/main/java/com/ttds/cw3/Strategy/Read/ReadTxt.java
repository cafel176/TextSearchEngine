package com.ttds.cw3.Strategy.Read;

import java.io.*;
import java.util.ArrayList;

public final class ReadTxt extends ReadStrategy<ArrayList<String>>
{
    @Override
    public ArrayList<String> read(String file,String encoding)
    {
        ArrayList<String> list = new ArrayList<String>();

        try
        {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));

            String str;
            while((str = bufferedReader.readLine()) != null)
            {
                list.add(str);
            }

            inputStream.close();
            bufferedReader.close();

            return list;
        }
        catch (FileNotFoundException e)
        {
            System.out.printf("文件 %s 不存在！\n",file);
            return null;
        }
        catch (IOException e)
        {
            System.out.printf("读取txt文件 %s 出现问题！\n",file);
            e.printStackTrace();

            return null;
        }

    }
}

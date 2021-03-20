package com.ttds.cw3.Strategy.Read;

import com.ttds.cw3.Data.Doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class ReadXml extends ReadStrategy<ArrayList<Doc>>
{
    @Override
    public ArrayList<Doc> read(String file, String encoding)
    {
        ArrayList<Doc> list = new ArrayList<Doc>();

        try
        {
            File f = new File(file);
            //1.创建Reader对象
            SAXReader reader = new SAXReader();
            //2.加载xml
            Document document = reader.read(f);
            //3.获取根节点
            Element rootElement = document.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while (iterator.hasNext())
            {
                Element stu = (Element) iterator.next();
                Iterator iterator1 = stu.elementIterator();
                Doc d = new Doc();
                String txt = "";
                while (iterator1.hasNext())
                {
                    Element stuChild = (Element) iterator1.next();
                    String tag = stuChild.getName().toLowerCase();
                    String str = stuChild.getStringValue();

                    if(tag.equals("TEXT".toLowerCase()))
                    {
                        txt += " "+str;
                    }
                    else if(tag.equals("HEADLINE".toLowerCase()))
                    {
                        txt += " "+str;
                    }
                    else if(tag.equals("DOCNO".toLowerCase()))
                    {
                        d.setName(str);
                    }
                }
                d.setText(txt);

                list.add(d);
            }

            return list;
        }
        catch (DocumentException e)
        {
            if(e.getNestedException() instanceof FileNotFoundException)
            {
                System.out.printf("文件 %s 不存在！\n",file);
            }
            else
            {
                System.out.printf("读取xml文件 %s 出现问题！\n",file);
                e.printStackTrace();
            }
            return null;
        }
    }
}

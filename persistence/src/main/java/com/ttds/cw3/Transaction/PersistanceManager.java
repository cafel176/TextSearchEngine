package com.ttds.cw3.Transaction;

import com.ttds.cw3.Adapter.ModelManagerAdapter;
import com.ttds.cw3.Adapter.PreProcessingAdapter;
import com.ttds.cw3.Transaction.BoolTree.BoolTree;
import com.ttds.cw3.Transaction.BoolTree.DataNode.DataType;
import com.ttds.cw3.Data.OtherParams;
import com.ttds.cw3.Data.SearchResult;
import com.ttds.cw3.Interface.OtherParamsInterface;
import com.ttds.cw3.Interface.PersistanceManagerInterface;
import com.ttds.cw3.Interface.SearchResultInterface;
import com.ttds.cw3.Strategy.RetrievalModel.RetrievalModelType;
import com.ttds.cw3.Strategy.SearchModule.SearchModuleType;
import com.ttds.cw3.Transaction.Symbols.SymbolManager;
import com.ttds.cw3.Tools.*;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public final class PersistanceManager implements PersistanceManagerInterface
{
    private ModelManagerAdapter m;

    private BoolTree tree;
    private SymbolManager symbols;
    private PreProcessingAdapter preProcessing;

    private SearchDriver search;
    private RetrievalDriver retrieval;

    private Properties props;
    private int sthread = 3;
    private int rthread = 5;

    @Autowired
    public PersistanceManager(ModelManagerAdapter m)
    {
        this.m = m;

        preProcessing = m.getPreProcessing();

        loadProperties("application.properties");

        System.out.printf("Persistance Manager构造完成。\n");
    }

    public boolean loadProperties(String name)
    {
        boolean success;
        try
        {
            props = PropertiesLoaderUtils.loadAllProperties(name);

            sthread = Integer.parseInt(props.getProperty("sthread").trim());
            rthread = Integer.parseInt(props.getProperty("rthread").trim());

            symbols = new SymbolManager(props);

            int limit = Integer.parseInt(props.getProperty("limit").trim());
            String pattern = props.getProperty("pattern").trim();

            search = new SearchDriver(m,preProcessing,limit,sthread,pattern);
            retrieval = new RetrievalDriver(m,preProcessing,rthread);

            success = true;
        }
        catch (IOException e)
        {
            success = false;
            props = null;
            System.out.printf("PersistanceManager读取application.properties文件出现问题！\n");
            e.printStackTrace();
        }
        return success;
    }

    public Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> searchByRetrievalModel(String str, int start, int end)  throws Exception
    {
        DataType dataType = DataType.Double;
        buildTree(str,dataType);
        setRetrievalDatas("",null);
        ArrayList<SearchResult<Double>> arr = getRetrievalResult(dataType);
        ArrayList<SearchResult<Double>> results = retrieval.sort(retrieval.clip(arr));
        ArrayList<SearchResultInterface<Double>> output = new ArrayList<>();
        for(int i=start;i<results.size() && i<=end;i++)
        {
            output.add(results.get(i));
        }

        OtherParams param = new OtherParams();
        param.setNum(results.size());

        return new Pair(param,output);
    }

    public Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Boolean>>> searchBySearchModule(String str, int start, int end) throws Exception
    {
        DataType dataType = DataType.Boolean;
        buildTree(str, dataType);
        setSearchModuleDatas();
        ArrayList<SearchResult<Boolean>> arr = getSearchModuleResult(dataType);
        ArrayList<SearchResult<Boolean>> results = search.sort(search.clip(arr));

        ArrayList<SearchResultInterface<Boolean>> output = new ArrayList<>();
        for(int i=start;i<results.size() && i<=end;i++)
        {
            output.add(results.get(i));
        }

        OtherParams param = new OtherParams();
        param.setNum(results.size());

        return new Pair(param,output);
    }

    public Pair<OtherParamsInterface,ArrayList<SearchResultInterface<Double>>> search(String str, int start, int end) throws Exception
    {
        String[] st = str.split(" ",2);
        RetrievalModelType type = symbols.TxtToRetrievalTypeAll(st[0].trim());
        String query = st[1].trim();

        DataType dataType = DataType.Boolean;
        buildTree(query, dataType);
        setSearchModuleDatas();
        ArrayList<SearchResult<Boolean>> arr = getSearchModuleResult(dataType);
        ArrayList<SearchResult<Boolean>> results = search.clip(arr);
        ArrayList<String> filter = new ArrayList<>();
        for(int i=0;i<results.size();i++)
        {
            filter.add(results.get(i).getDocid());
        }

        dataType = DataType.Double;
        buildTree(query, dataType);
        setRetrievalDatas(st[0].trim(),filter);
        ArrayList<SearchResult<Double>> arr2 = getRetrievalResult(dataType);
        ArrayList<SearchResult<Double>> results2 = retrieval.clip(arr2);
        for(int i=0;i<results2.size();i++)
        {
            results2.get(i).setDesc(results.get(i).getDesc());
        }
        results2 = retrieval.sort(results2);

        ArrayList<SearchResultInterface<Double>> output = new ArrayList<>();
        for(int i=start;i<results2.size() && i<=end;i++)
        {
            output.add(results2.get(i));
        }

        OtherParams param = new OtherParams();
        param.setNum(results.size());

        return new Pair(param,output);
    }

    private boolean buildTree(String str, DataType dataType) throws Exception
    {
        switch (dataType)
        {
            case Boolean:tree = new BoolTree<Boolean>();break;
            case Double:tree = new BoolTree<Double>();break;
            default: tree = null;break;
        }
        if(tree==null)
            return false;

        ArrayList<String> query = QueryParser.parse(str,symbols);
        tree.buildTree(dataType,query,symbols);

        return true;
    }

    private ArrayList<SearchResult<Boolean>> getSearchModuleResult(DataType dataType)
    {
        return tree.getResult(dataType);
    }

    private ArrayList<SearchResult<Double>> getRetrievalResult(DataType dataType)
    {
        return tree.getResult(dataType);
    }

    private String[] getParams(String str)
    {
        String[] re;

        String txt;
        String operate;

        try
        {
            String[] st = str.split(" ",2);
            txt = st[1];

            try
            {
                String[] ops = st[0].split("_",2);
                operate = ops[0].trim();
                String param = ops[1].trim();

                re = new String[]{operate,txt,param};
            }
            catch (Exception e)
            {
                operate = st[0];
                re = new String[]{operate,txt,""};
                return re;
            }

        }
        catch (Exception e)
        {
            txt = str;
            re = new String[]{txt};
            return re;
        }

        return re;
    }

    private void setSearchModuleDatas() throws Exception
    {
        ArrayList<String> datas = tree.getDatas();
        for(int i=0;i<datas.size();i++)
        {
            String[] params = getParams(datas.get(i));

            String[] st = params[0].split("_",2);
            SearchModuleType type = symbols.TxtToSearchTypeAll(st[0]);
            String txt;
            String param;
            if(params.length==1)
            {
                if(type!=SearchModuleType.base)
                    throw new Exception(st[0] + "运算参数异常");

                txt = params[0];
                param = "";
            }
            else{
                txt = params[1];
                param = params[2];
            }

            ArrayList<SearchResult<Boolean>> arr = search.spaenDatas(type,txt,param,m);
            tree.setData(i, arr);
        }
    }

    private void setRetrievalDatas(String type_, ArrayList<String> filter) throws Exception
    {
        ArrayList<String> datas = tree.getDatas();
        for(int i=0;i<datas.size();i++)
        {
            String[] params = getParams(datas.get(i));

            String[] st = params[0].split("_",2);
            RetrievalModelType type;
            if(type_.isEmpty())
                type = symbols.TxtToRetrievalTypeAll(st[0]);
            else
                type = symbols.TxtToRetrievalTypeAll(type_);
            String txt;
            String param;
            if(params.length==1)
            {
                if(type_.isEmpty() && type!=RetrievalModelType.base)
                    throw new Exception(st[0] + "运算参数异常");

                txt = params[0];
                param = "";
            }
            else{
                txt = params[1];
                param = params[2];
            }

            ArrayList<SearchResult<Double>> re = retrieval.spaenDatas(type,txt,param,filter);
            tree.setData(i, re);
        }
    }
}


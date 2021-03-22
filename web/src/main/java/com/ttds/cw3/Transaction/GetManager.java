package com.ttds.cw3.Transaction;

import com.ttds.cw3.Adapter.OtherParamsAdapter;
import com.ttds.cw3.Adapter.PersistanceManagerAdapter;
import com.ttds.cw3.Adapter.SearchResultAdapter;
import com.ttds.cw3.Data.AllResponseData;
import com.ttds.cw3.Data.ResponseData;
import com.ttds.cw3.Data.ResponseType;
import com.ttds.cw3.Strategy.StrategyType;
import com.ttds.cw3.Tools.DocAnalysis;
import com.ttds.cw3.Tools.DocReader;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 定义get请求，URI:/person/find/all
 * Flux 0-n 对象集合
 * Mono 0-1 对象集合
 * Reactive 中 Flux Mono 异步处理（非阻塞）
 * 集合对象基本是同步处理(阻塞)
 */

@Configuration
public class GetManager
{
    private String address = "/get/";

    private String allowOrigin = "*";
    private int lengthLimit = 100;

    private Properties props;

    @Autowired
    public GetManager()
    {
        loadProperties("application.properties");

        System.out.printf("Get Manager构造完成。\n");
    }

    @Bean
    @Autowired
    @CrossOrigin
    @ResponseBody
    public RouterFunction<ServerResponse> search(PersistanceManagerAdapter m)
    {
        return RouterFunctions.route(RequestPredicates.GET(address+"search"),
                request->{
                    MultiValueMap<String,String> params = request.exchange().getRequest().getQueryParams();
                    String txt = params.getFirst("txt").trim();
                    int start = Integer.parseInt(params.getFirst("start").trim());
                    int end = Integer.parseInt(params.getFirst("end").trim());
                    System.out.printf("Search查询：%s 起始：%d 结束：%d \n",txt,start,end);

                    AllResponseData res;
                    if(txt.isEmpty())
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Can not input empty query!");
                    }
                    else if(txt.length()>lengthLimit)
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Query text is too long!");
                    }
                    else
                    {
                        try
                        {
                            Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Boolean>>> p = m.searchBySearchModule(txt,start,end);
                            ArrayList<SearchResultAdapter<Boolean>> arr = p.getValue();
                            ArrayList<ResponseData> re = new ArrayList<>();
                            for(int i=0;i<arr.size();i++)
                            {
                                re.add(new ResponseData<>(arr.get(i)));
                            }

                            int num = p.getKey().getNum();

                            res = new AllResponseData();
                            res.setNum(num);
                            res.setDatas(re);
                        }
                        catch (Exception e)
                        {
                            //e.printStackTrace();
                            res = new AllResponseData(ResponseType.illegalInput,e.getMessage());
                        }
                    }

                    Mono<AllResponseData> reMono = Mono.just(res);
                    ServerResponse.BodyBuilder a = ServerResponse.ok();
                    a = a.header("Access-Control-Allow-Origin",allowOrigin);
                    a = a.header("Access-Control-Allow-Credentials","true");
                    return a.body(reMono,AllResponseData.class);
                });
    }

    @Bean
    @Autowired
    @CrossOrigin
    @ResponseBody
    public RouterFunction<ServerResponse> retrieval(PersistanceManagerAdapter m)
    {
        return RouterFunctions.route(RequestPredicates.GET(address+"retrieval"),
                request->{
                    MultiValueMap<String,String> params = request.exchange().getRequest().getQueryParams();
                    String txt = params.getFirst("txt").trim();
                    int start = Integer.parseInt(params.getFirst("start").trim());
                    int end = Integer.parseInt(params.getFirst("end").trim());
                    System.out.printf("Retrieval查询：%s 起始：%d 结束：%d \n",txt,start,end);

                    AllResponseData res;
                    if(txt.isEmpty())
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Can not input empty query!");
                    }
                    else if(txt.length()>lengthLimit)
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Query text is too long!");
                    }
                    else
                    {
                        try
                        {
                            Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Double>>> p = m.searchByRetrievalModel(txt,start,end);
                            ArrayList<SearchResultAdapter<Double>> arr = p.getValue();
                            ArrayList<ResponseData> re = new ArrayList<>();
                            for(int i=0;i<arr.size();i++)
                            {
                                re.add(new ResponseData<>(arr.get(i)));
                            }

                            int num = p.getKey().getNum();

                            res = new AllResponseData();
                            res.setNum(num);
                            res.setDatas(re);
                        }
                        catch (Exception e)
                        {
                            //e.printStackTrace();
                            res = new AllResponseData(ResponseType.illegalInput,e.getMessage());
                        }
                    }

                    Mono<AllResponseData> reMono = Mono.just(res);
                    ServerResponse.BodyBuilder a = ServerResponse.ok();
                    a = a.header("Access-Control-Allow-Origin",allowOrigin);
                    a = a.header("Access-Control-Allow-Credentials","true");
                    return a.body(reMono,AllResponseData.class);
                });
    }

    @Bean
    @Autowired
    @CrossOrigin
    @ResponseBody
    public RouterFunction<ServerResponse> searchAndretrieval(PersistanceManagerAdapter m)
    {
        return RouterFunctions.route(RequestPredicates.GET(address+"all"),
                request->{
                    MultiValueMap<String,String> params = request.exchange().getRequest().getQueryParams();
                    String txt = params.getFirst("txt").trim();
                    int start = Integer.parseInt(params.getFirst("start").trim());
                    int end = Integer.parseInt(params.getFirst("end").trim());
                    System.out.printf("查询：%s 起始：%d 结束：%d \n",txt,start,end);

                    AllResponseData res;
                    if(txt.isEmpty())
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Can not input empty query!");
                    }
                    else if(txt.length()>lengthLimit)
                    {
                        res = new AllResponseData(ResponseType.illegalInput,"Query text is too long!");
                    }
                    else
                    {
                        try
                        {
                            Pair<OtherParamsAdapter,ArrayList<SearchResultAdapter<Double>>> p = m.search(txt,start,end);
                            ArrayList<SearchResultAdapter<Double>> arr = p.getValue();
                            ArrayList<ResponseData> re = new ArrayList<>();
                            for(int i=0;i<arr.size();i++)
                            {
                                re.add(new ResponseData<>(arr.get(i)));
                            }

                            int num = p.getKey().getNum();

                            res = new AllResponseData();
                            res.setNum(num);
                            res.setDatas(re);
                        }
                        catch (Exception e)
                        {
                            //e.printStackTrace();
                            res = new AllResponseData(ResponseType.illegalInput,e.getMessage());
                        }
                    }

                    Mono<AllResponseData> reMono = Mono.just(res);
                    ServerResponse.BodyBuilder a = ServerResponse.ok();
                    a = a.header("Access-Control-Allow-Origin",allowOrigin);
                    a = a.header("Access-Control-Allow-Credentials","true");
                    return a.body(reMono,AllResponseData.class);
                });
    }

    public boolean loadProperties(String name)
    {
        boolean success;
        try
        {
            props = PropertiesLoaderUtils.loadAllProperties(name);
            allowOrigin = props.getProperty("allowOrigin").trim();
            lengthLimit = Integer.parseInt(props.getProperty("lengthLimit").trim());

            success = true;
        }
        catch (IOException e)
        {
            success = false;
            props = null;
            System.out.printf("Get Manager读取application.properties文件出现问题！\n");
            e.printStackTrace();
        }

        return success;
    }
}

/*
.GET("/hello/{name}",serverRequest -> {String name=serverRequest.pathVariable("name");}
 */
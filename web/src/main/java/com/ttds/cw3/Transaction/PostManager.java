package com.ttds.cw3.Transaction;

import com.ttds.cw3.Adapter.PersistanceManagerAdapter;
import com.ttds.cw3.Data.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Properties;

@RestController
public final class PostManager
{
    private String address = "/post/";
    private String allowOrigin = "*";

    private Properties props;

    @Autowired
    public PostManager()
    {
        loadProperties("application.properties");

        System.out.printf("Post Manager构造完成。\n");
    }

    @Bean
    @CrossOrigin
    @Autowired
    @ResponseBody
    public RouterFunction<ServerResponse> test(PersistanceManagerAdapter m)
    {
        return RouterFunctions.route(RequestPredicates.POST(address + "test"),
                request -> {
                    MultiValueMap<String,String> params = request.exchange().getRequest().getQueryParams();
                    String txt = params.getFirst("txt").trim();
                    String txt2 = params.getFirst("txt2").trim();
                    ResponseData<String> re = new ResponseData("docid",txt,txt2,"This is a test");
                    Mono<ResponseData<String>> reMono = Mono.just(re);

                    ServerResponse.BodyBuilder a = ServerResponse.ok();
                    a = a.header("Access-Control-Allow-Origin",allowOrigin);
                    a = a.header("Access-Control-Allow-Credentials","true");
                    return a.body(reMono,ResponseData.class);
                });
    }

    public boolean loadProperties(String name)
    {
        boolean success;
        try
        {
            props = PropertiesLoaderUtils.loadAllProperties(name);
            allowOrigin = props.getProperty("allowOrigin").trim();

            success = true;
        }
        catch (IOException e)
        {
            success = false;
            props = null;
            System.out.printf("Post Manager读取application.properties文件出现问题！\n");
            e.printStackTrace();
        }

        return success;
    }
}

package cn.fllday.deft.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.ip:#{localhost}}")
    private String hosts;
    @Value("${elasticsearch.port:#{9200}}")
    private Integer port;
    @Value("${elasticsearch.schema}")
    private String schema;
    private ArrayList<HttpHost> hostList;
    @Value("${elasticsearch.connecttimeout}")
    private Integer connectTimeout;
    @Value("${elasticsearch.sockettimeout}")
    private Integer scoketTimeout;
    @Value("${elasticsearch.connection-request-timeout}")
    private Integer connectRequestTimeout;
    @Value("${elasticsearch.max-connect-num}")
    private Integer maxConnectNum;
    @Value("${elasticsearch.max-connect-perroute}")
    private Integer maxConnectPerroute;

    @Bean
    public RestHighLevelClient client()
    {
        hostList = new ArrayList<>();
        String[] hostStrs = hosts.split(",");
        for (String host:hostStrs){
            hostList.add(new HttpHost(host,port,schema));
        }
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 配置异步 client 连接延时配置
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                httpAsyncClientBuilder.setMaxConnTotal(maxConnectNum);
                httpAsyncClientBuilder.setMaxConnPerRoute(maxConnectPerroute);
                return httpAsyncClientBuilder;
            }
        });

        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectionRequestTimeout(connectRequestTimeout);
                builder.setSocketTimeout(scoketTimeout);
                builder.setConnectTimeout(connectTimeout);
                return builder;
            }
        });
        return new RestHighLevelClient(builder);
    }


}

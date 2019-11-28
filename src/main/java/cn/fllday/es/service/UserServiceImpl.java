package cn.fllday.es.service;

import cn.fllday.es.entity.User;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RestHighLevelClient client;

    @Override
    public void adds() throws IOException {
//        User user = new User("zs", 19, "男","1998");
        User user1 = new User(4,"zs1", 19, "男","1998");
//        User user1 = new User("gss", 19, "男","1997");
//        User user2 = new User("gss", 19, "男","1997");
        add1(user1);
//        add1(user1);
//        System.out.println("ss");
//        add1(user2);
    }

    public Boolean delete(Integer id) throws IOException {
        DeleteRequest request = new DeleteRequest("user", String.valueOf(id));
        DeleteResponse deleteResponse = client.delete(request,RequestOptions.DEFAULT);
        if (deleteResponse.status() == RestStatus.OK) {
           return true;
        }else {
            return  false;
        }

    }

      private void  add1(User user) throws IOException {
        IndexRequest index = new IndexRequest();
        index.index("user");
        index.id(user.getName());
//        User user = new User("张三", 19, "男","1997");
        index.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse response = client.index(index, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Override
    public List<Map<String, Object>> search(String name, String birth) throws IOException {

//     MUST   select * from user where name = "gss" and brith = "男"
//     SHOULD   select * from user where name = "gss" OR brith = "男"
        // 创建搜索其你去
        SearchRequest search = new SearchRequest();
        // 构造条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", name);
        boolQuery.should(termQuery);
        boolQuery.should(QueryBuilders.termQuery("birth",birth));
        //
        SearchSourceBuilder source  = new SearchSourceBuilder();

        source.query(boolQuery);
        // 指定索引库
        search.indices("user");
        search.source(source);
        // 执行搜索，获取返回结果
        SearchResponse response = client.search(search, RequestOptions.DEFAULT);
        // 获取数据条数
        SearchHits hits = response.getHits();
        SearchHit[] docs = response.getHits().getHits();
        List<Map<String, Object>> results = new ArrayList<>();
        Arrays.stream(docs)
                .forEach(data->{
                    Map<String, Object> sourceAsMap = data.getSourceAsMap();
                    results.add(sourceAsMap);
                });
        return results;
    }
}

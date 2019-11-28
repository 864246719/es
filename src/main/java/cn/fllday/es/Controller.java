package cn.fllday.es;

import cn.fllday.es.entity.User;
import cn.fllday.es.service.UserService;
import cn.fllday.es.utils.PagesUtil;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Resource
    private UserService userService;

    @Resource
    private RestHighLevelClient client;

    @RequestMapping("/add")
    public void add() throws IOException {
        userService.adds();
    }

    @RequestMapping("/del")
    public Map<String, String> delete(Integer id) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", userService.delete(id).toString());
        return map;
    }

    @RequestMapping("/search")
    public List<Map<String, Object>> search(SpringDataWebProperties.Pageable pageable, String name, String birth) throws IOException {
//        pageable.getPage
//        userService.adds();
        try {
            return userService.search(name, birth);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/queryPage")
    public PagesUtil<User> listPage(int pageIndex, int size, String name) throws Exception {
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest("user");
        //创建搜索构建者
        SearchSourceBuilder source = new SearchSourceBuilder();
        //设置构建搜索属性
        source.from((pageIndex - 1) * size); // 需要注意这里是从多少条开始
        source.size(size); //共返回多少条数据
        source.sort(new FieldSortBuilder("id").order(SortOrder.ASC)); //根据自己的需求排序
        if (!StringUtils.isEmpty(name)) {
            //自定义组合查询
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//            TermQueryBuilder termQuery = QueryBuilders.termQuery(STATUS, STATUS);
//            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("coutent",coutent)
////                    .fuzziness(Fuzziness.AUTO); //模糊匹配
//            boolQueryBuilder.must(termQuery).must(queryBuilder);
            boolQuery.should(QueryBuilders.termQuery("name", name));
            source.query(boolQuery);
        }
        //传入构建进行搜索
        searchRequest.source(source);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //处理结果
        RestStatus restStatus = response.status();
        if (restStatus != RestStatus.OK) {
            throw new RuntimeException("搜索错误");
        }
        List<User> list = new ArrayList<>();
        SearchHits hits = response.getHits();
        hits.forEach(item -> list.add(JSON.parseObject(item.getSourceAsString(), User.class)));
        PagesUtil<User> page = new PagesUtil<User>();
        page.setTotalPages((int) hits.getTotalHits().value, size);
        page.setPageIndex(pageIndex);
        page.setList(list);
        return page;
    }


}

package cn.fllday.es.service;

import cn.fllday.es.entity.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public interface UserService {

     Boolean delete(Integer id) throws IOException;

     void adds () throws IOException;

     List<Map<String, Object>> search(String name,String birth) throws IOException;

}

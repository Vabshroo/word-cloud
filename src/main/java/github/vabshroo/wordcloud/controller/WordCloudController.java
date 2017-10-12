package github.vabshroo.wordcloud.controller;

import com.alibaba.fastjson.JSONObject;
import github.vabshroo.wordcloud.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 22:36
 * @desc WordCloudController
 */
@Controller
@RequestMapping("/")
public class WordCloudController extends AbstractController {

    private final static Logger LOGGER = LoggerFactory.getLogger(WordCloudController.class);

    @RequestMapping("cloud/{key}/{num}/{percent}")
    public String cloud(@PathVariable(value = "key") String key,@PathVariable(value = "num") Integer num,@PathVariable(value = "percent") Integer percent, Model model){
        model.addAttribute("words", JSONObject.toJSONString(getWordsByKey(key,num)));
        model.addAttribute("percent", percent);
        return "index";
    }

    private List<Map<String,Object>> getWordsByKey(String key, Integer num) {
        List<Map<String,Object>> result = new ArrayList<>();

        if(num == null || num <= 0){
            num = 200;
        }

        String path = System.getProperty("user.dir") + "/src/main/resources/analyseResult/" + key;
        String content = FileUtil.readFile(path);
        List<String> list = Arrays.asList(content.split("\n"));

        Integer finalNum = num;
        list.forEach(s -> {
            if(StringUtils.isNotBlank(s) && result.size() < finalNum){
                Map<String,Object> map = new HashMap<>();

                String[] line = s.split("\\s+");
                map.put("text",line[0]);
                map.put("size",line[1]);

                result.add(map);
            }
        });

        return result;
    }

}

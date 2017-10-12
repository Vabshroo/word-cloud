package github.vabshroo.wordcloud.analyzer;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/8
 * @time 12:52
 * @desc Analyzer
 */
public interface Analyzer {

    Analyzer analyse(List<String> sources);
    Map<String,Integer> saveResult();

}

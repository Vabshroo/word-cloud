package github.vabshroo.wordcloud.crawler;

import java.util.List;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/8
 * @time 12:55
 * @desc Crawler
 */
public interface Crawler {

    Crawler crawler();
    List<String> saveResult();

}

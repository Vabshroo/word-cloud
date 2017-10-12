package github.vabshroo.wordcloud.entry.poem;

import github.vabshroo.wordcloud.analyzer.SimpleAnalyzer;
import github.vabshroo.wordcloud.crawler.PoemCrawler;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 22:03
 * @desc PoemQinguan
 */
public class PoemQinguan {

    public static void main(String[] args){
        new SimpleAnalyzer("qinguan").analyse(
                new PoemCrawler("qinguan",38).crawler().saveResult()
        ).saveResult();
    }

}

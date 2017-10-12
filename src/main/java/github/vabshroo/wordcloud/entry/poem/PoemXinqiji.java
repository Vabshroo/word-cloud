package github.vabshroo.wordcloud.entry.poem;

import github.vabshroo.wordcloud.analyzer.SimpleAnalyzer;
import github.vabshroo.wordcloud.crawler.PoemCrawler;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 22:15
 * @desc PoemXinqiji
 */
public class PoemXinqiji {

    public static void main(String[] args){
        new SimpleAnalyzer("xinqiji").analyse(
                new PoemCrawler("xinqiji",44).crawler().saveResult()
        ).saveResult();
    }

}

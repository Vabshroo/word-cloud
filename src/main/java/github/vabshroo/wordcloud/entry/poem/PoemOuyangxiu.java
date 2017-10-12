package github.vabshroo.wordcloud.entry.poem;

import github.vabshroo.wordcloud.analyzer.SimpleAnalyzer;
import github.vabshroo.wordcloud.crawler.PoemCrawler;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 22:23
 * @desc PoemOuyangxiu
 */
public class PoemOuyangxiu {

    public static void main(String[] args){
        new SimpleAnalyzer("ouyangxiu").analyse(
                new PoemCrawler("ouyangxiu",115).crawler().saveResult()
        ).saveResult();
    }

}

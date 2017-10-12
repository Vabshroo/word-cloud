package github.vabshroo.wordcloud.entry.weibo;

import github.vabshroo.wordcloud.analyzer.SimpleAnalyzer;
import github.vabshroo.wordcloud.crawler.WeiboCommentCrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/9
 * @time 23:14
 * @desc WeiboLuhanGuanxiaotong
 */
public class WeiboLuhanGuanxiaotong {

    public static void main(String args[]){

        Map<String,String> header = new HashMap<>();
        header.put("Cookie","");
        header.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        Map<String,String> param = new HashMap<>();
        param.put("ajwvr","6");
        param.put("id","4160547165300149");
        param.put("filter","hot");
        param.put("from","singleWeiBo");
        param.put("__rnd",String.valueOf(new Date().getTime()));

        new SimpleAnalyzer("luhanguanxiaotong")
                .analyse(
                        new WeiboCommentCrawler("luhanguanxiaotong",param,header)
                                .crawler()
                                .saveResult()
                )
                .saveResult();
    }

}

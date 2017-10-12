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
 * @time 21:29
 * @desc WeiboEDGLoss
 */
public class WeiboEDGLoss {

    public static void main(String args[]){

        Map<String,String> header = new HashMap<>();
        header.put("Cookie","");
        header.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

        Map<String,String> param = new HashMap<>();
        param.put("ajwvr","6");
        param.put("id","4160322153474931");
        param.put("filter","hot");
        /*param.put("root_comment_max_id","343194243313031");
        param.put("root_comment_max_id_type","0");
        param.put("root_comment_ext_param","");
        param.put("sum_comment_number","19700");
        param.put("filter_tips_before","0");*/
        //param.put("page","1");
        param.put("from","singleWeiBo");
        param.put("__rnd",String.valueOf(new Date().getTime()));
        //=&=1507557614105

        new SimpleAnalyzer("edgloss")
                .analyse(
                        new WeiboCommentCrawler("edgloss",param,header)
                                .crawler()
                                .saveResult()
                )
                .saveResult();
    }

}

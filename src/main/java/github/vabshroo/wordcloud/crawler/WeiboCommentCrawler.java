package github.vabshroo.wordcloud.crawler;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static github.vabshroo.wordcloud.util.HttpClientUtil.get;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/9
 * @time 21:07
 * @desc WeiboCommentCrawler
 */
public class WeiboCommentCrawler extends AbstractCrawler {

    private final static Logger LOGGER = LoggerFactory.getLogger(WeiboCommentCrawler.class);

    private Map<String,String> param;

    private Map<String,String> header;

    public WeiboCommentCrawler(String title, Map<String,String> param, Map<String,String> header){

        super(title);

        this.param = param;
        this.header = header;
    }

    @Override
    public Crawler crawler() {
        setListResult(Collections.synchronizedList(new ArrayList<>()));

        Integer page = 1;
        AtomicInteger finished = new AtomicInteger(0);
        ExecutorService service = Executors.newFixedThreadPool(1);
        while(page <= getCrawlerPage()){
            Integer finalPage = page;
            service.submit(() -> {

                try{
                    changeProxy();
                    //Thread.sleep(1000L);
                    Map<String,String> finalParam = new HashMap<>();
                    finalParam.putAll(param);
                    finalParam.put("page",finalPage.toString());
                    finalParam.put("__rnd",String.valueOf(new Date().getTime()));

                    String userInfo = get("http://weibo.com/aj/v6/comment/big",header,finalParam);
                    String html = null;
                    Elements commentElements = null;
                    Integer tryTimes = 0;
                    while((html == null || commentElements == null || commentElements.size() == 0) && tryTimes++ < MAX_TRY_TIMES){
                        html = ((JSONObject)JSONObject.parseObject(userInfo).get("data")).get("html").toString().replaceAll("\\\\\"","\"").replaceAll("\\\\/","/");
                        Document doc = Jsoup.parse(html);
                        commentElements = doc.select("div.list_con");
                    }

                    if(commentElements == null || commentElements.size() == 0){
                        LOGGER.error("page{} missing!",finalPage);
                    }else{
                        commentElements.forEach(element -> {
                            String s = element.text()
                                    .replaceAll("^.*：","")
                                    .replaceAll("^回复@.+:","");
                            getListResult().add(s.contains("举报 ") ? s.substring(0,s.indexOf("举报 ")) : "");
                        });
                    }

                }catch (Exception e){
                    LOGGER.error("page{},{}",finalPage,e.getMessage());
                }

                finished.addAndGet(1);

            });
            page ++;
        }
        service.shutdown();

        while(!service.isTerminated()){
            try {
                Thread.sleep(5000l);
            } catch (InterruptedException e) {
                LOGGER.error("sleep error",e);
            }
            LOGGER.info("抓取中：{} / {}",finished,getCrawlerPage());
        }

        return this;
    }
}

package github.vabshroo.wordcloud.crawler;

import github.vabshroo.wordcloud.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 21:02
 * @desc PoemCrawler
 */
public class PoemCrawler extends AbstractCrawler {

    private final static Logger LOGGER = LoggerFactory.getLogger(PoemCrawler.class);

    private final static String POET_PAGE_URL = "http://www.shicimingju.com/chaxun/zuozhe/%s.html#chaxun_miao";
    private final static String POET_URL_BASE = "http://www.shicimingju.com";

    private Integer poetId;
    private Integer pageSize;
    private Integer poemNum;
    private List<String> poemUrls = new ArrayList<>();

    public PoemCrawler(String title,Integer poetId){
        super(title);
        this.poetId = poetId;
    }

    @Override
    public Crawler crawler() {
        //初始化pageSize和pageNum
        initPage();

        Integer pageNum = new Double(Math.ceil(poemNum / pageSize)).intValue();

        LOGGER.info("poemNum:{} / pageNum:{} / pageSize:{}",poemNum,pageNum,pageSize);

        Integer page = 0;
        while (page ++ <= pageNum){
            LOGGER.info("page:{}",page);
            String url = String.format(POET_PAGE_URL,poetId + "_" + page);
            LOGGER.info("url:{}",url);
            String html = HttpClientUtil.get(url,null,null);

            Document doc = Jsoup.parse(html);
            Elements poemUl = doc.select("div.shicilist").get(0).select("ul");
            if(poemUl != null && poemUl.size() > 0){
                poemUl.forEach(ul -> poemUrls.add(POET_URL_BASE + ul.select("li").get(0).select("a").get(0).attr("href")));
            }

            changeProxy();
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                LOGGER.error("sleep error",e);
            }
        }

        poemUrls.forEach( poemUrl -> {
            LOGGER.info("url:{}",poemUrl);
            getListResult().add(Jsoup.parse(HttpClientUtil.get(poemUrl,null,null)).getElementById("shicineirong").text());
        });

        return this;
    }

    private void initPage() {

        String url = String.format(POET_PAGE_URL,poetId);

        String html = HttpClientUtil.get(url,null,null);

        if(StringUtils.isBlank(html)){
            LOGGER.error("查询诗人主页失败：{}",poetId);
            return;
        }

        Document doc = Jsoup.parse(html);
        Elements profile = doc.select("div.jianjie");
        if(profile != null && profile.size() > 0){
            poemNum = Integer.parseInt(matchPoemNum(profile.get(0).select("h1").select("a").text()));
        }

        Elements poemUl = doc.select("div.shicilist").get(0).select("ul");
        if(poemUl != null && poemUl.size() > 0){
            pageSize = poemUl.size();
        }


    }

    private String matchPoemNum(String text){
        Pattern pattern = Pattern.compile(".+\\((\\d+).+\\)");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            return matcher.group(1);
        }
        return "0";
    }
}

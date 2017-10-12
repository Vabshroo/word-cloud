package github.vabshroo.wordcloud.crawler;

import github.vabshroo.wordcloud.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/11
 * @time 21:04
 * @desc AbstractCrawler
 */
public abstract class AbstractCrawler implements Crawler {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCrawler.class);

    protected final static Integer MAX_TRY_TIMES = 5;

    protected final static String PROXY_IP_PATH = "/proxyip/proxyip.txt";

    protected final static List<String> PROXY_IP_LIST = new ArrayList<>();

    private String resultPath = "/crawlerResult/";

    private File resultFile;

    private String title;

    private List<String> listResult = new ArrayList<>();

    private Integer crawlerPage = 100;

    public String getResultPath() {
        return resultPath;
    }

    public AbstractCrawler setResultPath(String resultPath) {
        this.resultPath = resultPath;
        return this;
    }

    public File getResultFile() {
        return resultFile;
    }

    public AbstractCrawler setResultFile(File resultFile) {
        this.resultFile = resultFile;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AbstractCrawler setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getListResult() {
        return listResult;
    }

    public AbstractCrawler setListResult(List<String> listResult) {
        this.listResult = listResult;
        return this;
    }

    public Integer getCrawlerPage() {
        return crawlerPage;
    }

    public AbstractCrawler setCrawlerPage(Integer crawlerPage) {
        this.crawlerPage = crawlerPage;
        return this;
    }

    AbstractCrawler(String title) {
        this.title = title;
        this.resultPath = System.getProperty("user.dir") + "/src/main/resources" + resultPath + title;
        this.resultFile = new File(resultPath);

        if(resultFile.exists()){
            LOGGER.warn("{}抓取结果已存在，将被覆盖！",title);
        }else{
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("创建文件{}失败！",resultPath,e);
            }
        }
        this.crawlerPage = crawlerPage;

        PROXY_IP_LIST.addAll(Arrays.asList(FileUtil.readFile(System.getProperty("user.dir") + "/src/main/resources" + PROXY_IP_PATH).split("\n")));
    }

    void changeProxy() {

        Random random = new Random();
        int randomInt = random.nextInt(PROXY_IP_LIST.size());
        String ipport = PROXY_IP_LIST.get(randomInt);
        String proxyIp = ipport.substring(0, ipport.lastIndexOf(":"));
        String proxyPort = ipport.substring(ipport.lastIndexOf(":") + 1, ipport.length());

        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", proxyIp);
        System.getProperties().setProperty("http.proxyPort", proxyPort);

        System.out.println("设置代理ip为：" + proxyIp + "端口号为：" + proxyPort);
    }


    @Override
    public List<String> saveResult() {

        if(listResult != null && !listResult.isEmpty()){
            FileUtil.writeFile(resultFile.getPath(), StringUtils.join(listResult,"\n"),false);
        }

        return listResult;
    }
}

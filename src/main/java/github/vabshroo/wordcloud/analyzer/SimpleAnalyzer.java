package github.vabshroo.wordcloud.analyzer;

import github.vabshroo.wordcloud.util.FileUtil;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/9
 * @time 20:17
 * @desc SimpleAnalyzer
 */
public class SimpleAnalyzer implements Analyzer {

    private final static Logger LOGGER = LoggerFactory.getLogger(Analyzer.class);

    private final static String STOP_WORDS_DIC = "/library/stopWord.dic";

    private String resultPath = "/analyseResult/";

    private File resultFile;

    private String title;

    private Map<String,Integer> mapResult;

    public SimpleAnalyzer(String title){

        this.title = title;
        this.resultPath = System.getProperty("user.dir") + "/src/main/resources" + resultPath + title;
        this.resultFile = new File(resultPath);

        if(resultFile.exists()){
            LOGGER.warn("{} 分析结果已存在，将被覆盖！",title);
        }else{
            try {
                resultFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("创建文件{}失败！",resultPath,e);
            }
        }
    }

    public String getResultPath() {
        return resultPath;
    }

    public Analyzer setResultPath(String resultPath) {
        this.resultPath = resultPath;
        return this;
    }

    public File getResultFile() {
        return resultFile;
    }

    public Analyzer setResultFile(File resultFile) {
        this.resultFile = resultFile;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Analyzer setTitle(String title) {
        this.title = title;
        return this;
    }

    public Map<String, Integer> getMapResult() {
        return mapResult;
    }

    public Analyzer setMapResult(Map<String, Integer> mapResult) {
        this.mapResult = mapResult;
        return this;
    }

    /**
     * 分析聚合结果
     * @param sources
     * @return
     */
    public Analyzer analyse(List<String> sources){
        LOGGER.info("开始分析：{}",title);
        mapResult = new HashMap<>();
        if(sources != null && !sources.isEmpty()){
            Set<String> stopWords = new HashSet<>(Arrays.asList(FileUtil.readFile(System.getProperty("user.dir") + "/src/main/resources" + STOP_WORDS_DIC).split("\n")));

            sources.forEach(source -> {
                List<String> words = Arrays.asList(NlpAnalysis.parse(source).toString().split(","));
                words.forEach(word -> {
                    word = word.replaceAll("/\\w+","");
                    if(!stopWords.contains(word) && StringUtils.isNotBlank(word)){
                        if(mapResult.containsKey(word)){
                            mapResult.put(word,mapResult.get(word) + 1);
                        }else{
                            mapResult.put(word,1);
                        }
                    }
                });
            });
        }
        LOGGER.info("分析结束：{}",title);
        return this;
    }

    /**
     * 保存结果至文件
     * @return
     */
    public Map<String,Integer> saveResult(){
        LOGGER.info("开始保存分析结果至文件：{}",resultPath);
        if(mapResult != null && !mapResult.isEmpty()){

            List<String> resultLines = new ArrayList<>();

            List<Map.Entry<String,Integer>> entries = new ArrayList<>(mapResult.entrySet());
            entries.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            entries.forEach(entry -> resultLines.add(entry.getKey() + " " + entry.getValue()));

            FileUtil.writeFile(resultFile.getPath(), StringUtils.join(resultLines,"\n"),false);
        }
        LOGGER.info("保存分析结果至文件结束：{}",resultPath);
        return mapResult;
    }
}

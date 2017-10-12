package github.vabshroo.wordcloud.test;

import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/8
 * @time 12:39
 * @desc TestWordSeg
 */
public class TestWordSeg {

    public static void main(String[] args){
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
        System.out.println(ToAnalysis.parse(str));
        System.out.println(NlpAnalysis.parse(str));
    }

}

package github.vabshroo.wordcloud.util;

import java.io.*;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/10/8
 * @time 14:48
 * @desc FileUtil
 */
public class FileUtil {

    public static String readFile(String fileName){
        StringBuilder result = new StringBuilder();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static void writeFile(String fileName, String content,Boolean append) {
        try {
            FileWriter writer = new FileWriter(fileName, append);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

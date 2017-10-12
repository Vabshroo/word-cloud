package github.vabshroo.wordcloud.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/9/16
 * @time 7:40
 * @desc HttpClient
 */
public class HttpClientUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String post(String url, Map<String,String> header,Object param){
        if(StringUtils.isBlank(url)){
            return null;
        }

        HttpClient client = HttpClientManager.getHttpClient();
        try {

            HttpPost httpPost = new HttpPost(url);

            if(header != null && !header.isEmpty()){
                header.forEach((key, value) -> httpPost.setHeader(new BasicHeader(key, value)));
            }

            if(param != null){
                if(param instanceof JSONObject){
                    StringEntity httpEntity = new StringEntity(JSONObject.toJSONString(param),"UTF-8");
                    httpEntity.setContentEncoding("UTF-8");
                    httpEntity.setContentType("application/json");
                    httpPost.setEntity(httpEntity);
                }else if(param instanceof Map){
                    List<NameValuePair> formParams = new ArrayList<>();
                    ((Map<String,Object>) param).forEach((key,value) -> formParams.add(new BasicNameValuePair(key, value.toString())));
                    UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
                    httpPost.setEntity(uefEntity);
                }
            }

            HttpResponse response = client.execute(httpPost);

            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity,"UTF-8");
        }catch (Exception e){
            LOGGER.error("POST error!",e);
        }

        return null;
    }

    /**
     * get 请求
     * @param url
     * @param header
     * @param param
     * @return
     */
    public static String get(String url,Map<String,String> header,Object param){

        if(StringUtils.isBlank(url)){
            return null;
        }

        HttpClient client = HttpClientManager.getHttpClient();

        if(param != null){
            url = genGetUrl(url,param);
        }

        HttpGet httpGet = new HttpGet(url);
        try {

            if(header != null && !header.isEmpty()){
                header.forEach((key, value) -> httpGet.setHeader(new BasicHeader(key, value)));
            }

            HttpResponse response = client.execute(httpGet);

            if(response.getStatusLine().getStatusCode() != 200){
                httpGet.abort();
            }

            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity,"UTF-8");

        }catch (Exception e){
            httpGet.abort();
            LOGGER.error("GET error!",e);
        }

        return null;

    }

    public static String downGet(String url,Map<String,String> header,Object param){
        if(StringUtils.isBlank(url)){
            return null;
        }

        HttpClient client = HttpClientManager.getHttpClient();
        try {

            if(param != null){
                url = genGetUrl(url,param);
            }

            HttpGet httpGet = new HttpGet(url);

            if(header != null && !header.isEmpty()){
                header.forEach((key, value) -> httpGet.setHeader(new BasicHeader(key, value)));
            }

            HttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            StringBuffer result = new StringBuffer();
            InputStream in = entity.getContent();
            byte[] buffer = new byte[4096];

            int readLength = 0;
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                result.append(new String(bytes,"UTF-8"));
            }

            return result.toString();
        }catch (Exception e){
            LOGGER.error("GET error!",e);
        }

        return null;
    }

    /**
     * 拼接url
     *
     * @param url
     * @param param
     * @return
     */
    private static String genGetUrl(String url, Object param) {
        if(param instanceof String){
            url += "?" + param;
        }else if(param instanceof Map){
            url += "?";
            List<String> values = new ArrayList<>();
            ((Map) param).forEach((key, value) -> {
                try {
                    values.add(URLEncoder.encode(key.toString(), "UTF-8") + "=" + URLEncoder.encode(value.toString(),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            url += StringUtils.join(values,"&");
        }
        return url;
    }

    /*public static void main(String[] args){
        ExecutorService service = Executors.newFixedThreadPool(128);

        int i = 0;
        while(i ++ < 1000){
            service.submit(() -> LOGGER.info(get("https://www.baidu.com/",null,null)));
        }
        service.shutdown();
    }*/

    public static void main(String args[]){
        Map<String,String> header = new HashMap<>();
        header.put("Cookie","SINAGLOBAL=9090590802192.11.1480943806638; UM_distinctid=15cc1e677be5d5-0d583cb1c2ff29-323f5e0f-1fa400-15cc1e677c0cb; login_sid_t=c6054eed47404cb636bb96bee23685b2; YF-V5-G0=572595c78566a84019ac3c65c1e95574; _s_tentry=www.baidu.com; Apache=4877980613466.774.1507438591698; ULV=1507438591710:22:1:1:4877980613466.774.1507438591698:1506607233353; YF-Page-G0=b5853766541bcc934acef7f6116c26d1; YF-Ugrow-G0=8751d9166f7676afdce9885c6d31cd61; UOR=i.youku.com,widget.weibo.com,www.baidu.com; SSOLoginState=1507441351; SCF=Anier8EUpS_Hcr3KFIZMku12Tu20S0k0uVmTFccwsknnOA11n4GLb2voLtiRduyBOf1b532xX9p-xJoWI-mWaQo.; SUB=_2A2503cqYDeRhGedJ6FUT9S7NzTiIHXVXqrtQrDV8PUNbmtBeLWPQkW-GOwjr0bTp7IcW74LQ3D_W7XQj0A..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5XaaUsZ2xez16fhYQIEfi65JpX5K2hUgL.Fo2Ne0MESK5pSoB2dJLoIpxhKFH8Sb-4BE-R1CH8SbHWxb-4entt; SUHB=0ftoY1WkCft_uL; ALF=1538977351; un=clyd@sina.cn; wvr=6; wb_cusLike_1737250164=N");
        //String userInfo = get("http://weibo.com/aj/v6/comment/big?ajwvr=6&id=4160322153474931&from=singleWeiBo&__rnd=1507447323767",header,null);
        String userInfo = get("http://weibo.com/aj/v6/comment/big?ajwvr=6&id=4160322153474931&root_comment_max_id=972664650199718&root_comment_max_id_type=0&root_comment_ext_param=&page=5&filter=all&sum_comment_number=12614&filter_tips_before=0",header,null);
        String html = ((JSONObject)JSONObject.parseObject(userInfo).get("data")).get("html").toString().replaceAll("\\\\\"","\"").replaceAll("\\\\/","/");

        System.out.println(html);

        Document doc = Jsoup.parse(html);
        Elements commentElements = doc.select("div.list_con");

        //Pattern pattern = Pattern.compile("(^.+：(回复@.+)*)(.+)(\\s举报(\\s查看对话)*\\s回复\\sñ赞\\s\\d+分钟前)$");
        //commentElements.forEach(element -> System.out.println(pattern.matcher(element.text()).group(0)));
        commentElements.forEach(element -> {
            String s = element.text()
                .replaceAll("^.*：","")
                .replaceAll("^回复@.+:","");
            System.out.println(s.contains("举报 ") ? s.substring(0,s.indexOf("举报 ")) : "");
        });
        //WZQingg-：回复@刘佳威牛逼:6 举报 查看对话 回复 ñ赞 4分钟前
    }

}

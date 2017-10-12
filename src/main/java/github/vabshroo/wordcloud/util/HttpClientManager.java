package github.vabshroo.wordcloud.util;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA
 *
 * @author chenlei
 * @date 2017/9/16
 * @time 7:54
 * @desc HttpClientManager
 */
public class HttpClientManager {

    private static PoolingHttpClientConnectionManager connectionManager;

    // 最大连接数
    public final static int MAX_TOTAL_CONNECTIONS = 200;
    // 获取连接的最大等待时间
    public final static int WAIT_TIMEOUT = 30000;// 30s
    // 每个路由最大连接数
    public final static int MAX_ROUTE_CONNECTIONS = 100;
    // 连接超时时间
    public final static int CONNECT_TIMEOUT = 10000;// 10s
    // 读取超时时间
    public final static int READ_TIMEOUT = 10000;// 10s

    static {

        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

    }

    public static HttpClient getHttpClient(){

        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(WAIT_TIMEOUT)    // 请求超时时间
                .setSocketTimeout(CONNECT_TIMEOUT)    // 等待数据超时时间
                .setConnectionRequestTimeout(READ_TIMEOUT)  // 连接超时时间
                .build();
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .build();
    }


}

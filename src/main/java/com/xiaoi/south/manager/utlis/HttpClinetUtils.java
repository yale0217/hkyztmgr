package com.xiaoi.south.manager.utlis;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Node;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClinetUtils {


    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 1000;

    /**
     * 每个路由最大连接数,对于同一个目标机器的最大并发连接只有默认只有2个 哪怕你设置连接池的最大连接数为200，但是实际上还是只有2个连接在工作，
     * 其他剩余的198个连接都在等待，都是为别的目标机器服务的（目标服务器通常指同一台服务器或者同一个域名）
     */
    public final static int MAX_ROUTE_CONNECTIONS = 100;// 100

    /**
     * 连接超时时间 10s
     */
    public final static int CONNECT_REQUEST_TIMEOUT = 10 * 1000;

    /**
     * 连接超时时间 10s
     */
    public final static int CONNECT_TIMEOUT = 10 * 1000;

    /**
     * 连接超时时间 10s
     */
    public final static int SOCKET_TIMEOUT = 10 * 1000;

    private static ScheduledExecutorService executor;

    private static PoolingHttpClientConnectionManager connManager;

    // private static final String USER_AGENT =
    // "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)";//IE6
    // private static final String USER_AGENT =
    // "Mozilla/5.0 (Windows NT 5.1; rv:13.0) Gecko/20100101 Firefox/13.0.1";

    private static CloseableHttpClient httpClient = null;

    private static HttpRequestBase configRequest(HttpRequestBase httpRequestBase ){
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout( CONNECT_REQUEST_TIMEOUT ).setConnectTimeout( CONNECT_TIMEOUT ).setSocketTimeout( SOCKET_TIMEOUT ).build();
        httpRequestBase.setConfig( requestConfig );

        // httpRequestBase.addHeader( name, value );
        return httpRequestBase;
    }

    /**
     * 获取HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient getHttpClient(){
        if( httpClient == null ){
            if( httpClient == null ){
                httpClient = createHttpClient( 200, 20, 100 );
            }
        }
        return httpClient;
    }

    public static SSLContext getSSLContext(){
        // SSLContext sslContext = SSLContexts.custom().
        SSLContext sslContext = null;
        try{
            sslContext = SSLContext.getInstance( "SSLv3" );
            // 忽略https校验
            sslContext.init( null, new TrustManager[] { new X509TrustManager(){
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType ) throws CertificateException {
                }

                @Override
                public void checkServerTrusted( X509Certificate[] chain, String authType ) throws CertificateException{
                }

                @Override
                public X509Certificate[] getAcceptedIssuers(){
                    return null;
                }
            } }, null );
        }catch( Exception e ){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sslContext;
    }

    /**
     * 创建HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient createHttpClient( int maxTotal, int maxPerRoute, int maxRoute ){
        // ConnectionSocketFactory plainsf =
        // PlainConnectionSocketFactory.getSocketFactory();
        // LayeredConnectionSocketFactory sslsf =
        // SSLConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory( getSSLContext() );
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register( "http", PlainConnectionSocketFactory.INSTANCE ).register( "https", sslsf ).build();

        connManager = new PoolingHttpClientConnectionManager( registry );

        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay( true ).build();
        connManager.setDefaultSocketConfig( socketConfig );
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount( 200 ).setMaxLineLength( 2000 ).build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction( CodingErrorAction.IGNORE ).setUnmappableInputAction( CodingErrorAction.IGNORE ).setCharset( Consts.UTF_8 ).setMessageConstraints( messageConstraints ).build();
        connManager.setDefaultConnectionConfig( connectionConfig );
        // 将最大连接数增加
        connManager.setMaxTotal( maxTotal );
        // 将每个路由基础的连接增加
        connManager.setDefaultMaxPerRoute( maxPerRoute );
        // HttpHost httpHost = new HttpHost( hostname, port );
        // 将目标主机的最大连接数增加
        // cm.setMaxPerRoute( new HttpRoute( httpHost ), maxRoute );
        // cm.setDefaultMaxPerRoute( maxRoute );

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler(){
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context ){
                if( executionCount >= 2 ){// 如果已经重试了2次
                    return false;
                }
                if( exception instanceof NoHttpResponseException){// 如果服务器丢掉了连接，那么就重试
                    return true;
                }else if( exception instanceof SSLHandshakeException){// 不要重试SSL握手异常
                    return false;
                }else if( exception instanceof InterruptedIOException){// 超时
                    return false;
                }else if( exception instanceof UnknownHostException){// 目标服务器不可达
                    return false;
                }else if( exception instanceof ConnectTimeoutException){// 连接被拒绝
                    return false;
                }else if( exception instanceof SSLException){// SSL握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt( context );
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if( !(request instanceof HttpEntityEnclosingRequest) ){
                    return true;
                }
                return false;
            }
        };

        //
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate( new Runnable(){
            @Override
            public void run(){// 每30执行，处理各类任务
                connManager.closeExpiredConnections();
            }
        }, 5, 30, TimeUnit.SECONDS );

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager( connManager ).setRetryHandler( httpRequestRetryHandler ).build();
        return httpClient;
    }

    /**
     * @Description <b> #get请求，返回字节数组 </b>
     * @param url 地址
     * @param params 参数
     * @param charset 编码
     * @return byte[]
     * @since 1.0
     */
    public static byte[] doGet(String url, List<NameValuePair> params, String charset ){
        // 产生最终地址
        String realUrl = getQueryUrl( url, params, charset );
        HttpGet get = new HttpGet( realUrl );
        configRequest( get );
        CloseableHttpResponse response = null;
        byte bt[] = null;
        try{
            response = getHttpClient().execute( get, HttpClientContext.create() );
            HttpEntity entity = response.getEntity();
            // int status = response.getStatusLine().getStatusCode();
            // String result = EntityUtils.toString(entity, charset);
            if( entity != null ){
                bt = EntityUtils.toByteArray( response.getEntity() );
            }
            EntityUtils.consume( entity );
        }catch( Exception e ){
            e.printStackTrace();
            get.abort();
        }finally{
            closeResponse( response );
            get.releaseConnection();
        }
        return bt;
    }

    /**
     * @Description <b> #get方法发起请求,返回字节数组 </b>
     * @param url 地址
     * @param paramsMap 参数
     * @param charset 编码
     * @return byte[]
     * @since 1.0
     */
    public static byte[] doGet(String url, Map<String, String> paramsMap, String charset ){
        List<NameValuePair> params = map2Nvp( paramsMap );
        return doGet( url, params, charset );
    }

    /**
     * @Description <b> #get方法发起请求，返回字符串 </b>
     * @param url 地址
     * @param params 参数
     * @param headerMap header头参数
     * @param charset 编码
     * @return String
     * @since 1.0
     */
    public static String getContent( String url, List<NameValuePair> params, Map<String, String> headerMap, String charset ){
        // 产生最终地址
        String realUrl = getQueryUrl( url, params, charset );
        HttpGet get = new HttpGet( realUrl );
        configRequest( get );
        CloseableHttpResponse response = null;
        // 设置header
        addHeaders( get, headerMap );
        String result = null;
        try{
            response = getHttpClient().execute( get, HttpClientContext.create() );
            // int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if( entity != null ){
                result = EntityUtils.toString( entity, charset );
            }
            EntityUtils.consume( entity );
        }catch( Exception e ){
            e.printStackTrace();
            get.abort();
        }finally{
            closeResponse( response );
            get.releaseConnection();
        }
        return result;
    }

    /**
     * @Description <b> #get方式传入map参数，请求url，获取结果 </b>
     * @param url 地址
     * @param paramsMap 参数
     * @param headerMap header头参数
     * @param charset 编码
     * @return String
     * @since 1.0
     */
    public static String getContent( String url, Map<String, String> paramsMap, Map<String, String> headerMap, String charset ){
        List<NameValuePair> params = map2Nvp( paramsMap );
        return getContent( url, params, headerMap, charset );
    }

    /**
     * @Description <b> #get方式传入map参数，请求url，获取结果 </b>
     * @param url 地址
     * @param paramsMap 参数
     * @param charset 编码
     * @return String
     * @since 1.0
     */
    public static String getContent( String url, Map<String, String> paramsMap, String charset ){
        List<NameValuePair> params = map2Nvp( paramsMap );
        return getContent( url, params, null, charset );
    }

    /**
     * @Description <b> # 传入List<NameValuePair>参数，请求url，获取结果 </b>
     * @param url 地址
     * @param params 参数
     * @param charset 编码
     * @return byte[]
     * @since 1.0
     */
    public static byte[] doPost( String url, List<NameValuePair> params, String charset ){
        // 产生最终地址
        HttpPost post = new HttpPost( url );
        configRequest( post );
        CloseableHttpResponse response = null;
        byte bt[] = null;
        try{
            post.setEntity( new UrlEncodedFormEntity( params, charset ) );
            response = getHttpClient().execute( post, HttpClientContext.create() );
            // int status = response.getStatusLine().getStatusCode();
            if( response.getEntity() != null ){
                bt = EntityUtils.toByteArray( response.getEntity() );
            }
        }catch( Exception e ){
            e.printStackTrace();
            post.abort();
        }finally{
            closeResponse( response );
            post.releaseConnection();
        }
        return bt;
    }

    /**
     * @Description <b> #post方法传入map参数，请求url，获取结果 </b>
     * @param url 地址
     * @param paramsMap 参数
     * @param charset 编码
     * @return byte[]
     * @since 1.0
     */
    public static byte[] doPost( String url, Map<String, String> paramsMap, String charset ){
        List<NameValuePair> list = map2Nvp( paramsMap );
        return doPost( url, list, charset );
    }

    /**
     * @Description <b> #
     *              自定义设置header头，post传入List<NameValuePair>参数，请求url，获取结果</b>
     * @param url 地址
     * @param params 参数
     * @param headerMap header头
     * @param charset 编码
     * @return String
     * @since 1.0
     */
    public static String getPostContent( String url, List<NameValuePair> params, Map<String, String> headerMap, String charset ){
        // 产生最终地址
        HttpPost post = new HttpPost( url );
        configRequest( post );
        addHeaders( post, headerMap );
        CloseableHttpResponse response = null;
        String result = null;
        try{
            post.setEntity( new UrlEncodedFormEntity( params, charset ) );
            response = getHttpClient().execute( post, HttpClientContext.create() );
            // int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if( entity != null ){
                result = EntityUtils.toString( entity, charset );
            }
        }catch( Exception e ){
            e.printStackTrace();
            post.abort();
        }finally{
            closeResponse( response );
            post.releaseConnection();
        }
        return result;
    }

    /**
     * @Description <b> #传入map参数，请求url，获取结果 </b>
     * @param url 地址
     * @param paramsMap 参数
     * @param charset 编码
     * @return String
     * @since 1.0
     */
    public static String getPostContent( String url, Map<String, String> paramsMap, String charset ){
        List<NameValuePair> params = map2Nvp( paramsMap );
        return getPostContent( url, params, null, charset );
    }

    /**
     * @Description <b> #设置自定义header头，post传入map参数，请求url，获取结果 </b>
     * @param url
     * @param paramsMap
     * @param headerMap
     * @param charset
     * @return String
     * @since 1.0
     */
    public static String getPostContent( String url, Map<String, String> paramsMap, Map<String, String> headerMap, String charset ){
        List<NameValuePair> params = map2Nvp( paramsMap );
        return getPostContent( url, params, headerMap, charset );
    }

    /**
     * @Description <b> #下载url文件，保存到本地 </b>
     * @param url 文件地址
     * @param destFile 文件路径名称
     * @return String
     * @since 1.0
     */
    public  String download( String url, String destFile ){
        // 产生最终地址
        HttpGet get = new HttpGet( url );
        configRequest( get );
        CloseableHttpResponse response = null;
        InputStream in = null;
        FileOutputStream fout = null;
        try{
            get = new HttpGet( url );
            response = getHttpClient().execute( get );
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
            File file = new File( destFile );
            fout = new FileOutputStream( file );
            int l = -1;
            byte[] tmp = new byte[1024];
            while( (l = in.read( tmp )) != -1 ){
                fout.write( tmp, 0, l );
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
            fout.close();
        }catch( Exception e ){
            e.printStackTrace();
        }finally{
            closeResponse( response );
            get.releaseConnection();
            try{
                if( in != null ){
                    in.close();
                }
            }catch( Exception e ){
            }
            try{
                if( fout != null ){
                    fout.close();
                }
            }catch( Exception e ){
            }
        }
        return null;
    }

    /**
     * @Description <b>( 使用http请求体，如json,xml等，获取结果 )</b></br>
     * @param url 地址
     * @param requestBody 参数
     * @param charset 编码
     * @return String
     * @since 2016年2月2日
     */
    public  String sendRequestBody( String url, String requestBody, String charset ){
        // 产生最终地址
        HttpPost post = new HttpPost( url );
        configRequest( post );
        CloseableHttpResponse response = null;
        String result = null;
        try{
            StringEntity entity = new StringEntity(  charset );
            entity.setContentEncoding( charset );
            // entity.setContentType("application/json");
            post.setEntity( entity );
            response =  getHttpClient().execute( post );
            if( response.getEntity() != null ){
                result = EntityUtils.toString( response.getEntity(), charset );
            }
        }catch( Exception e ){
            e.printStackTrace();
            post.abort();
        }finally{
            closeResponse( response );
            post.releaseConnection();
        }
        return result;
    }

    /**
     * <pre>
     * <b>增加header</b>
     * </pre>
     *
     * @param request
     * @param headerMap void
     * @since 1.0
     */
    public static void addHeaders( HttpRequestBase request, Map<String, String> headerMap ){
        // 设置header
        if( headerMap != null ){
            Set<Map.Entry<String, String>> entrySet = headerMap.entrySet();
            for( Map.Entry<String, String> e : entrySet ){
                String name = e.getKey();
                String value = e.getValue();
                request.setHeader( name, value );
            }
        }
    }

    /**
     * @Description <b>( Map<String, String> 转换成 List<NameValuePair> )</b></br>
     * @param paramsMap 参数Map
     * @return List<NameValuePair>
     * @since 2016年1月13日
     */
    public static List<NameValuePair> map2Nvp( Map<String, String> paramsMap ){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> entrySet = paramsMap.entrySet();
        for( Map.Entry<String, String> e : entrySet ){
            String name = e.getKey();
            String value = e.getValue();
            NameValuePair nameValuePair = new BasicNameValuePair( name, value );
            params.add( nameValuePair );
        }
        return params;
    }

    /**
     * <pre>
     * <b>#</b>
     * </pre>
     *
     * @param response void
     * @since 1.0
     */
    public static void closeResponse( CloseableHttpResponse response ){
        try{
            if( response != null ){
                response.close();
            }
        }catch( IOException e ){
            e.printStackTrace();
        }
    }

    /**
     * @Description <b>(返回url参数字符串)</b></br>
     * @param params 参数
     * @param charset 编码
     * @return String
     * @since 2016年2月2日
     */
    public static String getQueryUrl( List<NameValuePair> params, String charset ){
        return URLEncodedUtils.format( params, charset );
    }

    /**
     * @Description <b>(Map<String,String> 拼接成url查询字符串)</b></br>
     * @param params 参数
     * @param charset 编码
     * @return String
     * @since 2016年2月2日
     */
    public static String getQueryUrl( Map<String, String> params, String charset ){
        return URLEncodedUtils.format( map2Nvp( params ), charset );
    }

    /**
     * @Description <b>(组装带queryString的地址)</b></br>
     * @param url 地址
     * @param params 参数
     * @param charset 编码
     * @return String
     * @since 2016年2月2日
     */
    public static String getQueryUrl( String url, List<NameValuePair> params, String charset ){
        String paramsStr = null;
        String realUrl = null;
        if( params != null ){
            paramsStr = URLEncodedUtils.format( params, charset );
            StringBuffer sb = new StringBuffer();
            sb.append( url ).append( "?" ).append( paramsStr );
            realUrl = sb.toString();
        }else{
            realUrl = url;
        }
        return realUrl;
    }

    /**
     * @Description <b>(组装带queryString的地址)</b></br>
     * @param url 地址
     * @param params 参数
     * @param charset 编码
     * @return String
     * @since 2016年2月2日
     */
    public static String getQueryUrl( String url, Map<String, String> params, String charset ){
        return getQueryUrl( url, map2Nvp( params ), charset );
    }
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        return ip;
    }

//    /**
//     * https 无参数get请求
//     * @param url
//     * @param charset
//     * @return
//     */
    public String doGet(String url,String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        // 产生最终地址
       // String realUrl = getQueryUrl( url, params, charset );
        HttpGet get = new HttpGet( url );
        configRequest( get );
        CloseableHttpResponse response = null;
        // 设置header
        //addHeaders( get, headerMap );
        String result = null;
        try{
            response = getHttpClient().execute( get, HttpClientContext.create() );
            // int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if( entity != null ){
                result = EntityUtils.toString( entity, charset );
            }
            EntityUtils.consume( entity );
        }catch( Exception e ){
            e.printStackTrace();
            get.abort();
        }finally{
            closeResponse( response );
            get.releaseConnection();
        }
        return result;


    }

    /**
     * 去掉html注释
     * @param node
     */

    public static void removeComments(Node node) {
        for (int i = 0; i < node.childNodeSize();) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#comment")){
                child.remove();
            }
            else {
                removeComments(child);
                i++;
            }
        }
    }
    /**
     * <pre>
     * <b>#</b>
     * </pre>
     *
     * @param args void
     * @since 1.0
     */
    public static void main( String[] args ){
        // TODO Auto-generated method stub

    }
}

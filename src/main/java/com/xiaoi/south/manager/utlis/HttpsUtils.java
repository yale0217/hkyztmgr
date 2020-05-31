//package com.xiaoi.south.manager.utlis;
//import com.alibaba.fastjson.JSONObject;
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.jsoup.nodes.Node;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.ResourceUtils;
//
//import java.io.*;
//
//import java.util.*;
//
//
//
//public class HttpsUtils {
//    private static final Logger LOGGER = LoggerFactory.getLogger( HttpsUtils.class );
//
//
//    /**
//     * 返回成功状态码
//     */
//    private static final int SUCCESS_CODE = 200;
//
//    /**
//     * 发送GET请求
//     * @param url   请求url
//     * @param nameValuePairList    请求参数
//     * @return JSON或者字符串
//     * @throws Exception
//     */
//    public static Object sendGet(String url, List<NameValuePair> nameValuePairList) throws Exception{
//        JSONObject jsonObject = null;
//        // CloseableHttpClient client = null;
//        HttpClient client = new SSLClient();
//        CloseableHttpResponse response = null;
//        try{
//            /**
//             * 创建HttpClient对象
//             */
//            client =  HttpClients.createDefault();
//            /**
//             * 创建URIBuilder
//             */
//            URIBuilder uriBuilder = new URIBuilder(url);
//            /**
//             * 设置参数
//             */
//            uriBuilder.addParameters(nameValuePairList);
//            /**
//             * 创建HttpGet
//             */
//            HttpGet httpGet = new HttpGet(uriBuilder.build());
//            /**
//             * 设置请求头部编码
//             */
//             httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
////            httpGet.setHeader(new BasicHeader("outlook.timezone","China Standard Time"));
////            httpGet.setHeader(new BasicHeader("Authorization",acces_token));
//            /**
//             * 设置返回编码
//             */
//              httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
//            /**
//             * 请求服务
//             */
//            response = (CloseableHttpResponse) client.execute(httpGet);
//            /**
//             * 获取响应吗
//             */
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            if (SUCCESS_CODE == statusCode){
//                /**
//                 * 获取返回对象
//                 */
//                HttpEntity entity = response.getEntity();
//                /**
//                 * 通过EntityUitls获取返回内容
//                 */
//                String result = EntityUtils.toString(entity,"UTF-8");
//                /**
//                 * 转换成json,根据合法性返回json或者字符串
//                 */
//                try{
//                    jsonObject = JSONObject.parseObject(result);
//                    return jsonObject;
//                }catch (Exception e){
//                    return result;
//                }
//            }else{
//                LOGGER.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
//            }
//        }catch (Exception e){
//            LOGGER.error("HttpClientService-line: {}, Exception: {}", 100, e);
//        } finally {
//            response.close();
//            ((Closeable) client).close();
//        }
//        return null;
//    }
//    public static Object doGet(String url) throws Exception {
//
//        return null;
//    }
//    /**
//     * 发送POST请求
//     * @param url
//     * @param nameValuePairList
//     * @return JSON或者字符串
//     * @throws Exception
//     */
//    @SuppressWarnings("resource")
//    public static Object sendPost(String url, List<NameValuePair> nameValuePairList,String name, String password ) throws Exception{
//        JSONObject jsonObject = null;
//        //CloseableHttpClient client = null;
//        HttpClient client = new SSLClient();
//        CloseableHttpResponse response = null;
//        try{
//            /**
//             *  创建一个httpclient对象
//             */
//            client = HttpClients.createDefault();
//            /**
//             * 创建一个post对象
//             */
//            HttpPost post = new HttpPost(url);
//            /**
//             * 包装成一个Entity对象
//             */
//            StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
//            /**
//             * 设置请求的内容
//             */
//            post.setEntity(entity);
//            /**
//             * 设置请求的报文头部的编码
//             */
//            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
//
//            /**
//             * 设置请求的报文头部的编码
//             */
//            post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
//            /**
//             * 执行post请求
//             */
//            response = (CloseableHttpResponse) client.execute(post);
//            /**
//             * 获取响应码
//             */
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (SUCCESS_CODE == statusCode){
//                /**
//                 * 通过EntityUitls获取返回内容
//                 */
//                String result = EntityUtils.toString(response.getEntity(),"UTF-8");
//                /**
//                 * 转换成json,根据合法性返回json或者字符串
//                 */
//                try{
//                    jsonObject = JSONObject.parseObject(result);
//                    return jsonObject;
//                }catch (Exception e){
//                    return result;
//                }
//            }else{
//                LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
//            }
//        }catch (Exception e){
//            LOGGER.error("HttpClientService-line: {}, Exception：{}", 149, e);
//        }finally {
//            response.close();
//            ((Closeable) client).close();
//        }
//        return null;
//    }
//
//    /**
//     * 组织请求参数{参数名和参数值下标保持一致}
//     * @param params    参数名数组
//     * @param values    参数值数组
//     * @return 参数对象
//     */
//    public static List<NameValuePair> getParams(Object[] params, Object[] values){
//        /**
//         * 校验参数合法性
//         */
//        boolean flag = params.length>0 && values.length>0 &&  params.length == values.length;
//        if (flag){
//            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
//            for(int i =0; i<params.length; i++){
//                nameValuePairList.add(new BasicNameValuePair(params[i].toString(),values[i].toString()));
//            }
//            return nameValuePairList;
//        }else{
//            LOGGER.error("HttpClientService-line: {}, errorMsg：{}", 197, "请求参数为空且参数长度不一致");
//        }
//        return null;
//    }
//
//    /**
//     * https 无参数get请求
//     * @param url
//     * @param charset
//     * @return
//     */
//    public String doGet(String url,String charset) {
//        if (null == charset) {
//            charset = "utf-8";
//        }
//        HttpClient httpClient = null;
//        HttpGet httpGet = null;
//        String result = null;
//        try {
//            httpClient = new SSLClient();
//            httpGet = new HttpGet(url);
//            HttpResponse response = httpClient.execute(httpGet);
//            response.getStatusLine().getStatusCode();
//            if (response != null) {
//                HttpEntity resEntity = response.getEntity();
//                if (resEntity != null) {
//                    result = EntityUtils.toString(resEntity, charset);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    /**
//     * 上传文件路径
//     * @return
//     */
//    public String loadUrl(){
//        File path = null;
//        try {
//            path = new File(ResourceUtils.getURL("classpath:").getPath());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        File upload = new File(path.getAbsolutePath(),"static/upload/");
//        if(!upload.exists()) {
//            upload.mkdirs();
//        }
//        System.out.println("upload url:"+upload.getAbsolutePath());
//        String url = upload.getAbsolutePath();
//        return url;
//    }
//
//    /**
//     * 获取文件夹下最新的修改的文件
//     * @param url
//     * @return
//     */
//    public String  getUrlName(String url){
//        File path=new File(url);
//    //列出该目录下所有文件和文件夹
//        File[] files = path.listFiles();
//    //按照文件最后修改日期倒序排序
//        Arrays.sort(files, new Comparator<File>() {
//            @Override
//            public int compare(File file1, File file2) {
//                return (int)(file2.lastModified()-file1.lastModified());
//            }
//        });
//    //取出第一个(即最新修改的)文件，打印文件名
//        System.out.println(files[0].getName());
//        return files[0].getName();
//    }
//    public static WebClient initWebClient(){
//        WebClient wc = new WebClient(BrowserVersion.CHROME);
//        wc.getCookieManager().setCookiesEnabled(true);
//        wc.getOptions().setJavaScriptEnabled(false);
//        wc.getOptions().setThrowExceptionOnScriptError(true);
//
//        System.out.println("USE SSL");
//        wc.getOptions().setUseInsecureSSL(true);
//
//        return wc;
//    }
//    /**
//     * 去掉html注释
//     * @param node
//     */
//
//    public static void removeComments(Node node) {
//        for (int i = 0; i < node.childNodeSize();) {
//            Node child = node.childNode(i);
//            if (child.nodeName().equals("#comment")){
//                child.remove();
//            }
//            else {
//                removeComments(child);
//                i++;
//            }
//        }
//    }
//}

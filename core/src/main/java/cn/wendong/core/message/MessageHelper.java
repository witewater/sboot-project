package cn.wendong.core.message;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.nio.charset.Charset;
/**
 * 推送工具类
 * @author MB yangtdo@qq.com
 * @date 2018-12-11
 */
public class MessageHelper {

	 /**
     * 推送消息
     * @param url
     * @param publishDto
     * @return
     */
    public BaseResult publish(String url, PublishDto publishDto) {
        HttpPost httpPost = null;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");

            HttpEntity httpEntity = new StringEntity(JSONObject.toJSONString(publishDto), "utf-8");
            httpPost.setEntity(httpEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, Charset.forName("utf-8"));
                    return JSONObject.parseObject(result, BaseResult.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return new BaseResult(-1, "error", "publish error");
    }
}

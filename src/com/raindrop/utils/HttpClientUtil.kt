package com.raindrop.utils

import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils
import java.net.URLDecoder

/**
 * @name: com.raindrop.utils.HttpClientUtil.kt
 * @description HttpClient tool
 * @author Raindrop
 * @create 2019-04-09
 */
class HttpClientUtil {

    companion object {
        private var clientInstance: HttpClientUtil? = null
        private var connectionManager: PoolingHttpClientConnectionManager? = null

        fun getHttpClient(): HttpClientUtil? {
            var tmp: HttpClientUtil? = clientInstance
            if (tmp == null) {
                synchronized(HttpClientUtil.javaClass) {
                    tmp = clientInstance
                    if (tmp == null) {
                        tmp = HttpClientUtil()
                        clientInstance = tmp
                    }
                }
            }
            return tmp
        }
    }

    private constructor() {
        connectionManager = PoolingHttpClientConnectionManager()
        connectionManager!!.setMaxTotal(200)// 连接池
        connectionManager!!.setDefaultMaxPerRoute(100)// 每条通道的并发连接数
    }

    private fun getHttpClient(connectionTimeout: Int, socketTimeOut: Int): CloseableHttpClient {
        var requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionTimeout).setSocketTimeout(socketTimeOut).build()
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build()
    }

    fun get(url: String): String {
        var httpGet = HttpGet(url.replace(" ", "%20"))
        return getResponseContent(url, httpGet)
    }

    private fun getResponseContent(url: String, request: HttpRequestBase): String {
        var response: HttpResponse? = null
        try {
            response = this.getHttpClient(2000, 2000).execute(request)
            return EntityUtils.toString(response.getEntity())
        } catch (e: Exception) {
            throw Exception("got an error from HTTP for url : " + URLDecoder.decode(url, "UTF-8"), e)
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
            request.releaseConnection();
        }
    }

}
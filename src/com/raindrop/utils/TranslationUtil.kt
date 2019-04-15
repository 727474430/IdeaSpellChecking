package com.raindrop.utils

/**
 * @name: com.raindrop.utils.StringUtil.kt
 * @description Youdao translation url generator tool
 * @author Raindrop
 * @create 2019-04-09
 */
open class TranslationUtil {

    companion object {
        /*source language*/
        private var srcLang = "en"
        /*target language*/
        private var targetLang = "zh_CH"
        /*youdao translation */
        private val url = "http://openapi.youdao.com/api?appKey=%s&from=%s&to=%s&salt=%s&sign=%s&q=%s"
        /*youdao appId*/
        var appId = "68d05da5f32670b8"
        /*youdao privateKey*/
        var privateKey = "IumgfqygmTJthj5G42gPQ1tj1pFpnyRO"

        open fun generatorUrl(appId: String, privateKey: String, query: String): String {
            val salt = System.currentTimeMillis().toString()
            val sign = (appId + query + salt + privateKey).md5()
            return String.format(url, appId, srcLang, targetLang, salt, sign, query)
        }
    }

}
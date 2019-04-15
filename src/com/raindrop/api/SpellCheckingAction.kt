package com.raindrop.api

import com.alibaba.fastjson.JSONObject
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.raindrop.utils.HttpClientUtil
import com.raindrop.utils.SpellCheckingUtils
import com.raindrop.utils.TranslationUtil
import com.raindrop.utils.splitByCamelWord
import javax.swing.ImageIcon

/**
 * @name: com.raindrop.api.SpellCheckingAction.kt
 * @description Word spell checking entrance
 * @author Raindrop
 * @create 2019-04-09
 */
class SpellCheckingAction : AnAction {

    constructor() : super(ImageIcon(SpellCheckingUtils.javaClass.classLoader.getResource("/icon/check.png")))

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        var text = editor.selectionModel.selectedText
        // Get the word where the cursor is When no word is selected
        if (text == null || text.isEmpty()) {
            editor.selectionModel.selectWordAtCaret(true)
            text = editor.selectionModel.selectedText
        }
        if (text != null && text != "") {
            // Check word spell, Get the correct word list when you get an error
            var isCorrect = SpellCheckingUtils.wordSpellIsCorrect(text)
            if (!isCorrect) {
                var wordCorrectList = SpellCheckingUtils.wordCorrectList(text, 10)
                var list = mutableListOf<String>()
                for (i in wordCorrectList.indices) {
                    var word = wordCorrectList[i]
                    var cWord = getTranslationWord(word)
                    list.add("$i. $word.$cWord\n")
                }
                // Show list popup
                showListPopup(e, editor, list)
            }
        }
    }

    /**
     * Using youdao translation api get chinese translation
     */
    private fun getTranslationWord(word: String): String {
        var result = HttpClientUtil.getHttpClient()?.get(TranslationUtil.generatorUrl(
                TranslationUtil.appId, TranslationUtil.privateKey, word.splitByCamelWord()
        ))
        var jsonObj = JSONObject.parseObject(result)
        var errorCode = jsonObj.getString("errorCode")
        var translation = jsonObj.getString("translation")
        println("Youdao Translation errorCode: $errorCode  translation: $translation")
        return if (errorCode != "0") "" else translation
    }

    /**
     * Show list pop: Including serial number、word、translation
     * Example: 1.hello.好
     *          2.word.单词
     */
    private fun showListPopup(e: AnActionEvent, editor: Editor, list: MutableList<String>) {
        var actionGroup = ActionManager.getInstance().getAction("Sample_JBPopupActionGroup") as DefaultActionGroup
        actionGroup.removeAll()
        for (s in list) {
            actionGroup.add(object : AnAction(s) {
                override fun actionPerformed(sunE: AnActionEvent) {
                    replaceWordAction(s, editor)
                }
            })
        }
        var listPopup = JBPopupFactory.getInstance().createActionGroupPopup("WordSpell", actionGroup, e.dataContext, JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false)
        listPopup.showInBestPositionFor(e.dataContext)
    }

    /**
     * Replace incorrect word
     */
    private fun replaceWordAction(source: String, editor: Editor) {
        val replacement = source.replace(" ", "")
                .replace("\n", "")
                .split(".")[1]
        var start = editor.selectionModel.selectionStart
        var end = editor.selectionModel.selectionEnd
        WriteCommandAction.runWriteCommandAction(editor.project) {
            editor.document.replaceString(start, end, replacement)
        }
        editor.selectionModel.removeSelection()
    }

}
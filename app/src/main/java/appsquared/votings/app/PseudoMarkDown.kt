package appsquared.votings.app

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.TextView
import java.util.regex.Pattern

object PseudoMarkDown {

    private var mHeadlineColor: String? = null

    fun styleTextView(
        text: String,
        textviewInput: TextView,
        headlineColor: Int,
        contentTextColor: Int
    ): TextView {
        val textTemp = text.replace(".#", " #")
        mHeadlineColor = String.format("#%06X", 0xFFFFFF and headlineColor)
        textviewInput.text = ""
        var htmlText = SpannableStringBuilder()
        htmlText = SpannableStringBuilder.valueOf(textTemp)
        var converted: Spannable = replaceMdWithH1Tags(htmlText, "\\#(.+?)\\#")
        converted = replaceMdWithItalicTags(converted, "\\_{2}(.+?)\\_{2}")
        converted = replaceMdWithBoldTags(converted, "\\*{2}(.+?)\\*{2}")
        // Escape text with html-escapes
        var escapedHtml = converted.toString()
        escapedHtml =
            escapedHtml.replace("</h1>\n\n", "</h1>") // Workaround: Remove Newlines AFTER Headline
        escapedHtml = escapedHtml.replace("\\n", "<br />") // restliche Tags umwandeln
        escapedHtml = escapedHtml.replace("\n", "<br />") // restliche Tags umwandeln

        // Display html-version of text, after everything is replaced with tags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textviewInput.text = Html.fromHtml(escapedHtml, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textviewInput.setText(Html.fromHtml(escapedHtml), TextView.BufferType.SPANNABLE)
        }
        textviewInput.setTextColor(contentTextColor)
        return textviewInput
    }

    private fun replaceMdWithH1Tags(text: Spanned, regex: String): SpannableStringBuilder {
        val sb = StringBuffer()
        val spannable = SpannableStringBuilder()
        //SpannableStringBuilder spannable = new SpannableStringBuilder(text.getSpans());

        // Headline
        val pattern =
            Pattern.compile(regex, Pattern.DOTALL)
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            sb.setLength(0) // clear
            val group = matcher.group()
            // caution, this code assumes your regex has single char delimiters
            var tmp = group.replaceFirst(
                "\\#".toRegex(),
                "<h1><font color=\"$mHeadlineColor\">"
            )
            tmp = tmp.replace(" #", "</h1>")
            val spanText = tmp.substring(0, tmp.length)
            matcher.appendReplacement(sb, spanText)
            spannable.append(sb.toString())
            //int start = spannable.length() - spanText.length();
        }
        sb.setLength(0)
        matcher.appendTail(sb)
        spannable.append(sb.toString())
        return spannable
    }

    private fun replaceMdWithBoldTags(text: Spanned, regex: String): SpannableStringBuilder {
        val sb = StringBuffer()
        val spannable = SpannableStringBuilder()
        //SpannableStringBuilder spannable = new SpannableStringBuilder(text.getSpans());
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            sb.setLength(0) // clear
            val group = matcher.group()
            // caution, this code assumes your regex has single char delimiters
            var tmp = group.replaceFirst("\\*{2}".toRegex(), "<b>")
            tmp = tmp.replace("**", "</b>")
            val spanText = tmp.substring(0, tmp.length)
            matcher.appendReplacement(sb, spanText)
            spannable.append(sb.toString())
            //int start = spannable.length() - spanText.length();
        }
        sb.setLength(0)
        matcher.appendTail(sb)
        spannable.append(sb.toString())
        return spannable
    }

    private fun replaceMdWithItalicTags(
        text: Spanned,
        regex: String
    ): SpannableStringBuilder {
        val sb = StringBuffer()
        val spannable = SpannableStringBuilder()
        //SpannableStringBuilder spannable = new SpannableStringBuilder(text.getSpans());
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            sb.setLength(0) // clear
            val group = matcher.group()
            // caution, this code assumes your regex has single char delimiters
            var tmp = group.replaceFirst("\\_{2}".toRegex(), "<i>")
            tmp = tmp.replace("__", "</i>")
            val spanText = tmp.substring(0, tmp.length)
            matcher.appendReplacement(sb, spanText)
            spannable.append(sb.toString())
            //int start = spannable.length() - spanText.length();
        }
        sb.setLength(0)
        matcher.appendTail(sb)
        spannable.append(sb.toString())
        return spannable
    }
}
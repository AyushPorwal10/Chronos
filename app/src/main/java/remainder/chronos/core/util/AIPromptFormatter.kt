package remainder.chronos.core.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object AIPromptFormatter {

    fun formatToMotivationalQuote(prompt : String ) : String {
        val aiPrompt = "Give me a motivational quote for title $prompt Just quote with no extra words"
        val encodedPrompt = URLEncoder.encode(aiPrompt, StandardCharsets.UTF_8.toString())
        return encodedPrompt
    }

    fun formatToShareableText(prompt: String): String{
        val aiPrompt = "$prompt Give me a concise response with no extra words."
        val encodedPrompt = URLEncoder.encode(aiPrompt, StandardCharsets.UTF_8.toString())
        return encodedPrompt
    }
}
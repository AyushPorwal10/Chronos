package remainder.chronos.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response


interface AiMessageRepository {
    suspend fun getAiMotivationalMessage(prompt: String) :Response<ResponseBody>
}
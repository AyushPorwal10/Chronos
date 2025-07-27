package remainder.chronos.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AiMessageApiService {

    @GET("prompt/{prompt}")
    suspend fun getAiMotivationalMessage(
        @Path ("prompt", encoded = true)prompt : String
    ) : Response<ResponseBody>
}
package remainder.chronos.data.repoimpl

import okhttp3.ResponseBody
import remainder.chronos.data.remote.AiMessageApiService
import remainder.chronos.domain.repository.AiMessageRepository
import retrofit2.Response
import javax.inject.Inject

class AiMessageRepositoryImpl @Inject constructor(
    private val aiMessageApiService: AiMessageApiService
) : AiMessageRepository{


    override suspend fun getAiMotivationalMessage(prompt: String): Response<ResponseBody> {
     return aiMessageApiService.getAiMotivationalMessage(prompt)
    }

}
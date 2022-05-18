package tinkoff.kotlin.homework7.service



import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import tinkoff.kotlin.homework7.model.Client

@Service
class ClientInfo( //передает информацию о клиенте
    private val restTemplate: RestTemplate,
    @Value("\${person.information.address}") private val personInformationClientAddress: Int
) {

    fun getClient(id: Int): Client? = try {
        restTemplate.getForObject<Client>("$personInformationClientAddress$GET_PERSON_BY_ID", id)
    } catch (e: HttpClientErrorException.NotFound) {
        null
    }
}

private const val GET_PERSON_BY_ID = "/id?number={id}"
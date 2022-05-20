package tinkoff.kotlin.homework7

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tinkoff.kotlin.homework7.model.Client
import tinkoff.kotlin.homework7.service.ClientImage
import tinkoff.kotlin.homework7.service.ClientInfo
import java.time.LocalDate
import kotlin.text.Charsets.UTF_8

@SpringBootTest
@AutoConfigureMockMvc
class ClientServiceTest(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) : FeatureSpec() {

    @MockkBean
    private lateinit var infoClient: ClientInfo

    @SpykBean
    private lateinit var image: ClientImage
    private companion object { //тестовые пользователи
        private val client1 = Client("Liss", "Mo", LocalDate.of(1985, 10, 10), 1, 10)
        private val client2 = Client("Lissy", "Mi", LocalDate.of(1997, 12, 19), 111111,20)
        private val client3 = Client("Lissa", "Ma", LocalDate.of(1995, 5, 8), 222222, 30)
        private val client4 = Client("Lisa", "My", LocalDate.of(1995, 4, 5), 333333, 35)
        private val client5 = Client("Elisabeth", "Me", LocalDate.of(1999, 3, 9), 777777, 345)

        private const val notExistId=0
        private const val minusId = -111111
    }
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    override suspend fun beforeSpec(spec: Spec) {
        image.saveClient(client1)
        image.saveClient(client2)
        image.saveClient(client3)
        image.saveClient(client4)
        image.saveClient(client5)
        every { infoClient.getClient(client1.id) } returns client1
        every { infoClient.getClient(notExistId) } returns null
    }

    init {
        feature("добавление пользователя") {
            mockMvc.get("/get/{notExistId}", notExistId).andReturn().response.status shouldBe HttpStatus.BAD_REQUEST.value()
                val client = adding(client1.id)
                client shouldBe client5
            mockMvc.get("/get/{client1.id}", client1.id).andReturn().response.status shouldBe HttpStatus.OK.value()
        }
        feature("получение пользователя по id") {
                val client = getByID(client1.id)
                client shouldBe client1
        }
        feature("пагинационный лист") {
            scenario("выдает страницу") {
                val actualList = pagg("Liss", 2, 2)
                val expectedList = listOf(client1, client3)
                actualList shouldBe expectedList
            }
            scenario("успешно, выдает страницу") {
                val actualList = pagg("Liss", 20, 20)
                val expectedList = emptyList<Client>()
                actualList shouldBe expectedList
            }
            scenario("ошибка") {
                paggStatus("Liss", 0, 1) shouldBe
                        HttpStatus.BAD_REQUEST.value()
                paggStatus("Liss", 1, 0) shouldBe
                        HttpStatus.BAD_REQUEST.value()
            }
        }
    }

    private fun adding(id: Int): Client =
        mockMvc.post("/add") { contentType = MediaType.TEXT_PLAIN; content = id }.readResponse()

    private fun addStatus(id: Int): Int =
        mockMvc.post("/add") { contentType = MediaType.TEXT_PLAIN; content = id }
            .andReturn().response.status

    private fun getByID(id: Int): Client =
        mockMvc.get("/get/{id}", id).readResponse()


    private fun pagg(name: String, pageSize: Int, page: Int): List<Client> =
        mockMvc.get("/find?name={name}&pageSize={pageSize}&page={page}", name, pageSize, page).readResponse()

    private fun paggStatus(name: String, pageSize: Int, page: Int): Int =
        mockMvc.get("/find?name={name}&pageSize={pageSize}&page={page}", name, pageSize, page)
            .andReturn().response.status

    private inline fun <reified T> ResultActionsDsl.readResponse(expectedStatus: HttpStatus = HttpStatus.OK): T =
        this.andExpect { status { isEqualTo(expectedStatus.value()) } }.andReturn().response.getContentAsString(UTF_8)
            .let { if (T::class == String::class) it as T else objectMapper.readValue(it) }
}

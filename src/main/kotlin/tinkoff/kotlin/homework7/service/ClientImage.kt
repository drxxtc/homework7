package tinkoff.kotlin.homework7.service


import org.springframework.stereotype.Repository
import tinkoff.kotlin.homework7.model.Client
import java.util.concurrent.ConcurrentHashMap
//сервис для работы с получением опредленного клиента и информации о нем
@Repository
class ClientImage {
    private val clients: ConcurrentHashMap<Int, Client> = ConcurrentHashMap()

    fun saveClient(client: Client) {
        clients[client.id] = client
    }

    fun clientById(id: Int): Client? {
        return clients[id]
    }

    fun clientPagination(name: String, pageSize: Int, page: Int): List<Client> =
        clients.asSequence()
            .map { it.value }
            .filter { it.name == name }
            .sortedBy { it.surname }
            .drop(pageSize * (page - 1))
            .take(pageSize)
            .toList()
}
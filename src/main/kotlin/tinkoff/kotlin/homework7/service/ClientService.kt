package tinkoff.kotlin.homework7.service

import org.springframework.stereotype.Service
import tinkoff.kotlin.homework7.model.Client


@Service
class ClientService( //проверка клиента
    private val infoClient: ClientInfo,
    private val clientImage: ClientImage
) {

    fun addClient(id: Int): Client {
        val client = infoClient.getClient(id)
        checkNotNull(client) { "Клиента $id нет в базе данных!" }
        clientImage.saveClient(client)
        return client
    }

    fun getClientById(id: Int): Client {
        val client = clientImage.clientById(id)
        return requireNotNull(client) { "Клиента $client нет в базе!" }
    }

    fun ClientWithPagination(name: String, pageSize: Int, page: Int): List<Client> {
        require(page > 0 && pageSize > 0) { "Неккоректный номер" }
        return clientImage.clientPagination(name, pageSize, page)
    }

    private fun validateNum(id: Int) {
        require(id >= 0) {
            "Некорректный номер"
        }
    }

}
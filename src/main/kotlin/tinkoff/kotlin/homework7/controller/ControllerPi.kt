package controller
import org.springframework.web.bind.annotation.*
import tinkoff.kotlin.homework7.model.Client
import tinkoff.kotlin.homework7.service.ClientService

@RestController
@RequestMapping("/")
class ClientController(private val clientService: ClientService) {

    //создание нового объекта
    @PostMapping("/add")
    fun addClient(@RequestBody id: Int): Client =
        clientService.addClient(id)
    //получение объекта по id
    @GetMapping("/get/{id}")
    fun getClient(@PathVariable id: Int): Client? =
        clientService.getClientById(id)

    //поиск объектов по параметрам с паgинацией
    @GetMapping("/find")
    fun ClientWithPagination(
        @RequestParam name: String,
        @RequestParam pageSize: Int,
        @RequestParam page: Int
    ): List<Client> =
        clientService.ClientWithPagination(name, pageSize, page)
}
/*В контроллере должны быть следующие эндпоинты:

создание нового объекта (POST + @RequestBody) - возвращает готовый объект
получение объекта по id (GET + @PathVariable)
поиск объектов по параметрам с пажинацией (GET + @RequestParam)*/
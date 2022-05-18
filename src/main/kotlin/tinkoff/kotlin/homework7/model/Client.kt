package tinkoff.kotlin.homework7.model
import java.time.LocalDate
//клиент, его счет кошелька
data class Client(
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val id: Int,
    val balance: Int
)
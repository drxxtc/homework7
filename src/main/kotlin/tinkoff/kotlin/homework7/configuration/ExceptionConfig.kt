package tinkoff.kotlin.homework7.configuration


import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdviceConfig {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(e: IllegalArgumentException): String? {
        log.warn(e.message, e)
        return e.message
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleIllegalStateException(e: IllegalStateException): String? {
        log.warn(e.message, e)
        return e.message
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): String {
        log.error(e.message, e)
        return "Error of connect"
    }

    companion object {
        private val log = LoggerFactory.getLogger(ControllerAdviceConfig::class.java)
    }
}

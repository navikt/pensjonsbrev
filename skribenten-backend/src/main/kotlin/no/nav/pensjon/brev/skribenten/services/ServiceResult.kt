package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.*

sealed class ServiceResult<Result> {
    data class Ok<Result>(val result: Result) : ServiceResult<Result>()
    data class Error<Result>(val error: String, val statusCode: HttpStatusCode) : ServiceResult<Result>()

    inline fun <T> map(func: (Result) -> T): ServiceResult<T> = when (this) {
        is Ok -> Ok(func(result))
        is Error -> Error(error, statusCode)
    }

    inline fun <T> then(func: (Result) -> ServiceResult<T>): ServiceResult<T> = when (this) {
        is Ok -> func(result)
        is Error -> Error(error, statusCode)
    }

    inline fun <T, Second> then(second: ServiceResult<Second>, func: (Result, Second) -> ServiceResult<T>): ServiceResult<T> =
        when (this) {
            is Ok -> second.then { func(result, it) }
            is Error -> Error(error, statusCode)
        }

    inline fun <T, Second, Third> then(
        second: ServiceResult<Second>,
        third: ServiceResult<Third>,
        func: (Result, Second, Third) -> ServiceResult<T>
    ): ServiceResult<T> =
        when (this) {
            is Ok -> second.then(third) { secondResult, thirdResult -> func(result, secondResult, thirdResult) }
            is Error -> Error(error, statusCode)
        }

    inline fun catch(func: (String, HttpStatusCode) -> Result): Result = when (this) {
        is Ok -> result
        is Error -> func(error, statusCode)
    }

    inline fun onOk(func: (Result) -> Unit): ServiceResult<Result> {
        if (this is Ok) func(result)
        return this
    }

    inline fun onError(func: (error: String, statusCode: HttpStatusCode) -> Unit): ServiceResult<Result> {
        if (this is Error) func(error, statusCode)
        return this
    }

    fun resultOrNull(): Result? =
        if (this is Ok) result else null

    fun nonNull(error: String, statusCode: HttpStatusCode): ServiceResult<Result & Any> = when (this) {
        is Ok -> result?.let { Ok(result) } ?: Error(error, statusCode)
        is Error -> Error(this.error, this.statusCode)
    }

}

suspend inline fun <reified R> PipelineContext<Unit, ApplicationCall>.respondWithResult(
    result: ServiceResult<R>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { if (it != null) respond(it) else respond(HttpStatusCode.NotFound) },
    noinline onError: suspend ApplicationCall.(String) -> Unit = { message ->
        respond(HttpStatusCode.InternalServerError, message)
    }
) {
    when (result) {
        is ServiceResult.Ok -> call.onOk(result.result)
        is ServiceResult.Error -> call.onError(result.error)
    }
}


suspend inline fun <reified R> HttpResponse.toServiceResult(): ServiceResult<R> =
    toServiceResult { ServiceResult.Error(it.bodyAsText(), it.status) }

suspend inline fun <reified R> HttpResponse.toServiceResult(noinline errorHandler: suspend (HttpResponse) -> ServiceResult<R>): ServiceResult<R> =
    if (status.isSuccess()) {
        ServiceResult.Ok(body())
    } else {
        errorHandler(this)
    }

suspend inline fun <reified R> AuthorizedHttpClientResult.toServiceResult(noinline errorHandler: (suspend (HttpResponse) -> ServiceResult<R>)? = null): ServiceResult<R> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult.Error(
            "Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}",
            HttpStatusCode.Unauthorized
        )

        is AuthorizedHttpClientResult.Response -> errorHandler?.let { response.toServiceResult(it) } ?: response.toServiceResult()
    }
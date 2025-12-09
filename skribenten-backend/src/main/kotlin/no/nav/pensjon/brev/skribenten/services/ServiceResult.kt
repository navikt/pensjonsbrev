package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

sealed class ServiceResult<Result> {
    data class Ok<Result>(val result: Result) : ServiceResult<Result>()
    data class Error<Result>(val error: String, val statusCode: HttpStatusCode, val tittel: String? = null) : ServiceResult<Result>()

    inline fun <T> map(func: (Result) -> T): ServiceResult<T> = when (this) {
        is Ok -> Ok(func(result))
        is Error -> Error(error,  statusCode, tittel)
    }

    inline fun <T> then(func: (Result) -> ServiceResult<T>): ServiceResult<T> = when (this) {
        is Ok -> func(result)
        is Error -> Error(error, statusCode, tittel)
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

    inline fun onError(func: (error: String, statusCode: HttpStatusCode, tittel: String?) -> Unit): ServiceResult<Result> {
        if (this is Error) func(error,  statusCode, tittel)
        return this
    }

    fun resultOrNull(): Result? =
        if (this is Ok) result else null

}

suspend inline fun <reified R> HttpResponse.toServiceResult(): ServiceResult<R> =
    toServiceResult { ServiceResult.Error(it.bodyAsText(), it.status) }

suspend inline fun <reified R> HttpResponse.toServiceResult(noinline errorHandler: suspend (HttpResponse) -> ServiceResult<R>): ServiceResult<R> =
    if (status.isSuccess()) {
        ServiceResult.Ok(body())
    } else {
        errorHandler(this)
    }

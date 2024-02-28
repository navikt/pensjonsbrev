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

    inline fun catch(func: (String, HttpStatusCode) -> Result): Result = when (this) {
        is Ok -> result
        is Error -> func(error, statusCode)
    }

    fun resultOrNull(): Result? =
        if (this is Ok) result else null

}

suspend inline fun <reified R : Any> PipelineContext<Unit, ApplicationCall>.respondWithResult(
    result: ServiceResult<R>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { respond(it) },
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
    if (status.isSuccess()) {
        ServiceResult.Ok(body())
    } else {
        ServiceResult.Error(body(), status)
    }


suspend inline fun <reified R> AuthorizedHttpClientResult.toServiceResult(): ServiceResult<R> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult.Error("Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}", HttpStatusCode.Unauthorized)
        is AuthorizedHttpClientResult.Response -> response.toServiceResult()
    }
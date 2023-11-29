package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.*

sealed class ServiceResult<out Result: Any, out Err: Any> {
    data class Ok<out Result: Any, Error: Any>(val result: Result) : ServiceResult<Result, Error>()
    data class AuthorizationError<out Result: Any, Error: Any>(val error: TokenResponse.ErrorResponse): ServiceResult<Result, Error>()
    data class Error<out Result: Any, Error: Any>(val error: Error, val statusCode: HttpStatusCode?): ServiceResult<Result, Error>()

    inline fun <T : Any> map(func: (Result) -> T): ServiceResult<T, Err> = when (this) {
        is Ok -> Ok(func(result))
        is Error -> Error(error, statusCode)
        is AuthorizationError -> AuthorizationError(error)
    }

    inline fun <T : Any> catch(func: (Err) -> T): ServiceResult<Result, T> = when (this) {
        is Ok -> Ok(result)
        is Error -> Error(func(error), statusCode)
        is AuthorizationError -> AuthorizationError(error)
    }
}

suspend inline fun <reified R : Any, reified E : Any> PipelineContext<Unit, ApplicationCall>.respondWithResult(
    result: ServiceResult<R, E>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { respond(it) },
    noinline onAuthErr: suspend ApplicationCall.(TokenResponse.ErrorResponse) -> Unit = { respond(HttpStatusCode.Forbidden, it) },
    noinline onError: suspend ApplicationCall.(E, HttpStatusCode?) -> Unit = { body, upstreamStatus -> respond(upstreamStatus ?: HttpStatusCode.BadRequest, body) }
) {
    when (result) {
        is ServiceResult.Ok -> call.onOk(result.result)
        is ServiceResult.AuthorizationError -> call.onAuthErr(result.error)
        is ServiceResult.Error -> call.onError(result.error, result.statusCode)
    }
}

suspend inline fun <reified R : Any, reified E : Any> HttpResponse.toServiceResult(): ServiceResult<R, E> =
    if (status.isSuccess()) {
        ServiceResult.Ok(body())
    } else {
        ServiceResult.Error(body(), status)
    }

suspend inline fun <reified R : Any, reified E : Any> AuthorizedHttpClientResult.toServiceResult(): ServiceResult<R, E> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult.AuthorizationError(error)
        is AuthorizedHttpClientResult.Response -> response.toServiceResult()
    }
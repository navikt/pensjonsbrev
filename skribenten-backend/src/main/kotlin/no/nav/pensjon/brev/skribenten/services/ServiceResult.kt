package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.*

sealed class ServiceResult<out Result: Any, out Err: Any> {
    data class Ok<out Result: Any, Error: Any>(val result: Result) : ServiceResult<Result, Error>()
    data class AuthorizationError<out Result: Any, Error: Any>(val error: TokenResponse.ErrorResponse): ServiceResult<Result, Error>()
    data class Error<out Result: Any, Error: Any>(val error: Error): ServiceResult<Result, Error>()

    inline fun <T : Any> map(func: (Result) -> T): ServiceResult<T, Err> = when (this) {
        is Ok -> Ok(func(this.result))
        is Error -> Error(this.error)
        is AuthorizationError -> AuthorizationError(this.error)
    }

    inline fun <T : Any> catch(func: (Err) -> T): ServiceResult<Result, T> = when (this) {
        is Ok -> Ok(result)
        is Error -> Error(func(error))
        is AuthorizationError -> AuthorizationError(this.error)
    }
}

suspend inline fun <reified R : Any, reified E : Any> PipelineContext<Unit, ApplicationCall>.respondWithResult(
    result: ServiceResult<R, E>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { respond(it) },
    noinline onAuthErr: suspend ApplicationCall.(TokenResponse.ErrorResponse) -> Unit = { respond(HttpStatusCode.Forbidden, it) },
    noinline onError: suspend ApplicationCall.(E) -> Unit = { respond(HttpStatusCode.BadRequest, it) }
) {
    when (result) {
        is ServiceResult.Ok -> call.onOk(result.result)
        is ServiceResult.AuthorizationError -> call.onAuthErr(result.error)
        is ServiceResult.Error -> call.onError(result.error)
    }
}

suspend inline fun <reified R : Any, reified E : Any> AuthorizedHttpClientResult.toServiceResult(): ServiceResult<R, E> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult.AuthorizationError(error)
        is AuthorizedHttpClientResult.Response ->
            if (response.status.isSuccess()) {
                ServiceResult.Ok(response.body())
            } else {
                ServiceResult.Error(response.body())
            }
    }
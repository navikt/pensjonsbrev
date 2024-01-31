package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.auth.*

@Deprecated(
    message = "Depricated due to error messages generally being strings when status is not 200 ok",
    replaceWith = ReplaceWith("no.nav.pensjon.brev.skribenten.services.ServiceResult2")
)
sealed class ServiceResult<out Result : Any, out Err : Any> {
    data class Ok<out Result : Any, Error : Any>(val result: Result) : ServiceResult<Result, Error>()
    data class AuthorizationError<out Result : Any, Error : Any>(val error: TokenResponse.ErrorResponse) : ServiceResult<Result, Error>()
    data class Error<out Result : Any, Error : Any>(val error: Error, val statusCode: HttpStatusCode?) : ServiceResult<Result, Error>()

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

// TODO rename to ServiceResult when all usages of it are removed
sealed class ServiceResult2<Result : Any> {
    data class Ok<Result : Any>(val result: Result) : ServiceResult2<Result>()
    data class Error<Result : Any>(val error: String, val statusCode: HttpStatusCode) : ServiceResult2<Result>()

    inline fun <T : Any> map(func: (Result) -> T): ServiceResult2<T> = when (this) {
        is Ok -> Ok(func(result))
        is Error -> Error(error, statusCode)
    }

    inline fun catch(func: (String, HttpStatusCode) -> Result): Result = when (this) {
        is Ok -> result
        is Error -> func(error, statusCode)
    }
}

suspend inline fun <reified R : Any, reified E : Any> PipelineContext<Unit, ApplicationCall>.respondWithResult(
    result: ServiceResult<R, E>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { respond(it) },
    noinline onAuthErr: suspend ApplicationCall.(TokenResponse.ErrorResponse) -> Unit = { respond(HttpStatusCode.Forbidden, it) },
    noinline onError: suspend ApplicationCall.(E, HttpStatusCode?) -> Unit = { body, upstreamStatus ->
        respond(
            upstreamStatus ?: HttpStatusCode.BadRequest, body
        )
    }
) {
    when (result) {
        is ServiceResult.Ok -> call.onOk(result.result)
        is ServiceResult.AuthorizationError -> call.onAuthErr(result.error)
        is ServiceResult.Error -> call.onError(result.error, result.statusCode)
    }
}

suspend inline fun <reified R : Any> PipelineContext<Unit, ApplicationCall>.respondWithResult2(
    result: ServiceResult2<R>,
    noinline onOk: suspend ApplicationCall.(R) -> Unit = { respond(it) },
    noinline onError: suspend ApplicationCall.(String) -> Unit = { message ->
        respond(HttpStatusCode.InternalServerError, message)
    }
) {
    when (result) {
        is ServiceResult2.Ok -> call.onOk(result.result)
        is ServiceResult2.Error -> call.onError(result.error)
    }
}


@Deprecated(
    message = "Depricated due to error messages generally being strings when status is not 200 ok",
    replaceWith = ReplaceWith("no.nav.pensjon.brev.skribenten.services.toServiceResult2")
)
suspend inline fun <reified R : Any, reified E : Any> HttpResponse.toServiceResult(): ServiceResult<R, E> =
    if (status.isSuccess()) {
        ServiceResult.Ok(body())
    } else {
        ServiceResult.Error(body(), status)
    }

@Deprecated(
    message = "Depricated due to error messages generally being strings when status is not 200 ok",
    replaceWith = ReplaceWith("no.nav.pensjon.brev.skribenten.services.toServiceResult2")
)
suspend inline fun <reified R : Any, reified E : Any> AuthorizedHttpClientResult.toServiceResult(): ServiceResult<R, E> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult.AuthorizationError(error)
        is AuthorizedHttpClientResult.Response -> response.toServiceResult()
    }

suspend inline fun <reified R : Any> HttpResponse.toServiceResult2(): ServiceResult2<R> =
    if (status.isSuccess()) {
        ServiceResult2.Ok(body())
    } else {
        ServiceResult2.Error(body(), status)
    }


suspend inline fun <reified R : Any> AuthorizedHttpClientResult.toServiceResult2(): ServiceResult2<R> =
    when (this) {
        is AuthorizedHttpClientResult.Error -> ServiceResult2.Error("Feil ved token-utveksling correlation_id: ${error.correlation_id} Description:${error.error_description}", HttpStatusCode.Unauthorized)
        is AuthorizedHttpClientResult.Response -> response.toServiceResult2()
    }
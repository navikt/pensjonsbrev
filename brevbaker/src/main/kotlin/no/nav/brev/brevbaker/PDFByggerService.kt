package no.nav.brev.brevbaker

import no.nav.brev.brevbaker.HttpStatusCodes.Companion.BadRequest
import no.nav.brev.brevbaker.HttpStatusCodes.Companion.InternalServerError
import no.nav.brev.brevbaker.HttpStatusCodes.Companion.ServiceUnavailable
import no.nav.pensjon.brev.PDFRequest

class LatexCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexTimeoutException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class LatexInvalidException(msg: String, cause: Throwable? = null) : Exception(msg, cause)

interface PDFByggerService {
    suspend fun producePDF(pdfRequest: PDFRequest, url: String = PATH): PDFCompilationOutput

    suspend fun validateResponse(statusCode: HttpStatusCodes, logWarning: (msg: String) -> Unit, getBody: suspend () -> String) {
        when (statusCode.code) {
            BadRequest.code -> {
                val body = getBody()
                logWarning("Rendered latex is invalid, couldn't compile pdf: $body")
                throw LatexInvalidException("Rendered latex is invalid, couldn't compile pdf: $body")
            }

            InternalServerError.code -> {
                val body = getBody()
                logWarning("Couldn't compile latex to pdf due to server error: $body")
                throw LatexCompileException("Couldn't compile latex to pdf due to server error: $body")
            }

            ServiceUnavailable.code -> {
                val body = getBody()
                logWarning("Service unavalailable - couldn't compile latex to pdf: $body")
                throw LatexCompileException("Service unavalailable - couldn't compile latex to pdf: $body")
            }
        }
    }
}

private const val PATH = "/produserBrev"
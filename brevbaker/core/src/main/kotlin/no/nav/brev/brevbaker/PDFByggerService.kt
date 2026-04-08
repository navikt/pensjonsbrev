package no.nav.brev.brevbaker

import no.nav.brev.brevbaker.HttpStatusCodes.*
import no.nav.pensjon.brev.PDFRequest

class PDFCompileException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class PDFTimeoutException(msg: String, cause: Throwable? = null) : Exception(msg, cause)
class PDFInvalidException(msg: String, cause: Throwable? = null) : Exception(msg, cause)

interface PDFByggerService {
    enum class TypstFeatureToggle { REDIGERBAR, AUTO }

    suspend fun producePDF(pdfRequest: PDFRequest, path: String = PATH, shouldRetry: Boolean, typstFeatureToggle: TypstFeatureToggle? = null): PDFCompilationOutput

    suspend fun validateResponse(statusCode: Int, logWarning: (msg: String) -> Unit, getBody: suspend () -> String) {
        when (statusCode) {
            BadRequest.code -> {
                val body = getBody()
                logWarning("Rendered content is invalid, couldn't compile pdf: $body")
                throw PDFInvalidException("Rendered content is invalid, couldn't compile pdf: $body")
            }

            InternalServerError.code -> {
                val body = getBody()
                logWarning("Couldn't compile pdf due to server error: $body")
                throw PDFCompileException("Couldn't compile pdf due to server error: $body")
            }

            ServiceUnavailable.code -> {
                val body = getBody()
                logWarning("Service unavailable - couldn't compile pdf: $body")
                throw PDFCompileException("Service unavailable - couldn't compile pdf: $body")
            }
        }
    }
}

private const val PATH = "/produserBrev"

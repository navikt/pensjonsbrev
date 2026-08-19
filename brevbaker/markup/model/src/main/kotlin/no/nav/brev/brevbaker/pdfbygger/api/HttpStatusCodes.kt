package no.nav.brev.brevbaker.pdfbygger.api

enum class HttpStatusCodes(val code: Int, val description: String) {
    BadRequest(400, "Bad Request"),
    InternalServerError(500, "Internal Server Error"),
    ServiceUnavailable(503, "Service Unavailable")
}
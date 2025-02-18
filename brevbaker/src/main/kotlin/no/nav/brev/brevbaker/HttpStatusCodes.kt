package no.nav.brev.brevbaker

data class HttpStatusCodes(val code: Int, val description: String) {
    companion object {
        val BadRequest = HttpStatusCodes(400, "Bad Request")
        val InternalServerError = HttpStatusCodes(500, "Internal Server Error")
        val ServiceUnavailable = HttpStatusCodes(503, "Service Unavailable")
    }
}
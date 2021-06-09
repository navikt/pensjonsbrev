package no.nav.pensjon.brevmaker

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@CrossOrigin
@RequestMapping("api")
class BrevMakerRestController(val brevBakerService: BrevMakerService) {

    @PostMapping(value = ["/letter/{letterType}"], produces = [MediaType.APPLICATION_PDF_VALUE])
    fun testBrev(@RequestBody fields: Map<String, String>, @PathVariable letterType: String): ByteArray {

        val letterTemplate = (LetterTemplate.fromId(letterType)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid letter type"))

        return brevBakerService.lagBrev(letterTemplate, fields)
    }

    @GetMapping("/letterTypes")
    fun brevTyper() = LetterTemplate.values().map { it.letterId }

    @GetMapping("/letterFields/{letterType}")
    fun letterFields(@PathVariable("letterType") letterType: String) =
        LetterTemplate.fromId(letterType)?.fields?.let { fields -> fields.map { it.typeKey }}
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid letter type")

}
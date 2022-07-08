package no.nav.pensjon.brev.api

import com.fasterxml.jackson.module.kotlin.convertValue
import io.ktor.server.plugins.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.template.jacksonObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

val objectMapper = jacksonObjectMapper()

class LetterResourceTest {

    private val testLetterResource = LetterResource()
    private val template = OmsorgEgenAuto
    private val omsorgEgenAutoDto = objectMapper.convertValue<Map<String, Any>>(Fixtures.create<OmsorgEgenAutoDto>())

    @Test
    fun `create finds correct template`() {
        val letter =
            testLetterResource.create(
                VedtaksbrevRequest(
                    template.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )

        assertEquals(template.template, letter.template)
    }

    @Test
    fun `create fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource(TemplateResource(setOf(OmsorgEgenAuto))).create(
                VedtaksbrevRequest(
                    UngUfoerAuto.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        }
    }

    @Test
    fun `create requires arguments`() {
        assertThrows<ParseLetterDataException> {
            testLetterResource.create(
                VedtaksbrevRequest(
                    template.kode,
                    emptyMap<String, String>(),
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        }
    }

    @Test
    fun `create parses arguments`() {
        println(objectMapper.readValue(objectMapper.writeValueAsString(Fixtures.create(OmsorgEgenAutoDto::class)), OmsorgEgenAutoDto::class.java))
        val letter =
            testLetterResource.create(
                VedtaksbrevRequest(
                    template.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        assertEquals(Fixtures.felles.avsenderEnhet.returAdresse, letter.felles.avsenderEnhet.returAdresse)
    }

    @Test
    fun `create fails when letterData is invalid`() {
        assertThrows<ParseLetterDataException> {
            testLetterResource.create(VedtaksbrevRequest(template.kode, mapOf("pensjonInnvilget" to true), Fixtures.felles, LanguageCode.BOKMAL))
        }
    }

    @Test
    fun `create fails for unsupported language`() {
        assertThrows<BadRequestException> {
            testLetterResource.create(
                VedtaksbrevRequest(
                    UngUfoerAuto.kode,
                    Fixtures.create(UngUfoerAutoDto::class),
                    Fixtures.felles,
                    LanguageCode.ENGLISH,
                )
            )
        }
    }

    // TODO: Remove `deprecated create` tests when pesys supports new create
    @Test
    fun `deprecated create finds correct template`() {
        val letter =
            testLetterResource.create(
                LetterRequest(
                    template.template.name,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )

        assertEquals(template.template, letter.template)
    }

    @Test
    fun `deprecated create fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            testLetterResource.create(
                LetterRequest(
                    "non existing",
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        }
    }

    @Test
    fun `deprecated create requires arguments`() {
        assertThrows<ParseLetterDataException> {
            testLetterResource.create(
                LetterRequest(
                    template.template.name,
                    emptyMap<String, String>(),
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        }
    }

    @Test
    fun `deprecated create parses arguments`() {
        println(objectMapper.readValue(objectMapper.writeValueAsString(Fixtures.create(OmsorgEgenAutoDto::class)), OmsorgEgenAutoDto::class.java))
        val letter =
            testLetterResource.create(
                LetterRequest(
                    template.template.name,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        assertEquals(Fixtures.felles.avsenderEnhet.returAdresse, letter.felles.avsenderEnhet.returAdresse)
    }

    @Test
    fun `deprecated create fails when letterData is invalid`() {
        assertThrows<ParseLetterDataException> {
            testLetterResource.create(LetterRequest(template.template.name, mapOf("pensjonInnvilget" to true), Fixtures.felles, LanguageCode.BOKMAL))
        }
    }

    @Test
    fun `deprecated create fails for unsupported language`() {
        assertThrows<BadRequestException> {
            testLetterResource.create(
                LetterRequest(
                    UngUfoerAuto.kode.brevkoder.first(),
                    Fixtures.create(UngUfoerAutoDto::class),
                    Fixtures.felles,
                    LanguageCode.ENGLISH,
                )
            )
        }
    }

}
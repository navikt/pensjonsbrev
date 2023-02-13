package no.nav.pensjon.brev.api

import com.fasterxml.jackson.module.kotlin.convertValue
import io.ktor.server.plugins.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.jacksonObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

val objectMapper = jacksonObjectMapper()

class LetterResourceTest {
    private val testLetterResource = LetterResource()
    private val vedtakTemplate = OmsorgEgenAuto
    private val omsorgEgenAutoDto = objectMapper.convertValue<Map<String, Any>>(Fixtures.create<OmsorgEgenAutoDto>())
    private val redigerbarTemplate = InformasjonOmSaksbehandlingstid
    private val redigerbarData = objectMapper.convertValue<Map<String, Any>>(Fixtures.create<InformasjonOmSaksbehandlingstidDto>())

    @Test
    fun `create vedtaksbrev finds correct template`() {
        val letter =
            testLetterResource.create(
                AutobrevRequest(
                    vedtakTemplate.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )

        assertEquals(vedtakTemplate.template, letter.template)
    }

    @Test
    fun `create vedtaksbrev fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource(TemplateResource(vedtaksbrevTemplates = setOf(OmsorgEgenAuto))).create(
                AutobrevRequest(
                    UngUfoerAuto.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        }
    }

    @Test
    fun `create redigerbart finds correct template`() {
        val letter =
            testLetterResource.create(
                RedigerbartbrevRequest(
                    redigerbarTemplate.kode,
                    redigerbarData,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )

        assertEquals(redigerbarTemplate.template, letter.template)
    }

    @Test
    fun `create redigerbart fails when template doesnt exist`() {
        assertThrows<NotFoundException> {
            LetterResource(TemplateResource(redigerbareTemplates = emptySet())).create(
                RedigerbartbrevRequest(
                    redigerbarTemplate.kode,
                    redigerbarData,
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
                AutobrevRequest(
                    vedtakTemplate.kode,
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
                AutobrevRequest(
                    vedtakTemplate.kode,
                    omsorgEgenAutoDto,
                    Fixtures.felles,
                    LanguageCode.BOKMAL
                )
            )
        assertEquals(Fixtures.felles.avsenderEnhet.navn, letter.felles.avsenderEnhet.navn)
    }

    @Test
    fun `create fails when letterData is invalid`() {
        assertThrows<ParseLetterDataException> {
            testLetterResource.create(AutobrevRequest(vedtakTemplate.kode, mapOf("pensjonInnvilget" to true), Fixtures.felles, LanguageCode.BOKMAL))
        }
    }

    @Test
    fun `create fails for unsupported language`() {
        assertThrows<BadRequestException> {
            testLetterResource.create(
                AutobrevRequest(
                    UngUfoerAuto.kode,
                    Fixtures.create(UngUfoerAutoDto::class),
                    Fixtures.felles,
                    LanguageCode.ENGLISH,
                )
            )
        }
    }

}
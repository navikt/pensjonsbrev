package no.nav.pensjon.brev.api

import com.fasterxml.jackson.databind.JavaType
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.jvm.jvmErasure

private val objectMapper = brevbakerJacksonObjectMapper()

class LetterFactory<Kode: Brevkode<Kode>>(alltidValgbareVedlegg: Set<AlltidValgbartVedlegg<*>>) {
    private val vedleggLibrary = AlltidValgbartVedleggLibrary(alltidValgbareVedlegg)


    fun createLetter(brevbestilling: BestillBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template,kode, letterData, language, felles, listOf()) }

    fun createLetter(brevbestilling: BestillRedigertBrevRequest<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template, kode, letterData, language, felles, alltidValgbareVedlegg) }

    fun createLetter(brevbestilling: BestillRedigertBrevRequestV2<Kode>, template: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?) =
        with(brevbestilling) { createLetter(template, kode, letterData, language, felles, alltidValgbareVedlegg) }

    private fun createLetter(
        brevTemplate: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>?,
        brevkode: Kode,
        brevdata: BrevbakerBrevdata,
        spraak: LanguageCode,
        felles: BrevbakerFelles,
        valgteVedlegg: List<AlltidValgbartVedleggKode>
    ): Letter<BrevbakerBrevdata> {
        val template = brevTemplate?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

        val language = spraak.toLanguage()
        if (!template.language.supports(language)) {
            throw BadRequestException("Template '${brevkode}' doesn't support language: ${template.language}")
        }

        val vedlegg = vedleggLibrary.getVedlegg(valgteVedlegg)
        vedlegg.forEach {
            require(it.kode.spraak.contains(spraak)) {
                "Vedlegg '${it.kode}' støtter ikke språk $spraak"
            }
        }

        @OptIn(InterneDataklasser::class)
        return LetterImpl(
            template = template.medEkstraVedlegg(vedlegg.map { it.asIncludeAttachment() }),
            argument = parseArgument(brevdata, brevTemplate),
            language = language,
            felles = felles,
        )
    }

    private fun parseArgument(
        letterData: BrevbakerBrevdata,
        brevTemplate: BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>,
    ): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, brevTemplate.letterDataJavaType())
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }

    /**
     * `template.letterDataType` er alltid den rå (generic-løse) `RedigerbarBrevdata`-typen for
     * [RedigerbarTemplate]: den er reifisert fra det statiske typeparameteret på [BrevTemplate], og JVM-ens
     * type erasure lar det kollapse uansett hvilken konkrete `FagData` malen faktisk bruker. Konverterer vi
     * `letterData` (på dette tidspunktet en generisk, felt-for-felt-lik kopi bygget av
     * [no.nav.pensjon.brev.converters.BrevbakerBrevdataModule] under den første JSON-deserialiseringen) til
     * den rå typen, får vi derfor bare en ny generisk kopi tilbake - `pesysData` blir aldri den konkrete
     * Pesys-dataklassen. Vi bygger derfor en presist parameterisert [JavaType] ved å reflektere over malens
     * konkrete klasse for å finne `FagData`, slik at Jackson kan binde typeparameteret riktig.
     */
    private fun BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>.letterDataJavaType(): JavaType =
        if (this is RedigerbarTemplate<*>) {
            objectMapper.typeFactory.constructParametricType(RedigerbarBrevdata::class.java, fagsystemDataType().java)
        } else {
            objectMapper.typeFactory.constructType(template.letterDataType.java)
        }

    // TODO: Dette er komplekse greier, håper vi kan få til noko lettare
    @Suppress("UNCHECKED_CAST")
    private fun RedigerbarTemplate<*>.fagsystemDataType(): KClass<out FagsystemBrevdata> =
        this::class.allSupertypes
            .first { it.classifier == RedigerbarTemplate::class }
            .arguments
            .single()
            .type
            ?.jvmErasure as? KClass<out FagsystemBrevdata>
            ?: error("Fant ikke FagData-typen (typeparameteret til RedigerbarTemplate) for ${this::class.simpleName}")
}
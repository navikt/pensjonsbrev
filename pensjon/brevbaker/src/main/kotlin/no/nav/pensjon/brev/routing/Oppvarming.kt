package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import no.nav.brev.brevbaker.Brevbaker
import no.nav.brev.brevbaker.PDFByggerService
import no.nav.pensjon.brev.api.LetterFactory
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Telefonnummer
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.routing.Oppvarming")

internal fun varmOpp(
    maler: Set<RedigerbarTemplate<*>>,
    objectMapper: ObjectMapper,
    pdfByggerService: PDFByggerService,
): Boolean {
    val mal = maler.firstOrNull { it.template.letterDataType == EmptyRedigerbarBrevdata::class }
    if (mal == null) {
        logger.warn("Fant ingen mal med EmptyRedigerbarBrevdata å varme opp med")
        return false
    }
    return try {
        val varighet = measureTime {
            val bestilling = BestillBrevRequest(
                kode = mal.kode,
                letterData = EmptyRedigerbarBrevdata(saksbehandlerValg = IngenSaksbehandlervalg),
                felles = BrevbakerFelles(
                    dokumentDato = LocalDate.now(),
                    saksnummer = "1",
                    avsenderEnhet = BrevbakerFelles.NavEnhet(
                        nettside = "nav.no",
                        navn = "Nav",
                        telefonnummer = Telefonnummer("55553333"),
                    ),
                    bruker = BrevbakerFelles.Bruker(
                        foedselsnummer = Foedselsnummer("01010100000"),
                        fornavn = "Oppvarming",
                        mellomnavn = null,
                        etternavn = "Oppvarming",
                    ),
                    annenMottakerNavn = null,
                    signerendeSaksbehandlere = BrevbakerFelles.SignerendeSaksbehandlere(saksbehandler = "Oppvarming"),
                ),
                language = mal.template.language.all().first().toCode(),
            )
            val mottatt = objectMapper.readValue(
                objectMapper.writeValueAsBytes(bestilling),
                jacksonTypeRef<BestillBrevRequest<Brevkode.Redigerbart>>(),
            )
            @Suppress("UNCHECKED_CAST")
            val brev = LetterFactory<Brevkode.Redigerbart>(emptySet())
                .createLetter(mottatt, mal as BrevTemplate<BrevbakerBrevdata, out Brevkode<*>>)
            val brevbaker = Brevbaker(pdfByggerService)
            objectMapper.writeValueAsBytes(brevbaker.renderLetterMarkupWithDataUsage(brev))
            objectMapper.writeValueAsBytes(brevbaker.renderLetterMarkupWithDataUsageV2(brev))
        }
        logger.info("Oppvarming med ${mal.kode.kode()} ferdig på ${varighet.inWholeMilliseconds} ms")
        true
    } catch (e: Exception) {
        logger.warn("Oppvarming feilet", e)
        false
    }
}

private object IngenSaksbehandlervalg : SaksbehandlervalgIDSL, Map<String, Any?> by emptyMap()

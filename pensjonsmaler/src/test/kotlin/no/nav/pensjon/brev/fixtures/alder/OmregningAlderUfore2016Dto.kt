package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.PersongrunnlagAvdod
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOmregningAlderUfore2016Dto() =
        OmregningAlderUfore2016Dto(
            virkFom = LocalDate.now(),
            uttaksgrad = 100,
            totalPensjon = Kroner(1000),
            antallBeregningsperioder = 2,
            faktiskBostedsland = "Skottland",
            gjenlevendetilleggKap19Innvilget = true,
            gjenlevenderettAnvendt = true,
            inngangOgEksportVurdering = InngangOgEksportVurdering(
                eksportTrygdeavtaleAvtaleland = true,

                erEksportberegnet = true,
                eksportberegnetUtenGarantipensjon = true,
                borINorge = true,
                erEOSLand = true,
                eksportTrygdeavtaleEOS = true,
                avtaleland = "Australia",
                oppfyltVedSammenleggingKap19 = true,
                oppfyltVedSammenleggingKap20 = true,
                oppfyltVedSammenleggingFemArKap19 = true,
                oppfyltVedSammenleggingFemArKap20 = true,
            ),
            pensjonstilleggInnvilget = true,
            garantipensjonInnvilget = true,
            godkjentYrkesskade = true,
            skjermingstilleggInnvilget = true,
            garantitilleggInnvilget = true,
            innvilgetFor67 = true,
            fullTrygdetid = true,
            persongrunnlagAvdod = PersongrunnlagAvdod(
                avdodNavn = "Per",
                avdodFnr = "13377331"
            ),
            informasjonOmMedlemskap = InformasjonOmMedlemskap.IKKE_RELEVANT,
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
            opplysningerBruktIBeregningenAlderDto = createOpplysningerBruktIBeregningAlderDto(),
            opplysningerOmAvdoedBruktIBeregningDto = null,
            maanedligPensjonFoerSkattAlderspensjonDto = createMaanedligPensjonFoerSkattAlderspensjonDto(),
            opplysningerBruktIBeregningenAlderAP2025Dto = createOpplysningerBruktIBeregningAlderAP2025Dto()
        )


package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.Sivilstand
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurdering
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.PersongrunnlagAvdod
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createOmregningAlderUfore2016Dto() =
    OmregningAlderUfore2016Dto(
        virkFom = vilkaarligDato,
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
        opplysningerBruktIBeregningenAlderAP2025Dto = createOpplysningerBruktIBeregningAlderAP2025Dto(),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
        brukersSivilstand = Sivilstand.SEPARERT,
        borMedSivilstand = BorMedSivilstand.GIFT_LEVER_ADSKILT,
        over2G = false,
        kronebelop2G = Kroner(400000),
    )


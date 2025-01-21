package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.Barnetillegg.Fellesbarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.Barnetillegg.Saerkullsbarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpplysningerOmEtteropgjoeretTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggOpplysningerOmEtteroppgjoeret,
            OpplysningerOmEtteroppgjoeretDto(
                periode = Year(2022),
                harGjenlevendeTillegg = true,
                ufoeretrygd = AvviksResultat(skulleFaatt = Kroner(10), fikk = Kroner(14), avvik = Kroner(-4), harFaattForMye = true),
                barnetillegg = Barnetillegg(
                    felles = Fellesbarn(
                        sivilstand = BorMedSivilstand.EKTEFELLE,
                        grunnbelop = Kroner(117_000),
                        fribeloep = Kroner(1),
                        resultat = AvviksResultat(skulleFaatt = Kroner(10), fikk = Kroner(14), avvik = Kroner(-4), harFaattForMye = true),
                        personinntektAnnenForelder = InntektOgFratrekk(
                            inntekt = Inntekt(
                                inntekter = listOf(
                                    InntektLinje(InntektLinje.InntektType.ARBEIDSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN, Kroner(10)),
                                    InntektLinje(InntektLinje.InntektType.NAERINGSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                                    InntektLinje(InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                                ),
                                sum = Kroner(30),
                            ),
                            fratrekk = Fratrekk(fratrekk = emptyList(), sum = Kroner(0))
                        ),
                        harSamletInntektOverInntektstak = true,
                        inntektstakSamletInntekt = Kroner(10),
                        samletInntekt = Kroner(12),
                    ),
                    saerkull = Saerkullsbarn(
                        fribeloep = Kroner(1),
                        resultat = AvviksResultat(skulleFaatt = Kroner(10), fikk = Kroner(14), avvik = Kroner(-4), harFaattForMye = true),
                        harSamletInntektOverInntektstak = true,
                        inntektstakSamletInntekt = Kroner(8),
                        samletInntekt = Kroner(10),
                    ),
                    personinntekt = InntektOgFratrekk(
                        inntekt = Inntekt(
                            inntekter = listOf(
                                InntektLinje(InntektLinje.InntektType.ARBEIDSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN, Kroner(10)),
                                InntektLinje(InntektLinje.InntektType.NAERINGSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                                InntektLinje(InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                                InntektLinje(InntektLinje.InntektType.UFOERETRYGD, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                            ),
                            sum = Kroner(40),
                        ),
                        fratrekk = Fratrekk(fratrekk = emptyList(), sum = Kroner(0))
                    ),
                    mindreEnn40AarTrygdetid = true,
                    totaltResultat = AvviksResultat(
                        skulleFaatt = Kroner(20),
                        fikk = Kroner(28),
                        avvik = Kroner(-8),
                        harFaattForMye = true,
                    )
                ),
                harFaattForMye = true,
                totaltAvvik = Kroner(4),
                pensjonsgivendeInntektBruktIBeregningen = Kroner(100),
                pensjonsgivendeInntekt = InntektOgFratrekk(
                    inntekt = Inntekt(
                        inntekter = listOf(
                            InntektLinje(InntektLinje.InntektType.ARBEIDSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN, Kroner(10)),
                            InntektLinje(InntektLinje.InntektType.NAERINGSINNTEKT, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                            InntektLinje(InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET, InntektLinje.Kilde.OPPGITT_AV_BRUKER, Kroner(10)),
                        ),
                        sum = Kroner(30),
                    ),
                    fratrekk = Fratrekk(
                        fratrekk = listOf(
                            FratrekkLinje(FratrekkLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER, FratrekkLinje.Aarsak.FOER_INNVILGET_UFOERETRYGD, Kroner(10))
                        ),
                        sum = Kroner(10),
                    ),
                ),
            ).expr(),
            languages(Bokmal),
        )
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerOmEtteroppgjoeret")

    }
}




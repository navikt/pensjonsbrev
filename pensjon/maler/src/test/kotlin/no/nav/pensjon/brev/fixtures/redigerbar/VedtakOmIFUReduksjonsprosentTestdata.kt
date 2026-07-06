package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.UTTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakOmIFUReduksjonsprosentRedigerbarDto
import no.nav.pensjon.brev.fixtures.createDineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakOmIFUReduksjonsprosentAutoDto() =
    VedtakOmIFUReduksjonsprosentAutoDto(
        vedtakData = VedtakOmIFUReduksjonsprosentData(
            beregningFomDato = LocalDate.of(2026, Month.JULY, 1),
            nettoUforetrygdUtenTillegg = Kroner(26000),
            nettoBarnetillegg = Kroner(4000),
            nettoGjenlevendetillegg = Kroner(0),
            totalbelop = Kroner(30000),
            etterbetalingJuli = Kroner(1500),
            reduksjonsprosent = 50.0,
            inntektstak = Kroner(500000),
            ifu = Kroner(300000),
            tillegg = listOf(UTTillegg.BT, UTTillegg.GJT),
            endringNettoUforetrygdUtenTillegg = true,
            endringNettoBarnetillegg = true,
            endringNettoGjenlevendetillegg = false,
            endringInntektstak = true,
            erInntektsavkortet = true,
            hjemler = setOf("12-13", "12-16", "12-18", "22-12"),
            pe = createPEgruppe10(),
            maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            inntektsgrense = Kroner(30000),
            endringInntektsgrense = true,
            uforegrad = 100,
            endringUforegrad = true
        )
    )

fun createVedtakOmIFUReduksjonsprosentRedigerbarDto() =
    VedtakOmIFUReduksjonsprosentRedigerbarDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmIFUReduksjonsprosentRedigerbarDto.PesysData(
            vedtakData = VedtakOmIFUReduksjonsprosentData(
                beregningFomDato = LocalDate.of(2026, Month.JULY, 1),
                nettoUforetrygdUtenTillegg = Kroner(26000),
                nettoBarnetillegg = Kroner(4000),
                nettoGjenlevendetillegg = Kroner(0),
                totalbelop = Kroner(30000),
                etterbetalingJuli = Kroner(1500),
                reduksjonsprosent = 50.0,
                inntektstak = Kroner(500000),
                ifu = Kroner(300000),
                tillegg = listOf(UTTillegg.BT, UTTillegg.GJT),
                endringNettoUforetrygdUtenTillegg = true,
                endringNettoBarnetillegg = true,
                endringNettoGjenlevendetillegg = false,
                endringInntektstak = true,
                erInntektsavkortet = true,
                hjemler = setOf("12-13", "12-16", "12-18", "22-12"),
                pe = createPEgruppe10(),
                maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
                dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
                inntektsgrense = Kroner(30000),
                endringInntektsgrense = true,
                uforegrad = 100,
                endringUforegrad = true
            )
        )
    )
package no.nav.pensjon.brev.alder.maler.aldersovergang

import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.aldersovergang.AFPPrivatBeregning
import no.nav.pensjon.brev.alder.model.aldersovergang.OpptjeningInfo
import no.nav.pensjon.brev.alder.model.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakEndringAFPEndretOpptjeningAutoDto(): VedtakEndringAFPEndretOpptjeningAutoDto =
    VedtakEndringAFPEndretOpptjeningAutoDto(
        virkFom = LocalDate.of(2024, 7, 1),
        opptjeningType = OpptjeningType.KORRIGERING,
        belopEndring = BeloepEndring.ENDR_OKT,
        afpPrivatBeregningVedVirk = AFPPrivatBeregning(totalPensjon = Kroner(5678)),
        afpPrivatBeregningGjeldende = AFPPrivatBeregning(totalPensjon = Kroner(5678)),
        endretOpptjening =
            OpptjeningInfo(
                sisteGyldigeOpptjeningsAar = 2022,
                antallAarEndretOpptjening = 2,
                endretOpptjeningsAar = listOf(2023, 2020, 2021),
            ),
        maanedligPensjonFoerSkattAFP =
            MaanedligPensjonFoerSkattAFPDto(
                afpPrivatBeregningGjeldende =
                    MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregning(
                        datoFom = LocalDate.of(2024, 7, 1),
                        datoTil = null,
                        afpLivsvarigNetto = Kroner(10),
                        kronetilleggNetto = Kroner(20),
                        komptilleggNetto = Kroner(30),
                        totalPensjon = Kroner(60),
                    ),
                krav =
                    MaanedligPensjonFoerSkattAFPDto.Krav(
                        virkDatoFom = LocalDate.of(2024, 7, 1),
                    ),
                afpPrivatBeregningListe =
                    MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregningListe(
                        antallBeregningsperioder = 2,
                        afpPrivatBeregningListe =
                            listOf(
                                MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregning(
                                    datoFom = LocalDate.of(2023, 6, 1),
                                    datoTil = LocalDate.of(2024, 7, 1),
                                    afpLivsvarigNetto = Kroner(100),
                                    kronetilleggNetto = Kroner(200),
                                    komptilleggNetto = Kroner(300),
                                    totalPensjon = Kroner(600),
                                ),
                                MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregning(
                                    datoFom = LocalDate.of(2024, 7, 1),
                                    datoTil = null,
                                    afpLivsvarigNetto = Kroner(10),
                                    kronetilleggNetto = Kroner(20),
                                    komptilleggNetto = Kroner(30),
                                    totalPensjon = Kroner(60),
                                ),
                            ),
                    ),
                opptjeningType = OpptjeningType.KORRIGERING,
                belopEndring = BeloepEndring.ENDR_OKT,
            ),
    )

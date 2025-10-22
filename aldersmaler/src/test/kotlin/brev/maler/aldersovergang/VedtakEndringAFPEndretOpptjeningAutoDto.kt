package brev.maler.aldersovergang

import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.AFPPrivatBeregning
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningInfo
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakEndringAFPEndretOpptjeningAutoDto(): VedtakEndringAFPEndretOpptjeningAutoDto =
    VedtakEndringAFPEndretOpptjeningAutoDto(
        virkFom = LocalDate.of(2024, 7, 1),
        opptjeningType = OpptjeningType.KORRIGERING,
        belopEndring = BeloepEndring.ENDR_OKT,
        afpPrivateBeregningVedVirk = AFPPrivatBeregning(totalPensjon = Kroner(5678)),
        afpPrivatBeregningGjeldende = AFPPrivatBeregning(totalPensjon = Kroner(5678)),
        endretOpptjening =
            OpptjeningInfo(
                sisteGyldigeOpptjeningsAar = 2022,
                antallAarEndretOpptjening = 2,
                endretOpptjeningsAar = setOf(2021, 2022),
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
                    MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregingListe(
                        antallBeregningsperioder = 2,
                        afpPrivatBeregingListe =
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

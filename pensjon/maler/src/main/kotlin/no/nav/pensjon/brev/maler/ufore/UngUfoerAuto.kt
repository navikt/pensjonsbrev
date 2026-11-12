package no.nav.pensjon.brev.maler.ufore

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.api.model.maler.selectors.ungUfoerAutoDto.barnetillegg.*
import no.nav.pensjon.brev.api.model.maler.selectors.ungUfoerAutoDto.innvilgetTillegg.*
import no.nav.pensjon.brev.api.model.maler.selectors.ungUfoerAutoDto.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.ufore.fraser.Barnetillegg
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// BrevTypeKode: PE_BA_04_505
@TemplateModelHelpers
object UngUfoerAuto : AutobrevTemplate<UngUfoerAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_UNG_UFOER_20_AAR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Nav har regnet om uføretrygden din" },
                nynorsk { + "Nav har endra uføretrygda di" },
            )
        }

        outline {

            title1 {
                text(
                    bokmal { + "Vedtak" },
                    nynorsk { + "Vedtak" },
                )
            }
            paragraph {
                val formatertDato = kravVirkningFraOgMed.format()
                text(
                    bokmal { + "Vi har økt uføretrygden din fra " + formatertDato + " fordi du fyller 20 år. Du vil nå få utbetalt uføretrygd med rettighet som ung ufør." },
                    nynorsk { + "Vi har auka uføretrygda di frå " + formatertDato + " fordi du fyller 20 år. Du får no utbetalt uføretrygd med rett som ung ufør." },
                )
            }

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = totaltUfoerePerMnd,
                    ufoeretrygd = true.expr(),
                    ektefelle = ektefelle.safe { utbetalt }.ifNull(false),
                    gjenlevende = gjenlevende.safe { utbetalt }.ifNull(false),
                    fellesbarn = fellesbarn.safe { utbetalt }.ifNull(false),
                    saerkullsbarn = saerkullsbarn.safe { utbetalt }.ifNull(false),
                )
            )

            includePhrase(
                Barnetillegg.BarnetilleggIkkeUtbetalt(
                    saerkullInnvilget = saerkullsbarn.notNull(),
                    saerkullUtbetalt = saerkullsbarn.safe { utbetalt }.ifNull(false),
                    harFlereSaerkullsbarn = saerkullsbarn.safe { gjelderFlereBarn }.ifNull(false),
                    inntektstakSaerkullsbarn = saerkullsbarn.safe { inntektstak }.ifNull(Kroner(0)),
                    fellesInnvilget = fellesbarn.notNull(),
                    fellesUtbetalt = fellesbarn.safe { utbetalt }.ifNull(false),
                    harFlereFellesBarn = fellesbarn.safe { gjelderFlereBarn }.ifNull(false),
                    inntektstakFellesbarn = fellesbarn.safe { inntektstak }.ifNull(Kroner(0)),
                )
            )

            title1 {
                text(
                    bokmal { + "Begrunnelse for vedtaket" },
                    nynorsk { + "Grunngiving for vedtaket" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har tidligere fått innvilget uføretrygd som ung ufør. Fra og med den måneden du fyller 20 år har du rett til høyere minsteytelse." },
                    nynorsk { + "Du har tidlegare innvilga rett som ung ufør i uføretrygda di. Frå og med månaden du fyller 20 år har du rett til høgare minsteyting." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Sivilstanden din avgjør hva du kan få i minsteytelse som ung ufør:" },
                    nynorsk { + "Sivilstanden din avgjer kva du kan få i minsteyting som ung ufør:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "Er du enslig, er minste årlige uføretrygd 2,91 ganger folketrygdens grunnbeløp." },
                            nynorsk { + "Er du einsleg, er minste årlege uføretrygd 2,91 gangar grunnbeløpet i folketrygda." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "Lever du sammen med en ektefelle eller samboer, er minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp." },
                            nynorsk { + "Lever du saman med ein ektefelle eller sambuar, er minste årlege yting 2,66 gangar grunnbeløpet i folketrygda." }
                        )
                    }
                }
            }

            paragraph {
                val satsFormatert = minsteytelseVedVirkSats.format()
                text(
                    bokmal { + "Du får derfor en årlig ytelse som utgjør "
                        + satsFormatert + " ganger grunnbeløpet." },

                    nynorsk { + "Du får derfor ei årleg yting som utgjer "
                        + satsFormatert + " gangar grunnbeløpet." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12." },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12." },
                )
            }

            title1 {
                text(
                    bokmal { +"Dette er virkningstidspunktet ditt" },
                    nynorsk { +"Dette er verknadstidspunktet ditt" },
                )
            }
            includePhrase(Ufoeretrygd.VirkningFraOgMed(kravVirkningFraOgMed))

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}
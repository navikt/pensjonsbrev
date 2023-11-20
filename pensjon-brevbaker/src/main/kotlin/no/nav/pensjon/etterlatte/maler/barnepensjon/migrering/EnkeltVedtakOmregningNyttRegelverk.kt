package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.anvendtTrygdetid
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltEtterReform
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltFoerReform
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet

@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverk : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utkast til - endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Utkast til vedtak - endring av barnepensjon",
                Language.Nynorsk to "",
                Language.English to "Draft decision – adjustment of children's pension",
            )
        }
        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to "Stortinget har vedtatt nye regler for barnepensjon. Du får høyere barnepensjon. Du har ".expr()
                            + utbetaltFoerReform.format() + " kroner per måned i pensjon til 31. desember 2023. Du får " + utbetaltEtterReform.format()
                            + " kroner før skatt per måned fra 1. januar 2024.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "The Norwegian Parliament has adopted new rules for children's pensions. ".expr() +
                            "Your children's pension will now increase. " +
                            "You will receive pension payments amounting to " + utbetaltFoerReform.format() + " kroner per month until 31 December 2023. " +
                            "The gross (pre-tax) amount you will receive is NOK " + utbetaltEtterReform.format() + " per month starting 1 January 2024."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter lov 18. desember 2020 nr. 139 om endringer i folketrygdloven del II nr. 4, jf. Folketrygdloven § 18-4 og § 18-5.",
                    Language.Nynorsk to "",
                    Language.English to "The decision was made pursuant to Act #139 of 18 December 2020 concerning Amendments to the National Insurance Act, Part II #4; cf. sections 18-4 and 18-5 of the National Insurance Act."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr de nye reglene for deg?",
                    Language.Nynorsk to "",
                    Language.English to "What do the new rules mean to you?"
                )
            }

            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "Barnepensjonen din øker.",
                            Language.Nynorsk to "",
                            Language.English to "Your children's pension will increase."
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Du får barnepensjon til du blir 20 år.",
                            Language.Nynorsk to "",
                            Language.English to "You will receive a children's pension until you turn 20."
                        )
                    }
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Slik har vi beregnet pensjonen din",
                    Language.Nynorsk to "",
                    Language.English to "This is how we calculated your pension"
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Barnepensjonen din blir lik en ganger folketrygdens grunnbeløp per år. Grunnbeløpet i januar 2024 er ".expr() +
                            grunnbeloep.format() + " kroner. Dette deles på 12 måneder. Du vil derfor få " +
                            utbetaltEtterReform.format() + " kroner per måned før skatt fra 1. januar 2024.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "Your children's pension will equal one times the National Insurance basic amount (G) per year. ".expr() +
                            "The basic amount in January 2024 is " + grunnbeloep.format() + " kroner. " +
                            "This is divided by 12 months. You will therefore receive NOK <kronebeløp> per month (pre-tax) starting 1 January 2024."
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to "For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det beregnet framtidig trygdetid. Det er vanligvis fram til og med det året avdøde ville ha fylt 66 år. Barnepensjonen din er beregnet med ".expr() + anvendtTrygdetid.format() + " år trygdetid.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "To be entitled to a full pension, the deceased must have accumulated at least 40 years of national insurance coverage. The period of national insurance coverage equals the number of years the deceased has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. If the deceased was less than 67 years old at the time of death, a calculation is made for what would have remained of the future period of national insurance coverage. This is usually calculated up to the year in which the deceased would have turned 66. Your children's pension is calculated based on a period of national insurance coverage of ".expr() + anvendtTrygdetid.format() + " years."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Skattetrekk på barnepensjon",
                    Language.Nynorsk to "",
                    Language.English to "Tax deductions on children's pensions"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du gir beskjed om det. Du kan lese mer om skatt på barnepensjon på nav.no/skatt-pengestotte. Her finner du også informasjon om frivillig skattetrekk og hvordan du får registrert dette.",
                    Language.Nynorsk to "",
                    Language.English to "Children's pensions are taxable, but we do not deduct tax without being notified. You can read more about the taxation of children's pensions online: nav.no/skatt-pengestotte. Here you will also find information about voluntary tax withholding and how to register this."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "",
                    Language.English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra 1. januar 2024. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
                    Language.Nynorsk to "",
                    Language.English to "If you believe the decision is incorrect, you can appeal within six weeks after 1 January 2024. The appeal must be in writing. You can find the form and information online: nav.no/klage."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Du må melde fra om endringer",
                    Language.Nynorsk to "",
                    Language.English to "You must report any changes"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for utbetalingen av barnepensjon, eller retten til å få barnepensjon. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Language.Nynorsk to "",
                    Language.English to "You are obligated to notify us of any changes that affect the payment of a children's pension, or the right to receive a children's pension. You will see which changes you must report in the attachment, Your Rights and Obligations."
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "",
                    Language.English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du finner mer informasjon på nav.no/barnepensjon. Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon 55 55 33 34 hverdager 9-15. Om du oppgir fødselsnummer til barnet, kan vi lettere gi deg rask og god hjelp.",
                    Language.Nynorsk to "",
                    Language.English to "For more information, visit us online: nav.no/barnepensjon. If you cannot find the answer to your question, you can call us by phone (55 55 33 34) weekdays 9-15. If you provide your child's national identity number, we can more easily provide you with quick and good help."
                )
            }
        }
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, this.argument)
        includeAttachment(dineRettigheterOgPlikter, this.argument)
    }
}
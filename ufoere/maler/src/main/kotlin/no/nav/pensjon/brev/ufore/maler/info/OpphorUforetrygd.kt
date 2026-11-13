package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.info.OpphorUforetrygdDto
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OpphorUforetrygd : RedigerbarTemplate<OpphorUforetrygdDto> {
    override val kode = Ufoerebrevkoder.Redigerbar.UT_OPPHOR_UFORETRYGD
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val featureToggle = FeatureToggles.opphorUforetrygd.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Opphør uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        val arsak = saksbehandlervalg("arsak", "Velg årsak").enum<OpphorUforetrygdDto.Arsak>()
        val opphorTilbakeITid = saksbehandlervalg("opphorTilbakeITid", "Opphør tilbake i tid").bool()

        title {
            text(
                bokmal { +"Vi har opphørt uføretrygden din" },
                nynorsk { +"Vi har opphøyrd uføretrygda di" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Utbetalingen av uføretrygden din er stanset fra " + fritekst("dato") + ", og du har ikke lenger rett til uføretrygd." },
                    nynorsk { +"Utbetalinga av uføretrygda di er stansa frå " + fritekst("dato") + ", og du har ikkje lenger rett til uføretrygd." },
                )
            }
            title1 {
                text(
                    bokmal { +"Derfor opphører uføretrygden din" },
                    nynorsk { +"Derfor opphøyrer uføretrygda di" },
                )
            }
            showIf(arsak.equalTo(OpphorUforetrygdDto.Arsak.FLYTTET_FRA_NORGE)) {
                paragraph {
                    text(
                        bokmal { +"Vi har opphørt uføretrygden din fra " + fritekst("dato") + ", fordi du har flyttet fra Norge og bor i et land som Norge ikke har trygdeavtale med." },
                        nynorsk { +"Vi har opphøyrd uføretrygda di frå " + fritekst("dato") + ", fordi du har flytta frå Noreg og bur i eit land som Noreg ikkje har trygdeavtale med." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"For å ha rett til uføretrygd må du bo i et land Norge har trygdeavtale med." },
                        nynorsk { +"For å ha rett til uføretrygd må du bu i eit land Noreg har trygdeavtale med." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du oppfyller ikke lenger vilkårene for uføretrygd, og vi opphører derfor uføretrygden din." },
                        nynorsk { +"Du oppfyller ikkje lenger vilkåra for uføretrygd, og vi opphøyrer derfor uføretrygda di." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 12-3 og 22-13." },
                        nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 12-3 og 22-13." },
                    )
                }
            }.orShowIf(arsak.equalTo(OpphorUforetrygdDto.Arsak.EGET_ONSKE)) {
                paragraph {
                    text(
                        bokmal { +"Vi har opphørt uføretrygden din fordi du har tatt kontakt med oss " + fritekst("dato bruker tok kontakt") + " med ønske om at uføretrygden din opphøres fra " + fritekst("ønsket opphørsdato") + "." },
                        nynorsk { +"Vi har opphøyrd uføretrygda di fordi du har teke kontakt med oss " + fritekst("dato bruker tok kontakt") + " med ønske om at uføretrygda di vert opphøyrd frå " + fritekst("ønsket opphørsdato") + "." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +fritekst("Fritekst") },
                        nynorsk { +fritekst("Fritekst") },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter §§ " + fritekst("hjemmel") + "." },
                        nynorsk { +"Vedtaket er gjort etter §§ " + fritekst("hjemmel") + "." },
                    )
                }
            }.orShowIf(arsak.equalTo(OpphorUforetrygdDto.Arsak.IKKE_MOTTATT_LEVEATTEST)) {
                paragraph {
                    text(
                        bokmal { +"Vi opphører uføretrygden din fordi vi ikke har fått utfylt leveattest." },
                        nynorsk { +"Vi opphøyrer uføretrygda di fordi vi ikkje har fått utfylt leveattest." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi sendte  " + fritekst("Navn") + " et brev " + fritekst("dato for varselbrev") + " hvor vi ba om utfylt leveattest. Vi har ikke fått disse opplysningene innen fristen, som var " + fritekst("frist for retur av leveattest") + "." },
                        nynorsk { +"Vi sende  " + fritekst("Navn") + "  eit brev " + fritekst("dato for varselbrev") + "  der  vi  bad om utfylt  leveattest. Vi har ikkje  fått  desse opplysningane innan fristen, som var " + fritekst("frist for retur av  leveattest") + "." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"For å ha rett på uføretrygd må personer som bor i utlandet årlig sende utfylt leveattest til Nav. Vi innhenter leveattest for å sikre at uføretrygden utbetales til rett person." },
                        nynorsk { +"For å ha rett på uføretrygd må personar som bur i utlandet  årleg  sende utfylt  leveattest  til Nav. Vi innhentar  leveattest  for å sikre at uføretrygda vert utbetalt til rett person." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 21-3, 21-7 bokstav c og 22-12." },
                        nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 21-3, 21-7 bokstav c og 22-12." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du som etterlatt etter " + fritekst("Navn") + " leser dette brevet, ber vi deg om å varsle norske myndigheter om dødsfallet." },
                        nynorsk { +"Dersom du som etterlaten  etter " + fritekst("Navn") + "  les dette brevet, ber vi deg om å varsle norske styresmakter om dødsfallet." },
                    )
                }
            }
            showIf(arsak.isOneOf(OpphorUforetrygdDto.Arsak.EGET_ONSKE, OpphorUforetrygdDto.Arsak.FLYTTET_FRA_NORGE)) {
                title1 {
                    text(
                        bokmal { +"Honnørkort" },
                        nynorsk { +"Honnørkort" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du har ikke lenger rett på uføretrygd, og må derfor levere tilbake honnørkortet til Nav-kontoret ditt." },
                        nynorsk { +"Du har ikkje lenger rett på uføretrygd, og må derfor levere tilbake honnørkortet til Nav-kontoret ditt." },
                    )
                }
            }
            showIf(opphorTilbakeITid) {
                title1 {
                    text(
                        bokmal { +"Du kan ha fått utbetalt for mye i uføretrygd" },
                        nynorsk { +"Du kan ha fått utbetalt for mykje i uføretrygd" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er opphørt tilbake i tid. Det betyr at du har fått for mye utbetalt i uføretrygd. Vi vil vurdere om beløpet skal betales tilbake." },
                        nynorsk { +"Uføretrygda di er opphøyrd tilbake i tid. Det betyr at du har fått for mykje utbetalt i uføretrygd. Vi vil vurdere om beløpet skal betalast tilbake." },
                    )
                }
            }
            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}

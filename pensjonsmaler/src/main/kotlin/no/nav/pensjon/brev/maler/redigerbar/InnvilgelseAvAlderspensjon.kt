package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.gjenlevendetilleggKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.afpPrivatResultatFellesKontoret_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdodNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.avdodNavn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.egenOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevenderett
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.harGjenlevendetilleggKap19
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.kravVirkDatoFomSenereEnnOensketUttakstidspunkt
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

// Tekster og logikk mht ektefelletillegg og barnetillegg er fjernet fra brevmalen etter en samtale med Ingrid Strand

@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.format()
        val regelverkType = pesysData.regelverkType
        val sivilstand = pesysData.sivilstand
        val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
        val sivilstandBestemtLitenBokstav = pesysData.sivilstand.bestemtForm(storBokstav = false)
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.uforeKombinertMedAlder
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
        val gjenlevendetilleggInnvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val gjenlevendetilleggKap19Innvilget = pesysData.alderspensjonVedVirk.gjenlevendetilleggInnvilget
        val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.format()
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val gjenlevendetilleggKap19 =
            pesysData.beregnetPensjonPerManedVedVirk.gjenlevendetilleggKap19_safe.ifNull(then = Kroner(0))
        val gjenlevenderettAnvendt = pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt
        val avdodNavn = pesysData.avdodNavn_safe.ifNull(then = String)
        val privatAFPErBrukt = pesysData.alderspensjonVedVirk.privatAFPErBrukt
        val afpPrivatResultatFellesKontoret = pesysData.afpPrivatResultatFellesKontoret_safe.ifNull(then = false)





        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + uttaksgrad + " percent retirement pension",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {
            showIf(
                not(uforeKombinertMedAlder) and not(innvilgetFor67)
                        and not(gjenlevendetilleggInnvilget) and not(gjenlevendetilleggKap19Innvilget)
            ) {
                // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + " i alderspensjon fra folketrygden.",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + " i alderspensjon frå folketrygda",
                        English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + " as retirement pension from the National Insurance Scheme."
                    )
                }
            }.orShowIf(
                uforeKombinertMedAlder and innvilgetFor67 and not(gjenlevendetilleggInnvilget) and not(
                    gjenlevendetilleggKap19Innvilget
                )
            ) {
                // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + "." +
                                " Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + "." +
                                " Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + "." +
                                " You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                    )
                }
            }.orShowIf(
                gjenlevendetilleggKap19Innvilget
            ) {
                //  beloepApOgGjtvedVirkMedDato_001, beloepApOgGjvedVirkMedDato_002
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalPensjon + " kroner i alderspensjon og gjenlevendetillegg fra folketrygden hver måned før skatt fra ".expr() +
                                kravVirkDatoFom + ".",
                        Nynorsk to "Du får ".expr() + totalPensjon + " kroner i alderspensjon og attlevandetillegg frå folketrygda kvar månad før skatt frå ".expr() +
                                kravVirkDatoFom + ".",
                        English to "You will receive NOK ".expr() + totalPensjon + " in retirement pension and survivor’s supplement from the National Insurance Scheme every month before tax from ".expr() +
                                kravVirkDatoFom + "."
                    )
                    showIf(gjenlevendetilleggInnvilget) {
                        textExpr(
                            Bokmal to " Av dette er gjenlevendetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            Nynorsk to " Av dette er attlevandetillegget ".expr() + gjenlevendetilleggKap19.format() + " kroner.",
                            English to " Of this, the survivor’s supplement is NOK ".expr() + gjenlevendetilleggKap19.format() + "."
                        )
                    }
                    showIf(uforeKombinertMedAlder) {
                        text(
                            Bokmal to " Dette er i tillegg til uføretrygden din.",
                            Nynorsk to " Dette er i tillegg til uføretrygda di.",
                            English to " This is in addition to your disability benefit.",
                        )
                    }
                }
            }
            showIf(saksbehandlerValg.kravVirkDatoFomSenereEnnOensketUttakstidspunkt) {
                // invilgelseAPVirkfom
                paragraph {
                    text(
                        Bokmal to "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. Dette går frem av folketrygdloven § 22-13.",
                        Nynorsk to "Vi kan tidlegast innvilge søknaden din månaden etter at du søkte. Dette går fram av folketrygdlova § 22-13.",
                        English to "We can at the earliest grant your application the month after you applied. This is in accordance with the regulations of § 22-13 of the National Insurance Act.",
                    )
                }
            }

            includePhrase(Felles.FlereBeregningsperioder)

            showIf(
                saksbehandlerValg.harGjenlevenderett
                        and gjenlevenderettAnvendt and avdodNavn.notNull() and not(gjenlevendetilleggKap19Innvilget) and not(
                    gjenlevendetilleggInnvilget
                )
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "I beregningen vår har vi tatt utgangspunkt i pensjonsrettigheter du har etter ".expr() + avdodNavn + ". Dette gir deg en høyere pensjon enn om vi bare hadde tatt utgangspunkt i din egen opptjening.",
                        Nynorsk to "I beregninga vår har vi teke utgangspunkt i pensjonsrettar du har etter ".expr() + avdodNavn + ". Dette gir deg ein høgare pensjon enn om vi berre hadde teke utgangspunkt i di eiga opptening.",
                        English to "We have based the calculation on the pension rights you have after ".expr() + avdodNavn + ". This gives you a higher pension than if we had only based the calculation on your own earnings."
                    )
                }
            }
            // TODO is this correct?
            showIf(saksbehandlerValg.harGjenlevendetillegg and gjenlevenderettAnvendt and gjenlevendetilleggKap19.isNull()) {
                // beregningAPGjtOpptj
                paragraph {
                    text(
                        Bokmal to "Fra januar 2024 er gjenlevenderett i alderspensjonen din skilt ut som et eget gjenlevendetillegg." +
                                " Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Frå januar 2024 er attlevanderett i alderspensjonen din skild ut som eit eige attlevandetillegg." +
                                " Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er differansen mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "From January 2024, the survivor’s right in your retirement pension is separated out as a separate survivor’s supplement." +
                                " The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år.",
                        Nynorsk to "Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år.",
                        English to "The survivor's supplement will not be adjusted when the pension increases from 1 May every year.",
                    )
                }
            }
            showIf(saksbehandlerValg.harGjenlevendetilleggKap19 and gjenlevendetilleggKap19Innvilget and avdodNavn) {
                // beregningAPGjtKap19
                paragraph {
                    textExpr(
                        Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr() + avdodNavn + ".",
                        Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr() + avdodNavn + ".",
                        English to "You receive a survivor’s supplement in retirement pension because you have pension rights after ".expr() + avdodNavn + "."
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, og alderspensjon du har tjent opp selv.",
                        Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, og alderspensjon du har tent opp sjølv.",
                        English to "The retirement pension is based on your own pension earnings. The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, and retirement pension you have earned yourself."
                    )
                }
            }
            showIf(saksbehandlerValg.egenOpptjening and not(gjenlevenderettAnvendt and avdodNavn.isNull())) {
                // beregningAPGjRettOpptjEgen_002
                title1 {
                    text(
                        Bokmal to "Gjenlevenderett i alderspensjon",
                        Nynorsk to "Attlevenderett i alderspensjon",
                        English to "Survivor's rights in retirement pension",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter ".expr() + avdodNavn + ".",
                        Nynorsk to "I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter ".expr() + avdodNavn + ".",
                        English to "We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after ".expr() + avdodNavn + ".",
                    )
                }
            }

            showIf(privatAFPErBrukt) {
                // innvilgelseAPogAFPPrivat
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + uttaksgrad + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefestede pensjonen din (AFP) gjør at du har rett til alderspensjon før du fyller 67 år.",
                        Nynorsk to "Du får ".expr() + uttaksgrad + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefesta pensjonen din (AFP) gjer at du har rett til alderspensjon før 67 år.",
                        English to "You have been granted ".expr() + uttaksgrad + " percent retirement pension because your total retirement pension and contractual early retirement pension (AFP) makes you eligible for retirement pension before the age of 67."
                    )
                }
            }
            showIf(afpPrivatResultatFellesKontoret) {
                // soktAFPPrivatInfo
                paragraph {
                    text(
                        Bokmal to "Du har også søkt om avtalefestet pensjon (AFP), og du vil få et eget vedtak om dette.",
                        Nynorsk to "Du har også søkt om avtalefesta pensjon (AFP), og du vil få eit eige vedtak om dette.",
                        English to "You have also applied for contractual early retirement pension (AFP) and will receive a separate decision on this.",
                    )

                }
            }
        }
    }
}






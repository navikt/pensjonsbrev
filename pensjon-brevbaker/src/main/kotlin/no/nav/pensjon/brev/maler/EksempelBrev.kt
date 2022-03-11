package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.template.Element.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        //ID på brevet
        name = "EKSEMPEL_BREV",

        //Master-mal for brevet.
        base = PensjonLatex,

        // Unik datagrunnlag/DTO for brevet
        letterDataType = EksempelBrevDto::class,

        // Hovedtittel inne i brevet
        title = newText(Bokmal to "Eksempelbrev"),

        // Metadata knyttet til en brevmal som ikke påvirker innholdet
        letterMetadata = LetterMetadata(

            // Visningstittel for ulike systemer.
            // F.eks saksbehandling brev oversikt, brukerens brev oversikt osv
            displayTitle = "Dette er ett eksempel-brev",

            // Krever brevet bankid/ nivå 4 pålogging for å vises(og ikke brukernavn/passord nivå 3)
            isSensitiv = false,
        )
    ) {
        // Tekst-innholdet i malen
        outline {

            // Undertittel
            title1 {
                // Tekst
                text(Bokmal to "Du har fått innvilget pensjon")
            }

            paragraph {
                text(
                    Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt."
                )
            }

            // Avsnitt
            paragraph {
                text(Bokmal to "Test dette er en test")
                list {
                    showIf(true.expr()) {
                        item {
                            text(Bokmal to "Test1")
                        }
                    }
                    showIf(false.expr()) {
                        item {
                            text(Bokmal to "Test2")
                        }
                    }
                    showIf(true.expr()) {
                        item {
                            text(Bokmal to "Test3")
                        }
                    }
                    item{
                        textExpr(Bokmal to "Hello".expr() + "world".expr())
                    }
                }
            }

            title1{
                text(Bokmal to "Utbetalingsoversikt")
            }
            paragraph {
                text(Bokmal to "Dette er din inntekt fra 01.01.2020 til 01.05.2020")
                table {
                    columnSpec {
                        column(3) {text(Bokmal to "Kolonne 1", FontType.BOLD)}
                        column(1, RIGHT) {text(Bokmal to "Kolonne 2", FontType.BOLD)}
                        column(1, RIGHT) {text(Bokmal to "Kolonne 3", FontType.BOLD)}
                        column(1, RIGHT) {text(Bokmal to "Kolonne 4", FontType.BOLD)}
                    }
                    for (i in 1..50) {
                        row {
                            cell {
                                text(Bokmal to "Dette er ditt beløp av noe test test test test test test test test")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                        }
                    }
                }
            }
            paragraph {text(Bokmal to "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque porttitor condimentum lorem, at commodo libero iaculis ut. Pellentesque vulputate gravida bibendum. Etiam non ante augue. Etiam vehicula at massa eget consectetur. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed ornare nulla quis purus tempor, eget hendrerit lorem feugiat. Nulla tincidunt eu ipsum et rutrum. Donec non leo laoreet, molestie lectus non, dictum ipsum. Maecenas in dui tincidunt, fermentum felis ut, volutpat dui. Phasellus eu fermentum dui. Suspendisse feugiat mauris eros, sed dapibus enim eleifend a.")}
            paragraph {text(Bokmal to "Etiam porta turpis et eros ullamcorper sodales. Cras et eleifend leo. Aenean vehicula nunc sit amet quam tincidunt, id aliquam arcu cursus. Morbi non imperdiet augue, nec placerat tellus. Aenean imperdiet auctor porta. Morbi in lacus nec purus commodo sodales non in ligula. Praesent euismod mollis elit, mollis finibus massa pretium eget. Fusce mollis tempus nisl vitae suscipit. Morbi in elementum tortor. Aenean varius odio non sem convallis, at venenatis arcu ullamcorper. Duis porttitor nulla facilisis mattis porttitor. Quisque pharetra hendrerit tellus, id consequat sapien maximus sit amet. Vestibulum vehicula pellentesque nulla, sit amet egestas felis pellentesque ac. Ut viverra vel magna eget mollis. Aliquam dictum aliquet tortor vitae efficitur. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.")}
            paragraph {text(Bokmal to "Fusce sed diam ac dui luctus venenatis sit amet sit amet sapien. Praesent non congue metus. Morbi rutrum pellentesque rhoncus. Duis semper dictum rutrum. Curabitur iaculis, magna sit amet varius dignissim, sapien augue pellentesque mi, id malesuada arcu risus et justo. Vivamus fermentum neque ac purus faucibus, non viverra massa pulvinar. Suspendisse ornare erat hendrerit, condimentum lorem vel, fringilla dui. Donec non tortor dignissim, ornare metus nec, malesuada nulla. Nulla convallis arcu ultricies augue consectetur, eu mattis neque tristique. Sed suscipit lacus vel risus lobortis, sed dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. Donec elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim rhoncus consequat. Vivamus laoreet semper facilisis.")}
            paragraph {text(Bokmal to "Donec consequat nibh vitae faucibus blandit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut vel fermentum urna. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla dictum justo in egestas venenatis. Sed vel eros quis turpis blandit accumsan. Nulla sed velit euismod, aliquet nibh eu, finibus mauris.")}
            paragraph {text(Bokmal to "Cras efficitur tincidunt eleifend. Vestibulum auctor diam in tortor tincidunt dapibus. Nulla id nunc luctus, sodales tellus sed, pulvinar turpis. Etiam vel vulputate ex, nec efficitur nunc. Morbi vel maximus quam. Pellentesque id iaculis ipsum. Sed facilisis dui et arcu aliquam rhoncus. Nullam id ex dictum, porttitor elit sed, hendrerit risus. Duis convallis sed magna sit amet porttitor. Phasellus neque ligula, cursus id tristique et, facilisis in magna. Sed bibendum tempus neque.")}
            paragraph {text(Bokmal to "Suspendisse faucibus lorem ante, vel gravida enim dignissim sit amet. Pellentesque a tempor ligula. Nunc eu nisl massa. Nullam ut semper magna. Aliquam condimentum massa dui. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac ex pretium, pretium mi ut, congue nulla. Integer eget neque id orci tristique hendrerit quis quis quam. Praesent quis nunc nunc. Cras luctus maximus mi quis dapibus. Nullam ultricies bibendum velit quis pulvinar. Cras a turpis elit. Mauris placerat rhoncus metus ac varius. Etiam rhoncus mi sit amet sollicitudin posuere. Vivamus scelerisque, ex vitae imperdiet luctus, metus tortor fringilla lacus, non facilisis dui tortor vel ante. Cras efficitur lacus felis, in imperdiet enim feugiat a.")}
            paragraph {text(Bokmal to "In suscipit, velit convallis feugiat semper, dolor urna vehicula arcu, ornare hendrerit quam ex non dui. Donec consectetur, urna et faucibus viverra, mi est consectetur enim, ac luctus lectus eros nec mauris. Fusce quam sapien, elementum ut neque a, tempus ultrices nunc. Integer id aliquet lacus. Nam rhoncus ligula et diam gravida, eget auctor urna vehicula. Phasellus vel dignissim metus, sed consectetur risus. Proin in diam in sapien condimentum tristique. Duis ultrices est eu nisl scelerisque porta. Sed vulputate quis felis id semper. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam eleifend, ante in porta aliquam, velit sem faucibus libero, quis rhoncus elit libero nec diam. Suspendisse vel faucibus orci.")}
            paragraph {text(Bokmal to "Etiam luctus, velit eu semper cursus, orci justo porttitor leo, sed tincidunt purus nunc commodo quam. Integer sed varius sapien. Vestibulum felis dolor, tristique eget mauris id, semper fermentum tortor. Ut imperdiet auctor sodales. Proin ut tincidunt nulla, at lobortis nulla. Vestibulum quis justo vitae mi tempor ultricies a sit amet dolor. Praesent vitae metus viverra, consectetur purus ut, consequat mi. Integer vitae tempus eros. Curabitur orci nisi, varius eget orci quis, pulvinar rhoncus quam. Fusce non dui id augue semper vulputate. Morbi aliquet blandit ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur tempus massa dui, quis malesuada enim suscipit sed.")}
            paragraph {text(Bokmal to "Aenean vel mauris massa. Aliquam vel tortor in mi posuere suscipit et vitae mauris. Aenean tortor nulla, tempus quis suscipit vel, ornare non orci. Phasellus non semper tellus. Fusce finibus neque eu accumsan efficitur. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Cras dictum euismod volutpat. Praesent quis rutrum massa. Nullam iaculis, erat vel sodales fringilla, leo tellus ullamcorper risus, a bibendum nulla nisl in dui. Duis ac nisi felis.")}
            paragraph {text(Bokmal to "Ut congue ac orci a porttitor. Nullam ut ex erat. Nullam at venenatis augue. Morbi luctus fringilla ex, ut ultrices enim auctor ac. Maecenas eget blandit justo, quis euismod nunc. Cras venenatis nunc non euismod hendrerit. Suspendisse facilisis mi dui, in ultricies tellus dictum ut. Nulla id elit eu arcu viverra iaculis non at est. Phasellus leo nunc, posuere vitae est vel, sagittis lobortis tellus. Vivamus viverra eu tortor at placerat. Sed vehicula congue magna vitae ultrices. Nulla facilisi. Vivamus bibendum metus vitae lacus consequat, eget ullamcorper sem blandit.")}
            paragraph {text(Bokmal to "Fusce tincidunt porta orci, ac pretium erat mattis vel. Suspendisse quis turpis sit amet enim lacinia interdum at et dui. Nulla fringilla bibendum magna sed interdum. Donec congue orci non malesuada dignissim. Mauris pulvinar nibh et varius fermentum. Proin efficitur bibendum viverra. Nam sollicitudin euismod tempor. Sed dapibus ac dolor ut dignissim. Quisque sagittis, sem eget suscipit venenatis, odio elit finibus eros, vel faucibus nibh libero vitae nisi.")}
            paragraph {text(Bokmal to "Nulla in bibendum tellus. Praesent sit amet luctus ligula. Nulla facilisi. Ut laoreet arcu dignissim arcu dignissim, efficitur porta risus lobortis. Suspendisse elit nunc, tempus id maximus nec, faucibus non ipsum. Cras semper neque quis est facilisis, sed egestas neque accumsan. Nulla facilisi.")}
            paragraph {text(Bokmal to "Aliquam erat volutpat. Integer ac euismod augue. Phasellus venenatis lorem in ipsum vestibulum volutpat. Proin eu sapien id lectus egestas placerat vel quis dui. Fusce dapibus risus urna, eu feugiat orci auctor id. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor nisl non mi finibus, ut malesuada enim congue. Nullam auctor enim sollicitudin orci viverra tempor. Etiam placerat volutpat nisl vitae vehicula. Donec quis rutrum dui. Nunc congue, sapien quis pharetra gravida, nisl tortor vehicula arcu, a mattis leo turpis ac massa. Duis sagittis augue in arcu sollicitudin, sit amet dapibus turpis dignissim. Sed id efficitur risus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut cursus eget augue vitae faucibus.")}
            paragraph {text(Bokmal to "Nullam rhoncus faucibus nisi, at tristique ante fermentum et. Donec hendrerit, sem eu euismod convallis, nisi risus dapibus quam, id egestas metus justo ac tortor. Nulla sagittis mauris eleifend odio efficitur, gravida rutrum turpis aliquet. Sed a turpis sed ex molestie viverra. Quisque maximus tincidunt dui, ut faucibus quam sollicitudin ut. Mauris nec rhoncus ipsum. Fusce in enim ac risus tincidunt interdum et vitae mauris. Aliquam non sodales elit, sit amet pharetra lectus. Nulla facilisi. Quisque vitae nisl eu nulla eleifend sagittis. Praesent rutrum elit nec est viverra, a feugiat nibh dignissim. Donec sed scelerisque leo. Donec sed pulvinar eros, nec mollis nunc. Curabitur ac erat et justo porttitor auctor. Donec sed condimentum dui, at sodales magna. Vivamus quis lacinia risus.")}
            paragraph {text(Bokmal to "Sed sed nunc vitae nunc convallis placerat quis non quam. Sed vulputate augue nulla, in finibus tortor consectetur in. Vestibulum mollis sagittis feugiat. Donec a interdum diam, nec pretium justo. Fusce congue lacus ac euismod rutrum. Vivamus nulla nisl, ornare vel tempor eget, porta eget dui. Praesent vulputate dui vitae venenatis porttitor. Sed aliquam ligula libero, id rhoncus mauris pharetra vel. Donec et pulvinar ante. Curabitur id malesuada leo. Vestibulum vel augue vehicula, rutrum magna id, consectetur erat. Maecenas condimentum felis varius, eleifend ex ac, interdum augue. Nam quis ex sed urna ornare suscipit sed quis ante. Curabitur vitae est pharetra, condimentum purus et, mollis ante. Morbi id feugiat dui, ullamcorper fermentum ligula.")}
            paragraph {text(Bokmal to "Etiam dictum molestie nunc eget vehicula. Suspendisse et interdum est. Ut sed luctus massa. Phasellus aliquet, felis vel tempus aliquet, velit est vestibulum orci, nec hendrerit nunc odio vitae magna. In mi est, lobortis nec suscipit in, auctor vel nisi. Duis ante neque, dictum non consequat non, ornare sed mauris. Quisque eu lorem convallis, sodales magna eget, dictum ante. Aliquam ac pharetra tortor. Mauris dictum gravida vestibulum. Duis lacinia lacinia sem a ultrices. Phasellus posuere fermentum ex, vel iaculis diam lobortis eget. Integer et efficitur ipsum, id hendrerit nulla. Phasellus dapibus, lacus et euismod rutrum, mi ante convallis massa, sit amet fermentum ante ante sed orci. Aliquam sapien dolor, imperdiet blandit odio nec, laoreet euismod nisl.")}
            paragraph {text(Bokmal to "Pellentesque at est tempor, maximus velit vitae, ullamcorper felis. Quisque non sodales turpis. Vestibulum tincidunt et orci vel auctor. Pellentesque eu consequat est. Nulla condimentum venenatis ex, non vehicula sem pulvinar et. Quisque aliquet semper eros. Donec dictum nibh in libero molestie ultrices. Sed quis libero neque. Integer vehicula quam at sollicitudin dignissim. Pellentesque maximus arcu dolor, id gravida elit hendrerit quis.")}
            paragraph {text(Bokmal to "Donec sed rutrum metus. Sed at est a orci iaculis tristique. Ut non bibendum nisl. Integer ante eros, suscipit sit amet nisl ut, ultricies faucibus ante. Vivamus hendrerit felis arcu, et convallis velit mattis porta. Curabitur varius, leo et lacinia pellentesque, eros massa tempor felis, at gravida leo augue et nulla. Donec efficitur quam ac elit sagittis maximus. Duis in rhoncus nunc, eu aliquet risus. Curabitur posuere mattis mi, sed placerat justo pellentesque vel. Suspendisse sed nulla sapien. Praesent feugiat neque in diam pharetra mattis. Nullam laoreet finibus arcu sit amet eleifend. In mollis dictum ultricies. Donec id justo a sapien tempor scelerisque. Ut varius, sapien vitae semper commodo, velit leo mollis orci, id efficitur velit augue vel neque. In rutrum laoreet semper.")}
            paragraph {text(Bokmal to "Nulla dignissim non ante sit amet mattis. Sed condimentum tincidunt ligula sed sodales. Quisque volutpat et nisl at molestie. Curabitur consectetur, massa efficitur tempus euismod, enim velit lacinia nulla, eu vehicula purus justo et mauris. Curabitur in dui tempor, porttitor lorem vitae, tincidunt ligula. Maecenas ultrices quam nec mattis hendrerit. Aenean pretium aliquet ipsum, sed iaculis felis tristique dapibus.")}
            paragraph {text(Bokmal to "Ut aliquam magna non elit molestie feugiat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut blandit, augue in tristique accumsan, arcu felis ornare elit, et luctus nulla odio in libero. Vestibulum commodo eleifend sapien. Vivamus tempor ac nulla non pulvinar. Nulla pulvinar at odio ultrices semper. Donec tempor mauris eu tortor rhoncus lobortis. Fusce egestas velit congue massa eleifend blandit. Maecenas egestas fringilla urna a dapibus. Nulla id leo a diam rhoncus tempus.")}
        }
    }
}
import {FC} from "react"
import styles from "./LetterPreview.module.css"
import {StarFillIcon, StarIcon} from "@navikt/aksel-icons"
import {Button} from "@navikt/ds-react"
import Image from "next/image"
import {Metadata} from "../../../../lib/model/skribenten"


interface LetterPreviewProps {
    selectedLetter: Metadata | undefined
    onAddToFavourites: () => void
    selectedIsFavourite: boolean
}

const LetterPreview: FC<LetterPreviewProps> = ({selectedLetter, selectedIsFavourite, onAddToFavourites}) => {
    return (
        //TODO Should we get an example of all different kinds of old letters? Where do we get that?
        <div className={styles.previewContainer}>
            <div className={`${!selectedLetter ? styles.topMenuDisabled : ""} ${styles.topMenu}`}>
                <div className={styles.letterTitle}>
                    {selectedLetter?.name || ""}
                </div>
                <Button disabled={!selectedLetter || selectedLetter.isEblankett} variant="secondary" className={styles.addToFavourites} onClick={onAddToFavourites}>
                    {selectedIsFavourite?
                        (<StarFillIcon className={styles.starIcon} fontSize={"1.5rem"}/>)
                        :(<StarIcon className={styles.starIcon} fontSize={"1.5rem"}/>)
                        }
                    { (selectedIsFavourite?(<>Fjern som favoritt</>):(<>Legg til som favoritt</>))}
                </Button>
            </div>
            <Image
                className={styles.previewImage}
                src={'/pdfpreview/4/output_page1.svg'}
                alt="letter example"
                width={807}
                height={1048}/>
        </div>
    )
}

export default LetterPreview
import {FC, useState} from "react"
import styles from "./LetterPreview.module.css"
import ImageWithZoom from "./ImageWithZoom/ImageWithZoom"
import {StarFillIcon, StarIcon, ZoomMinusIcon, ZoomPlusIcon} from "@navikt/aksel-icons"
import {Button} from "@navikt/ds-react"


interface LetterPreviewProps {
    selectedLetter: string | null
    onAddToFavourites: () => void
    selectedIsFavourite: boolean
}

const LetterPreview: FC<LetterPreviewProps> = ({selectedLetter, selectedIsFavourite, onAddToFavourites}) => {
    const [scale, setScale] = useState(1)

    const zoomInHandler = () => {
        setScale((prevScale) => prevScale * 1.2)
    }

    const zoomOutHandler = () => {
        setScale((prevScale) => {
            const newScale = prevScale / 1.2
            return newScale < 1 ? 1 : newScale
        })
    }

    // TODO
    const reset = () => {
        setScale(1)
    }

    return (
        //TODO Should we get an example of all different kinds of old letters? Where do we get that?
        <div className={styles.previewContainer}>
            <div className={styles.topMenu}>

                <ZoomMinusIcon onClick={zoomOutHandler} className={styles.zoomButton}/>
                <ZoomPlusIcon onClick={zoomInHandler} className={styles.zoomButton}/>
                <Button variant="secondary" className={styles.addToFavourites} onClick={onAddToFavourites}>
                    {selectedIsFavourite?
                        (<StarFillIcon className={styles.starIcon} fontSize={"1.5rem"}/>)
                        :(<StarIcon className={styles.starIcon} fontSize={"1.5rem"}/>)
                        }
                    { (selectedIsFavourite?(<>Fjern som favoritt</>):(<>Legg til som favoritt</>))}
                </Button>


            </div>
            <ImageWithZoom
                src={'/pdfpreview/4/output_page1.svg'}
                alt="letter example"
                scale={scale}/>
        </div>
    )
}

export default LetterPreview
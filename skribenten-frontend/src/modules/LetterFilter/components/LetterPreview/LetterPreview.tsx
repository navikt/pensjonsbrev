import {FC, useState} from "react"
import styles from "./LetterPreview.module.css"
import ImageWithZoom from "./ImageWithZoom/ImageWithZoom"
import {ZoomMinusIcon, ZoomPlusIcon} from "@navikt/aksel-icons";


interface LetterPreviewProps {
    selectedLetter: string | null
}

const LetterPreview:FC<LetterPreviewProps> = ({selectedLetter}) => {
    const [scale, setScale] = useState(1)

    const zoomIn = () => {
        setScale((prevScale) => prevScale * 1.2)
    }

    const zoomOut = () => {
        setScale((prevScale) => {
            const newScale = prevScale / 1.2
            return newScale < 1? 1: newScale
        })
    }

    const reset = () => {
        setScale(1)
    }

    return (
        //TODO Should we get an example of all different kinds of old letters? Where do we get that?
        <div className={styles.previewContainer}>
            <div className={styles.topMenu}>
                <ZoomMinusIcon width="1.5rem" onClick={zoomOut} className={styles.zoomButton}/>
                <ZoomPlusIcon width="1.5rem" onClick={zoomIn} className={styles.zoomButton}/>

            </div>
            <ImageWithZoom
                src={'/pdfpreview/4/output_page1.svg'}
                alt="letter example"
                scale={scale}/>
        </div>
    )
}

export default LetterPreview
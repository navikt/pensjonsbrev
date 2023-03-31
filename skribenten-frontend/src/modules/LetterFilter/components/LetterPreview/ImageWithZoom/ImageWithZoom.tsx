import React, {useRef} from 'react'
import styles from './ImageWithZoom.module.css'
import Image from "next/image"

interface ImageWithZoomProps {
    src: string;
    alt: string;
    scale: number;
}

const viewBaseWidth = 770
const viewBaseHeight = 1100

const ImageWithZoom: React.FC<ImageWithZoomProps> = ({src, alt, scale}) => {
    const viewerRef: React.RefObject<HTMLDivElement> = useRef<HTMLDivElement>(null)

    const mouseDownHandler = (e: { pageX: number; pageY: number; }): void => {
        if (viewerRef.current !== null) {
            const startX: number = e.pageX
            const startY: number = e.pageY
            const initialScrollLeft: number = viewerRef.current.scrollLeft
            const initialScrollTop: number = viewerRef.current.scrollTop

            const handleMouseMove = (moveEvent: MouseEvent) => {
                const deltaX = moveEvent.pageX - startX
                const deltaY = moveEvent.pageY - startY
                if (viewerRef.current !== null) {
                    viewerRef.current.scrollLeft = initialScrollLeft - deltaX
                    viewerRef.current.scrollTop = initialScrollTop - deltaY
                }
            }

            const handleMouseUp = () => {
                window.removeEventListener('mousemove', handleMouseMove)
                window.removeEventListener('mouseup', handleMouseUp)
            }

            window.addEventListener('mousemove', handleMouseMove)
            window.addEventListener('mouseup', handleMouseUp)
        }
    }
    return (
        <div className={styles.viewer} ref={viewerRef}>
            <Image
                className={styles.image}
                src={src}
                alt={alt}
                style={{transform: `scale(${scale})`}}
                draggable="false"
                width={viewBaseWidth}
                height={viewBaseHeight}
                onDragStart={(e) => e.preventDefault()}
                onMouseDown={mouseDownHandler}
            />
        </div>
    )
}

export default ImageWithZoom


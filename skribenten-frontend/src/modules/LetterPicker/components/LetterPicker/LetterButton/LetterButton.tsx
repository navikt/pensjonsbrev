import React, {FC, useEffect, useLayoutEffect, useRef, useState} from 'react'
import styles from "./LetterButton.module.css"
import {BodyLong, BodyShort, Popover} from "@navikt/ds-react"
import popoverContent from "@navikt/ds-react/src/popover/PopoverContent"

interface LetterButtonProps {
    text: string,
    id: string,
    isSelected: boolean,
    onClicked: (id: string | null) => void,
}

const LetterButton: FC<LetterButtonProps> = ({text, id, isSelected = false, onClicked}) => {
    const [showPopover, setShowPopover] = useState(false)
    const ref = useRef<HTMLButtonElement>(null)

    function checkIfOverflowing(): boolean{
        if (ref.current) {
            return ref.current.clientWidth < ref.current.scrollWidth
        } else return false
    }

    const startHoverHandler = () => {
        setShowPopover(checkIfOverflowing())
    }

    const stopHoverHandler = () => {
        setShowPopover(false)
    }

    return <li className={styles.buttonElement}>
        <button className={`${isSelected ? styles.letterButtonOpen : ""} ${styles.letterButton}`}
                onMouseOver={startHoverHandler}
                onMouseLeave={stopHoverHandler}
                onClick={() => onClicked(isSelected ? null : id)}>
            <span ref={ref}>{text}</span>
            {showPopover && (<Popover
                open
                onClose={() => null}
                anchorEl={ref.current}
                placement="right">
                <Popover.Content className={styles.popover}>
                    <span className={styles.popoverBody}>{text}</span>
                </Popover.Content>
            </Popover>)}
        </button>
    </li>
}

export default LetterButton
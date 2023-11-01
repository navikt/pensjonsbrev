import React, {FC, useRef, useState} from 'react'
import styles from "./LetterButton.module.css"
import {Popover} from "@navikt/ds-react"
import {Brevsystem} from "../../../../../lib/model/skribenten"

interface LetterButtonProps {
    text: string,
    id: string,
    isSelected: boolean,
    onClick: (id: string) => void,
    brevsystem: Brevsystem,
}

const LetterButton: FC<LetterButtonProps> = ({text, id, isSelected = false, onClick, brevsystem}) => {
    const [showPopover, setShowPopover] = useState(false)
    const ref = useRef<HTMLButtonElement>(null)
    function checkIfOverflowing(): boolean {
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
                onClick={() => onClick(id)}>
            {brevsystem && (<div className={styles.tag}>{systemLetter(brevsystem)}</div>)}
            <span ref={ref} className={styles.buttonText}>
                {text}
            </span>
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

const systemLetter = (system: Brevsystem): JSX.Element => {
    switch (system) {
        case "EXTERAM": return <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="7.5" fill="#97E6FF" stroke="#134852"/>
            <path d="M5 13L5.0001 3H10.9971L11 5H6.99189L6.98902 7H9.99581V9H6.98902L7 11H11.0081L10.997 13H5Z" fill="#134852"/>
        </svg>
        case "DOKSYS": return <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="7.5" fill="#E0D8E9" stroke="#412B5D"/>
            <path d="M6.00002 11.9981L6.00004 4.00193C7.95313 4.00193 11 3.78336 11 7.99995C11 12.2165 7.56249 11.9981 6.00002 11.9981Z" stroke="#412B5D" strokeWidth="1.71543"/>
        </svg>
        case "BREVBAKER": return <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle cx="8" cy="8" r="7.5" fill="#D9E366" stroke="#474E00"/>
            <path d="M5 13V3.00022L7.93813 3C8.61511 3 9.2224 3.07669 9.76 3.23006C10.2976 3.3732 10.7257 3.62371 11.0443 3.98157C11.3628 4.33944 11.5221 4.84045 11.5221 5.48461C11.5221 5.92427 11.4076 6.35882 11.1787 6.78826C10.9596 7.20747 10.651 7.49377 10.2528 7.64714V7.70848C10.7506 7.84141 11.1637 8.10214 11.4923 8.49068C11.8308 8.86899 12 9.45947 12 10.1241C12 10.8091 11.8308 11.2974 11.4923 11.7268C11.1538 12.1562 10.7008 12.4783 10.1333 12.693C9.56587 12.8975 8.92871 12.9998 8.22187 12.9998L5 13ZM7 6.75455H7.96693C8.50453 6.75455 8.8928 6.64719 9.13173 6.43247C9.38062 6.21775 9.50507 5.93146 9.50507 5.5736C9.50507 5.18506 9.38062 4.90899 9.13173 4.74539C8.88285 4.5818 8.49956 4.5 7.98187 4.5H7V6.75455ZM7 11.0029H8.176C9.38062 11.0029 9.98293 10.553 9.98293 9.65326C9.98293 9.2136 9.8336 8.90174 9.53493 8.7177C9.23627 8.52343 8.78329 8.42629 8.176 8.42629H7V11.0029Z" fill="#474E00"/>
        </svg>
    }
}

export default LetterButton
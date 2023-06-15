import React, {FC} from "react"
import styles from "./BottomMenu.module.css"

interface BottomMenuProps {
    children?: React.ReactNode
}

const BottomMenu: FC<BottomMenuProps> = ({children}) => {
    return (
        <div className={styles.bottomMenu}>
            {children}
        </div>
    )
}

export default BottomMenu
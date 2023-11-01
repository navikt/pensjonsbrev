import {FC, useState} from "react"
import {Button, Modal, Tabs} from "@navikt/ds-react"
import styles from "./ChangeRecipient.module.css"
import {XMarkIcon} from "@navikt/aksel-icons"
import RecipientSearch from "./RecipientSearch/RecipientSearch"
import ConfirmChange from "./ConfirmChange/ConfirmChange"

interface ChangeRecipientProps {
    open: boolean,
    onExit: () => void,
    onChange: (recipientChange: RecipientChange | null) => void,
}

export type AddressContext = "recipientsearch" | "manualrecipient"
export type RecipientChange = {
    recipientName: string,
    addressContext: AddressContext,
    addressLines: string[]
}

const ChangeRecipient: FC<ChangeRecipientProps> = ({open, onExit, onChange}) => {
    const [recipientChange, setRecipientChange] = useState<RecipientChange | null>(null)
    const handleRecipientChanged = () => {
        if(recipientChange) {
            onChange(recipientChange)
            setRecipientChange(null)
        }
        onExit()
    }

    const handleExit = () =>{
        setRecipientChange(null)
        onExit()
    }
    return (
        <Modal open={open} onClose={onExit}>
            <Modal.Body className={styles.content}>
                <div className={styles.banner}>
                    <p className={styles.bannerText}>Endre mottaker</p>
                    <Button
                        onClick={handleExit}
                        className={styles.exitButton}
                        icon={<XMarkIcon/>} variant="tertiary"/>
                </div>
                {recipientChange && recipientChange.addressLines ? (
                    <ConfirmChange recipientChange={recipientChange}
                                   //TODO on cancel
                                   onConfirm={handleRecipientChanged}/>
                ): (<Tabs defaultValue="recipientsearch">
                    <Tabs.List>
                        <Tabs.Tab
                            value="recipientsearch"
                            label="Søk mottaker"
                        />
                        <Tabs.Tab
                            value="manualrecipient"
                            label="Legg til mottaker manuelt"
                        />
                    </Tabs.List>
                    <Tabs.Panel value="recipientsearch" className={styles.tabContent}>
                        <RecipientSearch onMottakerChosen={setRecipientChange}/>
                    </Tabs.Panel>
                </Tabs>)}

            </Modal.Body>
        </Modal>
    )
}

export default ChangeRecipient
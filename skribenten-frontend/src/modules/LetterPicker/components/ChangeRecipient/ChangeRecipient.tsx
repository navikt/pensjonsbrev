import {FC} from "react"
import {Button, Modal, Tabs} from "@navikt/ds-react"
import styles from "./ChangeRecipient.module.css"
import {XMarkIcon} from "@navikt/aksel-icons"
import RecipientSearch from "./RecipientSearch/RecipientSearch"

interface ChangeRecipientProps {
    open: boolean,
    onExit: () => void
}

const ChangeRecipient: FC<ChangeRecipientProps> = ({open, onExit}) => {
    const handleMottakerChanged = (pid: string) =>{ //TODO use address instead

    }
    return (
        <Modal open={open} onClose={onExit}>
            <Modal.Body className={styles.content}>
                <div className={styles.banner}>
                    <p className={styles.bannerText}>Mottaker</p>
                    <Button
                        onClick={() => onExit()}
                        className={styles.exitButton}
                        icon={<XMarkIcon/>} variant="tertiary"/>
                </div>
                <Tabs defaultValue="recipientsearch">
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
                        <RecipientSearch onMottakerChosen={handleMottakerChanged}/>
                    </Tabs.Panel>
                </Tabs>
            </Modal.Body>
        </Modal>
    )
}

export default ChangeRecipient
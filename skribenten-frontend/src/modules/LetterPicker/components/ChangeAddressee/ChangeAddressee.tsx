import {FC} from "react"
import {Button, Modal, Tabs} from "@navikt/ds-react"
import styles from "./ChangeAddressee.module.css"
import {XMarkIcon} from "@navikt/aksel-icons"
import AddresseeSearch from "./AddresseeSearch/AddresseeSearch"

interface ChangeAddresseeProps {
    open: boolean,
    onExit: () => void
}

const ChangeAddressee: FC<ChangeAddresseeProps> = ({open, onExit}) => {
    const handleMottakerChanged = (pid: string) =>{ //TODO use address instead

    }
    return (
        <Modal open={open} onClose={onExit} closeButton={false}>
            <Modal.Content className={styles.content}>
                <div className={styles.banner}>
                    <p className={styles.bannerText}>Mottaker</p>
                    <Button
                        onClick={() => onExit()}
                        className={styles.exitButton}
                        icon={<XMarkIcon/>} variant="tertiary"/>
                </div>
                <Tabs>
                    <Tabs.List>
                        <Tabs.Tab
                            value="favourites"
                            label="Favoritter"
                        />
                        <Tabs.Tab
                            value="addresseesearch"
                            label="Søk mottaker"
                        />
                        <Tabs.Tab
                            value="manualaddressee"
                            label="Legg til mottaker manuelt"
                        />
                    </Tabs.List>
                    <Tabs.Panel value="addresseesearch" className={styles.tabContent}>
                        <AddresseeSearch onMottakerChosen={handleMottakerChanged}/>
                    </Tabs.Panel>
                </Tabs>
            </Modal.Content>
        </Modal>
    )
}

export default ChangeAddressee
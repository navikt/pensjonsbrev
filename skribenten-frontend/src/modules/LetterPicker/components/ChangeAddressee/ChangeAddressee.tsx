import {FC, useState} from "react"
import {Button, Modal, Tabs} from "@navikt/ds-react"
import styles from "./ChangeAddressee.module.css"
import {XMarkIcon} from "@navikt/aksel-icons"
import AddresseeSearch from "./AddresseeSearch/AddresseeSearch"

interface ChangeAddresseeProps {
    open: boolean,
    onExit: ()=>void
}

const ChangeAddressee : FC<ChangeAddresseeProps> = ({open, onExit}) => {
    return (
        <Modal open={open} onClose={onExit} closeButton={false}>
            <Modal.Content className={styles.content}>
                <div className={styles.banner}>
                    <p className={styles.bannerText}>Mottaker</p>
                    <Button
                        onClick={()=>onExit()}
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
                    <AddresseeSearch/>
                </Tabs>

                <div className={styles.actionBar}>
                    <Button size="small" variant="secondary">Avbryt</Button>
                    <Button size="small">Ferdig</Button>
                </div>
            </Modal.Content>
        </Modal>
    )
}

export default ChangeAddressee
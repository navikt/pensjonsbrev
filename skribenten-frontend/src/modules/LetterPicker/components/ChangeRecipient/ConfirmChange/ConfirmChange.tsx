import React, {FC} from "react"
import {Button} from "@navikt/ds-react"
import {RecipientChange} from "../ChangeRecipient"

interface ConfirmChangeProps {
    recipientChange: RecipientChange,
    onConfirm: () => void,
}

const ConfirmChange: FC<ConfirmChangeProps> = ({recipientChange, onConfirm}) => {
    return (
        <div>
            <div>
                <p>{recipientChange.recipientName}</p>
                <p>{recipientChange.addressLines.map((line,index) => (<p key={index}>{line}</p>))}</p>
            </div>
            <Button onClick={onConfirm}>Bekreft ny mottaker</Button>
        </div>
    )
}

export default ConfirmChange
import {FC} from "react"
import {HeaderBlock} from "../../model"
import Text from "../text/Text"

export interface HeaderProps {
    block: HeaderBlock
}

const Header: FC<HeaderProps> = ({block}) => (
    <div className={"le-block-header"}>
        <h1 className={"le-block-content"}>
            {block.content.map((c, id) =>
                <Text key={id} content={c}/>)
            }
        </h1>
    </div>
)

export default Header
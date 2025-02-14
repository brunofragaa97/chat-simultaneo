import "../CSS/desktop/modalCadastro.css"


const ModalCadastro = ({ isOpen, onClose, children }) => {
    if(!isOpen) return null;


    return (
        <div
         className="modal-overlay-cadastro"
         onClick={onClose}
         
         >
        <div className="container-modal-cadastro" onClick={(e) => e.stopPropagation()}>
            <div>
            {children}
            </div>
        </div>
        



        </div>

    )


}
export default ModalCadastro;
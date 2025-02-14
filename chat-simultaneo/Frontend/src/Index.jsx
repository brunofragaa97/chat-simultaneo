import '../CSS/desktop/index.css'
import '../CSS/mobile/index.css'
import Login from './login';
import {  useState } from "react";
import ModalCadastro from './ModalCadastro';
import CadastroCliente from './CadastroCliente';

function Index() {

    const [activeModalContent, setActiveModalContent] = useState(null);

// Função para abrir o modal
const openModal = (index) => {
    setActiveModalContent(index);
  };

  // Função para fechar o modal
  const closeModal = () => {
    setActiveModalContent(null);
  };


return <>
    <div className="background-container">
        <div className="container-principal">
        <Login />
        <a onClick={() => openModal(1)}>Ou CADASTRE-SE</a>
        <ModalCadastro isOpen={activeModalContent === 1} onClose={closeModal}>
            <CadastroCliente closeModal={closeModal} />
          </ModalCadastro>
        </div>
       
    </div>
</>

}

export default Index; 
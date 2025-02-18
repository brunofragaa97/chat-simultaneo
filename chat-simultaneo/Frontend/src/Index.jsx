import '../CSS/desktop/index.css'
import '../CSS/mobile/index.css'
import Login from './login';
import { useState } from "react";
import ModalCadastro from './ModalCadastro';
import CadastroCliente from './CadastroCliente';

function Index() {
  const [isLoged, setIsLoged] = useState(localStorage.getItem("estadoDeLogin"));
  const [activeModalContent, setActiveModalContent] = useState(null);

  const onLogout = () => {
    setIsLoged(false);
    localStorage.setItem("estadoDeLogin", false)
  }

  const onLogin = () => {
    setIsLoged(true);
    localStorage.setItem("estadoDeLogin", true);
    if(isLoged)
    console.log ("Esta logado agora")
  }

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
      {isLoged && <div className='btn-logout'><button onClick={onLogout}>SAIR</button></div>}
      <div className="container-principal">
        <Login onLogin={onLogin} />
        <a onClick={() => openModal(1)}>Ou CADASTRE-SE</a>
        <ModalCadastro isOpen={activeModalContent === 1} onClose={closeModal}>
          <CadastroCliente closeModal={closeModal} />
        </ModalCadastro>
      </div>

    </div>
  </>

}

export default Index; 
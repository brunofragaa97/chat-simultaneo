import React, { useEffect, useState } from "react";
import "../CSS/desktop/chat.css";

const UserListItem = ({ user }) => (
  <li className="user-item">
    <div className="user-info">
      <img src={`${user.image}`} alt={user.name} className="user-image" />
      <div className="user-details">
        <p className="user-name">{user.name}</p>
        <p className="user-status">{user.status}</p>
      </div>
    </div>
  </li>
);

const Chat = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [userDetails, setUserDetails] = useState(null); // Estado para armazenar as informações do usuário
  const [loading, setLoading] = useState(true); // Estado para controlar o carregamento
  const [error, setError] = useState(null); // Estado para controlar erros



  // Obtendo o token JWT do localStorage
  const token = localStorage.getItem("userToken");
  console.log("##O Token é este: ", token);

  useEffect(() => {
    if (!token) {
      setError("Token não encontrado. Por favor, faça login novamente.");
      setLoading(false);
      return;
    }

    // Definindo a URL do endpoint
    const apiUrl = 'http://192.168.0.197:9000/api/userDetails';

    // Fazendo a requisição GET
    fetch(apiUrl, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Erro ao carregar os dados');
        }
        return response.json();
      })
      .then(data => {
        setUserDetails(data); // Armazenando os dados no estado
        setLoading(false); // Definindo o carregamento como concluído
      })
      .catch(err => {
        setError(err.message); // Armazenando o erro, se houver
        setLoading(false); // Definindo o carregamento como concluído
      });
  }, [token]);

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (error) {
    return <div>Erro: {error}</div>;
  }

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
  console.log(userDetails.nome)
  console.log(userDetails.nome)
  console.log(userDetails.imgPerfil)
  return (
    <div className="chat-container">
      <div className={`sidebar ${isSidebarOpen ? 'open' : ''}`} id="sidebar">
        <h3>Usuários</h3>
        <ul>
          {/* Exibindo o usuário logado */}
          {userDetails && (
            <UserListItem 
              user={{
                id: userDetails.id,
                name: userDetails.nome, // Nome do usuário retornado da API
                status: userDetails.status, // Status do usuário
                image: userDetails.imgPerfil // Imagem do usuário (pode ser alterada conforme necessário)
              }}
            />
          )}
        </ul>
      </div>

      {/* Exibindo as informações do usuário */}
      <div className="chat-content">
        <div className="messages-box">
          <p>Mensagem 1</p>
          <p>Mensagem 2</p>
        </div>
        <div className="input-container">
          <input type="text" className="message-input" placeholder="Digite sua mensagem..." />
          <button className="send-button">Enviar</button>
        </div>
      </div>

      <button onClick={toggleSidebar} className="toggle-sidebar" id="toggleSidebar">
        {isSidebarOpen ? 'Fechar Sidebar' : 'Abrir Sidebar'}
      </button>
    </div>
  );
};

export default Chat;

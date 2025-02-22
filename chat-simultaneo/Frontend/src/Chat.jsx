import React, { useEffect, useState } from "react";
import "../CSS/desktop/chat.css";

const UserListItem = ({ user }) => (
  <li className="user-item">
    <div className="user-info">
      <img src={user.image} alt={user.name} className="user-image" />
      <div className="user-details">
        <p className="user-name">{user.name}</p>
        <p className="user-status">{user.status}</p>
      </div>
    </div>
  </li>
);

const Chat = ({ onLogout }) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [userDetails, setUserDetails] = useState(null); // Dados do usuário logado
  const [onlineUsers, setOnlineUsers] = useState({}); // Objeto vazio como estado inicial
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [socket, setSocket] = useState(null);

  const token = localStorage.getItem("userToken");
  console.log("##O Token é este: ", token);

  // useEffect para buscar os detalhes do usuário logado
  useEffect(() => {
    if (!token) {
      alert("Token não encontrado. Por favor, faça login novamente.");
      setLoading(false);
      return;
    }

    const apiUrl = "http://192.168.0.197:9000/api/userDetails";
    fetch(apiUrl, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Erro ao carregar os dados");
        }
        return response.json();
      })
      .then((data) => {
        setUserDetails(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [token]);

  // useEffect para configurar o WebSocket
  useEffect(() => {
    if (!token) return;

    const ws = new WebSocket(`ws://192.168.0.197:9000/chat?token=${token}`);

    ws.onopen = () => {
      console.log("WebSocket estabelecido");
    };

    ws.onmessage = (event) => {
      try {
        const userList = JSON.parse(event.data);
        // Garante que userList é um objeto válido
        if (userList && typeof userList === "object" && !Array.isArray(userList)) {
          setOnlineUsers(userList);
        } else {
          console.error("Dados inválidos recebidos do WebSocket:", userList);
          setOnlineUsers({});
        }
      } catch (e) {
        console.error("Erro ao parsear mensagem do WebSocket:", e);
        setOnlineUsers({});
      }
    };

    ws.onclose = () => {
      console.log("Conexão WebSocket encerrada");
      setOnlineUsers({});
    };

    ws.onerror = (error) => {
      console.error("Erro no WebSocket:", error);
    };

    setSocket(ws);

    return () => {
      ws.close(); // Fecha a conexão corretamente
    };
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

  const handleLogout = () => {
    if (socket) {
      socket.close();
    }
    localStorage.removeItem("userToken");
    onLogout();
  };

  return (
    <div className="chat-container">
      <div className={`sidebar ${isSidebarOpen ? "open" : ""}`} id="sidebar">
        <h3>Usuários</h3>
        <ul>
          {/* Usuário logado */}
          {userDetails && (
            <UserListItem
              user={{
                id: userDetails.id,
                name: userDetails.nome,
                status: userDetails.status || "online",
                image: userDetails.imgPerfil || "default_photo_url",
              }}
            />
          )}
          {/* Usuários online via WebSocket */}
          {onlineUsers && Object.keys(onlineUsers).length > 0 ? (
            Object.entries(onlineUsers).map(([email, user]) =>
              email !== userDetails?.email ? (
                <UserListItem
                  key={email}
                  user={{
                    id: email,
                    name: user.name,
                    status: user.status,
                    image: user.photo || "default_photo_url",
                  }}
                />
              ) : null
            )
          ) : (
            <li>Nenhum usuário online</li>
          )}
        </ul>
      </div>

      <div className="chat-content">
        <div className="messages-box">
          <p>Mensagem 1</p>
          <p>Mensagem 2</p>
        </div>
        <div className="input-container">
          <input
            type="text"
            className="message-input"
            placeholder="Digite sua mensagem..."
          />
          <button className="send-button">Enviar</button>
        </div>
      </div>

      <button
        onClick={toggleSidebar}
        className="toggle-sidebar"
        id="toggleSidebar"
      >
        {isSidebarOpen ? "Fechar Sidebar" : "Abrir Sidebar"}
      </button>
     
    </div>
  );
};

export default Chat;
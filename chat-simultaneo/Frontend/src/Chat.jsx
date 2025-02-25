import React, { useEffect, useState, useRef } from "react";
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
  const [userDetails, setUserDetails] = useState(null);
  const [onlineUsers, setOnlineUsers] = useState({});
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [socket, setSocket] = useState(null);
  const messagesContainerRef = useRef(null);

  const token = localStorage.getItem("userToken");

  const scrollToBottom = () => {
    const container = messagesContainerRef.current;
    if (container) {
      container.scrollTop = container.scrollHeight;
      console.log("Scrolled to bottom, scrollHeight:", container.scrollHeight);
    } else {
      console.log("messagesContainerRef não está definido ainda");
    }
  };

  useEffect(() => {
    if (!token) {
      alert("Token não encontrado. Por favor, faça login novamente.");
      setLoading(false);
      return;
    }

    fetch("http://192.168.0.197:9000/api/userDetails", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Erro ao carregar os dados do usuário");
        return response.json();
      })
      .then((data) => {
        console.log("User details loaded:", data);
        setUserDetails(data);
      })
      .catch((err) => setError(err.message));

    fetch("http://192.168.0.197:9000/api/chat", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Erro ao carregar mensagens");
        return response.json();
      })
      .then((data) => {
        console.log("Mensagens iniciais carregadas:", data);
        setMessages(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [token]);

  useEffect(() => {
    if (!token) return;

    const ws = new WebSocket(`ws://192.168.0.197:9000/chat?token=${token}`);
    setSocket(ws);

    ws.onopen = () => {
      console.log("WebSocket estabelecido");
    };

    ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log("Mensagem recebida do WebSocket:", data);
        if (data.remetente && data.mensagem && data.time) {
          setMessages((prev) => [...prev, data]);
        } else if (data.error) {
          console.log("Erro recebido do servidor:", data.error);
          setMessages((prev) => [
            ...prev,
            { remetente: "Sistema", mensagem: data.error, time: new Date().toISOString() }
          ]);
        } else if (typeof data === "object" && !Array.isArray(data)) {
          setOnlineUsers(data);
        }
      } catch (e) {
        console.error("Erro ao parsear mensagem do WebSocket:", e, "Dados recebidos:", event.data);
      }
    };

    ws.onclose = () => {
      console.log("Conexão WebSocket encerrada");
      setOnlineUsers({});
      setSocket(null);
    };

    ws.onerror = (error) => {
      console.error("Erro no WebSocket:", error);
    };

    return () => {
      console.log("Fechando WebSocket no cleanup");
      ws.close();
    };
  }, [token]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const sendMessage = () => {
    if (inputMessage.trim() && socket && socket.readyState === WebSocket.OPEN) {
      if (inputMessage.length > 8190) {
        alert("A mensagem excede o limite de 8190 caracteres.");
        return;
      }
      console.log("Enviando mensagem:", inputMessage);
      socket.send(inputMessage);
      setInputMessage("");
    } else {
      console.log("Não foi possível enviar mensagem. Socket estado:", socket?.readyState);
    }
  };

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const handleLogout = () => {
    if (socket) socket.close();
    localStorage.removeItem("userToken");
    onLogout();
  };

  if (loading) return <div>Carregando...</div>;
  if (error) return <div>Erro: {error}</div>;

  const getSenderName = (remetente) => {
    return typeof remetente === "object" && remetente.nome ? remetente.nome : remetente;
  };

  return (
    <div className="chat-container">
      <div className={`sidebar ${isSidebarOpen ? "open" : ""}`} id="sidebar">
        <h3>Usuários</h3>
        <ul>
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
          {Object.keys(onlineUsers).length > 0 ? (
            Object.entries(onlineUsers)
              .filter(([email]) => email !== userDetails?.email)
              .map(([email, user]) => (
                <UserListItem
                  key={email}
                  user={{
                    id: email,
                    name: user.name,
                    status: user.status,
                    image: user.photo || "default_photo_url",
                  }}
                />
              ))
          ) : (
            <li>Nenhum usuário online</li>
          )}
        </ul>
      </div>

      <div className="chat-content">
        <div className="messages-box" ref={messagesContainerRef}>
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`message ${
                getSenderName(msg.remetente) === userDetails?.nome ? "sent" : "received"
              }`}
            >
              <div className="sender">{getSenderName(msg.remetente)}</div>
              <div className="content">{msg.mensagem}</div>
              <div className="time">{msg.time}</div>
            </div>
          ))}
        </div>
        <div className="input-container">
          <input
            type="text"
            className="message-input"
            placeholder="Digite sua mensagem (máx. 8190 caracteres)..."
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            onKeyPress={(e) => e.key === "Enter" && sendMessage()}
            maxLength={8190} // Limite de 8190 caracteres no input
          />
          <button className="send-button" onClick={sendMessage}>
            Enviar
          </button>
        </div>
      </div>

      <button onClick={toggleSidebar} className="toggle-sidebar" id="toggleSidebar">
        {isSidebarOpen ? "Fechar Sidebar" : "Abrir Sidebar"}
      </button>
    </div>
  );
};

export default Chat;
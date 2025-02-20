import React, { useEffect, useState } from "react";
import "../CSS/desktop/chat.css";




const USER_DATA = [
  { id: 1, name: 'Usuário 1', status: 'Online', image: 'user1.jpg' },
 
];
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


const Chat = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  useEffect(() => {
    console.log(localStorage.getItem("userToken"))
  }, []);



  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className="chat-container">
      <div className={`sidebar ${isSidebarOpen ? 'open' : ''}`} id="sidebar">
        <h3>Usuários</h3>
        <ul>
          {USER_DATA.map((user, index) => (
            <React.Fragment key={user.id}>
              <UserListItem user={user} />
              {index < USER_DATA.length - 1 && <hr className="user-separator" />}
            </React.Fragment>
          ))}
        </ul>
      </div>
      <div className="chat-content">
        <div className="messages-box">
          {/* Placeholder for messages */}
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
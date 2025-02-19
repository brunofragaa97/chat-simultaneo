import "../CSS/desktop/chat.css"

const  Chat = () => {
        return (
            <div className="chat-container">
              {/* Sidebar de usuários */}
              <div className="sidebar">Usuários Conectados</div>
              
              {/* Área principal do chat */}
              <div className="chat-content">
                {/* Caixa de mensagens */}
                <div className="messages-box">Mensagens</div>
                
                {/* Campo de entrada e botão */}
                <div className="input-container">
                  <input type="text" className="message-input" placeholder="Digite sua mensagem..." />
                  <button className="send-button">Enviar</button>
                </div>
              </div>
            </div>
          );
        }
    
  export default Chat;
  
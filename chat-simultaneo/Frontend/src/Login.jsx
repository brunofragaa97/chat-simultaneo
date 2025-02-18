import '../CSS/desktop/login.css'
import '../CSS/mobile/login.css'
import React, { useState } from "react";

    const Login = () => {
        const [formData, setFormData] = useState({
          email: "",
          senha: "",
        });
      
        const [errors, setErrors] = useState({}); // Inicializa como objeto vazio
        const [inputError, setInputError] = useState({});
      
        const handleChange = (e) => {
          const { name, value } = e.target;
          setFormData((prevData) => ({ ...prevData, [name]: value }));
          setInputError((prevErrors) => ({ ...prevErrors, [name]: "" })); // Limpa erro ao digitar
        };
      
        const validateForm = () => {
          const newErrors = {};
          Object.keys(formData).forEach((key) => {
            if (!formData[key]) {
              newErrors[key] = "Este campo é obrigatório.";
            }
          });
          setInputError(newErrors);
          return Object.keys(newErrors).length === 0;
        };
      
        const efetuarLogin = async (e) => {
            e.preventDefault();
            if(validateForm()){
              try{
                const response = await fetch("http://192.168.0.197:9000/auth/authUser",{
                  method: 'POST',
                  headers: {"Content-Type": "application/json"},
                  body: JSON.stringify({
                    email: formData.email,
                    senha: formData.senha
                  }),
                });
                const servidor = await response.json();

                if (response.ok) {
                  console.log(servidor.message , "LOGADO")
                  alert(servidor.message)
                  setErrors({ login: servidor.message })
                }else{
                  alert(servidor.message)
                  setErrors ({ login: servidor.message });
                }
              }
              catch (error){
                alert("Que pena, parece que o servidor está indisponivel")
                setErrors({ login: "Servidor indisponivel =("});
              }
            }
        }
        
      
        return (
          <div className="form-container-login">
            <h2>ENTRE PARA ACESSAR O CHAT</h2>
            <form onSubmit={efetuarLogin} className="client-form-login">
              {[
                { label: "E-mail", name: "email", type: "email" },
                { label: "Senha", name: "senha", type: "password" },
              ].map((field) => (
                <div key={field.name} className="form-group">
                  <label htmlFor={field.name}>{field.label}</label>
                  <input
                    type={field.type}
                    id={field.name}
                    name={field.name}
                    value={formData[field.name]}
                    onChange={handleChange}
                    className={inputError[field.name] ? "input-error" : ""}
                  />
                  {inputError[field.name] && <small>{inputError[field.name]}</small>}
                </div>
              ))}
      
              {/* Exibe a mensagem de erro do login abaixo do formulário */}
              {errors.login && <div className="error-message">{errors.login}</div>}
      
              <button type="submit" className="submit-button-login">
                ENTRAR
              </button>
            </form>
          </div>
        );
      };

export default Login;
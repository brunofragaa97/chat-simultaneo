import React, { useState } from "react";
import "../CSS/desktop/CadastroCliente.css"
import "../CSS/mobile/CadastroCliente.css"


const CadastroCliente = ({ closeModal }) => {
  

  const [isLoged, setIsLoged] = useState(false);
  const [formData, setFormData] = useState({
    nome: "",
    senha: "",
    email: "",
  });

  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};
    Object.keys(formData).forEach((key) => {
      if (!formData[key]) {
        newErrors[key] = "Este campo é obrigatório.";
      }
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const enviarCadastro = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      try {
        const response = await fetch(
          "http:///192.168.0.197:9000/usuarios/cadastrarUsuario",
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              nome: formData.nome,
              senha: formData.senha,
              email: formData.email,
            }),
          }
        );
        const servidor = await response.json();
        if (response.ok) {
          console.log(servidor.message , "Cliente cadastrado: ", servidor.cliente);
          
          alert("Cadastro realizado com sucesso!");
          closeModal();
          setFormData({
            nome: "",
            senha: "",
            email: "",
          });
          setErrors({ });
        } else {
          console.log("Erro ao cadastrar cliente" , );
          setErrors({ login: servidor.mensagem});
        }
      } catch (error) {
        console.error("Erro ao conectar no servidor:", error);
        setErrors({ login: "Servidor indisponivel baby"});
      }
    } else {
      alert("Por favor, preencha todos os campos.");
      
    }
  };

  return (
    <div className="form-container">
      <h1>Cadastro de Usuario</h1>
      <form onSubmit={enviarCadastro} className="client-form">
        {[
          { label: "Nome", name: "nome", type: "text" },
          { label: "Senha", name: "senha", type: "password" },
          { label: "E-mail", name: "email", type: "email" },
        
        ].map((field) => (
          <div key={field.name} className="form-group">
            <label htmlFor={field.name}>{field.label}</label>
            <input
              type={field.type}
              id={field.name}
              name={field.name}
              value={formData[field.name]}
              onChange={handleChange}
              className={errors[field.name] ? "input-error" : ""}
            />
            {errors[field.name] && <small>{errors[field.name]}</small>}
          </div>
        ))}
        <button type="submit" className="submit-button">
          Enviar Cadastro
        </button>
        
      </form>
      {errors.login && <div className="error-message">{errors.login}</div>}
    </div>
  );
};

export default CadastroCliente;

const UsersInfo = ({ user }) => (
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

export default UsersInfo;
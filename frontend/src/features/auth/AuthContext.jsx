import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

//simulacija ulogovanog korisnika, zameni sa pravim auth
const loggedUserMock = {
  id: 1,
  username: "john.doe",
  avatar: "https://i.pravatar.cc/150?img=1",
};

export function AuthProvider({ children }) {
  const [user] = useState(loggedUserMock);
  return (
    <AuthContext.Provider value={{ user }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
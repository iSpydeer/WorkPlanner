import { createContext, useContext, useState } from "react";
import executeJwtAuthentication from "../api/AuthenticationService";
import { apiClient } from "../api/ApiClients";
import { jwtDecode } from "jwt-decode";

// Create AuthContext and useAuth hook
export const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

/**
 * AuthProvider component manages authentication state and provides authentication methods.
 *
 * @component
 * @param {Object} props - The component props.
 * @returns {JSX.Element} - The rendered AuthProvider component.
 */
export default function AuthProvider({ children }) {
	const [isAuthenticated, setAuthenticated] = useState(false);
	const [username, setUsername] = useState(null);
	const [userId, setUserId] = useState(null);
	const [role, setRole] = useState(null);
	const [token, setToken] = useState(null);
	const [interceptorId, setInterceptorId] = useState(null);

	/**
	 * Authenticates a user with provided username and password.
	 *
	 * @param {string} username - The username of the user.
	 * @param {string} password - The password of the user.
	 * @returns {Promise<boolean>} - Returns true if authentication is successful, otherwise false.
	 */
	async function login(username, password) {
		try {
			const response = await executeJwtAuthentication(username, password);

			if (response.status === 200) {
				const jwtToken = "Bearer " + response.data.token; // Create the JWT token.
				setAuthenticated(true);
				setUsername(username);
				setToken(jwtToken);

				const decodedToken = jwtDecode(jwtToken.split(" ")[1]);
				setUserId(decodedToken.id);
				setRole(decodedToken.scope);

				if (interceptorId !== null) {
					apiClient.interceptors.request.eject(interceptorId); // Eject the previous interceptor if it exists.
				}

				const id = apiClient.interceptors.request.use((config) => {
					config.headers.Authorization = jwtToken; // Set the interceptor to include the JWT token in API requests.
					return config;
				});

				setInterceptorId(id);

				return true;
			} else {
				logout();
				return false;
			}
		} catch (error) {
			logout();
			return false;
		}
	}

	/**
	 * Logs out the user and clears authentication state.
	 */
	function logout() {
		setAuthenticated(false);
		setUsername(null);
		setToken(null);
		setUserId(null);
		setRole(null);

		if (interceptorId !== null) {
			apiClient.interceptors.request.eject(interceptorId);
			setInterceptorId(null);
		}
	}

	return (
		<AuthContext.Provider
			value={{
				isAuthenticated,
				login,
				logout,
				username,
				userId,
				role,
				token,
			}}
		>
			{children}
		</AuthContext.Provider>
	);
}

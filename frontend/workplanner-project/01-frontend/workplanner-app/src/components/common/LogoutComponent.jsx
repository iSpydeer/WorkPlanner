import { useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import { ENDPOINTS } from "../../api/UrlMappings";
import { useEffect } from "react";

/**
 * Represents a logout component
 * It calls the logout function from the authentication context
 * and redirects the user to the login page.
 *
 * @component
 * @returns {JSX.Element} The rendered login component.
 */
export default function LogoutComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();

	useEffect(() => {
		authContext.logout();
		navigate(ENDPOINTS.LOGIN); // Navigate to the login page
	});
}

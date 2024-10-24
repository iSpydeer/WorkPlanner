import { apiClient } from "./ApiClients";

/**
 * Executes JWT authentication by sending a POST request to the /authenticate endpoint.
 *
 * @param {string} username - The username of the user.
 * @param {string} password - The password of the user.
 * @returns {Promise} A promise that resolves to the response from the authentication API.
 */
const executeJwtAuthentication = (username, password) =>
	apiClient.post("/authenticate", { username, password });

export default executeJwtAuthentication;

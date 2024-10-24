import { apiClient } from "./ApiClients";

/**
 * Creates a new team.
 *
 * @param {Object} teamDto - DTO containing team details.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const createTeam = (teamDto) => apiClient.post("/teams", teamDto);

/**
 * Retrieves all teams.
 *
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveAllTeams = () => apiClient.get("/teams");

/**
 * Retrieves a specific team by ID.
 *
 * @param {number} teamId - The ID of the team to retrieve.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveTeam = (teamId) => apiClient.get(`/teams/${teamId}`);

/**
 * Deletes a team by ID.
 *
 * @param {number} teamId - The ID of the team to delete.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const deleteTeam = (teamId) => apiClient.delete(`/teams/${teamId}`);

/**
 * Retrieves all users for a specific team.
 *
 * @param {number} teamId - The ID of the team.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveTeamUsers = (teamId) =>
	apiClient.get(`/teams/${teamId}/users`);

/**
 * Adds users to a team.
 *
 * @param {number} teamId - The ID of the team.
 * @param {Array<number>} userIds - The array of user IDs to add to the team.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const addTeamUsers = (teamId, userIds) =>
	apiClient.put(`/teams/${teamId}/users`, userIds);

/**
 * Removes a specific user from a team.
 *
 * @param {number} teamId - The ID of the team.
 * @param {number} userId - The ID of the user to remove.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const deleteTeamUser = (teamId, userId) =>
	apiClient.delete(`/teams/${teamId}/users/${userId}`);

/**
 * Retrieves plan entries for a specific user in a specific team.
 *
 * @param {number} teamId - The ID of the team.
 * @param {number} userId - The ID of the user.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrievePlanEntries = (teamId, userId) =>
	apiClient.get(`/plan-entries/teams/${teamId}/users/${userId}`);

/**
 * Retrieves all teams for a specific user.
 *
 * @param {number} userId - The ID of the user.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveUserTeams = (userId) =>
	apiClient.get(`/users/${userId}/teams`);

/**
 * Assigns a user as the leader of a team.
 *
 * @param {number} teamId - The ID of the team.
 * @param {number} userId - The ID of the user to assign as team leader.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const setTeamLeader = (teamId, userId) =>
	apiClient.put(`/teams/${teamId}/team-leader/${userId}`);

/**
 * Unassigns the leader of a team.
 *
 * @param {number} teamId - The ID of the team.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const resetTeamLeader = (teamId) =>
	apiClient.delete(`/teams/${teamId}/team-leader`);

/**
 * Creates a new user.
 *
 * @param {Object} userRegistrationDto - DTO containing user registration details.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const createUser = (userRegistrationDto) =>
	apiClient.post("/users", userRegistrationDto);

/**
 * Retrieves all users.
 *
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveAllUsers = () => apiClient.get("/users");

/**
 * Retrieves a specific user by ID.
 *
 * @param {number} userId - The ID of the user to retrieve.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const retrieveUser = (userId) => apiClient.get(`users/${userId}`);

/**
 * Removes a user from a specific team.
 *
 * @param {number} userId - The ID of the user.
 * @param {number} teamId - The ID of the team.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const deleteUserTeam = (userId, teamId) =>
	apiClient.delete(`/users/${userId}/teams/${teamId}`);

/**
 * Deletes a user by ID.
 *
 * @param {number} userId - The ID of the user to delete.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const deleteUser = (userId) => apiClient.delete(`/users/${userId}`);

/**
 * Creates a new plan entry for a user in a team.
 *
 * @param {number} teamId - The ID of the team.
 * @param {number} userId - The ID of the user.
 * @param {Object} planEntryDto - DTO containing plan entry details.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const createPlanEntry = (teamId, userId, planEntryDto) =>
	apiClient.post(
		`/plan-entries/teams/${teamId}/users/${userId}`,
		planEntryDto
	);

/**
 * Deletes a plan entry by ID.
 *
 * @param {number} planEntryId - The ID of the plan entry to delete.
 * @returns {Promise} A promise that resolves to the response from the API.
 */
export const deletePlanEntry = (planEntryId) =>
	apiClient.delete(`/plan-entries/${planEntryId}`);

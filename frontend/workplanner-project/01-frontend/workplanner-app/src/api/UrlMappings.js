// Application route constants used for navigation.
export const ROUTES = {
	LOGIN: "/",
	LOGOUT: "/logout",
	SIGNUP: "/sign-up",
	HOME: "/home",
	MY_TEAMS: "/my-teams",
	ALL_TEAMS: "/all-teams",
	TEAM: "/teams/:team_id",
	NEW_TEAM: "/teams/new",
	ADD_TEAM_USER: "/teams/:team_id/add-user",
	ASSIGN_LEADER: "/teams/:team_id/assign-leader",
	ALL_USERS: "/users",
	USER: "/users/:user_id",
	FORBIDDEN: "/forbidden",
	ACCOUNT: "/account",
};

// API endpoint constants used for making HTTP requests.
export const ENDPOINTS = {
	LOGIN: "/",
	LOGOUT: "/logout",
	SIGNUP: "/sign-up",
	HOME: "/home",
	MY_TEAMS: "/my-teams",
	ALL_TEAMS: "/all-teams",
	TEAM: (team_id) => `/teams/${team_id}`,
	NEW_TEAM: "/teams/new",
	ADD_TEAM_USER: (team_id) => `/teams/${team_id}/add-user`,
	ASSIGN_LEADER: (team_id) => `/teams/${team_id}/assign-leader`,
	ALL_USERS: "/users",
	USER: (user_id) => `/users/${user_id}`,
	FORBIDDEN: "/forbidden",
	ACCOUNT: "/account",
};

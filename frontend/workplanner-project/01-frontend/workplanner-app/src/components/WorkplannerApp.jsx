import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AuthProvider, { useAuth } from "../security/AuthContext";
import { ROUTES } from "../api/UrlMappings.js";

import HomeComponent from "./common/HomeComponent";
import HeaderComponent from "./common/HeaderComponent";
import SignupComponent from "./common/SignupComponent";
import LoginComponent from "./common/LoginComponent";
import LogoutComponent from "./common/LogoutComponent";
import ErrorComponent from "./common/ErrorComponent";
import OpForbiddenComponent from "./common/OpForbiddenComponent.jsx";
import ListMyTeamsComponent from "./team/ListMyTeamsComponent";
import ListAllTeamsComponent from "./team/ListAllTeamsComponent";
import TeamComponent from "./team/TeamComponent";
import NewTeamComponent from "./team/NewTeamComponent";
import AddTeamUsersComponent from "./team/AddTeamUsersComponent";
import AssignTeamLeaderComponent from "./team/AssignTeamLeaderComponent";
import AccountComponent from "./common/AccountComponent.jsx";
import ListAllUsersComponent from "./user/ListAllUsersComponent";
import UserComponent from "./user/UserComponent";

import "./WorkplannerApp.css";

/**
 * AuthenticatedRoute component checks user authentication and role.
 * Redirects to the login page if not authenticated, or to the forbidden page if the role does not match.
 *
 * @param {ReactNode} props.children - The child components to render if authentication and role checks pass.
 * @param {string} [props.role] - The required role to access the route.
 * @returns {JSX.Element} - The rendered children or a Navigate component for redirection.
 */
function AuthenticatedRoute({ children, role }) {
	const authContext = useAuth();

	if (!authContext.isAuthenticated) {
		return <Navigate to={ROUTES.LOGIN} />;
	}

	if (role && authContext.role !== role) {
		return <Navigate to={ROUTES.FORBIDDEN} />;
	}

	return children;
}

/**
 * WorkPlannerApp component serves as the main application layout,
 * providing routing and authentication context for the application.
 *
 * @returns {JSX.Element} - The rendered WorkPlannerApp component.
 */
export default function WorkPlannerApp() {
	return (
		<div className="WorkplannerApp">
			<AuthProvider>
				<BrowserRouter>
					<HeaderComponent />
					<Routes>
						<Route
							path={ROUTES.LOGIN}
							element={<LoginComponent />}
						/>
						<Route
							path={ROUTES.SIGNUP}
							element={<SignupComponent />}
						/>

						<Route
							path={ROUTES.HOME}
							element={
								<AuthenticatedRoute>
									<HomeComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.MY_TEAMS}
							element={
								<AuthenticatedRoute>
									<ListMyTeamsComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.ALL_TEAMS}
							element={
								<AuthenticatedRoute>
									<ListAllTeamsComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.TEAM}
							element={
								<AuthenticatedRoute>
									<TeamComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.NEW_TEAM}
							element={
								<AuthenticatedRoute role="ADMIN">
									<NewTeamComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.ADD_TEAM_USER}
							element={
								<AuthenticatedRoute>
									<AddTeamUsersComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.ASSIGN_LEADER}
							element={
								<AuthenticatedRoute role="ADMIN">
									<AssignTeamLeaderComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.ALL_USERS}
							element={
								<AuthenticatedRoute>
									<ListAllUsersComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.USER}
							element={
								<AuthenticatedRoute>
									<UserComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.ACCOUNT}
							element={
								<AuthenticatedRoute>
									<AccountComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.LOGOUT}
							element={
								<AuthenticatedRoute>
									<LogoutComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path={ROUTES.FORBIDDEN}
							element={
								<AuthenticatedRoute>
									<OpForbiddenComponent />
								</AuthenticatedRoute>
							}
						/>

						<Route
							path="*"
							element={
								<AuthenticatedRoute>
									<ErrorComponent />
								</AuthenticatedRoute>
							}
						/>
					</Routes>
				</BrowserRouter>
			</AuthProvider>
		</div>
	);
}

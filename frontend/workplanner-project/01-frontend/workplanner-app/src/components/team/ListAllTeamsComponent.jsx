import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import { retrieveAllTeams } from "../../api/UserService";
import { Container, Card, CardBody, Row, Col, Button } from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a list all teams component
 * It displays a list of all teams available in the system.
 * It allows users to view team details and, if the user is an admin, provides an option to create new teams.
 *
 * @component
 * @returns {JSX.Element} - The rendered list of teams.
 */
export default function ListAllTeamsComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();

	const isAdmin = authContext.role === "ADMIN";

	const [teams, setTeams] = useState([]);

	useEffect(() => refreshTeams(), []);

	/**
	 * Retrieves the list of all teams from the server and sets the state.
	 */
	function refreshTeams() {
		retrieveAllTeams()
			.then((response) => {
				setTeams(response.data);
			})
			.catch((error) => console.log(error));
	}

	function showTeam(teamId) {
		navigate(ENDPOINTS.TEAM(teamId));
	}

	return (
		<Container className="w-50 ListComponent">
			<div className="fw-bold fs-4">List of all teams</div>
			{teams.map((team) => (
				<Card key={team.id} className="list-item-card my-2">
					<CardBody>
						<Row>
							<Col>
								<div className="fw-bold">{team.name}</div>
							</Col>
							<Col>
								<div>{team.description}</div>
							</Col>
							<Col>
								<Button
									className="btn btn-confirm w-50"
									onClick={() => showTeam(team.id)}
								>
									View
								</Button>
							</Col>
						</Row>
					</CardBody>
				</Card>
			))}
			<Button
				onClick={() => navigate(ENDPOINTS.MY_TEAMS)}
				variant="link"
				style={{ color: "gray", textDecoration: "underline" }}
				className="fw-bold"
			>
				See your teams
			</Button>
			{isAdmin ? (
				<Button
					onClick={() => navigate(ENDPOINTS.NEW_TEAM)}
					variant="link"
					style={{ color: "gray", textDecoration: "underline" }}
					className="fw-bold"
				>
					Create new team
				</Button>
			) : (
				""
			)}
		</Container>
	);
}

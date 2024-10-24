import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import { retrieveUserTeams } from "../../api/UserService";
import { Container, Card, CardBody, Row, Col, Button } from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a list of user's teams component
 * It displays the list of teams that the currently logged-in user belongs to.
 * It allows the user to view team details and navigate to the specific team page.
 *
 * @component
 * @returns {JSX.Element} - The rendered list of user's teams.
 */
export default function ListMyTeamsComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();

	const [teams, setTeams] = useState([]);
	const userId = authContext.userId;

	useEffect(() => refreshTeams(), []);

	/**
	 * Retrieves the list of teams that the user is part of from the server and sets the state.
	 */
	function refreshTeams() {
		retrieveUserTeams(userId)
			.then((response) => {
				setTeams(response.data);
			})
			.catch((error) => console.log(error));
	}

	function showTeam(teamId) {
		navigate(ENDPOINTS.TEAM(teamId));
	}

	return (
		<Container className="w-50">
			<div className="fw-bold fs-4">List of your teams</div>
			{teams.map((team) => (
				<Card key={team.id} className="list-item-card my-2">
					<CardBody>
						<Row>
							<Col>
								<div className="fw-bold"> {team.name}</div>
							</Col>
							<Col>
								<div>{team.description}</div>
							</Col>
							<Col>
								<Button
									className="btn-confirm w-50"
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
				onClick={() => navigate(ENDPOINTS.ALL_TEAMS)}
				variant="link"
				style={{ color: "gray", textDecoration: "underline" }}
				className="fw-bold"
			>
				See all teams
			</Button>
		</Container>
	);
}

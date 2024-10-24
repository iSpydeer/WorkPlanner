import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
	retrieveTeam,
	retrieveTeamUsers,
	setTeamLeader,
} from "../../api/UserService";
import {
	Container,
	Form,
	ListGroup,
	Row,
	Col,
	Button,
	ToggleButton,
	Card,
	CardBody,
} from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a team leader assignment component
 * It allows the user to select a team leader from the existing team members.
 * It displays a list of team members, provides search functionality,
 * and allows selecting one user as the team leader.
 *
 * @component
 * @returns {JSX.Element} The rendered component for assigning a team leader.
 */
export default function AssignTeamLeaderComponent() {
	const navigate = useNavigate();
	const { team_id } = useParams();
	const teamId = team_id;

	const [team, setTeam] = useState(null);
	const [teamUsers, setTeamUsers] = useState([]);
	const [checkedUser, setCheckedUser] = useState(null);

	useEffect(() => {
		if (teamId) {
			refreshUsers();
			refreshTeam();
		}
	}, [teamId]);

	/**
	 * Fetches the list of users who are part of the team.
	 */
	function refreshUsers() {
		retrieveTeamUsers(teamId)
			.then((response) => {
				setTeamUsers(response.data);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	/**
	 * Fetches the details of the current team.
	 */
	function refreshTeam() {
		retrieveTeam(teamId)
			.then((response) => {
				setTeam(response.data);
			})
			.catch((error) => console.log(error));
	}

	const handleToggleCheck = (userId) => {
		setCheckedUser(userId === checkedUser ? null : userId);
	};

	function handleSubmit() {
		if (checkedUser) {
			setTeamLeader(teamId, checkedUser)
				.then((response) => {
					navigate(ENDPOINTS.TEAM(teamId));
				})
				.catch((error) => {
					console.error(error);
				});
		}
	}

	// Filters the team users based on the search term.
	const [searchTerm, setSearchTerm] = useState("");
	const handleSearch = (e) => {
		setSearchTerm(e.target.value);
	};

	return (
		<Container className="w-50">
			<Card className="data-card">
				<CardBody>
					{team ? (
						<div>
							<div className="fw-bold fs-4">
								Assign Leader for {team.name}
							</div>
							<div className="SearchList">
								<Form.Control
									type="text"
									placeholder="Search users..."
									value={searchTerm}
									onChange={handleSearch}
									className="mb-3"
								/>
								<div
									style={{
										height: "400px",
										overflowY: "auto",
									}}
								>
									<ListGroup>
										{teamUsers.length > 0 ? (
											teamUsers
												.filter((user) =>
													`${user.firstName} ${user.lastName}`
														.toLowerCase()
														.includes(
															searchTerm.toLowerCase()
														)
												)
												.map((user, index) => (
													<ListGroup.Item key={index}>
														<Row>
															<Col xs={10}>
																{user.firstName}{" "}
																{user.lastName}
															</Col>
															<Col xs={2}>
																<ToggleButton
																	id={`toggle-check-${user.id}`}
																	type="checkbox"
																	variant="outline-secondary"
																	checked={
																		checkedUser ===
																		user.id
																	}
																	value="1"
																	onChange={() =>
																		handleToggleCheck(
																			user.id
																		)
																	}
																>
																	✓
																</ToggleButton>
															</Col>
														</Row>
													</ListGroup.Item>
												))
										) : (
											<ListGroup.Item>
												No users found
											</ListGroup.Item>
										)}
									</ListGroup>
								</div>
							</div>
							<Button
								className="btn-confirm my-3 w-25"
								onClick={handleSubmit}
								disabled={!checkedUser}
							>
								Assign Leader
							</Button>
						</div>
					) : (
						<div>Loading...</div>
					)}
				</CardBody>
			</Card>
		</Container>
	);
}

import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
	retrieveAllUsers,
	retrieveTeam,
	retrieveTeamUsers,
	addTeamUsers,
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
 * Represents a add new users to team component
 * It allows the user to select and add users to a team.
 * It retrieves all users, compares them with the team's current members,
 * and lets the user select non-member users to add to the team.
 *
 * @component
 * @returns {JSX.Element} The rendered component for adding users to a team.
 */
export default function AddTeamUsersComponent() {
	const navigate = useNavigate();
	const { team_id } = useParams();
	const teamId = team_id;

	const [team, setTeam] = useState(null);
	const [users, setUsers] = useState([]);
	const [nonMemberUsers, setNonMemberUsers] = useState([]);
	const [checkedUsers, setCheckedUsers] = useState({});

	useEffect(() => {
		if (teamId) {
			refreshUsers();
			refreshTeam();
		}
	}, [teamId]);

	/**
	 * Refreshes and fetches all users and team members, then filters out team members from the all-users list.
	 */
	function refreshUsers() {
		Promise.all([retrieveAllUsers(), retrieveTeamUsers(teamId)])
			.then(([allUsersResponse, teamUsersResponse]) => {
				const allUsers = allUsersResponse.data;
				const teamUsers = teamUsersResponse.data;

				setUsers(allUsers);

				const unassignedUsers = allUsers.filter(
					(user) =>
						!teamUsers.some((teamUser) => teamUser.id === user.id)
				);
				setNonMemberUsers(unassignedUsers);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	/**
	 * Fetches and sets team details for the current team.
	 */
	function refreshTeam() {
		retrieveTeam(teamId)
			.then((response) => {
				setTeam(response.data);
			})
			.catch((error) => console.log(error));
	}

	const handleToggleCheck = (userId) => {
		setCheckedUsers((prevState) => ({
			...prevState,
			[userId]: !prevState[userId],
		}));
	};

	function handleSubmit() {
		const userIds = Object.keys(checkedUsers).filter(
			(userId) => checkedUsers[userId]
		);
		addTeamUsers(teamId, userIds)
			.then((response) => {
				navigate(ENDPOINTS.TEAM(teamId));
			})
			.catch((error) => {
				console.error(error);
			});
	}

	const [searchTerm, setSearchTerm] = useState("");
	const handleSearch = (e) => {
		setSearchTerm(e.target.value);
	};

	// Filters users based on the search term.
	const filteredUsers = users
		? nonMemberUsers.filter((user) =>
				`${user.firstName.toLowerCase()} ${user.lastName.toLowerCase()}`.includes(
					searchTerm.toLowerCase()
				)
		  )
		: [];

	return (
		<Container className="w-50 ListComponent">
			<Card className="data-card">
				<CardBody>
					{team ? (
						<div>
							<div className="fw-bold fs-4">
								Select new members for {team.name}
							</div>
							<div className="SearchList my-2">
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
										{filteredUsers.length > 0 ? (
											filteredUsers
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
																		checkedUsers[
																			user
																				.id
																		]
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
								className="btn-confirm mt-2 w-25"
								onClick={handleSubmit}
							>
								Add Members
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

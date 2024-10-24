import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import {
	retrieveUser,
	retrieveUserTeams,
	deleteTeamUser,
	deleteUser,
	retrievePlanEntries,
} from "../../api/UserService";
import { convertToGroups, convertToItems } from "../plan/TimelineUtilities";
import {
	Card,
	CardBody,
	CardHeader,
	Container,
	ListGroup,
	Form,
	Col,
	Row,
	Button,
} from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";
import PlanComponent from "../plan/PlanComponent";

/**
 * Represents a user component
 * It displays user information, including their teams and work plans.
 * It allows administrators to delete users and manage team memberships.
 *
 * @component
 * @returns {JSX.Element} The rendered user component with details and actions.
 */
export default function UserComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();
	const { user_id } = useParams();
	const userId = user_id;

	const isAdmin = authContext.role === "ADMIN";

	const [user, setUser] = useState(null);
	const [teams, setTeams] = useState(null);
	const [teamsCount, setTeamsCount] = useState(0);
	const [items, setItems] = useState([]);
	const [groups, setGroups] = useState([]);

	useEffect(() => {
		if (userId) {
			refreshUser();
			refreshUserTeams();
			refreshPlan();
		}
	}, [userId]);

	/**
	 * Fetches user's work plan and converts teams to groups and plan entries to items.
	 */
	const refreshPlan = async () => {
		try {
			const response = await retrieveUserTeams(userId);
			setTeams(response.data);

			const fetchedGroups = convertToGroups(response.data);
			setGroups(fetchedGroups);

			const itemsPromises = response.data.map(async (team) => {
				const planEntryResponse = await retrievePlanEntries(
					team.id,
					userId
				);
				return convertToItems(planEntryResponse.data, team.id);
			});

			const fetchedItems = await Promise.all(itemsPromises);
			setItems(fetchedItems.flat());
		} catch (error) {
			console.error(error);
		}
	};

	/**
	 * Fetches current user details
	 */
	function refreshUser() {
		retrieveUser(userId)
			.then((response) => {
				setUser(response.data);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	/**
	 * Fetches the teams associated with the user.
	 */
	function refreshUserTeams() {
		retrieveUserTeams(userId)
			.then((response) => {
				const fetchedTeams = response.data;
				setTeams(fetchedTeams);
				setTeamsCount(fetchedTeams ? fetchedTeams.length : 0);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeleteTeam(teamId) {
		deleteTeamUser(teamId, userId)
			.then((response) => {
				refreshUserTeams();
				refreshPlan();
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeleteUser(userId) {
		deleteUser(userId)
			.then((response) => {
				navigate(ENDPOINTS.ALL_USERS);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	const [searchTerm, setSearchTerm] = useState("");
	const handleSearch = (e) => {
		setSearchTerm(e.target.value);
	};

	// Filters teams based on the search term.
	const filteredTeams = teams
		? teams.filter((team) =>
				`${team.name.toLowerCase()}`.includes(searchTerm.toLowerCase())
		  )
		: [];

	return (
		<Container>
			{user ? (
				<div>
					<div>
						<Card
							className="plan-card border-0"
							style={{ background: "#e3e3e3" }}
						>
							<CardHeader className="fw-bold fs-5">
								{user.firstName} {user.lastName}
							</CardHeader>
							<CardBody>
								<div className="d-flex">
									<Col xs={12}>
										<Card>
											<CardBody>
												<div
													style={{ height: "270px" }}
												>
													{groups.length > 0 ? (
														<div
															className="border"
															style={{
																maxHeight:
																	"260px",
																overflowY:
																	"scroll",
															}}
														>
															<PlanComponent
																items={items}
																groups={groups}
															/>
														</div>
													) : (
														<div>
															There is no work
															plan, because user
															doesn't belong to
															any team...
														</div>
													)}
												</div>
											</CardBody>
										</Card>
									</Col>
								</div>
							</CardBody>
						</Card>
					</div>

					<div className="d-flex gap-2">
						<Col>
							<Card
								className="data-card border-0 my-2"
								style={{
									height: "290px",
									background: "#e3e3e3",
								}}
							>
								<CardBody>
									<div className="SearchList">
										<Form.Control
											type="text"
											placeholder="Search teams..."
											value={searchTerm}
											onChange={handleSearch}
											className="mb-3"
										/>

										<div
											style={{
												maxHeight: "200px",
												overflowY: "auto",
											}}
										>
											<ListGroup>
												{filteredTeams.length > 0 ? (
													filteredTeams.map(
														(team, index) => (
															<ListGroup.Item
																key={index}
															>
																<Row>
																	<Col xs={8}>
																		{
																			team.name
																		}
																	</Col>
																	<Col xs={2}>
																		<Button
																			className="btn-confirm"
																			onClick={() =>
																				navigate(
																					ENDPOINTS.TEAM(
																						team.id
																					)
																				)
																			}
																		>
																			View
																		</Button>
																	</Col>
																	<Col xs={2}>
																		<Button
																			className="btn-delete"
																			disabled={
																				!isAdmin
																			}
																			onClick={() =>
																				handleDeleteTeam(
																					team.id
																				)
																			}
																		>
																			Delete
																		</Button>
																	</Col>
																</Row>
															</ListGroup.Item>
														)
													)
												) : (
													<ListGroup.Item>
														No teams found
													</ListGroup.Item>
												)}
											</ListGroup>
										</div>
									</div>
								</CardBody>
							</Card>
						</Col>
						<Col>
							<Card
								className="data-card border-0 my-2"
								style={{
									height: "290px",
									width: "100%",
									background: "#e3e3e3",
								}}
							>
								<CardBody className="d-flex">
									<Col xs={8} className="d-flex h-100">
										<Card className="mx-2 w-100 h-100">
											<Card.Body className="d-flex flex-column">
												<Card.Title className="fw-bold">
													{user.firstName}{" "}
													{user.lastName}
												</Card.Title>
												<div className="flex-grow-1"></div>
												<ListGroup
													variant="flush"
													className="mt-auto"
												>
													<ListGroup.Item>
														<strong>
															Username:{" "}
														</strong>
														{user.username}
													</ListGroup.Item>
													<ListGroup.Item>
														<strong>
															Teams number:{" "}
														</strong>
														{teamsCount}
													</ListGroup.Item>
												</ListGroup>
											</Card.Body>
										</Card>
									</Col>
									<Col xs={4} className="d-flex h-100">
										<Card className="mx-2 w-100 h-100">
											<CardBody className="d-flex flex-column justify-content-start">
												<Button
													className="btn-delete my-1"
													disabled={!isAdmin}
													onClick={() =>
														handleDeleteUser(
															user_id
														)
													}
												>
													Delete User
												</Button>
											</CardBody>
										</Card>
									</Col>
								</CardBody>
							</Card>
						</Col>
					</div>
				</div>
			) : (
				<div>Loading...</div>
			)}
		</Container>
	);
}

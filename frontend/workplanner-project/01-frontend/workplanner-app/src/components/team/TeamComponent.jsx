import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import { convertToGroups, convertToItems } from "../plan/TimelineUtilities";
import {
	retrieveTeam,
	retrieveTeamUsers,
	deleteTeamUser,
	deleteTeam,
	resetTeamLeader,
	retrievePlanEntries,
	createPlanEntry,
	deletePlanEntry,
} from "../../api/UserService";
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
	Modal,
} from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";
import PlanComponent from "../plan/PlanComponent";
import DeletePlanEntryComponent from "../plan/DeletePlanEntryComponent";

/**
 * Represents a user component
 * It handles the display and management of a team, including team details,
 * user management, and planning entries.
 *
 * @component
 * @returns {JSX.Element} The rendered team component with details and actions.
 */
export default function TeamComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();
	const { team_id } = useParams();
	const teamId = team_id;

	const isAdmin = authContext.role === "ADMIN";

	const [team, setTeam] = useState(null);
	const [teamSize, setTeamSize] = useState(0);
	const [users, setUsers] = useState(null);
	const [isLeader, setIsLeader] = useState(false);

	const [items, setItems] = useState([]);
	const [groups, setGroups] = useState([]);

	const [title, setTitle] = useState("");
	const [startTime, setStartTime] = useState("2024-01-01T08:00");
	const [endTime, setEndTime] = useState("2024-01-01T16:00");
	const [color, setColor] = useState("red");
	const [selectedUser, setSelectedUser] = useState(null);

	const [errorMessage, setErrorMessage] = useState("");
	const [creationFailed, setCreationFailed] = useState(false);

	const [modalShow, setModalShow] = useState(false);
	const [selectedItem, setSelectedItem] = useState(null);
	const [selectedGroup, setSelectedGroup] = useState(null);

	useEffect(() => {
		if (teamId) {
			refreshTeam();
			refreshTeamUsers();
			refreshPlan();
		}
	}, [teamId]);

	/**
	 * Refreshes the plan for the team and its users.
	 * Fetches users and their plan entries, then formats and stores them.
	 */
	const refreshPlan = async () => {
		try {
			const response = await retrieveTeamUsers(teamId);

			const fetchedGroups = convertToGroups(response.data);
			setGroups(fetchedGroups);

			const itemsPromises = response.data.map(async (user) => {
				const planEntryResponse = await retrievePlanEntries(
					teamId,
					user.id
				);
				return convertToItems(planEntryResponse.data, user.id);
			});

			const fetchedItems = await Promise.all(itemsPromises);
			setItems(fetchedItems.flat());
		} catch (error) {
			console.error(error);
		}
	};

	/**
	 * Checks if the current user is the team leader and updates state accordingly.
	 * @param {Object} team - The team object containing team details.
	 */
	function checkIfLeader(team) {
		setIsLeader(team?.teamLeader?.id === authContext.userId ? true : false);
	}

	/**
	 * Refreshes the team details.
	 */
	function refreshTeam() {
		retrieveTeam(teamId)
			.then((response) => {
				const fetchedTeam = response.data;
				setTeam(fetchedTeam);
				checkIfLeader(fetchedTeam);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	/**
	 * Refreshes the users in the team.
	 */
	function refreshTeamUsers() {
		retrieveTeamUsers(teamId)
			.then((response) => {
				const fetchedUsers = response.data;
				setUsers(fetchedUsers);
				setTeamSize(fetchedUsers ? fetchedUsers.length : 0);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeleteUser(userId) {
		deleteTeamUser(teamId, userId)
			.then((response) => {
				refreshTeamUsers();
				refreshTeam();
				refreshPlan();
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeleteTeamLeader() {
		resetTeamLeader(teamId)
			.then((response) => {
				refreshTeam();
				refreshPlan();
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeleteTeam() {
		deleteTeam(teamId)
			.then((response) => {
				navigate(ENDPOINTS.MY_TEAMS);
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleDeletePlanEntry(planEntryId) {
		console.log(planEntryId);
		deletePlanEntry(planEntryId)
			.then((response) => {
				setModalShow(false);
				refreshPlan();
			})
			.catch((error) => {
				console.error(error);
			});
	}

	function handleTitleChange(event) {
		setTitle(event.target.value);
	}

	function handleColorChange(event) {
		setColor(event.target.value);
	}

	function handleStartTimeChange(event) {
		setStartTime(event.target.value);
	}

	function handleEndTimeChange(event) {
		setEndTime(event.target.value);
	}

	function handleSelectedUserChange(event) {
		setSelectedUser(event.target.value);
	}

	function handleItemClick(itemId, e, time) {
		const x = items.find((item) => item.id === itemId);
		setSelectedItem(x);
		const y = groups.find((group) => group.id === x.group);
		setSelectedGroup(y);
		setModalShow(true);
	}

	/**
	 * Validates the form input for creating a new plan entry.
	 * @returns {boolean} - Returns true if the input is valid; otherwise, false.
	 */
	function validate() {
		if (title.length < 4) {
			setErrorMessage("Title too short");
			setCreationFailed(true);
			return false;
		}

		if (title.length > 20) {
			setErrorMessage("Title too long");
			setCreationFailed(true);
			return false;
		}

		if (!startTime || !endTime) {
			setErrorMessage("Dates set incorrectly");
			setCreationFailed(true);
			return false;
		}

		if (endTime <= startTime) {
			setErrorMessage("Dates set incorrectly");
			setCreationFailed(true);
			return false;
		}
		if (!selectedUser) {
			setErrorMessage("User not selected");
			setCreationFailed(true);
			return false;
		}

		return true;
	}

	function handleSubmit() {
		if (validate()) {
			const planEntryDto = {
				title: title,
				startTime: startTime,
				endTime: endTime,
				planEntryColor: color.toUpperCase(),
			};

			createPlanEntry(team_id, selectedUser, planEntryDto)
				.then((response) => {
					console.log(planEntryDto.color);
					setErrorMessage("");
					setCreationFailed(false);
					refreshTeam();
					refreshTeamUsers();
					refreshPlan();
				})
				.catch((error) => {
					setErrorMessage("Creation failed");
					setCreationFailed(true);
				});
		}
	}

	const [searchTerm, setSearchTerm] = useState("");
	const handleSearch = (e) => {
		setSearchTerm(e.target.value);
	};

	// Filters users based on the search term.
	const filteredUsers = users
		? users.filter((user) =>
				`${user.firstName.toLowerCase()} ${user.lastName.toLowerCase()}`.includes(
					searchTerm.toLowerCase()
				)
		  )
		: [];

	return (
		<Container>
			{team ? (
				<div>
					<div>
						<Card
							className="data-card border-0"
							style={{ background: "#e3e3e3" }}
						>
							<CardHeader className="fw-bold fs-5">
								{team.name}
							</CardHeader>
							<CardBody>
								<div className="d-flex gap-2">
									<Col xs={10}>
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
																onItemClick={
																	handleItemClick
																}
															/>
														</div>
													) : (
														<div>
															There is no work
															plan, because there
															are no members of
															this team...
														</div>
													)}
												</div>
											</CardBody>
										</Card>
									</Col>
									<Col xs={2}>
										<Card>
											<CardBody>
												<div>
													<Form
														className="d-flex flex-column justify-content-around"
														style={{
															height: "270px",
														}}
													>
														<Form.Group>
															<Form.Control
																type="text"
																placeholder="Title"
																name="title"
																value={title}
																onChange={
																	handleTitleChange
																}
																autoComplete="off"
															/>
														</Form.Group>

														<div className="d-flex">
															<Col xs={5}>
																<Form.Group className="">
																	<Form.Control
																		type="datetime-local"
																		name="startTime"
																		value={
																			startTime
																		}
																		defaultValue="2024-01-01T08:00"
																		onChange={
																			handleStartTimeChange
																		}
																	/>
																</Form.Group>
															</Col>
															<Col>
																<div>-</div>
															</Col>
															<Col xs={5}>
																<Form.Group className="">
																	<Form.Control
																		type="datetime-local"
																		name="endTime"
																		value={
																			endTime
																		}
																		defaultValue="2024-01-01T16:00"
																		onChange={
																			handleEndTimeChange
																		}
																	/>
																</Form.Group>
															</Col>
														</div>
														<Form.Select
															defaultValue="red"
															onChange={
																handleColorChange
															}
														>
															<option
																key="red"
																value="red"
															>
																Red
															</option>
															<option
																key="green"
																value="green"
															>
																Green
															</option>
															<option
																key="blue"
																value="blue"
															>
																Blue
															</option>
															<option
																key="gray"
																value="gray"
															>
																Gray
															</option>
														</Form.Select>
														<Form.Select
															onChange={
																handleSelectedUserChange
															}
														>
															<option
																value={null}
															>
																No user selected
															</option>
															{users &&
																users.length >
																	0 &&
																users.map(
																	(user) => (
																		<option
																			key={
																				user.id
																			}
																			value={
																				user.id
																			}
																		>
																			{
																				user.firstName
																			}{" "}
																			{
																				user.lastName
																			}
																		</option>
																	)
																)}
														</Form.Select>

														<div
															style={{
																height: "1.2rem",
															}}
														>
															{creationFailed && (
																<div
																	style={{
																		color: "red",
																		fontSize:
																			"0.9rem",
																	}}
																>
																	{
																		errorMessage
																	}
																</div>
															)}
														</div>

														<Button
															className="btn-confirm"
															onClick={
																handleSubmit
															}
															disabled={
																!isAdmin &&
																!isLeader
															}
														>
															Add New Entry
														</Button>
													</Form>
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
											placeholder="Search members..."
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
												{filteredUsers.length > 0 ? (
													filteredUsers.map(
														(user, index) => (
															<ListGroup.Item
																key={index}
															>
																<Row>
																	<Col xs={8}>
																		{
																			user.firstName
																		}{" "}
																		{
																			user.lastName
																		}
																	</Col>
																	<Col xs={2}>
																		<Button
																			className="btn-confirm"
																			onClick={() =>
																				navigate(
																					ENDPOINTS.USER(
																						user.id
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
																				!isAdmin &&
																				!isLeader
																			}
																			onClick={() =>
																				handleDeleteUser(
																					user.id
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
														No members found
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
													{team.name}
												</Card.Title>
												<div className="flex-grow-1"></div>
												<ListGroup
													variant="flush"
													className="mt-auto"
												>
													<ListGroup.Item>
														{team.description}
													</ListGroup.Item>
													<ListGroup.Item>
														<strong>Leader:</strong>{" "}
														{team.teamLeader
															? team.teamLeader
																	.firstName +
															  " " +
															  team.teamLeader
																	.lastName
															: "-"}
													</ListGroup.Item>
													<ListGroup.Item>
														<strong>
															Team Size:
														</strong>{" "}
														{teamSize}
													</ListGroup.Item>
												</ListGroup>
											</Card.Body>
										</Card>
									</Col>
									<Col xs={4} className="d-flex h-100">
										<Card className="mx-2 w-100 h-100">
											<CardBody className="d-flex flex-column justify-content-start">
												<Button
													className="btn-confirm my-1"
													disabled={
														!isAdmin && !isLeader
													}
													onClick={() =>
														navigate(
															ENDPOINTS.ADD_TEAM_USER(
																team_id
															)
														)
													}
												>
													Add New Member
												</Button>
												<Button
													className="btn-confirm my-1"
													disabled={!isAdmin}
													onClick={() =>
														navigate(
															ENDPOINTS.ASSIGN_LEADER(
																team_id
															)
														)
													}
												>
													Assign Leader
												</Button>
												<Button
													className="btn-delete my-1"
													disabled={!isAdmin}
													onClick={
														handleDeleteTeamLeader
													}
												>
													Delete Leader
												</Button>
												<Button
													className="btn-delete my-1"
													disabled={!isAdmin}
													onClick={handleDeleteTeam}
												>
													Delete Team
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
			<DeletePlanEntryComponent
				show={modalShow}
				onHide={() => setModalShow(false)}
				item={selectedItem}
				group={selectedGroup}
				onDelete={handleDeletePlanEntry}
			/>
		</Container>
	);
}

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createTeam } from "../../api/UserService";
import { Card, CardBody, Container, Form, Button } from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a new team component
 * It provides a form to create a new team.
 * It includes input validation and error handling for team creation.
 *
 * @component
 * @returns {JSX.Element} - The rendered form for creating a new team.
 */
export default function NewTeamComponent() {
	const navigate = useNavigate();

	const [teamName, setTeamName] = useState("");
	const [description, setDescription] = useState("");
	const [errorMessage, setErrorMessage] = useState("");
	const [creationFailed, setCreationFailed] = useState(false);

	function handleTeamNameChange(event) {
		setTeamName(event.target.value);
	}

	function handleDescriptionChange(event) {
		setDescription(event.target.value);
	}

	/**
	 * Validates the form input for team name and description.
	 *
	 * @returns {boolean} - True if the input is valid, false otherwise.
	 */
	function validate() {
		if (teamName.length < 4 || teamName.length > 20) {
			setErrorMessage(
				"Team Name must contain min. 5 characters and max. 20 characters..."
			);
			setCreationFailed(true);
			return false;
		}

		if (description.length < 5 || description.length > 30) {
			setErrorMessage(
				"Description must contain min. 5 characters and max. 30 characters..."
			);
			setCreationFailed(true);
			return false;
		}

		return true;
	}

	function handleSubmit() {
		if (validate()) {
			const teamDto = {
				name: teamName,
				description: description,
			};

			createTeam(teamDto)
				.then((response) => {
					navigate(ENDPOINTS.ALL_TEAMS);
				})
				.catch((error) => {
					if (error.status === 409) {
						setErrorMessage("Team Name already in use...");
						setCreationFailed(true);
					}
				});
		}
	}

	return (
		<Container className="d-flex justify-content-center">
			<Card
				className="data-card w-50"
				style={{ minWidth: "300px", maxWidth: "800px" }}
			>
				<CardBody>
					<div className="fw-bold fs-4">New Team</div>
					<div>
						<div className="m-2">
							<Form>
								<Form.Group className="mb-3">
									<Form.Control
										type="text"
										placeholder="Team Name"
										name="teamName"
										value={teamName}
										onChange={handleTeamNameChange}
									></Form.Control>
								</Form.Group>
								<Form.Group className="mb-3">
									<Form.Control
										type="text"
										placeholder="Description"
										name="description"
										value={description}
										onChange={handleDescriptionChange}
									></Form.Control>
								</Form.Group>
								<div style={{ height: "2rem" }}>
									{creationFailed && (
										<div
											style={{
												color: "red",
												fontSize: "0.9rem",
											}}
										>
											{errorMessage}
										</div>
									)}
								</div>
								<Button
									className="btn-confirm my-2 w-50  fs-5"
									onClick={handleSubmit}
								>
									Create team
								</Button>
							</Form>
						</div>
					</div>
				</CardBody>
			</Card>
		</Container>
	);
}

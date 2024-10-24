import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createUser } from "../../api/UserService";
import {
	Row,
	Col,
	Card,
	CardBody,
	Container,
	Form,
	Button,
} from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a signup component
 * It handles user registration, including form validation,
 * account creation, and navigation upon successful signup.
 *
 * @component
 * @returns {JSX.Element} The rendered signup component.
 */
export default function SignupComponent() {
	const navigate = useNavigate();

	const [username, setUsername] = useState("");
	const [firstName, setFirstName] = useState("");
	const [lastName, setLastName] = useState("");
	const [password, setPassword] = useState("");
	const [passwordConfirmation, setPasswordConfirmation] = useState("");
	const [errorMessage, setErrorMessage] = useState("");
	const [signupFailed, setSignupFailed] = useState(false);
	const [signupSuccessfull, setSignupSuccessfull] = useState(false);

	function handleUsernameChange(event) {
		setUsername(event.target.value);
	}

	function handleFirstNameChange(event) {
		setFirstName(event.target.value);
	}

	function handleLastNameChange(event) {
		setLastName(event.target.value);
	}

	function handlePasswordChange(event) {
		setPassword(event.target.value);
	}

	function handlePasswordConfirmationChange(event) {
		setPasswordConfirmation(event.target.value);
	}

	/**
	 * Validates user input for the signup form.
	 * @returns {boolean} True if validation passes; otherwise, false.
	 */
	function validate() {
		if (username.length < 4 || username.length > 20) {
			setErrorMessage(
				"Username must contain min. 4 characters and max. 20 characters..."
			);
			setSignupFailed(true);
			return false;
		}

		if (firstName.length < 4 || firstName.length > 20) {
			setErrorMessage(
				"First name must contain min. 4 characters and max. 20 characters..."
			);
			setSignupFailed(true);
			return false;
		}

		if (lastName.length < 4 || lastName.length > 20) {
			setErrorMessage(
				"Last name must contain min. 4 characters and max. 20 characters..."
			);
			setSignupFailed(true);
			return false;
		}

		if (password.length < 4 || password.length > 20) {
			setErrorMessage(
				"Password must contain min. 3 characters and max. 15 characters..."
			);
			setSignupFailed(true);
			return false;
		}

		if (password !== passwordConfirmation) {
			setErrorMessage("Passwords do not match...");
			setSignupFailed(true);
			return false;
		}
		return true;
	}

	function handleSubmit() {
		if (validate()) {
			const userRegistrationDto = {
				username: username,
				firstName: firstName,
				lastName: lastName,
				password: password,
			};

			createUser(userRegistrationDto)
				.then((response) => {
					setSignupFailed(false);
					setSignupSuccessfull(true);

					setTimeout(() => {
						navigate(ENDPOINTS.LOGIN); // Navigate to login page on successful signup
					}, 1000);
				})
				.catch((error) => {
					if (error.status === 409) {
						setErrorMessage("Username already in use...");
					} else {
						setErrorMessage("Signup failed");
					}
					setSignupFailed(true);
				});
		}
	}

	return (
		<Container className="d-flex justify-content-center align-items-center vh-100">
			<Card className="w-75 login-card">
				<CardBody>
					<Row>
						<Col className="border-end d-flex justify-content-center flex-column">
							<div className="fs-2 fw-bold">WorkPlanner</div>
							<div className="fs-5">
								<p className="mb-0 text-center">
									Already have an account?{" "}
									<a
										onClick={() =>
											navigate(ENDPOINTS.LOGIN)
										}
										className="text-primary fw-bold"
										style={{ cursor: "pointer" }}
									>
										Log in
									</a>
								</p>
							</div>
						</Col>
						<Col>
							<div>
								<div className="m-2">
									<Form>
										<Form.Group className="mb-3">
											<Form.Control
												type="text"
												placeholder="Username"
												name="username"
												value={username}
												onChange={handleUsernameChange}
											></Form.Control>
										</Form.Group>
										<Form.Group className="mb-3">
											<Form.Control
												type="text"
												placeholder="First Name"
												name="firstName"
												value={firstName}
												onChange={handleFirstNameChange}
											></Form.Control>
										</Form.Group>
										<Form.Group className="mb-3">
											<Form.Control
												type="text"
												placeholder="Last Name"
												name="lastName"
												value={lastName}
												onChange={handleLastNameChange}
											></Form.Control>
										</Form.Group>
										<Form.Group className="mb-3">
											<Form.Control
												type="password"
												placeholder="Password"
												name="password"
												value={password}
												onChange={handlePasswordChange}
											></Form.Control>
										</Form.Group>
										<Form.Group className="mb-3">
											<Form.Control
												type="password"
												placeholder="Confirm Password"
												value={passwordConfirmation}
												onChange={
													handlePasswordConfirmationChange
												}
											></Form.Control>
										</Form.Group>
										<div style={{ height: "2rem" }}>
											{signupFailed && (
												<div
													style={{
														color: "red",
														fontSize: "0.9rem",
													}}
												>
													{errorMessage}
												</div>
											)}
											{signupSuccessfull && (
												<div
													style={{
														color: "green",
														fontSize: "0.9rem",
													}}
												>
													Account created,
													redirecting...
												</div>
											)}
										</div>
										<Button
											className="btn-confirm my-3 w-50  fs-5"
											onClick={handleSubmit}
										>
											Create account
										</Button>
									</Form>
								</div>
							</div>
						</Col>
					</Row>
				</CardBody>
			</Card>
		</Container>
	);
}

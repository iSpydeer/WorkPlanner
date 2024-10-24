import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
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
 * Represents a login component
 * It manages the login form state and handles authentication.
 *
 * @component
 * @returns {JSX.Element} The rendered login component.
 */
export default function LoginComponent() {
	const authContext = useAuth();
	const navigate = useNavigate();

	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [loginFailed, setLoginFailed] = useState(false);

	function handleUsernameChange(event) {
		setUsername(event.target.value);
	}

	function handlePasswordChange(event) {
		setPassword(event.target.value);
	}

	async function handleLogin() {
		if (await authContext.login(username, password)) {
			navigate(ENDPOINTS.HOME); // Navigate to home on successful login
		} else {
			setLoginFailed(true);
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
								Great to have you back...
							</div>
						</Col>
						<Col>
							<div className="m-2">
								<Form>
									<Form.Group className="mb-3">
										<Form.Control
											type="text"
											placeholder="Username"
											name="username"
											value={username}
											className="login-inputs text-center"
											onChange={handleUsernameChange}
										/>
									</Form.Group>
									<Form.Group className="mb-3">
										<Form.Control
											type="password"
											placeholder="Password"
											name="password"
											value={password}
											className="login-inputs text-center"
											onChange={handlePasswordChange}
										/>
									</Form.Group>
									<div style={{ height: "1rem" }}>
										{loginFailed && (
											<div
												style={{
													color: "red",
													fontSize: "0.9rem",
												}}
											>
												Login failed...
											</div>
										)}
									</div>
									<Button
										type="button"
										name="login"
										onClick={handleLogin}
										className="btn-confirm my-3 w-100 fs-5"
									>
										Sign in!
									</Button>
								</Form>
								<div>
									<p className="mb-0 text-center">
										New to Workplanner?{" "}
										<a
											onClick={() =>
												navigate(ENDPOINTS.SIGNUP)
											}
											className="text-primary fw-bold"
											style={{ cursor: "pointer" }}
										>
											Sign up
										</a>
									</p>
								</div>
							</div>
						</Col>
					</Row>
				</CardBody>
			</Card>
		</Container>
	);
}

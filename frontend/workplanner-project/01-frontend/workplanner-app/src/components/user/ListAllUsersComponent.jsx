import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { retrieveAllUsers } from "../../api/UserService";
import { Container, Card, CardBody, Row, Col, Button } from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a list all teams component
 * It fetches and displays a list of all users.
 * Each user is displayed with their username and full name,
 * along with a button to view more details about the user.
 *
 * @component
 * @returns {JSX.Element} The rendered list of users.
 */
export default function ListAllUsersComponent() {
	const navigate = useNavigate();
	const [users, setUsers] = useState([]);

	useEffect(() => refreshUsers(), []);

	/**
	 * Fetches all users from the server and updates the users state.
	 */
	function refreshUsers() {
		retrieveAllUsers()
			.then((response) => {
				setUsers(response.data);
			})
			.catch((error) => console.log(error));
	}

	function showUser(userId) {
		navigate(ENDPOINTS.USER(userId));
	}

	return (
		<Container className="w-50">
			<div className="fw-bold fs-4">List of all users</div>
			{users.map((user) => (
				<Card key={user.id} className="list-item-card my-2">
					<CardBody>
						<Row>
							<Col>
								<div className="fw-bold">{user.username}</div>
							</Col>
							<Col>
								<div>
									{user.firstName} {user.lastName}
								</div>
							</Col>
							<Col>
								<Button
									className="btn-confirm w-50"
									onClick={() => showUser(user.id)}
								>
									View
								</Button>
							</Col>
						</Row>
					</CardBody>
				</Card>
			))}
		</Container>
	);
}

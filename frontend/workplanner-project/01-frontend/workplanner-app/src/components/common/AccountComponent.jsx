import { Container, Card, CardBody, CardHeader } from "react-bootstrap";
import { useEffect, useState } from "react";
import { retrieveUser } from "../../api/UserService";
import { useAuth } from "../../security/AuthContext";

/**
 * Represents an account component
 * It retrieves user details using the user ID from the authentication context.
 *
 * @component
 * @returns {JSX.Element} The rendered account component.
 */
export default function AccountComponent() {
	const authContext = useAuth();

	const userId = authContext.userId;
	const [user, setUser] = useState(null);

	useEffect(() => {
		refreshUser(); // Fetch user data when the component mounts
	}, []);

	function refreshUser() {
		retrieveUser(userId).then((response) => {
			setUser(response.data); // Update user state with fetched data
		});
	}

	return (
		<Container className="w-25">
			<Card className="data-card">
				<CardHeader className="fs-5 fw-bold">Account Data</CardHeader>
				<CardBody className="" style={{ height: "200px" }}>
					<div className="d-flex flex-column">
						<div>
							<b>Username: </b>
							{user?.username}
						</div>
						<div>
							<b>First Name: </b>
							{user?.firstName}
						</div>
						<div>
							<b>Last Name: </b>
							{user?.lastName}
						</div>
						<div>
							<b>Role: </b>
							{user?.role.toLowerCase()}
						</div>
					</div>
				</CardBody>
			</Card>
		</Container>
	);
}

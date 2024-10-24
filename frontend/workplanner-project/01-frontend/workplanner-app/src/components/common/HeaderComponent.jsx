import { Link } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import { Navbar, Nav, Container, NavDropdown } from "react-bootstrap";
import { ENDPOINTS } from "../../api/UrlMappings";

/**
 * Represents a navigation header component
 *  It conditionally renders the navigation links based on the user's authentication status.
 *
 * @component
 * @returns {JSX.Element} The rendered navigation header component.
 */
export default function HeaderComponent() {
	const AuthContext = useAuth();
	const isAuthenticated = AuthContext.isAuthenticated; // Check if the user is authenticated

	return (
		<div>
			{isAuthenticated && (
				<header className="border-bottom border-light border-5 mb-3 p-1">
					<Container>
						<Navbar expand="sm">
							<Navbar.Brand className="fs-2 fw-bold">
								WorkPlanner
							</Navbar.Brand>
							<Nav className="ms-auto">
								<Nav.Item className="fs-5 mx-3 nav-highlight">
									<Nav.Link as={Link} to={ENDPOINTS.HOME}>
										Home
									</Nav.Link>
								</Nav.Item>
								<Nav.Item className="fs-5 mx-3 nav-highlight">
									<Nav.Link as={Link} to={ENDPOINTS.MY_TEAMS}>
										Teams
									</Nav.Link>
								</Nav.Item>
								<Nav.Item className="fs-5 mx-3 nav-highlight">
									<Nav.Link
										as={Link}
										to={ENDPOINTS.ALL_USERS}
									>
										Users
									</Nav.Link>
								</Nav.Item>
								<NavDropdown align="end" className="fs-5 mx-3">
									<NavDropdown.Item className="fs-5 test">
										<Nav.Link
											as={Link}
											to={ENDPOINTS.ACCOUNT}
											className="dropdown-item-highlight"
										>
											My Account
										</Nav.Link>
									</NavDropdown.Item>
									<NavDropdown.Item className="fs-5">
										<Nav.Link
											as={Link}
											to={ENDPOINTS.LOGOUT}
											className="dropdown-item-highlight"
										>
											Log out
										</Nav.Link>
									</NavDropdown.Item>
								</NavDropdown>
							</Nav>
						</Navbar>
					</Container>
				</header>
			)}
		</div>
	);
}

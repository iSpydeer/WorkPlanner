import { Container } from "react-bootstrap";

/**
 * Represents an error component
 * It informs users that the requested page was not found.
 *
 * @component
 * @returns {JSX.Element} The rendered error component.
 */
export default function ErrorComponent() {
	return (
		<Container>
			<div className="fs-1 fw-bold">404</div>
			<div className="fs-5">Oops... page not found</div>
		</Container>
	);
}

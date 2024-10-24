import { Container } from "react-bootstrap";

/**
 * Represents an forbidden operation component
 * displays a message indicating that the operation is forbidden.
 *
 * @component
 * @returns {JSX.Element} The rendered forbidden operation component.
 */
export default function OpForbiddenComponent() {
	return (
		<Container>
			<div className="fs-1 fw-bold">401</div>
			<div className="fs-5">Oops... operation forbidden</div>
		</Container>
	);
}

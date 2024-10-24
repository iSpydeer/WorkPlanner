import moment from "moment";
import { Button, Modal } from "react-bootstrap";

/**
 * Represents a plan entry deletion component
 * It handles the deletion of a plan entry.
 * It displays the details of the selected plan entry and provides an option to delete it.
 *
 * @component
 * @param {Object} props - The properties passed to the component.
 * @param {boolean} props.show - Controls whether the modal is visible.
 * @param {Object} props.item - The plan entry item being deleted.
 * @param {Object} props.group - The group to which the plan entry belongs.
 * @param {function} props.onDelete - Function to handle the deletion of the plan entry.
 * @param {function} props.onHide - Function to close the modal.
 * @returns {JSX.Element} The modal for confirming the deletion of a plan entry.
 */
export default function DeletePlanEntryComponent(props) {
	return (
		<Modal show={props.show} onHide={props.onHide} size="md" centered>
			<Modal.Header closeButton>
				<Modal.Title>
					<div className="fw-bold">{props.item?.title}</div>
				</Modal.Title>
			</Modal.Header>
			<Modal.Body>
				<div className="fw-bold">Assigned to: </div>
				{props?.group?.title}
				<div className="fw-bold">Start time: </div>
				{moment(props.item?.start_time).format("DD-MM-YYYY HH:mm")}
				<div className="fw-bold">End time: </div>
				{moment(props.item?.end_time).format("DD-MM-YYYY HH:mm")}
			</Modal.Body>
			<Modal.Footer className="d-flex justify-content-center">
				<Button
					className="btn-delete"
					onClick={() => props.onDelete(props.item?.id)}
				>
					Delete Entry
				</Button>
			</Modal.Footer>
		</Modal>
	);
}

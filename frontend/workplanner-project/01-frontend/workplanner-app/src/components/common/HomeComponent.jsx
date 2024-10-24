import { useState, useEffect } from "react";
import { useAuth } from "../../security/AuthContext";
import { convertToGroups, convertToItems } from "../plan/TimelineUtilities";
import { retrieveUserTeams, retrievePlanEntries } from "../../api/UserService";
import { Container, CardHeader, Card, CardBody } from "react-bootstrap";
import PlanComponent from "../plan/PlanComponent";

/**
 * Represents an home component
 * It is responsible for displaying the user's work plan.
 * It retrieves the user's teams and plan entries, converting them
 * into a format suitable for rendering.
 *
 * @component
 * @returns {JSX.Element} The rendered home component.
 */
export default function HomeComponent() {
	const authContext = useAuth();

	const [items, setItems] = useState([]);
	const [groups, setGroups] = useState([]);

	const userId = authContext.userId; // Get the user ID from the authentication context

	useEffect(() => {
		refreshPlan(); // Fetch user teams and plan entries when the component mounts
	}, []);

	const refreshPlan = async () => {
		try {
			const response = await retrieveUserTeams(userId);

			const fetchedGroups = convertToGroups(response.data); // Convert teams to groups
			setGroups(fetchedGroups);

			const itemsPromises = response.data.map(async (team) => {
				const planEntryResponse = await retrievePlanEntries(
					team.id,
					userId
				); // Convert entries to items
				return convertToItems(planEntryResponse.data, team.id);
			});

			const fetchedItems = await Promise.all(itemsPromises); // Wait for all item promises to resolve
			setItems(fetchedItems.flat());
		} catch (error) {
			console.error(error);
		}
	};

	return (
		<div className="WelcomeComponent">
			<Container>
				<Card
					className="data-card border-0"
					style={{ background: "#e3e3e3" }}
				>
					<CardHeader>
						<div className="fw-bold fs-4">Your Work Plan</div>
					</CardHeader>
					<CardBody>
						<Card>
							<CardBody>
								{groups.length > 0 ? (
									<div
										className="border-start border-end"
										style={{
											maxHeight: "500px",
											overflowY: "scroll",
										}}
									>
										<PlanComponent
											items={items}
											groups={groups}
										/>
									</div>
								) : (
									<div>
										There is no work plan, because you don't
										belong to any team...
									</div>
								)}
							</CardBody>
						</Card>
					</CardBody>
				</Card>
			</Container>
		</div>
	);
}

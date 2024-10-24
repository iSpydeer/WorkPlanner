import "react-calendar-timeline/lib/Timeline.css";
import moment from "moment";
import Timeline, {
	DateHeader,
	SidebarHeader,
	TimelineHeaders,
} from "react-calendar-timeline";

/**
 * Represents a plan component
 * It renders a timeline view of the work plan items and groups.
 *
 * @component
 * @param {Object[]} groups - An array of group objects to be displayed on the timeline.
 * @param {Object[]} items - An array of item objects representing plan entries on the timeline.
 * @param {function} onItemClick - Function to be called when an item on the timeline is clicked.
 * @returns {JSX.Element} The rendered plan component.
 */
export default function PlanComponent({ groups, items, onItemClick }) {
	return (
		<Timeline
			groups={groups}
			items={items}
			defaultTimeStart={moment().add(-12, "hour")}
			defaultTimeEnd={moment().add(12, "hour")}
			timeSteps={{
				second: 1,
				minute: 15,
				hour: 1,
				day: 1,
				month: 1,
				year: 1,
			}}
			lineHeight={50}
			stackItems
			canMove={false}
			canResize={false}
			onItemClick={onItemClick}
			onItemSelect={onItemClick}
		>
			<TimelineHeaders
				style={{ backgroundColor: "#4a9ff5fd", color: "white" }}
			>
				<SidebarHeader>
					{({ getRootProps }) => <div {...getRootProps()}></div>}
				</SidebarHeader>
				<DateHeader unit="primaryHeader" />
				<DateHeader style={{ color: "black" }} />
			</TimelineHeaders>
		</Timeline>
	);
}

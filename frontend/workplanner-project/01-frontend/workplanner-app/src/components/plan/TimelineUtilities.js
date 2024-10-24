import moment from "moment";

const format = "YYYY-MM-DD HH:mm:ss";

/**
 * Converts a list of entries (users or teams) to groups for use in the timeline.
 *
 * @param {Object[]} entries - The array of entries to convert. Each entry can be a user or team.
 * @returns {Object[]} An array of group objects with id and title properties.
 */
export function convertToGroups(entries) {
	return entries.map((entry) => {
		if (entry.name) {
			return {
				id: entry.id,
				title: entry.name,
			};
		} else if (entry.firstName && entry.lastName) {
			return {
				id: entry.id,
				title: `${entry.firstName} ${entry.lastName}`,
			};
		}
	});
}

/**
 * Converts a list of plan entries into timeline items.
 *
 * @param {Object[]} entries - The array of plan entries to convert.
 * @param {string} groupId - The ID of the group (team or user) the entry belongs to.
 *
 * @returns {Object[]} An array of item objects formatted for use in the timeline.
 */
export function convertToItems(entries, groupId) {
	return entries.map((entry) => ({
		id: entry.id,
		group: groupId,
		title: entry.title,
		start_time: moment(entry.startTime, format),
		end_time: moment(entry.endTime, format),
		itemProps: {
			style: {
				background: entry.planEntryColor.toLowerCase(),
				color: "white",
				borderRadius: "10px",
				overflow: "hidden",
				fontSize: "12px",
			},
		},
	}));
}

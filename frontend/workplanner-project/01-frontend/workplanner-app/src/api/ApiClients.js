import axios from "axios";

const apiUrl = process.env.REACT_APP_BASE_URL;

// Creating a reusable Axios instance with a Backend API base URL
export const apiClient = axios.create({ baseURL: apiUrl });

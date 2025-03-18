import axios from 'axios';

const Api = axios.create({
  baseURL: 'http://localhost:8080/api', // Base URL for your backend
  headers: {
    'Content-Type': 'application/json',
  },
});

// // Add a request interceptor (optional)
// Api.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem('token'); // If you use JWT tokens
//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`;
//     }
//     return config;
//   },
//   (error) => {
//     return Promise.reject(error);
//   }
// );

export default Api;
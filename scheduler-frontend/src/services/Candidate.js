import api from './Api';

const CandidateService = {
  // Register a new candidate
  register: async (candidateData) => {
    try {
      const response = await api.post('/candidates', candidateData);
      return response.data;
    } catch (error) {
      throw error.response ? error.response.data : error.message;
    }
  },

  // Login a candidate
  login: async (email, password) => {
    try {
      const response = await api.post('/candidates/login', { email, password });
      return response.data;
    } catch (error) {
      throw error.response ? error.response.data : error.message;
    }
  },

  // Get candidate details by ID
  getCandidateById: async (id) => {
    try {
      const response = await api.get(`/candidates/${id}`);
      return response.data;
    } catch (error) {
      throw error.response ? error.response.data : error.message;
    }
  },

  // Delete a candidate by ID
  deleteCandidate: async (id) => {
    try {
      const response = await api.delete(`/candidates/${id}`);
      return response.data;
    } catch (error) {
      throw error.response ? error.response.data : error.message;
    }
  },

  getSchedulesForCandidate: async (email) => {
    try {
      const response = await api.get('/schedules/candidate', { params: { email } });
      return response.data;
    } catch (error) {
      throw error.response ? error.response.data : error.message;
    }
  },

};

export default CandidateService;
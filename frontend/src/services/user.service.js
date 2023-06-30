import api from './api';

class UserService {
  getUserInfo() {
    return api.get('/user/info');
  }

  addUserInfo({ name, surname, birthDate, description }) {
    return api
        .post("/user/add", {
          name,
          surname,
          birthDate,
          description
        });
  }
}

export default new UserService();

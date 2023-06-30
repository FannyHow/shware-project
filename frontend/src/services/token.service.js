class TokenService {
  getLocalRefreshToken() {
    const user = JSON.parse(localStorage.getItem("user"));
    return user?.refreshToken;
  }

  getLocalToken() {
    const user = JSON.parse(localStorage.getItem("user"));
    return user?.token;
  }

  updateLocalToken(token) {
    let user = JSON.parse(localStorage.getItem("user"));
    user.token = token;
    localStorage.setItem("user", JSON.stringify(user));
  }

  setUser(user) {
    console.log(JSON.stringify(user));
    localStorage.setItem("user", JSON.stringify(user));
  }

  removeUser() {
    localStorage.removeItem("user");
  }
}

export default new TokenService();

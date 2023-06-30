import { createStore } from "vuex";
import { auth } from "./auth.module";
import { socket } from "./socket.module";

const store = createStore({
  modules: {
    auth,
    socket
  },
});

export default store;

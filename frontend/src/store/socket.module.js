import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

const user = JSON.parse(localStorage.getItem('user'));
let sockJS = new SockJS("http://localhost:41147/socket/notifications");
const initialStateUser = user
    ? { status: { loggedIn: true }, user, notifications: 0, stompClient: null}
    : { status: { loggedIn: false }, user: null, notifications: 0, stompClient: null}



export const socket = {
    namespaced: true,
    state: initialStateUser,
    getters: {
        getNotifications: state => state.notifications
    },
    actions: {
        connect({ commit, state }, user) {
            sockJS = new SockJS("http://localhost:41147/socket/notifications");
            state.stompClient = Stomp.over(sockJS);
            state.stompClient.connect(
                { username: user.username },
                frame => {
                    console.log(frame);
                    commit('connectTrue');
                    state.stompClient.send("/swns/start", {});
                    state.stompClient.subscribe("/user/notification/item", () => {
                        // this.$store.received_messages.push(JSON.parse(tick.body).content);
                        commit('incrementNotifications')
                    });

                },
                () => {
                    commit('connectFalse');
                }
            );
        },
        disconnect({ commit, state }) {
            if (state.stompClient) {
                state.stompClient.disconnect();
                sockJS.close();
            }
            commit('connectFalse');
        },
    },
    mutations: {
        connectTrue(state) {
            state.connected = true;
        },
        connectFalse(state) {
            state.connected = false;
        },
        incrementNotifications(state) {
            state.notifications++;
        },
        setNotifications(state, value) {
            state.notifications = value;
        }
    }
};

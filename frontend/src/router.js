import { createWebHistory, createRouter } from "vue-router";
import Home from "./components/Home.vue";
import Login from "./components/Login.vue";
import Register from "./components/Register.vue";
// lazy-loaded
const Notifications = () => import("./components/Notifications.vue")
const Profile = () => import("./components/Profile.vue")
const BoardAdmin = () => import("./components/BoardAdmin.vue")
const BoardUser = () => import("./components/BoardUser.vue")
const AddProject = () => import("./components/AddProject.vue")
const Messages = () => import("./components/Messages.vue")
const Favourites = () => import("./components/Favourites.vue")

const routes = [
  {
    path: "/",
    name: "home",
    component: Home,

  },
  {
    path: "/home",
    component: Home,
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/register",
    component: Register,
  },
  {
    path: "/profile",
    name: "profile",
    // lazy-loaded
    component: Profile,
  },
  {
    path: "/notifications",
    name: "notifications",
    // lazy-loaded
    component: Notifications,
  },
  {
    path: "/favourites",
    name: "favourites",
    // lazy-loaded
    component: Favourites,
  },
  {
    path: "/messages",
    name: "Messages",
    // lazy-loaded
    component: Messages,
  },
  {
    path: "/notifications",
    name: "notifications",
    // lazy-loaded
    component: Notifications,
  },
  {
    path: "/admin",
    name: "admin",
    // lazy-loaded
    component: BoardAdmin,
  },
  {
    path: "/user",
    name: "user",
    // lazy-loaded
    component: BoardUser,
  },
  {
    path: "/add",
    name: "add",
    // lazy-loaded
    component: AddProject,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});



export default router;

<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <a href="/" class="navbar-brand">TeamBuilder</a>
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link to="/home" class="nav-link">
            <font-awesome-icon icon="list" /> Projects
          </router-link>
        </li>
        <li class="nav-item">
          <router-link v-if="currentUser" to="/add" class="nav-link">
            <font-awesome-icon icon="plus" /> Add New Project
          </router-link>
        </li>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" /> Sign Up
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" /> Login
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/notifications" class="nav-link">
            <font-awesome-icon icon="bell"/>
            Notifications
            <label v-if="currentNotifications !== 0">
              ({{ currentNotifications }})
            </label>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/favourite" class="nav-link">
            <font-awesome-icon icon="heart"/>
            Favourite
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/messages" class="nav-link">
            <font-awesome-icon icon="envelope"/>
            Messages
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt" /> LogOut
          </a>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
import EventBus from "./common/EventBus";
import ProjectService from "./services/project.service";

export default {

  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    currentNotifications() {
      return this.$store.state.socket.notifications;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$store.dispatch('socket/disconnect');
      this.$store.state.socket.notifications = 0;
      this.$router.push('/login');
    },
  },
  mounted() {
    EventBus.on("logout", () => {
      this.logOut();
    });
    if (this.currentUser !== null) {
      this.$store.dispatch("socket/connect", this.currentUser);
      ProjectService.getNotifications().then(
          (response) => {
            this.content = null;
            this.$store.state.socket.notifications += response.data.checkUserAsLeaderResponseDtoList.length;
            },
          (error) => {

            this.content =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();

            if (error.response && (error.response.status === 403 || error.response.status === 401))
              EventBus.dispatch("logout");
          }
      );
    }

  },
  beforeUnmount() {
    EventBus.remove("logout");
  },
  beforeMount() {
    this.$store.state.socket.notifications = 0;
  }
};
</script>

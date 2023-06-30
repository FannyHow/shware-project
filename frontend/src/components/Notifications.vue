<template>
  <div class="alert alert-danger" role="alert" v-if="content != null" style="margin: 1em">
    {{ content }}
  </div>
  <div class="alert alert-success" role="alert" v-if="checks.length !== 0" style="margin: 1em">
    Всего проектов: {{ checks.length }}
  </div>
  <div>
    <div class="card text-center" style="margin: 1em" v-for="check in checks" :key="check.userProjectId">
      <div class="card-header">
        Ваш проект
      </div>
      <div class="card-body">
        <h5 class="card-title">{{ check.projectResponseDto.name }}</h5>
        <p class="card-text">{{ check.projectResponseDto.description }}</p>
        <span class="badge bg-success text-white" v-for="tagName in check.projectResponseDto.tagNameList" :key="tagName" style="margin: 0.1em">
        {{ tagName }}
      </span>
      </div>

      <div class="card-header text-muted">
        Дедлайн: {{ formatDate(check.projectResponseDto.endDate) }}
      </div>
      <div class="card-footer text-muted">
        К вашему проекту хочет присоединится
      </div>
      <div class="card-body">
        <h5 class="card-title">{{ check.username }}</h5>
        <p class="card-text">{{ check.userDescription }}</p>
        <span class="badge bg-success text-white" v-for="userTagName in check.userTagNameList" :key="userTagName" style="margin: 0.1em">
          {{ userTagName }}
        </span>
      </div>
      <div class="card-footer text-white">
        <a class="btn btn-success" style="margin: 0.1em" v-on:click="sendInvitation({ 'projectId' : check.projectResponseDto.id, 'userProjectId' : check.userProjectId, 'isAccepted': true, 'username': check.username })">Подходит</a>
        <a class="btn btn-danger" style="margin: 0.1em" v-on:click="sendInvitation({ 'projectId' : check.projectResponseDto.id, 'userProjectId' : check.userProjectId, 'isAccepted': false, 'username': check.username })">Не подходит</a>
      </div>
    </div>
  </div>
</template>

<script>
import ProjectService from "../services/project.service";
import EventBus from "../common/EventBus";
import { mapGetters } from "vuex";

export default {
  name: "Notifications",
  data() {
    return {
      checks: [],
      content: "Проверяем, есть ли новые уведомления...",
    };
  },
  computed:
    mapGetters({
      notifications: 'socket/getNotifications'
    }),
  methods: {
    formatDate: d => new Date (d).toLocaleString('ru-RU', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),
    sendInvitation(invitation) {
      ProjectService.sendInvitation(invitation).then(
          () => {
            // this.content = response.data;
            this.getNotifications();
          },
          (error) => {
            this.content =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();

            if (error.response && (error.response.status === 403 || error.response.status === 401)) {
              EventBus.dispatch("logout");
            }
          }
      );
    },
    getNotifications() {
      ProjectService.getNotifications().then(
          (response) => {
            this.content = null;
            this.checks = response.data.checkUserAsLeaderResponseDtoList;
            this.$store.state.socket.notifications = response.data.checkUserAsLeaderResponseDtoList.length;
            if (this.checks.length === 0) this.content = "Новых уведомлений не приходило!";
          },
          (error) => {
            this.content =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();

            if (error.response && error.response.status === 404 )
              this.content = 'Запросов не найдено!'
            console.log(error.response)

            if (error.response && (error.response.status === 403 || error.response.status === 401
               || error.response.data.message === "class java.lang.String cannot be cast to class org.springframework.security.core.userdetails.UserDetails (java.lang.String is in module java.base of loader 'bootstrap'; org.springframework.security.core.userdetails.UserDetails is in unnamed module of loader 'app')"))
              EventBus.dispatch("logout");
          }
      );
    },
  },
  mounted() {
    this.getNotifications();
  },
  watch: {
    notifications: {
      handler: function () {
        this.getNotifications()
      },
      deep:true
    }
  },
};
</script>

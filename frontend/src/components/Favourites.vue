<template>
  <div class="alert alert-danger" role="alert" v-if="content != null" style="margin: 1em">
    {{ content }}
  </div>
  <div class="alert alert-success" role="alert" v-if="checks.length !== 0" style="margin: 1em">
    Всего проектов: {{ checks.length }}
  </div>
  <div class="card text-center" style="margin: 1em" v-for="project in projects" :key="project.id">
    <div class="card-header">
      {{ project.leaderEmail }}
    </div>
    <div class="card-body">
      <h5 class="card-title">{{ project.name }}</h5>
      <p class="card-text">{{ project.description }}</p>
      <p class="card-text" style="margin-top: 1em" v-if="this.$store.state.auth.user">{{ project.answer }}</p>
    </div>
    <div class="card-footer text-white" v-if="project.tagNameList.length !== 0">
      <span class="badge bg-success" v-for="tagName in project.tagNameList" :key="tagName" style="margin: 0.1em">
        {{ tagName }}
      </span>
    </div>

    <div class="card-footer text-muted">
      Дедлайн: {{ formatDate(project.end_date) }}
    </div>
  </div>
</template>

<script>

import ProjectService from "../services/project.service";

export default {
  name: "Favourites",
  data() {
    return {
      checks: [],
      projects: [],
      content: "К сожалению, у вас нет никаких избранных ",
    };
  },
  methods: {
    formatDate: d => new Date (d).toLocaleString('ru-RU', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),
    getFavouriteProjects() {
      ProjectService.getFavouriteProjects().then(
          (response) => {
            this.content = null;
            this.projects = response.data.projects;
          },
          (error) => {
            this.content =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();
          }
      );
    },
  },
  mounted() {
    this.getFavouriteProjects();
  },
};
</script>

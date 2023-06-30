<template>
  <form class="d-flex" style="margin: 1em">
    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" v-model="name">
    <button class="btn btn-outline-success" type="button" @click="search">Search</button>
  </form>
  <div class="alert alert-danger" role="alert" v-if="content != null" style="margin: 1em">
    {{ content }}
  </div>
  <div style="margin: 1em">
    <div class="form-group">
      <button
          v-on:click="addTag(tag.tagName)"
          type="button"
          class="btn btn-warning"
          v-for="tag in tags"
          :key="tag"
          style="margin: 0.1em">
        {{ tag.tagName }}
      </button>
    </div>
  </div>
  <div style="margin: 1em">
    <div class="form-group">
      <button
          v-on:click="addTag(tag)"
          type="button"
          class="btn btn-success"
          v-for="tag in tagNameList"
          :key="tag"
          style="margin: 0.1em"
      >
        {{ tag }}
      </button>
    </div>
  </div>
  <div class="card text-center" style="margin: 1em" v-for="project in projects" :key="project.id">
    <div class="card-header">
      {{ project.leaderEmail }}
    </div>
    <div class="card-body">
      <h5 class="card-title">{{ project.name }}</h5>
      <p class="card-text">{{ project.description }}</p>
      <a class="btn btn-primary" v-on:click="addUserToProject({'id': project.id})" v-if="this.$store.state.auth.user">Подать заявку</a>
      <a class="btn btn-primary" v-on:click="addProjectToFavourite({'id': project.id})" v-if="this.$store.state.auth.user">Подать заявку</a>
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
  <div class="text-center" style="margin: 1em">
    <div class="form-group">
      <button
          v-on:click="getPage(n-1)"
          type="button"
          class="btn btn-success"
          v-for="n in number_of_pages"
          :key="n"
          style="margin: 0.1em">
        {{ n }}
      </button>
    </div>
  </div>
</template>

<script>
import ProjectService from "../services/project.service";
import TagService from "../services/tag.service";
import EventBus from "../common/EventBus";


export default {
  name: "Home",
  filters: {

  },
  data() {
    return {
      name: "",
      projects: "",
      size: 7,
      number_of_elements: null,
      number_of_pages: null,
      page: 0,
      tags: [],
      content: null,
      tagNameList: [],
      requestedTags: {
        'tags' : []
      },
    };
  },
  mounted() {
    ProjectService.getNumberOfElements().then(
        (response) => {
          this.content = null;
          this.number_of_elements = response.data;
          this.number_of_pages = Math.ceil(this.number_of_elements / this.size);
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
    ProjectService.getProjectsWithCustomParams("", this.page, this.size, this.tagNameList).then(
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
    TagService.getCategories().then(
        (response) => {
          this.content = null;
          this.tags = response.data;
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
  methods: {
    formatDate: d => new Date (d).toLocaleString('ru-RU', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),
    search() {
      this.content = null;
      this.projects = null;
      this.page = 0;
      ProjectService.getProjectsWithCustomParams(this.name, "", "", this.tagNameList).then(
          (response) => {
            this.number_of_elements = response.data.numberOfProjects;
            this.number_of_pages = Math.ceil(this.number_of_elements / this.size);
            ProjectService.getProjectsWithCustomParams(this.name, this.page, this.size, this.tagNameList).then(
                (response) => {
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
    getPage(page) {
      this.page = page;
      this.projects = null;
      ProjectService.getProjectsWithCustomParams(this.name, this.page, this.size, this.tagNameList).then(
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
      window.scrollTo(0,0);
    },
    addTag(tagName){
      this.tagNameList.includes(tagName) ? this.tagNameList.splice(this.tagNameList.indexOf(tagName),1) : this.tagNameList.push(tagName);
      this.page = 0;
      ProjectService.getProjectsWithCustomParams("", "", "", this.tagNameList).then(
          (response) => {
            this.number_of_elements = response.data.numberOfProjects;
            this.number_of_pages = Math.ceil(this.number_of_elements / this.size);
            ProjectService.getProjectsWithCustomParams(this.name, this.page, this.size, this.tagNameList).then(
                (response) => {
                  this.content = null;
                  this.projects = response.data.projects;

                  for (const tagName in this.tagNameList)
                    this.requestedTags.tags.push({
                      'tagName': this.tagNameList[tagName],
                      'categoryName': ''
                    });

                  TagService.getCheckedTags(this.requestedTags).then(
                      (response) => {
                        this.tags = response.data;
                      });

                  this.requestedTags = {
                    'tags' : []
                  };
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
    },
    addProjectToFavourite(projectId) {
      ProjectService.addFavouriteProject(projectId).then(
          (response) => {
            this.content = null;
            for (const project in this.projects)
              if (this.projects[project].id === projectId.id)
                this.projects[project].answer = response.data.message;
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
    },
  },
};
</script>

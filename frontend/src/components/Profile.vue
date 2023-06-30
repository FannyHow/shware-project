<template>
  <div class="container">
    <div v-if="message" class="alert alert-danger" role="alert">
      {{ message }}
    </div>
    <div class="alert alert-danger" role="alert" v-if="content != null" style="margin: 1em">
      {{ content }}
    </div>
    <Form @submit="addUserInfo" :validation-schema="schema">
      <div class="container rounded bg-white mt-5 mb-5">
        <div class="row">
          <div class="col-md-5 border-right">
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-right">My Profile</h4>
              </div>
              <div class="row mt-2">
                <div class="col-md-6">
                  <span class="font-weight-normal" v-if="info != null">User: {{ info.username }}</span>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col-md-6"><label class="labels">Name</label>
                  <Field name="name" type="text" class="form-control" v-bind:placeholder="getName()" />
                  <ErrorMessage name="name" class="error-feedback" />
                </div>
                <div class="col-md-6">
                  <label class="labels">Surname</label>
                  <Field name="surname"  type="text" class="form-control" v-bind:placeholder="getSurname()" />
                  <ErrorMessage name="surname" class="error-feedback" />
                </div>
              </div>
              <div class="row mt-3">
                 <div class="col-md-6">
                  <span>Birth date: <p/>{{ getBirthDate() }}<p/></span>
                  <Field name="birthDate" type="datetime-local" class="form-control" v-bind:placeholder="getBirthDate()"/>
                  <ErrorMessage name="birthDate" class="error-feedback" />
                </div>
              </div>
              <div class="row mt-1">
                <div class="col-md-6">
                  <label>Description</label>
                  <Field name="description" as="textarea" type="text" class="form-control" v-bind:placeholder="getDescription()"/>
                  <ErrorMessage name="description" class="error-feedback" />
                </div>
              </div>
              <div class="mt-5 text-center">
                <div class="form-group">
                  <button class="btn btn-primary btn-block" :disabled="loading">
                  <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                    <span>Add user info</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-5">
            <div class="p-3 py-5">
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-right">My Tags</h4>
              </div>
              <div class="" >
                <div class="form-group">
                  <div class="" v-if="!added_tags.tags.isEmpty" >
                    <label><p/>Added tags:</label>
                    <span class="badge bg-success text-white" v-for="tag in added_tags.tags" :key="tag" style="margin: 0.1em">
                      {{ tag.tagName }}
                    </span>
                  </div>
                  <label><p/>Add some new tags:<p/></label>
                  <button v-on:click="addTagToUser({'tagName': tag.tagName, 'categoryName': ''})" type="button" class="btn btn-success" v-for="tag in tags" :key="tag" style="margin: 0.1em">
                    {{ tag.tagName }}
                  </button>
                  <ErrorMessage name="tags" class="error-feedback" />
                </div>
              </div>
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-right">My Projects</h4>
              </div>
              <div class="card text-center" style="margin: 1em" v-for="project in projects" :key="project.id">
                <div class="card-body">
                  <h5 class="card-title">{{ project.name }}</h5>
                  <p class="card-text">{{ project.description }}</p>
                  <!--      TODO: ДОБАВИТЬ КНОПКУ ДЛЯ УДАЛЕНИЯ ДЛЯ АДМИНА  -->
                  <p class="card-text "><em>{{ connections[project.id] }}</em></p>
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
            </div>
          </div>
        </div>
      </div>
    </Form>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import UserService from "../services/user.service";
import EventBus from "../common/EventBus";
import ProjectService from "../services/project.service";
import TagService from "../services/tag.service";

export default {
  name: 'Profile',
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      name:
          yup
              .string(),
      surname:
          yup
              .string(),
      birthDate:
          yup
              .date(),
      description:
          yup
              .string(),

    });
    return {
      tags: null,
      connections: [],
      added_tags: {
        'tags' : []
      },
      info: null,
      content: null,
      message: null,
      projects: null,
      loading: false,
      schema,
      months: [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June',
        'July',
        'August',
        'September',
        'October',
        'November',
        'December'
      ],
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) this.$router.push('/login');
    ProjectService.getProfileProjects().then(
        (response) => {
          this.projects = response.data.projectListResponseDto.projects;
          this.connections = response.data.connections;
        },
        (error) => {

          let message = (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
              error.message ||
              error.toString();

          if (message === 'Error: Projects not found') {
            TagService.getCategories().then(
                (response) => {
                  this.tags = response.data;
                  this.content = null;
                },
            );
          } else this.content = message;
        }
    );
    UserService.getUserInfo().then(
        (response) => {
          this.info = response.data;
        },
        (error) => {
          if (error.response && (error.response.status === 403 || error.response.status === 401)) {
            EventBus.dispatch("logout");
          }

          this.content =
              (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
              error.message ||
              error.toString();
        }
    );
    TagService.getUserTags().then(
        (response) => {
          response.data.forEach(
              tag =>
                  this.added_tags.tags.push({
                    'tagName': tag.tagName,
                    'categoryName': ''
                  })
          );
          TagService.getCheckedTags(this.added_tags).then(
              (response) => {

                this.tags = response.data;
              });
        },
        (error) => {
          if (error.response && (error.response.status === 403 || error.response.status === 401)) {
            EventBus.dispatch("logout");
          }

          let message = (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
              error.message ||
              error.toString();

          if (message === 'Error: Tags Not Found') {
            TagService.getCategories().then(
                (response) => {
                  this.tags = response.data;
                  this.content = null;
                },
            );
          } else this.content = message;

        }
    );
  },
  methods: {
    formatDate: d => new Date (d).toLocaleString('ru-RU', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),
    getName() {
      return this.info == null  || this.info.name == null ? 'Write your name' : this.info.name;
    },
    getSurname() {
      return this.info == null  || this.info.surname == null ? 'Write your surname' : this.info.surname;
    },
    getBirthDate() {
      return this.info == null  || this.info.birthDate == null ? 'Write your birth date' : new Date(this.info.birthDate).getDate() + " " + this.months[new Date(this.info.birthDate).getMonth()] + " " + new Date(this.info.birthDate).getFullYear();
    },
    getDescription() {
      return this.info == null  || this.info.description == null ? 'Write your description' : this.info.description;
    },
    addUserInfo(user) {
      UserService.addUserInfo(user).then(
          (response) => {
            this.info = response.data;
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
    addTagToUser(tag) {
      TagService.addTagToUser(tag).then(
          () => {
            this.added_tags.tags.push({
              'tagName': tag.tagName,
              'categoryName': ''
            });
            TagService.getCheckedTags(this.added_tags).then(
                (response) => {
                  this.tags = response.data;
                });
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
  },
};
</script>
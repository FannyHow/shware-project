<template>
  <div class="card card-container" v-if="isProjectCreated">
    <label for="project" class="font-weight-bold">ADD NEW PROJECT</label>
    <Form @submit="addProject" :validation-schema="schema">
      <div class="form-group">
        <label for="name">Name</label>
        <Field name="name" type="text" class="form-control" />
        <ErrorMessage name="name" class="error-feedback" />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <Field name="description" as="textarea" type="text" class="form-control" />
        <ErrorMessage name="description" class="error-feedback" />
      </div>
      <div class="form-group">
        <label for="connect">Way to connect</label>
        <Field name="connect" as="textarea" type="text" class="form-control" placeholder="Describe how users can connect with you"/>
        <ErrorMessage name="connect" class="error-feedback" />
      </div>
      <div class="form-group">
        <label for="startDate">Start date</label>
        <Field name="startDate" type="datetime-local" class="form-control" />
        <ErrorMessage name="startDate" class="error-feedback" />
      </div>
      <div class="form-group">
        <label for="endDate">Deadline</label>
        <Field name="endDate" type="datetime-local" class="form-control" />
        <ErrorMessage name="endDate" class="error-feedback" />
      </div>

      <div class="form-group">
        <button class="btn btn-primary btn-block" :disabled="loading">
          <span
              v-show="loading"
              class="spinner-border spinner-border-sm"
          ></span>
          <span>Add new project</span>
        </button>
      </div>

      <div class="form-group">
        <div v-if="message" class="alert alert-danger" role="alert">
          {{ message }}
        </div>
        <div v-if="content" class="alert alert-success" role="alert">
          {{ content }}
        </div>
      </div>
    </Form>
  </div>
  <div class="card card-container" v-if="!isProjectCreated">
    <label for="tag" class="font-weight-bold">ADD TAGS TO YOUR PROJECT</label>
      <div class="form-group">
        <button v-on:click="addTag({'id': project.id, 'tagName': tag.tagName})" type="button" class="btn btn-success" v-for="tag in tags" :key="tag" style="margin: 0.1em">
          {{ tag.tagName }}
        </button>
        <ErrorMessage name="tags" class="error-feedback" />
        <div class="" v-if="added_tags.tags !== null" style="margin: 1em">
          <label for="tag">Added tags:</label>
          <span class="badge bg-success text-white" v-for="tag in added_tags.tags" :key="tag" style="margin: 0.1em">
              {{ tag.tagName }}
          </span>
        </div>
      </div>
      <div class="form-group">
        <div v-if="message" class="alert alert-danger" role="alert">
          {{ message }}
        </div>
        <div v-if="content" class="alert alert-success" role="alert">
          {{ content }}
        </div>
      </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import ProjectService from "../services/project.service";
import TagService from "../services/tag.service";
import EventBus from "../common/EventBus";

export default {
  name: "Login",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      name:
          yup
              .string()
              .required("Name is required!"),
      description:
          yup
              .string()
              .required("Description is required!"),
      connect:
          yup
              .string()
              .required("A way to connect is required!"),
      startDate:
        yup
              .date()
              .required("Start date is required!"),
      endDate:
          yup
              .date()
              .min(
                  yup.ref('startDate'),
                  "Deadline can't be before start date"
              )
              .required("End date is required!"),
    });

    return {
      loading: false,
      message: "",
      content: "",
      project: null,
      tags: null,
      added_tags: {
        'tags' : []
      },
      schema,
    };
  },
  computed: {
    isProjectCreated() {
      return this.project === null;
    },

  },
  mounted() {
    TagService.getCategories().then(
        (response) => {
          this.tags = response.data;
          this.content = null;
        },
    );
  },
  created() {
  },
  methods: {
    addProject(project) {
      ProjectService.addNewProject(project).then(
          (response) => {
            this.project = response.data;
          },
          (error) => {
            this.message =
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
    addTag(tag) {
      TagService.addTagToProject(tag).then(
          () => {
            this.added_tags.tags.push({
              'tagName': tag.tagName,
              'categoryName': ''
            });
            TagService.getCheckedTags(this.added_tags).then(
                (response) => {
                  this.tags = response.data;
                  this.content = null;
                });
          },
          (error) => {
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();

            if (error.response && (error.response.status === 403 || error.response.status === 401)) EventBus.dispatch("logout");

          }
      );
    },
  },
};
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}

.error-feedback {
  color: red;
}
</style>

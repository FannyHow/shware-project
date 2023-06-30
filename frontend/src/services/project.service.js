import api from './api';

class ProjectService {

    getProjectsWithCustomParams(name, page, size, tagNameList) {
        return api.get(`/project/get/all/custom?page=${page}&size=${size}&name=${name}&tagNameList=${tagNameList}`)
    }

    getNumberOfElements() {
        return api.get(`/project/get/number`)
    }

    getProfileProjects() {
        return api.get(`/project/get/profile`)
    }

    getFavouriteProjects() {
        return api.get(`/project/get/favourite`)
    }

    addNewProject({ name, description, connect, startDate, endDate }) {
        return api
            .post("/project/add/project", {
                name,
                startDate,
                connect,
                endDate,
                description
            });
    }

    addFavouriteProject({ id }) {
        return api
            .post("/project/add/favourite", {
                id
            })
    }

    addUserToProject({ id }) {
        return api
            .post("/project/add/user", {
                id
            })
    }

    getNotifications() {
        return api
            .get("/project/get/notifications")
    }

    sendInvitation({ projectId, userProjectId, isAccepted, username }) {
        return api.post("/project/invite",
            { projectId, userProjectId, isAccepted, username }
        )
    }

    sendMessage({ projectId, userProjectId, isAccepted, username }) {
        return api.post("/project/message",
            { projectId, userProjectId, isAccepted, username }
        )
    }

}

export default new ProjectService();

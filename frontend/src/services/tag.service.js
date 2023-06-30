import api from './api';

class TagService {

    getCategories() {
        return api.get('/tag/get/categories');
    }

    getCheckedTags(tags) {
        return api.post('/tag/check/tags', tags);
    }

    addTagToProject({id, tagName}) {
        return api.post("/tag/add/project", {
            id,
            tagName
        });
    }

    getUserTags() {
        return api.get('/tag/get/profile');
    }

    addTagToUser({tagName, categoryName}) {
        return api.post("/tag/add/user", {
            tagName,
            categoryName
        });
    }

}

export default new TagService();

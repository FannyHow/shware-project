<template>
  <div>
    <chat-window
        :height="'calc(100vh - 5vh)'"
        :current-user-id="currentUserId"
        :rooms="rooms"
        :rooms-loaded="roomsLoaded"
        :loading-rooms="loadingRooms"
        :messages="messages"
        :messages-loaded="messagesLoaded"
        @fetch-messages="onFetchMessages"
    />
  </div>
</template>

<script>
import ChatWindow from "vue-advanced-chat"
import "vue-advanced-chat/dist/vue-advanced-chat.css"
import ProjectService from "../services/project.service";
import EventBus from "../common/EventBus";
export default {
  name: "Messages",
  components: {
    ChatWindow,
  },
  data() {
    return {
      messages: []
    }
  },
  methods: {
    onFetchMessages() {
      setTimeout(() => {
        this.sendMessage()
      })
    },
    sendMessage(message) {
      ProjectService.sendMessage(message).then(
          (response) => {
            this.messages = response.data.messages;
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
}
</script>

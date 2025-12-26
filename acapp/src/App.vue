<template>
  <div class="window">
    <div class="game-body">
      <MenuViewVue v-if="$store.state.router.router_name === 'menu'" />
      <PkIndexViewVue v-else-if="$store.state.router.router_name === 'pk'" />
      <RecordContentViewVue v-else-if="$store.state.router.router_name === 'record_content'" />
      <RecordIndexViewVue v-else-if="$store.state.router.router_name === 'record'" />
      <RankListIndexViewVue v-else-if="$store.state.router.router_name === 'ranklist'" />
      <UserBotIndexViewVue v-else-if="$store.state.router.router_name === 'user_bot'" /><!--没有子标签可以这样写<.../> -->
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex'
import MenuViewVue from "./views/MenuView.vue"
import PkIndexViewVue from "./views/pk/PkIndexView.vue"
import RecordContentViewVue from "./views/record/RecordContentView.vue"
import RecordIndexViewVue from "./views/record/RecordIndexView.vue"
import UserBotIndexViewVue from "./views/user/bot/UserBotIndexView.vue"
import RankListIndexViewVue from "./views/ranklist/RankListIndexView.vue"
import $ from 'jquery'

export default {
  components: {
    MenuViewVue,
    PkIndexViewVue,
    RecordContentViewVue,
    RecordIndexViewVue,
    RankListIndexViewVue,
    UserBotIndexViewVue,
  },
  setup() {
    const store = useStore();
    $.ajax({
      url: "https://app7634.acapp.acwing.com.cn/api/user/account/acwing/acapp/apply_code/",
      type: "get",
      success: resp => {
        if (resp.result === "success") {
          store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp => {
            if (resp.result === "success") {
              const jwt_token = resp.jwt_token;
              store.commit('updateToken', jwt_token);
              store.dispatch('getinfo', {
                success() {
                  store.commit('updatePullingInfo', false);
                },
                error() {
                  store.commit('updatePullingInfo', false);
                },
              });
            } else { store.state.user.AcWingOS.api.window.close(); }
          });
        } else { store.state.user.AcWingOS.api.window.close(); }
      }
    })
  }
}
</script>

<style scoped>
body {
  margin: 0;
}

div.game-body {
  background-image: url('@/assets/images/background.png');
  background-size: cover;
  width: 100%;
  height: 100%;
}

div.window {
  width: 100vw;
  height: 100vh;
}
</style>

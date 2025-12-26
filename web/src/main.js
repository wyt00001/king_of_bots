import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store' //store文件夹下的方法会被注册

createApp(App).use(store).use(router).mount('#app')

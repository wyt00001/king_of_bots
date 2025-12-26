import { createApp } from 'vue'
import App from './App.vue'
import store from './store' //store文件夹下的方法会被注册

createApp(App).use(store).mount('#app')

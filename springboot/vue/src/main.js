import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import store from './store'
import axios from "axios"

import VueResource from 'vue-resource'
import request from "./utils/request";

Vue.config.productionTip = false

Vue.use(ElementUI, { size: "mini" });

Vue.use(VueResource);

Vue.prototype.request=request


new Vue({
  router,
  store,
  axios,
  render: h => h(App)
}).$mount('#app')


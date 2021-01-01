// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";

import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

Vue.use(ElementUI);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  components: { App },
  template: "<App/>"
});

// http request 拦截器
axios.interceptors.request.use(
  config => {
    if (store.state.token) {
      // 判断是否有token，若存在，每个http header加上token
      config.headers.Authorization = store.state.token;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    console.log(error.response);
    if (error) {
      // 清除token 如果不是register/login, 跳转至login
      // store.commit("logout");
      // router.currentRoute.path !== "/login" &&
      //   router.currentRoute.path !== "/register" &&
      //   router.replace({
      //     path: "/login",
      //     query: { redirect: router.currentRoute.path }
      //   });
    }
    return Promise.reject(error.response.data);
  }
);

Vue.directive("title", {
  inserted: function(el, binding) {
    document.title = el.dataset.title;
  }
});

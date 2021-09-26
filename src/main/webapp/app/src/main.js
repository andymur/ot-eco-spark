import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import vSelect from "vue-select";
import PureVueChart from "pure-vue-chart";

Vue.config.productionTip = false;
Vue.component("v-select", vSelect);
Vue.component("pure-vue-chart", PureVueChart);

vSelect.props.components.default = () => ({
  Deselect: {
    render: createElement => createElement("span", "❌"),
  },
  OpenIndicator: {
    render: createElement => createElement("span", "🔽"),
  },
});

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");

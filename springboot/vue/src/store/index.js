import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

const index = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        }
    }
})


export default index
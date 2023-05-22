import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

const state = {
    carts: [],
    type: 0
}

const mutations = {
    setCarts (state, carts) {
        state.carts = carts
    },
    setType (state, type) {
        state.type = type
    }
}


const store = new Vuex.Store({
    state,
    mutations
})


    // const store = new Vuex.Store({
    //     state: {
    //         currentPathName: ''
    //     },
    //     mutations: {
    //         setPath (state) {
    //             state.currentPathName = localStorage.getItem("currentPathName")
    //         },
    //         logout() {
    //             // 清空缓存
    //             localStorage.removeItem("user")
    //             localStorage.removeItem("menus")
    //             router.push("/login")
    //
    //             // 重置路由
    //             resetRouter()
    //         }
    //     }
    // })

export default store

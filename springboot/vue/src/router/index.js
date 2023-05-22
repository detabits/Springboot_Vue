import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/1',
    name: '1',
    component: () => import('../views/front/1.vue')
  },

  {
    path: '/front',
    name: 'Front',
    component: () => import("../layout/front"),
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import("../views/front/home")
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import("../views/front/search")
      },
      {
        path: 'goods',
        name: 'Goods',
        component: () => import("../views/front/goods")
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import("../views/front/cart")
      },
      {
        path: 'preOrder',
        name: 'PreOrder',
        component: () => import("../views/front/preOrder")
      },
      {
        path: 'order',
        name: 'FrontOrder',
        component: () => import("../views/front/order")
      },
      {
        path: 'collect',
        name: 'Collect',
        component: () => import("../views/front/collect")
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("../views/front/person")
      },
      {
        path: 'address',
        name: 'Address',
        component: () => import("../views/front/address")
      },
    ]
  },


  {
    path: '/about',
    name: 'About',
    component: () => import('../views/manage/About.vue')
  },






  {
    path: '/about2',
    name: 'About2',
    component: () => import('../views/About2.vue')
  },
  {
    path: '/about3',
    name: 'About3',
    component: () => import('../views/About3.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus) {

    const currentRouteNames = router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')){
      //拼装动态路由
      const manageRoute = {path: '/',name:'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
          //{ path: 'password', name: '修改密码', component: () => import('../views/Password.vue')}
        ]}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {

        if(item.path) {
          let itemMenu = {
            path: item.path.replace("/", ""),
            name: item.name,
            component: () => import('../views/' + item.pagePath + '.vue')
          }
          manageRoute.children.push(itemMenu)
        }
        else if(item.children.length){

          item.children.forEach(item => {
            if(item.path) {
              let itemMenu = {
                path: item.path.replace("/", ""),
                name: item.name,
                component: () => import('../views/' + item.pagePath + '.vue')
              }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      //动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

setRoutes()

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新

// 未找到路由的情况
if (!to.matched.length) {
  const storeMenus = localStorage.getItem("menus")
  if (storeMenus) {
    next("/404")
  } else {
    // 跳回登录页面
    next("/login")
  }
}
// 其他的情况都放行
next()

})

export default router
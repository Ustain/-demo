import { createApp } from 'vue'
import { ref } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'
import '@/assets/global.css'
// 全局icon导入
import * as ELIcons from '@element-plus/icons-vue'

const app = createApp(App)

for (let iconName in ELIcons) {
    app.component(iconName, ELIcons[iconName])
}
app.use(store)
app.use(router)
app.use(ElementPlus, {locale: zhCn,size:"small"})
app.use(ElementPlus)

app.mount('#app')



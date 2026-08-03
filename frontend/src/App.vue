<template>
  <el-config-provider :locale="zhCn">
    <MusicPlayer v-if="!route.path.startsWith('/admin')" />
    <PolicyModal />
    <router-view />
  </el-config-provider>
</template>

<script setup>
import { defineAsyncComponent } from 'vue'
import { useRoute } from 'vue-router'
import PolicyModal from './components/PolicyModal.vue'
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const route = useRoute()
const MusicPlayer = defineAsyncComponent(() => import('./components/MusicPlayer.vue'))
</script>

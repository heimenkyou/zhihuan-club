<template>
  <el-config-provider :locale="zhCn">
    <MusicPlayer />
    <PolicyModal />
    <router-view />
  </el-config-provider>
</template>

<script setup>
import PolicyModal from './components/PolicyModal.vue'
import MusicPlayer from './components/MusicPlayer.vue'
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
</script>

<script setup lang="ts">
// 导入协议弹窗组件
import PolicyModal from './components/PolicyModal.vue';
</script>

<template>
  <!-- 协议弹窗组件，会在所有页面显示 -->
  <PolicyModal />
  
  <!-- 路由视图 -->
  <router-view />
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>

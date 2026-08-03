<template>
  <div class="admin-results" :class="{ 'admin-results--natural': !scroll }"><slot /></div>
</template>

<script setup>
defineProps({ scroll: { type: Boolean, default: true } });
</script>

<style scoped>
.admin-results { min-width: 0; overflow-x: auto; overflow-y: hidden; -webkit-overflow-scrolling: touch; }
.admin-results :deep(.el-table) { min-width: 720px; }
.admin-results--natural :deep(.el-table) { min-width: 0; }
</style>

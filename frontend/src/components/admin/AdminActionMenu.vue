<template>
  <el-dropdown trigger="click" @click.stop>
    <el-button class="action-trigger" text circle aria-label="更多操作" @click.stop>
      <el-icon><MoreFilled /></el-icon>
    </el-button>
    <template #dropdown><el-dropdown-menu><slot /></el-dropdown-menu></template>
  </el-dropdown>
</template>

<script setup>
import { MoreFilled } from "@element-plus/icons-vue";
</script>

<style scoped>
.action-trigger { width: 28px; height: 28px; color: #475467; }
</style>

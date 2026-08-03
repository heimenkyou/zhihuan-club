<template>
  <div class="admin-toolbar"><slot /></div>
</template>

<style scoped>
.admin-toolbar { display: flex; flex-wrap: wrap; align-items: center; gap: 8px; margin-bottom: 14px; }
.admin-toolbar :deep(.el-input), .admin-toolbar :deep(.el-select) { width: 220px; }
.admin-toolbar :deep(.el-button) { margin: 0; }
@media (max-width: 768px) { .admin-toolbar { display: grid; grid-template-columns: 1fr; } .admin-toolbar :deep(.el-input), .admin-toolbar :deep(.el-select), .admin-toolbar :deep(.el-button) { width: 100%; } }
</style>

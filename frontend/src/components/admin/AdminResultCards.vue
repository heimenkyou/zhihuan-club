<template>
  <div class="admin-result-cards"><slot /></div>
</template>

<style scoped>
.admin-result-cards { display: grid; gap: 8px; }
.admin-result-cards :slotted(.admin-result-card) { display: flex; align-items: flex-start; justify-content: space-between; gap: 12px; min-width: 0; padding: 12px; background: #fff; border: 1px solid #eaecf0; border-radius: 8px; }
.admin-result-cards :slotted(.admin-result-card) > :first-child { display: grid; min-width: 0; gap: 4px; }
.admin-result-cards :slotted(.admin-result-card) span { overflow: hidden; color: #667085; font-size: 13px; text-overflow: ellipsis; white-space: nowrap; }
</style>

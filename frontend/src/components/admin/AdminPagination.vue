<template>
  <div v-if="total" class="admin-pagination">
    <span class="total-count">共 {{ total }} 条</span>
    <el-pagination v-model:current-page="currentPage" :page-size="pageSize" layout="prev, pager, next" :total="total" small @current-change="$emit('change', $event)" />
  </div>
</template>

<script setup>
const currentPage = defineModel("currentPage", {
	type: Number,
	required: true,
});
defineProps({
	pageSize: { type: Number, required: true },
	total: { type: Number, required: true },
});
defineEmits(["change"]);
</script>

<style scoped>
.admin-pagination { display: flex; align-items: center; justify-content: flex-end; gap: 8px; margin-top: 12px; }
.total-count { color: #667085; font-size: 13px; }
@media (max-width: 768px) { .admin-pagination { justify-content: center; } }
</style>

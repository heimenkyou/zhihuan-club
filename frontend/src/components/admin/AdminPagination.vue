<template>
  <div v-if="total" class="admin-pagination">
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
.admin-pagination { display: flex; justify-content: flex-end; margin-top: 12px; }
@media (max-width: 768px) { .admin-pagination { justify-content: center; } }
</style>

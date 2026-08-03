<template>
  <AdminPage title="留言管理">
    <AdminToolbar><el-input v-model="keyword" placeholder="搜索昵称或留言内容" clearable @keyup.enter="search" /><el-button type="primary" @click="search">搜索</el-button><el-button @click="reset">重置</el-button><el-button v-if="selected.length" type="danger" @click="batchRemove">批量删除</el-button></AdminToolbar>
    <AdminTable v-if="!isMobile"><el-table v-loading="loading" :data="page.records" stripe @selection-change="selected = $event"><el-table-column type="selection" width="52" /><el-table-column prop="id" label="ID" width="80" /><el-table-column prop="nickname" label="昵称" width="120" /><el-table-column prop="content" label="留言内容" min-width="360" show-overflow-tooltip /><el-table-column prop="likeCount" label="点赞" width="80" /><el-table-column prop="createTime" label="时间" width="180" /><el-table-column label="操作" width="64" fixed="right"><template #default="{ row }"><AdminActionMenu><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></template></el-table-column></el-table></AdminTable>
    <AdminResultCards v-else v-loading="loading"><article v-for="row in page.records" :key="row.id" class="admin-result-card message-card"><div><strong>{{ row.nickname }}</strong><p>{{ row.content }}</p><span>{{ row.createTime }}</span></div><AdminActionMenu><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></article></AdminResultCards>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />
  </AdminPage>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import { deleteAdminMessage, getAdminMessages } from "@/services/adminService";

const { isMobile } = useAdminMobile();
const keyword = ref("");
const loading = ref(false);
const selected = ref([]);
const page = ref({ current: 1, size: 10, total: 0, records: [] });
const load = async () => {
	loading.value = true;
	try {
		page.value = await getAdminMessages({
			current: page.value.current,
			size: page.value.size,
			keyword: keyword.value,
		});
	} catch (error) {
		ElMessage.error("获取留言信息失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const search = () => {
	page.value.current = 1;
	load();
};
const reset = () => {
	keyword.value = "";
	search();
};
const remove = async (id) => {
	try {
		await ElMessageBox.confirm("确定要删除这条留言吗？", "确认删除", {
			type: "warning",
		});
		await deleteAdminMessage(id);
		ElMessage.success("删除留言成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("删除留言失败");
			console.error(error);
		}
	}
};
const batchRemove = async () => {
	try {
		await ElMessageBox.confirm("确定要删除选中的留言吗？", "确认删除", {
			type: "warning",
		});
		await Promise.all(selected.value.map((row) => deleteAdminMessage(row.id)));
		ElMessage.success("批量删除成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("批量删除失败");
			console.error(error);
		}
	}
};
onMounted(load);
</script>

<style scoped>
.message-card p { display: -webkit-box; margin: 5px 0; overflow: hidden; color: #475467; font-size: 13px; line-height: 20px; -webkit-box-orient: vertical; -webkit-line-clamp: 2; }:deep(.danger-item) { color: var(--el-color-danger); }
</style>

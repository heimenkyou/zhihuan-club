<template>
  <AdminPage title="项目管理"><template #action><el-button type="primary" @click="router.push('/admin/projects/edit')">添加项目</el-button></template>
    <AdminToolbar><el-input v-model="keyword" placeholder="搜索项目标题" clearable @keyup.enter="search" /><el-button type="primary" @click="search">搜索</el-button><el-button @click="reset">重置</el-button></AdminToolbar>
    <AdminTable v-if="!isMobile" :scroll="false"><el-table v-loading="loading" :data="projects"><el-table-column prop="id" label="ID" width="80" /><el-table-column prop="title" label="项目标题" min-width="280" show-overflow-tooltip /><el-table-column prop="category" label="分类" width="130" /><el-table-column label="技术栈" min-width="220"><template #default="{ row }"><el-tag v-for="tag in row.techStackTags?.slice(0, 3)" :key="tag" size="small" class="tag">{{ tag }}</el-tag></template></el-table-column><el-table-column label="操作" width="64"><template #default="{ row }"><AdminActionMenu><el-dropdown-item :disabled="!row.id" @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item :disabled="!row.id" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></template></el-table-column></el-table></AdminTable>
    <AdminResultCards v-else v-loading="loading"><article v-for="row in projects" :key="row.id" class="admin-result-card"><div><strong>{{ row.title }}</strong><span>{{ row.category }}</span><span>{{ row.techStackTags?.join('、') || '未填写技术栈' }}</span></div><AdminActionMenu><el-dropdown-item :disabled="!row.id" @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item :disabled="!row.id" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></article></AdminResultCards>
    <AdminPagination v-model:current-page="current" :page-size="size" :total="total" @change="load" />
  </AdminPage>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import { deleteProject, getAdminProjects } from "@/services/projectService";

const router = useRouter();
const { isMobile } = useAdminMobile();
const keyword = ref("");
const loading = ref(false);
const projects = ref([]);
const current = ref(1);
const size = ref(10);
const total = ref(0);
const load = async () => {
	loading.value = true;
	try {
		const data = (
			await getAdminProjects({
				current: current.value,
				size: size.value,
				keyword: keyword.value,
			})
		)?.data;
		projects.value = data?.records ?? [];
		current.value = Number(data?.current ?? current.value);
		total.value = Number(data?.total ?? 0);
	} catch (error) {
		ElMessage.error("获取项目信息失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const search = () => {
	current.value = 1;
	load();
};
const reset = () => {
	keyword.value = "";
	search();
};
const edit = (row) => router.push(`/admin/projects/edit/${row.id}`);
const remove = async (id) => {
	if (!id) return;
	try {
		await ElMessageBox.confirm("确定要删除这个项目吗？", "确认删除", {
			type: "warning",
		});
		await deleteProject(id);
		ElMessage.success("删除项目成功");
		if (projects.value.length === 1 && current.value > 1) current.value--;
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("删除项目失败");
			console.error(error);
		}
	}
};
onMounted(load);
</script>

<style scoped>
.tag { margin-right: 4px; }:deep(.danger-item) { color: var(--el-color-danger); }
</style>

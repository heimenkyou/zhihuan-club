<template>
  <AdminPage title="报名管理">
    <template #action><el-button type="primary" @click="exportApplications">导出数据</el-button></template>
    <AdminToolbar><el-input v-model="search.name" placeholder="姓名" clearable @keyup.enter="searchList" /><el-input v-model="search.studentId" placeholder="学号" clearable @keyup.enter="searchList" /><el-input v-model="search.QQNumber" placeholder="QQ号" clearable @keyup.enter="searchList" /><el-select v-model="search.majors" multiple collapse-tags placeholder="专业" :loading="majorLoading" @change="searchList"><el-option v-for="major in majors" :key="major" :label="major" :value="major" /></el-select><el-select v-model="search.department" placeholder="第一志愿" clearable @change="searchList"><el-option v-for="item in departments" :key="item" :label="item" :value="item" /></el-select><el-select v-model="search.secondDepartment" placeholder="第二志愿" clearable @change="searchList"><el-option v-for="item in secondDepartments" :key="item" :label="item" :value="item" /></el-select><el-select v-model="search.matchAllDepartments" placeholder="部门匹配" @change="searchList"><el-option label="匹配任一志愿" :value="false" /><el-option label="同时匹配两个志愿" :value="true" /></el-select><el-button type="primary" @click="searchList">搜索</el-button><el-button @click="reset">重置</el-button><el-button v-if="selected.length" type="danger" @click="batchRemove">批量删除</el-button></AdminToolbar>
    <AdminTable v-if="!isMobile"><el-table v-loading="loading" :data="page.records" stripe @selection-change="selected = $event" @row-click="handleRowClick"><el-table-column type="selection" width="52" /><el-table-column prop="name" label="姓名" width="100" /><el-table-column prop="studentId" label="学号" width="130" /><el-table-column prop="major" label="专业" min-width="160" /><el-table-column prop="phone" label="电话" width="140" /><el-table-column prop="QQNumber" label="QQ号" width="130" /><el-table-column prop="department" label="第一志愿" width="120" /><el-table-column prop="secondDepartment" label="第二志愿" width="120" /><el-table-column label="操作" width="64" fixed="right"><template #default="{ row }"><AdminActionMenu><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></template></el-table-column></el-table></AdminTable>
    <AdminResultCards v-else v-loading="loading"><article v-for="row in page.records" :key="row.id" class="admin-result-card application-card" @click="view(row)"><div><strong>{{ row.name }}</strong><span>{{ row.studentId }} · {{ row.major }}</span><span>{{ row.phone }} · {{ row.department }}</span></div><AdminActionMenu><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></article></AdminResultCards>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />
  </AdminPage>
  <el-dialog v-model="visible" title="报名详情" width="600px" class="admin-dialog"><el-descriptions :column="1" border><el-descriptions-item label="姓名">{{ current?.name }}</el-descriptions-item><el-descriptions-item label="学号">{{ current?.studentId }}</el-descriptions-item><el-descriptions-item label="专业">{{ current?.major }}</el-descriptions-item><el-descriptions-item label="电话">{{ current?.phone }}</el-descriptions-item><el-descriptions-item label="QQ号">{{ current?.QQNumber }}</el-descriptions-item><el-descriptions-item label="第一志愿">{{ current?.department }}</el-descriptions-item><el-descriptions-item label="第二志愿">{{ current?.secondDepartment || '无' }}</el-descriptions-item><el-descriptions-item label="兴趣方向">{{ current?.interests?.join('、') || '无' }}</el-descriptions-item><el-descriptions-item label="申请理由"><span class="multiline">{{ current?.reason }}</span></el-descriptions-item><el-descriptions-item label="个人介绍"><span class="multiline">{{ current?.introduction }}</span></el-descriptions-item></el-descriptions><template #footer><el-button @click="visible = false">关闭</el-button></template></el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as XLSX from "xlsx";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	deleteApplication,
	getApplicationMajors,
	getApplications,
} from "@/services/adminService";

const { isMobile } = useAdminMobile();
const departments = ["算法部", "项目竞赛部", "综合管理部"];
const secondDepartments = [...departments, "无"];
const search = reactive({
	name: "",
	studentId: "",
	department: "",
	secondDepartment: "",
	majors: [],
	QQNumber: "",
	matchAllDepartments: false,
});
const page = reactive({ current: 1, size: 10, total: 0, records: [] });
const majors = ref([]);
const majorLoading = ref(false);
const loading = ref(false);
const selected = ref([]);
const current = ref();
const visible = ref(false);
const load = async () => {
	loading.value = true;
	try {
		const data = await getApplications({
			current: page.current,
			size: page.size,
			...search,
			majors: search.majors.length ? search.majors : undefined,
		});
		Object.assign(page, data);
	} catch (error) {
		ElMessage.error("获取报名信息失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const searchList = () => {
	page.current = 1;
	load();
};
const reset = () => {
	Object.assign(search, {
		name: "",
		studentId: "",
		department: "",
		secondDepartment: "",
		majors: [],
		QQNumber: "",
		matchAllDepartments: false,
	});
	searchList();
};
const view = (row) => {
	current.value = row;
	visible.value = true;
};
const handleRowClick = (row, _, event) => {
	if (!event.target.closest(".el-checkbox, .el-dropdown")) view(row);
};
const remove = async (id) => {
	try {
		await ElMessageBox.confirm("确定要删除这条报名信息吗？", "确认删除", {
			type: "warning",
		});
		await deleteApplication(id);
		ElMessage.success("删除报名信息成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("删除报名信息失败");
			console.error(error);
		}
	}
};
const batchRemove = async () => {
	try {
		await ElMessageBox.confirm("确定要删除选中的报名信息吗？", "确认删除", {
			type: "warning",
		});
		await Promise.all(selected.value.map((row) => deleteApplication(row.id)));
		ElMessage.success("批量删除成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("批量删除失败");
			console.error(error);
		}
	}
};
const exportApplications = async () => {
	try {
		loading.value = true;
		const data = await getApplications({
			current: 1,
			size: 1000,
			...search,
			majors: search.majors.length ? search.majors : undefined,
		});
		const rows = data.records.map((item) => ({
			姓名: item.name,
			学号: item.studentId,
			专业: item.major,
			电话: item.phone,
			QQ号: item.QQNumber,
			部门: item.department,
			第二志愿部门: item.secondDepartment || "无",
			兴趣方向: item.interests?.join("、") || "无",
			申请理由: item.reason,
			个人介绍: item.introduction,
		}));
		const workbook = XLSX.utils.book_new();
		XLSX.utils.book_append_sheet(
			workbook,
			XLSX.utils.json_to_sheet(rows),
			"报名信息",
		);
		XLSX.writeFile(
			workbook,
			`报名信息_${new Date().toLocaleDateString()}.xlsx`,
		);
	} catch (error) {
		ElMessage.error("导出失败，请重试");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
onMounted(async () => {
	majorLoading.value = true;
	try {
		majors.value = await getApplicationMajors();
	} catch (error) {
		ElMessage.error("获取专业选项失败");
		console.error(error);
	} finally {
		majorLoading.value = false;
	}
	load();
});
</script>

<style scoped>
.application-card { cursor: pointer; }.multiline { white-space: pre-wrap; word-break: break-word; }:deep(.danger-item) { color: var(--el-color-danger); } @media (max-width: 768px) { :deep(.admin-dialog) { width: calc(100% - 24px) !important; margin: 12px auto; } }
</style>

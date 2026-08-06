<template>
  <div class="application-picker">
    <AdminToolbar>
      <el-select v-if="years.length" v-model="search.applicationYear" placeholder="招新年份" clearable @change="searchList">
        <el-option v-for="year in years" :key="year" :label="`${year} 年`" :value="year" />
      </el-select>
      <el-input v-model="search.name" placeholder="姓名" clearable @keyup.enter="searchList" />
      <el-input v-model="search.studentId" placeholder="学号" clearable :disabled="!!presetStudentId" @keyup.enter="searchList" />
      <el-input v-model="search.QQNumber" placeholder="QQ号" clearable @keyup.enter="searchList" />
      <el-select v-model="search.majors" multiple collapse-tags placeholder="专业" :loading="majorLoading" @change="searchList">
        <el-option v-for="major in majors" :key="major" :label="major" :value="major" />
      </el-select>
      <el-select v-model="search.department" placeholder="第一志愿" clearable @change="searchList">
        <el-option v-for="item in departments" :key="item" :label="item" :value="item" />
      </el-select>
      <el-select v-model="search.secondDepartment" placeholder="第二志愿" clearable @change="searchList">
        <el-option v-for="item in secondDepartments" :key="item" :label="item" :value="item" />
      </el-select>
      <el-select v-model="search.matchAllDepartments" placeholder="部门匹配" @change="searchList">
        <el-option label="匹配任一志愿" :value="false" />
        <el-option label="同时匹配两个志愿" :value="true" />
      </el-select>
      <el-button type="primary" @click="searchList">搜索</el-button>
      <el-button @click="reset">重置</el-button>
      <template v-if="exportable">
        <el-button type="primary" @click="exportApplications">导出数据</el-button>
      </template>
      <el-button v-if="removable && selected.length" type="danger" @click="batchRemove">批量删除</el-button>
    </AdminToolbar>
    <AdminTable v-if="!isMobile">
      <el-table v-loading="loading" :data="page.records" stripe @selection-change="selected = $event" @row-click="handleRowClick">
        <el-table-column v-if="removable" type="selection" width="52" />
        <el-table-column prop="applicationYear" label="年份" width="80" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="major" label="专业" min-width="160" />
        <el-table-column prop="phone" label="电话" width="140" />
        <el-table-column prop="QQNumber" label="QQ号" width="130" />
        <el-table-column prop="department" label="第一志愿" width="120" />
        <el-table-column prop="secondDepartment" label="第二志愿" width="120" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <AdminActionMenu v-if="!selectable">
              <el-dropdown-item @click="view(row)">详情</el-dropdown-item>
              <el-dropdown-item v-if="removable" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item>
            </AdminActionMenu>
            <el-button v-else size="small" type="primary" @click.stop="emitSelect(row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
    </AdminTable>
    <AdminResultCards v-else v-loading="loading">
      <article v-for="row in page.records" :key="row.id" class="admin-result-card application-card" @click="view(row)">
        <div>
          <strong>{{ row.name }}</strong>
          <span>{{ row.applicationYear }} 年 · {{ row.studentId }} · {{ row.major }}</span>
          <span>{{ row.phone }} · {{ row.department }}</span>
        </div>
        <AdminActionMenu v-if="!selectable">
          <el-dropdown-item v-if="removable" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item>
        </AdminActionMenu>
        <el-button v-else size="small" type="primary" @click.stop="emitSelect(row)">选择</el-button>
      </article>
    </AdminResultCards>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />

    <el-dialog v-model="visible" title="报名详情" :width="isMobile ? 'calc(100% - 24px)' : '600px'" class="admin-dialog">
      <el-descriptions :column="1" border class="detail-descriptions">
        <el-descriptions-item label="招新年份">{{ current?.applicationYear || '—' }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ current?.name }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ current?.studentId }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ current?.className || '—' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ current?.major }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ current?.phone }}</el-descriptions-item>
        <el-descriptions-item label="QQ号">{{ current?.QQNumber }}</el-descriptions-item>
        <el-descriptions-item label="第一志愿">{{ current?.department }}</el-descriptions-item>
        <el-descriptions-item label="第二志愿">{{ current?.secondDepartment || '无' }}</el-descriptions-item>
        <el-descriptions-item label="第一阶段最想尝试什么">{{ current?.initialDirections?.join('、') || '无' }}</el-descriptions-item>
        <el-descriptions-item label="个人介绍"><span class="multiline">{{ current?.introduction }}</span></el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="visible = false">关闭</el-button>
        <el-button v-if="selectable" type="primary" @click="emitSelect(current)">选择这条记录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { onMounted, reactive, ref, watch } from "vue";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	deleteApplication,
	getApplicationMajors,
	getApplications,
	getApplicationYears,
} from "@/services/adminService";

const props = defineProps({
	/** 是否允许选择记录并向外抛出 select 事件 */
	selectable: { type: Boolean, default: false },
	/** 是否显示删除与批量删除操作 */
	removable: { type: Boolean, default: false },
	/** 是否显示导出按钮 */
	exportable: { type: Boolean, default: false },
	/** 预填学号（只读），用于成员详情查看该成员的报名记录 */
	presetStudentId: { type: String, default: "" },
	/** 初始招新年份，仅作默认值，重置时恢复；不传则年份保持未选择 */
	initialYear: { type: Number, default: null },
});
const emit = defineEmits(["select"]);

const { isMobile } = useAdminMobile();
const departments = ["算法部", "项目竞赛部", "综合管理部"];
const secondDepartments = [...departments, "无"];
const search = reactive({
	name: "",
	studentId: props.presetStudentId,
	department: "",
	secondDepartment: "",
	majors: [],
	QQNumber: "",
	matchAllDepartments: false,
	applicationYear: props.initialYear,
});
const page = reactive({ current: 1, size: 10, total: 0, records: [] });
const majors = ref([]);
const years = ref([]);
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
		studentId: props.presetStudentId,
		department: "",
		secondDepartment: "",
		majors: [],
		QQNumber: "",
		matchAllDepartments: false,
		applicationYear: props.initialYear,
	});
	searchList();
};
// 成员详情打开后学号才就绪，变化时同步查询条件并重新加载
watch(
	() => props.presetStudentId,
	(studentId) => {
		search.studentId = studentId || "";
		page.current = 1;
		load();
	},
);
const view = (row) => {
	current.value = row;
	visible.value = true;
};
const handleRowClick = (row, _, event) => {
	if (!event.target.closest(".el-checkbox, .el-dropdown, .el-button"))
		view(row);
};
const emitSelect = (row) => {
	emit("select", row);
};
const remove = async (id) => {
	try {
		await ElMessageBox.confirm("确定要删除这条报名信息吗？", "确认删除", {
			type: "warning",
		});
		await deleteApplication(id);
		ElMessage.success("删除报名信息成功");
		if (page.records.length === 1 && page.current > 1) page.current--;
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
		if (page.records.length <= selected.value.length && page.current > 1)
			page.current--;
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
		const XLSX = await import("xlsx");
		const data = await getApplications({
			current: 1,
			size: 1000,
			...search,
			majors: search.majors.length ? search.majors : undefined,
		});
		const rows = data.records.map((item) => ({
			招新年份: item.applicationYear,
			姓名: item.name,
			学号: item.studentId,
			班级: item.className,
			专业: item.major,
			电话: item.phone,
			QQ号: item.QQNumber,
			部门: item.department,
			第二志愿部门: item.secondDepartment || "无",
			第一阶段最想尝试什么: item.initialDirections?.join("、") || "无",
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
	try {
		majorLoading.value = true;
		majors.value = await getApplicationMajors();
		years.value = await getApplicationYears();
	} catch (error) {
		ElMessage.error("获取筛选选项失败");
		console.error(error);
	} finally {
		majorLoading.value = false;
	}
	load();
});
</script>

<style scoped>
.application-card { cursor: pointer; }
.multiline { white-space: pre-wrap; word-break: break-word; }
:deep(.danger-item) { color: var(--el-color-danger); }
/* 固定表格列宽，保证窄屏下长内容可折行 */
.detail-descriptions :deep(.el-descriptions__table) {
	table-layout: fixed;
	width: 100%;
}
.detail-descriptions :deep(.el-descriptions__content) {
	word-break: break-word;
	overflow-wrap: anywhere;
}
/* 弹窗 footer 允许换行，避免窄屏横向溢出 */
:deep(.el-dialog__footer) {
	display: flex;
	flex-wrap: wrap;
	justify-content: flex-end;
	gap: 8px;
}
:deep(.el-dialog__footer .el-button + .el-button) {
	margin-left: 0;
}
</style>

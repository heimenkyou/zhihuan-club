<template>
  <AdminPage title="奖项管理"><template #action><el-button type="primary" @click="openAdd">添加奖项</el-button></template>
    <AdminToolbar><el-input v-model="keyword" placeholder="搜索奖项名称或获奖人" clearable @keyup.enter="applyFilters" /><el-select v-model="filter.competitionLevel" placeholder="竞赛级别" clearable><el-option v-for="item in levels" :key="item" :label="item" :value="item" /></el-select><el-select v-model="filter.awardLevel" placeholder="获奖等级" clearable><el-option v-for="item in awardLevels" :key="item" :label="item" :value="item" /></el-select><el-select v-model="filter.year" placeholder="年份" clearable><el-option v-for="item in years" :key="item" :label="`${item}年`" :value="String(item)" /></el-select><el-select v-model="filter.competitionName" placeholder="竞赛项目" clearable><el-option v-for="item in competitionNames" :key="item" :label="item" :value="item" /></el-select><el-select v-if="tracks.length" v-model="filter.competitionTrack" placeholder="赛道" clearable><el-option v-for="item in tracks" :key="item" :label="item" :value="item" /></el-select><el-select v-model="sortBy" placeholder="排序"><el-option label="竞赛级别" value="competitionLevel" /><el-option label="获奖日期" value="awardDate" /><el-option label="竞赛名称" value="competitionName" /></el-select><el-select v-model="sortOrder" placeholder="顺序"><el-option label="降序" value="desc" /><el-option label="升序" value="asc" /></el-select><el-button type="primary" @click="applyFilters">搜索</el-button><el-button @click="reset">重置</el-button></AdminToolbar>
    <AdminTable v-if="!isMobile"><el-table v-loading="loading" :data="paged"><el-table-column prop="id" label="ID" width="80" /><el-table-column prop="competitionName" label="奖项名称" min-width="220" /><el-table-column prop="competitionLevel" label="级别" width="110" /><el-table-column prop="competitionTrack" label="赛道" width="130" /><el-table-column prop="awardLevel" label="等级" width="110" /><el-table-column label="获奖人员" min-width="140"><template #default="{ row }">{{ row.winners?.join('、') }}</template></el-table-column><el-table-column prop="year" label="年份" width="90" /><el-table-column label="操作" width="64" fixed="right"><template #default="{ row }"><AdminActionMenu><el-dropdown-item @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></template></el-table-column></el-table></AdminTable>
    <AdminResultCards v-else v-loading="loading"><article v-for="row in paged" :key="row.id" class="admin-result-card"><div><strong>{{ row.competitionName }}</strong><span>{{ row.winners?.join('、') || '未填写获奖人' }}</span><span>{{ row.competitionLevel }} · {{ row.awardLevel }} · {{ row.year }}年</span></div><AdminActionMenu><el-dropdown-item @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></article></AdminResultCards>
    <AdminPagination v-model:current-page="current" :page-size="size" :total="total" />
  </AdminPage>
  <el-dialog v-model="visible" :title="dialogTitle" width="560px" class="admin-dialog"><el-form ref="formRef" :model="form" :rules="rules" label-position="top"><el-form-item label="奖项名称" prop="competitionName"><el-input v-model="form.competitionName" /></el-form-item><el-form-item label="赛道"><el-input v-model="form.competitionTrack" /></el-form-item><el-form-item label="竞赛级别" prop="competitionLevel"><el-select v-model="form.competitionLevel"><el-option v-for="item in levels" :key="item" :label="item" :value="item" /></el-select></el-form-item><el-form-item label="获奖等级" prop="awardLevel"><el-input v-model="form.awardLevel" /></el-form-item><el-form-item label="获奖人员" prop="winners"><el-input v-model="winnerInput" placeholder="用逗号分隔" /></el-form-item><el-form-item label="获奖日期" prop="awardDate"><el-date-picker v-model="form.awardDate" type="month" value-format="YYYY-MM" /></el-form-item></el-form><template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template></el-dialog>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	createAward,
	deleteAward,
	getAdminAwards,
	updateAward,
} from "@/services/adminService";

const { isMobile } = useAdminMobile();
const levels = ["国家级", "省级", "校级"];
const awardLevels = [
	"一等奖",
	"金牌",
	"二等奖",
	"银牌",
	"三等奖",
	"铜牌",
	"优秀奖",
];
const years = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i);
const keyword = ref("");
const filter = reactive({
	competitionLevel: "",
	awardLevel: "",
	year: "",
	competitionName: "",
	competitionTrack: "",
});
const sortBy = ref("competitionLevel");
const sortOrder = ref("desc");
const awards = ref([]);
const loading = ref(false);
const current = ref(1);
const size = ref(10);
const visible = ref(false);
const editingId = ref(null);
const dialogTitle = ref("添加奖项");
const formRef = ref();
const winnerInput = ref("");
const form = reactive({
	competitionName: "",
	competitionLevel: "国家级",
	competitionTrack: "",
	awardLevel: "一等奖",
	winners: [],
	year: new Date().getFullYear(),
	awardDate: new Date().toISOString().slice(0, 7),
});
const rules = {
	competitionName: [
		{ required: true, message: "请输入奖项名称", trigger: "blur" },
	],
	competitionLevel: [
		{ required: true, message: "请选择竞赛级别", trigger: "change" },
	],
	awardLevel: [{ required: true, message: "请输入获奖等级", trigger: "blur" }],
	winners: [
		{
			validator: (_, value, callback) =>
				value.length ? callback() : callback(new Error("请输入获奖人员")),
			trigger: "blur",
		},
	],
	awardDate: [{ required: true, message: "请选择获奖日期", trigger: "change" }],
};
const competitionNames = computed(() =>
	[
		...new Set(
			awards.value.map((item) => item.competitionName).filter(Boolean),
		),
	].sort(),
);
const tracks = computed(() =>
	filter.competitionName
		? [
				...new Set(
					awards.value
						.filter((item) => item.competitionName === filter.competitionName)
						.map((item) => item.competitionTrack)
						.filter(Boolean),
				),
			]
		: [],
);
const filtered = computed(() =>
	awards.value.filter(
		(item) =>
			(!keyword.value ||
				item.competitionName?.includes(keyword.value) ||
				item.winners?.some((name) => name.includes(keyword.value)) ||
				item.awardLevel?.includes(keyword.value)) &&
			(!filter.competitionLevel ||
				item.competitionLevel === filter.competitionLevel) &&
			(!filter.awardLevel ||
				item.awardLevel === filter.awardLevel ||
				(filter.awardLevel === "一等奖" && item.awardLevel === "金牌") ||
				(filter.awardLevel === "二等奖" && item.awardLevel === "银牌") ||
				(filter.awardLevel === "三等奖" && item.awardLevel === "铜牌")) &&
			(!filter.year || String(item.year) === filter.year) &&
			(!filter.competitionName ||
				item.competitionName?.includes(filter.competitionName)) &&
			(!filter.competitionTrack ||
				item.competitionTrack === filter.competitionTrack),
	),
);
const total = computed(() => filtered.value.length);
const paged = computed(() =>
	[...filtered.value]
		.sort((a, b) => {
			const av =
				sortBy.value === "awardDate"
					? a.awardDate
					: sortBy.value === "competitionName"
						? a.competitionName
						: levels.indexOf(a.competitionLevel);
			const bv =
				sortBy.value === "awardDate"
					? b.awardDate
					: sortBy.value === "competitionName"
						? b.competitionName
						: levels.indexOf(b.competitionLevel);
			return sortOrder.value === "desc"
				? String(bv).localeCompare(String(av))
				: String(av).localeCompare(String(bv));
		})
		.slice((current.value - 1) * size.value, current.value * size.value),
);
const load = async () => {
	loading.value = true;
	try {
		const data = await getAdminAwards({ keyword: keyword.value });
		awards.value = Array.isArray(data) ? data : [];
	} catch (error) {
		ElMessage.error("获取奖项信息失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const applyFilters = () => {
	current.value = 1;
};
const reset = () => {
	keyword.value = "";
	Object.assign(filter, {
		competitionLevel: "",
		awardLevel: "",
		year: "",
		competitionName: "",
		competitionTrack: "",
	});
	sortBy.value = "competitionLevel";
	sortOrder.value = "desc";
	applyFilters();
};
const openAdd = () => {
	editingId.value = null;
	dialogTitle.value = "添加奖项";
	Object.assign(form, {
		competitionName: "",
		competitionLevel: "国家级",
		competitionTrack: "",
		awardLevel: "一等奖",
		winners: [],
		year: new Date().getFullYear(),
		awardDate: new Date().toISOString().slice(0, 7),
	});
	winnerInput.value = "";
	visible.value = true;
};
const edit = (row) => {
	editingId.value = row.id;
	dialogTitle.value = "编辑奖项";
	Object.assign(form, row);
	winnerInput.value = (row.winners || []).join("、");
	visible.value = true;
};
const submit = async () => {
	form.winners = winnerInput.value
		.split(/[、,，]/)
		.map((item) => item.trim())
		.filter(Boolean);
	form.year = Number(form.awardDate.slice(0, 4));
	if (!(await formRef.value.validate().catch(() => false))) return;
	try {
		const payload = {
			...form,
			awardDate: form.awardDate.endsWith("-01")
				? form.awardDate
				: `${form.awardDate}-01`,
		};
		if (editingId.value) await updateAward(editingId.value, payload);
		else await createAward(payload);
		ElMessage.success(editingId.value ? "更新奖项成功" : "添加奖项成功");
		visible.value = false;
		load();
	} catch (error) {
		ElMessage.error(editingId.value ? "更新奖项失败" : "添加奖项失败");
		console.error(error);
	}
};
const remove = async (id) => {
	try {
		await ElMessageBox.confirm("确定要删除这个奖项吗？", "确认删除", {
			type: "warning",
		});
		await deleteAward(id);
		ElMessage.success("删除奖项成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("删除奖项失败");
			console.error(error);
		}
	}
};
watch(
	() => filter.competitionName,
	() => {
		filter.competitionTrack = "";
	},
);
onMounted(load);
</script>

<style scoped>
:deep(.danger-item) { color: var(--el-color-danger); } @media (max-width: 768px) { :deep(.admin-dialog) { width: calc(100% - 24px) !important; margin: 12px auto; } }
</style>

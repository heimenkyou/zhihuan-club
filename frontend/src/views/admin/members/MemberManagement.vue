<template>
  <AdminPage title="成员管理">
    <template #action>
      <el-button type="primary" @click="openAdd">添加成员</el-button>
      <el-button type="primary" plain @click="pickVisible = true">从报名记录添加</el-button>
    </template>
    <AdminToolbar>
      <el-input v-model="search.name" placeholder="姓名" clearable @keyup.enter="searchList" />
      <el-input v-model="search.studentId" placeholder="学号" clearable @keyup.enter="searchList" />
      <el-input v-model="search.major" placeholder="专业" clearable @keyup.enter="searchList" />
      <el-input v-model="search.QQNumber" placeholder="QQ号" clearable @keyup.enter="searchList" />
      <el-select v-model="search.department" placeholder="部门" clearable @change="searchList">
        <el-option v-for="item in departments" :key="item" :label="item" :value="item" />
      </el-select>
      <el-select v-model="search.joinYear" placeholder="加入年份" clearable @change="searchList">
        <el-option v-for="year in yearOptions" :key="year" :label="`${year} 年`" :value="year" />
      </el-select>
      <el-select v-model="search.status" placeholder="状态" clearable @change="searchList">
        <el-option label="活跃" value="active" />
        <el-option label="非活跃" value="inactive" />
      </el-select>
      <el-button type="primary" @click="searchList">搜索</el-button>
      <el-button @click="reset">重置</el-button>
    </AdminToolbar>
    <AdminTable v-if="!isMobile">
      <el-table v-loading="loading" :data="page.records" stripe @row-click="handleRowClick">
        <el-table-column prop="name" label="姓名" width="110" />
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="major" label="专业" min-width="150" />
        <el-table-column prop="className" label="班级" min-width="120" />
        <el-table-column prop="department" label="部门" width="130" />
        <el-table-column prop="joinYear" label="加入年份" width="100" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">{{ row.status === 'active' ? '活跃' : '非活跃' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="64" fixed="right">
          <template #default="{ row }">
            <AdminActionMenu>
              <el-dropdown-item @click="view(row)">详情</el-dropdown-item>
              <el-dropdown-item @click="edit(row)">编辑</el-dropdown-item>
              <el-dropdown-item @click="toggleStatus(row)">{{ row.status === 'active' ? '设为非活跃' : '重新启用' }}</el-dropdown-item>
            </AdminActionMenu>
          </template>
        </el-table-column>
      </el-table>
    </AdminTable>
    <AdminResultCards v-else v-loading="loading">
      <article v-for="row in page.records" :key="row.id" class="admin-result-card" @click="view(row)">
        <div>
          <strong>{{ row.name }}</strong>
          <span>{{ row.studentId }} · {{ row.major }} · {{ row.className || '—' }}</span>
          <span>{{ row.department }} · {{ row.joinYear }} 年 · {{ row.status === 'active' ? '活跃' : '非活跃' }}</span>
        </div>
        <AdminActionMenu>
          <el-dropdown-item @click="edit(row)">编辑</el-dropdown-item>
          <el-dropdown-item @click="toggleStatus(row)">{{ row.status === 'active' ? '设为非活跃' : '重新启用' }}</el-dropdown-item>
        </AdminActionMenu>
      </article>
    </AdminResultCards>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />
  </AdminPage>

  <el-dialog v-model="visible" :title="dialogTitle" :width="isMobile ? 'calc(100% - 24px)' : '560px'" class="admin-dialog">
    <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
      <el-form-item label="学号" prop="studentId">
        <el-input v-model="form.studentId" @input="autoFillFromStudentId" />
        <div class="form-tip">输入 11 位学号后自动填充专业、班级与入学年份，并可预填报名记录，均可手动修改</div>
      </el-form-item>
      <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
      <el-form-item label="班级" prop="className"><el-input v-model="form.className" /></el-form-item>
      <el-form-item label="专业" prop="major"><el-input v-model="form.major" /></el-form-item>
      <el-form-item label="电话" prop="phone"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="QQ号" prop="QQNumber"><el-input v-model="form.QQNumber" /></el-form-item>
      <el-form-item label="所属部门" prop="department">
        <el-select v-model="form.department" placeholder="请选择部门" style="width: 100%">
          <el-option v-for="item in departments" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="加入年份" prop="joinYear">
        <el-select v-model="form.joinYear" placeholder="请选择年份" style="width: 100%">
          <el-option v-for="year in yearOptions" :key="year" :label="`${year} 年`" :value="year" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" style="width: 100%">
          <el-option label="活跃" value="active" />
          <el-option label="非活跃" value="inactive" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submit">保存</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="detailVisible" title="成员详情" :width="isMobile ? 'calc(100% - 24px)' : '600px'" class="admin-dialog">
    <el-descriptions :column="1" border class="detail-descriptions">
      <el-descriptions-item label="姓名">{{ detail?.name }}</el-descriptions-item>
      <el-descriptions-item label="学号">{{ detail?.studentId }}</el-descriptions-item>
      <el-descriptions-item label="班级">{{ detail?.className || '—' }}</el-descriptions-item>
      <el-descriptions-item label="专业">{{ detail?.major }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ detail?.phone }}</el-descriptions-item>
      <el-descriptions-item label="QQ号">{{ detail?.QQNumber }}</el-descriptions-item>
      <el-descriptions-item label="部门">{{ detail?.department }}</el-descriptions-item>
      <el-descriptions-item label="加入年份">{{ detail?.joinYear }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ detail?.status === 'active' ? '活跃' : '非活跃' }}</el-descriptions-item>
      <el-descriptions-item label="备注"><span class="multiline">{{ detail?.remark || '—' }}</span></el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ detail?.createTime || '—' }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button @click="viewApplicationsVisible = true">查看报名记录</el-button>
      <el-button @click="editFromDetail">编辑</el-button>
      <el-button @click="detailVisible = false">关闭</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="pickVisible" title="从报名记录添加" :width="isMobile ? 'calc(100% - 24px)' : '800px'" class="admin-dialog">
    <ApplicationPicker selectable @select="fillFromApplication" />
  </el-dialog>

  <el-dialog v-model="viewApplicationsVisible" title="查看报名记录" :width="isMobile ? 'calc(100% - 24px)' : '800px'" class="admin-dialog">
    <ApplicationPicker :preset-student-id="detail?.studentId" />
  </el-dialog>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { computed, onMounted, reactive, ref } from "vue";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import ApplicationPicker from "@/components/admin/ApplicationPicker.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import { getMajorMapping } from "@/services/applicationsService";
import {
	createMember,
	getApplications,
	getMember,
	getMembers,
	updateMember,
	updateMemberStatus,
} from "@/services/adminService";

const { isMobile } = useAdminMobile();
const departments = ["算法部", "项目竞赛部", "综合管理部"];
const search = reactive({
	name: "",
	studentId: "",
	major: "",
	QQNumber: "",
	department: "",
	joinYear: null,
	// 默认只展示活跃成员，管理员可清空状态筛选查看全部
	status: "active",
});
const page = reactive({ current: 1, size: 10, total: 0, records: [] });
const loading = ref(false);
const visible = ref(false);
const detailVisible = ref(false);
const pickVisible = ref(false);
const viewApplicationsVisible = ref(false);
const editingId = ref(null);
const dialogTitle = ref("添加成员");
const formRef = ref();
const detail = ref();
const currentYear = new Date().getFullYear();
// 年份选项覆盖近五年到次年，方便手动录入不同批次
const yearOptions = computed(() => {
	const options = [];
	for (let year = currentYear + 1; year >= currentYear - 5; year--)
		options.push(year);
	return options;
});
const emptyForm = () => ({
	name: "",
	studentId: "",
	className: "",
	major: "",
	phone: "",
	QQNumber: "",
	department: "",
	joinYear: currentYear,
	status: "active",
	remark: "",
});
const form = reactive(emptyForm());
/** 专业代号到专业信息的映射表，用于学号自动填充专业。 */
const majorMapping = ref({});
const rules = {
	name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
	studentId: [{ required: true, message: "请输入学号", trigger: "blur" }],
	className: [{ required: true, message: "请输入班级", trigger: "blur" }],
	major: [{ required: true, message: "请输入专业", trigger: "blur" }],
	department: [
		{ required: true, message: "请选择所属部门", trigger: "change" },
	],
	joinYear: [{ required: true, message: "请选择加入年份", trigger: "change" }],
};

/**
 * 依据 11 位学号自动填充专业、班级与入学年份，并静默查询报名记录预填基础信息。
 * 专业取学号第 5-8 位查映射表全称，班级与后端报名逻辑一致（B+入学年份后两位+班级序号），
 * 入学年份取学号前四位；报名记录命中时预填姓名、电话、QQ号（仅填空字段）。
 * 只在做输入时计算，搜不到报名记录则不改变现有值。
 */
const autoFillFromStudentId = async () => {
	const studentId = form.studentId?.trim() ?? "";
	if (!/^\d{11}$/.test(studentId)) return;
	const major = majorMapping.value[studentId.substring(4, 8)];
	if (major?.fullName) form.major = major.fullName;
	form.className = `B${studentId.substring(2, 4)}${studentId.charAt(8)}`;
	form.joinYear = Number(studentId.substring(0, 4));
	// 静默查询报名记录，命中且学号未再变化时才预填，避免快速输入时的竞态
	try {
		const data = await getApplications({
			current: 1,
			size: 1,
			studentId,
		});
		const record = data?.records?.[0];
		if (!record || form.studentId?.trim() !== studentId) return;
		if (!form.name) form.name = record.name ?? "";
		if (!form.phone) form.phone = record.phone ?? "";
		if (!form.QQNumber) form.QQNumber = record.QQNumber ?? "";
	} catch (error) {
		console.error("查询报名记录失败:", error);
	}
};

const load = async () => {
	loading.value = true;
	try {
		const data = await getMembers({
			current: page.current,
			size: page.size,
			...search,
		});
		Object.assign(page, data);
	} catch (error) {
		ElMessage.error("获取成员列表失败");
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
		major: "",
		QQNumber: "",
		department: "",
		joinYear: null,
		status: "active",
	});
	searchList();
};
const handleRowClick = (row, _, event) => {
	if (!event.target.closest(".el-dropdown")) view(row);
};
const openAdd = () => {
	dialogTitle.value = "添加成员";
	editingId.value = null;
	Object.assign(form, emptyForm());
	visible.value = true;
};
const edit = (row) => {
	dialogTitle.value = "编辑成员";
	editingId.value = row.id;
	Object.assign(form, {
		name: row.name,
		studentId: row.studentId,
		className: row.className || "",
		major: row.major,
		phone: row.phone || "",
		QQNumber: row.QQNumber || "",
		department: row.department,
		joinYear: row.joinYear,
		status: row.status,
		remark: row.remark || "",
	});
	visible.value = true;
};
const view = async (row) => {
	detail.value = row;
	detailVisible.value = true;
};
const editFromDetail = () => {
	detailVisible.value = false;
	edit(detail.value);
};
const toggleStatus = async (row) => {
	const next = row.status === "active" ? "inactive" : "active";
	try {
		await updateMemberStatus(row.id, next);
		ElMessage.success(
			next === "active" ? "成员已重新启用" : "成员已设为非活跃",
		);
		load();
	} catch (error) {
		ElMessage.error("更新成员状态失败");
		console.error(error);
	}
};
const submit = async () => {
	if (!(await formRef.value?.validate().catch(() => false))) return;
	const payload = { ...form };
	try {
		if (editingId.value) {
			await updateMember(editingId.value, payload);
			ElMessage.success("更新成员成功");
		} else {
			await createMember(payload);
			ElMessage.success("添加成员成功");
		}
		visible.value = false;
		load();
	} catch (error) {
		// 学号重复时携带已有成员ID，提示并打开已有成员详情
		if (!editingId.value && error?.code === "A000500") {
			const existingId = error?.data?.id;
			visible.value = false;
			ElMessage.warning(
				`该学号已是社团成员${existingId ? `（成员ID ${existingId}）` : ""}，已为你打开既有成员资料`,
			);
			if (existingId) {
				detail.value = await getMember(existingId);
				detailVisible.value = true;
			}
			return;
		}
		ElMessage.error(editingId.value ? "更新成员失败" : "添加成员失败");
		console.error(error);
	}
};
// 从报名记录添加：带入基础信息，部门默认取报名第一志愿，管理员可改
const fillFromApplication = (row) => {
	pickVisible.value = false;
	dialogTitle.value = "添加成员（来自报名记录）";
	editingId.value = null;
	Object.assign(form, {
		name: row.name,
		studentId: row.studentId,
		className: row.className || "",
		major: row.major,
		phone: row.phone || "",
		QQNumber: row.QQNumber || "",
		department: row.department,
		joinYear: currentYear,
		status: "active",
		remark: "",
	});
	visible.value = true;
};
onMounted(async () => {
	try {
		majorMapping.value = await getMajorMapping();
	} catch (error) {
		console.error("获取专业映射表失败:", error);
	}
	load();
});
</script>

<style scoped>
.multiline { white-space: pre-wrap; word-break: break-word; }
.form-tip { margin-top: 4px; color: #98a2b3; font-size: 12px; line-height: 1.5; }
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

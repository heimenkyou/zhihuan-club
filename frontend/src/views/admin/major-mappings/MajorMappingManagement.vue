<template>
  <AdminPage title="专业映射">
    <template #action>
      <el-button type="primary" plain @click="openUnmapped">从报名记录添加</el-button>
      <el-button type="primary" @click="openAdd">添加映射</el-button>
    </template>

    <AdminToolbar>
      <el-input v-model="keyword" placeholder="搜索编号、简称或专业全称" clearable />
    </AdminToolbar>

    <AdminTable v-if="!isMobile" v-loading="loading">
      <el-table :data="filteredMappings" stripe>
        <el-table-column prop="code" label="专业编号" width="110" />
        <el-table-column prop="shortName" label="专业简称" width="140" />
        <el-table-column prop="fullName" label="专业全称" min-width="220" />
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </AdminTable>
    <AdminResultCards v-else v-loading="loading">
      <article v-for="row in filteredMappings" :key="row.code" class="admin-result-card">
        <div>
          <strong>{{ row.shortName }} · {{ row.fullName }}</strong>
          <span>专业编号：{{ row.code }}</span>
        </div>
        <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
      </article>
    </AdminResultCards>

    <el-empty v-if="!loading && !filteredMappings.length" description="暂无专业映射" />

    <el-dialog v-model="visible" :title="dialogTitle" :width="isMobile ? 'calc(100% - 24px)' : '520px'" class="admin-dialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="专业编号" prop="code">
          <el-input v-model="form.code" maxlength="4" :disabled="editing" placeholder="学号第5至8位，例如 0613" />
        </el-form-item>
        <el-form-item label="专业简称" prop="shortName">
          <el-input v-model="form.shortName" placeholder="例如：物工、智科" />
        </el-form-item>
        <el-form-item label="专业全称" prop="fullName">
          <el-input v-model="form.fullName" placeholder="例如：物联网工程" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="unmappedVisible" title="选择报名记录" :width="isMobile ? 'calc(100% - 24px)' : '820px'" class="admin-dialog">
      <el-alert title="选择后会自动填入专业全称和专业编号，专业简称仍需手动填写。" type="info" :closable="false" show-icon />
      <AdminTable v-if="!isMobile" v-loading="unmappedLoading">
        <el-table :data="unmappedApplications" stripe>
          <el-table-column prop="name" label="姓名" width="110" />
          <el-table-column prop="studentId" label="学号" width="140" />
          <el-table-column prop="major" label="专业全称" min-width="220" />
          <el-table-column label="操作" width="90" fixed="right">
            <template #default="{ row }"><el-button type="primary" link @click="selectApplication(row)">选择</el-button></template>
          </el-table-column>
        </el-table>
      </AdminTable>
      <AdminResultCards v-else v-loading="unmappedLoading">
        <article v-for="row in unmappedApplications" :key="row.id" class="admin-result-card">
          <div><strong>{{ row.name }}</strong><span>{{ row.studentId }} · {{ row.major }}</span></div>
          <el-button type="primary" link @click="selectApplication(row)">选择</el-button>
        </article>
      </AdminResultCards>
      <el-empty v-if="!unmappedLoading && !unmappedApplications.length" description="没有待建立映射的报名记录" />
    </el-dialog>
  </AdminPage>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { computed, onMounted, reactive, ref } from "vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	createMajorMapping,
	getAdminMajorMappings,
	getUnmappedMajorApplications,
	updateMajorMapping,
} from "@/services/adminService";

const { isMobile } = useAdminMobile();
const mappings = ref([]);
const unmappedApplications = ref([]);
const loading = ref(false);
const unmappedLoading = ref(false);
const visible = ref(false);
const unmappedVisible = ref(false);
const editing = ref(false);
const dialogTitle = ref("添加专业映射");
const keyword = ref("");
const formRef = ref();
const form = reactive({ code: "", shortName: "", fullName: "" });
const rules = {
	code: [
		{ required: true, message: "请输入专业编号", trigger: "blur" },
		{ pattern: /^\d{4}$/, message: "专业编号必须是4位数字", trigger: "blur" },
	],
	shortName: [{ required: true, message: "请输入专业简称", trigger: "blur" }],
	fullName: [{ required: true, message: "请输入专业全称", trigger: "blur" }],
};
const filteredMappings = computed(() => {
	const value = keyword.value.trim().toLowerCase();
	if (!value) return mappings.value;
	return mappings.value.filter((item) =>
		[item.code, item.shortName, item.fullName].some((field) =>
			String(field || "").toLowerCase().includes(value),
		),
	);
});

const load = async () => {
	loading.value = true;
	try {
		mappings.value = (await getAdminMajorMappings()) || [];
	} catch (error) {
		ElMessage.error("获取专业映射失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const openAdd = () => {
	Object.assign(form, { code: "", shortName: "", fullName: "" });
	editing.value = false;
	dialogTitle.value = "添加专业映射";
	visible.value = true;
};
const openEdit = (row) => {
	Object.assign(form, row);
	editing.value = true;
	dialogTitle.value = "编辑专业映射";
	visible.value = true;
};
const openUnmapped = async () => {
	unmappedVisible.value = true;
	unmappedLoading.value = true;
	try {
		unmappedApplications.value = (await getUnmappedMajorApplications()) || [];
	} catch (error) {
		ElMessage.error("获取未映射报名记录失败");
		console.error(error);
	} finally {
		unmappedLoading.value = false;
	}
};
const selectApplication = (row) => {
	const studentId = String(row.studentId || "");
	Object.assign(form, {
		code: /^\d{8,}$/.test(studentId) ? studentId.slice(4, 8) : "",
		shortName: "",
		fullName: row.major || "",
	});
	editing.value = false;
	dialogTitle.value = "添加专业映射";
	unmappedVisible.value = false;
	visible.value = true;
};
const submit = async () => {
	if (!(await formRef.value?.validate().catch(() => false))) return;
	try {
		if (editing.value) await updateMajorMapping(form.code, { ...form });
		else await createMajorMapping({ ...form });
		ElMessage.success(editing.value ? "更新专业映射成功" : "添加专业映射成功");
		visible.value = false;
		await load();
	} catch (error) {
		ElMessage.error(error?.response?.data?.message || "保存专业映射失败");
		console.error(error);
	}
};

onMounted(load);
</script>

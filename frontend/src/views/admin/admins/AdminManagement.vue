<template>
  <AdminPage title="管理员管理">
    <template #action><el-button v-if="isSuperAdmin" type="primary" @click="openAdd">添加管理员</el-button></template>
    <AdminTable v-if="!isMobile"><el-table :data="admins"><el-table-column prop="id" label="ID" width="80" /><el-table-column prop="username" label="用户名" min-width="180" /><el-table-column label="角色" width="120"><template #default="{ row }"><el-tag :type="row.role === 'super' ? 'danger' : row.role === 'submitter' ? 'warning' : 'primary'" size="small">{{ roleLabel(row.role) }}</el-tag></template></el-table-column><el-table-column prop="createTime" label="创建时间" min-width="180" /><el-table-column label="操作" width="64" fixed="right"><template #default="{ row }"><AdminActionMenu><el-dropdown-item @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item v-if="isSuperAdmin && row.role !== 'super'" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></template></el-table-column></el-table></AdminTable>
    <AdminResultCards v-else><article v-for="row in admins" :key="row.id" class="admin-result-card"><div><strong>{{ row.username }}</strong><span>{{ roleLabel(row.role) }}</span></div><AdminActionMenu><el-dropdown-item @click="edit(row)">编辑</el-dropdown-item><el-dropdown-item v-if="isSuperAdmin && row.role !== 'super'" class="danger-item" @click="remove(row.id)">删除</el-dropdown-item></AdminActionMenu></article></AdminResultCards>
  </AdminPage>
  <el-dialog v-model="visible" :title="dialogTitle" width="520px" class="admin-dialog"><el-form ref="formRef" :model="form" :rules="rules" label-position="top"><el-form-item label="用户名" prop="username"><el-input v-model="form.username" /></el-form-item><el-form-item v-if="editingId" label="新密码（选填）" prop="newPassword"><el-input v-model="form.newPassword" type="password" show-password /></el-form-item><el-form-item v-else label="密码" prop="password"><el-input v-model="form.password" type="password" show-password /></el-form-item><el-form-item v-if="isSuperAdmin" label="角色" prop="role"><el-select v-model="form.role"><el-option label="超级管理员" value="super" /><el-option label="普通管理员" value="normal" /><el-option label="内容提交员" value="submitter" /></el-select></el-form-item></el-form><template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template></el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminResultCards from "@/components/admin/AdminResultCards.vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import { useAdminStore } from "@/stores/adminStore";
import {
	createAdmin,
	deleteAdmin,
	getAdmins,
	updateAdmin,
} from "@/services/adminService";

const { isMobile } = useAdminMobile();
const isSuperAdmin = useAdminStore().isSuperAdmin();
const admins = ref([]);
const visible = ref(false);
const editingId = ref(null);
const formRef = ref();
const form = reactive({
	username: "",
	password: "",
	newPassword: "",
	role: "normal",
});
const rules = {
	username: [
		{ required: true, message: "请输入用户名", trigger: "blur" },
		{ min: 3, message: "用户名长度不能少于3位", trigger: "blur" },
	],
	password: [
		{ required: true, message: "请输入密码", trigger: "blur" },
		{ min: 6, message: "密码长度不能少于6位", trigger: "blur" },
	],
	newPassword: [{ min: 6, message: "密码长度不能少于6位", trigger: "blur" }],
};
const dialogTitle = ref("添加管理员");
const roleLabel = (role) => ({ super: "超级管理员", normal: "普通管理员", submitter: "内容提交员" })[role] || role;
const load = async () => {
	try {
		admins.value = (await getAdmins())?.records ?? [];
	} catch (error) {
		ElMessage.error("加载管理员列表失败");
		console.error(error);
	}
};
const openAdd = () => {
	dialogTitle.value = "添加管理员";
	editingId.value = null;
	Object.assign(form, {
		username: "",
		password: "",
		newPassword: "",
		role: "normal",
	});
	visible.value = true;
};
const edit = (row) => {
	dialogTitle.value = "编辑管理员";
	editingId.value = row.id;
	Object.assign(form, {
		username: row.username,
		password: "",
		newPassword: "",
		role: row.role,
	});
	visible.value = true;
};
const remove = async (id) => {
	try {
		await deleteAdmin(id);
		ElMessage.success("删除管理员成功");
		load();
	} catch (error) {
		ElMessage.error("删除管理员失败");
		console.error(error);
	}
};
const submit = async () => {
	if (!(await formRef.value?.validate().catch(() => false))) return;
	try {
		if (editingId.value) {
			const payload = { username: form.username, role: form.role };
			if (form.newPassword) payload.password = form.newPassword;
			await updateAdmin(editingId.value, payload);
		} else
			await createAdmin({
				username: form.username,
				password: form.password,
				role: form.role,
				createTime: new Date().toISOString(),
				updateTime: new Date().toISOString(),
			});
		ElMessage.success(editingId.value ? "更新管理员成功" : "添加管理员成功");
		visible.value = false;
		load();
	} catch (error) {
		ElMessage.error(editingId.value ? "更新管理员失败" : "添加管理员失败");
		console.error(error);
	}
};
onMounted(load);
</script>

<style scoped>
:deep(.danger-item) { color: var(--el-color-danger); } @media (max-width: 768px) { :deep(.admin-dialog) { width: calc(100% - 24px) !important; margin: 12px auto; } }
</style>

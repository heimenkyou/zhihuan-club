<template>
  <AdminPage title="附件库"><template #action><el-upload :show-file-list="false" :http-request="upload" accept="image/*"><el-button type="primary" :loading="uploading">上传图片</el-button></el-upload></template>
    <el-empty v-if="!attachments.length && !loading" description="暂无附件" />
    <div v-else v-loading="loading" class="attachment-grid"><article v-for="attachment in attachments" :key="attachment.id" class="attachment-card"><el-image v-if="isImage(attachment)" :src="attachment.url" fit="cover" :preview-src-list="[attachment.url]" preview-teleported /><div v-else class="file-placeholder"><el-icon><i-ep-document /></el-icon><span>{{ fileExtension(attachment.originalName) || '文件' }}</span></div><div class="attachment-info"><span :title="attachment.originalName">{{ attachment.originalName }}</span><small>{{ formatSize(attachment.size) }}</small></div><AdminActionMenu class="attachment-action"><el-dropdown-item class="danger-item" @click="remove(attachment)">删除</el-dropdown-item></AdminActionMenu></article></div>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />
  </AdminPage>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import AdminActionMenu from "@/components/admin/AdminActionMenu.vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import {
	deleteAttachment,
	getAttachments,
	uploadImage,
} from "@/services/attachmentService";

const attachments = ref([]);
const loading = ref(false);
const uploading = ref(false);
const page = ref({ current: 1, size: 24, total: 0 });
const load = async () => {
	loading.value = true;
	try {
		const data = await getAttachments({
			current: page.value.current,
			size: page.value.size,
		});
		attachments.value = data.records || [];
		page.value = { ...page.value, ...data };
	} catch (error) {
		ElMessage.error(error.message || "加载附件失败");
	} finally {
		loading.value = false;
	}
};
const upload = async ({ file }) => {
	uploading.value = true;
	try {
		await uploadImage(file);
		ElMessage.success("图片上传成功");
		load();
	} catch (error) {
		ElMessage.error(error.message || "图片上传失败");
	} finally {
		uploading.value = false;
	}
};
const remove = async (attachment) => {
	try {
		await ElMessageBox.confirm(
			`确定删除“${attachment.originalName}”吗？`,
			"删除图片",
			{ type: "warning" },
		);
		await deleteAttachment(attachment.id);
		ElMessage.success("图片已删除");
		load();
	} catch (error) {
		if (error !== "cancel") ElMessage.error(error.message || "删除图片失败");
	}
};
const formatSize = (size) => `${(size / 1024 / 1024).toFixed(2)} MB`;
const isImage = (attachment) => attachment.mimeType?.startsWith("image/");
const fileExtension = (name) => name?.split(".").pop()?.toUpperCase();
onMounted(load);
</script>

<style scoped>
.attachment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; }.attachment-card { position: relative; min-width: 0; overflow: hidden; border: 1px solid #eaecf0; border-radius: 8px; }.attachment-card :deep(.el-image), .file-placeholder { display: grid; width: 100%; height: 112px; place-items: center; }.file-placeholder { gap: 4px; color: #667085; background: #f8fafc; }.file-placeholder .el-icon { font-size: 28px; }.file-placeholder span { font-size: 12px; font-weight: 600; }.attachment-info { display: grid; gap: 3px; padding: 8px; }.attachment-info span { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 13px; }.attachment-info small { color: #98a2b3; }.attachment-action { position: absolute; top: 5px; right: 5px; border-radius: 5px; background: rgb(255 255 255 / 88%); }:deep(.danger-item) { color: var(--el-color-danger); } @media (max-width: 768px) { .attachment-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }.attachment-card :deep(.el-image), .file-placeholder { height: 120px; } }
</style>

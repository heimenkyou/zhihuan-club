<template>
  <AdminPage title="附件库">
    <template #action><el-button type="primary" @click="pickerVisible = true">上传图片</el-button></template>
    <div v-if="selectedIds.size > 0" class="selection-bar">
      <span class="selection-count">已选 {{ selectedIds.size }} 张</span>
      <el-button type="danger" :loading="deleting" @click="batchDelete">删除选中</el-button>
      <el-button @click="clearSelection">取消选择</el-button>
    </div>
    <el-empty v-if="!attachments.length && !loading" description="暂无附件" />
    <div v-else v-loading="loading" class="attachment-grid">
      <article
        v-for="attachment in attachments"
        :key="attachment.id"
        class="attachment-card"
        :class="{ selected: selectedIds.has(attachment.id) }"
        @click="openDetail(attachment)"
      >
        <el-image :src="attachment.url" fit="cover" class="card-image">
          <template #error>
            <div class="file-placeholder"><el-icon><i-ep-document /></el-icon><span>{{ fileExtension(attachment.originalName) || '文件' }}</span></div>
          </template>
        </el-image>
        <div class="select-mask" />
        <button
          class="select-checkbox"
          type="button"
          :class="{ checked: selectedIds.has(attachment.id) }"
          :aria-label="selectedIds.has(attachment.id) ? '取消选择' : '选择'"
          @click.stop="toggleSelect(attachment)"
        >
          <el-icon v-if="selectedIds.has(attachment.id)"><i-ep-check /></el-icon>
        </button>
        <div class="attachment-info">
          <span :title="attachment.originalName">{{ attachment.originalName }}</span>
          <small>{{ formatSize(attachment.size) }}</small>
        </div>
      </article>
    </div>
    <AdminPagination v-model:current-page="page.current" :page-size="page.size" :total="page.total" @change="load" />
    <AttachmentPicker v-model:visible="pickerVisible" :selectable="false" @uploaded="load" />

    <el-dialog v-model="detailVisible" title="附件详情" width="min(640px, calc(100% - 24px))">
      <div v-if="current" class="detail-body">
        <div class="detail-preview">
          <el-image :src="current.url" fit="contain" :preview-src-list="[current.url]" preview-teleported class="detail-image">
            <template #error>
              <div class="file-placeholder"><el-icon><i-ep-document /></el-icon><span>{{ fileExtension(current.originalName) || '文件' }}</span></div>
            </template>
          </el-image>
          <span class="detail-preview-tip">点击图片可放大预览</span>
        </div>
        <el-form label-width="80px" class="detail-form">
          <el-form-item label="附件名称">
            <div class="name-edit">
              <el-input v-model="nameInput" maxlength="255" placeholder="请输入附件名称" />
              <el-button type="primary" :loading="saving" @click="saveName">保存</el-button>
            </div>
          </el-form-item>
          <el-form-item label="文件类型"><span>{{ current.mimeType }}</span></el-form-item>
          <el-form-item label="文件大小"><span>{{ formatSize(current.size) }}</span></el-form-item>
          <el-form-item label="上传时间"><span>{{ current.createTime }}</span></el-form-item>
        </el-form>
        <div class="detail-links">
          <div class="links-heading"><strong>引用链接</strong></div>
          <div v-for="item in linkItems" :key="item.label" class="link-item">
            <span class="link-label">{{ item.label }}</span>
            <el-input :model-value="item.value" readonly class="link-input" />
            <el-button @click="copyLink(item.value)">复制</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </AdminPage>
</template>

<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, onMounted, ref } from "vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminPagination from "@/components/admin/AdminPagination.vue";
import AttachmentPicker from "@/components/admin/AttachmentPicker.vue";
import {
	deleteAttachment,
	getAttachments,
	renameAttachment,
} from "@/services/attachmentService";

const attachments = ref([]);
const loading = ref(false);
const pickerVisible = ref(false);
const page = ref({ current: 1, size: 24, total: 0 });
const detailVisible = ref(false);
const current = ref(null);
const nameInput = ref("");
const saving = ref(false);
const selectedIds = ref(new Set());
const deleting = ref(false);
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

/**
 * 切换附件选中状态。
 *
 * @param {Object} attachment 附件记录
 */
const toggleSelect = (attachment) => {
	const next = new Set(selectedIds.value);
	if (next.has(attachment.id)) next.delete(attachment.id);
	else next.add(attachment.id);
	selectedIds.value = next;
};

/** 清空所有选中项。 */
const clearSelection = () => {
	selectedIds.value = new Set();
};

/** 批量删除选中的附件。 */
const batchDelete = async () => {
	const count = selectedIds.value.size;
	try {
		await ElMessageBox.confirm(
			`确定删除选中的 ${count} 张图片吗？`,
			"批量删除",
			{ type: "warning" },
		);
		deleting.value = true;
		await Promise.all(
			[...selectedIds.value].map((id) => deleteAttachment(id)),
		);
		ElMessage.success(`已删除 ${count} 张图片`);
		clearSelection();
		load();
	} catch (error) {
		if (error !== "cancel") ElMessage.error(error.message || "批量删除失败");
	} finally {
		deleting.value = false;
	}
};

/**
 * 打开附件详情，展示预览图、元数据与三种引用链接。
 *
 * @param {Object} attachment 附件记录
 */
const openDetail = (attachment) => {
	current.value = attachment;
	nameInput.value = attachment.originalName;
	detailVisible.value = true;
};

/** 保存修改后的附件名称。 */
const saveName = async () => {
	const name = nameInput.value?.trim();
	if (!name) {
		ElMessage.warning("附件名称不能为空");
		return;
	}
	saving.value = true;
	try {
		const updated = await renameAttachment(current.value.id, name);
		current.value.originalName = updated.originalName;
		ElMessage.success("名称已保存");
	} catch (error) {
		ElMessage.error(error.message || "保存失败");
	} finally {
		saving.value = false;
	}
};

/** 生成 URL、HTML、Markdown 三种引用链接。 */
const linkItems = computed(() => {
	if (!current.value) return [];
	const { url, originalName } = current.value;
	return [
		{ label: "URL", value: url },
		{ label: "HTML", value: `<img src="${url}" alt="${originalName}">` },
		{ label: "Markdown", value: `![${originalName}](${url})` },
	];
});

/** 复制引用链接到剪贴板。 */
const copyLink = async (value) => {
	try {
		await navigator.clipboard.writeText(value);
		ElMessage.success("已复制");
	} catch {
		ElMessage.error("复制失败");
	}
};
const formatSize = (size) => `${(size / 1024 / 1024).toFixed(2)} MB`;
const fileExtension = (name) => name?.split(".").pop()?.toUpperCase();
onMounted(load);
</script>

<style scoped>
.attachment-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
	gap: 12px;
}

.selection-bar {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 14px;
}

.selection-count {
	color: #667085;
	font-size: 13px;
}

.attachment-card {
	position: relative;
	min-width: 0;
	overflow: hidden;
	border: 1px solid #eaecf0;
	border-radius: 8px;
	cursor: pointer;
	transition: border-color 0.2s, box-shadow 0.2s;
}

.attachment-card.selected {
	border-color: #67c23a;
	box-shadow: 0 0 0 2px #67c23a;
}

.attachment-card :deep(.el-image),
.file-placeholder {
	display: grid;
	width: 100%;
	height: 112px;
	place-items: center;
}

.file-placeholder {
	gap: 4px;
	color: #667085;
	background: #f8fafc;
}

.file-placeholder .el-icon {
	font-size: 28px;
}

.file-placeholder span {
	font-size: 12px;
	font-weight: 600;
}

/* 悬浮时图片上半部分覆盖灰色渐变，选中后保持 */
.select-mask {
	position: absolute;
	inset: 0;
	background: linear-gradient(to bottom, rgb(128 128 128 / 45%), rgb(128 128 128 / 0%));
	opacity: 0;
	pointer-events: none;
	transition: opacity 0.2s;
}

.attachment-card:hover .select-mask,
.attachment-card.selected .select-mask {
	opacity: 1;
}

/* 右上角圆形复选框，悬浮显示、悬浮变绿，选中后常驻为绿色 */
.select-checkbox {
	position: absolute;
	top: 6px;
	right: 6px;
	z-index: 1;
	display: grid;
	place-items: center;
	width: 20px;
	height: 20px;
	padding: 0;
	color: #fff;
	border: 2px solid rgb(255 255 255 / 90%);
	border-radius: 50%;
	background: rgb(0 0 0 / 30%);
	cursor: pointer;
	opacity: 0;
	transition: opacity 0.2s, color 0.2s, border-color 0.2s, background 0.2s;
}

.attachment-card:hover .select-checkbox,
.attachment-card.selected .select-checkbox {
	opacity: 1;
}

.select-checkbox:hover {
	color: #67c23a;
	border-color: #67c23a;
}

.select-checkbox.checked {
	color: #fff;
	border-color: #67c23a;
	background: #67c23a;
}

.attachment-info {
	display: grid;
	gap: 3px;
	padding: 8px;
}

.attachment-info span {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	font-size: 13px;
}

.attachment-info small {
	color: #98a2b3;
}

.detail-body {
	display: grid;
	gap: 20px;
}

.detail-preview {
	display: grid;
	gap: 6px;
	place-items: center;
	padding: 16px;
	background: #f8fafc;
	border-radius: 8px;
}

.detail-image {
	width: 100%;
	max-height: 320px;
	cursor: zoom-in;
}

.detail-image :deep(.el-image__inner) {
	max-height: 320px;
}

.detail-preview-tip {
	color: #98a2b3;
	font-size: 12px;
}

.name-edit {
	display: flex;
	gap: 8px;
	width: 100%;
}

.detail-links {
	display: grid;
	gap: 8px;
}

.links-heading {
	margin-bottom: 4px;
}

.link-item {
	display: flex;
	gap: 8px;
	align-items: center;
	min-width: 0;
}

.link-label {
	flex: 0 0 auto;
	width: 68px;
	color: #667085;
	font-size: 13px;
	font-weight: 600;
}

.link-input {
	flex: 1;
	min-width: 0;
}

@media (hover: none) {
	.select-mask,
	.select-checkbox {
		opacity: 1;
	}
}

@media (max-width: 768px) {
	.attachment-grid {
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 8px;
	}

	.attachment-card :deep(.el-image),
	.file-placeholder {
		height: 120px;
	}

	.link-item {
		flex-wrap: wrap;
	}

	.link-input {
		flex-basis: 100%;
		order: 2;
	}
}
</style>

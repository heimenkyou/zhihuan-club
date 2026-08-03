<template>
  <el-dialog v-model="visible" title="图片附件" class="attachment-picker-dialog" destroy-on-close>
    <div class="attachment-picker-content">
      <el-upload
        drag
        multiple
        accept="image/*"
        :auto-upload="false"
        :show-file-list="false"
        :on-change="addToQueue"
      >
        <i-ep-upload-filled class="upload-icon" />
        <div class="el-upload__text">拖拽图片到这里，或<em>点击选择多个文件</em></div>
        <template #tip><div class="el-upload__tip">仅支持图片，单个文件不超过 10MB</div></template>
      </el-upload>

      <section v-if="uploadQueue.length" class="upload-queue">
        <div class="queue-header">
          <strong>待上传（{{ uploadQueue.length }}）</strong>
          <el-button type="primary" :loading="uploading" @click="uploadQueueFiles">
            上传 {{ uploadQueue.length }} 个文件
          </el-button>
        </div>
        <div class="queue-list">
          <article v-for="item in uploadQueue" :key="item.id" class="queue-item">
            <el-image :src="item.previewUrl" fit="cover" class="queue-thumbnail">
              <template #error><div class="file-placeholder"><i-ep-document /></div></template>
            </el-image>
            <div class="queue-file">
              <span :title="item.file.name">{{ item.file.name }}</span>
              <small>{{ formatSize(item.file.size) }}</small>
              <el-progress
                v-if="item.status === 'uploading'"
                :percentage="item.progress"
                :show-text="false"
              />
              <small v-else :class="{ 'upload-error': item.status === 'error' }">
                {{ uploadStatusText(item) }}
              </small>
            </div>
            <el-button
              text
              type="danger"
              :disabled="item.status === 'uploading'"
              aria-label="移除待上传文件"
              @click="removeFromQueue(item)"
            >
              <i-ep-delete />
            </el-button>
          </article>
        </div>
      </section>

      <section class="attachment-library">
        <div class="library-heading"><strong>已有附件</strong></div>
        <el-empty v-if="attachments.length === 0 && !loading" description="暂无附件" />
        <div v-else v-loading="loading" class="attachment-grid">
          <button
            v-for="attachment in attachments"
            :key="attachment.id"
            type="button"
            class="attachment-card"
            :class="{ selected: selectable && selectedUrls.includes(attachment.url) }"
            :disabled="!selectable"
            @click="selectAttachment(attachment)"
          >
            <el-image :src="attachment.url" fit="cover">
              <template #error><div class="file-placeholder"><i-ep-document /></div></template>
            </el-image>
            <span :title="attachment.originalName">{{ attachment.originalName }}</span>
          </button>
        </div>
        <el-pagination
          v-if="page.total > page.size"
          v-model:current-page="page.current"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total"
          @current-change="loadAttachments"
        />
      </section>
    </div>
  </el-dialog>
</template>

<script setup>
import { computed, onBeforeUnmount, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { getAttachments, uploadImage } from "@/services/attachmentService";

const props = defineProps({
	modelValue: { type: [String, Array], default: "" },
	multiple: Boolean,
	selectable: { type: Boolean, default: true },
});
const emit = defineEmits(["update:modelValue", "uploaded"]);
const visible = defineModel("visible", { default: false });
const attachments = ref([]);
const loading = ref(false);
const uploading = ref(false);
const uploadQueue = ref([]);
const page = ref({ current: 1, size: 24, total: 0 });
const selectedUrls = computed(() =>
	props.multiple ? props.modelValue || [] : [props.modelValue],
);

/** 加载附件库当前页。 */
const loadAttachments = async () => {
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

/** 将本地选择的文件加入上传队列。 */
const addToQueue = (uploadFile) => {
	const file = uploadFile.raw;
	if (!file || uploadQueue.value.some((item) => item.file === file)) return;
	uploadQueue.value.push({
		id: uploadFile.uid,
		file,
		previewUrl: URL.createObjectURL(file),
		status: "waiting",
		progress: 0,
	});
};

/** 释放本地预览地址并从队列中移除文件。 */
const removeFromQueue = (item) => {
	URL.revokeObjectURL(item.previewUrl);
	uploadQueue.value = uploadQueue.value.filter((queued) => queued !== item);
};

/** 依次上传当前队列，避免多个大文件同时占用网络。 */
const uploadQueueFiles = async () => {
	if (uploading.value) return;
	uploading.value = true;
	const items = [...uploadQueue.value];
	try {
		for (const item of items) {
			if (!uploadQueue.value.includes(item)) continue;
			item.status = "uploading";
			item.progress = 0;
			try {
				const attachment = await uploadImage(item.file, (progress) => {
					item.progress = progress;
				});
				removeFromQueue(item);
				emit("uploaded", attachment);
				selectAttachment(attachment, false);
				page.value.current = 1;
				await loadAttachments();
			} catch (error) {
				item.status = "error";
				ElMessage.error(`${item.file.name}：${error.message || "上传失败"}`);
			}
		}
	} finally {
		uploading.value = false;
	}
};

/** 将附件 URL 写入调用方字段。 */
const selectAttachment = (attachment, closeAfterSelect = true) => {
	if (!props.selectable) return;
	if (!props.multiple) {
		emit("update:modelValue", attachment.url);
		if (closeAfterSelect) visible.value = false;
		return;
	}
	const urls = [...selectedUrls.value];
	const index = urls.indexOf(attachment.url);
	if (index === -1) urls.push(attachment.url);
	else urls.splice(index, 1);
	emit("update:modelValue", urls);
};

const formatSize = (size) => `${(size / 1024 / 1024).toFixed(2)} MB`;
const uploadStatusText = (item) =>
	item.status === "error" ? "上传失败，可重试" : "等待上传";

watch(visible, (value) => {
	if (value) loadAttachments();
});

onBeforeUnmount(() => {
	uploadQueue.value.forEach((item) => {
		URL.revokeObjectURL(item.previewUrl);
	});
});
</script>

<style scoped>
.attachment-picker-dialog { width: min(900px, calc(100% - 24px)); }
.attachment-picker-content { max-height: 70vh; overflow-y: auto; padding-right: 4px; }
.upload-icon { margin-bottom: 8px; font-size: 32px; color: var(--el-color-primary); }
.upload-queue, .attachment-library { margin-top: 20px; }
.queue-header, .library-heading { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; }
.queue-list { display: grid; gap: 8px; }
.queue-item { display: flex; align-items: center; gap: 10px; min-width: 0; padding: 8px; border: 1px solid var(--el-border-color); border-radius: 6px; }
.queue-thumbnail { flex: 0 0 auto; width: 48px; height: 48px; }
.queue-file { display: grid; flex: 1; gap: 3px; min-width: 0; }
.queue-file span { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.queue-file small { color: var(--el-text-color-secondary); }
.upload-error { color: var(--el-color-danger) !important; }
.attachment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; margin-bottom: 20px; }
.attachment-card { overflow: hidden; padding: 0; color: inherit; text-align: left; background: var(--el-bg-color); border: 1px solid var(--el-border-color); border-radius: 6px; cursor: pointer; }
.attachment-card:disabled { color: inherit; cursor: default; }
.attachment-card.selected { border: 2px solid var(--el-color-primary); }
.attachment-card :deep(.el-image) { display: block; width: 100%; height: 110px; }
.file-placeholder { display: grid; width: 100%; height: 100%; place-items: center; color: #98a2b3; background: #f8fafc; font-size: 24px; }
.attachment-card span { display: block; overflow: hidden; padding: 8px; text-overflow: ellipsis; white-space: nowrap; }
@media (max-width: 768px) { .attachment-picker-dialog { width: calc(100% - 24px); }.attachment-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }.attachment-card :deep(.el-image) { height: 120px; } }
</style>

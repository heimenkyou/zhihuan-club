<template>
  <el-dialog v-model="visible" title="选择图片" width="820px" destroy-on-close>
    <div class="toolbar">
      <el-upload :show-file-list="false" :http-request="handleUpload" accept="image/*">
        <el-button type="primary" :loading="uploading">上传图片</el-button>
      </el-upload>
    </div>
    <el-empty v-if="attachments.length === 0 && !loading" description="暂无附件" />
    <div v-else v-loading="loading" class="attachment-grid">
      <button
        v-for="attachment in attachments"
        :key="attachment.id"
        type="button"
        class="attachment-card"
        :class="{ selected: selectedUrls.includes(attachment.url) }"
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
  </el-dialog>
</template>

<script setup>
import { computed, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { getAttachments, uploadImage } from "@/services/attachmentService";

const props = defineProps({
	modelValue: { type: [String, Array], default: "" },
	multiple: Boolean,
});
const emit = defineEmits(["update:modelValue"]);
const visible = defineModel("visible", { default: false });
const attachments = ref([]);
const loading = ref(false);
const uploading = ref(false);
const page = ref({ current: 1, size: 24, total: 0 });
const selectedUrls = computed(() =>
	props.multiple ? props.modelValue || [] : [props.modelValue],
);
/** 加载可选择的附件。 */
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
		ElMessage.error(error.message || "加载图片失败");
	} finally {
		loading.value = false;
	}
};

/** 将图片 URL 写入调用方字段。 */
const selectAttachment = (attachment) => {
	if (!props.multiple) {
		emit("update:modelValue", attachment.url);
		visible.value = false;
		return;
	}
	const urls = [...selectedUrls.value];
	const index = urls.indexOf(attachment.url);
	if (index === -1) urls.push(attachment.url);
	else urls.splice(index, 1);
	emit("update:modelValue", urls);
};

/** 上传完成后自动选中图片。 */
const handleUpload = async ({ file }) => {
	uploading.value = true;
	try {
		const attachment = await uploadImage(file);
		attachments.value.unshift(attachment);
		page.value.total += 1;
		selectAttachment(attachment);
		ElMessage.success("图片上传成功");
	} catch (error) {
		ElMessage.error(error.message || "图片上传失败");
	} finally {
		uploading.value = false;
	}
};

watch(visible, (value) => {
	if (value) loadAttachments();
});
</script>

<style scoped>
.toolbar { display: flex; justify-content: flex-end; margin-bottom: 16px; }
.attachment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; margin-bottom: 20px; }
.attachment-card { overflow: hidden; padding: 0; color: inherit; text-align: left; background: var(--el-bg-color); border: 1px solid var(--el-border-color); border-radius: 6px; cursor: pointer; }
.attachment-card.selected { border: 2px solid var(--el-color-primary); }
.attachment-card :deep(.el-image) { display: block; width: 100%; height: 110px; }
.file-placeholder { display: grid; width: 100%; height: 100%; place-items: center; color: #98a2b3; background: #f8fafc; font-size: 24px; }
.attachment-card span { display: block; overflow: hidden; padding: 8px; text-overflow: ellipsis; white-space: nowrap; }
</style>

<template>
  <div class="attachment-management">
    <el-card>
      <template #header>
        <div class="header">
          <span>附件库</span>
          <el-upload :show-file-list="false" :http-request="handleUpload" accept="image/*">
            <el-button type="primary" :loading="uploading">上传图片</el-button>
          </el-upload>
        </div>
      </template>

      <el-empty v-if="attachments.length === 0 && !loading" description="暂无附件" />
      <div v-else v-loading="loading" class="attachment-grid">
        <article v-for="attachment in attachments" :key="attachment.id" class="attachment-card">
          <el-image :src="attachment.url" fit="cover" :preview-src-list="[attachment.url]" preview-teleported />
          <div class="attachment-info">
            <span :title="attachment.originalName">{{ attachment.originalName }}</span>
            <small>{{ formatSize(attachment.size) }}</small>
          </div>
          <div class="attachment-actions">
            <el-tag v-if="attachment.refId" size="small" type="info">已引用</el-tag>
            <el-button v-else link type="danger" @click="removeAttachment(attachment)">删除</el-button>
          </div>
        </article>
      </div>

      <el-pagination
        v-if="page.total > page.size"
        v-model:current-page="page.current"
        :page-size="page.size"
        layout="total, prev, pager, next"
        :total="page.total"
        @current-change="loadAttachments"
      />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteAttachment, getAttachments, uploadImage } from '@/services/attachmentService'

const attachments = ref([])
const loading = ref(false)
const uploading = ref(false)
const page = ref({ current: 1, size: 24, total: 0 })

/** 加载附件分页数据。 */
const loadAttachments = async () => {
  loading.value = true
  try {
    const data = await getAttachments(page.value)
    attachments.value = data.records || []
    page.value = { ...page.value, ...data }
  } catch (error) {
    ElMessage.error(error.message || '加载附件失败')
  } finally {
    loading.value = false
  }
}

/** 上传图片并刷新附件列表。 */
const handleUpload = async ({ file }) => {
  uploading.value = true
  try {
    await uploadImage(file)
    ElMessage.success('图片上传成功')
    await loadAttachments()
  } catch (error) {
    ElMessage.error(error.message || '图片上传失败')
  } finally {
    uploading.value = false
  }
}

/** 删除未引用附件。 */
const removeAttachment = async attachment => {
  await ElMessageBox.confirm(`确定删除“${attachment.originalName}”吗？`, '删除附件', { type: 'warning' })
  try {
    await deleteAttachment(attachment.id)
    ElMessage.success('附件已删除')
    await loadAttachments()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error(error.message || '删除附件失败')
  }
}

/** 格式化字节数。 */
const formatSize = size => `${(size / 1024 / 1024).toFixed(2)} MB`

onMounted(loadAttachments)
</script>

<style scoped>
.attachment-management { padding: 20px; }
.header { display: flex; align-items: center; justify-content: space-between; }
.attachment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 16px; margin-bottom: 20px; }
.attachment-card { overflow: hidden; border: 1px solid var(--el-border-color); border-radius: 6px; }
.attachment-card :deep(.el-image) { display: block; width: 100%; height: 130px; }
.attachment-info, .attachment-actions { display: flex; justify-content: space-between; gap: 8px; padding: 8px; }
.attachment-info span { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.attachment-info small { flex: none; color: var(--el-text-color-secondary); }
</style>

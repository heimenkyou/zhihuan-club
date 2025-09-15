<template>
  <div class="project-edit-container full-width-container">
    <el-card class="full-width-card">
      <template #header>
        <div class="card-header">
          <span>{{ isEditMode ? "编辑项目" : "添加项目" }}</span>
          <el-button @click="handleBack" class="back-btn">
            <el-icon><ArrowLeft /></el-icon>
            返回项目列表
          </el-button>
        </div>
      </template>

      <el-form
        ref="projectFormRef"
        :model="projectForm"
        :rules="rules"
        label-width="100px"
      >
        <!-- 基本信息 -->
        <el-form-item prop="title">
          <el-input
            v-model="projectForm.title"
            placeholder="请输入项目标题"
            clearable
            show-word-limit
            maxlength="100"
          />
        </el-form-item>
        <el-form-item prop="category">
          <el-select
            v-model="projectForm.category"
            placeholder="请选择项目分类"
            clearable
          >
            <el-option label="Web开发" value="Web开发" />
            <el-option label="人工智能" value="人工智能" />
            <el-option label="移动应用" value="移动应用" />
            <el-option label="数据科学" value="数据科学" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item prop="timeRange">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY.MM"
            value-format="YYYY.MM"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item prop="briefIntro">
          <el-input
            v-model="projectForm.briefIntro"
            placeholder="请输入项目简介"
            type="textarea"
            :rows="3"
            show-word-limit
            maxlength="200"
          />
        </el-form-item>
        <el-form-item prop="coverImage">
          <div class="upload-container">
            <div class="upload-main">
              <el-upload
                class="avatar-uploader"
                :show-file-list="false"
                :before-upload="beforeUpload"
                :http-request="handleUpload"
                accept="image/*"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
              >
                <img
                  v-if="projectForm.coverImage"
                  :src="projectForm.coverImage"
                  class="avatar"
                />
                <div v-else class="upload-placeholder">
                  <el-icon><Plus /></el-icon>
                  <div>点击上传封面图片</div>
                </div>
              </el-upload>

              <div class="upload-actions">
                <el-button type="text" @click="showImageLibrary"
                  >从图库选择</el-button
                >
                <el-input
                  v-model="projectForm.coverImage"
                  placeholder="或输入图片URL"
                  style="width: 100%"
                  clearable
                />
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item prop="descriptionMd">
          <div class="markdown-editor-container">
            <div class="editor-tabs">
              <el-tabs v-model="activeTab" type="card">
                <el-tab-pane label="编辑" name="edit" />
                <el-tab-pane label="预览" name="preview" />
                <el-tab-pane label="分屏" name="split" />
              </el-tabs>
            </div>
            <div v-if="activeTab === 'edit'" class="editor-full">
              <v-md-editor
                v-model="projectForm.descriptionMd"
                :disabled-menus="['image']"
                height="500px"
                class="markdown-editor"
              />
            </div>
            <div v-else-if="activeTab === 'preview'" class="editor-full">
              <div class="markdown-preview">
                <v-md-editor
                  :value="projectForm.descriptionMd"
                  mode="preview"
                  height="500px"
                />
              </div>
            </div>
            <div v-else class="editor-split">
              <div class="editor-left">
                <v-md-editor
                  v-model="projectForm.descriptionMd"
                  :disabled-menus="['image']"
                  height="500px"
                  class="markdown-editor"
                />
              </div>
              <div class="editor-right">
                <div class="markdown-preview">
                  <v-md-editor
                    :value="projectForm.descriptionMd"
                    mode="preview"
                    height="500px"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-form-item>

        <!-- 技术栈管理 -->
        <el-form-item label="技术栈">
          <el-input
            v-model="techStackInput"
            placeholder="请输入技术名称"
            style="width: 200px; margin-right: 10px"
            @keyup.enter="addTechStack"
          />
          <el-button type="primary" size="small" @click="addTechStack"
            >添加</el-button
          >

          <!-- 技术栈标签展示区域 - 确保编辑时显示已添加的技术栈 -->
          <div
            class="tech-stack-list mt-3"
            v-if="
              projectForm.techStackTags && projectForm.techStackTags.length > 0
            "
          >
            <el-tag
              v-for="(tech, index) in projectForm.techStackTags"
              :key="index"
              closable
              @close="removeTechStack(index)"
              style="margin-right: 10px; margin-bottom: 10px"
              class="tech-tag"
            >
              {{ tech }}
            </el-tag>
          </div>

          <!-- 空状态提示 -->
          <div v-else class="no-tech-stack-tip text-gray-500 text-sm mt-3">
            暂无技术栈，请添加
          </div>
        </el-form-item>

        <!-- 团队成员管理 -->
        <el-form-item label="团队成员">
          <div v-if="projectForm.teamDivision" class="team-members-list">
            <div
              v-for="(member, index) in projectForm.teamDivision"
              :key="index"
              class="team-member-item"
            >
              <el-input
                v-model="member.name"
                placeholder="成员姓名"
                style="width: 150px; margin-right: 10px"
              />
              <el-input
                v-model="member.role"
                placeholder="成员角色"
                style="width: 150px; margin-right: 10px"
              />
              <el-button
                type="danger"
                size="small"
                @click="removeTeamMember(index)"
                :disabled="projectForm.teamDivision!.length <= 1"
              >
                移除
              </el-button>
            </div>
            <el-button
              type="primary"
              size="small"
              class="mt-3"
              @click="addTeamMember"
            >
              添加成员
            </el-button>
          </div>
        </el-form-item>

        <!-- 轮播图管理 -->
        <el-form-item label="轮播图片">
          <div class="media-resources-container">
            <!-- 已选择的媒体资源列表 -->
            <div
              v-if="selectedMediaResources.length > 0"
              class="selected-media-list"
            >
              <div
                v-for="media in selectedMediaResources"
                :key="media.id"
                class="selected-media-item"
              >
                <img
                  :src="media.url"
                  alt="预览"
                  class="selected-media-thumbnail"
                />
                <div class="selected-media-info">
                  <div class="selected-media-title">
                    {{ media.title || "无标题" }}
                  </div>
                  <div class="selected-media-desc">
                    {{ media.description || "无描述" }}
                  </div>
                </div>
                <el-button
                  type="danger"
                  size="small"
                  @click="removeSelectedMedia(media.id)"
                >
                  移除
                </el-button>
              </div>
            </div>
            <div v-else class="no-selected-media text-gray-500 text-sm mb-3">
              暂无轮播图，请从图库选择或上传
            </div>

            <!-- 操作按钮 -->
            <div class="media-actions">
              <el-button type="primary" size="small" @click="showImageLibrary">
                从图库选择
              </el-button>
              <el-button
                type="success"
                size="small"
                @click="showMediaUploadDialog = true"
              >
                上传新图片
              </el-button>
            </div>

            <div class="form-tip text-gray-500 text-sm mt-1">
              请至少添加一张轮播图片
            </div>
          </div>
        </el-form-item>

        <!-- 奖项管理 -->
        <el-form-item label="获奖情况">
          <div class="award-management">
            <el-input
              v-model="awardIdsInput"
              placeholder="请输入奖项ID，用逗号分隔"
              style="width: 100%"
            />
            <el-button
              type="primary"
              size="small"
              class="mt-3"
              @click="parseAwardIds"
            >
              确认奖项ID
            </el-button>
            <div class="form-tip text-gray-500 text-sm mt-1">
              请输入与奖项系统中对应的ID
            </div>
          </div>
        </el-form-item>
      </el-form>

      <div class="form-actions">
        <el-button @click="handleBack">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </el-card>

    <!-- 图片图库对话框 -->
    <el-dialog v-model="imageLibraryVisible" title="选择图片" width="800px">
      <div class="image-library">
        <div
          class="image-item"
          v-for="image in availableMediaResources"
          :key="image.id"
          :class="{
            selected: projectForm.mediaResourceIds?.includes(image.id),
          }"
        >
          <img :src="image.url" @click="selectMediaResource(image)" />
          <div class="image-info">
            <div class="image-title">{{ image.title || "无标题" }}</div>
            <div class="image-actions">
              <el-button
                v-if="!projectForm.mediaResourceIds?.includes(image.id)"
                type="text"
                size="small"
                @click.stop="selectMediaResource(image)"
              >
                选择
              </el-button>
              <el-button v-else type="text" size="small" style="color: #67c23a">
                已选择
              </el-button>
            </div>
          </div>
        </div>
        <div class="no-images" v-if="availableMediaResources.length === 0">
          暂无图片，请先上传
        </div>
      </div>
      <template #footer>
        <el-button @click="imageLibraryVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 媒体资源上传对话框 -->
    <el-dialog v-model="showMediaUploadDialog" title="上传图片" width="600px">
      <el-form ref="uploadFormRef">
        <el-form-item label="图片标题">
          <el-input v-model="uploadTitle" placeholder="请输入图片标题" />
        </el-form-item>
        <el-form-item label="图片描述">
          <el-input
            v-model="uploadDescription"
            type="textarea"
            placeholder="请输入图片描述"
          />
        </el-form-item>
        <el-form-item label="选择图片">
          <el-upload
            ref="uploadRef"
            class="upload-demo"
            :before-upload="beforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :auto-upload="false"
            accept="image/*"
          >
            <el-button slot="trigger" type="primary">选择图片</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showMediaUploadDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmUpload">确认上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, computed } from "vue"
import { useRouter, useRoute } from "vue-router"
import { ElMessage, ElForm } from "element-plus"
import { ArrowLeft, Plus } from "@element-plus/icons-vue"
import {
  getProject,
  createProject,
  updateProject,
  type Project,
  // 添加新导入的类型和函数
  type MediaResource,
  getUnreferencedMedia,
  // deleteMedia,
  uploadMedia,
} from "../../../services/adminService"
// 移除本地导入，依赖全局注册的组件
// import VMdEditor from '@kangc/v-md-editor'
// import { getProjects } from "../../../services/projectService"
const router = useRouter()
const route = useRoute()
const projectId = computed(() => {
  const id = route.params.id
  return id ? Number(id) : null
})

const isEditMode = computed(() => projectId.value !== null)

// 表单引用
const projectFormRef = ref<InstanceType<typeof ElForm> | null>(null)

// 媒体资源相关状态
const availableMediaResources = ref<MediaResource[]>([])
const selectedMediaResources = ref<MediaResource[]>([])
const imageLibraryVisible = ref(false)
const showMediaUploadDialog = ref(false)

// 奖项ID输入处理
const awardIdsInput = ref("")

// 日期范围选择
const dateRange = ref<string[]>([])

// Markdown编辑器相关
const activeTab = ref<string>("edit")
const techStackInput = ref("")

// 上传表单相关状态
const uploadTitle = ref("")
const uploadDescription = ref("")
const uploadRef = ref<any>(null)
const uploadFormRef = ref<any>(null)

// 项目表单 - 更新为新的数据结构
const projectForm = reactive<Partial<Project>>({
  title: "",
  category: "Web开发",
  timeRange: "",
  briefIntro: "",
  descriptionMd: "",
  coverImage: "",
  techStackTags: [] as string[],
  teamDivision: [{ name: "", role: "" }],
  // 替换为媒体资源ID数组
  mediaResourceIds: [] as number[],
  // 添加奖项ID数组
  awardIds: [] as number[],
  createTime: new Date().toISOString(),
  updateTime: new Date().toISOString(),
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: "请输入项目标题", trigger: "blur" }],
  category: [{ required: true, message: "请选择项目分类", trigger: "blur" }],
  timeRange: [
    { required: true, message: "请输入开发时间范围", trigger: "blur" },
  ],
  briefIntro: [{ required: true, message: "请输入项目简介", trigger: "blur" }],
  descriptionMd: [
    { required: true, message: "请输入项目详细描述", trigger: "blur" },
  ],
  coverImage: [
    { required: true, message: "请上传项目展示图片", trigger: "blur" },
  ],
  // 自定义验证：确保至少有一个轮播图
  mediaResourceIds: [
    {
      validator: (_: unknown, value: any, callback: any) => {
        if (!value || value.length === 0) {
          callback(new Error("请至少添加一张轮播图"))
        } else {
          callback()
        }
      },
      trigger: "submit",
    },
  ],
}

// 提交表单
const submitForm = async () => {
  if (!projectFormRef.value) return

  try {
    // 自定义验证轮播图
    if (
      !projectForm.mediaResourceIds ||
      projectForm.mediaResourceIds.length === 0
    ) {
      ElMessage.error("请至少添加一张轮播图")
      return
    }

    await projectFormRef.value.validate()

    // 准备提交数据，确保所有字段都符合API要求
    const submitData = {
      ...projectForm,
      // 确保所有必填字段都有值
      type: projectForm.type || "project",
      // 清理封面图片URL
      coverImage: projectForm.coverImage
        ? projectForm.coverImage.replace(/[`\s]/g, "")
        : "",
    } as Omit<Project, "id">

    // 打印提交数据，方便调试
    console.log("提交数据:", JSON.stringify(submitData, null, 2))

    if (isEditMode.value && projectId.value !== null) {
      // 编辑项目
      await updateProject(projectId.value, submitData)
      ElMessage.success("更新项目成功")
    } else {
      // 添加项目
      await createProject(submitData)
      ElMessage.success("添加项目成功")
    }

    // 返回项目列表
    handleBack()
  } catch (error) {
    ElMessage.error(isEditMode.value ? "更新项目失败" : "添加项目失败")
    console.error("提交表单失败:", error)
  }
}

// 日期范围变化处理
const handleDateRangeChange = (dates?: string[]) => {
  if (dates && dates.length === 2) {
    projectForm.timeRange = `${dates[0]}-${dates[1]}`
  }
}

// 图片上传前检查
const beforeUpload = (file: File) => {
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过 2MB!")
  }
  return isLt2M
}

// 处理图片上传
const handleUpload = (options: any) => {
  const { onSuccess } = options
  // 模拟图片上传过程
  const mockImageUrl = `https://picsum.photos/id/${Math.floor(
    Math.random() * 100
  )}/400/300`
  
  setTimeout(() => {
    onSuccess({ url: mockImageUrl })
  }, 1000)
  
  // 返回一个Promise以满足UploadRequestHandler类型要求
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ url: mockImageUrl })
    }, 1000)
  })
}

// 图片上传成功处理
const handleUploadSuccess = (response: any) => {
  projectForm.coverImage = response
  ElMessage.success("图片上传成功")
}

// 图片上传失败处理
const handleUploadError = () => {
  ElMessage.error("图片上传失败，请重试")
}

// 显示图片图库
const showImageLibrary = () => {
  loadUnreferencedMedia()
  imageLibraryVisible.value = true
}

// 从图库选择图片（用于封面）
// const selectImageFromLibrary = (image: MediaResource) => {
//   projectForm.coverImage = image.url
//   imageLibraryVisible.value = false
//   ElMessage.success("图片已选择")
// }

// 添加技术栈
const addTechStack = () => {
  const techValue = techStackInput.value.trim()
  if (techValue && projectForm.techStackTags) {
    const exists = projectForm.techStackTags.some((tech) => tech === techValue)
    if (!exists) {
      projectForm.techStackTags.push(techValue)
      techStackInput.value = ""
    } else {
      ElMessage.warning("该技术已存在")
    }
  }
}

// 移除技术栈
const removeTechStack = (index: number) => {
  if (projectForm.techStackTags) {
    projectForm.techStackTags.splice(index, 1)
  }
}

// 添加团队成员
const addTeamMember = () => {
  if (projectForm.teamDivision) {
    projectForm.teamDivision.push({ name: "", role: "" })
  }
}

// 移除团队成员
const removeTeamMember = (index: number) => {
  if (projectForm.teamDivision && projectForm.teamDivision.length > 1) {
    projectForm.teamDivision.splice(index, 1)
  }
}

// 解析奖项ID
const parseAwardIds = () => {
  if (awardIdsInput.value) {
    projectForm.awardIds = awardIdsInput.value
      .split(",")
      .map((id) => parseInt(id.trim()))
      .filter((id) => !isNaN(id))
  }
}

// 加载未引用的媒体资源
const loadUnreferencedMedia = async () => {
  try {
    const media = await getUnreferencedMedia()
    availableMediaResources.value = media

    // 将已选择的媒体资源也加入列表，方便查看
    if (selectedMediaResources.value.length > 0) {
      availableMediaResources.value = [
        ...availableMediaResources.value,
        ...selectedMediaResources.value,
      ]
      // 去重
      const seen = new Set()
      availableMediaResources.value = availableMediaResources.value.filter(
        (media) => {
          const duplicate = seen.has(media.id)
          seen.add(media.id)
          return !duplicate
        }
      )
    }
  } catch (error) {
    ElMessage.error("加载媒体资源失败")
    console.error("加载媒体资源失败:", error)
  }
}

// 选择媒体资源（用于轮播图）
const selectMediaResource = (media: MediaResource) => {
  if (!projectForm.mediaResourceIds) {
    projectForm.mediaResourceIds = []
  }

  if (!projectForm.mediaResourceIds.includes(media.id)) {
    projectForm.mediaResourceIds.push(media.id)
    selectedMediaResources.value.push(media)
    ElMessage.success("已添加到轮播图")
  } else {
    ElMessage.warning("该图片已在轮播图中")
  }
}

// 移除已选媒体资源
const removeSelectedMedia = (mediaId: number) => {
  if (projectForm.mediaResourceIds) {
    projectForm.mediaResourceIds = projectForm.mediaResourceIds.filter(
      (id) => id !== mediaId
    )
    selectedMediaResources.value = selectedMediaResources.value.filter(
      (media) => media.id !== mediaId
    )
    ElMessage.success("已从轮播图移除")
  }
}

// 上传新的媒体资源
const handleMediaUpload = async (
  file: File,
  title: string,
  description: string
) => {
  try {
    const newMedia = await uploadMedia({ file, title, description })
    availableMediaResources.value.push(newMedia)
    selectedMediaResources.value.push(newMedia)
    projectForm.mediaResourceIds?.push(newMedia.id)
    showMediaUploadDialog.value = false
    ElMessage.success("媒体资源上传成功")
  } catch (error) {
    ElMessage.error("媒体资源上传失败")
    console.error("上传媒体资源失败:", error)
  }
}

// 确认上传
const confirmUpload = async () => {
  const uploader = uploadRef.value?.uploadFiles
  if (uploader && uploader.length > 0) {
    const file = uploader[0].raw
    if (file) {
      await handleMediaUpload(file, uploadTitle.value, uploadDescription.value)
      // 重置表单
      uploadTitle.value = ""
      uploadDescription.value = ""
      uploadRef.value.clearFiles()
    }
  } else {
    ElMessage.warning("请先选择要上传的图片")
  }
}

// 删除未引用的媒体资源
// const handleDeleteMedia = async (mediaId: number) => {
//   try {
//     await deleteMedia(mediaId)
//     availableMediaResources.value = availableMediaResources.value.filter(
//       (media) => media.id !== mediaId
//     )
//     ElMessage.success("媒体资源已删除")
//   } catch (error) {
//     ElMessage.error("媒体资源删除失败")
//     console.error("删除媒体资源失败:", error)
//   }
// }

// 加载项目详情（编辑模式）
const loadProjectDetail = async () => {
  if (!isEditMode.value || projectId.value === null) return

  try {
    //获取项目详细
    const projectDetail = await getProject(projectId.value)

    // 清理图片URL中的空格和反引号
    if (projectDetail.coverImage) {
      projectDetail.coverImage = projectDetail.coverImage.replace(/[`\s]/g, "")
    }

    // 应用清理后的数据到表单
    Object.assign(projectForm, projectDetail)

    // 处理日期范围
    if (projectDetail.timeRange) {
      const rangeParts = projectDetail.timeRange.split("-")
      if (rangeParts.length === 2) {
        dateRange.value = [rangeParts[0], rangeParts[1]]
      }
    }

    // 确保所有数组字段都已初始化
    if (!Array.isArray(projectForm.techStackTags)) {
      projectForm.techStackTags = []
    }

    if (
      !Array.isArray(projectForm.teamDivision) ||
      projectForm.teamDivision.length === 0
    ) {
      projectForm.teamDivision = [{ name: "", role: "" }]
    }

    if (!Array.isArray(projectForm.mediaResourceIds)) {
      projectForm.mediaResourceIds = []
    }

    if (!Array.isArray(projectForm.awardIds)) {
      projectForm.awardIds = []
    }

    // 如果有awardIds，设置到输入框
    if (
      Array.isArray(projectForm.awardIds) &&
      projectForm.awardIds.length > 0
    ) {
      awardIdsInput.value = projectForm.awardIds.join(",")
    }

    // 加载已选择的媒体资源详情
    await loadUnreferencedMedia()
  } catch (error) {
    const errorMessage = error instanceof Error ? error.message : "未知错误"
    ElMessage.error(`获取项目详情失败: ${errorMessage}`)
    console.error("获取项目详情失败:", error)
  }
}

// 返回项目列表
const handleBack = () => {
  router.push("/admin/projects")
}

// 页面加载时初始化数据
onMounted(() => {
  loadProjectDetail()
  loadUnreferencedMedia()
})

// 添加对编辑器实例的引用，用于清理
const editorRefs = ref<Array<HTMLElement | null>>([])

// 页面卸载时清理资源
onUnmounted(() => {
  // 清理所有可能的 ResizeObserver 实例
  if (typeof window !== "undefined" && window.ResizeObserver) {
    // 尝试通过覆盖 ResizeObserver 原型来清理所有实例
    const originalObserve = ((window.ResizeObserver as any).prototype.observe(
      window.ResizeObserver as any
    ).prototype.observe = function (target: Element, options?: any) {
      try {
        originalObserve.call(this, target, options)
      } catch (e) {
        console.warn("ResizeObserver 观察失败:", e)
      }
    })
  }

  // 清理编辑器相关引用
  editorRefs.value = []
})
</script>

<style scoped>
.project-edit-container {
  padding: 20px;
}

.full-width-container {
  max-width: 100%;
}

.full-width-card {
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.back-btn {
  background-color: #6c757d;
  color: white;
}

.back-btn:hover {
  background-color: #5a6268;
  color: white;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
  gap: 10px;
}

/* 图片上传样式 */
.upload-container {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 20px;
}

.upload-main {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  flex-wrap: wrap;
}

.avatar-uploader .avatar {
  width: 250px;
  height: 250px;
  display: block;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.avatar-uploader .avatar:hover {
  transform: scale(1.02);
}

.upload-placeholder {
  width: 250px;
  height: 250px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  background-color: #fafafa;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-placeholder:hover {
  border-color: #4096ff;
  color: #4096ff;
  background-color: #f0f7ff;
}

.upload-placeholder .el-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.upload-placeholder div {
  font-size: 14px;
}

/* 图库按钮和URL输入框样式优化 */
.upload-actions {
  display: flex;
  flex-direction: column;
  gap: 15px;
  flex: 1;
  min-width: 300px;
}

/* 图片图库样式优化 */
.image-library {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 12px;
  max-height: 450px;
  overflow-y: auto;
  padding: 15px;
}

.image-item {
  cursor: pointer;
  border: 2px solid transparent;
  border-radius: 6px;
  transition: all 0.3s ease;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: relative;
}

.image-item:hover {
  border-color: #4096ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.image-item.selected {
  border-color: #4096ff;
}

.image-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
}

.image-info {
  padding: 10px;
  background-color: rgba(0, 0, 0, 0.05);
}

.image-title {
  font-size: 12px;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.image-actions {
  display: flex;
  justify-content: flex-end;
}

.no-images {
  grid-column: 1 / -1;
  text-align: center;
  color: #909399;
  padding: 60px 0;
  font-size: 14px;
}

/* Markdown编辑器样式优化 - 增加宽度和高度 */
.markdown-editor-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  width: 100%; /* 确保编辑器容器占满宽度 */
}

.editor-full {
  height: 600px; /* 增加高度以提供更好的编辑体验 */
}

.editor-split {
  display: flex;
  height: 600px;
  width: 100%; /* 确保分屏模式占满宽度 */
}

.editor-left,
.editor-right {
  flex: 1;
  border-right: 1px solid #e4e7ed;
}

.editor-right {
  border-right: none;
  border-left: 1px solid #e4e7ed;
}

.markdown-editor {
  height: 100%;
}

.markdown-preview {
  height: 100%;
  overflow-y: auto;
  padding: 20px;
  background-color: #fafafa;
}

/* 技术栈列表样式优化 */
.tech-stack-list {
  margin-top: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 团队成员样式 */
.team-members-list {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  background-color: #fafafa;
}

.team-member-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  background-color: white;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

/* 媒体资源样式 */
.media-resources-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  background-color: #fafafa;
}

.selected-media-list {
  max-height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
}

.selected-media-item {
  display: flex;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  background-color: white;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.selected-media-thumbnail {
  width: 100px;
  height: 75px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.selected-media-info {
  flex: 1;
  min-width: 0;
}

.selected-media-title {
  font-weight: 500;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.selected-media-desc {
  color: #909399;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.media-actions {
  display: flex;
  gap: 10px;
}

.no-selected-media {
  text-align: center;
  padding: 40px 0;
}

/* 奖项样式 */
.award-management {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  background-color: #fafafa;
}

.form-tip {
  color: #909399;
  font-size: 12px;
  margin-top: 5px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .project-edit-container {
    padding: 10px;
  }

  .upload-main {
    flex-direction: column;
    align-items: stretch;
  }

  .avatar-uploader .avatar,
  .upload-placeholder {
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
  }

  .editor-full,
  .editor-split {
    height: 400px;
  }

  .team-member-item,
  .selected-media-item {
    flex-direction: column;
    align-items: stretch;
  }

  .team-member-item .el-input,
  .team-member-item .el-select {
    margin-right: 0 !important;
    margin-bottom: 10px;
  }

  .selected-media-thumbnail {
    margin-right: 0;
    margin-bottom: 10px;
    max-width: 100%;
  }
}
</style>

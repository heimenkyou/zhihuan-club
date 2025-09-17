<template>
  <div class="project-edit-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEditMode ? '编辑项目' : '创建新项目' }}</span>
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
        label-width="120px"
        v-loading="loading"
      >
        <!-- 基本信息 -->
        <el-form-item label="项目标题" prop="title">
          <el-input
            v-model="projectForm.title"
            placeholder="请输入项目标题"
            clearable
            show-word-limit
            maxlength="100"
          />
        </el-form-item>

        <el-form-item label="项目分类" prop="category">
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

        <el-form-item label="时间范围" prop="timeRange">
          <el-date-picker
            v-model="dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份 (如: 2024-11)"
            end-placeholder="结束月份 (如: 2025-01)"
            format="YYYY年MM月"
            value-format="YYYY.MM"
            @change="handleDateRangeChange"
          />
        </el-form-item>

        <el-form-item label="项目简介" prop="briefIntro">
          <el-input
            v-model="projectForm.briefIntro"
            placeholder="请输入项目简介"
            type="textarea"
            :rows="3"
            show-word-limit
            maxlength="200"
          />
        </el-form-item>

        <!-- 轮播图管理 -->
        <el-form-item label="轮播图片" prop="mediaResourceIds">
          <div class="carousel-management">
            <!-- 当前封面显示 -->
            <div class="current-cover" v-if="projectForm.coverImage">
              <div class="cover-info">
                <span>当前封面：</span>
                <img
                  :src="projectForm.coverImage"
                  alt="封面图"
                  class="cover-thumbnail"
                />
                <el-button
                  type="danger"
                  size="small"
                  @click="projectForm.coverImage = ''"
                >
                  清除封面
                </el-button>
              </div>
            </div>

            <div class="carousel-images" v-if="carouselImages.length > 0">
              <div
                v-for="(image, index) in carouselImages"
                :key="image.id"
                class="carousel-image-item"
                :class="{ 'is-cover': image.id === coverImageId }"
              >
                <img :src="image.url" :alt="image.title || '轮播图'" />
                <div class="image-overlay">
                  <div class="overlay-top">
                    <el-tag
                      v-if="image.id === coverImageId"
                      type="success"
                      size="small"
                    >
                      封面图
                    </el-tag>
                    <el-tag
                      v-else
                      type="info"
                      size="small"
                      @click="setAsCover(image)"
                    >
                      设为封面
                    </el-tag>
                  </div>
                  <div class="overlay-bottom">
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeCarouselImage(index)"
                    >
                      移除
                    </el-button>
                  </div>
                </div>
                <div class="image-info">
                  <div class="image-title">{{ image.title || '无标题' }}</div>
                  <div class="image-desc">
                    {{ image.description || '无描述' }}
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="no-carousel-images">
              <el-empty description="暂无轮播图">
                <el-button type="primary" @click="showUploadDialog = true">
                  上传图片
                </el-button>
                <el-button type="info" @click="openImageLibrary">
                  从图库选择
                </el-button>
              </el-empty>
            </div>

            <div class="carousel-actions" v-if="carouselImages.length > 0">
              <el-button type="primary" @click="showUploadDialog = true">
                <el-icon><Plus /></el-icon>
                上传图片
              </el-button>
              <el-button type="info" @click="openImageLibrary">
                <el-icon><Picture /></el-icon>
                从图库选择
              </el-button>
            </div>
          </div>
        </el-form-item>

        <!-- Markdown编辑器 -->
        <el-form-item label="项目详情" prop="descriptionMd">
          <div class="markdown-editor-container w-full">
            <div class="editor-tabs">
              <el-radio-group v-model="editorMode" size="small">
                <el-radio-button label="edit">编辑</el-radio-button>
                <el-radio-button label="preview">预览</el-radio-button>
                <el-radio-button label="split">分屏</el-radio-button>
              </el-radio-group>
            </div>

            <div class="editor-content">
              <div v-if="editorMode === 'edit'" class="editor-pane">
                <v-md-editor
                  v-model="projectForm.descriptionMd"
                  :disabled-menus="[]"
                  height="400px"
                  @upload-image="handleImageUpload"
                />
              </div>

              <div v-else-if="editorMode === 'preview'" class="preview-pane">
                <v-md-editor
                  :value="projectForm.descriptionMd"
                  mode="preview"
                  height="400px"
                />
              </div>

              <div v-else class="split-pane">
                <div class="split-left">
                  <v-md-editor
                    v-model="projectForm.descriptionMd"
                    :disabled-menus="[]"
                    height="400px"
                    @upload-image="handleImageUpload"
                  />
                </div>
                <div class="split-right">
                  <v-md-editor
                    :value="projectForm.descriptionMd"
                    mode="preview"
                    height="400px"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-form-item>

        <!-- 技术栈、团队成员、获奖情况 -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="技术栈">
              <div class="tech-stack-container">
                <div class="tech-stack-input">
                  <el-input
                    v-model="techStackInput"
                    placeholder="输入技术名称后按回车添加"
                    @keyup.enter="addTechStack"
                    clearable
                  />
                  <el-button type="primary" @click="addTechStack"
                    >添加</el-button
                  >
                </div>
                <div
                  class="tech-stack-tags"
                  v-if="projectForm.techStackTags.length > 0"
                >
                  <el-tag
                    v-for="(tech, index) in projectForm.techStackTags"
                    :key="index"
                    closable
                    @close="removeTechStack(index)"
                    type="info"
                  >
                    {{ tech }}
                  </el-tag>
                </div>
                <div v-else class="no-tech-stack">
                  <span class="text-gray-500 text-sm">暂无技术栈，请添加</span>
                </div>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="团队成员">
              <div class="team-members-container">
                <div
                  class="team-members-list"
                  v-if="projectForm.teamDivision.length > 0"
                >
                  <div
                    v-for="(member, index) in projectForm.teamDivision"
                    :key="index"
                    class="team-member-item"
                  >
                    <el-input
                      v-model="member.name"
                      placeholder="成员姓名"
                      clearable
                    />
                    <el-input
                      v-model="member.role"
                      placeholder="成员角色"
                      clearable
                    />
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeTeamMember(index)"
                      :disabled="projectForm.teamDivision.length <= 1"
                    >
                      移除
                    </el-button>
                  </div>
                </div>
                <el-button type="primary" size="small" @click="addTeamMember">
                  <el-icon><Plus /></el-icon>
                  添加成员
                </el-button>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="获奖情况">
              <div class="award-management">
                <el-select
                  v-model="selectedAwardIds"
                  multiple
                  filterable
                  remote
                  reserve-keyword
                  placeholder="输入奖项关键词搜索"
                  :remote-method="searchAwards"
                  :loading="awardLoading"
                  style="width: 100%"
                  collapse-tags
                  :collapse-tags-tooltip="false"
                  :max-collapse-tags="0"
                >
                  <el-option
                    v-for="award in awardOptions"
                    :key="award.id"
                    :label="getAwardDisplayText(award.id)"
                    :value="award.id"
                  >
                    <div class="award-option">
                      <div class="award-name">
                        {{ getAwardDisplayText(award.id) }}
                      </div>
                      <div
                        class="award-detail"
                        v-if="award.winners && award.winners.length > 0"
                      >
                        获奖者: {{ award.winners.join(', ') }}
                      </div>
                    </div>
                  </el-option>
                </el-select>
                <div class="form-tip">
                  支持按竞赛名称、级别、奖项等级和获奖者搜索
                </div>
                <!-- 已选择的奖项展示 -->
                <div
                  v-if="selectedAwardIds.length > 0"
                  class="selected-awards-container mt-3"
                >
                  <div class="selected-awards-title">已选择的奖项：</div>
                  <div class="selected-awards-list">
                    <div
                      v-for="awardId in selectedAwardIds"
                      :key="awardId"
                      class="selected-award-item"
                    >
                      <span class="award-text">{{
                        getAwardDisplayText(awardId)
                      }}</span>
                      <el-button
                        type="danger"
                        size="small"
                        circle
                        @click="
                          selectedAwardIds.splice(
                            selectedAwardIds.indexOf(awardId),
                            1
                          )
                        "
                      >
                        <el-icon><Close /></el-icon>
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="form-actions">
        <el-button @click="handleBack">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">
          {{ isEditMode ? '更新项目' : '创建项目' }}
        </el-button>
        <el-button
          v-if="isEditMode"
          type="danger"
          @click="handleDelete"
          :loading="deleteLoading"
        >
          删除项目
        </el-button>
      </div>
    </el-card>

    <!-- 图片上传对话框 -->
    <el-dialog
      v-model="showUploadDialog"
      title="上传图片"
      width="600px"
      @close="resetUploadForm"
    >
      <el-form label-width="80px">
        <el-form-item label="图片标题">
          <el-input
            v-model="uploadForm.title"
            placeholder="请输入图片标题（选填）"
            clearable
          />
        </el-form-item>
        <el-form-item label="图片描述">
          <el-input
            v-model="uploadForm.description"
            type="textarea"
            placeholder="请输入图片描述（选填）"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="选择图片">
          <el-upload
            ref="uploadRef"
            class="upload-area"
            :class="{ 'has-image': uploadFileList.length > 0 }"
            :before-upload="beforeUpload"
            :auto-upload="false"
            accept="image/*"
            list-type="picture-card"
            :limit="1"
            :on-exceed="handleExceed"
            v-model:file-list="uploadFileList"
            @change="handleUploadChange"
          >
            <el-icon><Plus /></el-icon>

            <template #file="{ file }">
              <div>
                <img
                  class="el-upload-list__item-thumbnail"
                  :src="file.url"
                  alt=""
                />
                <span class="el-upload-list__item-actions">
                  <span
                    class="el-upload-list__item-preview"
                    @click="handlePictureCardPreview(file)"
                  >
                    <el-icon><ZoomIn /></el-icon>
                  </span>
                  <span
                    class="el-upload-list__item-delete"
                    @click="handleRemove(file)"
                  >
                    <el-icon><Delete /></el-icon>
                  </span>
                </span>
              </div>
            </template>
          </el-upload>

          <div class="upload-tip mt-2">
            支持上传单张图片，JPG、PNG、GIF 格式，文件大小不超过 2MB
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUploadDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="confirmUpload"
          :loading="uploadLoading"
        >
          确认上传
        </el-button>
      </template>
    </el-dialog>

    <!-- 图库选择对话框 -->
    <el-dialog
      v-model="showImageLibraryDialog"
      title="从图库选择图片"
      width="900px"
      top="5vh"
    >
      <div class="image-library">
        <div class="library-toolbar">
          <el-input
            v-model="librarySearch"
            placeholder="搜索图片标题或描述"
            clearable
            @input="filterLibrary"
            style="width: 300px"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button
            type="primary"
            @click="refreshLibrary"
            :loading="libraryLoading"
          >
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>

        <div v-loading="libraryLoading" class="library-grid">
          <div
            v-for="image in filteredLibraryImages"
            :key="image.id"
            class="library-image-item"
            :class="{
              selected: isImageSelected(image.id),
              disabled: isImageDisabled(image.id),
            }"
            @click="toggleLibraryImage(image)"
          >
            <img :src="image.url" :alt="image.title" />
            <div class="image-info">
              <div class="image-title">{{ image.title || '无标题' }}</div>
              <div class="image-desc">{{ image.description || '无描述' }}</div>
            </div>
            <div v-if="isImageSelected(image.id)" class="selected-badge">
              <el-icon><Check /></el-icon>
            </div>
          </div>
          <div
            v-if="filteredLibraryImages.length === 0"
            class="no-library-images"
          >
            <el-empty description="暂无图片" />
          </div>
        </div>

        <div class="library-actions">
          <el-button @click="showImageLibraryDialog = false">关闭</el-button>
          <el-button type="primary" @click="confirmLibrarySelection">
            确认选择 ({{ selectedLibraryImages.length }})
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="dialogVisible" title="图片预览" width="600px">
      <img
        w-full
        :src="dialogImageUrl"
        alt="预览图片"
        style="width: 100%; height: auto"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, computed, watch } from 'vue'
  import { useRouter, useRoute } from 'vue-router'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import type { FormInstance } from 'element-plus'
  import {
    ArrowLeft,
    Plus,
    Search,
    Check,
    Picture,
    Refresh,
    Delete,
    ZoomIn,
  } from '@element-plus/icons-vue'

  import {
    getUnreferencedMedia,
    uploadMedia,
    getAwards,
    getProjectForEdit,
    createProject,
    updateProject,
    deleteProject,
    type MediaResource,
    type AwardItem,
    type ProjectEditRespDTO,
  } from '../../../services/adminService'

  const router = useRouter()
  const route = useRoute()

  // 项目ID
  const projectId = computed(() => {
    const id = route.params.id
    return id ? Number(id) : null
  })

  // 是否为编辑模式
  const isEditMode = computed(() => projectId.value !== null)

  // 加载状态
  const loading = ref(false)
  const submitLoading = ref(false)
  const deleteLoading = ref(false)
  const uploadLoading = ref(false)
  const libraryLoading = ref(false)
  const awardLoading = ref(false)

  // 表单引用
  const projectFormRef = ref<FormInstance>()
  const uploadRef = ref()

  // 表单数据
  const projectForm = reactive({
    title: '',
    category: '',
    timeRange: '',
    briefIntro: '',
    descriptionMd: '',
    coverImage: '',
    techStackTags: [] as string[],
    teamDivision: [] as { name: string; role: string }[],
    mediaResourceIds: [] as number[],
    awardIds: [] as number[],
  })

  // 日期范围
  const dateRange = ref<string[]>([])

  // 轮播图数据
  const carouselImages = ref<MediaResource[]>([])
  const coverImageId = ref<number | null>(null)

  // 图库数据
  const availableMediaResources = ref<MediaResource[]>([])
  const librarySearch = ref('')
  const filteredLibraryImages = ref<MediaResource[]>([])
  const selectedLibraryImages = ref<MediaResource[]>([])

  // 上传相关
  const showUploadDialog = ref(false)
  const uploadFileList = ref<any[]>([])
  const uploadForm = reactive({
    title: '',
    description: '',
  })

  // 图片预览相关
  const dialogImageUrl = ref('')
  const dialogVisible = ref(false)

  // 图库选择
  const showImageLibraryDialog = ref(false)

  // 技术栈
  const techStackInput = ref('')

  // 奖项相关
  const awardOptions = ref<AwardItem[]>([])
  const allAwards = ref<AwardItem[]>([]) // 存储所有奖项数据
  const selectedAwardIds = ref<number[]>([])

  // Markdown编辑器模式
  const editorMode = ref('edit')

  // 表单验证规则
  const rules = {
    title: [{ required: true, message: '请输入项目标题', trigger: 'blur' }],
    category: [
      { required: true, message: '请选择项目分类', trigger: 'change' },
    ],
    timeRange: [
      { required: true, message: '请选择时间范围', trigger: 'change' },
    ],
    briefIntro: [
      { required: true, message: '请输入项目简介', trigger: 'blur' },
    ],
    descriptionMd: [
      { required: true, message: '请输入项目详情', trigger: 'blur' },
    ],
    coverImage: [
      { required: true, message: '请选择封面图片', trigger: 'change' },
    ],
    mediaResourceIds: [
      {
        validator: (_: any, value: any, callback: any) => {
          if (!value || value.length === 0) {
            callback(new Error('请至少添加一张轮播图'))
          } else {
            callback()
          }
        },
        trigger: 'submit',
      },
    ],
  }

  /**
   * 日期范围变化处理 - 格式：2024-11 - 2025-01
   */
  const handleDateRangeChange = (dates?: string[]) => {
    if (dates && dates.length === 2) {
      projectForm.timeRange = `${dates[0]} - ${dates[1]}`
    } else {
      projectForm.timeRange = ''
    }
  }

  /**
   * 加载项目详情（编辑模式）
   * 使用专门的编辑回显接口获取完整的项目数据包括关联的媒体资源和奖项
   */
  const loadProjectDetail = async () => {
    if (!isEditMode.value || !projectId.value) return

    loading.value = true
    try {
      // 使用专门的编辑回显接口获取完整数据
      const projectData: ProjectEditRespDTO = await getProjectForEdit(
        projectId.value
      )

      // 填充表单数据
      Object.assign(projectForm, {
        title: projectData.title || '',
        category: projectData.category || '',
        timeRange: projectData.timeRange || '',
        briefIntro: projectData.briefIntro || '',
        descriptionMd: projectData.descriptionMd || '',
        coverImage: projectData.coverImage || '',
        techStackTags: projectData.techStackTags || [],
        teamDivision:
          projectData.teamDivisions && projectData.teamDivisions.length > 0
            ? projectData.teamDivisions
            : [{ name: '', role: '' }],
        mediaResourceIds: projectData.mediaResources
          ? projectData.mediaResources.map(item => item.id)
          : [],
        awardIds: projectData.awards
          ? projectData.awards.map(item => item.id)
          : [],
      })

      /**
       * 处理日期范围 - 格式：2024-11 - 2025-01
       * 支持解析带空格和不带空格的格式
       */
      if (projectData.timeRange) {
        const rangeParts = projectData.timeRange.split(' - ')
        if (rangeParts.length === 2) {
          dateRange.value = [rangeParts[0].trim(), rangeParts[1].trim()]
        } else {
          // 兼容旧格式（不带空格）
          const oldRangeParts = projectData.timeRange.split('-')
          if (oldRangeParts.length === 2) {
            dateRange.value = [oldRangeParts[0].trim(), oldRangeParts[1].trim()]
          }
        }
      }

      // 设置轮播图数据
      carouselImages.value = projectData.mediaResources || []

      // 设置封面图ID - 从轮播图中找到封面图的ID
      if (projectData.coverImage && carouselImages.value.length > 0) {
        const coverImage = carouselImages.value.find(
          img => img.url === projectData.coverImage
        )
        coverImageId.value = coverImage ? coverImage.id : null
      } else {
        coverImageId.value = null
      }

      // 设置已选奖项
      selectedAwardIds.value = projectData.awards
        ? projectData.awards.map(item => item.id)
        : []

      console.log('✅ 项目数据加载完成:', {
        title: projectData.title,
        mediaResourcesCount: projectData.mediaResources?.length || 0,
        awardsCount: projectData.awards?.length || 0,
        teamMembersCount: projectData.teamDivisions?.length || 0,
      })
    } catch (error) {
      ElMessage.error(
        `获取项目详情失败: ${
          error instanceof Error ? error.message : '未知错误'
        }`
      )
      console.error('获取项目详情失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 搜索奖项
  const searchAwards = async (query: string) => {
    if (!query.trim()) {
      awardOptions.value = []
      return
    }

    awardLoading.value = true
    try {
      // 如果已经有奖项数据，直接使用本地数据过滤
      if (allAwards.value.length > 0) {
        const filtered = allAwards.value.filter(award => {
          const searchText = query.toLowerCase()
          return (
            award.competitionName?.toLowerCase().includes(searchText) ||
            award.competitionLevel?.toLowerCase().includes(searchText) ||
            award.awardLevel?.toLowerCase().includes(searchText) ||
            (award.winners &&
              award.winners.some(winner =>
                winner.toLowerCase().includes(searchText)
              ))
          )
        })
        awardOptions.value = filtered
      } else {
        // 如果没有本地数据，再从服务器查询
        const awards = await getAwards({ keyword: query })
        awardOptions.value = awards
      }
    } catch (error) {
      console.error('搜索奖项失败:', error)
      awardOptions.value = []
    } finally {
      awardLoading.value = false
    }
  }

  // 获取奖项展示文本
  const getAwardDisplayText = (awardId: number) => {
    const award = allAwards.value.find(a => a.id === awardId)
    if (!award) return '未知奖项'

    const awardDate = award.awardDate
      ? new Date(award.awardDate).toLocaleDateString()
      : null

    const winnersText = award.winners?.length
      ? ` - [${award.winners.join(', ')}]`
      : ''

    return (
      [
        award.competitionName,
        award.competitionLevel,
        award.awardLevel,
        awardDate,
      ]
        .filter(Boolean)
        .join(' - ') + winnersText
    )
  }

  // 图库相关函数
  const loadUnreferencedMediaResources = async () => {
    libraryLoading.value = true
    try {
      const media = await getUnreferencedMedia()
      availableMediaResources.value = media
      filterLibrary()
    } catch (error) {
      ElMessage.error('加载图库失败')
      console.error('加载图库失败:', error)
    } finally {
      libraryLoading.value = false
    }
  }

  const filterLibrary = () => {
    const search = librarySearch.value.toLowerCase().trim()
    if (!search) {
      filteredLibraryImages.value = availableMediaResources.value
    } else {
      filteredLibraryImages.value = availableMediaResources.value.filter(
        image =>
          (image.title || '').toLowerCase().includes(search) ||
          (image.description || '').toLowerCase().includes(search)
      )
    }
  }

  const refreshLibrary = () => {
    loadUnreferencedMediaResources()
  }

  const isImageSelected = (imageId: number) => {
    return selectedLibraryImages.value.some(img => img.id === imageId)
  }

  const isImageDisabled = (imageId: number) => {
    return projectForm.mediaResourceIds.includes(imageId)
  }

  const toggleLibraryImage = (image: MediaResource) => {
    if (isImageDisabled(image.id)) return

    const index = selectedLibraryImages.value.findIndex(
      img => img.id === image.id
    )
    if (index > -1) {
      selectedLibraryImages.value.splice(index, 1)
    } else {
      selectedLibraryImages.value.push(image)
    }
  }

  const confirmLibrarySelection = () => {
    selectedLibraryImages.value.forEach(image => {
      if (!projectForm.mediaResourceIds.includes(image.id)) {
        projectForm.mediaResourceIds.push(image.id)
        carouselImages.value.push(image)
      }
    })

    selectedLibraryImages.value = []
    showImageLibraryDialog.value = false
    ElMessage.success(
      `已添加 ${selectedLibraryImages.value.length} 张图片到轮播图`
    )
  }

  const openImageLibrary = () => {
    showImageLibraryDialog.value = true
    selectedLibraryImages.value = []
    loadUnreferencedMediaResources()
  }

  // 轮播图管理
  const setAsCover = (image: MediaResource) => {
    projectForm.coverImage = image.url
    coverImageId.value = image.id
    ElMessage.success('已设为封面图')
  }

  const removeCarouselImage = (index: number) => {
    const image = carouselImages.value[index]
    const mediaIndex = projectForm.mediaResourceIds.indexOf(image.id)

    if (mediaIndex > -1) {
      projectForm.mediaResourceIds.splice(mediaIndex, 1)
    }

    carouselImages.value.splice(index, 1)

    // 如果移除的是封面图，清空封面
    if (coverImageId.value === image.id) {
      projectForm.coverImage = ''
      coverImageId.value = null
    }

    ElMessage.success('已移除轮播图')
  }

  // 上传相关
  const beforeUpload = (file: File) => {
    const isImage = file.type.startsWith('image/')
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isImage) {
      ElMessage.error('只能上传图片文件!')
      return false
    }
    if (!isLt2M) {
      ElMessage.error('图片大小不能超过 2MB!')
      return false
    }
    return true
  }

  /**
   * 确认上传图片 - 单张上传，第一张自动设为封面
   */
  const confirmUpload = async () => {
    if (uploadFileList.value.length === 0) {
      ElMessage.warning('请先选择要上传的图片')
      return
    }

    uploadLoading.value = true
    try {
      const fileItem = uploadFileList.value[0]
      const file = fileItem.raw

      const uploadedMedia = await uploadMedia({
        file,
        title: uploadForm.title,
        description: uploadForm.description,
      })

      // 将上传的图片添加到项目
      projectForm.mediaResourceIds.push(uploadedMedia.id)
      carouselImages.value.push(uploadedMedia)

      // 如果是第一张图片，自动设为封面
      if (carouselImages.value.length === 1) {
        projectForm.coverImage = uploadedMedia.url
        coverImageId.value = uploadedMedia.id
        ElMessage.success('图片上传成功，已自动设为封面')
      } else {
        ElMessage.success('图片上传成功')
      }

      showUploadDialog.value = false
      resetUploadForm()
    } catch (error) {
      ElMessage.error('上传失败，请重试')
      console.error('上传失败:', error)
    } finally {
      uploadLoading.value = false
    }
  }

  /**
   * 处理文件变化
   */
  const handleUploadChange = (_file: any, fileList: any[]) => {
    uploadFileList.value = fileList
  }

  /**
   * 处理文件超出限制 - 替换上一个文件
   */
  const handleExceed = (files: any) => {
    // 替换现有文件
    uploadFileList.value = [files[0]]
    ElMessage.info('已替换为新选择的图片')
  }

  /**
   * 处理图片预览
   */
  const handlePictureCardPreview = (file: any) => {
    dialogImageUrl.value = file.url
    dialogVisible.value = true
  }

  /**
   * 处理图片移除
   */
  const handleRemove = (file: any) => {
    const index = uploadFileList.value.findIndex(item => item.uid === file.uid)
    if (index !== -1) {
      uploadFileList.value.splice(index, 1)
    }
  }

  const resetUploadForm = () => {
    uploadForm.title = ''
    uploadForm.description = ''
    uploadFileList.value = []
  }

  // Markdown图片上传
  const handleImageUpload = async (
    _event: any,
    insertImage: any,
    files: File[]
  ) => {
    const file = files[0]
    if (!file) return

    try {
      const uploadedMedia = await uploadMedia({
        file,
        title: 'Markdown图片',
        description: 'Markdown编辑器插入的图片',
      })

      insertImage({
        url: uploadedMedia.url,
        desc: uploadedMedia.title || '图片',
      })
    } catch (error) {
      ElMessage.error('图片上传失败')
      console.error('Markdown图片上传失败:', error)
    }
  }

  // 技术栈管理
  const addTechStack = () => {
    const tech = techStackInput.value.trim()
    if (!tech) return

    if (projectForm.techStackTags.includes(tech)) {
      ElMessage.warning('该技术栈已存在')
      return
    }

    projectForm.techStackTags.push(tech)
    techStackInput.value = ''
  }

  const removeTechStack = (index: number) => {
    projectForm.techStackTags.splice(index, 1)
  }

  // 团队成员管理
  const addTeamMember = () => {
    projectForm.teamDivision.push({ name: '', role: '' })
  }

  const removeTeamMember = (index: number) => {
    if (projectForm.teamDivision.length > 1) {
      projectForm.teamDivision.splice(index, 1)
    }
  }

  /**
   * 提交表单 - 创建或更新项目
   * 让拦截器处理错误，不吞掉错误
   */
  const submitForm = async () => {
    if (!projectFormRef.value) return

    await projectFormRef.value.validate()

    submitLoading.value = true

    try {
      // 准备提交数据
      const submitData = {
        ...projectForm,
        awardIds: selectedAwardIds.value,
        teamDivision: projectForm.teamDivision.filter(
          member => member.name.trim() || member.role.trim()
        ),
        type: 'project',
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString(),
      }

      if (isEditMode.value && projectId.value) {
        await updateProject(projectId.value, submitData)
        ElMessage.success('项目更新成功')
      } else {
        await createProject(submitData)
        ElMessage.success('项目创建成功')
      }

      handleBack()
    } finally {
      submitLoading.value = false
    }
  }

  /**
   * 删除项目 - 让拦截器处理错误
   */
  const handleDelete = async () => {
    if (!projectId.value) return

    await ElMessageBox.confirm(
      '此操作将永久删除该项目，是否继续？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    deleteLoading.value = true
    await deleteProject(projectId.value)
    ElMessage.success('项目删除成功')
    handleBack()
    deleteLoading.value = false
  }

  // 返回项目列表
  const handleBack = () => {
    router.push('/admin/projects')
  }

  // 监听奖项选择变化
  watch(selectedAwardIds, newVal => {
    projectForm.awardIds = newVal
  })

  // 监听轮播图变化，更新封面图ID
  watch(
    carouselImages,
    newImages => {
      if (
        coverImageId.value &&
        !newImages.some(img => img.id === coverImageId.value)
      ) {
        coverImageId.value = null
        if (projectForm.coverImage) {
          projectForm.coverImage = ''
        }
      }
    },
    { deep: true }
  )

  // 页面加载时初始化数据
  onMounted(async () => {
    if (isEditMode.value) {
      await loadProjectDetail()
    } else {
      // 创建模式，初始化默认值
      projectForm.teamDivision = [{ name: '', role: '' }]
      projectForm.techStackTags = []
      projectForm.mediaResourceIds = []
      projectForm.awardIds = []
    }

    // 加载奖项选项
    try {
      const awards = await getAwards()
      allAwards.value = awards // 存储所有奖项数据
      awardOptions.value = awards
    } catch (error) {
      console.error('加载奖项失败:', error)
    }
  })
</script>

<style scoped>
  .project-edit-container {
    padding: 20px;
    min-height: 100vh;
    background-color: #f5f7fa;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .back-btn {
    background-color: #909399;
    border-color: #909399;
    color: white;
  }

  .back-btn:hover {
    background-color: #82848a;
    border-color: #82848a;
  }

  /* 封面图样式 */
  .cover-image-container {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 20px;
    background-color: #fafafa;
  }

  .cover-image-preview {
    text-align: center;
  }

  .cover-image-preview img {
    max-width: 300px;
    max-height: 200px;
    object-fit: cover;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 15px;
  }

  .cover-image-empty {
    text-align: center;
    padding: 40px 0;
  }

  /* 轮播图管理样式 */
  .carousel-management {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 20px;
    background-color: #fafafa;
  }

  .current-cover {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f0f9ff;
    border-radius: 8px;
    border: 1px solid #b3d8ff;
  }

  .cover-info {
    display: flex;
    align-items: center;
    gap: 15px;
  }

  .cover-thumbnail {
    width: 60px;
    height: 40px;
    object-fit: cover;
    border-radius: 4px;
    border: 2px solid #409eff;
  }

  .carousel-images {
    display: flex;
    flex-wrap: nowrap;
    overflow-x: auto;
    gap: 15px;
    margin-bottom: 20px;
    padding-bottom: 10px;
  }

  .carousel-images::-webkit-scrollbar {
    height: 6px;
  }

  .carousel-images::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  .carousel-images::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
  }

  .carousel-images::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
  }

  .carousel-image-item {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    cursor: pointer;
    flex: 0 0 200px;
    height: 150px;
  }

  .carousel-image-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }

  .carousel-image-item.is-cover {
    border: 3px solid #67c23a;
    box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.3);
  }

  .carousel-image-item img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(transparent 40%, rgba(0, 0, 0, 0.7));
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 10px;
  }

  .overlay-top {
    display: flex;
    justify-content: flex-end;
  }

  .overlay-bottom {
    display: flex;
    justify-content: center;
  }

  .image-info {
    padding: 10px;
    background: white;
  }

  .image-title {
    font-weight: 500;
    margin-bottom: 5px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 14px;
  }

  .image-desc {
    color: #909399;
    font-size: 12px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .no-carousel-images {
    text-align: center;
    padding: 40px 0;
  }

  .carousel-actions {
    display: flex;
    gap: 10px;
    justify-content: center;
  }

  /* Markdown编辑器样式 */
  .markdown-editor-container {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .editor-tabs {
    padding: 10px 10px 0;
    background-color: #f5f7fa;
    border-bottom: 1px solid #e4e7ed;
  }

  .editor-content {
    height: 400px;
  }

  .editor-pane,
  .preview-pane {
    height: 100%;
  }

  .split-pane {
    display: flex;
    height: 100%;
  }

  .split-left,
  .split-right {
    flex: 1;
    border-right: 1px solid #e4e7ed;
  }

  .split-right {
    border-right: none;
    border-left: 1px solid #e4e7ed;
  }

  /* 技术栈样式 */
  .tech-stack-container {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 15px;
    background-color: #fafafa;
  }

  .tech-stack-input {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
  }

  .tech-stack-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .no-tech-stack {
    text-align: center;
    color: #909399;
    font-size: 14px;
  }

  /* 团队成员样式 */
  .team-members-container {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 15px;
    background-color: #fafafa;
  }

  .team-members-list {
    margin-bottom: 15px;
  }

  .team-member-item {
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
    align-items: center;
  }

  .team-member-item:last-child {
    margin-bottom: 0;
  }

  /* 奖项管理样式 */
  .award-management {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 15px;
    background-color: #fafafa;
  }

  .award-option {
    padding: 8px 0;
  }

  .award-name {
    font-weight: 500;
    margin-bottom: 4px;
    font-size: 14px;
  }

  .award-detail {
    color: #909399;
    font-size: 12px;
  }

  .form-tip {
    margin-top: 8px;
    color: #909399;
    font-size: 12px;
  }

  /* 已选择奖项展示样式 */
  .selected-awards-container {
    background-color: white;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    padding: 12px;
  }

  .selected-awards-title {
    font-weight: 500;
    margin-bottom: 10px;
    color: #303133;
    font-size: 14px;
  }

  .selected-awards-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .selected-award-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 12px;
    background-color: #f5f7fa;
    border-radius: 6px;
    border: 1px solid #e4e7ed;
  }

  .selected-award-item .award-text {
    flex: 1;
    font-size: 14px;
    color: #303133;
    line-height: 1.4;
  }

  .selected-award-item .el-button {
    margin-left: 10px;
    flex-shrink: 0;
  }

  /* 表单操作 */
  .form-actions {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }

  /* 上传对话框样式 */
  .upload-area {
    width: 100%;
  }

  .upload-icon {
    font-size: 48px;
    color: #c0c4cc;
    margin-bottom: 10px;
  }

  .upload-text {
    color: #606266;
    font-size: 14px;
  }

  .upload-text em {
    color: #409eff;
    font-style: normal;
  }

  .upload-tip {
    color: #909399;
    font-size: 12px;
    margin-top: 10px;
    text-align: center;
  }

  /* 隐藏上传按钮当已有图片时 */
  .upload-area.has-image :deep(.el-upload--picture-card) {
    display: none;
  }

  /* 图库样式 */
  .image-library {
    max-height: 70vh;
    display: flex;
    flex-direction: column;
  }

  .library-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    gap: 15px;
  }

  .library-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
    overflow-y: auto;
    max-height: 400px;
    padding: 10px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background-color: #fafafa;
  }

  .library-image-item {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;
  }

  .library-image-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  }

  .library-image-item.selected {
    border-color: #409eff;
    box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.3);
  }

  .library-image-item.disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .library-image-item img {
    width: 100%;
    height: 120px;
    object-fit: cover;
  }

  .library-image-item .image-info {
    padding: 8px;
    background: white;
  }

  .library-image-item .image-title {
    font-weight: 500;
    margin-bottom: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 12px;
  }

  .library-image-item .image-desc {
    color: #909399;
    font-size: 11px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .selected-badge {
    position: absolute;
    top: 5px;
    right: 5px;
    background: #409eff;
    color: white;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
  }

  .no-library-images {
    text-align: center;
    padding: 40px 0;
    grid-column: 1 / -1;
  }

  .library-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
    padding-top: 15px;
    border-top: 1px solid #e4e7ed;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .project-edit-container {
      padding: 10px;
    }

    .carousel-images {
      grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    }

    .library-grid {
      grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    }

    .team-member-item {
      flex-direction: column;
      align-items: stretch;
    }

    .tech-stack-input {
      flex-direction: column;
    }

    .library-toolbar {
      flex-direction: column;
      align-items: stretch;
    }

    .library-toolbar .el-input {
      width: 100% !important;
    }
  }

  /* 深色模式适配 */
  @media (prefers-color-scheme: dark) {
    .project-edit-container {
      background-color: #141414;
    }

    .el-card {
      background-color: #1f1f1f;
      color: #e8e8e8;
    }

    .carousel-management,
    .cover-image-container,
    .tech-stack-container,
    .team-members-container,
    .award-management {
      background-color: #1f1f1f;
      border-color: #434343;
    }

    .library-grid {
      background-color: #1f1f1f;
      border-color: #434343;
    }
  }
</style>

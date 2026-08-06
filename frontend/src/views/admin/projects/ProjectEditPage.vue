<template>
  <div class="project-edit-container">
    <el-card class="project-edit-card">
      <template #header>
        <div class="card-header">
          <span>{{ isEditMode ? '编辑项目' : '添加项目' }}</span>
          <el-button @click="handleBack" class="back-btn">
            <el-icon><i-ep-arrow-left /></el-icon>
            返回项目列表
          </el-button>
        </div>
      </template>

      <el-form
        ref="projectFormRef"
        :model="projectForm"
        :rules="rules"
        label-width="100px"
        class="project-form"
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
              <el-option
                v-for="category in projectCategories"
                :key="category"
                :label="category"
                :value="category"
              />
            </el-select>
          </el-form-item>
        <el-form-item label="开发时间" prop="timeRange">
          <el-date-picker
            v-model="dateRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="YYYY.MM"
            value-format="YYYY.MM"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="简要介绍" prop="briefIntro">
          <el-input
            v-model="projectForm.briefIntro"
            placeholder="用一两句话说明项目解决的问题、主要功能和目标用户"
            type="textarea"
            :rows="3"
            show-word-limit
            maxlength="200"
          />
        </el-form-item>
        <el-form-item label="项目封面" prop="coverImage">
          <div class="upload-container">
            <div class="upload-main">
              <img
                v-if="projectForm.coverImage"
                :src="projectForm.coverImage"
                class="avatar"
                @click="coverPickerVisible = true"
              />
               <div v-else class="upload-placeholder" @click="coverPickerVisible = true"><el-icon><i-ep-plus /></el-icon><div>请选择封面图片</div></div>
            </div>
            <div v-if="projectForm.coverImage" class="cover-actions">
              <el-button size="small" @click="coverPickerVisible = true">更换</el-button>
              <el-button size="small" @click="projectForm.coverImage = ''">移除</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="详细介绍" prop="descriptionMd">
          <div class="markdown-editor-container">
            <div class="editor-tabs">
              <el-tabs v-model="activeTab" type="card">
                <el-tab-pane label="编辑" name="edit" />
                <el-tab-pane label="预览" name="preview" />
                <el-tab-pane label="分屏" name="split" />
              </el-tabs>
            </div>
            <div v-if="activeTab === 'edit'" class="editor-full">
                  <MdEditor
                    v-model="projectForm.descriptionMd"
                :toolbars-exclude="['image']"
                :style="{ height: editorHeight }"
                class="markdown-editor"
              />
            </div>
            <div v-else-if="activeTab === 'preview'" class="editor-full">
              <div class="markdown-preview">
                <MdPreview
                  :modelValue="projectForm.descriptionMd"
                  :style="{ height: editorHeight, overflowY: 'auto' }"
                />
              </div>
            </div>
            <div v-else class="editor-split">
              <div class="editor-left">
                <MdEditor
                  v-model="projectForm.descriptionMd"
                  :toolbars-exclude="['image']"
                  :style="{ height: editorHeight }"
                  class="markdown-editor"
                />
              </div>
              <div class="editor-right">
                <div class="markdown-preview">
                  <MdPreview
                    :modelValue="projectForm.descriptionMd"
                    :style="{ height: editorHeight, overflowY: 'auto' }"
                  />
                </div>
              </div>
            </div>
          </div>
        </el-form-item>

        <!-- 技术栈管理 -->
        <el-form-item label="技术栈">
          <div class="tech-stack-management">
            <div class="tech-stack-controls">
              <el-input
                v-model="techStackInput"
                placeholder="请输入技术名称"
                class="tech-stack-input"
                @keyup.enter="addTechStack"
              />
              <el-button type="primary" @click="addTechStack">添加</el-button>
            </div>
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
              class="tech-tag"
            >
              {{ tech }}
            </el-tag>
          </div>
          <div v-else class="no-tech-stack-tip text-gray-500 text-sm mt-3">
            暂无技术栈，请添加
          </div>
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
                class="team-member-input"
              />
              <el-input
                v-model="member.role"
                placeholder="成员角色"
                class="team-member-input"
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
        <el-form-item label="轮播图片" prop="imageUrls">
          <div class="media-resources-container">
            <!-- 已选择的媒体资源列表 -->
            <div
                v-if="projectForm.imageUrls.length > 0"
              class="selected-media-list"
            >
              <div
                v-for="url in projectForm.imageUrls"
                :key="url"
                class="selected-media-item"
              >
                <img
                  :src="url"
                  alt="预览"
                  class="selected-media-thumbnail"
                />
                <span class="selected-media-name" :title="getImageName(url)">
                  {{ getImageName(url) }}
                </span>
                <el-button
                  type="danger"
                  text
                  circle
                  size="small"
                  :aria-label="`移除 ${getImageName(url)}`"
                  @click="removeImageUrl(url)"
                >
                  <i-ep-delete />
                </el-button>
              </div>
            </div>
            <div v-else class="no-selected-media text-gray-500 text-sm mb-3">
              暂无轮播图，请从图库选择或上传
            </div>

            <!-- 操作按钮 -->
            <div class="media-actions">
              <el-button type="primary" size="small" @click="galleryPickerVisible = true">
                从图库选择
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
            <el-select
              v-model="projectForm.awardIds"
              multiple
              filterable
              clearable
              placeholder="输入竞赛名称、级别、奖项、获奖人或年份搜索"
              class="award-input"
            >
              <el-option
                v-for="award in awardOptions"
                :key="award.id"
                :label="formatAward(award)"
                :value="award.id"
              />
            </el-select>
            <div class="form-tip text-gray-500 text-sm mt-1">
              可选择多个奖项，输入关键字会立即筛选
            </div>
          </div>
        </el-form-item>
      </el-form>

      <div class="form-actions">
        <el-button @click="handleBack">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </el-card>

    <AttachmentPicker v-model:visible="coverPickerVisible" v-model="projectForm.coverImage" />
    <AttachmentPicker
      v-model:visible="galleryPickerVisible"
      v-model="projectForm.imageUrls"
      multiple
      confirm
      @selected="rememberImageName"
    />
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import {
	computed,
	defineAsyncComponent,
	h,
	onMounted,
	reactive,
	ref,
} from "vue";
import { useRoute, useRouter } from "vue-router";
import AttachmentPicker from "@/components/admin/AttachmentPicker.vue";
import { getAdminAwards } from "@/services/adminService";
import {
	createProject,
	getProjectForEdit,
	updateProject,
} from "@/services/projectService";

const EditorLoading = {
	render: () => h("div", { class: "editor-loading" }, "编辑器加载中…"),
};
const MdEditor = defineAsyncComponent({
	loader: async () => {
		await import("md-editor-v3/lib/style.css");
		return (await import("md-editor-v3")).MdEditor;
	},
	loadingComponent: EditorLoading,
	suspensible: false,
});
const MdPreview = defineAsyncComponent({
	loader: async () => {
		await import("md-editor-v3/lib/style.css");
		return (await import("md-editor-v3")).MdPreview;
	},
	loadingComponent: EditorLoading,
	suspensible: false,
});

const defaultCategory = "Web开发";
const editorHeight = "500px";
const projectCategories = [
	"Web开发",
	"人工智能",
	"移动应用",
	"数据科学",
	"其他",
];

const router = useRouter();
const route = useRoute();
const projectId = computed(() => {
	const id = route.params.id;
	return id ? Number(id) : null;
});

const isEditMode = computed(() => projectId.value !== null);

const projectFormRef = ref(null);
const coverPickerVisible = ref(false);
const galleryPickerVisible = ref(false);
const awardOptions = ref([]);
const imageNames = ref({});
const dateRange = ref([]);
const activeTab = ref("edit");
const techStackInput = ref("");

/**
 * 创建默认团队成员。
 *
 * @returns {{ name: string, role: string }}
 */
const createEmptyTeamMember = () => ({
	name: "",
	role: "",
});

/**
 * 清理封面图片地址中的空白字符。
 *
 * @param {unknown} coverImage
 * @returns {string}
 */
const sanitizeCoverImage = (coverImage) => {
	return typeof coverImage === "string" ? coverImage.replace(/[`\s]/g, "") : "";
};

/**
 * 保证输入值始终为数组。
 *
 * @param {unknown} value
 * @returns {any[]}
 */
const ensureArray = (value) => {
	return Array.isArray(value) ? value : [];
};

/**
 * 生成提交接口需要的项目数据。
 *
 * @returns {Object}
 */
const buildSubmitData = () => {
	return {
		...projectForm,
		coverImage: sanitizeCoverImage(projectForm.coverImage),
		techStackTags: ensureArray(projectForm.techStackTags),
		teamDivision: ensureArray(projectForm.teamDivision),
		imageUrls: ensureArray(projectForm.imageUrls),
		awardIds: ensureArray(projectForm.awardIds),
	};
};

const projectForm = reactive({
	title: "",
	category: defaultCategory,
	timeRange: "",
	briefIntro: "",
	descriptionMd: "",
	coverImage: "",
	techStackTags: [],
	teamDivision: [createEmptyTeamMember()],
	imageUrls: [],
	awardIds: [],
});

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
	imageUrls: [
		{
			validator: (_, value, callback) => {
				if (!value || value.length === 0) {
					callback(new Error("请至少添加一张轮播图"));
				} else {
					callback();
				}
			},
			trigger: "submit",
		},
	],
};

/**
 * 提交项目表单。
 */
const submitForm = async () => {
	if (!projectFormRef.value) return;

	try {
		await projectFormRef.value.validate();

		if (!projectForm.imageUrls || projectForm.imageUrls.length === 0) {
			ElMessage.error("请至少添加一张轮播图");
			return;
		}

		const submitData = buildSubmitData();

		if (isEditMode.value && projectId.value !== null) {
			await updateProject(projectId.value, submitData);
			ElMessage.success("更新项目成功");
		} else {
			await createProject(submitData);
			ElMessage.success("添加项目成功");
		}

		handleBack();
	} catch (error) {
		if (
			error &&
			!(error instanceof Error && error.message.includes("Validation failed"))
		) {
			ElMessage.error(isEditMode.value ? "更新项目失败" : "添加项目失败");
		}
		console.error("提交表单失败:", error);
	}
};

/**
 * 同步日期范围到表单字段。
 *
 * @param {string[]} dates
 */
const handleDateRangeChange = (dates) => {
	if (dates && dates.length === 2) {
		projectForm.timeRange = `${dates[0]}-${dates[1]}`;
	}
};

/**
 * 添加技术栈标签。
 */
const addTechStack = () => {
	const techValue = techStackInput.value.trim();
	if (techValue && projectForm.techStackTags) {
		const exists = projectForm.techStackTags.some((tech) => tech === techValue);
		if (!exists) {
			projectForm.techStackTags.push(techValue);
			techStackInput.value = "";
		} else {
			ElMessage.warning("该技术已存在");
		}
	}
};

/**
 * 移除技术栈标签。
 *
 * @param {number} index
 */
const removeTechStack = (index) => {
	if (projectForm.techStackTags) {
		projectForm.techStackTags.splice(index, 1);
	}
};

/**
 * 添加团队成员输入项。
 */
const addTeamMember = () => {
	if (projectForm.teamDivision) {
		projectForm.teamDivision.push(createEmptyTeamMember());
	}
};

/**
 * 移除团队成员输入项。
 *
 * @param {number} index
 */
const removeTeamMember = (index) => {
	if (projectForm.teamDivision && projectForm.teamDivision.length > 1) {
		projectForm.teamDivision.splice(index, 1);
	}
};

/**
 * 读取可选奖项，供表单内即时筛选。
 */
const loadAwardOptions = async () => {
	try {
		awardOptions.value = await getAdminAwards();
	} catch (error) {
		ElMessage.error("获取奖项列表失败");
	}
};

/** 将附件库中的原始文件名保留在轮播图列表中。 */
const rememberImageName = (attachment) => {
	imageNames.value[attachment.url] = attachment.originalName;
};

/** 从附件元数据或对象地址生成轮播图片名称。 */
const getImageName = (url) => {
	if (imageNames.value[url]) return imageNames.value[url];
	const objectName = decodeURIComponent(url.split("/").pop() || "图片");
	return objectName.replace(/^[a-f\d-]{36}_/i, "");
};

/** 生成覆盖奖项全部可检索字段的选项文本。 */
const formatAward = (award) =>
	[
		award.competitionName,
		award.competitionTrack,
		award.competitionLevel,
		award.awardLevel,
		...(award.winners || []),
		award.year,
	]
		.filter(Boolean)
		.join(" · ");

/** 移除轮播图片。 */
const removeImageUrl = (url) => {
	projectForm.imageUrls = projectForm.imageUrls.filter((item) => item !== url);
	delete imageNames.value[url];
};

/**
 * 加载编辑态项目数据。
 */
const loadProjectDetail = async () => {
	if (!isEditMode.value || projectId.value === null) return;

	try {
		const projectDetail = await getProjectForEdit(projectId.value);
		const awards = Array.isArray(projectDetail.awards)
			? projectDetail.awards
			: [];

		Object.assign(projectForm, {
			title: projectDetail.title || "",
			category: projectDetail.category || defaultCategory,
			timeRange: projectDetail.timeRange || "",
			briefIntro: projectDetail.briefIntro || "",
			descriptionMd: projectDetail.descriptionMd || "",
			coverImage: sanitizeCoverImage(projectDetail.coverImage),
			techStackTags: ensureArray(projectDetail.techStackTags),
			teamDivision:
				Array.isArray(projectDetail.teamDivisions) &&
				projectDetail.teamDivisions.length > 0
					? projectDetail.teamDivisions
					: [createEmptyTeamMember()],
			imageUrls: ensureArray(projectDetail.imageUrls),
			awardIds: awards.map((item) => item.id),
		});

		if (projectDetail.timeRange) {
			const rangeParts = projectDetail.timeRange.split("-");
			if (rangeParts.length === 2) {
				dateRange.value = [rangeParts[0], rangeParts[1]];
			}
		}
	} catch (error) {
		const errorMessage = error instanceof Error ? error.message : "未知错误";
		ElMessage.error(`获取项目详情失败: ${errorMessage}`);
		console.error("获取项目详情失败:", error);
	}
};

/**
 * 返回项目列表页。
 */
const handleBack = () => {
	router.push("/admin/projects");
};

/**
 * 初始化页面数据。
 */
onMounted(() => {
	loadAwardOptions();
	if (isEditMode.value) {
		loadProjectDetail();
		return;
	}
});
</script>

<style scoped>
  .project-edit-container {
    padding: 20px;
  }

    .project-edit-card {
      width: 100%;
      max-width: none;
      margin: 0;
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

   :deep(.el-form-item__content) {
     flex: 1;
     min-width: 0;
   }

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

  .avatar {
    width: 250px;
    height: 250px;
    display: block;
    object-fit: cover;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s ease;
    cursor: pointer;
  }

  .avatar:hover {
    transform: scale(1.02);
  }

  .cover-actions {
    display: flex;
    gap: 8px;
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

   .cover-image-input,
   .award-input {
     width: 100%;
   }

   .tech-stack-management {
     width: 100%;
   }

   .tech-stack-controls {
     display: flex;
     gap: 8px;
   }

   .tech-stack-input {
     flex: 1;
     min-width: 0;
   }

  .team-member-input {
    margin-right: 12px;
  }

  .media-upload-button {
    margin-left: 0;
  }

  .selected-label-button {
    color: #67c23a;
  }

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

   .markdown-editor-container {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
     width: 100%;
     min-height: 500px;
   }

   :deep(.editor-loading) {
     display: grid;
     min-height: 500px;
     place-items: center;
     color: #98a2b3;
     background: #f9fafb;
   }

  .editor-full {
    height: 600px;
  }

  .editor-split {
    display: flex;
    height: 600px;
    width: 100%;
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

  .tech-stack-list {
    margin-top: 15px;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

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

   .media-resources-container {
     border: 1px solid #e4e7ed;
     border-radius: 8px;
     padding: 20px;
     background-color: #fafafa;
     width: 100%;
     box-sizing: border-box;
  }

   .selected-media-list {
     display: grid;
     grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
     gap: 12px;
     margin-bottom: 20px;
   }

   .selected-media-item {
     position: relative;
     display: grid;
     gap: 8px;
     min-width: 0;
     padding: 8px;
     background-color: white;
     border-radius: 6px;
     border: 1px solid #ebeef5;
   }

   .selected-media-thumbnail {
     width: 100%;
     height: 96px;
     object-fit: cover;
     border-radius: 4px;
   }

   .selected-media-name {
     display: block;
     font-weight: 500;
     font-size: 12px;
     white-space: nowrap;
     overflow: hidden;
     text-overflow: ellipsis;
   }

   .selected-media-item > .el-button {
     position: absolute;
     top: 4px;
     right: 4px;
     background: rgb(255 255 255 / 88%);
   }

  .media-actions {
    display: flex;
    gap: 10px;
  }

  .no-selected-media {
    text-align: center;
    padding: 40px 0;
  }

   .award-management {
     border: 1px solid #e4e7ed;
     border-radius: 8px;
     padding: 20px;
     background-color: #fafafa;
     width: 100%;
     box-sizing: border-box;
  }

  .form-tip {
    color: #909399;
    font-size: 12px;
    margin-top: 5px;
  }

   @media (max-width: 768px) {
     .project-edit-container {
       padding: 0;
     }

      .project-edit-card {
        width: 100%;
        border-radius: 8px;
     }

     .card-header {
       align-items: flex-start;
       flex-wrap: wrap;
       gap: 10px;
       margin-bottom: 0;
     }

     .back-btn {
       max-width: 100%;
     }

     :deep(.el-form-item) {
       display: block;
     }

     :deep(.el-form-item__content) {
       min-width: 0;
       margin-left: 0 !important;
     }

     :deep(.el-form-item__content > .el-input),
     :deep(.el-form-item__content > .el-select),
     :deep(.el-form-item__content > .el-date-editor) {
       width: 100%;
     }

     :deep(.el-date-editor) {
       max-width: 100%;
     }

    .upload-main {
      flex-direction: column;
      align-items: stretch;
    }

     .avatar,
     .upload-placeholder {
       width: 100%;
       max-width: 300px;
      margin: 0 auto;
    }

      .editor-full,
      .editor-split {
        height: 360px;
      }

      .markdown-editor-container,
      :deep(.editor-loading) {
        min-height: 360px;
      }

     .editor-split {
       flex-direction: column;
     }

     .editor-left,
     .editor-right {
       min-height: 0;
       border: 0;
     }

     .editor-right {
       border-top: 1px solid #e4e7ed;
     }

    .team-member-item,
      .selected-media-item {
       min-width: 0;
    }

     .team-member-item .el-input,
    .team-member-item .el-select {
      margin-right: 0 !important;
      margin-bottom: 10px;
     }

     .team-members-list,
     .media-resources-container,
     .award-management {
       width: 100%;
       box-sizing: border-box;
       padding: 12px;
     }

      .tech-stack-input {
        width: 100%;
      }

      .selected-media-item {
        gap: 8px;
     }

      .selected-media-item .el-button,
      .media-actions .el-button {
        width: auto;
      }

      .selected-media-thumbnail {
       max-width: 100%;
      }

     .form-actions {
       justify-content: stretch;
       margin-top: 20px;
     }

     .form-actions .el-button {
       flex: 1;
       min-width: 0;
     }
   }
</style>

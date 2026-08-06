<template>
  <AdminPage title="首页高光管理">
    <template #action>
      <el-button type="primary" @click="showAddMenu = true">添加内容</el-button>
    </template>

    <AdminToolbar>
      <span class="manage-tip">拖拽可调整展示顺序，保存后前台轮播立即生效</span>
    </AdminToolbar>

    <!-- 列表 -->
    <div v-loading="loading" class="highlight-list">
      <el-empty v-if="!loading && highlights.length === 0" description="暂无高光内容，点击右上角添加" />
      <div v-else-if="!loading && loadError" class="highlight-error">
        <p>{{ loadError }}</p>
        <el-button type="primary" @click="load">重新加载</el-button>
      </div>
      <ul v-else class="highlight-sort-list">
        <li
          v-for="(item, index) in highlights"
          :key="item.id"
          class="highlight-sort-item"
          :draggable="!isSubmitter"
          :class="{ dragging: dragIndex === index }"
          @dragstart="handleDragStart(index)"
          @dragover.prevent="handleDragOver(index)"
          @dragleave="handleDragLeave(index)"
          @drop.prevent="handleDrop(index)"
          @dragend="handleDragEnd"
        >
          <span class="order-badge">{{ index + 1 }}</span>
          <span v-if="!isSubmitter" class="drag-handle" title="拖拽排序"><el-icon><i-ep-rank /></el-icon></span>
          <div class="highlight-cover">
            <el-image v-if="item.coverImage" :src="item.coverImage" fit="cover" class="cover-image">
              <template #error><div class="cover-placeholder"><el-icon><i-ep-picture /></el-icon></div></template>
            </el-image>
            <div v-else class="cover-placeholder"><el-icon><i-ep-picture /></el-icon></div>
          </div>
          <div class="highlight-info">
            <div class="highlight-title">
              <el-tag :type="item.type === 'project' ? 'primary' : 'success'" size="small" effect="light">
                {{ item.type === 'project' ? '项目' : '活动' }}
              </el-tag>
              <span class="title-text" :title="item.title">{{ item.title || '（无标题）' }}</span>
            </div>
            <p class="highlight-desc" :title="item.description">{{ item.description || '暂无简介' }}</p>
            <p v-if="item.type === 'activity' && item.activityDate" class="highlight-date">
              活动日期：{{ item.activityDate }}
            </p>
          </div>
          <div v-if="!isSubmitter" class="highlight-actions">
            <el-button v-if="item.type === 'activity'" text size="small" @click="openEdit(item)">编辑</el-button>
            <el-button text type="danger" size="small" @click="remove(item)">移除</el-button>
          </div>
        </li>
      </ul>
    </div>
  </AdminPage>

  <!-- 添加入口选择 -->
  <el-dialog v-model="showAddMenu" title="添加高光内容" width="min(440px, calc(100% - 24px))" align-center>
    <div class="add-menu">
      <button type="button" class="add-menu-card" @click="openProjectPicker">
        <el-icon><i-ep-box /></el-icon>
        <div><strong>从项目库添加</strong><span>选择已有项目，展示其封面与简介</span></div>
      </button>
      <button type="button" class="add-menu-card" @click="openActivityForm">
        <el-icon><i-ep-trophy /></el-icon>
        <div><strong>添加活动</strong><span>填写活动标题、简介、日期与封面</span></div>
      </button>
    </div>
  </el-dialog>

  <!-- 从项目库选择 -->
  <el-dialog v-model="projectPickerVisible" title="从项目库添加" width="min(640px, calc(100% - 24px))" align-center>
    <el-select
      v-model="selectedProjectId"
      filterable
      placeholder="搜索项目标题"
      class="project-select"
      size="large"
    >
      <el-option
        v-for="project in projectOptions"
        :key="project.id"
        :label="project.title"
        :value="project.id"
        :disabled="addedProjectIds.has(project.id)"
      />
    </el-select>
    <p class="dialog-tip">已添加的项目将置灰不可重复选择</p>
    <template #footer>
      <el-button @click="projectPickerVisible = false">取消</el-button>
      <el-button type="primary" :disabled="!selectedProjectId" @click="submitProject">添加</el-button>
    </template>
  </el-dialog>

  <!-- 活动表单 -->
  <el-dialog v-model="activityFormVisible" :title="editingId ? '编辑活动' : '添加活动'" width="min(560px, calc(100% - 24px))" align-center>
    <el-form ref="activityFormRef" :model="activityForm" :rules="activityRules" label-position="top">
      <el-form-item label="活动标题" prop="title">
        <el-input v-model="activityForm.title" placeholder="请输入活动标题" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="活动简介" prop="description">
        <el-input v-model="activityForm.description" type="textarea" :rows="3" placeholder="用一两句话描述活动内容" maxlength="500" show-word-limit />
      </el-form-item>
      <el-form-item label="活动日期（可空）">
        <el-date-picker v-model="activityForm.activityDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" class="date-picker" />
      </el-form-item>
      <el-form-item label="封面图片" prop="coverImage">
        <div class="cover-field">
          <img v-if="activityForm.coverImage" :src="activityForm.coverImage" class="cover-preview" alt="封面预览" />
          <div v-else class="cover-placeholder" @click="coverPickerVisible = true">
            <el-icon><i-ep-plus /></el-icon><span>请选择封面图片</span>
          </div>
          <div v-if="activityForm.coverImage" class="cover-actions">
            <el-button size="small" @click="coverPickerVisible = true">更换</el-button>
            <el-button size="small" @click="activityForm.coverImage = ''">移除</el-button>
          </div>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="activityFormVisible = false">取消</el-button>
      <el-button type="primary" @click="submitActivity">保存</el-button>
    </template>
  </el-dialog>

  <AttachmentPicker v-model:visible="coverPickerVisible" v-model="activityForm.coverImage" />
</template>

<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, onMounted, reactive, ref } from "vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import AttachmentPicker from "@/components/admin/AttachmentPicker.vue";
import {
	addActivityHighlight,
	addProjectHighlight,
	deleteHighlight,
	getAdminHomepageHighlights,
	reorderHighlights,
	updateActivityHighlight,
} from "@/services/homepageHighlightService";
import { getAdminProjects } from "@/services/projectService";
import { useAdminStore } from "@/stores/adminStore";

const isSubmitter = useAdminStore().hasRole(["submitter"]);
const loading = ref(false);
const loadError = ref("");
const highlights = ref([]);
const showAddMenu = ref(false);
const projectPickerVisible = ref(false);
const activityFormVisible = ref(false);
const coverPickerVisible = ref(false);
const projectOptions = ref([]);
const selectedProjectId = ref(null);
const editingId = ref(null);
const activityFormRef = ref(null);

const activityForm = reactive({
	title: "",
	description: "",
	activityDate: "",
	coverImage: "",
});
const activityRules = {
	title: [{ required: true, message: "请输入活动标题", trigger: "blur" }],
	description: [{ required: true, message: "请输入活动简介", trigger: "blur" }],
	coverImage: [
		{ required: true, message: "请选择封面图片", trigger: "change" },
	],
};

/** 已添加的项目ID集合，用于项目选择时置灰。 */
const addedProjectIds = computed(
	() =>
		new Set(
			highlights.value
				.filter((item) => item.type === "project" && item.projectId)
				.map((item) => item.projectId),
		),
);

/** 加载高光列表。 */
const load = async () => {
	loading.value = true;
	loadError.value = "";
	try {
		highlights.value = await getAdminHomepageHighlights();
	} catch (error) {
		loadError.value =
			error instanceof Error ? error.message : "获取高光列表失败";
		console.error(error);
	} finally {
		loading.value = false;
	}
};

/** 加载项目库，供选择添加。 */
const loadProjects = async () => {
	try {
		const data = (await getAdminProjects({ current: 1, size: 100 }))?.data;
		projectOptions.value = data?.records ?? [];
	} catch (error) {
		ElMessage.error("获取项目列表失败");
		console.error(error);
	}
};

const openProjectPicker = () => {
	showAddMenu.value = false;
	selectedProjectId.value = null;
	loadProjects();
	projectPickerVisible.value = true;
};

const openActivityForm = () => {
	showAddMenu.value = false;
	editingId.value = null;
	Object.assign(activityForm, {
		title: "",
		description: "",
		activityDate: "",
		coverImage: "",
	});
	activityFormVisible.value = true;
};

const openEdit = (item) => {
	editingId.value = item.id;
	Object.assign(activityForm, {
		title: item.title || "",
		description: item.description || "",
		activityDate: item.activityDate || "",
		coverImage: item.coverImage || "",
	});
	activityFormVisible.value = true;
};

const submitProject = async () => {
	if (!selectedProjectId.value) return;
	try {
		await addProjectHighlight({ projectId: selectedProjectId.value });
		ElMessage.success("添加项目高光成功");
		projectPickerVisible.value = false;
		load();
	} catch (error) {
		ElMessage.error(
			error instanceof Error ? error.message : "添加项目高光失败",
		);
		console.error(error);
	}
};

const submitActivity = async () => {
	if (!(await activityFormRef.value?.validate().catch(() => false))) return;
	try {
		const payload = { ...activityForm };
		if (editingId.value) {
			await updateActivityHighlight(editingId.value, payload);
			ElMessage.success("更新活动成功");
		} else {
			await addActivityHighlight(payload);
			ElMessage.success("添加活动成功");
		}
		activityFormVisible.value = false;
		load();
	} catch (error) {
		ElMessage.error(error instanceof Error ? error.message : "保存失败");
		console.error(error);
	}
};

const remove = async (item) => {
	try {
		await ElMessageBox.confirm(
			`确定移除这条${item.type === "project" ? "项目" : "活动"}高光吗？`,
			"确认移除",
			{ type: "warning" },
		);
		await deleteHighlight(item.id);
		ElMessage.success("移除成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("移除失败");
			console.error(error);
		}
	}
};

// —— 原生 HTML5 拖拽排序 ——
const dragIndex = ref(null);
const dragOverIndex = ref(null);

const handleDragStart = (index) => {
	dragIndex.value = index;
};

const handleDragOver = (index) => {
	if (dragOverIndex.value !== index) dragOverIndex.value = index;
};

const handleDragLeave = (index) => {
	if (dragOverIndex.value === index) dragOverIndex.value = null;
};

const handleDrop = (index) => {
	const from = dragIndex.value;
	const to = index;
	if (from === null || from === to) {
		handleDragEnd();
		return;
	}
	const list = [...highlights.value];
	const [moved] = list.splice(from, 1);
	list.splice(to, 0, moved);
	highlights.value = list;
	handleDragEnd();
	// 拖拽完成调用全量排序 API
	saveOrder();
};

const handleDragEnd = () => {
	dragIndex.value = null;
	dragOverIndex.value = null;
};

/** 将当前列表顺序提交给后端。 */
const saveOrder = async () => {
	const ids = highlights.value.map((item) => item.id);
	if (ids.length === 0) return;
	try {
		await reorderHighlights(ids);
		ElMessage.success("排序已保存");
	} catch (error) {
		ElMessage.error(error instanceof Error ? error.message : "排序保存失败");
		console.error(error);
		load();
	}
};

onMounted(load);
</script>

<style scoped>
.manage-tip { color: #667085; font-size: 13px; }
.highlight-list { min-height: 120px; }
.highlight-error { padding: 48px 0; text-align: center; color: #6b7280; }
.highlight-sort-list { display: grid; gap: 8px; list-style: none; margin: 0; padding: 0; }
.highlight-sort-item { display: flex; align-items: center; gap: 12px; min-width: 0; padding: 10px 12px; background: #fff; border: 1px solid #eaecf0; border-radius: 8px; transition: background 0.2s ease; }
.highlight-sort-item.dragging { background: #f0f6ff; box-shadow: 0 2px 8px rgb(16 24 40 / 10%); }
.highlight-sort-item[draggable="true"] { cursor: grab; }
.highlight-sort-item[draggable="true"]:active { cursor: grabbing; }
.order-badge { flex: none; display: grid; width: 26px; height: 26px; place-items: center; border-radius: 50%; background: #3b82f6; color: #fff; font-size: 13px; font-weight: 600; }
.drag-handle { flex: none; color: #98a2b3; cursor: grab; }
.highlight-cover { flex: none; width: 88px; height: 60px; overflow: hidden; border-radius: 6px; }
.cover-image { width: 100%; height: 100%; }
.cover-placeholder { display: grid; width: 100%; height: 100%; place-items: center; gap: 4px; color: #98a2b3; background: #f8fafc; border: 1px dashed #d0d5dd; border-radius: 6px; font-size: 12px; cursor: pointer; }
.highlight-info { min-width: 0; flex: 1; }
.highlight-title { display: flex; align-items: center; gap: 8px; min-width: 0; }
.title-text { overflow: hidden; color: #1d2939; font-weight: 600; text-overflow: ellipsis; white-space: nowrap; }
.highlight-desc { overflow: hidden; margin: 4px 0 0; color: #667085; font-size: 13px; text-overflow: ellipsis; white-space: nowrap; }
.highlight-date { margin: 4px 0 0; color: #98a2b3; font-size: 12px; }
.highlight-actions { flex: none; display: flex; gap: 4px; }
.add-menu { display: grid; gap: 12px; }
.add-menu-card { display: flex; align-items: center; gap: 14px; padding: 16px; border: 1px solid #eaecf0; border-radius: 10px; background: #fff; text-align: left; cursor: pointer; transition: all 0.2s ease; }
.add-menu-card:hover { border-color: #3b82f6; background: #f0f6ff; }
.add-menu-card .el-icon { flex: none; font-size: 26px; color: #3b82f6; }
.add-menu-card strong { display: block; color: #1d2939; }
.add-menu-card span { display: block; margin-top: 2px; color: #667085; font-size: 13px; }
.project-select { width: 100%; }
.dialog-tip { margin: 8px 0 0; color: #98a2b3; font-size: 12px; }
.date-picker { width: 100%; }
.cover-field { display: grid; gap: 10px; width: 100%; }
.cover-preview { width: 100%; max-width: 320px; height: 180px; object-fit: cover; border-radius: 8px; }
.cover-field .cover-placeholder { height: 180px; }
.cover-actions { display: flex; gap: 8px; }
@media (max-width: 768px) {
  .highlight-sort-item { gap: 8px; padding: 8px; }
  .highlight-cover { width: 64px; height: 48px; }
  .highlight-desc { max-width: 40vw; }
}
</style>

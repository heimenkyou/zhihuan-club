<template>
  <div class="project-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目管理</span>
          <el-button type="primary" @click="openAddDialog" class="add-btn">
            <el-icon><Plus /></el-icon>
            添加项目
          </el-button>
        </div>
      </template>

      <!-- 搜索与筛选 -->
      <div class="search-filter-container">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索项目标题"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch" class="action-button">
          搜索
        </el-button>
        <el-button @click="resetFilter" class="reset-btn action-button">
          重置
        </el-button>
      </div>

      <!-- 项目数据表格 -->
      <el-table
        :data="projectsData"
        class="project-table"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column
          prop="title"
          label="项目标题"
          width="300"
          show-overflow-tooltip
        />
        <el-table-column prop="category" label="项目类型" width="120" />
        <el-table-column label="技术栈" width="200">
          <template #default="scope">
            <div
              v-if="
                scope.row.techStackTags && scope.row.techStackTags.length > 0
              "
              class="tech-stack-tags"
            >
              <el-tag
                size="small"
                v-for="(tech, index) in scope.row.techStackTags.slice(0, 3)"
                :key="index"
                type="primary"
                class="tech-tag"
                >{{ tech }}</el-tag
              >
            </div>
            <span v-else>暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="轮播图数量" width="100">
          <template #default="scope">
            {{
              scope.row.mediaResourceIds && scope.row.mediaResourceIds.length
                ? scope.row.mediaResourceIds.length
                : "0"
            }}
          </template>
        </el-table-column>
        <el-table-column label="奖项数量" width="100">
          <template #default="scope">
            {{
              scope.row.awardIds && scope.row.awardIds.length
                ? scope.row.awardIds.length
                : "0"
            }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.row)"
              :disabled="!scope.row.id"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteProject(scope.row.id)"
              :disabled="!scope.row.id"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container" v-if="totalCount > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalCount"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { Plus } from "@element-plus/icons-vue"
import { useRouter } from "vue-router"
import {
  getProjects,
  deleteProject as deleteProjectApi,
} from "@/services/projectService"

const router = useRouter()

const searchKeyword = ref("")
const projectsData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

/**
 * 加载项目分页数据。
 *
 * @returns {Promise<void>}
 */
const loadProjects = async () => {
  loading.value = true
  try {
    const response = await getProjects({
      current: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value,
    })

    const pageData = response?.data

    if (pageData?.records) {
      projectsData.value = pageData.records
      currentPage.value = Number(pageData.current ?? currentPage.value)
      pageSize.value = Number(pageData.size ?? pageSize.value)
      totalCount.value = Number(pageData.total ?? 0)
    } else {
      projectsData.value = []
      totalCount.value = 0
    }
  } catch (error) {
    ElMessage.error("获取项目信息失败")
    console.error("获取项目信息失败:", error)
    projectsData.value = []
  } finally {
    loading.value = false
  }
}

/**
 * 按关键字重新查询项目。
 */
const handleSearch = () => {
  currentPage.value = 1
  loadProjects()
}

/**
 * 重置搜索条件并刷新列表。
 */
const resetFilter = () => {
  searchKeyword.value = ""
  currentPage.value = 1
  loadProjects()
}

/**
 * 处理分页大小变化。
 *
 * @param {number} size
 */
const handleSizeChange = size => {
  pageSize.value = size
  currentPage.value = 1
  loadProjects()
}

/**
 * 处理页码变化。
 *
 * @param {number} current
 */
const handleCurrentChange = current => {
  currentPage.value = current
  loadProjects()
}

/**
 * 跳转到新增项目页。
 */
const openAddDialog = () => {
  router.push("/admin/projects/edit")
}

/**
 * 跳转到编辑项目页。
 *
 * @param {{ id: number }} row
 */
const handleEdit = row => {
  router.push(`/admin/projects/edit/${row.id}`)
}

/**
 * 删除指定项目。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
const deleteProject = async id => {
  if (!id) {
    ElMessage.warning("项目ID不存在")
    return
  }

  try {
    await ElMessageBox.confirm("确定要删除这个项目吗？", "确认删除", {
      type: "warning",
    })

    loading.value = true
    await deleteProjectApi(id)
    ElMessage.success("删除项目成功")
    loadProjects()
  } catch (error) {
    if (error instanceof Error && error.message !== "cancel") {
      ElMessage.error("删除项目失败")
      console.error("删除项目失败:", error)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 初始化项目管理页。
 */
onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.project-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.add-btn {
  margin-top: 0;
}
.search-filter-container {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
  gap: 10px;
}
.project-table {
  width: 100%;
}
.search-input {
  width: 300px;
}
.action-button {
  margin-left: 0;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.reset-btn {
  background-color: #6c757d;
  color: white;
}
.reset-btn:hover {
  background-color: #5a6268;
  color: white;
}
.tech-stack-tags {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}
.tech-tag {
  margin-right: 4px;
}
.more-tags {
  color: #909399;
  font-size: 12px;
}
</style>

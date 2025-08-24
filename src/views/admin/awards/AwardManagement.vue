<template>
  <div class="award-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>奖项管理</span>
          <el-button type="primary" @click="openAddDialog" class="add-btn">
            添加奖项
          </el-button>
        </div>
      </template>
      <el-form :model="searchForm" class="search-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="搜索">
              <el-input
                v-model="searchForm.keyword"
                placeholder="请输入奖项名称或作品名称"
                suffix-icon="Search"
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="handleSearch" class="search-btn">
              搜索
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
      <el-table
        :data="awardsData.records"
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="奖项名称" width="200" />
        <el-table-column prop="workName" label="作品名称" width="200" />
        <el-table-column prop="level" label="奖项级别" width="120">
          <template #default="scope">
            {{ mapLevelToDisplay(scope.row.level) }}
          </template>
        </el-table-column>
        <el-table-column prop="year" label="获得年份" width="120" />
        <el-table-column prop="date" label="日期" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="openEditDialog(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteAward(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="awardsData.current"
          v-model:page-size="awardsData.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="awardsData.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form
        ref="awardFormRef"
        :model="awardForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item prop="title">
          <el-input v-model="awardForm.title" placeholder="请输入奖项名称" />
        </el-form-item>
        <el-form-item prop="workName">
          <el-input v-model="awardForm.workName" placeholder="请输入作品名称" />
        </el-form-item>
        <el-form-item prop="level">
          <el-select v-model="awardForm.level" placeholder="请选择奖项级别">
            <el-option label="国家级" value="national" />
            <el-option label="省级" value="provincial" />
            <el-option label="校级" value="school" />
          </el-select>
        </el-form-item>
        <el-form-item prop="year">
          <el-select v-model="awardForm.year" placeholder="请选择获得年份">
            <el-option label="2023" value="2023" />
            <el-option label="2022" value="2022" />
            <el-option label="2021" value="2021" />
          </el-select>
        </el-form-item>
        <el-form-item prop="date">
          <el-date-picker
            v-model="awardForm.date"
            type="date"
            placeholder="请选择日期"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox, ElForm } from "element-plus"
import {
  getAwards,
  createAward,
  updateAward,
  deleteAward as deleteAwardApi,
  type ApplicationPageData,
  type AwardItem,
} from "../../../services/adminService"

type ElFormInstance = InstanceType<typeof ElForm>

// 级别映射函数 - 将英文级别转换为中文显示
const mapLevelToDisplay = (level: string): string => {
  const levelMap: Record<string, string> = {
    national: "国家级",
    provincial: "省级",
    school: "校级",
  }
  return levelMap[level.toLowerCase()] || "其他"
}

const searchForm = reactive<{ keyword: string }>({
  keyword: "",
})
const awardsData = ref<ApplicationPageData<AwardItem>>({
  current: 1,
  size: 10,
  total: 0,
  pages: 0,
  records: [],
})
const dialogVisible = ref(false)
const dialogTitle = ref("添加奖项")
const awardForm = reactive<Partial<AwardItem>>({
  title: "",
  workName: "",
  level: "national",
  year: "2023",
  date: new Date().toISOString().split("T")[0],
})
const currentAwardId = ref<number | null>(null)
const rules = {
  title: [{ required: true, message: "请输入奖项名称", trigger: "blur" }],
  workName: [{ required: true, message: "请输入作品名称", trigger: "blur" }],
  level: [{ required: true, message: "请选择奖项级别", trigger: "blur" }],
  year: [{ required: true, message: "请选择获得年份", trigger: "blur" }],
  date: [{ required: true, message: "请选择日期", trigger: "blur" }],
}
const loading = ref(false)
const awardFormRef = ref<ElFormInstance | null>(null)

// 加载奖项数据
const loadAwards = async () => {
  loading.value = true
  try {
    const params = {
      current: awardsData.value.current,
      size: awardsData.value.size,
      keyword: searchForm.keyword,
    }
    const data = await getAwards(params)
    // 确保data不为undefined且包含必要字段
    if (data && typeof data === "object") {
      awardsData.value = {
        current: data.current || 1,
        size: data.size || 10,
        total: data.total || 0,
        pages: data.pages || 0,
        records: data.records || [],
      }
    } else {
      // 数据格式不正确时重置为默认值
      awardsData.value = {
        current: 1,
        size: 10,
        total: 0,
        pages: 0,
        records: [],
      }
      ElMessage.error("获取的奖项数据格式不正确")
    }
  } catch (error) {
    ElMessage.error("获取奖项信息失败")
    console.error("获取奖项信息失败:", error)
    // 发生错误时确保awardsData有有效值
    awardsData.value = {
      current: 1,
      size: 10,
      total: 0,
      pages: 0,
      records: [],
    }
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  awardsData.value.current = 1
  loadAwards()
}

// 重置表单
const resetForm = () => {
  searchForm.keyword = ""
  awardsData.value.current = 1
  loadAwards()
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  awardsData.value.size = size
  loadAwards()
}

// 当前页码变化
const handleCurrentChange = (current: number) => {
  awardsData.value.current = current
  loadAwards()
}

// 打开添加对话框
const openAddDialog = () => {
  dialogTitle.value = "添加奖项"
  currentAwardId.value = null
  // 重置表单
  Object.keys(awardForm).forEach((key) => {
    if (key === "date") {
      awardForm[key as keyof AwardItem] = new Date()
        .toISOString()
        .split("T")[0] as any
    } else if (key === "level") {
      awardForm[key as keyof AwardItem] = "national" as any
    } else if (key === "year") {
      awardForm[key as keyof AwardItem] = "2023" as any
    } else {
      awardForm[key as keyof AwardItem] = "" as any
    }
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row: AwardItem) => {
  dialogTitle.value = "编辑奖项"
  currentAwardId.value = row.id
  // 确保正确复制所有字段，特别是日期字段
  awardForm.title = row.title
  awardForm.workName = row.workName
  awardForm.level = row.level
  awardForm.year = row.year
  // 确保日期是正确的字符串格式
  awardForm.date =
    typeof row.date === "string"
      ? row.date
      : new Date(row.date).toISOString().split("T")[0]
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  if (!awardFormRef.value) return

  try {
    await awardFormRef.value.validate()
    loading.value = true

    if (currentAwardId.value) {
      // 编辑奖项
      // 修复：确保传递正确格式的数据
      // const updateData = {
      //   title: awardForm.title,
      //   workName: awardForm.workName,
      //   level: awardForm.level,
      //   year: awardForm.year,
      //   date: awardForm.date
      // }
      await updateAward(currentAwardId.value, {
        title: awardForm.title || "",
        workName: awardForm.workName || "",
        level: awardForm.level || "national",
        year: awardForm.year || "2023",
        date: awardForm.date || new Date().toISOString().split("T")[0],
      })
      ElMessage.success("更新奖项成功")
    } else {
      // 添加奖项
      await createAward(awardForm as Omit<AwardItem, "id">)
      ElMessage.success("添加奖项成功")
    }

    dialogVisible.value = false
    loadAwards()
  } catch (error) {
    ElMessage.error(currentAwardId.value ? "更新奖项失败" : "添加奖项失败")
    console.error("提交表单失败:", error)
  } finally {
    loading.value = false
  }
}

// 删除奖项
const deleteAward = async (id: number) => {
  try {
    await ElMessageBox.confirm("确定要删除这个奖项吗？", "确认删除", {
      type: "warning",
    })

    loading.value = true
    await deleteAwardApi(id)
    ElMessage.success("删除奖项成功")
    loadAwards()
  } catch (error) {
    ElMessage.error("删除奖项失败")
    console.error("删除奖项失败:", error)
  } finally {
    loading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadAwards()
})
</script>

<style scoped>
.award-container {
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
.search-form {
  margin-bottom: 20px;
}
.search-btn {
  margin-right: 10px;
}
.desc-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

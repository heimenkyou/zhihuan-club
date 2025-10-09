<template>
  <div class="code-submit-page min-h-screen">
    <CommonNavbar />
    <main
      class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6 mt-14 mobile:py-4 mobile:mt-12"
    >
      <!-- 页面标题 -->
      <div class="text-center mb-8 animate-fade-in">
        <h1 class="text-3xl font-bold text-dark mb-3">代码与视频提交</h1>
        <p class="text-gray-500 max-w-2xl mx-auto">
          请提交您的代码文件和相关视频，我们将尽快审核
        </p>
      </div>

      <!-- 提交表单 -->
      <el-card class="mb-4">
        <template #header>
          <div class="card-header">
            <span>提交表单</span>
          </div>
        </template>
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="学号" prop="studentId">
            <el-input
              v-model="formData.studentId"
              placeholder="请输入学号"
              maxlength="32"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="formData.name"
              placeholder="请输入姓名"
              maxlength="32"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="项目描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              placeholder="简要描述你的项目功能"
              :rows="3"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="代码文件" prop="codeFile">
            <el-upload
              ref="codeFileUploadRef"
              v-model:file-list="formData.codeFileList"
              class="upload-demo"
              action=""
              accept=".zip,.rar,.7z"
              :auto-upload="false"
              :on-change="handleCodeFileChange"
              :before-remove="beforeRemove"
              :show-file-list="false"
            >
              <el-button type="primary">选择压缩包</el-button>
              <template #tip>
                <div class="el-upload__tip text-sm text-gray-500">
                  请上传zip/rar/7z格式的压缩包，要求解压后直接是项目根目录<br />
                  命名格式：学号-姓名.zip (系统会自动重命名)
                </div>
              </template>
            </el-upload>
            <div
              v-if="formData.codeFile"
              class="file-info mt-2 text-sm text-gray-600"
            >
              已选择: {{ formData.codeFile.name }}
            </div>
          </el-form-item>
          <el-form-item label="视频文件" prop="videoFile">
            <el-upload
              ref="videoFileUploadRef"
              v-model:file-list="formData.videoFileList"
              class="upload-demo"
              action=""
              accept=".mp4,.avi,.mov"
              :auto-upload="false"
              :on-change="handleVideoFileChange"
              :before-remove="beforeRemove"
              :show-file-list="false"
            >
              <el-button type="primary">选择视频文件</el-button>
              <template #tip>
                <div class="el-upload__tip text-sm text-gray-500">
                  请上传1-5分钟的演示视频 (mp4/mov/avi格式)
                </div>
              </template>
            </el-upload>
            <div
              v-if="formData.videoFile"
              class="file-info mt-2 text-sm text-gray-600"
            >
              已选择: {{ formData.videoFile.name }}
            </div>
          </el-form-item>
          <el-form-item>
            <el-button
              type="success"
              @click="handleSubmit"
              :loading="isSubmitting"
              :disabled="!formData.codeFile || !formData.videoFile"
            >
              提交作业
            </el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 压缩包规范说明 -->
      <el-card class="mb-4">
        <template #header>
          <div class="card-header">
            <span>提交规范说明</span>
          </div>
        </template>
        <el-alert type="info" :closable="false">
          <div>
            <p><strong>压缩包结构要求：</strong></p>
            <ul>
              <li>压缩包打开后直接是项目根目录，不能套文件夹</li>
              <li>必须包含 index.html 文件</li>
              <li>项目应该能直接在浏览器中运行</li>
            </ul>
            <p><strong>正确示例：</strong></p>
            <pre class="bg-gray-100 p-3 rounded text-sm">
20230101-张三.zip
 ├── index.html
 ├── css/
 │   └── style.css
 ├── js/
 │   └── script.js
 └── assets/
     └── images/</pre
            >
            <p><strong>错误示例：</strong></p>
            <pre class="bg-gray-100 p-3 rounded text-sm">
20230101-张三.zip
 └── 我的项目/          ← 不要这一层！
     ├── index.html
     ├── css/
     └── js/</pre
            >
          </div>
        </el-alert>
      </el-card>

      <!-- 提交指南 -->
      <el-card>
        <template #header>
          <div class="card-header">
            <span>提交指南</span>
          </div>
        </template>
        <div class="guide-content">
          <ul class="list-disc pl-5 space-y-2 text-gray-700">
            <li>代码文件请压缩为ZIP、RAR等格式上传，大小不超过50MB</li>
            <li>视频文件请确保清晰可见，长度控制在1-5分钟，大小不超过200MB</li>
            <li>请填写真实的学号和姓名，以便我们联系您</li>
            <li>项目描述请尽可能详细，说明项目的功能、技术栈和实现难点</li>
            <li>提交后我们会在3个工作日内进行审核</li>
            <li>如有疑问，请联系相关负责人</li>
          </ul>
        </div>
      </el-card>
    </main>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue'
  import { ElMessage } from 'element-plus'
  import type { UploadFile } from 'element-plus'
  import CommonNavbar from '@/components/CommonNavbar.vue'
  import { submitUserCode } from '@/services/codeSubmissionService'

  // 表单引用
  const formRef = ref()
  const codeFileUploadRef = ref()
  const videoFileUploadRef = ref()
  const isSubmitting = ref(false)

  // 表单数据
  const formData = reactive({
    studentId: '',
    name: '',
    description: '',
    codeFile: null as File | null,
    codeFileList: [] as UploadFile[],
    videoFile: null as File | null,
    videoFileList: [] as UploadFile[],
  })

  // 表单验证规则
  const rules = {
    studentId: [
      { required: true, message: '请输入学号', trigger: 'blur' },
      {
        pattern: /^[0-9A-Za-z]{6,20}$/,
        message: '学号格式不正确',
        trigger: 'blur',
      },
    ],
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      {
        min: 2,
        max: 20,
        message: '姓名长度在 2 到 20 个字符',
        trigger: 'blur',
      },
    ],
    description: [
      { required: true, message: '请输入项目描述', trigger: 'blur' },
      { min: 10, message: '项目描述至少10个字符', trigger: 'blur' },
    ],
    codeFile: [
      { required: true, message: '请上传代码文件', trigger: 'change' },
    ],
    videoFile: [
      { required: true, message: '请上传视频文件', trigger: 'change' },
    ],
  }

  // 处理代码文件选择
  const handleCodeFileChange = (file: UploadFile) => {
    // 验证文件大小不超过50MB
    if (file.size && file.size > 50 * 1024 * 1024) {
      ElMessage.error('代码文件大小不能超过50MB')
      formData.codeFileList = []
      formData.codeFile = null
      return false
    }

    // 验证文件类型
    const validTypes = ['.zip', '.rar', '.7z']
    const fileExtension = file.name?.substring(file.name.lastIndexOf('.')) || ''
    if (!validTypes.includes(fileExtension.toLowerCase())) {
      ElMessage.error('请上传zip、rar或7z格式的压缩包')
      formData.codeFileList = []
      formData.codeFile = null
      return false
    }

    formData.codeFileList = [file]
    formData.codeFile = file.raw as File
    return false
  }

  // 处理视频文件选择
  const handleVideoFileChange = (file: UploadFile) => {
    // 验证文件大小不超过200MB
    if (file.size && file.size > 200 * 1024 * 1024) {
      ElMessage.error('视频文件大小不能超过200MB')
      formData.videoFileList = []
      formData.videoFile = null
      return false
    }

    // 验证文件类型
    const validTypes = ['.mp4', '.avi', '.mov']
    const fileExtension = file.name?.substring(file.name.lastIndexOf('.')) || ''
    if (!validTypes.includes(fileExtension.toLowerCase())) {
      ElMessage.error('请上传mp4、avi或mov格式的视频文件')
      formData.videoFileList = []
      formData.videoFile = null
      return false
    }

    formData.videoFileList = [file]
    formData.videoFile = file.raw as File
    return false
  }

  // 移除文件前的钩子
  const beforeRemove = (file: UploadFile, uploadFiles: UploadFile[]) => {
    if (file.raw === formData.codeFile) {
      formData.codeFile = null
    } else if (file.raw === formData.videoFile) {
      formData.videoFile = null
    }
    return true
  }

  // 提交表单
  const handleSubmit = async () => {
    if (!formRef.value) return

    try {
      // 表单验证
      await formRef.value.validate()

      if (!formData.codeFile || !formData.videoFile) {
        ElMessage.error('请上传完整的代码文件和视频文件')
        return
      }

      isSubmitting.value = true

      // 构建FormData
      const formDataToSubmit = new FormData()
      formDataToSubmit.append('studentId', formData.studentId)
      formDataToSubmit.append('name', formData.name)
      formDataToSubmit.append('description', formData.description)
      formDataToSubmit.append('codeFile', formData.codeFile)
      formDataToSubmit.append('videoFile', formData.videoFile)

      // 调用API提交数据
      const response = await submitUserCode(formDataToSubmit)

      ElMessage.success('提交成功！我们将尽快审核您的作品')
      handleReset()
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '未知错误'
      ElMessage.error(`提交失败: ${errorMessage}`)
      console.error('提交失败:', error)
    } finally {
      isSubmitting.value = false
    }
  }

  // 重置表单
  const handleReset = () => {
    if (formRef.value) {
      formRef.value.resetFields()
    }
    formData.codeFileList = []
    formData.videoFileList = []
    formData.codeFile = null
    formData.videoFile = null
  }
</script>

<style scoped>
  .code-submit-page {
    background-color: #f5f5f5;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .guide-content {
    padding: 10px 0;
  }

  .upload-demo {
    width: 100%;
  }
</style>

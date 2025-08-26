<template>
  <div class="join-page min-h-screen bg-gray-50 font-sans">
    <!-- 引入通用导航栏（前置显示） -->
    <CommonNavbar />
    <!-- 主内容区：适配CommonNavbar高度，避免遮挡 -->
    <main class="container mx-auto pt-20 md:pt-28 px-4 pb-16 mt-[60px] md:mt-0">
      <!-- Hero 区域 -->
      <section
        id="home"
        class="py-10 md:py-20 text-center opacity-0 transition-opacity duration-500"
        ref="homeRef"
      >
        <h1 class="text-4xl md:text-5xl font-bold text-dark mb-6">
          加入我们，<span class="text-primary">共创未来</span>
        </h1>
        <p class="text-xl text-gray-600 max-w-3xl mx-auto mb-10">
          这里是培养技术人才摇篮，我们期待有激情、有梦想的你加入！
        </p>
        <a
          href="#form"
          class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-full shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
          >立即报名</a
        >
      </section>

      <!-- 招新亮点 -->
      <section
        id="highlights"
        class="py-16 md:py-24 opacity-0 transition-opacity duration-500"
        ref="highlightsRef"
      >
        <h2 class="text-3xl md:text-4xl font-bold text-center text-dark mb-16">
          招新亮点
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- 亮点 1 -->
          <div
            class="bg-white rounded-xl shadow-md hover:shadow-xl p-8 transition-shadow duration-300 border-t-4 border-primary"
          >
            <div
              class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mb-6 mx-auto"
            >
              <i class="fa fa-code text-primary text-2xl"></i>
            </div>
            <h3 class="text-xl font-semibold text-center text-dark mb-4">
              专业技术指导
            </h3>
            <p class="text-gray-600 text-center">
              由经验丰富的学长学姐提供专业技术指导，从零开始学习编程和项目开发。
            </p>
          </div>

          <!-- 亮点 2 -->
          <div
            class="bg-white rounded-xl shadow-md hover:shadow-xl p-8 transition-shadow duration-300 border-t-4 border-primary"
          >
            <div
              class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mb-6 mx-auto"
            >
              <i class="fa fa-users text-primary text-2xl"></i>
            </div>
            <h3 class="text-xl font-semibold text-center text-dark mb-4">
              团队协作
            </h3>
            <p class="text-gray-600 text-center">
              参与实际项目开发，锻炼团队协作能力，积累项目经验，提升综合竞争力。
            </p>
          </div>

          <!-- 亮点 3 -->
          <div
            class="bg-white rounded-xl shadow-md hover:shadow-xl p-8 transition-shadow duration-300 border-t-4 border-primary"
          >
            <div
              class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mb-6 mx-auto"
            >
              <i class="fa fa-trophy text-primary text-2xl"></i>
            </div>
            <h3 class="text-xl font-semibold text-center text-dark mb-4">
              竞赛机会
            </h3>
            <p class="text-gray-600 text-center">
              有机会参加各类科技创新竞赛，获得奖项和荣誉，为个人简历增添亮点。
            </p>
          </div>
        </div>
      </section>

      <!-- 招新流程 -->
      <section
        id="process"
        class="py-16 md:py-24 opacity-0 transition-opacity duration-500"
        ref="processRef"
      >
        <h2 class="text-3xl md:text-4xl font-bold text-center text-dark mb-16">
          招新流程
        </h2>
        <div class="relative max-w-4xl mx-auto">
          <!-- 连接线 -->
          <div
            class="hidden md:block absolute left-1/2 transform -translate-x-1/2 h-full w-1 bg-gray-200"
          ></div>

          <!-- 步骤 1 -->
          <div
            class="flex flex-col md:flex-row items-center mb-12 md:mb-24 relative"
          >
            <div class="md:w-1/2 md:pr-12 md:text-right mb-6 md:mb-0">
              <h3 class="text-xl font-semibold text-dark mb-2">线上报名</h3>
              <p class="text-gray-600">
                填写在线报名表单，提交个人基本信息和特长爱好。
              </p>
            </div>
            <div
              class="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center z-10 font-bold text-lg"
            >
              <i class="fa fa-file-text-o"></i>
            </div>
            <div class="md:w-1/2 md:pl-12 hidden md:block"></div>
          </div>

          <!-- 步骤 2 -->
          <div
            class="flex flex-col md:flex-row items-center mb-12 md:mb-24 relative"
          >
            <div class="md:w-1/2 md:pr-12 hidden md:block"></div>
            <div
              class="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center z-10 font-bold text-lg"
            >
              <i class="fa fa-comments-o"></i>
            </div>
            <div class="md:w-1/2 md:pl-12 mt-6 md:mt-0">
              <h3 class="text-xl font-semibold text-dark mb-2">简单面试</h3>
              <p class="text-gray-600">
                主要了解学习意愿和自主学习能力，无需担心技术水平。
              </p>
            </div>
          </div>

          <!-- 步骤 3 -->
          <div
            class="flex flex-col md:flex-row items-center mb-12 md:mb-24 relative"
          >
            <div class="md:w-1/2 md:pr-12 md:text-right mb-6 md:mb-0">
              <h3 class="text-xl font-semibold text-dark mb-2">结果公布</h3>
              <p class="text-gray-600">
                通过面试的同学将收到正式加入的通知，并安排后续培训。
              </p>
            </div>
            <div
              class="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center z-10 font-bold text-lg"
            >
              <i class="fa fa-check-circle-o"></i>
            </div>
            <div class="md:w-1/2 md:pl-12 hidden md:block"></div>
          </div>

          <!-- 步骤 4 -->
          <div class="flex flex-col md:flex-row items-center relative">
            <div class="md:w-1/2 md:pr-12 hidden md:block"></div>
            <div
              class="w-12 h-12 rounded-full bg-primary text-white flex items-center justify-center z-10 font-bold text-lg"
            >
              <i class="fa fa-users"></i>
            </div>
            <div class="md:w-1/2 md:pl-12 mt-6 md:mt-0">
              <h3 class="text-xl font-semibold text-dark mb-2">正式加入</h3>
              <p class="text-gray-600">
                开始参与部门活动和项目开发，开启你的技术成长之旅。
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- 部门介绍 -->
      <section
        id="departments"
        class="py-16 md:py-24 opacity-0 transition-opacity duration-500"
        ref="departmentsRef"
      >
        <h2 class="text-3xl md:text-4xl font-bold text-center text-dark mb-16">
          部门介绍
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <!-- 算法部 -->
          <div
            class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"
          >
            <div class="h-2 bg-purple-500"></div>
            <div class="p-8">
              <h3
                class="text-xl font-semibold text-dark mb-4 flex items-center"
              >
                <span
                  class="w-8 h-8 rounded-full bg-purple-100 flex items-center justify-center mr-3"
                >
                  <i class="fa fa-brain text-purple-500"></i>
                </span>
                算法部
              </h3>
              <p class="text-gray-600 mb-6">
                专注于算法设计与实现，参加各类算法竞赛，提升编程能力和逻辑思维。
              </p>
              <div class="mb-6">
                <h4 class="font-semibold text-dark mb-2">招新要求：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>对算法和编程有兴趣</li>
                  <li>具备一定的逻辑思维能力</li>
                  <li>能够自主学习</li>
                </ul>
              </div>
              <div>
                <h4 class="font-semibold text-dark mb-2">学习内容：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>数据结构与算法</li>
                  <li>Python/Java/C++编程</li>
                  <li>机器学习基础</li>
                  <li>算法竞赛技巧</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 项目竞赛部 -->
          <div
            class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"
          >
            <div class="h-2 bg-blue-500"></div>
            <div class="p-8">
              <h3
                class="text-xl font-semibold text-dark mb-4 flex items-center"
              >
                <span
                  class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center mr-3"
                >
                  <i class="fa fa-laptop-code text-blue-500"></i>
                </span>
                项目竞赛部
              </h3>
              <p class="text-gray-600 mb-6">
                负责项目开发和管理，参加各类项目竞赛，提升团队协作和项目管理能力。
              </p>
              <div class="mb-6">
                <h4 class="font-semibold text-dark mb-2">招新要求：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>对软件开发有兴趣</li>
                  <li>具备团队协作精神</li>
                  <li>有responsibility and execution</li>
                </ul>
              </div>
              <div>
                <h4 class="font-semibold text-dark mb-2">学习内容：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>前端/后端开发技术</li>
                  <li>数据库设计与应用</li>
                  <li>项目管理方法</li>
                  <li>竞赛项目开发技巧</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 综合管理部 -->
          <div
            class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"
          >
            <div class="h-2 bg-green-500"></div>
            <div class="p-8">
              <h3
                class="text-xl font-semibold text-dark mb-4 flex items-center"
              >
                <span
                  class="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center mr-3"
                >
                  <i class="fa fa-users-cog text-green-500"></i>
                </span>
                综合管理部
              </h3>
              <p class="text-gray-600 mb-6">
                负责团队日常管理、活动策划和对外宣传，提升组织协调和沟通能力。
              </p>
              <div class="mb-6">
                <h4 class="font-semibold text-dark mb-2">招新要求：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>具备良好的沟通能力</li>
                  <li>有responsibility and execution</li>
                  <li>擅长组织策划活动</li>
                </ul>
              </div>
              <div>
                <h4 class="font-semibold text-dark mb-2">学习内容：</h4>
                <ul class="list-disc list-inside text-gray-600 space-y-1">
                  <li>活动策划与组织</li>
                  <li>团队管理方法</li>
                  <li>宣传和文案写作</li>
                  <li>沟通与协调技巧</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 报名表单 -->
      <section
        id="form"
        class="py-16 md:py-24 opacity-0 transition-opacity duration-500"
        ref="formRef"
      >
        <h2 class="text-3xl md:text-4xl font-bold text-center text-dark mb-16">
          在线报名
        </h2>
        <div
          class="max-w-3xl mx-auto bg-white rounded-xl shadow-md p-8 md:p-12"
        >
          <form id="recruitmentForm" @submit.prevent="handleSubmit">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <label for="name" class="block text-gray-700 font-medium mb-2"
                  >姓名</label
                >
                <input
                  type="text"
                  id="name"
                  name="name"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  placeholder="请输入您的姓名"
                  v-model="formData.name"
                />
              </div>
              <div>
                <label
                  for="studentId"
                  class="block text-gray-700 font-medium mb-2"
                  >学号</label
                >
                <input
                  type="text"
                  id="studentId"
                  name="studentId"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  placeholder="请输入您的学号"
                  v-model="formData.studentId"
                />
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <label for="major" class="block text-gray-700 font-medium mb-2"
                  >专业</label
                >
                <input
                  type="text"
                  id="major"
                  name="major"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  placeholder="请输入您的专业"
                  v-model="formData.major"
                />
              </div>
              <!-- 年级字段已删除 -->
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <label for="phone" class="block text-gray-700 font-medium mb-2"
                  >手机号码</label
                >
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  placeholder="请输入您的手机号码"
                  v-model="formData.phone"
                />
              </div>
              <div>
                <label
                  for="QQNumber"
                  class="block text-gray-700 font-medium mb-2"
                  >QQ号</label
                >
                <input
                  type="text"
                  id="QQNumber"
                  name="QQNumber"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  placeholder="请输入您的QQ号"
                  v-model="formData.QQNumber"
                  @input="validateQQNumber"
                />
                <p v-if="qqNumberError" class="text-red-500 text-sm mt-1">
                  {{ qqNumberError }}
                </p>
              </div>
            </div>

            <!-- 邮箱字段已删除 -->

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
              <div>
                <label
                  for="department"
                  class="block text-gray-700 font-medium mb-2"
                  >第一意向部门</label
                >
                <select
                  id="department"
                  name="department"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  required
                  v-model="formData.department"
                >
                  <option value="">请选择意向部门</option>
                  <option value="algorithm">算法部</option>
                  <option value="project">项目竞赛部</option>
                  <option value="management">综合管理部</option>
                </select>
              </div>
              <div>
                <label
                  for="secondDepartment"
                  class="block text-gray-700 font-medium mb-2"
                  >第二意向部门</label
                >
                <select
                  id="secondDepartment"
                  name="secondDepartment"
                  class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                  v-model="formData.secondDepartment"
                >
                  <option value="">无</option>
                  <option value="algorithm">算法部</option>
                  <option value="project">项目竞赛部</option>
                  <option value="management">综合管理部</option>
                </select>
              </div>
            </div>

            <div class="mb-6">
              <label
                class="block text-gray-700 font-medium mb-2"
                for="interests"
                >兴趣方向（可多选）</label
              >
              <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3">
                <label
                  class="inline-flex items-center p-2 border rounded-lg hover:bg-gray-50 transition-colors"
                  v-for="interest in interests"
                  :key="interest.value"
                >
                  <input
                    type="checkbox"
                    name="interests"
                    :value="interest.value"
                    class="w-4 h-4 text-primary focus:ring-primary border-gray-300 rounded"
                    v-model="formData.interests"
                  />
                  <span class="ml-2 text-gray-600 text-sm">{{
                    interest.label
                  }}</span>
                </label>
              </div>
            </div>

            <div class="mb-6">
              <label for="reason" class="block text-gray-700 font-medium mb-2"
                >加入原因</label
              >
              <textarea
                id="reason"
                name="reason"
                rows="4"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                placeholder="请简要描述您加入我们的原因"
                v-model="formData.reason"
              ></textarea>
            </div>

            <div class="mb-8">
              <label
                for="introduction"
                class="block text-gray-700 font-medium mb-2"
                >自我介绍</label
              >
              <textarea
                id="introduction"
                name="introduction"
                rows="4"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition-colors duration-200"
                required
                placeholder="请简要介绍您自己，包括兴趣爱好、特长等"
                v-model="formData.introduction"
              ></textarea>
            </div>

            <button
              type="submit"
              class="w-full bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-lg shadow-md hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1 flex items-center justify-center"
            >
              <i class="fa fa-paper-plane mr-2"></i> 提交报名
            </button>
          </form>
        </div>
      </section>

      <!-- 常见问题 -->
      <section
        id="faq"
        class="py-16 md:py-24 opacity-0 transition-opacity duration-500"
        ref="faqRef"
      >
        <h2 class="text-3xl md:text-4xl font-bold text-center text-dark mb-16">
          常见问题
        </h2>
        <div
          class="max-w-3xl mx-auto bg-white rounded-xl shadow-md p-8 md:p-12"
        >
          <div class="space-y-6">
            <!-- FAQ 列表：循环渲染，用响应式状态管理展开/折叠 -->
            <div
              class="border-b border-gray-100 pb-4"
              v-for="(faq, index) in faqList"
              :key="index"
            >
              <button
                class="flex items-center justify-between w-full text-left"
                @click="toggleFaq(index)"
              >
                <h3 class="text-lg font-semibold text-dark">
                  {{ faq.question }}
                </h3>
                <i
                  class="fa fa-chevron-down text-gray-500 transition-transform"
                  :class="{ 'rotate-180': faq.isOpen }"
                ></i>
              </button>
              <div
                class="faq-content hidden mt-3 text-gray-600"
                :class="{ hidden: !faq.isOpen }"
              >
                {{ faq.answer }}
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import CommonNavbar from "../components/CommonNavbar.vue" // 引入通用导航栏
import { ref, onMounted } from "vue"
// 导入 submitApplication 函数
import { submitApplication } from "../services/applicationsService"
type ElementRef = HTMLElement | null

// 1. 响应式状态定义
// 表单数据状态（类型化管理）-------------申请表收集
interface FormData {
  name: string
  studentId: string
  major: string
  phone: string
  QQNumber: string
  department: string // algorithm/project/management
  secondDepartment: string // algorithm/project/management/""
  interests: string[] // 兴趣方向（多选）
  reason: string
  introduction: string
}
const formData = ref<FormData>({
  name: "",
  studentId: "",
  major: "",
  phone: "",
  QQNumber: "",
  department: "",
  secondDepartment: "",
  interests: [],
  reason: "",
  introduction: "",
})

// 兴趣方向选项（统一管理）
const interests = ref([
  { label: "编程", value: "programming" },
  { label: "设计", value: "design" },
  { label: "算法", value: "algorithm" },
  { label: "数据库", value: "database" },
  { label: "前端开发", value: "frontend" },
  { label: "后端开发", value: "backend" },
  { label: "PPT", value: "ppt" },
  { label: "演讲", value: "speech" },
  { label: "视频剪辑", value: "video_editing" },
  { label: "无", value: "none" },
])

// QQ号校验状态
const qqNumberError = ref("")

// QQ号校验方法
const validateQQNumber = () => {
  const qq = formData.value.QQNumber
  if (!/^\d+$/.test(qq)) {
    qqNumberError.value = "QQ号必须为纯数字"
  } else if (qq.length < 5 || qq.length > 11) {
    qqNumberError.value = "QQ号长度必须在5-11位之间"
  } else {
    qqNumberError.value = ""
  }
}

// FAQ状态（类型化管理）---------问题
interface FaqItem {
  question: string
  answer: string
  isOpen: boolean
}
const faqList = ref<FaqItem[]>([
  {
    question: "加入有什么条件？",
    answer:
      "我们对技术水平没有严格要求，只要你对技术有兴趣，有学习的意愿和热情，都可以加入我们。我们更看重的是学习态度和自主学习能力。",
    isOpen: false,
  },
  {
    question: "每周需要投入多少时间？",
    answer:
      "通常情况下，每周需要投入3-5小时参与团队活动和项目开发。具体时间会根据项目需求和个人情况有所不同。我们鼓励成员合理安排时间，平衡学习和社团活动。",
    isOpen: false,
  },
  {
    question: "加入能学到什么？",
    answer:
      "1. 掌握专业的编程技术和项目开发经验；\n2. 参与实际项目开发，积累项目经验；\n3. 有机会参加各类科技创新竞赛，获得奖项和荣誉；\n4. 认识志同道合的同学，建立深厚友谊；\n5. 获得实习和就业机会，为未来职业发展打下基础。",
    isOpen: false,
  },
  {
    question: "如何选择适合自己的部门？",
    answer:
      "选择部门时，可以根据自己的兴趣爱好和职业规划来考虑。如果你专注算法类竞赛，可以选择算法部；如果你更喜欢做项目并参加项目类竞赛，可以选择项目竞赛部；如果你擅长组织管理和活动策划，可以选择综合管理部。所有部门都没有技术要求，主要看重学习意愿和自主学习能力。",
    isOpen: false,
  },
  {
    question: "招新流程和时间安排是怎样的？",
    answer:
      "招新流程主要包括：线上报名、简单面试、正式加入三个环节。具体时间安排如下：\n1. 线上报名：9月1日-9月15日\n2. 简单面试：9月16日-9月20日（主要了解学习意愿和自主学习能力）\n3. 正式加入：9月21日公布结果\n请关注我们的微信公众号获取最新的招新信息和时间安排。",
    isOpen: false,
  },
])

// 页面section的Ref（用于滚动动画）
const homeRef = ref<ElementRef | null>(null)
const highlightsRef = ref<ElementRef | null>(null)
const processRef = ref<ElementRef | null>(null)
const departmentsRef = ref<ElementRef | null>(null)
const formRef = ref<ElementRef | null>(null)
const faqRef = ref<ElementRef | null>(null)

// 2. 方法定义

// 滚动动画：监听section进入视口
const observeSections = () => {
  const observerOptions: IntersectionObserverInit = {
    root: null,
    rootMargin: "0px",
    threshold: 0.1,
  }

  // 定义观察者回调（添加类型）
  const observerCallback: IntersectionObserverCallback = (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        // 添加动画类
        entry.target.classList.add("animate-slide-up")
        observer.unobserve(entry.target)
      }
    })
  }

  const observer = new IntersectionObserver(observerCallback, observerOptions)

  // 监听所有section（获取DOM元素并过滤null）
  const sections = [
    homeRef.value,
    highlightsRef.value,
    processRef.value,
    departmentsRef.value,
    formRef.value,
    faqRef.value,
  ].filter(Boolean) as Element[]

  sections.forEach((section) => observer.observe(section))
}

// FAQ展开/折叠（通过索引控制，替代原DOM查找）
const toggleFaq = (index: number) => {
  faqList.value[index].isOpen = !faqList.value[index].isOpen
}

// 表单提交处理（类型化表单数据）
const handleSubmit = async () => {
  // 1. 表单数据校验
  if (
    !formData.value.name ||
    !formData.value.studentId ||
    !formData.value.major ||
    !formData.value.phone ||
    !formData.value.QQNumber ||
    !formData.value.department ||
    !formData.value.introduction
  ) {
    alert("请填写必填字段！")
    return
  }

  // 2. QQ号校验
  validateQQNumber()
  if (qqNumberError.value) {
    alert(qqNumberError.value)
    return
  }

  try {
    // 3. 显示加载状态
    const submitButton = document.querySelector('button[type="submit"]')
    if (submitButton) {
      if (submitButton instanceof HTMLButtonElement) {
        submitButton.disabled = true
      }
      submitButton.innerHTML =
        '<i class="fa fa-spinner fa-spin mr-2"></i> 提交中...'
    }

    // 4. 使用封装的 axios 请求提交表单
    await submitApplication(formData.value)

    // 5. 处理成功响应
    alert("报名信息提交成功！我们将尽快与您联系，请保持手机畅通。")
    // 重置表单
    formData.value = {
      name: "",
      studentId: "",
      major: "",
      phone: "",
      QQNumber: "",
      department: "",
      secondDepartment: "",
      interests: [],
      reason: "",
      introduction: "",
    }
  } catch (error) {
    console.error("提交失败:", error)
    alert(
      `提交失败: ${
        error instanceof Error ? error.message : '网络请求错误，请稍后重试。'
      }`
    )
  } finally {
    // 恢复按钮状态
    const submitButton = document.querySelector('button[type="submit"]')
    if (submitButton) {
      if (submitButton instanceof HTMLButtonElement) {
        submitButton.disabled = false
      }
      submitButton.innerHTML = '<i class="fa fa-paper-plane mr-2"></i> 提交报名'
    }
  }
}

// 3. 生命周期钩子
onMounted(() => {
  // 页面加载后初始化滚动动画监听
  observeSections()
})
</script>

<style scoped>
/* 基础布局：适配CommonNavbar高度，避免内容遮挡 */
.join-page {
  padding-top: 0 !important;
}

/* 自定义动画效果：进入视口时的滑入动画 */
.animate-slide-up {
  opacity: 1 !important;
  transform: translateY(0) !important;
  transition: opacity 0.6s ease-out, transform 0.6s ease-out;
}

/* 初始状态：section默认隐藏并向下偏移 */
section {
  opacity: 0;
  transform: translateY(20px);
}

/* 加载中按钮样式 */
button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* 移动端内容区适配：避开CommonNavbar的移动端高度 */
@media (max-width: 768px) {
  main {
    margin-top: 94px !important; /* iOS：状态栏44px + 导航50px */
  }
}

/* 颜色变量定义（统一管理） */
.text-primary {
  color: #4f46e5;
}

.bg-primary {
  background-color: #4f46e5;
}

.bg-primary\/10 {
  background-color: rgba(79, 70, 229, 0.1);
}

.hover\:bg-primary-dark:hover {
  background-color: #4338ca;
}

.text-dark {
  color: #1f2937;
}

/* FAQ内容样式：处理换行符 */
.faq-content {
  white-space: pre-line; /* 保留文本中的换行符 */
}
</style>

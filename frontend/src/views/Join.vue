<template>
  <div class="join-page min-h-screen bg-gray-50 text-dark">
    <CommonNavbar />
    <main class="mx-auto mt-14 max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div class="grid gap-8 lg:grid-cols-[minmax(0,0.8fr)_minmax(0,1.2fr)] lg:items-start">
        <aside class="lg:sticky lg:top-24">
          <h1 class="text-4xl font-bold leading-tight sm:text-5xl">找几个愿意<br />一起动手的人</h1>
          <p class="mt-5 max-w-lg leading-relaxed text-gray-600">写代码、做硬件、刷题、做设计都行。先聊聊你想做什么，再一起试着把它做出来。</p>
          <div class="mt-6 flex flex-wrap gap-2">
            <span v-for="highlight in highlights" :key="highlight" class="rounded-full bg-primary/10 px-3 py-1.5 text-sm text-primary">{{ highlight }}</span>
          </div>

          <section class="mt-10">
            <h2 class="mb-4 text-lg font-bold">怎么加入</h2>
            <ol class="grid gap-3 sm:grid-cols-3 lg:grid-cols-1">
              <li v-for="(step, index) in steps" :key="step" class="flex items-center gap-3">
                <span class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-primary text-sm font-bold text-white">0{{ index + 1 }}</span>
                <span class="text-sm text-gray-700"><strong class="block">{{ step.title }}</strong>{{ step.description }}</span>
              </li>
            </ol>
          </section>

          <section class="mt-10">
            <h2 class="mb-3 text-lg font-bold">常见问题</h2>
            <div class="divide-y divide-gray-200 rounded-xl border border-gray-200 bg-white px-4">
              <div v-for="(faq, index) in faqList" :key="faq.question">
                <button class="flex w-full items-center justify-between py-3 text-left text-sm font-medium" @click="toggleFaq(index)">
                  {{ faq.question }}
                  <span class="text-gray-400">{{ faq.isOpen ? '−' : '+' }}</span>
                </button>
                <p v-if="faq.isOpen" class="pb-3 text-sm leading-relaxed text-gray-600">{{ faq.answer }}</p>
              </div>
            </div>
          </section>
        </aside>

        <section id="form" class="rounded-2xl bg-white p-5 shadow-lg sm:p-8">
          <h2 class="text-2xl font-bold">在线报名</h2>
          <p class="mt-2 text-sm text-gray-500">认真填完这份表单，随后可以加入QQ群。</p>
          <form class="mt-8" @submit.prevent="handleSubmit">
            <div class="grid gap-5 sm:grid-cols-2">
              <label class="block font-medium">姓名 <span class="text-red-500">*</span><input v-model="formData.name" required placeholder="请输入您的姓名" class="form-input mt-2" /></label>
              <label class="block font-medium">学号 <span class="text-red-500">*</span><input v-model="formData.studentId" required placeholder="输入后将自动填充专业与班级" class="form-input mt-2" @input="autoFillFromStudentId" /></label>
              <label class="block font-medium">专业 <span class="text-red-500">*</span><input v-model="formData.major" required placeholder="请输入您的专业（自动识别）" class="form-input mt-2" /></label>
              <label class="block font-medium">班级 <span class="text-red-500">*</span><input v-model="formData.className" required placeholder="请输入您的班级（自动识别）" class="form-input mt-2" /></label>
              <label class="block font-medium">手机号码 <span class="text-red-500">*</span><input v-model="formData.phone" required type="tel" placeholder="请输入手机号码" class="form-input mt-2" /></label>
              <label class="block font-medium">QQ号 <span class="text-red-500">*</span><input v-model="formData.QQNumber" required placeholder="请输入QQ号" class="form-input mt-2" @input="validateQQNumber" /><p v-if="qqNumberError" class="mt-1 text-xs text-red-500">{{ qqNumberError }}</p></label>
            </div>

            <div class="mt-7">
              <p class="mb-3 text-sm text-gray-500">部门用于成员归属和日常管理，不限制之后参与其他方向。</p>
              <fieldset>
                <legend class="mb-3 font-medium">主要归属部门 <span class="text-red-500">*</span></legend>
                <label v-for="department in departments" :key="department.value" class="department-option">
                  <input v-model="formData.department" required type="radio" name="department" :value="department.value" />
                  <span><strong>{{ department.label }}</strong><small>{{ department.description }}</small></span>
                </label>
              </fieldset>
              <fieldset class="mt-6">
                <legend class="mb-3 font-medium">第二意向部门 <span class="text-sm font-normal text-gray-400">（选填）</span></legend>
                <div class="flex flex-wrap gap-2">
                  <label v-for="department in secondDepartments" :key="department" class="second-department-option"><input v-model="formData.secondDepartment" type="radio" name="secondDepartment" :value="department" />{{ department }}</label>
                </div>
              </fieldset>
            </div>

            <fieldset class="mt-7">
              <legend class="mb-1 font-medium">如果你顺利加入，第一阶段你最想先尝试什么？</legend>
              <p class="mb-3 text-sm text-gray-500">最多选 2 项</p>
              <div class="grid gap-3 sm:grid-cols-2">
                <label v-for="interest in interests" :key="interest.value" :class="{ 'interest-option-disabled': isInterestDisabled(interest.value) }" class="interest-option">
                  <input type="checkbox" :checked="formData.initialDirections.includes(interest.value)" :disabled="isInterestDisabled(interest.value)" @change="toggleInterest(interest.value, $event.target.checked)" />
                  <span><strong>{{ interest.label }}</strong><small>{{ interest.description }}</small></span>
                </label>
              </div>
              <p v-if="hasReachedInterestLimit" class="mt-2 text-sm text-primary">最多选择 2 项</p>
            </fieldset>

            <div class="mt-7">
              <label class="mb-2 block font-medium" for="introduction">随便聊聊 <span class="text-red-500">*</span></label>
              <p class="mb-3 text-sm leading-relaxed text-gray-500">可以说说你曾经为了搞懂一个东西，做过最“折腾”的一件事是什么？或者，你加入我们后，最想亲手做出的一个东西是什么？没有相关经历也没关系，写真实想法就行。</p>
              <textarea id="introduction" v-model="formData.introduction" required rows="7" class="form-input resize-y" placeholder="写下你的经历或想法…"></textarea>
            </div>

            <button type="submit" class="mt-8 flex w-full items-center justify-center rounded-lg bg-primary px-8 py-3 font-semibold text-white transition hover:bg-primary-dark disabled:cursor-not-allowed disabled:opacity-70" :disabled="isSubmitting">
              {{ isSubmitting ? '提交中...' : '提交报名' }}
            </button>
          </form>
        </section>
      </div>
    </main>
    <CommonFooter />

    <div v-if="showSuccessModal" class="modal-overlay" @click.self="showSuccessModal = false">
      <div class="modal-content">
        <button class="absolute right-4 top-3 text-xl text-gray-400 hover:text-gray-700" @click="showSuccessModal = false">×</button>
        <h3 class="text-xl font-bold">报名成功！</h3>
        <p class="mt-3 text-gray-600">请加入 QQ 群，获取后续通知。</p>
        <img src="https://club-img.luowb.cn/static/QQGroup.webp" alt="社团招新QQ群二维码" class="mx-auto my-5 max-h-64 max-w-full" />
        <div class="flex flex-col gap-3 sm:flex-row">
          <button class="flex-1 rounded-lg bg-gray-100 px-4 py-2.5 text-sm" @click="copyGroupNumber">群号 724792873 · {{ copyButtonText }}</button>
          <a href="https://qm.qq.com/q/uEOXqCEEVO" target="_blank" rel="noopener noreferrer" class="flex-1 rounded-lg bg-primary px-4 py-2.5 text-center text-sm text-white">直接加群</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import CommonFooter from "@/components/CommonFooter.vue";
import CommonNavbar from "@/components/CommonNavbar.vue";
import {
	getMajorMapping,
	submitApplication,
} from "@/services/applicationsService";

const highlights = ["技术指导", "一起做项目", "组队打比赛"];
const steps = [
	{ title: "提交报名", description: "留下基本信息，说说最近想尝试什么。" },
	{
		title: "聊聊，试一下",
		description: "简单聊聊想法，再完成一个几天内能做完的小尝试。",
	},
	{ title: "正式加入", description: "双方觉得合适，就进入常驻成员群。" },
];
const departments = [
	{
		value: "算法部",
		label: "算法部",
		description: "主攻：蓝桥杯/CCPC等算法竞赛，天天刷题",
	},
	{
		value: "项目竞赛部",
		label: "项目竞赛部",
		description: "主攻：从零学做网站/APP/软硬件，打大创等综合赛",
	},
	{
		value: "综合管理部",
		label: "综合管理部",
		description: "主攻：社团管理、活动策划、文案与视觉设计",
	},
];
const secondDepartments = [
	"项目竞赛部",
	"算法部",
	"综合管理部",
	"无 / 不接受调剂",
];
const interests = [
	{
		label: "做个能用的软件",
		value: "做个能用的软件",
		description: "借助 AI 或跟着教程，从零做一个网页、小程序或实用工具。",
	},
	{
		label: "让硬件动起来",
		value: "让硬件动起来",
		description:
			"接线、烧录程序，让单片机跑起来，或者做一个简单的物联网 Demo。",
	},
	{
		label: "挑战逻辑与算法",
		value: "挑战逻辑与算法",
		description:
			"学算法、刷题，享受解决问题的过程，也为参加程序设计竞赛做准备。",
	},
	{
		label: "探索其他技术方向",
		value: "探索其他技术方向",
		description: "尝试网络安全、游戏开发等方向，愿意自己查资料，慢慢深入下去。",
	},
	{
		label: "做视觉与策划",
		value: "做视觉与策划",
		description: "制作海报、PPT、视频，或者参与活动策划和项目展示。",
	},
	{
		label: "还没想好，先看看",
		value: "还没想好，先看看",
		description: "暂时没有具体方向，想先了解大家平时都在做什么。",
	},
];
const faqList = ref([
	{
		question: "什么都不会可以报名吗？",
		answer:
			"可以。社团里没几个真大佬，大家都是边查边学。现在有了 AI，做个好看的网站或者小程序门槛很低。第一次尝试本来就是用来看看你对什么感兴趣，只要你想学，大把的入门项目可以练手。",
		isOpen: false,
	},
	{
		question: "加入后会有学长开课教我们吗？",
		answer:
			"坦白说，没有那种按部就班的讲座培训，听着也无聊。我们的方式是发任务和自学，但遇到 Bug 或者卡壳了，在群里问一声，老手们随时帮你排雷。",
		isOpen: false,
	},
	{
		question: "必须写代码吗？能干点别的吗？",
		answer:
			"当然可以。如果你对写代码没兴趣，但能用 AI 搞定好看的 UI 设计、排版，或者能搞定大创比赛的 PPT 和策划案，团队同样非常需要你。",
		isOpen: false,
	},
	{
		question: "平时活动多吗？都有什么活动？",
		answer:
			"没什么走形式的开会。更多时候是大家找个空教室，带着电脑坐一起各干各的（也就是自习）。偶尔办个展示会，看看大家最近又搞出了什么好玩的东西。",
		isOpen: false,
	},
	{
		question: "一定要参加比赛吗？",
		answer: "不一定。做项目、学算法、玩硬件、做设计都可以。",
		isOpen: false,
	},
	{
		question: "非计算机专业可以来吗？",
		answer: "可以。专业不作限制，选择自己愿意尝试的方向即可。",
		isOpen: false,
	},
]);
const formData = ref({
	name: "",
	studentId: "",
	major: "",
	className: "",
	phone: "",
	QQNumber: "",
	department: "",
	secondDepartment: "",
	initialDirections: [],
	introduction: "",
});
const majorMapping = ref({});
const qqNumberError = ref("");
const isSubmitting = ref(false);
const showSuccessModal = ref(false);
const copyButtonText = ref("复制");
const undecidedInterest = "还没想好，先看看";
const hasReachedInterestLimit = computed(
	() =>
		!formData.value.initialDirections.includes(undecidedInterest) &&
		formData.value.initialDirections.length >= 2,
);

const toggleFaq = (index) => {
	faqList.value[index].isOpen = !faqList.value[index].isOpen;
};
/** 限制常规方向最多选择两项，“还没想好”作为独立选择。 */
const toggleInterest = (value, checked) => {
	if (value === undecidedInterest) {
		formData.value.initialDirections = checked ? [value] : [];
		return;
	}

	const selected = formData.value.initialDirections.filter(
		(item) => item !== undecidedInterest && item !== value,
	);
	if (checked && selected.length < 2) selected.push(value);
	formData.value.initialDirections = selected;
};
const isInterestDisabled = (value) =>
	!formData.value.initialDirections.includes(value) &&
	(value !== undecidedInterest
		? formData.value.initialDirections.includes(undecidedInterest) ||
			formData.value.initialDirections.length >= 2
		: false);
const validateQQNumber = () => {
	const qq = formData.value.QQNumber;
	qqNumberError.value = !/^\d{5,11}$/.test(qq) ? "QQ号应为 5-11 位数字" : "";
};
/**
 * 依据 11 位学号自动填充专业与班级。
 * 专业取学号第 5-8 位查映射表全称，班级格式 B+入学年份后两位+班级序号。
 */
const autoFillFromStudentId = () => {
	const studentId = formData.value.studentId;
	if (/^\d{11}$/.test(studentId)) {
		const major = majorMapping.value[studentId.substring(4, 8)];
		if (major) formData.value.major = major.fullName;
		formData.value.className = `B${studentId.substring(2, 4)}${studentId.charAt(8)}`;
	}
};
const handleSubmit = async () => {
	validateQQNumber();
	if (qqNumberError.value) return;
	isSubmitting.value = true;
	try {
		await submitApplication(formData.value);
		showSuccessModal.value = true;
		formData.value = {
			name: "",
			studentId: "",
			major: "",
			className: "",
			phone: "",
			QQNumber: "",
			department: "",
			secondDepartment: "",
			initialDirections: [],
			introduction: "",
		};
	} catch (error) {
		alert(
			`提交失败：${error instanceof Error ? error.message : "网络请求错误，请稍后重试。"}`,
		);
	} finally {
		isSubmitting.value = false;
	}
};
const copyGroupNumber = async () => {
	await navigator.clipboard.writeText("724792873");
	copyButtonText.value = "已复制";
	setTimeout(() => {
		copyButtonText.value = "复制";
	}, 3000);
};
onMounted(async () => {
	majorMapping.value = await getMajorMapping();
});
</script>

<style scoped>
.join-page { color: #1f2937; }
.text-dark { color: #1f2937; }
.text-primary { color: #4f46e5; }
.bg-primary { background-color: #4f46e5; }
.hover\:bg-primary-dark:hover { background-color: #4338ca; }
.form-input { width: 100%; border: 1px solid #d1d5db; border-radius: 0.5rem; padding: 0.625rem 0.75rem; outline: none; transition: border-color 0.2s, box-shadow 0.2s; }
.form-input:focus { border-color: #4f46e5; box-shadow: 0 0 0 3px rgb(79 70 229 / 0.12); }
.department-option, .interest-option { display: flex; gap: 0.75rem; border: 1px solid #e5e7eb; border-radius: 0.5rem; padding: 0.75rem; cursor: pointer; transition: background-color 0.2s, border-color 0.2s; }
.department-option:hover, .interest-option:hover { border-color: #a5b4fc; background: #f5f3ff; }
.interest-option-disabled { cursor: not-allowed; opacity: 0.45; }
.interest-option-disabled:hover { border-color: #e5e7eb; background: transparent; }
.department-option input, .interest-option input, .second-department-option input { accent-color: #4f46e5; }
.department-option small, .interest-option small { display: block; margin-top: 0.2rem; color: #6b7280; font-size: 0.75rem; line-height: 1.4; }
.second-department-option { border: 1px solid #e5e7eb; border-radius: 9999px; padding: 0.4rem 0.65rem; color: #4b5563; font-size: 0.875rem; cursor: pointer; }
.second-department-option input { margin-right: 0.35rem; }
.modal-overlay { position: fixed; inset: 0; z-index: 50; display: flex; align-items: center; justify-content: center; background: rgb(0 0 0 / 0.5); padding: 1rem; }
.modal-content { position: relative; width: 100%; max-width: 30rem; border-radius: 0.75rem; background: white; padding: 1.5rem; }
</style>

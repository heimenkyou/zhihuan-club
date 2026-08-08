<template>
  <div class="home-page">
    <CommonNavbar />

    <!-- Hero：深色首屏，定位语 + 双 CTA -->
    <section class="hero relative flex min-h-screen items-center justify-center overflow-hidden pt-16">
      <div class="hero-grid-overlay pointer-events-none absolute inset-0" aria-hidden="true"></div>

      <div class="relative z-10 max-w-5xl px-4 py-20 text-center sm:px-6">
        <h1 class="mb-6 text-6xl font-bold leading-tight text-white sm:text-[4.25rem]">
          把你的脑洞，<br />
          <span class="hero-title-gradient">变成真实的代码。</span>
        </h1>
        <p class="mx-auto mb-8 max-w-3xl text-base leading-relaxed text-slate-300 sm:text-lg">
          这是一个由兴趣驱动的小型开发者社团。不用担心自己技术菜，哪怕你连
          HTML 都没听过，只要愿意带上电脑，用 AI 试着跑通第一行代码，这里都有人陪你一起折腾。
        </p>

        <div class="mb-10 flex flex-wrap justify-center gap-2">
          <span
            v-for="item in heroHighlights"
            :key="item"
            class="rounded-full border border-white/10 bg-white/5 px-4 py-1.5 text-sm text-slate-300"
          >
            {{ item }}
          </span>
        </div>

        <div class="flex flex-col items-center justify-center gap-4 sm:flex-row">
          <button
            @click="tojoin"
            class="w-full rounded-full bg-blue-500 px-12 py-4 text-lg font-semibold text-white shadow-lg shadow-blue-500/30 transition-all hover:-translate-y-0.5 hover:bg-blue-400 sm:w-auto"
          >
            加入我们
          </button>
          <button
            @click="toproject"
            class="w-full rounded-full border border-white/20 px-12 py-4 text-lg font-semibold text-white transition-all hover:-translate-y-0.5 hover:bg-white/10 sm:w-auto"
          >
            看看项目
          </button>
        </div>
      </div>

      <!-- 向下滚动指示 -->
      <a
        href="#about"
        class="absolute bottom-6 left-1/2 z-10 flex -translate-x-1/2 flex-col items-center gap-1 text-sm text-white/60 transition-colors hover:text-white"
      >
        向下滚动
        <i-fa6-solid-chevron-down class="animate-bounce text-lg" />
      </a>
    </section>

    <!-- 我们是谁 -->
    <section id="about" class="scroll-mt-20 bg-white py-20">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="grid items-center gap-12 lg:grid-cols-2">
          <div class="relative">
            <div class="absolute -left-4 -top-4 -z-10 h-24 w-24 rounded-2xl bg-blue-500/10"></div>
            <div class="absolute -bottom-4 -right-4 -z-10 h-32 w-32 rounded-2xl bg-indigo-500/10"></div>
            <img
              src="@/assets/images/group-photo.webp"
              alt="社团成员一起动手实践"
              class="aspect-[4/3] w-full rounded-2xl object-cover shadow-lg"
              loading="lazy"
            />
            <div class="absolute -bottom-6 left-6 rounded-xl bg-blue-500 px-5 py-3 font-semibold text-white shadow-lg">
              一群愿意动手的人
            </div>
          </div>

          <div>
            <h2 class="mb-6 text-3xl font-bold text-slate-900 sm:text-4xl">
              在这里，把想法做成真的东西
            </h2>
            <p class="mb-4 text-base leading-[1.75] text-slate-600">
              智环学创融合协会成立于 2024
              年。有人写网站，有人刷题，有人做硬件，也有人琢磨怎么把一个项目讲明白。大家喜欢的东西不一样，但都愿意动手试试。
            </p>
            <p class="mb-8 text-base leading-[1.75] text-slate-600">
              比起安排一条固定路线，我们更喜欢从一个小东西开始，边做边学，慢慢找到自己真正感兴趣的方向。
            </p>

            <ul class="mb-10 space-y-3">
              <li v-for="feature in features" :key="feature" class="flex items-start gap-3">
                <span class="mt-0.5 flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-blue-500/10 text-blue-600">
                  <i-fa6-solid-check class="text-xs" />
                </span>
                <span class="text-slate-700">{{ feature }}</span>
              </li>
            </ul>

            <router-link
              to="/about"
              class="inline-flex items-center gap-2 rounded-full border border-blue-500 px-6 py-3 font-semibold text-blue-600 transition-all hover:bg-blue-500 hover:text-white"
            >
              了解更多
              <i-fa6-solid-arrow-right class="text-sm" />
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 高光时刻：项目 / 活动展示 -->
    <section id="achievements" class="bg-gray-50 py-20">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="mb-12 text-center">
          <h2 class="mb-3 text-3xl font-bold text-slate-900 sm:text-4xl">
            我们最近搞出的一些动静
          </h2>
          <div class="mx-auto h-1 w-20 rounded-full bg-blue-500"></div>
          <p class="mx-auto mt-4 max-w-2xl text-slate-500">
            没那么多高大上的理论，就是遇到问题解决问题。这里记录了我们拿过的奖项，和平时敲出来的真实项目。
          </p>
        </div>

        <div v-if="highlightsLoading" v-loading="true" class="flex min-h-64 items-center justify-center"></div>

        <el-empty v-else-if="highlightsError" :description="highlightsError">
          <el-button type="primary" @click="loadHighlights">重新加载</el-button>
        </el-empty>

        <el-empty v-else-if="highlightMoments.length === 0" description="暂无高光内容" />

        <template v-else>
          <div class="relative">
            <!-- 横向自动轮播轨道：无缝循环，触摸/箭头均可手动切换 -->
            <div
              ref="trackRef"
              class="highlight-track flex gap-6 overflow-x-auto pb-2"
              @touchstart="onTouchStart"
              @touchend="onTouchEnd"
              @touchcancel="onTouchEnd"
            >
              <article
                v-for="(moment, index) in displayMoments"
                :key="`${moment.id}-${index}`"
                class="highlight-card flex w-[85%] shrink-0 flex-col overflow-hidden rounded-2xl bg-white shadow-lg transition-all hover:-translate-y-1 hover:shadow-xl sm:w-[45%] lg:w-[31%]"
                :class="{ 'cursor-pointer': isProjectMoment(moment) }"
                role="button"
                :tabindex="isProjectMoment(moment) ? 0 : -1"
                :aria-label="isProjectMoment(moment) ? `查看项目：${moment.title}` : undefined"
                @click="openProject(moment)"
                @keydown.enter="openProject(moment)"
                @keydown.space.prevent="openProject(moment)"
              >
                <el-image
                  :src="moment.coverImage"
                  :alt="moment.title"
                  class="h-44 w-full shrink-0 sm:h-52"
                  fit="cover"
                  :preview-src-list="[moment.coverImage]"
                  :preview-teleported="true"
                  @click.stop
                />
                <div class="flex flex-1 flex-col p-5">
                  <div class="mb-3 flex items-center justify-between">
                    <el-tag
                      :type="moment.type === 'project' ? 'primary' : 'success'"
                      size="small"
                      round
                    >
                      {{ moment.type === "project" ? "项目" : "活动" }}
                    </el-tag>
                    <span
                      v-if="moment.type === 'activity' && moment.activityDate"
                      class="text-xs text-gray-400"
                    >
                      {{ moment.activityDate }}
                    </span>
                  </div>
                  <h3 class="mb-2 line-clamp-2 text-lg font-bold text-slate-900">
                    {{ moment.title }}
                  </h3>
                  <p class="mb-4 line-clamp-3 flex-1 text-[15px] leading-[1.75] text-gray-500">
                    {{ moment.description }}
                  </p>
                  <span v-if="isProjectMoment(moment)" class="text-sm font-semibold text-blue-600">
                    查看项目 →
                  </span>
                </div>
              </article>
            </div>

            <!-- 左右切换箭头（PC 端显示） -->
            <button
              class="absolute left-0 top-1/2 z-10 hidden h-10 w-10 -translate-y-1/2 items-center justify-center rounded-full border border-gray-200 bg-white/80 shadow-md transition-all duration-300 hover:bg-white sm:flex"
              aria-label="上一张"
              @click="prev"
            >
              <i-fa6-solid-chevron-left />
            </button>
            <button
              class="absolute right-0 top-1/2 z-10 hidden h-10 w-10 -translate-y-1/2 items-center justify-center rounded-full border border-gray-200 bg-white/80 shadow-md transition-all duration-300 hover:bg-white sm:flex"
              aria-label="下一张"
              @click="next"
            >
              <i-fa6-solid-chevron-right />
            </button>
          </div>
        </template>

        <div class="mt-12 flex flex-col justify-center gap-4 sm:flex-row">
          <button
            @click="toproject"
            class="rounded-full border border-blue-500 bg-white px-6 py-3 font-semibold text-blue-600 shadow transition-all hover:bg-blue-500 hover:text-white hover:shadow-md"
          >
            查看更多项目
          </button>
          <button
            @click="toawards"
            class="rounded-full border border-blue-500 bg-white px-6 py-3 font-semibold text-blue-600 shadow transition-all hover:bg-blue-500 hover:text-white hover:shadow-md"
          >
            查看更多奖项
          </button>
        </div>
      </div>
    </section>

    <!-- 探索方向 -->
    <section id="directions" class="scroll-mt-20 bg-white py-20">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="mb-12 text-center">
          <h2 class="mb-3 text-3xl font-bold text-slate-900 sm:text-4xl">你可以从哪里开始</h2>
          <div class="mx-auto h-1 w-20 rounded-full bg-blue-500"></div>
          <p class="mx-auto mt-4 max-w-2xl text-slate-500">
            方向可以交叉，也可以随时调整。先选择一个小目标，做出第一个能够展示的成果。
          </p>
        </div>

        <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
          <article
            v-for="direction in directions"
            :key="direction.title"
            class="direction-card rounded-2xl bg-gray-50 p-8 text-center"
            :style="{ '--dir-color': direction.color }"
          >
            <div
              class="mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-full text-2xl"
              :style="{ backgroundColor: direction.iconBg, color: direction.color }"
            >
              <component :is="direction.icon" />
            </div>
            <h3 class="mb-3 text-lg font-bold text-slate-900">{{ direction.title }}</h3>
            <p class="mb-5 text-[15px] leading-[1.75] text-slate-600">{{ direction.description }}</p>
            <span
              class="inline-flex items-center rounded-full px-3 py-1 text-xs font-semibold"
              :style="{ backgroundColor: direction.tagBg, color: direction.color }"
            >
              {{ direction.tag }}
            </span>
          </article>
        </div>

      </div>
    </section>

    <!-- 加入我们 -->
    <section class="bg-white pb-20">
      <div class="mx-auto max-w-[1120px] px-4 sm:px-6 lg:px-8">
        <div class="join-cta-bg relative rounded-3xl px-8 py-14 sm:px-14">
          <div class="grid items-center gap-12 lg:grid-cols-[1.15fr_1fr]">
            <div class="text-center lg:text-left">
              <h2 class="mb-5 text-3xl font-bold text-slate-900 sm:text-4xl">
                想一起做点有意思的东西？
              </h2>
              <p class="mx-auto mb-4 max-w-md text-base leading-relaxed text-slate-600 lg:mx-0">
                不用先会什么，带上电脑，从一个小目标开始，把脑洞变成能跑的东西。
              </p>
              <p class="mx-auto mb-8 max-w-md text-base leading-relaxed text-slate-600 lg:mx-0">
                零基础也欢迎，遇到卡壳的地方，群里问一声就有人帮你排雷。
              </p>
              <button
                @click="tojoin"
                class="rounded-full bg-blue-500 px-10 py-3.5 font-semibold text-white shadow-lg shadow-blue-500/30 transition-all hover:-translate-y-0.5 hover:bg-blue-400"
              >
                查看加入方式
              </button>
            </div>
            <div class="relative flex justify-center lg:translate-x-4 lg:translate-y-6 lg:justify-end">
              <div class="join-badge-wrap w-48 sm:w-56 lg:w-64">
                <img
                  src="/you.webp"
                  alt="期待你的加入"
                  class="join-img"
                  loading="lazy"
                />
                <div class="join-badge">
                  <span>期待你的加入</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <CommonFooter />
  </div>
</template>

<script setup>
import { computed, markRaw, nextTick, onMounted, onUnmounted, ref, watch } from "vue";
import { useRouter } from "vue-router";
import ICode from "~icons/fa6-solid/code";
import IDesktop from "~icons/fa6-solid/desktop";
import ILightbulb from "~icons/fa6-solid/lightbulb";
import ITrophy from "~icons/fa6-solid/trophy";
import CommonFooter from "@/components/CommonFooter.vue";
import CommonNavbar from "@/components/CommonNavbar.vue";
import { getHomepageHighlights } from "@/services/homepageHighlightService";

const router = useRouter();

// 高光横向轮播相关
const trackRef = ref();
/** 自动滚动间隔（毫秒） */
const AUTO_SCROLL_INTERVAL = 3500;
/** 卡片滚动动画时长（毫秒） */
const SCROLL_DURATION = 500;
let scrollTimer = null;
let resumeTimer = null;
let animFrame = null;
/** 最近一次动画的目标位置，用于连点时从上次目标继续，避免依赖实时位置 */
let lastTarget = 0;

/** 复制一份列表实现无缝循环；单张时不复制，避免空转。 */
const displayMoments = computed(() => {
	const list = highlightMoments.value;
	return list.length <= 1 ? list : [...list, ...list];
});

/** 获取轨道中的卡片元素。 */
const getCards = () => [...(trackRef.value?.querySelectorAll(".highlight-card") ?? [])];

/** 单步滚动距离：相邻卡片左边缘之差，天然包含间距。 */
const getStep = (cards) =>
	cards.length >= 2 ? cards[1].offsetLeft - cards[0].offsetLeft : 0;

/**
 * 用 requestAnimationFrame 平滑滚动轨道到指定位置，避免 scroll-snap 吞掉动画。
 *
 * @param {HTMLElement} track
 * @param {number} to
 * @param {Function} [onDone] 动画完成回调
 */
const animateScroll = (track, to, onDone) => {
	if (animFrame) cancelAnimationFrame(animFrame);
	const from = track.scrollLeft;
	const startTime = performance.now();
	const tick = (now) => {
		const progress = Math.min((now - startTime) / SCROLL_DURATION, 1);
		// easeOutCubic 缓动
		const eased = 1 - (1 - progress) ** 3;
		track.scrollLeft = from + (to - from) * eased;
		if (progress < 1) {
			animFrame = requestAnimationFrame(tick);
		} else {
			animFrame = null;
			onDone?.();
		}
	};
	animFrame = requestAnimationFrame(tick);
};

/**
 * 按方向滚动一张卡片。
 * 连点以最近一次动画目标为基准（不从实时位置对齐），因此无需等待动画结束即可连续切换；
 * 越界时借副本区平滑滚到下一张，动画完成后再无动画回绕（内容一致，视觉无感）。
 *
 * @param {1|-1} direction
 */
const scrollByCard = (direction) => {
	const track = trackRef.value;
	const cards = getCards();
	const originalLen = highlightMoments.value.length;
	if (!track || cards.length === 0 || originalLen <= 1) return;
	const step = getStep(cards);
	if (!step) return;
	const duplicateStart = cards[originalLen]?.offsetLeft ?? 0;
	const maxScroll = track.scrollWidth - track.clientWidth;

	// 动画未完成时用上次目标作为连点基准，否则取当前最近整卡位置
	const base =
		animFrame !== null ? lastTarget : Math.round(track.scrollLeft / step) * step;
	let target = base + direction * step;

	if (target > maxScroll) {
		// 目标将超出轨道物理末尾：先无动画整体回绕（内容一致无感），再继续
		track.scrollLeft -= duplicateStart;
		lastTarget -= duplicateStart;
		target = lastTarget + direction * step;
	} else if (target < 0) {
		// 反向到边界：先无动画定位到副本末张前一张，再平滑滚到副本末张，完成后回绕
		const copyEnd = (originalLen * 2 - 1) * step;
		track.scrollLeft = copyEnd - step;
		lastTarget = copyEnd;
		target = copyEnd;
	}

	lastTarget = target;
	animateScroll(track, target, () => {
		// 动画完成且落在副本区：无动画回绕到原始区等价位置
		if (target >= duplicateStart) {
			track.scrollLeft = target - duplicateStart;
			lastTarget = target - duplicateStart;
		}
	});
};

const stopAutoScroll = () => {
	if (scrollTimer) {
		clearInterval(scrollTimer);
		scrollTimer = null;
	}
};

const startAutoScroll = () => {
	stopAutoScroll();
	scrollTimer = setInterval(() => scrollByCard(1), AUTO_SCROLL_INTERVAL);
};

/** 手动操作后重置计时，暂停片刻再恢复自动滚动。 */
const restartAutoScroll = () => {
	stopAutoScroll();
	if (resumeTimer) clearTimeout(resumeTimer);
	resumeTimer = setTimeout(startAutoScroll, 3000);
};

const prev = () => {
	scrollByCard(-1);
	restartAutoScroll();
};

const next = () => {
	scrollByCard(1);
	restartAutoScroll();
};

/** 触摸开始：中断动画并暂停自动滚动，让出手势。 */
const onTouchStart = () => {
	if (animFrame) {
		cancelAnimationFrame(animFrame);
		animFrame = null;
	}
	stopAutoScroll();
};

/** 触摸结束：保留浏览器原生滚动/惯性，片刻后恢复自动滚动。 */
const onTouchEnd = () => {
	restartAutoScroll();
};

// Hero 关键词
const heroHighlights = ["技术指导", "一起做项目", "组队打比赛"];

// 我们是谁：社团特点
const features = [
	"兴趣驱动，没有按部就班的讲座培训，想学什么自己主动",
	"边做边学，从第一个能跑的小作品开始",
	"零基础也欢迎，靠 AI 就能写出第一行能跑的代码",
	"遇 bug 卡壳，群里问一声就有人帮你排雷",
];

// 探索方向卡片
const directions = [
	{
		title: "AI 编程与应用",
		description: "不用死记硬背枯燥的语法，学习怎么用提示词让 AI 帮你写代码。零基础也能快速搞出能跑的网页。",
		tag: "从一个小工具开始",
		icon: markRaw(ICode),
		iconBg: "#eff6ff",
		tagBg: "#dbeafe",
		color: "#2563eb",
	},
	{
		title: "算法与竞赛",
		description: "针对蓝桥杯等算法竞赛的路线。大家一起刷题交流，适合想要拿奖、攒简历和赚学分的同学。",
		tag: "从一次练习开始",
		icon: markRaw(ITrophy),
		iconBg: "#eef2ff",
		tagBg: "#e0e7ff",
		color: "#4f46e5",
	},
	{
		title: "软件与硬件创作",
		description: "如果你有一定的基础，想做完整的项目，比如微信小程序或单片机硬件，可以来这里一起把东西落地。",
		tag: "从一个真实需求开始",
		icon: markRaw(IDesktop),
		iconBg: "#ecfeff",
		tagBg: "#cffafe",
		color: "#0891b2",
	},
	{
		title: "项目表达与设计",
		description: "负责团队的“面子工程”。用 AI 绘画和排版工具，搞定项目 UI、比赛演示文稿和视频剪辑。",
		tag: "从表达一个想法开始",
		icon: markRaw(ILightbulb),
		iconBg: "#f3e8ff",
		tagBg: "#ede9fe",
		color: "#7c3aed",
	},
];

// 高光时刻数据
const highlightMoments = ref([]);
const highlightsLoading = ref(true);
const highlightsError = ref("");

/** 判断是否为项目类型高光。 */
const isProjectMoment = (moment) => moment?.type === "project" && moment?.projectId;

/** 项目卡片点击跳转项目详情，活动不跳转。 */
const openProject = (moment) => {
	if (!isProjectMoment(moment)) return;
	router.push({ path: "/projectdetail", query: { id: moment.projectId } });
};

/** 加载首页高光列表。 */
const loadHighlights = async () => {
	highlightsLoading.value = true;
	highlightsError.value = "";
	try {
		highlightMoments.value = await getHomepageHighlights();
	} catch (error) {
		highlightsError.value =
			error instanceof Error ? error.message : "高光时刻加载失败";
		console.error("加载首页高光失败:", error);
	} finally {
		highlightsLoading.value = false;
	}
};

// 导航函数
const tojoin = () => {
	router.push("/join");
};

const toproject = () => {
	router.push("/projects");
};

const toawards = () => {
	router.push("/awards");
};

onMounted(() => {
	loadHighlights();
});

// 高光数据就绪且轨道渲染后启动自动滚动
watch(
	displayMoments,
	() => {
		startAutoScroll();
	},
	{ flush: "post" },
);

onUnmounted(() => {
	stopAutoScroll();
	if (resumeTimer) clearTimeout(resumeTimer);
	if (animFrame) cancelAnimationFrame(animFrame);
});
</script>

<style scoped lang="scss">
  .home-page {
    font-family: 'Inter', 'system-ui', 'sans-serif';
    color: #333;
  }

  /* Hero 深色背景：渐变 + 两侧冷色光晕 */
  .hero {
    background:
      radial-gradient(900px 420px at 85% -10%, rgb(59 130 246 / 0.28), transparent 60%),
      radial-gradient(700px 380px at 8% 110%, rgb(99 102 241 / 0.22), transparent 60%),
      linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  }

  /* Hero 网格点阵装饰，向四周渐隐 */
  .hero-grid-overlay {
    background-image: radial-gradient(rgb(148 163 184 / 0.12) 1px, transparent 1px);
    background-size: 26px 26px;
    -webkit-mask-image: radial-gradient(ellipse at center, black 0%, transparent 75%);
    mask-image: radial-gradient(ellipse at center, black 0%, transparent 75%);
  }

  /* Hero 标题渐变文字 */
  .hero-title-gradient {
    background: linear-gradient(90deg, #60a5fa, #818cf8);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
  }

  /* 高光卡片横向滚动轨道：隐藏滚动条 */
  .highlight-track {
    scrollbar-width: none;
  }

  .highlight-track::-webkit-scrollbar {
    display: none;
  }

  /* 探索方向卡片：浅边框，hover 上移并染上对应主题色 */
  .direction-card {
    border: 1px solid #e8edf3;
    transition:
      transform 0.2s ease,
      border-color 0.2s ease,
      background-color 0.2s ease;

    &:hover {
      transform: translateY(-3px);
      border-color: var(--dir-color, #2563eb);
    }
  }

  /* 加入我们 CTA 卡片：极淡蓝紫渐变背景 */
  .join-cta-bg {
    background: linear-gradient(135deg, #eef4ff 0%, #f5f1ff 100%);
  }

  /* 期待你的加入：小狗图左下角叠黄色斜切徽章 */
  .join-badge-wrap {
    position: relative;
    flex-shrink: 0;
  }

  .join-img {
    display: block;
    width: 100%;
    border-radius: 12px;
    box-shadow: 0 10px 15px rgb(0 0 0 / 0.1);
  }

  .join-badge {
    position: absolute;
    bottom: -20px;
    left: -20px;
    padding: 14px 22px;
    border-radius: 10px;
    background: #f59e0b;
    box-shadow: 0 10px 15px rgb(0 0 0 / 0.15);
    transform: skew(-20deg);

    span {
      display: inline-block;
      color: #fff;
      font-size: 1.25rem;
      font-weight: 700;
      transform: skew(20deg);
    }
  }
</style>

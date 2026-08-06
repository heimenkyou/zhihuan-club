<template>
  <div class="awards-page min-h-screen">
    <CommonNavbar />
    <main class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 mt-14 mobile:py-4 mobile:mt-12">
      <div class="text-center mb-8 animate-fade-in">
        <h1 class="text-4xl font-bold text-dark mb-3">竞赛奖项</h1>
      </div>

      <div class="award-toolbar sticky top-4 z-10 mb-8 rounded-xl border border-gray-200 bg-white/95 px-3 py-2 shadow-sm backdrop-blur">
        <div class="flex flex-wrap items-center gap-2">
          <div class="relative w-full sm:w-64">
            <input v-model="searchKeyword" type="text" placeholder="搜索奖项名称、获奖人员..." class="w-full rounded-lg border border-gray-200 py-2 pl-9 pr-3 text-sm outline-none transition focus:border-primary focus:ring-2 focus:ring-primary/20" @input="handleSearch" />
            <i class="fa fa-search absolute left-3 top-1/2 -translate-y-1/2 text-xs text-gray-400"></i>
          </div>
          <select v-model="filter.competitionLevel" class="toolbar-select" aria-label="竞赛级别">
            <option value="">竞赛级别</option>
            <option v-for="option in competitionLevelOptions" :key="option" :value="option">{{ option }}</option>
          </select>
          <select v-model="filter.awardLevel" class="toolbar-select" aria-label="奖项级别">
            <option value="">奖项级别</option>
            <option v-for="option in awardLevelOptions" :key="option" :value="option">{{ option }}</option>
          </select>
          <select v-model="filter.year" class="toolbar-select" aria-label="竞赛年份">
            <option value="">竞赛年份</option>
            <option v-for="option in yearOptions" :key="option" :value="String(option)">{{ option }}年</option>
          </select>
          <select v-model="filter.competitionName" class="toolbar-select toolbar-select-name" aria-label="竞赛名称">
            <option value="">竞赛名称</option>
            <option v-for="name in competitionNames" :key="name" :value="name">{{ name }}</option>
          </select>
          <div class="ml-auto flex items-center gap-3 whitespace-nowrap text-sm">
            <span class="text-gray-400">共找到 {{ filteredAwards.length }} 项荣誉</span>
            <button class="text-gray-500 transition hover:text-primary" @click="resetFilters">清空筛选</button>
          </div>
        </div>
      </div>

      <div v-if="loading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-primary"></div>
      </div>

      <div v-else-if="error" class="text-center py-12">
        <p class="text-red-500 mb-2">{{ error }}</p>
        <button class="text-primary hover:underline" @click="fetchAwards">重试</button>
      </div>

      <div v-else class="space-y-9">
        <section v-for="(year, index) in sortedYears" :key="year" class="animate-slide-up" :style="{ animationDelay: `${0.1 * index}s` }">
          <div class="mb-3 flex items-center gap-3">
            <h2 class="text-2xl font-bold text-dark">{{ year }} 年度</h2>
            <span class="h-px flex-1 bg-gray-200"></span>
            <span class="text-sm text-gray-400">{{ groupedAwards[year].length }} 项</span>
          </div>
          <div class="overflow-hidden rounded-xl border border-gray-200 bg-white">
            <button v-for="award in groupedAwards[year]" :key="award.id" :class="{ 'award-item-highlight': isMajorAward(award) }" class="award-item" @click="showAwardDetail(award)">
              <span :class="getCompetitionClass(award.competitionLevel)" class="competition-badge">{{ award.competitionLevel || '未分类' }}</span>
              <span :class="getAwardTextClass(award.awardLevel)" class="award-badge">{{ award.awardLevel }}</span>
              <span class="min-w-0 flex-1 truncate text-left font-medium text-gray-800">
                {{ award.competitionName }}<template v-if="award.competitionTrack"> · {{ award.competitionTrack }}</template>
              </span>
              <span class="award-winners"><span class="award-winners-label">获奖人：</span>{{ award.winners.join('、') || '未填写' }}</span>
              <span class="shrink-0 text-gray-300">›</span>
            </button>
          </div>
        </section>

        <div v-if="sortedYears.length === 0" class="py-12 text-center text-gray-500">暂无符合条件的奖项数据</div>
      </div>
    </main>

    <el-dialog v-model="showAwardDetailDialog" :title="undefined" :width="dialogWidth" :max-width="600" :before-close="closeAwardDetailDialog" class="award-detail-dialog" align-center :close-on-click-modal="true">
      <div v-if="selectedAward" class="award-detail">
        <div :class="getHonorClass(selectedAward.awardLevel)" class="honor-badge">
          <span>🏆</span>
          {{ selectedAward.competitionLevel }} · {{ selectedAward.awardLevel }}
        </div>
        <h2 class="award-detail-title">{{ selectedAward.competitionName }}</h2>
        <p class="award-detail-meta">
          <template v-if="selectedAward.competitionTrack">{{ selectedAward.competitionTrack }}赛道 · </template>
          {{ selectedAward.awardDate || `${selectedAward.year}年` }} 获奖
        </p>
        <div class="award-detail-divider"></div>
        <section class="award-winners-section">
          <h3>🎖️ 荣誉得主</h3>
          <div class="flex flex-wrap justify-center gap-2">
            <span v-for="winner in selectedAward.winners" :key="winner" class="winner-tag">👨‍💻 {{ winner }}</span>
          </div>
        </section>
      </div>
    </el-dialog>
    <CommonFooter />
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";
import CommonFooter from "@/components/CommonFooter.vue";
import CommonNavbar from "@/components/CommonNavbar.vue";
import { getAwards } from "@/services/adminService";

const awardLevelPriority = {
	特等奖: 0,
	一等奖: 1,
	金奖: 1,
	金牌: 1,
	二等奖: 2,
	银奖: 2,
	银牌: 2,
	三等奖: 3,
	铜奖: 3,
	铜牌: 3,
	优秀奖: 4,
};
const competitionLevelPriority = { 国家级: 0, 省级: 1, 校级: 2, 院级: 2 };
const awards = ref([]);
const loading = ref(true);
const error = ref(null);
const searchKeyword = ref("");
const filter = ref({
	competitionLevel: "",
	awardLevel: "",
	year: "",
	competitionName: "",
});
const selectedAward = ref(null);
const showAwardDetailDialog = ref(false);
const viewportWidth = ref(window.innerWidth);
let searchTimeout = null;

const competitionNames = computed(() =>
	[
		...new Set(
			awards.value.map((item) => item.competitionName).filter(Boolean),
		),
	].sort(),
);
const competitionLevelOptions = computed(() => [
	...new Set(awards.value.map((item) => item.competitionLevel).filter(Boolean)),
]);
const awardLevelOptions = computed(() =>
	[
		...new Set(awards.value.map((item) => item.awardLevel).filter(Boolean)),
	].sort(
		(a, b) => (awardLevelPriority[a] ?? 99) - (awardLevelPriority[b] ?? 99),
	),
);
const yearOptions = computed(() =>
	[
		...new Set(
			awards.value.map((item) => Number(item.year)).filter(Number.isFinite),
		),
	].sort((a, b) => b - a),
);
const dialogWidth = computed(() =>
	viewportWidth.value <= 640 ? "90%" : "40%",
);

/** 根据竞赛级别返回列表主标签样式。 */
const getCompetitionClass = (level) =>
	({ 国家级: "competition-national", 省级: "competition-provincial" })[level] ||
	"competition-campus";

/** 根据奖项等级返回辅助文字颜色。 */
const getAwardTextClass = (level) =>
	({
		特等奖: "award-gold",
		一等奖: "award-gold",
		金奖: "award-gold",
		金牌: "award-gold",
		二等奖: "award-silver",
		银奖: "award-silver",
		银牌: "award-silver",
		三等奖: "award-bronze",
		铜奖: "award-bronze",
		铜牌: "award-bronze",
	})[level] || "award-default";

/** 判断需在列表中突出展示的重要奖项。 */
const isMajorAward = (award) =>
	award.competitionLevel === "国家级" ||
	(award.competitionLevel === "省级" &&
		["特等奖", "一等奖", "金奖", "金牌"].includes(award.awardLevel));

/** 根据奖项等级返回详情页的荣誉主视觉。 */
const getHonorClass = (level) =>
	({
		一等奖: "honor-gold",
		金奖: "honor-gold",
		金牌: "honor-gold",
		二等奖: "honor-silver",
		银奖: "honor-silver",
		银牌: "honor-silver",
		三等奖: "honor-bronze",
		铜奖: "honor-bronze",
		铜牌: "honor-bronze",
	})[level] || "honor-default";

/** 拉取奖项数据，供前端即时组合筛选。 */
const fetchAwards = async () => {
	loading.value = true;
	error.value = null;
	try {
		const data = await getAwards({ keyword: searchKeyword.value });
		awards.value = Array.isArray(data)
			? data.map((item) => ({
					...item,
					year: Number(item.year),
					winners: Array.isArray(item.winners) ? item.winners : [],
				}))
			: [];
	} catch (err) {
		error.value = "获取奖项数据失败，请检查API服务是否运行";
		console.error("Failed to fetch awards:", err);
		awards.value = [];
	} finally {
		loading.value = false;
	}
};

const handleSearch = () => {
	clearTimeout(searchTimeout);
	searchTimeout = setTimeout(fetchAwards, 300);
};

/** 清空所有筛选条件并恢复完整列表。 */
const resetFilters = () => {
	filter.value = {
		competitionLevel: "",
		awardLevel: "",
		year: "",
		competitionName: "",
	};
	searchKeyword.value = "";
	fetchAwards();
};

const filteredAwards = computed(() =>
	awards.value.filter(
		(award) =>
			(!searchKeyword.value ||
				award.competitionName
					?.toLowerCase()
					.includes(searchKeyword.value.toLowerCase()) ||
				award.winners.some((winner) =>
					winner.toLowerCase().includes(searchKeyword.value.toLowerCase()),
				) ||
				award.awardLevel
					?.toLowerCase()
					.includes(searchKeyword.value.toLowerCase())) &&
			(!filter.value.competitionLevel ||
				award.competitionLevel === filter.value.competitionLevel) &&
			(!filter.value.awardLevel ||
				award.awardLevel === filter.value.awardLevel) &&
			(!filter.value.year || award.year === Number(filter.value.year)) &&
			(!filter.value.competitionName ||
				award.competitionName === filter.value.competitionName),
	),
);

const groupedAwards = computed(() =>
	filteredAwards.value
		.slice()
		.sort(
			(a, b) =>
				b.year - a.year ||
				(competitionLevelPriority[a.competitionLevel] ?? 99) -
					(competitionLevelPriority[b.competitionLevel] ?? 99) ||
				(awardLevelPriority[a.awardLevel] ?? 99) -
					(awardLevelPriority[b.awardLevel] ?? 99) ||
				a.competitionName.localeCompare(b.competitionName),
		)
		.reduce((groups, award) => {
			const year = String(award.year);
			if (!groups[year]) {
				groups[year] = [];
			}
			groups[year].push(award);
			return groups;
		}, {}),
);
const sortedYears = computed(() =>
	Object.keys(groupedAwards.value).sort((a, b) => Number(b) - Number(a)),
);

/** 打开奖项详情。 */
const showAwardDetail = (award) => {
	selectedAward.value = award;
	showAwardDetailDialog.value = true;
};

/** 关闭奖项详情并释放当前选中项。 */
const closeAwardDetailDialog = () => {
	showAwardDetailDialog.value = false;
	selectedAward.value = null;
};

const updateViewportWidth = () => {
	viewportWidth.value = window.innerWidth;
};

onMounted(() => {
	fetchAwards();
	window.addEventListener("resize", updateViewportWidth);
});
onUnmounted(() => {
	clearTimeout(searchTimeout);
	window.removeEventListener("resize", updateViewportWidth);
});
</script>

<style scoped>
.awards-page { background-color: #f8fafc; }
.toolbar-select { width: 128px; border: 1px solid #e5e7eb; border-radius: 0.5rem; background: white; padding: 0.5rem 0.75rem; font-size: 0.875rem; color: #4b5563; outline: none; }
.toolbar-select:focus { border-color: #3b82f6; box-shadow: 0 0 0 2px rgb(59 130 246 / 0.2); }
.toolbar-select-name { width: 150px; }
.award-item { display: flex; width: 100%; min-height: 52px; align-items: center; gap: 0.75rem; border-bottom: 1px solid #f3f4f6; padding: 0.5rem 1rem; text-align: left; transition: background-color 0.2s; }
.award-item:last-child { border-bottom: 0; }
.award-item:hover { background: #f8fafc; }
.award-item-highlight { border-left: 4px solid #d4a72c; background: rgb(255 215 0 / 0.05); }
.award-item-highlight:hover { background: rgb(255 215 0 / 0.1); }
.competition-badge { flex-shrink: 0; min-width: 3.75rem; border-radius: 9999px; padding: 0.25rem 0.5rem; font-size: 0.75rem; font-weight: 700; line-height: 1.25; text-align: center; }
.competition-national { background: linear-gradient(135deg, #7f1d1d, #eab308); color: #fff; }
.competition-national::before { content: "🏆 "; }
.competition-provincial { background: #0f766e; color: #fff; }
.competition-campus { border: 1px solid #d1d5db; color: #6b7280; }
.award-badge { flex-shrink: 0; font-size: 0.8125rem; font-weight: 700; line-height: 1; }
.award-gold { color: #ca8a04; }
.award-silver { color: #64748b; }
.award-bronze { color: #b45309; }
.award-default { color: #6b7280; }
.award-winners { flex-shrink: 0; color: #6b7280; font-size: 0.875rem; }
.award-detail { padding: 1.5rem 1rem; border: 1px solid #d6b76e; background: linear-gradient(135deg, #fffdf5, #fff 55%, #f7f0d8); box-shadow: inset 0 0 0 6px #fff, inset 0 0 0 7px #e5d09a; text-align: center; }
.honor-badge { display: inline-flex; align-items: center; gap: 0.4rem; margin-top: 0.25rem; border-radius: 9999px; padding: 0.75rem 1.25rem; font-size: 1.375rem; font-weight: 800; letter-spacing: 0.03em; }
.honor-gold { background: linear-gradient(135deg, #fef3c7, #f59e0b); color: #78350f; }
.honor-silver { background: linear-gradient(135deg, #e2e8f0, #94a3b8); color: #1e3a5f; }
.honor-bronze { background: linear-gradient(135deg, #fed7aa, #9a3412); color: #fff7ed; }
.honor-default { background: linear-gradient(135deg, #dbeafe, #60a5fa); color: #1e3a8a; }
.award-detail-title { margin-top: 1.5rem; color: #1f2937; font-size: 1.5rem; font-weight: 700; line-height: 1.4; }
.award-detail-meta { margin-top: 0.5rem; color: #6b7280; font-size: 0.875rem; }
.award-detail-divider { height: 1px; margin: 1.5rem 0; background: #e5e7eb; }
.award-winners-section h3 { margin-bottom: 0.75rem; color: #374151; font-size: 1rem; font-weight: 700; }
.winner-tag { border-radius: 9999px; background: #dbeafe; padding: 0.45rem 0.8rem; color: #1d4ed8; font-size: 0.875rem; font-weight: 600; }
:deep(.award-detail-dialog .el-dialog) { margin: 0 !important; overflow: hidden; border-radius: 2px; }
:deep(.award-detail-dialog .el-dialog__header) { position: absolute; z-index: 1; top: 0.5rem; right: 0.5rem; margin: 0; padding: 0; }
:deep(.award-detail-dialog .el-dialog__headerbtn) { top: 0; right: 0; }
:deep(.award-detail-dialog .el-dialog__body) { padding: 0; }
@media (max-width: 640px) {
  .award-toolbar { position: static; }
  .toolbar-select { flex: 1 1 calc(50% - 0.25rem); width: auto; }
  .toolbar-select-name { width: auto; }
  .award-item { gap: 0.5rem; padding: 0.5rem 0.75rem; }
  .competition-badge { min-width: 3rem; padding: 0.25rem; }
  .award-winners { max-width: 5.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 0.75rem; }
  .award-winners-label { display: none; }
  .honor-badge { padding: 0.65rem 1rem; font-size: 1.125rem; }
}
@media (max-width: 420px) { .award-badge { display: none; } }
</style>

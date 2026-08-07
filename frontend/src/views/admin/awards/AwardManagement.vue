<template>
  <AdminPage title="奖项管理"><template #action><el-button type="primary" @click="openAdd">添加奖项</el-button></template>
    <AdminToolbar><el-input v-model="keyword" placeholder="搜索奖项名称、获奖人员..." clearable @keyup.enter="applyFilters" /><el-select v-model="filter.competitionLevel" placeholder="竞赛级别" clearable><el-option v-for="item in competitionLevelOptions" :key="item" :label="item" :value="item" /></el-select><el-select v-model="filter.awardLevel" placeholder="奖项级别" clearable><el-option v-for="item in awardLevelOptions" :key="item" :label="item" :value="item" /></el-select><el-select v-model="filter.year" placeholder="竞赛年份" clearable><el-option v-for="item in yearOptions" :key="item" :label="`${item}年`" :value="String(item)" /></el-select><el-select v-model="filter.competitionName" placeholder="竞赛名称" clearable><el-option v-for="name in competitionNames" :key="name" :label="name" :value="name" /></el-select><el-button type="primary" @click="applyFilters">搜索</el-button><el-button @click="reset">重置</el-button></AdminToolbar>
    <div v-loading="loading" class="award-list">
      <template v-if="sortedYears.length">
        <section v-for="year in sortedYears" :key="year" class="award-year-section">
          <div class="award-year-header">
            <h2>{{ year }} 年度</h2>
            <span class="award-year-line"></span>
            <span class="award-year-count">{{ groupedAwards[year].length }} 项</span>
          </div>
          <div class="award-list-card">
            <button v-for="award in groupedAwards[year]" :key="award.id" type="button" :class="{ 'award-item-highlight': isMajorAward(award) }" class="award-item" @click="showAwardDetail(award)">
              <span :class="getCompetitionClass(award.competitionLevel)" class="competition-badge">{{ award.competitionLevel || '未分类' }}</span>
              <span :class="getAwardTextClass(award.awardLevel)" class="award-badge">{{ award.awardLevel }}</span>
              <span class="award-name">{{ award.competitionName }}<template v-if="award.competitionTrack"> · {{ award.competitionTrack }}</template></span>
              <span class="award-winners"><span class="award-winners-label">获奖人：</span>{{ award.winners.join('、') || '未填写' }}</span>
              <span class="award-arrow">›</span>
            </button>
          </div>
        </section>
      </template>
      <div v-else class="award-empty">暂无符合条件的奖项数据</div>
    </div>
  </AdminPage>
  <el-dialog v-model="detailVisible" :title="undefined" :width="isMobile ? 'calc(100% - 24px)' : '520px'" class="award-detail-dialog" align-center>
    <div v-if="selectedAward" class="award-detail">
      <div :class="getHonorClass(selectedAward.awardLevel)" class="honor-badge">🏆 {{ selectedAward.competitionLevel }} · {{ selectedAward.awardLevel }}</div>
      <h2 class="award-detail-title">{{ selectedAward.competitionName }}</h2>
      <p class="award-detail-meta"><template v-if="selectedAward.competitionTrack">{{ selectedAward.competitionTrack }} · </template>{{ selectedAward.awardDate || `${selectedAward.year}年` }} 获奖</p>
      <div class="award-detail-divider"></div>
      <section class="award-winners-section"><h3>🎖️ 荣誉得主</h3><div class="winner-tags"><span v-for="winner in selectedAward.winners" :key="winner" class="winner-tag">👨‍💻 {{ winner }}</span></div></section>
    </div>
    <template #footer>
      <el-button @click="detailVisible = false">关闭</el-button>
      <template v-if="!isSubmitter"><el-button type="primary" @click="editFromDetail">编辑</el-button><el-button type="danger" @click="removeFromDetail">删除</el-button></template>
    </template>
  </el-dialog>
  <el-dialog v-model="visible" :title="dialogTitle" :width="isMobile ? 'calc(100% - 24px)' : '560px'"><el-form ref="formRef" :model="form" :rules="rules" label-position="top"><el-form-item label="奖项/竞赛名称" prop="competitionName"><el-autocomplete v-model="form.competitionName" class="form-autocomplete" :fetch-suggestions="queryCompetitionNames" placeholder="选择或输入奖项/竞赛名称" /></el-form-item><el-form-item label="赛道"><el-autocomplete v-model="form.competitionTrack" class="form-autocomplete" :fetch-suggestions="queryTracks" placeholder="选择或输入赛道" /></el-form-item><el-form-item label="竞赛级别" prop="competitionLevel"><el-select v-model="form.competitionLevel" placeholder="请选择竞赛级别"><el-option v-for="item in levels" :key="item" :label="item" :value="item" /></el-select></el-form-item><el-form-item label="获奖等级" prop="awardLevel"><el-autocomplete v-model="form.awardLevel" class="form-autocomplete" :fetch-suggestions="queryAwardLevels" placeholder="选择或输入获奖等级" /></el-form-item><el-form-item label="获奖人员" prop="winners"><div class="winner-input"><el-tag v-for="winner in form.winners" :key="winner" closable @close="removeWinner(winner)">{{ winner }}</el-tag><el-input v-model="winnerInput" placeholder="输入姓名后按回车添加" @keydown.enter.prevent="addWinner" /></div></el-form-item><el-form-item label="获奖日期" prop="awardDate"><el-date-picker v-model="form.awardDate" type="month" value-format="YYYY-MM" /></el-form-item></el-form><template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="submit">确定</el-button></template></el-dialog>
</template>

<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, onMounted, reactive, ref } from "vue";
import AdminPage from "@/components/admin/AdminPage.vue";
import AdminToolbar from "@/components/admin/AdminToolbar.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	createAward,
	deleteAward,
	getAdminAwards,
	updateAward,
} from "@/services/adminService";
import { useAdminStore } from "@/stores/adminStore";

/** 奖项等级排序优先级，用于组内排序。 */
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
/** 竞赛级别排序优先级，用于组内排序。 */
const competitionLevelPriority = { 国家级: 0, 省级: 1, 校级: 2, 院级: 2 };

const { isMobile } = useAdminMobile();
const isSubmitter = useAdminStore().hasRole(["submitter"]);
const levels = ["国家级", "省级", "校级"];
const keyword = ref("");
const filter = reactive({
	competitionLevel: "",
	awardLevel: "",
	year: "",
	competitionName: "",
});
const awards = ref([]);
const loading = ref(false);
const visible = ref(false);
const detailVisible = ref(false);
const selectedAward = ref();
const editingId = ref(null);
const dialogTitle = ref("添加奖项");
const formRef = ref();
const winnerInput = ref("");
const namePattern = /^[\u4E00-\u9FFF]+$/;
const form = reactive({
	competitionName: "",
	competitionLevel: "",
	competitionTrack: "",
	awardLevel: "",
	winners: [],
	year: new Date().getFullYear(),
	awardDate: new Date().toISOString().slice(0, 7),
});
const rules = {
	competitionName: [
		{ required: true, message: "请输入奖项名称", trigger: "blur" },
	],
	competitionLevel: [
		{ required: true, message: "请选择竞赛级别", trigger: "change" },
	],
	awardLevel: [{ required: true, message: "请输入获奖等级", trigger: "blur" }],
	winners: [
		{
			required: true,
			type: "array",
			message: "请输入获奖人员",
			trigger: "blur",
		},
	],
	awardDate: [{ required: true, message: "请选择获奖日期", trigger: "change" }],
};

/** 数据中出现的竞赛名称选项。 */
const competitionNames = computed(() =>
	[
		...new Set(
			awards.value.map((item) => item.competitionName).filter(Boolean),
		),
	].sort(),
);
/** 数据中出现的竞赛级别选项。 */
const competitionLevelOptions = computed(() => [
	...new Set(awards.value.map((item) => item.competitionLevel).filter(Boolean)),
]);
/** 数据中出现的奖项等级选项，按荣誉优先级排序。 */
const awardLevelOptions = computed(() =>
	[
		...new Set(awards.value.map((item) => item.awardLevel).filter(Boolean)),
	].sort(
		(a, b) => (awardLevelPriority[a] ?? 99) - (awardLevelPriority[b] ?? 99),
	),
);
/** 数据中出现的年份选项，降序。 */
const yearOptions = computed(() =>
	[
		...new Set(
			awards.value.map((item) => Number(item.year)).filter(Number.isFinite),
		),
	].sort((a, b) => b - a),
);
/** 联想建议：按输入过滤已有竞赛名称。 */
const queryCompetitionNames = (queryString, cb) => {
	const query = queryString?.trim().toLowerCase();
	cb(
		competitionNames.value
			.filter((name) => !query || name.toLowerCase().includes(query))
			.map((value) => ({ value })),
	);
};

/** 联想建议：当前所选奖项名称下已存在的赛道，无则返回空。 */
const queryTracks = (queryString, cb) => {
	const name = form.competitionName?.trim();
	if (!name) return cb([]);
	const query = queryString?.trim().toLowerCase();
	cb(
		[
			...new Set(
				awards.value
					.filter((item) => item.competitionName === name)
					.map((item) => item.competitionTrack)
					.filter(Boolean),
			),
		]
			.sort()
			.filter((track) => !query || track.toLowerCase().includes(query))
			.map((value) => ({ value })),
	);
};

/** 联想建议：按输入过滤已有获奖等级。 */
const queryAwardLevels = (queryString, cb) => {
	const query = queryString?.trim().toLowerCase();
	cb(
		awardLevelOptions.value
			.filter((level) => !query || level.toLowerCase().includes(query))
			.map((value) => ({ value })),
	);
};

/** 筛选后的奖项列表，筛选语义与前台奖项页保持一致。 */
const filteredAwards = computed(() =>
	awards.value.filter(
		(award) =>
			(!keyword.value ||
				award.competitionName
					?.toLowerCase()
					.includes(keyword.value.toLowerCase()) ||
				award.winners.some((winner) =>
					winner.toLowerCase().includes(keyword.value.toLowerCase()),
				) ||
				award.awardLevel
					?.toLowerCase()
					.includes(keyword.value.toLowerCase())) &&
			(!filter.competitionLevel ||
				award.competitionLevel === filter.competitionLevel) &&
			(!filter.awardLevel || award.awardLevel === filter.awardLevel) &&
			(!filter.year || award.year === Number(filter.year)) &&
			(!filter.competitionName ||
				award.competitionName === filter.competitionName),
	),
);

/** 按年份降序分组，组内按级别与奖项等级排序。 */
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

/** 根据奖项等级返回详情主视觉样式。 */
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

/** 打开奖项详情，管理操作在详情中进行，避免破坏列表视觉。 */
const showAwardDetail = (award) => {
	selectedAward.value = award;
	detailVisible.value = true;
};

/** 拉取全部奖项数据，关键词与其余筛选均在前端完成，保证下拉建议基于全量数据。 */
const load = async () => {
	loading.value = true;
	try {
		const data = await getAdminAwards();
		awards.value = Array.isArray(data)
			? data.map((item) => ({
					...item,
					year: Number(item.year),
					winners: Array.isArray(item.winners) ? item.winners : [],
				}))
			: [];
	} catch (error) {
		ElMessage.error("获取奖项信息失败");
		console.error(error);
	} finally {
		loading.value = false;
	}
};
const applyFilters = () => {
	load();
};
const reset = () => {
	keyword.value = "";
	Object.assign(filter, {
		competitionLevel: "",
		awardLevel: "",
		year: "",
		competitionName: "",
	});
	load();
};
const openAdd = () => {
	editingId.value = null;
	dialogTitle.value = "添加奖项";
	Object.assign(form, {
		competitionName: "",
		competitionLevel: "",
		competitionTrack: "",
		awardLevel: "",
		winners: [],
		year: new Date().getFullYear(),
		awardDate: new Date().toISOString().slice(0, 7),
	});
	winnerInput.value = "";
	visible.value = true;
};
const edit = (row) => {
	editingId.value = row.id;
	dialogTitle.value = "编辑奖项";
	Object.assign(form, row);
	winnerInput.value = "";
	visible.value = true;
};
const editFromDetail = () => {
	detailVisible.value = false;
	edit(selectedAward.value);
};
const addWinner = () => {
	const winner = winnerInput.value.trim();
	if (!winner) return true;
	if (!namePattern.test(winner)) {
		ElMessage.warning("姓名只能包含汉字");
		return false;
	}
	if (form.winners.includes(winner)) {
		ElMessage.warning("该姓名已添加");
		return false;
	}
	form.winners.push(winner);
	winnerInput.value = "";
	return true;
};
const removeWinner = (winner) => {
	form.winners = form.winners.filter((item) => item !== winner);
};
const submit = async () => {
	if (!addWinner()) return;
	if (form.winners.some((winner) => !namePattern.test(winner))) {
		ElMessage.warning("姓名只能包含汉字");
		return;
	}
	form.year = Number(form.awardDate.slice(0, 4));
	if (!(await formRef.value.validate().catch(() => false))) return;
	try {
		const payload = {
			...form,
			// date-picker 输出 yyyy-MM（7位），后端需要 yyyy-MM-dd，统一补天
			awardDate:
				form.awardDate.length === 7 ? `${form.awardDate}-01` : form.awardDate,
		};
		if (editingId.value) await updateAward(editingId.value, payload);
		else await createAward(payload);
		ElMessage.success(editingId.value ? "更新奖项成功" : "添加奖项成功");
		visible.value = false;
		load();
	} catch (error) {
		ElMessage.error(editingId.value ? "更新奖项失败" : "添加奖项失败");
		console.error(error);
	}
};
const remove = async (id) => {
	try {
		await ElMessageBox.confirm("确定要删除这个奖项吗？", "确认删除", {
			type: "warning",
		});
		await deleteAward(id);
		ElMessage.success("删除奖项成功");
		load();
	} catch (error) {
		if (error !== "cancel") {
			ElMessage.error("删除奖项失败");
			console.error(error);
		}
	}
};
const removeFromDetail = async () => {
	const id = selectedAward.value?.id;
	if (!id) return;
	detailVisible.value = false;
	await remove(id);
};
onMounted(load);
</script>

<style scoped>
.award-list { min-height: 120px; }
.award-year-section { margin-bottom: 28px; }
.award-year-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.award-year-header h2 { margin: 0; color: #1f2937; font-size: 20px; font-weight: 700; }
.award-year-line { height: 1px; flex: 1; background: #e5e7eb; }
.award-year-count { color: #9ca3af; font-size: 14px; white-space: nowrap; }
.award-list-card { overflow: hidden; border: 1px solid #e5e7eb; border-radius: 12px; background: #fff; }
.award-item { display: flex; width: 100%; min-height: 52px; align-items: center; gap: 12px; border: 0; border-bottom: 1px solid #f3f4f6; padding: 8px 16px; background: transparent; text-align: left; cursor: pointer; }
.award-item:last-child { border-bottom: 0; }
.award-item:hover { background: #f8fafc; }
.award-item-highlight { border-left: 4px solid #d4a72c; background: rgb(255 215 0 / 0.05); }
.award-item-highlight:hover { background: rgb(255 215 0 / 0.1); }
.competition-badge { flex-shrink: 0; min-width: 3.75rem; border-radius: 9999px; padding: 4px 8px; font-size: 12px; font-weight: 700; line-height: 1.25; text-align: center; }
.competition-national { background: linear-gradient(135deg, #7f1d1d, #eab308); color: #fff; }
.competition-national::before { content: "🏆 "; }
.competition-provincial { background: #0f766e; color: #fff; }
.competition-campus { border: 1px solid #d1d5db; color: #6b7280; }
.award-badge { flex-shrink: 0; font-size: 13px; font-weight: 700; line-height: 1; }
.award-gold { color: #ca8a04; }
.award-silver { color: #64748b; }
.award-bronze { color: #b45309; }
.award-default { color: #6b7280; }
.award-name { min-width: 0; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; color: #374151; font-weight: 500; }
.award-winners { flex-shrink: 0; color: #6b7280; font-size: 14px; }
.award-winners-label { color: #9ca3af; }
.award-arrow { flex-shrink: 0; color: #cbd5e1; font-size: 20px; }
.award-empty { padding: 48px 0; text-align: center; color: #6b7280; }
.award-detail { padding: 24px 16px; border: 1px solid #d6b76e; background: linear-gradient(135deg, #fffdf5, #fff 55%, #f7f0d8); box-shadow: inset 0 0 0 6px #fff, inset 0 0 0 7px #e5d09a; text-align: center; }
.honor-badge { display: inline-flex; align-items: center; gap: 6px; border-radius: 999px; padding: 12px 20px; font-size: 20px; font-weight: 800; }
.honor-gold { background: linear-gradient(135deg, #fef3c7, #f59e0b); color: #78350f; }
.honor-silver { background: linear-gradient(135deg, #e2e8f0, #94a3b8); color: #1e3a5f; }
.honor-bronze { background: linear-gradient(135deg, #fed7aa, #9a3412); color: #fff7ed; }
.honor-default { background: linear-gradient(135deg, #dbeafe, #60a5fa); color: #1e3a8a; }
.award-detail-title { margin: 24px 0 0; color: #1f2937; font-size: 24px; line-height: 1.4; }
.award-detail-meta { margin: 8px 0 0; color: #64748b; }
.award-detail-divider { height: 1px; margin: 24px 0; background: #e5e7eb; }
.award-winners-section h3 { margin: 0 0 12px; color: #374151; }
.winner-tags { display: flex; flex-wrap: wrap; justify-content: center; gap: 8px; }
.winner-tag { border-radius: 999px; background: #dbeafe; padding: 7px 12px; color: #1d4ed8; font-weight: 600; }
.winner-input { display: flex; flex-wrap: wrap; gap: 8px; width: 100%; }
.winner-input .el-input { flex: 1; min-width: 160px; }
.form-autocomplete { width: 100%; }
@media (max-width: 768px) {
  .award-year-header h2 { font-size: 17px; }
  .award-item { gap: 8px; padding: 8px 12px; }
  .award-winners { max-width: 5.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 12px; }
  .competition-badge { min-width: 3rem; padding: 4px; }
  .award-winners-label { display: none; }
}
@media (max-width: 420px) { .award-badge { display: none; } }
</style>

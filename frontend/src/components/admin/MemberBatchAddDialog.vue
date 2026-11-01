<template>
  <el-dialog
    :model-value="modelValue"
    title="批量从报名记录添加"
    :width="isMobile ? 'calc(100% - 24px)' : '960px'"
    class="admin-dialog"
    append-to-body
    :close-on-click-modal="false"
    @update:model-value="emit('update:modelValue', $event)"
    @closed="reset"
  >
    <div v-loading="matching" :element-loading-text="loadingText" class="batch-body">
      <template v-if="step === 'input'">
        <el-input
          v-model="rawText"
          type="textarea"
          :rows="10"
          resize="vertical"
          placeholder="每行一个学号，也支持逗号、空格分隔&#10;例如：&#10;20230101001&#10;20230101002"
        />
        <p class="batch-tip">
          匹配到唯一报名记录且尚未加入社团的学号会自动添加；已有成员、多条报名或找不到报名记录的学号会保留在结果表中，可逐条处理。
        </p>
        <el-alert
          v-if="invalidTokens.length"
          class="batch-alert"
          type="warning"
          :closable="false"
          show-icon
          :title="`以下内容不是有效学号，将被忽略：${invalidTokens.slice(0, 10).join('、')}${invalidTokens.length > 10 ? ' 等' : ''}`"
        />
        <div class="batch-count">已识别 <strong>{{ studentIds.length }}</strong> 个学号</div>
      </template>

      <template v-else>
        <div class="batch-summary">
          <span>共 <strong>{{ rows.length }}</strong> 个学号</span>
          <span>本次添加 <strong>{{ summary.added }}</strong></span>
          <span>覆盖更新 <strong>{{ summary.updated }}</strong></span>
          <span>已存在 <strong>{{ summary.existing }}</strong></span>
          <span>待处理 <strong>{{ unresolvedCount }}</strong></span>
          <el-checkbox v-model="onlyUnresolved">只看待处理</el-checkbox>
        </div>

        <AdminTable>
          <el-table
            :data="displayRows"
            row-key="studentId"
            stripe
            :expand-row-keys="expandedKeys"
            @expand-change="handleExpandChange"
          >
            <el-table-column type="expand" width="40">
              <template #default="{ row }">
                <div class="batch-expand">
                  <template v-if="row.status === 'different'">
                    <div v-if="row.applications.length > 1" class="batch-picker">
                      <p class="batch-expand__title">该学号有多条报名记录，请选择要对比的一条</p>
                      <el-radio-group
                        :model-value="row.selectedId"
                        class="batch-radios"
                        @change="(id) => selectApplication(row, id)"
                      >
                        <el-radio v-for="app in row.applications" :key="app.id" :value="app.id" class="batch-radio">
                          {{ app.applicationYear }} 年 · {{ app.name }} · {{ app.department }} · {{ app.phone || "无电话" }}
                        </el-radio>
                      </el-radio-group>
                    </div>
                    <p class="batch-expand__title">成员现有资料与报名记录存在差异</p>
                    <el-table :data="row.diff" size="small" border>
                      <el-table-column prop="label" label="字段" width="100" />
                      <el-table-column prop="before" label="当前成员资料" min-width="160" />
                      <el-table-column prop="after" label="报名记录" min-width="160" />
                    </el-table>
                    <div class="batch-expand__actions">
                      <el-button type="primary" size="small" :loading="row.saving" @click="overwriteRow(row)">
                        用报名记录覆盖更新
                      </el-button>
                    </div>
                  </template>

                  <template v-else-if="row.status === 'multiple'">
                    <p class="batch-expand__title">该学号有 {{ row.applications.length }} 条报名记录，请选择本次采用的一条</p>
                    <el-radio-group
                      :model-value="row.selectedId"
                      class="batch-radios"
                      @change="(id) => selectApplication(row, id)"
                    >
                      <el-radio v-for="app in row.applications" :key="app.id" :value="app.id" class="batch-radio">
                        {{ app.applicationYear }} 年 · {{ app.name }} · {{ app.department }} · {{ app.phone || "无电话" }}
                      </el-radio>
                    </el-radio-group>
                    <div class="batch-expand__actions">
                      <el-button type="primary" size="small" :loading="row.saving" @click="confirmMultiple(row)">
                        {{ row.member ? "用所选记录覆盖更新" : "添加所选记录" }}
                      </el-button>
                    </div>
                  </template>

                  <template v-else-if="row.status === 'missing' || row.status === 'failed'">
                    <el-alert
                      v-if="row.status === 'failed'"
                      class="batch-alert"
                      type="error"
                      :closable="false"
                      show-icon
                      :title="row.message"
                    />
                    <div class="batch-form">
                      <div class="batch-field"><span>姓名</span><el-input v-model="row.form.name" size="small" /></div>
                      <div class="batch-field"><span>班级</span><el-input v-model="row.form.className" size="small" /></div>
                      <div class="batch-field"><span>专业</span><el-input v-model="row.form.major" size="small" /></div>
                      <div class="batch-field"><span>电话</span><el-input v-model="row.form.phone" size="small" /></div>
                      <div class="batch-field"><span>QQ号</span><el-input v-model="row.form.QQNumber" size="small" /></div>
                      <div class="batch-field">
                        <span>部门</span>
                        <el-select v-model="row.form.department" size="small" placeholder="请选择部门" style="width: 100%">
                          <el-option v-for="item in departments" :key="item" :label="item" :value="item" />
                        </el-select>
                      </div>
                      <div class="batch-field">
                        <span>加入年份</span>
                        <el-select v-model="row.form.joinYear" size="small" style="width: 100%">
                          <el-option v-for="year in yearOptions" :key="year" :label="`${year} 年`" :value="year" />
                        </el-select>
                      </div>
                    </div>
                    <div class="batch-expand__actions">
                      <el-button type="primary" size="small" :loading="row.saving" @click="saveManualRow(row)">
                        保存并添加
                      </el-button>
                    </div>
                  </template>

                  <template v-else>
                    <el-descriptions :column="2" size="small" border>
                      <el-descriptions-item label="姓名">{{ row.form.name || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="班级">{{ row.form.className || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="专业">{{ row.form.major || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="部门">{{ row.form.department || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="电话">{{ row.form.phone || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="QQ号">{{ row.form.QQNumber || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="加入年份">{{ row.form.joinYear || "—" }}</el-descriptions-item>
                      <el-descriptions-item label="报名记录">
                        {{ row.applications.length ? `${row.applications.length} 条` : "无" }}
                      </el-descriptions-item>
                    </el-descriptions>
                  </template>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="studentId" label="学号" width="130" />
            <el-table-column label="姓名" width="100">
              <template #default="{ row }">{{ row.form.name || "—" }}</template>
            </el-table-column>
            <el-table-column label="专业" min-width="150">
              <template #default="{ row }">{{ row.form.major || "—" }}</template>
            </el-table-column>
            <el-table-column label="部门" width="120">
              <template #default="{ row }">{{ row.form.department || "—" }}</template>
            </el-table-column>
            <el-table-column label="联系方式" min-width="150">
              <template #default="{ row }">
                <div>{{ row.form.phone || "—" }}</div>
                <div v-if="row.form.QQNumber" class="batch-sub">QQ {{ row.form.QQNumber }}</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="160">
              <template #default="{ row }">
                <el-tag :type="statusMeta(row.status).type" size="small">{{ statusMeta(row.status).label }}</el-tag>
                <div v-if="row.message" class="batch-sub">{{ row.message }}</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="130" fixed="right">
              <template #default="{ row }">
                <el-button
                  v-if="row.status === 'different'"
                  link
                  type="primary"
                  :loading="row.saving"
                  @click="overwriteRow(row)"
                >
                  覆盖更新
                </el-button>
                <el-button
                  v-else-if="row.status === 'multiple'"
                  link
                  type="primary"
                  :loading="row.saving"
                  @click="confirmMultiple(row)"
                >
                  {{ row.member ? "覆盖更新" : "添加所选" }}
                </el-button>
                <el-button
                  v-else-if="row.status === 'missing' || row.status === 'failed'"
                  link
                  type="primary"
                  @click="toggleExpand(row)"
                >
                  补录
                </el-button>
                <el-button
                  v-else-if="row.status === 'pending'"
                  link
                  type="primary"
                  :loading="row.saving"
                  @click="createRowAction(row)"
                >
                  添加
                </el-button>
                <el-button
                  v-else-if="row.status === 'existing' && row.member"
                  link
                  type="primary"
                  @click="emit('view-member', row.member.id)"
                >
                  查看
                </el-button>
                <span v-else class="batch-sub">—</span>
              </template>
            </el-table-column>
          </el-table>
        </AdminTable>
      </template>
    </div>

    <template #footer>
      <template v-if="step === 'input'">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" :loading="matching" :disabled="!studentIds.length" @click="start">
          开始匹配并添加
        </el-button>
      </template>
      <template v-else>
        <el-button @click="backToInput">重新匹配</el-button>
        <el-button v-if="summary.different" type="warning" :loading="overwritingAll" @click="overwriteAll">
          覆盖全部信息不同项（{{ summary.different }}）
        </el-button>
        <el-button @click="close">关闭</el-button>
      </template>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { computed, ref } from "vue";
import AdminTable from "@/components/admin/AdminTable.vue";
import { useAdminMobile } from "@/composables/useAdminMobile";
import {
	createMember,
	getMember,
	matchMembersByStudentIds,
	updateMember,
} from "@/services/adminService";

const props = defineProps({
	modelValue: { type: Boolean, default: false },
	/** 专业代号映射，用于无报名记录时按学号预填专业 */
	majorMapping: { type: Object, default: () => ({}) },
});
const emit = defineEmits(["update:modelValue", "added", "view-member"]);

const { isMobile } = useAdminMobile();
const departments = ["算法部", "项目竞赛部", "综合管理部"];
const currentYear = new Date().getFullYear();
const yearOptions = computed(() => {
	const options = [];
	for (let year = currentYear + 1; year >= currentYear - 6; year--)
		options.push(year);
	return options;
});

/** 参与重复判定的字段，加入年份和状态不参与覆盖比较 */
const COMPARE_FIELDS = [
	{ key: "name", label: "姓名" },
	{ key: "className", label: "班级" },
	{ key: "major", label: "专业" },
	{ key: "phone", label: "电话" },
	{ key: "QQNumber", label: "QQ号" },
	{ key: "department", label: "部门" },
];
const STATUS_META = {
	added: { label: "已添加", type: "success" },
	updated: { label: "已更新", type: "success" },
	pending: { label: "待添加", type: "primary" },
	existing: { label: "已存在", type: "info" },
	different: { label: "信息不同", type: "warning" },
	multiple: { label: "多条报名", type: "warning" },
	missing: { label: "无报名记录", type: "danger" },
	failed: { label: "添加失败", type: "danger" },
};

const step = ref("input");
const rawText = ref("");
const rows = ref([]);
const matching = ref(false);
const creatingTotal = ref(0);
const creatingDone = ref(0);
const overwritingAll = ref(false);
const expandedKeys = ref([]);
const onlyUnresolved = ref(false);

const statusMeta = (status) => STATUS_META[status] || STATUS_META.failed;
const loadingText = computed(() =>
	creatingTotal.value
		? `正在自动添加 ${creatingDone.value}/${creatingTotal.value}`
		: "正在匹配报名记录...",
);
const studentIds = computed(() => parseStudentIds(rawText.value).filter(isValidStudentId));
// 纯文字多为从表格粘贴带出的姓名，静默忽略；含数字但格式不对的才提示
const invalidTokens = computed(() =>
	parseStudentIds(rawText.value).filter(
		(token) => !isValidStudentId(token) && /\d/.test(token),
	),
);
const summary = computed(() => {
	const counts = {
		added: 0,
		updated: 0,
		existing: 0,
		different: 0,
		multiple: 0,
		missing: 0,
		failed: 0,
		pending: 0,
	};
	for (const row of rows.value) {
		if (counts[row.status] !== undefined) counts[row.status] += 1;
	}
	return counts;
});
const UNRESOLVED_STATUS = new Set([
	"different",
	"multiple",
	"missing",
	"failed",
	"pending",
]);
const unresolvedCount = computed(
	() =>
		summary.value.different +
		summary.value.multiple +
		summary.value.missing +
		summary.value.failed +
		summary.value.pending,
);
const displayRows = computed(() =>
	onlyUnresolved.value
		? rows.value.filter((row) => UNRESOLVED_STATUS.has(row.status))
		: rows.value,
);

/** 按分隔符拆分学号并去重，保留输入顺序 */
const parseStudentIds = (text) =>
	Array.from(
		new Set(
			text
				.split(/[\s,，、;；]+/)
				.map((token) => token.trim())
				.filter(Boolean),
		),
	);
const isValidStudentId = (token) => /^\d{6,20}$/.test(token);

const normalize = (value) => String(value ?? "").trim();
const diffMemberApplication = (member, form) =>
	COMPARE_FIELDS.map(({ key, label }) => ({
		key,
		label,
		before: normalize(member[key]),
		after: normalize(form[key]),
	})).filter((item) => item.after && item.before !== item.after);

/** 无报名记录时按学号推导班级、专业与入学年份，与手动添加逻辑保持一致 */
const deriveForm = (studentId) => {
	const form = {
		name: "",
		className: "",
		major: "",
		phone: "",
		QQNumber: "",
		department: "",
		joinYear: currentYear,
	};
	if (/^\d{11}$/.test(studentId)) {
		const major = props.majorMapping?.[studentId.substring(4, 8)];
		if (major?.fullName) form.major = major.fullName;
		form.className = `B${studentId.substring(2, 4)}${studentId.charAt(8)}`;
		form.joinYear = Number(studentId.substring(0, 4));
	}
	return form;
};
const formFromApplication = (application) => ({
	name: application.name ?? "",
	className: application.className ?? "",
	major: application.major ?? "",
	phone: application.phone ?? "",
	QQNumber: application.QQNumber ?? "",
	department: application.department ?? "",
	joinYear: application.applicationYear ?? currentYear,
});
const formFromMember = (member) => ({
	name: member.name ?? "",
	className: member.className ?? "",
	major: member.major ?? "",
	phone: member.phone ?? "",
	QQNumber: member.QQNumber ?? "",
	department: member.department ?? "",
	joinYear: member.joinYear ?? currentYear,
});

/** 根据成员与报名记录重新判定行状态，已有成员优先按信息差异分类 */
const refreshRowState = (row) => {
	row.diff = [];
	if (row.member) {
		if (!row.applications.length) {
			row.status = "existing";
			row.message = "已是成员，无报名记录";
			return;
		}
		row.diff = diffMemberApplication(row.member, row.form);
		if (row.diff.length) {
			row.status = "different";
			row.message = `${row.diff.length} 项信息与报名记录不同`;
		} else {
			row.status = "existing";
			row.message = "已是成员，信息一致";
		}
		return;
	}
	if (!row.applications.length) {
		row.status = "missing";
		row.message = "未找到报名记录";
		return;
	}
	if (row.applications.length > 1) {
		row.status = "multiple";
		row.message = `有 ${row.applications.length} 条报名记录，展开可选择`;
		return;
	}
	row.status = "pending";
	row.message = "";
};

/** 优先用报名记录作为待添加资料，其次复用已有成员资料，最后按学号推导 */
const buildForm = (studentId, member, application) => {
	if (application) return formFromApplication(application);
	if (member) return formFromMember(member);
	return deriveForm(studentId);
};

const rowFromMatch = (item) => {
	const member = item.member || null;
	const applications = Array.isArray(item.applications) ? item.applications : [];
	const selected = applications[0] || null;
	const row = {
		studentId: item.studentId,
		member,
		applications,
		selectedId: selected?.id ?? null,
		diff: [],
		form: buildForm(item.studentId, member, selected),
		status: "pending",
		message: "",
		saving: false,
	};
	refreshRowState(row);
	return row;
};

const selectApplication = (row, applicationId) => {
	const application = row.applications.find((item) => item.id === applicationId);
	if (!application) return;
	row.selectedId = applicationId;
	row.form = formFromApplication(application);
	refreshRowState(row);
	if (row.status === "multiple")
		row.message = `已选择 ${application.applicationYear} 年记录`;
};

const buildCreatePayload = (row) => ({
	name: row.form.name?.trim(),
	studentId: row.studentId,
	className: row.form.className?.trim(),
	major: row.form.major?.trim(),
	phone: row.form.phone?.trim(),
	QQNumber: row.form.QQNumber?.trim(),
	department: row.form.department,
	joinYear: row.form.joinYear || currentYear,
	status: "active",
	remark: "",
});

/** 创建成员，重复时回查已有成员并重新判定状态 */
const createRow = async (row) => {
	row.saving = true;
	try {
		await createMember(buildCreatePayload(row));
		row.status = "added";
		row.message = "已添加";
		return true;
	} catch (error) {
		if (error?.code === "A000500" && error?.data?.id) {
			try {
				row.member = await getMember(error.data.id);
				refreshRowState(row);
			} catch (fetchError) {
				row.status = "failed";
				row.message = "该学号已是成员，但读取成员资料失败";
				console.error("读取已有成员失败:", fetchError);
			}
		} else {
			row.status = "failed";
			row.message = error?.message || "添加失败";
		}
		return false;
	} finally {
		row.saving = false;
	}
};

/** 简单并发池，避免一次向服务端打出几十个创建请求 */
const runWithConcurrency = async (items, limit, worker) => {
	let cursor = 0;
	const runners = Array.from({ length: Math.min(limit, items.length) }, async () => {
		while (cursor < items.length) {
			const current = items[cursor];
			cursor += 1;
			await worker(current);
		}
	});
	await Promise.all(runners);
};

const autoAddPending = async () => {
	const targets = rows.value.filter((row) => row.status === "pending");
	if (!targets.length) return;
	creatingTotal.value = targets.length;
	creatingDone.value = 0;
	await runWithConcurrency(targets, 4, async (row) => {
		await createRow(row);
		creatingDone.value += 1;
	});
	creatingTotal.value = 0;
	const addedCount = targets.filter((row) => row.status === "added").length;
	if (addedCount) {
		ElMessage.success(`已自动添加 ${addedCount} 位成员`);
		emit("added");
	}
};

const start = async () => {
	if (!studentIds.value.length) return;
	matching.value = true;
	try {
		const results = await matchMembersByStudentIds(studentIds.value);
		rows.value = results.map(rowFromMatch);
		step.value = "result";
		await autoAddPending();
	} catch (error) {
		ElMessage.error(error?.message || "匹配报名记录失败");
		console.error(error);
	} finally {
		matching.value = false;
	}
};

const backToInput = () => {
	step.value = "input";
	rows.value = [];
	expandedKeys.value = [];
};

const close = () => {
	emit("update:modelValue", false);
};

const reset = () => {
	step.value = "input";
	rawText.value = "";
	rows.value = [];
	expandedKeys.value = [];
	matching.value = false;
	creatingTotal.value = 0;
	creatingDone.value = 0;
};

const handleExpandChange = (_row, expandedRows) => {
	expandedKeys.value = expandedRows.map((item) => item.studentId);
};
const toggleExpand = (row) => {
	expandedKeys.value = expandedKeys.value.includes(row.studentId)
		? expandedKeys.value.filter((id) => id !== row.studentId)
		: [...expandedKeys.value, row.studentId];
};

const createRowAction = async (row) => {
	const created = await createRow(row);
	if (created) {
		ElMessage.success(`${row.studentId} 已添加`);
		emit("added");
	}
};

const saveManualRow = async (row) => {
	const missing = [];
	if (!row.form.name?.trim()) missing.push("姓名");
	if (!row.form.major?.trim()) missing.push("专业");
	if (!row.form.department) missing.push("部门");
	if (!row.form.joinYear) missing.push("加入年份");
	if (missing.length) {
		ElMessage.warning(`请填写：${missing.join("、")}`);
		return;
	}
	await createRowAction(row);
};

const overwriteRow = async (row, { silent = false } = {}) => {
	if (!row.member?.id) return false;
	row.saving = true;
	try {
		// 报名记录缺失的字段保留成员原值，避免覆盖更新时清空已有资料
		const merged = (key) => row.form[key]?.trim() || row.member[key] || "";
		await updateMember(row.member.id, {
			name: merged("name"),
			studentId: row.studentId,
			className: merged("className"),
			major: merged("major"),
			phone: merged("phone"),
			QQNumber: merged("QQNumber"),
			department: merged("department"),
			joinYear: row.member.joinYear ?? row.form.joinYear,
			status: row.member.status || "active",
			remark: row.member.remark || "",
		});
		row.status = "updated";
		row.message = "已覆盖更新";
		row.diff = [];
		if (!silent) {
			ElMessage.success(`${row.studentId} 已覆盖更新`);
			emit("added");
		}
		return true;
	} catch (error) {
		if (!silent) ElMessage.error(error?.message || "覆盖更新失败");
		console.error(error);
		return false;
	} finally {
		row.saving = false;
	}
};

const confirmMultiple = async (row) => {
	if (row.member) {
		await overwriteRow(row);
		return;
	}
	await createRowAction(row);
};

const overwriteAll = async () => {
	const targets = rows.value.filter((row) => row.status === "different");
	if (!targets.length) return;
	try {
		await ElMessageBox.confirm(
			`将用报名记录覆盖 ${targets.length} 位成员的现有资料，是否继续？`,
			"批量覆盖更新",
			{ type: "warning" },
		);
	} catch (error) {
		if (error !== "cancel") console.error(error);
		return;
	}
	overwritingAll.value = true;
	let success = 0;
	await runWithConcurrency(targets, 4, async (row) => {
		if (await overwriteRow(row, { silent: true })) success += 1;
	});
	overwritingAll.value = false;
	if (success) {
		ElMessage.success(`已覆盖更新 ${success} 位成员`);
		emit("added");
	}
};
</script>

<style scoped>
.batch-body { min-height: 120px; }
.batch-tip { margin: 8px 0 0; color: #98a2b3; font-size: 12px; line-height: 1.6; }
.batch-count { margin-top: 10px; color: #475467; font-size: 13px; }
.batch-count strong { color: #1d2939; }
.batch-summary { display: flex; flex-wrap: wrap; gap: 6px 16px; margin-bottom: 12px; color: #475467; font-size: 13px; }
.batch-summary strong { color: #1d2939; }
.batch-sub { margin-top: 2px; color: #98a2b3; font-size: 12px; line-height: 1.4; }
.batch-expand { padding: 4px 12px 8px; }
.batch-expand__title { margin: 0 0 8px; color: #344054; font-size: 13px; }
.batch-expand__actions { display: flex; gap: 8px; margin-top: 10px; }
.batch-picker { margin-bottom: 12px; }
.batch-radios { display: flex; flex-direction: column; align-items: flex-start; gap: 4px; }
.batch-radio { height: auto; margin-right: 0; }
.batch-form { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 10px 12px; }
.batch-field { display: flex; flex-direction: column; gap: 4px; color: #475467; font-size: 12px; }
.batch-alert { margin-bottom: 10px; }
@media (max-width: 768px) {
	.batch-form { grid-template-columns: 1fr; }
}
</style>

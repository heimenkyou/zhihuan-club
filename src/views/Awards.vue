<template>
  <div class="awards-page min-h-screen">
    <CommonNavbar />
    <main
      class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 mt-14 mobile:py-4 mobile:mt-12"
    >
      <!-- 页面标题 -->
      <div class="text-center mb-12 animate-fade-in">
        <h1 class="text-4xl font-bold text-dark mb-3">竞赛奖项</h1>
        <p class="text-gray-500 max-w-2xl mx-auto">
          展示社团成员在各类竞赛中获得的奖项
        </p>
      </div>

      <!-- 搜索输入框 -->
      <div class="mb-6 max-w-md mx-auto">
        <div class="relative">
          <input
            type="text"
            v-model="searchKeyword"
            placeholder="搜索奖项名称、获奖人员..."
            class="w-full px-4 py-3 pl-10 bg-white rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all"
            @input="handleSearch"
          />
          <i
            class="fa fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
          ></i>
        </div>
      </div>

      <!-- 多条件筛选栏 -->
      <div class="bg-white rounded-xl shadow-md p-6 mb-8 animate-scale-in">
        <h3 class="text-lg font-bold text-dark mb-4">奖项筛选</h3>

        <!-- 竞赛级别筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛级别</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '国家级'
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '国家级'"
            >
              国家级
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '省级'
                  ? 'bg-blue-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '省级'"
            >
              省级
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionLevel === '校级'
                  ? 'bg-blue-400 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionLevel = '校级'"
            >
              校级
            </button>
          </div>
        </div>

        <!-- 奖项级别筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >奖项级别</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '一等奖'
                  ? 'bg-yellow-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '一等奖'"
            >
              一等奖(金牌)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '二等奖'
                  ? 'bg-gray-400 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '二等奖'"
            >
              二等奖(银牌)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '三等奖'
                  ? 'bg-orange-600 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '三等奖'"
            >
              三等奖(铜牌)
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '优秀奖'
                  ? 'bg-green-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '优秀奖'"
            >
              优秀奖
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.awardLevel === '其他'
                  ? 'bg-purple-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.awardLevel = '其他'"
            >
              其他
            </button>
          </div>
        </div>

        <!-- 竞赛年份筛选 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛年份</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = ''"
            >
              全部
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2025'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2025'"
            >
              2025年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2024'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2024'"
            >
              2024年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2023'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2023'"
            >
              2023年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2022'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2022'"
            >
              2022年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2021'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2021'"
            >
              2021年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2020'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2020'"
            >
              2020年
            </button>
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.year === '2019'
                  ? 'bg-indigo-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.year = '2019'"
            >
              2019年
            </button>
          </div>
        </div>

        <!-- 竞赛名称筛选 - 保留原始下拉框样式 -->
        <div class="mb-3">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >竞赛名称</label
          >
          <select
            v-model="filter.competitionName"
            class="w-full bg-white border border-gray-300 text-dark py-2 px-3 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary/50 cursor-pointer"
          >
            <option value="">全部名称</option>
            <option v-for="name in competitionNames" :key="name" :value="name">
              {{ name }}
            </option>
          </select>
        </div>

        <!-- 新增：赛道筛选（当选择了竞赛项目且该项目有赛道时显示） -->
        <div
          class="mb-3"
          v-if="filter.competitionName && currentCompetitionTracks.length > 0"
        >
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >赛道</label
          >
          <div class="flex flex-wrap gap-2">
            <button
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionTrack === ''
                  ? 'bg-primary text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionTrack = ''"
            >
              全部
            </button>
            <button
              v-for="track in currentCompetitionTracks"
              :key="track"
              :class="[
                'px-4 py-2 rounded-full text-sm transition-all',
                filter.competitionTrack === track
                  ? 'bg-teal-500 text-white'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
              ]"
              @click="filter.competitionTrack = track"
            >
              {{ track }}
            </button>
          </div>
        </div>

        <!-- 筛选操作按钮 -->
        <div class="flex justify-end mt-6 gap-3">
          <button
            @click="resetFilters"
            class="px-6 py-2.5 bg-gray-100 text-gray-700 rounded-lg text-sm font-medium hover:bg-gray-200 transition-colors"
          >
            重置筛选
          </button>
        </div>
      </div>

      <!-- 奖项统计卡片 -->
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-6 mb-12">
        <div class="bg-white rounded-xl shadow-lg p-6 animate-slide-up">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold text-dark">总奖项数</h3>
            <div
              class="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center"
            >
              <i class="fa fa-trophy text-2xl text-primary"></i>
            </div>
          </div>
          <p class="text-4xl font-bold text-primary text-red-500">
            {{ filteredAwards.length }}
          </p>
        </div>
      </div>

      <!-- 加载状态 -->
      <div
        v-if="loading"
        class="flex justify-center items-center py-12 bg-white rounded-xl shadow-sm"
      >
        <div
          class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"
        ></div>
      </div>

      <!-- 错误提示 -->
      <div
        v-else-if="error"
        class="text-center py-12 bg-white rounded-xl shadow-sm"
      >
        <i class="fa fa-exclamation-circle text-red-500 text-4xl mb-4"></i>
        <p class="text-red-500 mb-2">{{ error }}</p>
        <button
          @click="fetchAwards()"
          class="px-4 py-2 bg-primary text-white rounded-full hover:bg-primary/90 transition-colors"
        >
          重试
        </button>
      </div>

      <!-- 奖项列表 -->
      <div v-else class="space-y-10">
        <!-- 按年份分组渲染奖项 -->
        <section
          v-for="(year, index) in sortedYears"
          :key="year"
          class="animate-slide-up"
          :style="{ animationDelay: `${0.1 * index}s` }"
        >
          <!-- 年份标题栏 -->
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-2xl font-bold text-dark">{{ year }}年度奖项</h2>
            <span
              class="bg-primary/10 text-primary px-3 py-1 rounded-full text-sm"
            >
              {{
                groupedAwards[year]["国家级"].length +
                groupedAwards[year]["省级"].length +
                groupedAwards[year]["校级"].length
              }}个奖项
            </span>
          </div>

          <!-- 按奖项级别分组渲染 -->
          <div class="space-y-4">
            <!-- 国家级奖项组 -->
            <div
              v-if="groupedAwards[year]['国家级'].length"
              class="bg-white rounded-xl shadow-md overflow-hidden"
            >
              <div class="bg-blue-900 text-white p-4">
                <h3 class="text-lg font-semibold flex items-center gap-2">
                  <i class="fa fa-star"></i> 国家级奖项
                </h3>
              </div>
              <div class="award-grid grid grid-cols-1 md:grid-cols-2 gap-4 p-4">
                <div
                  v-for="award in groupedAwards[year]['国家级']"
                  :key="award.id"
                  class="award-card cursor-pointer bg-white rounded-xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-all duration-300 hover:translate-y-[-2px]"
                  @click="goToProjectDetail(award.id)"
                >
                  <!-- 竞赛信息头部 -->
                  <div class="flex items-start justify-between mb-4">
                    <h4
                      class="font-semibold text-dark text-lg line-clamp-2 flex-1 pr-2"
                    >
                      {{ award.competitionName }}
                    </h4>
                    <!-- 奖项级别徽章 - 直接使用原始奖项名称 -->
                    <span
                      :class="getAwardBadgeClass(award.awardLevel)"
                      class="text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap"
                    >
                      {{ award.awardLevel }}
                      <!-- 这里直接使用原始的奖项名称 -->
                    </span>
                  </div>

                  <!-- 修改赛道信息部分 - 无论是否有赛道都显示，但样式不同 -->
                  <div class="flex items-center mb-3">
                    <i class="fa fa-flag mr-1.5 text-gray-500"></i>
                    <span
                      v-if="award.competitionTrack"
                      class="bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full text-sm"
                    >
                      {{ award.competitionTrack }}
                    </span>
                    <span
                      v-else
                      class="bg-gray-50 text-gray-500 px-2 py-0.5 rounded-full text-sm italic"
                    >
                      尚未区分赛道
                    </span>
                  </div>

                  <!-- 获奖人员信息 -->
                  <div class="flex items-start mb-3">
                    <i class="fa fa-users text-gray-500 mr-2 mt-0.5"></i>
                    <p class="text-gray-600 text-sm flex-1 line-clamp-2">
                      {{ award.winners.join("、") }}
                    </p>
                  </div>

                  <!-- 底部信息 -->
                  <div
                    class="flex items-center justify-between pt-3 border-t border-gray-100"
                  >
                    <div class="flex items-center text-xs text-gray-500">
                      <i class="fa fa-calendar-o mr-1"></i>
                      {{ award.awardDate }}
                    </div>
                    <div class="text-xs text-gray-400">
                      {{ award.competitionLevel }}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 省级奖项组 - 结构与国家级相同 -->
            <div
              v-if="groupedAwards[year]['省级'].length"
              class="bg-white rounded-xl shadow-md overflow-hidden"
            >
              <div class="bg-blue-800 text-white p-4">
                <h3 class="text-lg font-semibold flex items-center gap-2">
                  <i class="fa fa-map-marker"></i> 省级奖项
                </h3>
              </div>
              <div class="award-grid grid grid-cols-1 md:grid-cols-2 gap-4 p-4">
                <div
                  v-for="award in groupedAwards[year]['省级']"
                  :key="award.id"
                  class="award-card cursor-pointer bg-white rounded-xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-all duration-300 hover:translate-y-[-2px]"
                  @click="goToProjectDetail(award.id)"
                >
                  <!-- 竞赛信息头部 -->
                  <div class="flex items-start justify-between mb-4">
                    <h4
                      class="font-semibold text-dark text-lg line-clamp-2 flex-1 pr-2"
                    >
                      {{ award.competitionName }}
                    </h4>
                    <!-- 奖项级别徽章 - 直接使用原始奖项名称 -->
                    <span
                      :class="getAwardBadgeClass(award.awardLevel)"
                      class="text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap"
                    >
                      {{ award.awardLevel }}
                      <!-- 这里直接使用原始的奖项名称 -->
                    </span>
                  </div>

                  <!-- 修改后的赛道信息显示 - 无论是否有赛道都显示 -->
                  <div class="flex items-center mb-3">
                    <i class="fa fa-flag mr-1.5 text-gray-500"></i>
                    <span
                      v-if="award.competitionTrack"
                      class="bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full text-sm"
                    >
                      {{ award.competitionTrack }}
                    </span>
                    <span
                      v-else
                      class="bg-gray-50 text-gray-500 px-2 py-0.5 rounded-full text-sm italic"
                    >
                      尚未区分赛道
                    </span>
                  </div>

                  <!-- 获奖人员信息 -->
                  <div class="flex items-start mb-3">
                    <i class="fa fa-users text-gray-500 mr-2 mt-0.5"></i>
                    <p class="text-gray-600 text-sm flex-1 line-clamp-2">
                      {{ award.winners.join("、") }}
                    </p>
                  </div>

                  <!-- 底部信息 -->
                  <div
                    class="flex items-center justify-between pt-3 border-t border-gray-100"
                  >
                    <div class="flex items-center text-xs text-gray-500">
                      <i class="fa fa-calendar-o mr-1"></i>
                      {{ award.awardDate }}
                    </div>
                    <div class="text-xs text-gray-400">
                      {{ award.competitionLevel }}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 校级奖项组 - 结构与国家级相同 -->
            <div
              v-if="groupedAwards[year]['校级'].length"
              class="bg-white rounded-xl shadow-md overflow-hidden"
            >
              <div class="bg-blue-700 text-white p-4">
                <h3 class="text-lg font-semibold flex items-center gap-2">
                  <i class="fa fa-university"></i> 校级奖项
                </h3>
              </div>
              <div
                class="award-grid grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 p-4"
              >
                <div
                  v-for="award in groupedAwards[year]['校级']"
                  :key="award.id"
                  class="award-card cursor-pointer bg-white rounded-xl border border-gray-100 p-5 shadow-sm hover:shadow-md transition-all duration-300 hover:translate-y-[-2px]"
                  @click="goToProjectDetail(award.id)"
                >
                  <!-- 竞赛信息头部 -->
                  <div class="flex items-start justify-between mb-4">
                    <h4
                      class="font-semibold text-dark text-lg line-clamp-2 flex-1 pr-2"
                    >
                      {{ award.competitionName }}
                    </h4>
                    <!-- 奖项级别徽章 - 直接使用原始奖项名称 -->
                    <span
                      :class="getAwardBadgeClass(award.awardLevel)"
                      class="text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap"
                    >
                      {{ award.awardLevel }}
                      <!-- 这里直接使用原始的奖项名称 -->
                    </span>
                  </div>

                  <!-- 修改后的赛道信息显示 - 无论是否有赛道都显示 -->
                  <div class="flex items-center mb-3">
                    <i class="fa fa-flag mr-1.5 text-gray-500"></i>
                    <span
                      v-if="award.competitionTrack"
                      class="bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full text-sm"
                    >
                      {{ award.competitionTrack }}
                    </span>
                    <span
                      v-else
                      class="bg-gray-50 text-gray-500 px-2 py-0.5 rounded-full text-sm italic"
                    >
                      尚未区分赛道
                    </span>
                  </div>

                  <!-- 获奖人员信息 -->
                  <div class="flex items-start mb-3">
                    <i class="fa fa-users text-gray-500 mr-2 mt-0.5"></i>
                    <p class="text-gray-600 text-sm flex-1 line-clamp-2">
                      {{ award.winners.join("、") }}
                    </p>
                  </div>

                  <!-- 底部信息 -->
                  <div
                    class="flex items-center justify-between pt-3 border-t border-gray-100"
                  >
                    <div class="flex items-center text-xs text-gray-500">
                      <i class="fa fa-calendar-o mr-1"></i>
                      {{ award.awardDate }}
                    </div>
                    <div class="text-xs text-gray-400">
                      {{ award.competitionLevel }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 空状态提示 -->
        <div
          v-if="sortedYears.length === 0"
          class="text-center py-12 bg-white rounded-xl shadow-sm"
        >
          <i class="fa fa-trophy text-gray-300 text-4xl mb-4"></i>
          <p class="text-gray-500">暂无该分类下的奖项数据</p>
        </div>

        <!-- 到底提示 -->
        <div v-else class="text-center py-8 mt-6">
          <div
            class="inline-flex items-center justify-center p-4 bg-primary/5 rounded-full mb-3"
          >
            <i class="fa fa-check-circle text-primary text-xl"></i>
          </div>
          <p class="text-gray-500 text-sm">已经浏览完所有奖项啦</p>
        </div>
      </div>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from "vue";
import CommonNavbar from "../components/CommonNavbar.vue";
import { useRouter } from "vue-router";
import { getAwards } from "../services/adminService";
import CommonFooter from "../components/CommonFooter.vue";

// 定义奖项数据结构
interface Award {
  id: number;
  competitionName: string; // 竞赛名称
  competitionLevel: string; // 竞赛级别（现在直接用中文）
  competitionTrack?: string; // 新增：赛道（可选）
  awardLevel: string; // 奖项级别
  winners: string[]; // 获奖人员数组
  year: number; // 年份
  awardDate: string; // 获奖日期
}

// 奖项级别排序权重映射 - 确保这个对象被正确使用
const awardLevelPriority: Record<string, number> = {
  一等奖: 1,
  金牌: 2,
  二等奖: 3,
  银牌: 4,
  三等奖: 5,
  铜牌: 6,
  优秀奖: 7,
};

// 新增：根据奖项级别返回对应的徽章样式（只修改样式，不改变原始文本）
const getAwardBadgeClass = (level: string): string => {
  const badgeClasses: Record<string, string> = {
    一等奖: "bg-yellow-100 text-yellow-800",
    金牌: "bg-yellow-100 text-yellow-800",
    二等奖: "bg-gray-100 text-gray-800",
    银牌: "bg-gray-100 text-gray-800",
    三等奖: "bg-orange-100 text-orange-800",
    铜牌: "bg-orange-100 text-orange-800",
    优秀奖: "bg-green-100 text-green-800",
    其他: "bg-purple-100 text-purple-800",
  };
  return badgeClasses[level] || "bg-blue-100 text-blue-800";
};

// 响应式状态管理
const router = useRouter();
const awards = ref<Award[]>([]); // 奖项数据
const loading = ref(true); // 加载状态
const error = ref<string | null>(null); // 错误信息
const searchKeyword = ref(""); // 搜索关键词
let searchTimeout: number | null = null; // 搜索防抖计时器

// 筛选条件状态
const filter = ref({
  competitionLevel: "",
  awardLevel: "",
  year: "",
  competitionName: "",
  competitionTrack: "", // 新增赛道筛选字段
}) as import("vue").Ref<{
  competitionLevel: string;
  awardLevel: string;
  year: string;
  competitionName: string;
  competitionTrack: string;
}>;

// 动态提取的竞赛名称列表
const competitionNames = computed(() => {
  const names = new Set<string>();
  awards.value.forEach((award) => {
    if (award.competitionName) {
      names.add(award.competitionName);
    }
  });
  return Array.from(names).sort();
});

// 修改 currentCompetitionTracks 计算属性，将没有赛道的项目归为"尚未区分赛道"
const currentCompetitionTracks = computed(() => {
  if (!filter.value.competitionName) return [];

  const tracks = new Set<string>();

  // 首先添加"尚未区分赛道"选项
  let hasNoTrack = false;

  awards.value.forEach((award) => {
    if (award.competitionName === filter.value.competitionName) {
      if (award.competitionTrack && award.competitionTrack !== "") {
        tracks.add(award.competitionTrack);
      } else {
        hasNoTrack = true;
      }
    }
  });

  // 如果有项目没有赛道，添加"尚未区分赛道"选项
  if (hasNoTrack) {
    tracks.add("尚未区分赛道");
  }

  return Array.from(tracks).sort();
});

// 从API获取奖项数据
const fetchAwards = async () => {
  loading.value = true;
  error.value = null;
  try {
    // 设置参数，包含keyword搜索功能
    const params = {
      keyword: searchKeyword.value,
      ...filter.value,
    };

    // 使用getAwards函数
    const data = await getAwards(params);

    // 确保数据格式正确
    if (Array.isArray(data)) {
      // 直接使用返回的奖项数组
      awards.value = data.map((item: any) => ({
        ...item,
        year: parseInt(item.year), // 将 year 从字符串转换为数字
        winners: item.winners as Array<string>,
      })) as Award[];
    } else {
      // 如果响应不是预期格式，使用空数组
      awards.value = [];
      console.warn("API响应格式不符合预期，使用空数组");
    }
  } catch (err) {
    error.value = "获取奖项数据失败，请检查API服务是否运行";
    console.error("Failed to fetch awards:", err);
    // 发生错误时，确保awards.value是数组
    awards.value = [];
  } finally {
    loading.value = false;
  }
};

// 处理搜索输入
const handleSearch = () => {
  // 防抖处理，避免频繁请求
  if (searchTimeout) {
    clearTimeout(searchTimeout);
  }
  searchTimeout = setTimeout(() => {
    fetchAwards();
  }, 300) as unknown as number; // 类型转换处理
};

// 重置筛选条件
const resetFilters = () => {
  filter.value = {
    competitionLevel: "",
    awardLevel: "",
    year: "",
    competitionName: "",
    competitionTrack: "", // 重置赛道筛选
  };
  searchKeyword.value = "";
  fetchAwards(); // 重新获取数据
};

// 分类筛选逻辑
const filteredAwards = computed(() => {
  return awards.value.filter((award) => {
    // 搜索关键词筛选
    if (
      searchKeyword.value &&
      !(
        award.competitionName
          .toLowerCase()
          .includes(searchKeyword.value.toLowerCase()) ||
        award.winners.some((winner) =>
          winner.toLowerCase().includes(searchKeyword.value.toLowerCase())
        ) ||
        award.awardLevel
          .toLowerCase()
          .includes(searchKeyword.value.toLowerCase())
      )
    ) {
      return false;
    }

    // 竞赛级别筛选
    if (
      filter.value.competitionLevel &&
      award.competitionLevel !== filter.value.competitionLevel
    ) {
      return false;
    }

    // 奖项级别筛选
    if (filter.value.awardLevel) {
      if (filter.value.awardLevel === "其他") {
        // 其他奖项：不是一等、二等、三等、优秀奖的归为其他
        if (
          ["一等奖", "二等奖", "三等奖", "优秀奖"].includes(award.awardLevel)
        ) {
          return false;
        }
      } else if (filter.value.awardLevel === "一等奖") {
        // 点击一等奖时同时加载金牌和一等奖
        if (award.awardLevel !== "一等奖" && award.awardLevel !== "金牌") {
          return false;
        }
      } else if (filter.value.awardLevel === "二等奖") {
        // 点击二等奖时同时加载二等奖和银牌
        if (award.awardLevel !== "二等奖" && award.awardLevel !== "银牌") {
          return false;
        }
      } else if (filter.value.awardLevel === "三等奖") {
        // 点击三等奖时同时加载三等奖和铜牌
        if (award.awardLevel !== "三等奖" && award.awardLevel !== "铜牌") {
          return false;
        }
      } else if (award.awardLevel !== filter.value.awardLevel) {
        return false;
      }
    }

    // 年份筛选
    if (filter.value.year && award.year !== parseInt(filter.value.year)) {
      return false;
    }

    // 竞赛名称筛选
    if (
      filter.value.competitionName &&
      award.competitionName !== filter.value.competitionName
    ) {
      return false;
    }

    // 修改赛道筛选逻辑，支持"尚未区分赛道"选项
    if (filter.value.competitionTrack) {
      if (filter.value.competitionTrack === "尚未区分赛道") {
        // 如果选择了"尚未区分赛道"，则只显示没有赛道的项目
        if (award.competitionTrack && award.competitionTrack !== "") {
          return false;
        }
      } else {
        // 否则只显示有对应赛道的项目
        if (award.competitionTrack !== filter.value.competitionTrack) {
          return false;
        }
      }
    }

    return true;
  });
});

// 数据分组：按年份→级别分组
const groupedAwards = computed(() => {
  // 先对筛选后的数据按照奖项级别排序
  const sortedAwards = [...filteredAwards.value].sort((a, b) => {
    // 获取两个奖项的优先级，如果不存在则默认为99（表示其他奖项）
    const priorityA = awardLevelPriority[a.awardLevel] || 99;
    const priorityB = awardLevelPriority[b.awardLevel] || 99;

    // 先按奖项级别优先级排序
    if (priorityA !== priorityB) {
      return priorityA - priorityB;
    }

    // 如果奖项级别相同，再按竞赛名称排序
    return a.competitionName.localeCompare(b.competitionName);
  });

  // 然后再按年份和竞赛级别分组 - 注意这里没有对award.awardLevel进行转换
  return sortedAwards.reduce((acc, award) => {
    const yearKey = award.year.toString();
    if (!acc[yearKey]) {
      acc[yearKey] = {
        国家级: [] as Award[],
        省级: [] as Award[],
        校级: [] as Award[],
      };
    }
    // 直接使用中文级别作为键
    const level = ["国家级", "省级", "校级"].includes(award.competitionLevel)
      ? award.competitionLevel
      : "校级"; // 默认使用校级
    // 确保 level 是有效的键类型，避免隐式 any 类型错误
    if (level === "国家级" || level === "省级" || level === "校级") {
      acc[yearKey][level].push(award); // 这里直接push原始的award对象，不修改其属性
    }
    return acc;
  }, {} as Record<string, { 国家级: Award[]; 省级: Award[]; 校级: Award[] }>);
});

// 年份排序：按降序排列
const sortedYears = computed(() => {
  return Object.keys(groupedAwards.value).sort(
    (a, b) => parseInt(b) - parseInt(a)
  );
});

// 跳转到项目详情页
const goToProjectDetail = (awardId: number) => {
  router.push({ path: "/projectdetailtest", query: { awardId } });
};

// 页面加载时获取数据
onMounted(() => {
  fetchAwards();

  nextTick(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("animate-slide-up");
            observer.unobserve(entry.target);
          }
        });
      },
      { root: null, rootMargin: "0px", threshold: 0.1 }
    );

    document
      .querySelectorAll("section")
      .forEach((section) => observer.observe(section));
  });
});

// 新增：监听竞赛项目变化，清空赛道筛选
watch(
  () => filter.value.competitionName,
  () => {
    filter.value.competitionTrack = "";
  }
);
</script>

<style scoped>
/* 样式部分保持不变 */
.awards-page {
  background-color: #f8fafc;
}

/* 增强筛选按钮的交互效果 */
button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

button:active {
  transform: translateY(0);
}

/* 优化筛选组的布局 */
.filter-item {
  transition: all 0.3s ease;
}

.filter-item:hover {
  transform: translateY(-2px);
}

/* 增强动画效果 */
.animate-scale-in {
  animation: scaleIn 0.5s ease-out;
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 为选中状态添加更明确的标识 */
.bg-primary {
  background-color: #3b82f6;
}

/* 优化奖项卡片样式 */
.award-card {
  transition: all 0.3s ease;
  border-radius: 0.75rem;
  border: 1px solid #e2e8f0;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.award-card:hover {
  background-color: #ffffff;
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1),
    0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

/* 优化卡片网格布局 */
.award-grid {
  display: grid;
  gap: 1rem;
}

/* 确保在不同屏幕尺寸下的良好显示 */
@media (max-width: 640px) {
  .award-grid {
    grid-template-columns: 1fr;
  }
}

@media (min-width: 641px) and (max-width: 1023px) {
  .award-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1024px) {
  .award-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  /* 校级奖项可以显示更多列 */
  .award-grid.grid-cols-3 {
    grid-template-columns: repeat(3, 1fr);
  }
}

/* 优化文本截断和行高 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

/* 奖项统计卡片优化 */
.text-red-500 {
  color: #ef4444;
}
</style>

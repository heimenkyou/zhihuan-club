<template>
  <div class="about-page min-h-screen bg-gray-50 text-dark">
    <CommonNavbar />
    <main class="mx-auto mt-14 max-w-6xl px-4 py-6 sm:px-6 lg:px-8">
      <section class="mb-12 animate-fade-in">
        <div class="mb-8 text-center">
          <h1 class="mb-3 text-[clamp(1.5rem,3vw,2.5rem)] font-bold">关于我们</h1>
          <p class="mx-auto max-w-2xl text-gray-600">我们因兴趣聚在一起，也想把一些想法真正做出来。</p>
        </div>
        <div class="overflow-hidden rounded-xl bg-white shadow-lg md:grid md:grid-cols-2">
          <div class="min-h-64 md:min-h-full">
            <img src="@/assets/images/group-photo.webp" alt="社团活动照片" class="h-full w-full object-cover" loading="lazy" />
          </div>
          <div class="flex flex-col justify-center p-6 sm:p-8 md:p-10">
            <h2 class="mb-4 text-2xl font-bold">一群愿意动手的人</h2>
            <p class="mb-3 leading-relaxed text-gray-700">智环学创融合协会成立于 2024 年。有人写网站，有人刷题，有人做硬件，也有人琢磨怎么把一个项目讲明白。大家喜欢的东西不一样，但都愿意动手试试。</p>
            <p class="mb-3 leading-relaxed text-gray-700">我们做过软件项目，参加过算法和创新创业比赛，也折腾过一些最后没能完成的想法。比起给每个人安排一条固定路线，我们更喜欢从一个小东西开始，边做边学，慢慢找到自己真正感兴趣的方向。</p>
            <p class="leading-relaxed text-gray-700">在这里，你可以认识一些同样喜欢技术和创作的人，一起讨论问题、分享经验，也可能找到下一个愿意和你一起做项目的伙伴。</p>
          </div>
        </div>
      </section>

      <section class="mb-16">
        <div class="mb-10 text-center">
          <h2 class="mb-4 text-2xl font-bold">这两年，我们做了什么</h2>
          <div class="mx-auto h-1 w-20 bg-primary"></div>
        </div>
        <div class="mx-auto max-w-4xl space-y-6 border-l-2 border-primary/20 pl-6 sm:pl-10">
          <article v-for="item in timeline" :key="item.date" class="relative animate-slide-up">
            <span class="absolute -left-[2.05rem] top-1 h-4 w-4 rounded-full border-4 border-gray-50 bg-primary sm:-left-[2.95rem]"></span>
            <div class="flex flex-col gap-1 sm:flex-row sm:items-baseline sm:gap-4">
              <time class="shrink-0 font-bold text-primary">{{ item.date }}</time>
              <h3 class="text-lg font-bold">{{ item.title }}</h3>
            </div>
            <p class="mt-2 leading-relaxed text-gray-600">{{ item.description }}</p>
          </article>
        </div>
      </section>

      <section class="mb-16">
        <div class="mb-10 text-center">
          <h2 class="mb-4 text-2xl font-bold">社团架构</h2>
          <div class="mx-auto h-1 w-20 bg-primary"></div>
        </div>
        <div class="mb-12 grid grid-cols-1 gap-6 md:grid-cols-2">
          <article v-for="member in leaders" :key="member.role" class="rounded-xl bg-white p-8 text-center shadow-lg">
            <div class="mx-auto mb-4 flex h-20 w-20 items-center justify-center rounded-full bg-primary/10 text-primary"><i-fa6-solid-id-card /></div>
            <h3 class="mb-3 text-xl font-bold">{{ member.role }}</h3>
            <p class="mb-4 text-sm text-gray-600">{{ member.description }}</p>
            <p class="text-sm text-gray-500">{{ member.role }}：{{ member.name }}</p>
          </article>
        </div>
        <div class="grid grid-cols-1 gap-6 md:grid-cols-3">
          <article v-for="department in departments" :key="department.name" class="rounded-xl bg-white p-6 text-center shadow-lg">
            <el-image :src="department.image" :alt="`${department.name}图标`" class="mx-auto mb-4 h-24 w-24" :preview-src-list="[department.image]" loading="lazy" />
            <h3 class="mb-3 text-xl font-bold">{{ department.name }}</h3>
            <p class="mb-4 text-sm leading-relaxed text-gray-600">{{ department.description }}</p>
            <p class="text-sm text-gray-500">负责人：{{ department.leader }}</p>
          </article>
        </div>
      </section>

      <section class="mb-8">
        <div class="mb-10 text-center">
          <h2 class="mb-4 text-2xl font-bold">联系我们</h2>
          <div class="mx-auto h-1 w-20 bg-primary"></div>
        </div>
        <div class="rounded-xl bg-white p-8 text-center shadow-lg"><p class="text-gray-600">点击底部页脚的图标就行辣！😎👇</p></div>
      </section>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup>
import algorithmImg from "@/assets/images/department-algorithm.webp";
import generalAdminImg from "@/assets/images/department-general-admin.webp";
import projectCompetitionImg from "@/assets/images/department-project-competition.webp";
import CommonFooter from "@/components/CommonFooter.vue";
import CommonNavbar from "@/components/CommonNavbar.vue";

const timeline = [
	{
		date: "2024.08",
		title: "社团成立",
		description:
			"最开始，我们只是想找一些同样对技术和竞赛感兴趣的人。智环学创融合协会正式成立，确立了以技术实践和创新为核心的社团方向。",
	},
	{
		date: "2024.09",
		title: "首次招新",
		description:
			"第一次招新来了很多新面孔，大家一起刷题、聊项目、尝试参加比赛。后来真正坚持下来的人不算多，但社团也因此有了最早的一批伙伴。",
	},
	{
		date: "2025",
		title: "成果落地",
		description:
			"社团成员在各自专注的领域陆续取得突破。在挑战杯、机器人大赛及蓝桥杯等赛事中，斩获多项省级与国家级成绩。",
	},
	{
		date: "2026.08",
		title: "平台重构",
		description:
			"随着社团的发展与成果积累，我们对社团官网进行了全面的底层重构与视觉升级，搭建起属于社团自己的技术与成果展示平台。",
	},
];

const leaders = [
	{
		role: "社长",
		name: "王熠博",
		description: "负责社团整体规划和对外联络，统筹各部门工作。",
	},
	{
		role: "副社长",
		name: "张泽天",
		description: "协助社长工作，负责内部管理和活动组织。",
	},
];

const departments = [
	{
		name: "算法部",
		image: algorithmImg,
		leader: "边韶涵",
		description:
			"主要面向蓝桥杯、天梯赛、CCPC省赛及码蹄杯等算法赛事。不谈虚的，就是踏踏实实从基础数据结构学起，以赛促学，积累代码实战经验。",
	},
	{
		name: "项目竞赛部",
		image: projectCompetitionImg,
		leader: "赵楚一洋",
		description:
			"负责社团各类软件项目的开发与落地。无论你对前端还是后端感兴趣，这里的目标都是把书本上的理论，变成能在设备上跑起来的真实应用。",
	},
	{
		name: "综合管理部",
		image: generalAdminImg,
		leader: "杨铠硕",
		description:
			"社团运转的大管家。负责日常运营、活动组织、纳新统筹以及部门间的协调沟通，保障整个团队有条不紊地前行。",
	},
];
</script>

<style scoped>
.about-page { background-color: #f8fafc; color: #1e293b; }
.text-dark { color: #1e293b; }
.bg-primary { background-color: #3b82f6; }
.text-primary { color: #3b82f6; }
.animate-fade-in { animation: fade-in 0.5s ease-out; }
.animate-slide-up { animation: slide-up 0.5s ease-out both; }
@keyframes fade-in { from { opacity: 0; } to { opacity: 1; } }
@keyframes slide-up { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
@media (max-width: 768px) { main { margin-top: 3rem; } }
</style>

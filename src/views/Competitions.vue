<template>
  <div class="competitions-page min-h-screen font-sans text-dark bg-gray-50">
    <CommonNavbar />

    <!-- 核心布局：按要求设置 max-w-7xl，解决标题遮挡 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 mt-16">
      <div class="max-w-6xl mx-auto">
        <!-- 内层保持原设计的 max-w-6xl，确保内容对齐 -->
        <!-- 1. Hero区域（完整保留原HTML内容） -->
        <section class="mb-16 animate-fade-in">
          <div
            class="bg-gradient-to-r from-blue-600 to-indigo-700 rounded-2xl shadow-xl overflow-hidden"
          >
            <div class="md:flex items-center">
              <div class="md:w-1/2 p-8 md:p-12">
                <h1
                  class="text-[clamp(1.8rem,4vw,3rem)] font-bold text-white mb-6"
                >
                  竞赛天地
                </h1>
                <p class="text-white/90 text-lg mb-8 max-w-xl">
                  展示各类科技创新竞赛，提供竞赛信息、备赛经验和获奖成果，助力社团成员在竞赛中取得优异成绩。
                </p>
                <div class="flex flex-wrap gap-4">
                  <button
                    class="px-6 py-3 bg-white text-blue-600 font-bold rounded-lg shadow-md hover:shadow-lg transition-shadow transform hover:-translate-y-1"
                  >
                    备赛指南
                  </button>
                  <button
                    class="px-6 py-3 bg-transparent border-2 border-white text-white font-bold rounded-lg hover:bg-white/10 transition-colors transform hover:-translate-y-1"
                  >
                    历年成果
                  </button>
                </div>
              </div>
              <div class="md:w-1/2 hidden md:block">
                <img
                  src="https://images.unsplash.com/photo-1625772452859-1c03d5bf1137?q=80&w=1740&auto=format&fit=crop"
                  alt="竞赛宣传图"
                  class="w-full h-full object-cover"
                  loading="lazy"
                />
              </div>
            </div>
          </div>
        </section>

        <!-- 2. 热门竞赛（完整保留6个竞赛卡片，无遗漏） -->
        <section class="mb-16">
          <div class="text-center mb-10">
            <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
              热门竞赛
            </h2>
            <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <!-- 竞赛1：挑战杯 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=1740&auto=format&fit=crop"
                  alt="挑战杯"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-primary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  国家级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  挑战杯全国大学生课外学术科技作品竞赛
                </h3>
                <p class="text-gray-600 mb-4">
                  全国性的大学生课外学术实践竞赛，被誉为中国大学生学术科技的"奥林匹克"盛会。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>每年11月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>28次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToChallengeCup"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>

            <!-- 竞赛2：互联网+ -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
              style="animation-delay: 0.1s"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?q=80&w=1740&auto=format&fit=crop"
                  alt="互联网+"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-primary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  国家级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  中国"互联网+"大学生创新创业大赛
                </h3>
                <p class="text-gray-600 mb-4">
                  面向全国高校大学生的创新创业盛会，旨在激发学生的创造力，培养"大众创业、万众创新"的生力军。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>每年9月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>22次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToInternetPlus"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>

            <!-- 竞赛3：ACM -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
              style="animation-delay: 0.2s"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1581091226033-d5c41243363c?q=80&w=1740&auto=format&fit=crop"
                  alt="ACM-ICPC"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-primary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  国际级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  ACM国际大学生程序设计竞赛
                </h3>
                <p class="text-gray-600 mb-4">
                  由国际计算机协会主办的一项旨在展示大学生创新能力、团队精神和在压力下编写程序、分析和解决问题能力的年度竞赛。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>每年10月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>15次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToACM"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>

            <!-- 竞赛4：电子设计竞赛 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
              style="animation-delay: 0.3s"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1620712943543-bcc4688e7485?q=80&w=1740&auto=format&fit=crop"
                  alt="全国电子设计竞赛"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-primary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  国家级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  全国大学生电子设计竞赛
                </h3>
                <p class="text-gray-600 mb-4">
                  面向全国高等院校电子信息类专业学生的学科竞赛，旨在培养大学生的创新能力、协作精神和工程实践能力。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>偶数年8月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>18次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToElectronicDesign"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>

            <!-- 竞赛5：智能汽车竞赛 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
              style="animation-delay: 0.4s"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1563986768609-322da115f123?q=80&w=1740&auto=format&fit=crop"
                  alt="智能汽车竞赛"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-secondary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  省级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  全国大学生智能汽车竞赛
                </h3>
                <p class="text-gray-600 mb-4">
                  面向全国大学生的智能汽车科技创新实践类竞赛，旨在培养大学生的综合知识运用能力、基本工程实践能力和创新意识。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>每年7月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>12次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToSmartCar"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>

            <!-- 竞赛6：数学建模竞赛 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-scale-in"
              style="animation-delay: 0.5s"
            >
              <div class="relative">
                <img
                  src="https://images.unsplash.com/photo-1517694712202-14dd9538aa97?q=80&w=1740&auto=format&fit=crop"
                  alt="数学建模竞赛"
                  class="w-full h-48 object-cover"
                  loading="lazy"
                />
                <div
                  class="absolute top-4 left-4 bg-secondary text-white px-3 py-1 rounded-full text-sm font-medium"
                >
                  省级
                </div>
              </div>
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  全国大学生数学建模竞赛
                </h3>
                <p class="text-gray-600 mb-4">
                  面向全国大学生的数学建模竞赛，旨在提高学生建立数学模型和运用计算机技术解决实际问题的综合能力。
                </p>
                <div class="flex items-center justify-between mb-4">
                  <div class="flex items-center text-gray-500">
                    <i class="fa fa-calendar mr-2"></i>
                    <span>每年9月</span>
                  </div>
                  <!-- <div class="flex items-center text-gray-500">
                    <i class="fa fa-trophy mr-2"></i>
                    <span>15次获奖</span>
                  </div> -->
                </div>
                <button
                  @click="goToMathModeling"
                  class="w-full py-2 border border-primary text-primary font-semibold rounded-lg hover:bg-primary hover:text-white transition-colors"
                >
                  查看详情
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- 3. 竞赛日历（完整保留3个月份所有日程项） -->
        <section class="mb-16">
          <div class="text-center mb-10">
            <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
              竞赛日历
            </h2>
            <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
          </div>

          <div class="bg-white rounded-xl shadow-lg p-6 animate-slide-up">
            <div class="flex justify-between items-center mb-6">
              <button
                class="p-2 rounded-full hover:bg-gray-100 transition-colors"
              >
                <i class="fa fa-chevron-left text-gray-600"></i>
              </button>
              <h3 class="text-xl font-bold text-dark">2023年竞赛日程</h3>
              <button
                class="p-2 rounded-full hover:bg-gray-100 transition-colors"
              >
                <i class="fa fa-chevron-right text-gray-600"></i>
              </button>
            </div>

            <div class="space-y-4">
              <!-- 9月日程 -->
              <div>
                <h4 class="text-lg font-semibold text-dark mb-3">9月</h4>
                <div class="space-y-3">
                  <div class="flex border-l-4 border-primary pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">9月15日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">
                        中国"互联网+"大学生创新创业大赛
                      </div>
                      <div class="text-sm text-gray-500">报名截止日期</div>
                    </div>
                  </div>
                  <div class="flex border-l-4 border-primary pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">9月20日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">
                        全国大学生数学建模竞赛
                      </div>
                      <div class="text-sm text-gray-500">比赛日期</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 10月日程 -->
              <div>
                <h4 class="text-lg font-semibold text-dark mb-3">10月</h4>
                <div class="space-y-3">
                  <div class="flex border-l-4 border-accent pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">10月10日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">
                        ACM国际大学生程序设计竞赛
                      </div>
                      <div class="text-sm text-gray-500">区域赛</div>
                    </div>
                  </div>
                  <div class="flex border-l-4 border-secondary pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">10月25日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">校级电子设计竞赛</div>
                      <div class="text-sm text-gray-500">初赛</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 11月日程 -->
              <div>
                <h4 class="text-lg font-semibold text-dark mb-3">11月</h4>
                <div class="space-y-3">
                  <div class="flex border-l-4 border-primary pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">11月5日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">
                        挑战杯全国大学生课外学术科技作品竞赛
                      </div>
                      <div class="text-sm text-gray-500">省级复赛</div>
                    </div>
                  </div>
                  <div class="flex border-l-4 border-accent pl-4 py-1">
                    <div class="w-24 font-medium text-gray-700">11月20日</div>
                    <div class="flex-1">
                      <div class="font-medium text-dark">
                        全国大学生智能汽车竞赛
                      </div>
                      <div class="text-sm text-gray-500">总决赛</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 4. 备赛指南（完整保留4个指南卡片） -->
        <section class="mb-16">
          <div class="text-center mb-10">
            <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
              备赛指南
            </h2>
            <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <!-- 指南1：挑战杯备赛指南 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
            >
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">挑战杯备赛指南</h3>
                <div class="flex items-center text-gray-500 mb-4">
                  <i class="fa fa-user mr-2"></i>
                  <span>李教授 · 竞赛指导教师</span>
                </div>
                <p class="text-gray-600 mb-4">
                  挑战杯是一项综合性的学术科技竞赛，需要团队具备扎实的专业知识和创新能力。本指南从选题、团队组建、资料收集、研究方法、论文撰写等方面提供详细指导...
                </p>
                <div class="flex items-center justify-between">
                  <div class="text-sm text-gray-500">
                    <i class="fa fa-eye mr-1"></i> 2.5k 阅读
                  </div>
                  <button class="text-primary font-medium flex items-center">
                    阅读全文 <i class="fa fa-angle-right ml-1"></i>
                  </button>
                </div>
              </div>
            </div>

            <!-- 指南2：互联网+创业大赛攻略 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
              style="animation-delay: 0.1s"
            >
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  互联网+创业大赛攻略
                </h3>
                <div class="flex items-center text-gray-500 mb-4">
                  <i class="fa fa-user mr-2"></i>
                  <span>张学长 · 往届获奖者</span>
                </div>
                <p class="text-gray-600 mb-4">
                  互联网+创业大赛注重项目的创新性、可行性和商业价值。本攻略分享了如何打造一个有竞争力的创业项目，包括商业模式设计、产品原型开发、市场分析、财务规划等...
                </p>
                <div class="flex items-center justify-between">
                  <div class="text-sm text-gray-500">
                    <i class="fa fa-eye mr-1"></i> 3.2k 阅读
                  </div>
                  <button class="text-primary font-medium flex items-center">
                    阅读全文 <i class="fa fa-angle-right ml-1"></i>
                  </button>
                </div>
              </div>
            </div>

            <!-- 指南3：ACM程序设计竞赛训练方法 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
              style="animation-delay: 0.2s"
            >
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  ACM程序设计竞赛训练方法
                </h3>
                <div class="flex items-center text-gray-500 mb-4">
                  <i class="fa fa-user mr-2"></i>
                  <span>王学姐 · 算法研究部部长</span>
                </div>
                <p class="text-gray-600 mb-4">
                  ACM程序设计竞赛要求参赛者具备扎实的算法基础和快速编程能力。本指南分享了如何系统地学习算法知识，提高编程技能，包括基础算法学习、刷题技巧、团队协作等...
                </p>
                <div class="flex items-center justify-between">
                  <div class="text-sm text-gray-500">
                    <i class="fa fa-eye mr-1"></i> 2.8k 阅读
                  </div>
                  <button class="text-primary font-medium flex items-center">
                    阅读全文 <i class="fa fa-angle-right ml-1"></i>
                  </button>
                </div>
              </div>
            </div>

            <!-- 指南4：电子设计竞赛经验分享 -->
            <div
              class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
              style="animation-delay: 0.3s"
            >
              <div class="p-6">
                <h3 class="text-xl font-bold text-dark mb-3">
                  电子设计竞赛经验分享
                </h3>
                <div class="flex items-center text-gray-500 mb-4">
                  <i class="fa fa-user mr-2"></i>
                  <span>陈学长 · 硬件开发部部长</span>
                </div>
                <p class="text-gray-600 mb-4">
                  电子设计竞赛注重实践能力和创新思维。本经验分享从电路设计、嵌入式系统开发、调试技巧等方面提供指导，帮助参赛者更好地准备竞赛，提高获奖概率...
                </p>
                <div class="flex items-center justify-between">
                  <div class="text-sm text-gray-500">
                    <i class="fa fa-eye mr-1"></i> 2.3k 阅读
                  </div>
                  <button class="text-primary font-medium flex items-center">
                    阅读全文 <i class="fa fa-angle-right ml-1"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 5. 获奖分享（完整保留所有内容，包括团队图片和文本） -->
        <section class="mb-16">
          <div class="text-center mb-10">
            <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
              获奖分享
            </h2>
            <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
          </div>

          <div
            class="bg-white rounded-xl shadow-lg overflow-hidden animate-slide-up"
          >
            <div class="md:flex">
              <div class="md:w-1/3">
                <img
                  src="https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1740&auto=format&fit=crop"
                  alt="获奖团队"
                  class="w-full h-full object-cover"
                  loading="lazy"
                />
              </div>
              <div class="md:w-2/3 p-8">
                <div
                  class="bg-blue-100 text-blue-800 inline-block px-3 py-1 rounded-full text-sm font-medium mb-4"
                >
                  挑战杯全国一等奖团队分享
                </div>
                <h3 class="text-2xl font-bold text-dark mb-4">
                  从创意到金奖：我们的挑战杯之路
                </h3>
                <div class="flex items-center text-gray-500 mb-6">
                  <img
                    src="https://randomuser.me/api/portraits/men/32.jpg"
                    alt="团队代表"
                    class="w-10 h-10 rounded-full mr-3"
                    loading="lazy"
                  />
                  <div>
                    <div class="font-medium text-dark">陈明 · 团队负责人</div>
                    <div class="text-sm">2023年12月15日</div>
                  </div>
                </div>
                <p class="text-gray-600 mb-6">
                  去年的挑战杯竞赛对我们团队来说是一次难忘的经历。从最初的创意构思，到后来的项目实践，再到最终的竞赛答辩，我们经历了无数次的失败和挫折，但也收获了成长和友谊。在这里，我想分享一下我们的备赛经验和心得体会...
                </p>
                <button
                  class="px-6 py-3 bg-primary text-white font-bold rounded-lg shadow-md hover:bg-primary/90 transition-colors transform hover:-translate-y-1"
                >
                  查看完整分享
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- 6. 竞赛咨询（完整保留表单和联系信息，无任何字段丢失） -->
        <section>
          <div class="text-center mb-10">
            <h2 class="text-2xl font-bold text-dark mb-4 animate-slide-up">
              竞赛咨询
            </h2>
            <div class="w-20 h-1 bg-primary mx-auto animate-slide-up"></div>
          </div>

          <div class="bg-white rounded-xl shadow-lg p-8 animate-slide-up">
            <div class="md:flex items-center justify-between">
              <div class="md:w-1/2 mb-6 md:mb-0">
                <h3 class="text-xl font-bold text-dark mb-4">
                  有任何竞赛相关问题？
                </h3>
                <p class="text-gray-600 mb-6">
                  如果你对某个竞赛感兴趣，或者在备赛过程中遇到了问题，欢迎随时联系我们的竞赛指导团队，我们将竭诚为你提供帮助。
                </p>
                <div class="space-y-4">
                  <!-- 邮箱联系 -->
                  <div class="flex items-center">
                    <div
                      class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center mr-4"
                    >
                      <i class="fa fa-envelope text-primary"></i>
                    </div>
                    <div>
                      <div class="font-medium text-dark">竞赛邮箱</div>
                      <div class="text-gray-600">competition@tech-club.com</div>
                    </div>
                  </div>
                  <!-- 微信联系 -->
                  <div class="flex items-center">
                    <div
                      class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center mr-4"
                    >
                      <i class="fa fa-weixin text-primary"></i>
                    </div>
                    <div>
                      <div class="font-medium text-dark">微信咨询</div>
                      <div class="text-gray-600">TechClub_Competition</div>
                    </div>
                  </div>
                  <!-- 咨询时间 -->
                  <div class="flex items-center">
                    <div
                      class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center mr-4"
                    >
                      <i class="fa fa-calendar text-primary"></i>
                    </div>
                    <div>
                      <div class="font-medium text-dark">咨询时间</div>
                      <div class="text-gray-600">每周三、周五 18:00-20:00</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 快速咨询表单（完整保留所有字段和样式） -->
              <div class="md:w-1/2 md:pl-8">
                <div class="bg-blue-50 rounded-xl p-6">
                  <h4 class="text-lg font-semibold text-dark mb-4">快速咨询</h4>
                  <form>
                    <!-- 姓名输入 -->
                    <div class="mb-4">
                      <label
                        for="name"
                        class="block text-gray-700 font-medium mb-2"
                        >姓名</label
                      >
                      <input
                        type="text"
                        id="name"
                        name="name"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                        placeholder="请输入您的姓名"
                      />
                    </div>
                    <!-- 邮箱输入 -->
                    <div class="mb-4">
                      <label
                        for="email"
                        class="block text-gray-700 font-medium mb-2"
                        >邮箱</label
                      >
                      <input
                        type="email"
                        id="email"
                        name="email"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                        placeholder="请输入您的邮箱"
                      />
                    </div>
                    <!-- 竞赛选择 -->
                    <div class="mb-4">
                      <label
                        for="competition"
                        class="block text-gray-700 font-medium mb-2"
                        >咨询竞赛</label
                      >
                      <select
                        id="competition"
                        name="competition"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                      >
                        <option value="">请选择咨询的竞赛</option>
                        <option value="challenge-cup">挑战杯</option>
                        <option value="internet-plus">互联网+</option>
                        <option value="acm">ACM程序设计竞赛</option>
                        <option value="electronic">电子设计竞赛</option>
                        <option value="other">其他竞赛</option>
                      </select>
                    </div>
                    <!-- 问题描述 -->
                    <div class="mb-4">
                      <label
                        for="question"
                        class="block text-gray-700 font-medium mb-2"
                        >问题描述</label
                      >
                      <textarea
                        id="question"
                        name="question"
                        rows="3"
                        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                        placeholder="请简要描述您的问题"
                      ></textarea>
                    </div>
                    <!-- 提交按钮 -->
                    <button
                      type="submit"
                      class="w-full py-3 bg-primary text-white font-bold rounded-lg shadow-md hover:bg-primary/90 transition-colors"
                    >
                      提交咨询
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </main>
    <CommonFooter />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue"
// import { useRouter } from "vue-router"
import CommonNavbar from "../components/CommonNavbar.vue"
import CommonFooter from "../components/CommonFooter.vue"

// const router = useRouter()
// 跳转到相关赛事的官网
const goToChallengeCup = () => {
  window.open("http://www.tiaozhanbei.net/", "_blank")
}
const goToInternetPlus = () => {
  window.open("https://cy.ncss.cn/", "_blank")
}
const goToACM = () => {
  window.open("https://acm.hust.edu.cn/", "_blank")
}
const goToElectronicDesign = () => {
  window.open("https://www.edc.hust.edu.cn/", "_blank")
}
const goToSmartCar = () => {
  window.open("http://www.smartcarrace.com/", "_blank")
}
const goToMathModeling = () => {
  window.open("https://www.mcm.edu.cn/", "_blank")
}

// 完整保留原HTML的滚动动画逻辑（迁移自DOMContentLoaded）
onMounted(() => {
  const observerOptions = {
    root: null,
    rootMargin: "0px",
    threshold: 0.1,
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("animate-slide-up")
        observer.unobserve(entry.target)
      }
    })
  }, observerOptions)

  // 监听所有section，确保动画触发逻辑与原HTML一致
  document.querySelectorAll("section").forEach((section) => {
    observer.observe(section)
  })
})
</script>

<style scoped>
/* 1. 基础样式：与原HTML保持一致 */
.competitions-page {
  background-color: #f8fafc;
}

/* 2. 自定义颜色：完整迁移原tailwind.config的颜色定义 */
.bg-primary {
  background-color: #3b82f6;
}
.text-primary {
  color: #3b82f6;
}
.bg-secondary {
  background-color: #10b981;
}
.text-secondary {
  color: #10b981;
}
.bg-accent {
  background-color: #f59e0b;
}
.text-accent {
  color: #f59e0b;
}
.text-dark {
  color: #1e293b;
}

/* 3. 动画样式：完整迁移@layer utilities中的所有动画（避免scoped失效） */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
@keyframes pulseSlow {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

.animate-fade-in {
  animation: fadeIn 0.8s ease-in-out;
}
.animate-slide-up {
  animation: slideUp 0.6s ease-out;
}
.animate-scale-in {
  animation: scaleIn 0.5s ease-out;
}
.animate-pulse-slow {
  animation: pulseSlow 2s infinite;
}

/* 4. 移动端适配：保留原HTML的响应式逻辑，优化触摸体验 */
@media (max-width: 768px) {
  /* 确保文字不溢出，强制换行 */
  span,
  p,
  h3,
  h4 {
    word-wrap: break-word !important;
    white-space: normal !important;
  }

  /* 优化卡片间距，避免拥挤 */
  .grid {
    gap: 1rem !important;
  }

  /* 按钮触摸区域优化（符合移动端交互规范） */
  button {
    min-height: 44px !important;
    min-width: 44px !important;
  }

  /* 调整图片高度，避免移动端遮挡 */
  .h-48 {
    height: 280px !important;
  }
}

/* 5. Font Awesome图标：确保图标正常显示（迁移原HTML的图标样式） */
.fa {
  display: inline-block;
  font-style: normal;
  font-variant: normal;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
}

/* 6. 补充原HTML的content-auto类（用于性能优化） */
.content-auto {
  content-visibility: auto;
}
</style>

<template>
  <div class="code-pulse-container">
    <!-- 导航栏 -->
    <el-header class="navbar">
      <div class="container">
        <div class="logo-container">
          <el-link
            :underline="false"
            href="#home"
            class="logo-link"
            @click.prevent="scrollTo('#home')"
          >
            <div class="logo-icon">
              <span
                ><img src="/apple-touch-icon.png"
              /></span>
            </div>
            <span class="logo-text">智</span>
          </el-link>
        </div>

        <!-- 桌面导航 -->
        <div class="desktop-nav">
          <el-link
            v-for="item in navItems"
            :key="item.id"
            :href="item.href"
            class="nav-link"
            :underline="false"
            @click.prevent="scrollTo(item.href)"
          >
            {{ item.text }}
          </el-link>
          <el-button
            type="primary"
            class="join-btn"
            round
            @click="goToMessageWall"
            >留言墙</el-button
          >
          <el-button
            type="primary"
            class="join-btn"
            round
            @click="scrollTo('#join')"
            >加入我们</el-button
          >
        </div>

        <!-- 移动端菜单按钮 -->
        <el-button
          class="mobile-menu-btn"
          @click="showMobileMenu = !showMobileMenu"
        >
          <el-icon>
            <Menu />
          </el-icon>
        </el-button>
      </div>

      <!-- 移动端导航菜单 -->
      <el-collapse-transition>
        <div v-show="showMobileMenu" class="mobile-menu">
          <el-link
            v-for="item in navItems"
            :key="item.id"
            :href="item.href"
            class="mobile-nav-link"
            :underline="false"
            @click.prevent="scrollTo(item.href)"
          >
            {{ item.text }}
          </el-link>
          <el-button
            type="primary"
            class="mobile-join-btn"
            round
            @click="goToMessageWall"
            >留言墙</el-button
          >
          <el-button
            type="primary"
            class="mobile-join-btn"
            round
            @click="scrollTo('#join')"
            >加入我们</el-button
          >
        </div>
      </el-collapse-transition>
    </el-header>

    <!-- 首页Banner -->
    <el-main class="home-banner" id="home">
      <!-- 粒子动画背景和代码流动画容器 -->
      <div id="particle-container" class="particle-bg"></div>
      <div id="code-flow" class="code-flow-bg"></div>

      <div class="banner-content">
        <div class="title-container">
          <h1 class="main-title">
            <span class="code-text">智环学创融合协会</span>
          </h1>
          <p class="subtitle">🤓 ：Hello, World？</p>
        </div>

        <p class="description">
          告别单机学习！告别单机学习，组队升级打怪！你的技术合伙人都在这里💥
        </p>

        <div class="action-buttons">
          <el-button class="learn-more-btn" round @click="scrollTo('#about')">
            了解更多
            <el-icon>
              <ArrowRight />
            </el-icon>
          </el-button>
          <el-button class="join-us-btn" round plain @click="scrollTo('#join')">
            加入我们
          </el-button>
        </div>

        <!-- 向下滚动指示 -->
        <div class="scroll-down">
          <el-link
            href="#about"
            :underline="false"
            @click.prevent="scrollTo('#about')"
          >
            <el-icon class="scroll-icon">
              <ArrowDown />
            </el-icon>
          </el-link>
        </div>
      </div>
    </el-main>

    <!-- 社团简介 -->
    <el-main class="about-section" id="about">
      <div class="about-container">
        <div class="text-center mb-12">
          <h1 class="section-title">社团简介</h1>
          <div class="section-divider"></div>
        </div>

        <div class="about-content">
          <div class="about-text">
            <h3 class="about-subtitle">关于 CodePulse</h3>
            <p class="about-paragraph">
              CodePulse
              计算机社团成立于2016年，是校内最具影响力的科技类社团之一。我们致力于为对计算机科学与技术感兴趣的同学提供一个交流、学习和实践的平台。
            </p>
            <p class="about-paragraph">
              成立背景源于一群热爱编程的学生希望打破课堂界限，在实践中提升技能、分享知识。多年来，社团不断发展壮大，已形成完善的组织架构和丰富的活动体系。
            </p>
            <p class="about-paragraph">
              我们的宗旨是：<span class="highlight-text"
                >"探索技术前沿，培养创新思维，提升实践能力，连接志同道合的伙伴"</span
              >。
            </p>

            <div class="stats-grid">
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon>
                    <User />
                  </el-icon>
                </div>
                <h4 class="stat-number">800+</h4>
                <p class="stat-label">注册成员</p>
              </div>
              <div class="stat-card">
                <div class="stat-icon">
                  <el-icon>
                    <Calendar />
                  </el-icon>
                </div>
                <h4 class="stat-number">60+</h4>
                <p class="stat-label">年度活动</p>
              </div>
            </div>
          </div>

          <div class="about-image">
            <div class="image-decoration top-left"></div>
            <div class="image-decoration bottom-right"></div>
            <img
              src="https://picsum.photos/id/180/800/600"
              style="width: 100%; height: 100%"
            />

            <div class="image-badge">
              <div class="badge-icon">
                <el-icon>
                  <Trophy />
                </el-icon>
              </div>
              <div>
                <h4 class="badge-title">连续三年</h4>
                <p class="badge-subtitle">校级优秀社团称号</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 社团数据统计 -->
        <!-- <div class="stats-chart">
          <h3 class="chart-title">社团发展数据</h3>

          <div class="chart-container">
            <div class="chart-placeholder">
              <el-icon>
                <PieChart />
              </el-icon>
              <p>社团发展数据图表</p>
            </div>
          </div>
        </div> -->
      </div>
    </el-main>

    <!-- 部门介绍 -->
    <el-main class="departments-section" id="departments">
      <div class="departments-container">
        <div class="text-center mb-12">
          <h1 class="section-title">部门介绍</h1>
          <div class="section-divider"></div>
          <p class="section-subtitle" style="margin-bottom: 20px">
            社团下设三个核心部门，各有侧重又相互协作，共同推动社团发展
          </p>
        </div>

        <!-- 部门卡片 -->
        <div class="departments-grid">
          <!-- 算法部 -->
          <div class="department-card">
            <div class="department-header algorithm">
              <el-icon class="department-icon">
                <Document />
              </el-icon>
            </div>
            <div class="department-content">
              <h3 class="department-title">
                <el-icon class="department-title-icon">
                  <Document />
                </el-icon>
                算法部
              </h3>
              <p class="department-description">
                专注于算法研究与编程能力提升，定期组织算法学习小组、编程训练和技术分享会。
              </p>
              <p class="department-description">
                主要研究方向包括数据结构、机器学习算法、深度学习框架应用等，培养成员的逻辑思维和问题解决能力。
              </p>

              <el-button
                class="department-details-btn"
                @click="toggleDepartment('algorithm')"
              >
                查看成员
                <el-icon>
                  <ArrowDown v-if="!expandedDepartments.algorithm" />
                  <ArrowUp v-else />
                </el-icon>
              </el-button>

              <!-- 成员列表 -->
              <el-collapse-transition>
                <div
                  v-show="expandedDepartments.algorithm"
                  class="department-members"
                >
                  <h4 class="members-title">核心成员</h4>
                  <div class="members-list">
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">张明</h5>
                        <p class="member-position">部长 | 计算机科学与技术</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">李华</h5>
                        <p class="member-position">副部长 | 软件工程</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">王芳</h5>
                        <p class="member-position">核心成员 | 人工智能</p>
                      </div>
                    </div>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
          </div>

          <!-- 竞赛部 -->
          <div class="department-card">
            <div class="department-header competition">
              <el-icon class="department-icon">
                <Trophy />
              </el-icon>
            </div>
            <div class="department-content">
              <h3 class="department-title">
                <el-icon class="department-title-icon">
                  <Trophy />
                </el-icon>
                竞赛部
              </h3>
              <p class="department-description">
                负责组织和参与各类编程竞赛，包括ACM、蓝桥杯、程序设计大赛等校内外赛事。
              </p>
              <p class="department-description">
                提供竞赛培训、模拟赛和经验分享，帮助成员提升实战能力和团队协作能力，争取优异成绩。
              </p>

              <el-button
                class="department-details-btn"
                @click="toggleDepartment('competition')"
              >
                查看成员
                <el-icon>
                  <ArrowDown v-if="!expandedDepartments.competition" />
                  <ArrowUp v-else />
                </el-icon>
              </el-button>

              <!-- 成员列表 -->
              <el-collapse-transition>
                <div
                  v-show="expandedDepartments.competition"
                  class="department-members"
                >
                  <h4 class="members-title">核心成员</h4>
                  <div class="members-list">
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">刘强</h5>
                        <p class="member-position">部长 | 计算机科学与技术</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">陈晨</h5>
                        <p class="member-position">副部长 | 数据科学</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">杨帆</h5>
                        <p class="member-position">核心成员 | 软件工程</p>
                      </div>
                    </div>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
          </div>

          <!-- 管理部 -->
          <div class="department-card">
            <div class="department-header management">
              <el-icon class="department-icon">
                <Setting />
              </el-icon>
            </div>
            <div class="department-content">
              <h3 class="department-title">
                <el-icon class="department-title-icon">
                  <Setting />
                </el-icon>
                管理部
              </h3>
              <p class="department-description">
                负责社团日常运营、活动策划、成员管理和对外联络，是社团高效运转的保障。
              </p>
              <p class="department-description">
                主要工作包括活动组织、宣传推广、经费管理、校企合作对接等，培养成员的组织和沟通能力。
              </p>

              <el-button
                class="department-details-btn"
                @click="toggleDepartment('management')"
              >
                查看成员
                <el-icon>
                  <ArrowDown v-if="!expandedDepartments.management" />
                  <ArrowUp v-else />
                </el-icon>
              </el-button>

              <!-- 成员列表 -->
              <el-collapse-transition>
                <div
                  v-show="expandedDepartments.management"
                  class="department-members"
                >
                  <h4 class="members-title">核心成员</h4>
                  <div class="members-list">
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">周敏</h5>
                        <p class="member-position">部长 | 信息管理</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">吴迪</h5>
                        <p class="member-position">副部长 | 电子商务</p>
                      </div>
                    </div>
                    <div class="member-item">
                      <div class="member-info">
                        <h5 class="member-name">郑亮</h5>
                        <p class="member-position">
                          核心成员 | 计算机科学与技术
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
          </div>
        </div>

        <!-- 组织架构图 -->
        <div class="organization-chart">
          <h3 class="chart-title">组织架构</h3>

          <div class="chart-container">
            <!-- 社团主席 -->
            <div class="org-node president">
              <h4 class="org-node-title">社团主席</h4>
              <p>陈晓峰</p>
            </div>

            <!-- 连接线 -->
            <div class="org-line vertical"></div>

            <!-- 部门总监 -->
            <div class="org-node director">
              <h4 class="org-node-title">部门总监</h4>
              <p>林小雨</p>
            </div>

            <!-- 连接线 -->
            <div class="org-line vertical"></div>
            <div class="org-line horizontal"></div>

            <!-- 三个部门 -->
            <div class="org-node department algorithm">
              <h4 class="org-node-title">算法部</h4>
              <p>部长: 张明</p>
            </div>

            <div class="org-node department competition">
              <h4 class="org-node-title">竞赛部</h4>
              <p>部长: 刘强</p>
            </div>

            <div class="org-node department management">
              <h4 class="org-node-title">管理部</h4>
              <p>部长: 周敏</p>
            </div>
          </div>

          <p class="chart-note">点击节点查看详情</p>
        </div>
      </div>
    </el-main>

    <!-- 活动风采 -->
    <el-main class="activities-section" id="activities">
      <div class="activities-container">
        <div class="text-center mb-12">
          <h1 class="section-title">活动风采</h1>
          <div class="section-divider"></div>
          <p class="section-subtitle" style="margin-bottom: 20px">
            回顾我们过往的精彩活动瞬间，感受技术交流的热烈氛围与实践的乐趣
          </p>
        </div>

        <!-- 重点活动回顾 -->
        <div class="activities-grid">
          <div class="featured-activity">
            <div class="activity-overlay">
              <el-tag class="activity-tag" type="warning">品牌活动</el-tag>
              <h3 class="activity-title">第七届校园编程大赛</h3>
              <p class="activity-description">
                吸引全校200+选手参与，历经初赛、复赛和决赛的激烈角逐，最终12支队伍脱颖而出
              </p>
              <div class="activity-meta">
                <div class="meta-item">
                  <el-icon>
                    <Calendar />
                  </el-icon>
                  <span>2023-05-20</span>
                </div>
                <div class="meta-item">
                  <el-icon>
                    <Trophy />
                  </el-icon>
                  <span>12支获奖队伍</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 活动集锦 -->
          <div class="activity-highlights">
            <h3 class="highlights-title">活动集锦</h3>

            <div class="highlights-grid">
              <div class="highlight-item">
                <div
                  class="highlight-overlay"
                  style="
                    background-image: url('https://picsum.photos/id/180/400/300');
                    background-size: cover;
                  "
                >
                  <span>技术讲座</span>
                </div>
              </div>
              <div class="highlight-item">
                <div
                  class="highlight-overlay"
                  style="
                    background-image: url('https://picsum.photos/id/48/400/300');
                    background-size: cover;
                  "
                >
                  <span>开发工作坊</span>
                </div>
              </div>
              <div class="highlight-item">
                <div
                  class="highlight-overlay"
                  style="
                    background-image: url('https://picsum.photos/id/160/400/300');
                    background-size: cover;
                  "
                >
                  <span>24小时黑客松</span>
                </div>
              </div>
              <div class="highlight-item">
                <div
                  class="highlight-overlay"
                  style="
                    background-image: url('https://picsum.photos/id/119/400/300');
                    background-size: cover;
                  "
                >
                  <span>企业参观</span>
                </div>
              </div>
            </div>

            <div class="activity-features">
              <div class="feature-header">
                <el-icon class="feature-icon">
                  <Lightning />
                </el-icon>
                <h4 class="feature-title">活动亮点</h4>
              </div>
              <ul class="feature-list">
                <li class="feature-item">
                  <el-icon class="feature-check">
                    <Check />
                  </el-icon>
                  <span>与5家科技企业建立合作，提供实践机会</span>
                </li>
                <li class="feature-item">
                  <el-icon class="feature-check">
                    <Check />
                  </el-icon>
                  <span>邀请行业专家开展12场技术前沿讲座</span>
                </li>
                <li class="feature-item">
                  <el-icon class="feature-check">
                    <Check />
                  </el-icon>
                  <span>举办各类技术竞赛，累计发放奖金超5万元</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 活动视频集锦 -->
        <div class="activity-videos">
          <h3 class="videos-title">活动视频集锦</h3>
          <div class="videos-grid">
            <div class="video-item">
              <div class="video-overlay">
                <el-icon class="play-icon">
                  <VideoPlay />
                </el-icon>
              </div>
              <div class="video-caption">编程大赛精彩瞬间</div>
            </div>

            <div class="video-item">
              <div class="video-overlay">
                <el-icon class="play-icon">
                  <VideoPlay />
                </el-icon>
              </div>
              <div class="video-caption">人工智能技术讲座</div>
            </div>

            <div class="video-item">
              <div class="video-overlay">
                <el-icon class="play-icon">
                  <VideoPlay />
                </el-icon>
              </div>
              <div class="video-caption">24小时黑客松活动记录</div>
            </div>
          </div>
        </div>
      </div>
    </el-main>

    <!-- 项目展示 -->
    <el-main class="projects-section" id="projects">
      <div class="projects-container">
        <div class="text-center mb-12">
          <h1 class="section-title">项目展示</h1>
          <div class="section-divider"></div>
          <p class="section-subtitle">
            探索我们的优秀项目成果，这些都是社团成员共同努力的结晶
          </p>
        </div>

        <!-- 项目卡片 -->
        <div class="projects-grid">
          <!-- 项目1 -->
          <div class="project-card">
            <div class="project-image-container">
              <img
                src="https://picsum.photos/id/160/600/400"
                alt="校园二手交易平台"
                style="width: 100%; height: 100%"
              />
              <el-tag class="project-tag" type="primary">Web开发</el-tag>
            </div>
            <div class="project-content">
              <h3 class="project-title">校园二手交易平台</h3>
              <p class="project-description">
                基于React和Node.js开发的校园二手交易平台，方便学生之间的物品交易，支持在线聊天和支付功能。
              </p>
              <div class="project-tags">
                <el-tag class="tech-tag">React</el-tag>
                <el-tag class="tech-tag">Node.js</el-tag>
                <el-tag class="tech-tag">MongoDB</el-tag>
              </div>
              <div class="project-footer">
                <div class="project-author">
                  <span class="author-name">张明团队</span>
                </div>
                <el-link class="project-link" :underline="false"
                  >查看详情</el-link
                >
              </div>
            </div>
          </div>

          <!-- 项目2 -->
          <div class="project-card">
            <div class="project-image-container">
              <img
                src="https://picsum.photos/id/119/600/400"
                alt="智能校园导航系统"
                style="width: 100%; height: 100%"
              />
              <el-tag class="project-tag" type="warning">移动应用</el-tag>
            </div>
            <div class="project-content">
              <h3 class="project-title">智能校园导航系统</h3>
              <p class="project-description">
                基于Flutter开发的校园导航APP，支持校内建筑导航、教室查询、课程提醒等功能，提升校园生活效率。
              </p>
              <div class="project-tags">
                <el-tag class="tech-tag">Flutter</el-tag>
                <el-tag class="tech-tag">高德地图API</el-tag>
                <el-tag class="tech-tag">Firebase</el-tag>
              </div>
              <div class="project-footer">
                <div class="project-author">
                  <span class="author-name">陈晨团队</span>
                </div>
                <el-link class="project-link" :underline="false"
                  >查看详情</el-link
                >
              </div>
            </div>
          </div>

          <!-- 项目3 -->
          <div class="project-card">
            <div class="project-image-container">
              <img
                src="https://picsum.photos/id/2/600/400"
                alt="学生成绩分析系统"
                style="width: 100%; height: 100%"
              />
              <el-tag class="project-tag">数据分析</el-tag>
            </div>
            <div class="project-content">
              <h3 class="project-title">学生成绩分析系统</h3>
              <p class="project-description">
                基于Python和Django开发的成绩分析平台，通过数据可视化展示学习情况，提供个性化学习建议。
              </p>
              <div class="project-tags">
                <el-tag class="tech-tag">Python</el-tag>
                <el-tag class="tech-tag">Django</el-tag>
                <el-tag class="tech-tag">Matplotlib</el-tag>
              </div>
              <div class="project-footer">
                <div class="project-author">
                  <span class="author-name">王芳团队</span>
                </div>
                <el-link class="project-link" :underline="false"
                  >查看详情</el-link
                >
              </div>
            </div>
          </div>

          <!-- 项目4 -->
          <div class="project-card">
            <div class="project-image-container">
              <img
                src="https://picsum.photos/id/96/600/400"
                alt="校园活动推荐系统"
                style="width: 100%; height: 100%"
              />
              <el-tag class="project-tag" type="primary">人工智能</el-tag>
            </div>
            <div class="project-content">
              <h3 class="project-title">校园活动推荐系统</h3>
              <p class="project-description">
                基于协同过滤算法的校园活动智能推荐系统，根据学生兴趣和历史参与记录精准推送合适活动。
              </p>
              <div class="project-tags">
                <el-tag class="tech-tag">Python</el-tag>
                <el-tag class="tech-tag">TensorFlow</el-tag>
                <el-tag class="tech-tag">Flask</el-tag>
              </div>
              <div class="project-footer">
                <div class="project-author">
                  <span class="author-name">刘强团队</span>
                </div>
                <el-link class="project-link" :underline="false"
                  >查看详情</el-link
                >
              </div>
            </div>
          </div>
        </div>

        <!-- 查看更多按钮 -->
        <div class="more-projects">
          <el-button class="more-btn" round>
            查看更多项目
            <el-icon>
              <ArrowRight />
            </el-icon>
          </el-button>
        </div>
      </div>
    </el-main>

    <!-- 加入我们 -->
    <el-main class="join-section" id="join">
      <div class="join-container">
        <div class="text-center mb-12">
          <h1 class="section-title">加入我们</h1>
          <div class="section-divider"></div>
          <p class="section-subtitle">
            无论你是编程新手还是技术达人，只要对计算机技术充满热情，我们都欢迎你的加入
          </p>
        </div>

        <div class="join-content">
          <!-- 加入理由 -->
          <div class="join-reasons">
            <h3 class="reasons-title">为什么加入我们？</h3>

            <div class="reasons-list">
              <div class="reason-item">
                <div class="reason-icon">
                  <el-icon>
                    <Document />
                  </el-icon>
                </div>
                <div class="reason-content">
                  <h4 class="reason-title">提升技术能力</h4>
                  <p class="reason-description">
                    参与技术分享和实践项目，在实践中快速提升编程和开发技能
                  </p>
                </div>
              </div>

              <div class="reason-item">
                <div class="reason-icon">
                  <el-icon>
                    <User />
                  </el-icon>
                </div>
                <div class="reason-content">
                  <h4 class="reason-title">结识志同道合的伙伴</h4>
                  <p class="reason-description">
                    与来自不同专业的技术爱好者交流，拓展人脉圈，找到志同道合的朋友
                  </p>
                </div>
              </div>

              <div class="reason-item">
                <div class="reason-icon">
                  <el-icon>
                    <Trophy />
                  </el-icon>
                </div>
                <div class="reason-content">
                  <h4 class="reason-title">参与竞赛拿奖</h4>
                  <p class="reason-description">
                    有机会参加各类编程竞赛，获得荣誉和奖励，为简历增添亮点
                  </p>
                </div>
              </div>

              <div class="reason-item">
                <div class="reason-icon">
                  <el-icon>
                    <Briefcase />
                  </el-icon>
                </div>
                <div class="reason-content">
                  <h4 class="reason-title">获取实习机会</h4>
                  <p class="reason-description">
                    优秀成员有机会获得合作企业的实习推荐，提前规划职业发展
                  </p>
                </div>
              </div>
            </div>

            <!-- 报名须知 -->
            <div class="join-notice">
              <div class="notice-header">
                <el-icon class="notice-icon">
                  <InfoFilled />
                </el-icon>
                <h4 class="notice-title">报名须知</h4>
              </div>
              <ul class="notice-list">
                <li class="notice-item">
                  <el-icon class="notice-check">
                    <Check />
                  </el-icon>
                  <span>报名截止日期：2023年11月30日</span>
                </li>
                <li class="notice-item">
                  <el-icon class="notice-check">
                    <Check />
                  </el-icon>
                  <span>面向对象：全体在校学生，专业不限</span>
                </li>
                <li class="notice-item">
                  <el-icon class="notice-check">
                    <Check />
                  </el-icon>
                  <span>无需编程基础，有兴趣即可报名</span>
                </li>
                <li class="notice-item">
                  <el-icon class="notice-check">
                    <Check />
                  </el-icon>
                  <span>报名后将收到面试通知，请注意查收邮件</span>
                </li>
              </ul>
            </div>
          </div>

          <!-- 报名表单 -->
          <div class="join-form-container">
            <el-form
              class="join-form"
              :model="joinForm"
              :rules="joinRules"
              ref="joinFormRef"
            >
              <h3 class="form-title">填写报名信息</h3>

              <el-form-item prop="name">
                <el-input
                  v-model="joinForm.name"
                  placeholder="请输入您的姓名"
                />
              </el-form-item>

              <el-form-item prop="studentId">
                <el-input
                  v-model="joinForm.studentId"
                  placeholder="请输入您的学号"
                />
              </el-form-item>

              <el-form-item prop="class">
                <el-input
                  v-model="joinForm.class"
                  placeholder="请输入您的班级，例如：计科2101"
                />
              </el-form-item>

              <el-form-item prop="major">
                <el-input
                  v-model="joinForm.major"
                  placeholder="请输入您的专业"
                />
              </el-form-item>

              <el-form-item prop="phone">
                <el-input
                  v-model="joinForm.phone"
                  placeholder="请输入您的联系电话"
                />
              </el-form-item>

              <el-form-item prop="email">
                <el-input
                  v-model="joinForm.email"
                  placeholder="请输入您的电子邮箱"
                />
              </el-form-item>

              <el-form-item prop="department">
                <el-select
                  v-model="joinForm.department"
                  placeholder="请选择您想加入的部门"
                >
                  <el-option label="算法部" value="algorithm"></el-option>
                  <el-option label="竞赛部" value="competition"></el-option>
                  <el-option label="管理部" value="management"></el-option>
                  <el-option label="暂未确定" value="undecided"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="introduction">
                <el-input
                  v-model="joinForm.introduction"
                  type="textarea"
                  :rows="4"
                  placeholder="请简要介绍一下自己，包括兴趣爱好、技能特长等（至少50字）"
                  :validate-event="false"
                />
              </el-form-item>

              <el-form-item prop="reason">
                <el-input
                  v-model="joinForm.reason"
                  type="textarea"
                  :rows="3"
                  placeholder="请说明您想加入社团的原因和期望"
                  :validate-event="false"
                />
              </el-form-item>

              <el-form-item>
                <el-button
                  class="submit-btn"
                  type="primary"
                  @click="submitJoinForm"
                >
                  提交报名
                  <el-icon>
                    <Promotion />
                  </el-icon>
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import {
  Menu,
  ArrowRight,
  ArrowDown,
  User,
  Calendar,
  Trophy,
  Document,
  Setting,
  Lightning,
  Check,
  VideoPlay,
  InfoFilled,
  Briefcase,
  Promotion,
  ArrowUp,
} from "@element-plus/icons-vue";

// 留言墙
// 从 vue-router 导入 useRouter 钩子
import { useRouter } from "vue-router";

// 获取路由实例
const router = useRouter();

// 跳转路由的方法
const goToMessageWall = () => {
  // 1. 直接通过路径跳转
  router.push("/messages");
};

const showMobileMenu = ref(false);
// 定义部门展开状态类型
interface DepartmentExpanded {
  algorithm: boolean;
  competition: boolean;
  management: boolean;
}

const expandedDepartments = reactive<DepartmentExpanded>({
  algorithm: false,
  competition: false,
  management: false,
});

const joinFormRef = ref();
const joinForm = reactive({
  name: "",
  studentId: "",
  class: "",
  major: "",
  phone: "",
  email: "",
  department: "",
  introduction: "",
  reason: "",
});

const joinRules = reactive({
  name: [{ required: true, message: "请输入您的姓名", trigger: "blur" }],
  studentId: [{ required: true, message: "请输入您的学号", trigger: "blur" }],
  class: [{ required: true, message: "请输入您的班级", trigger: "blur" }],
  major: [{ required: true, message: "请输入您的专业", trigger: "blur" }],
  phone: [{ required: true, message: "请输入您的联系电话", trigger: "blur" }],
  email: [
    { required: true, message: "请输入您的电子邮箱", trigger: "blur" },
    {
      type: "email",
      message: "请输入正确的邮箱格式",
      trigger: ["blur", "change"],
    },
  ],
  department: [
    { required: true, message: "请选择意向部门", trigger: "change" },
  ],
  introduction: [
    { required: true, message: "请填写个人介绍", trigger: "blur" },
    { min: 1, message: "个人介绍至少50字", trigger: "blur" },
  ],
  reason: [{ required: true, message: "请填写加入原因", trigger: "blur" }],
});

const navItems = [
  { id: 1, text: "首页", href: "#home" },
  { id: 2, text: "社团简介", href: "#about" },
  { id: 3, text: "部门介绍", href: "#departments" },
  { id: 4, text: "活动风采", href: "#activities" },
  { id: 5, text: "项目展示", href: "#projects" },
  { id: 6, text: "学生会", href: "#student-council" },
];

const scrollTo = (id: string) => {
  showMobileMenu.value = false;
  const element = document.querySelector(id);
  if (element) {
    element.scrollIntoView({
      behavior: "smooth",
    });
  }
};

const toggleDepartment = (department: keyof DepartmentExpanded) => {
  expandedDepartments[department] = !expandedDepartments[department];
};

const submitJoinForm = () => {
  joinFormRef.value.validate((valid: boolean) => {
    if (valid) {
      // 这里可以添加表单提交逻辑
      alert("报名信息提交成功！我们将尽快与您联系，请留意邮件通知。");
      // 实际项目中这里应该调用API提交表单
      // submitInformation(joinForm)
      joinFormRef.value.resetFields();
    } else {
      return false;
    }
  });
};
</script>

<style scoped lang="scss">
.code-pulse-container {
  font-family: "Inter", "system-ui", "sans-serif";
  color: #333;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: auto;
  padding: 0;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(12px);
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  .container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
    padding: 12px 24px;
  }

  .logo-link {
    display: flex;
    align-items: center;
    gap: 12px;
    color: inherit;

    .logo-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: linear-gradient(135deg, #165dff, #ff7d00);
      display: flex;
      align-items: center;
      justify-content: center;

      span {
        color: white;
        font-weight: bold;
        font-size: 20px;
      }
    }

    .logo-text {
      font-size: 20px;
      font-weight: bold;
      background: linear-gradient(90deg, #165dff, #ff7d00);
      -webkit-background-clip: text;
      background-clip: text;
      color: transparent;
    }
  }

  .desktop-nav {
    display: flex;
    align-items: center;
    gap: 32px;

    .nav-link {
      position: relative;
      color: #333;
      font-size: 16px;
      transition: color 0.3s;

      &:hover {
        color: #165dff;
      }

      &::after {
        content: "";
        position: absolute;
        bottom: -4px;
        left: 0;
        width: 0;
        height: 2px;
        background-color: #165dff;
        transition: width 0.3s;
      }

      &:hover::after {
        width: 100%;
      }
    }

    .join-btn {
      background: linear-gradient(90deg, #165dff, #ff7d00);
      border: none;
      color: white;
      padding: 8px 24px;
      font-weight: 500;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(22, 93, 255, 0.3);
      }
    }
  }

  .mobile-menu-btn {
    display: none;
    background: none;
    border: none;
    color: #333;
    font-size: 24px;
    padding: 8px;
  }

  .mobile-menu {
    display: none;
    background-color: white;
    border-top: 1px solid #eee;
    padding: 16px 24px;
    animation: fadeIn 0.3s ease;

    .mobile-nav-link {
      display: block;
      padding: 12px 0;
      color: #333;
      font-size: 18px;
      transition: color 0.3s;

      &:hover {
        color: #165dff;
      }
    }

    .mobile-join-btn {
      display: block;
      width: 100%;
      margin-top: 12px;
      background: linear-gradient(90deg, #165dff, #ff7d00);
      border: none;
      color: white;
      padding: 12px 0;
    }
  }

  @media (max-width: 992px) {
    .desktop-nav {
      display: none;
    }

    .mobile-menu-btn {
      display: block;
    }

    .mobile-menu {
      display: block;
    }
  }
}

/* 首页Banner样式 */
.home-banner {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(to bottom, #0f172a, #1e293b);
  padding-top: 80px;
  color: white;
  text-align: center;

  .particle-bg,
  .code-flow-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .particle-bg {
    background: transparent;
    z-index: 1;
  }

  .code-flow-bg {
    background: transparent;
    opacity: 0.4;
    z-index: 2;
  }

  .banner-content {
    position: relative;
    z-index: 3;
    max-width: 1200px;
    padding: 0 24px;
    margin: 0 auto;

    .title-container {
      margin-bottom: 40px;

      .main-title {
        font-size: clamp(2rem, 8vw, 4rem);
        font-weight: bold;
        margin-bottom: 24px;
        line-height: 1.2;

        .code-text {
          background: linear-gradient(90deg, #165dff, #ff7d00);
          -webkit-background-clip: text;
          background-clip: text;
          color: transparent;
          text-shadow: 0 0 10px currentColor;
        }

        .pulse-text {
          display: block;
        }
      }

      .subtitle {
        font-size: clamp(1rem, 4vw, 1.3rem);
        color: #cbd5e1;
        max-width: 800px;
        margin: 0 auto;
      }
    }

    .description {
      color: #cbd5e1;
      max-width: 700px;
      margin: 0 auto 40px;
      font-size: 16px;
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-bottom: 40px;

      .learn-more-btn {
        background-color: white;
        color: #0f172a;
        font-weight: 500;
        padding: 12px 24px;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-4px);
          box-shadow: 0 10px 20px rgba(255, 255, 255, 0.2);
        }
      }

      .join-us-btn {
        border-color: white;
        color: rgb(31, 3, 3);
        padding: 12px 24px;
        transition: all 0.3s;

        &:hover {
          background-color: rgba(255, 255, 255, 0.1);
          transform: translateY(-4px);
        }
      }
    }

    .scroll-down {
      position: absolute;
      bottom: -40px;
      left: 50%;
      transform: translateX(-50%);
      animation: bounce 2s infinite;

      .scroll-icon {
        color: rgba(255, 255, 255, 0.7);
        font-size: 24px;
        transition: color 0.3s;

        &:hover {
          color: white;
        }
      }
    }
  }
}

/* 通用部分样式 */
.section-title {
  font-size: clamp(1.5rem, 5vw, 2.5rem);
  font-weight: bold;
  margin-bottom: 16px;
  color: #333;
}

.section-divider {
  width: 80px;
  height: 2px;
  background: linear-gradient(to right, #165dff, #ff7d00);
  margin: 0 auto 24px;
}

.section-subtitle {
  color: #666;
  max-width: 800px;
  margin: 0 auto;
  font-size: 1rem;
}

.text-center {
  text-align: center;
}

/* 社团简介部分样式 */
.about-section {
  padding: 80px 0;
  background-color: white;

  .about-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .about-content {
    display: grid;
    grid-template-columns: 1fr;
    gap: 40px;
    margin-bottom: 60px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .about-text {
    .about-subtitle {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      color: #333;
    }

    .about-paragraph {
      color: #666;
      margin-bottom: 16px;
      line-height: 1.6;
    }

    .highlight-text {
      font-weight: bold;
      color: #165dff;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 24px;
      margin-top: 40px;
    }

    .stat-card {
      background-color: #f8fafc;
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
      }
    }

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background-color: rgba(22, 93, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;

      .el-icon {
        font-size: 24px;
        color: #165dff;
      }
    }

    .stat-number {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 8px;
      color: #333;
    }

    .stat-label {
      color: #666;
      font-size: 0.875rem;
    }
  }

  .about-image {
    position: relative;

    .image-decoration {
      position: absolute;
      background-color: rgba(22, 93, 255, 0.1);
      border-radius: 8px;
      z-index: -1;

      &.top-left {
        width: 96px;
        height: 96px;
        top: -16px;
        left: -16px;
      }

      &.bottom-right {
        width: 128px;
        height: 128px;
        bottom: -16px;
        right: -16px;
      }
    }

    .about-img {
      width: 100%;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    }

    .image-badge {
      position: absolute;
      bottom: -24px;
      left: -24px;
      background-color: white;
      padding: 16px;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      gap: 12px;

      .badge-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background-color: rgba(255, 125, 0, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;

        .el-icon {
          color: #ff7d00;
          font-size: 20px;
        }
      }

      .badge-title {
        font-weight: bold;
        margin-bottom: 4px;
        color: #333;
      }

      .badge-subtitle {
        font-size: 0.875rem;
        color: #666;
      }
    }
  }

  .stats-chart {
    margin-top: 80px;

    .chart-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      text-align: center;
      color: #333;
    }

    .chart-container {
      background-color: #f8fafc;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    }

    .chart-placeholder {
      height: 250px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #666;
      background-color: white;
      border-radius: 8px;
      border: 1px dashed #ddd;

      .el-icon {
        font-size: 48px;
        margin-bottom: 16px;
        color: #165dff;
      }
    }
  }
}

/* 部门介绍部分样式 */
.departments-section {
  padding: 80px 0;
  background-color: #f8fafc;

  .departments-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  /* 部门卡片网格布局 */
  .departments-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 60px;

    @media (min-width: 768px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  /* 单个部门卡片样式 */
  .department-card {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    /* 部门头部区域 */
    .department-header {
      height: 180px;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      overflow: hidden;

      /* 不同部门的颜色主题 */
      &.algorithm {
        background: linear-gradient(135deg, #165dff 0%, #4080ff 100%);
      }

      &.competition {
        background: linear-gradient(135deg, #ff7d00 0%, #ffa940 100%);
      }

      &.management {
        background: linear-gradient(135deg, #4b5563 0%, #6b7280 100%);
      }

      /* 部门图标样式 */
      .department-icon {
        color: white;
        font-size: 64px;
        z-index: 2;
        transition: all 0.3s ease;
      }

      /* 背景装饰元素 */
      &::before {
        content: "";
        position: absolute;
        width: 150px;
        height: 150px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 50%;
        top: -50px;
        right: -50px;
      }

      &::after {
        content: "";
        position: absolute;
        width: 100px;
        height: 100px;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 50%;
        bottom: -30px;
        left: -30px;
      }
    }

    /* 部门内容区域 */
    .department-content {
      padding: 24px;

      .department-title {
        font-size: 1.25rem;
        font-weight: 600;
        margin-bottom: 16px;
        color: #1f2937;
        display: flex;
        align-items: center;

        .department-title-icon {
          margin-right: 10px;
          font-size: 20px;
        }
      }

      .department-description {
        color: #6b7280;
        font-size: 0.875rem;
        line-height: 1.6;
        margin-bottom: 12px;
      }

      /* 查看成员按钮 */
      .department-details-btn {
        width: 100%;
        margin-top: 16px;
        background: transparent;
        border: 1px solid #e5e7eb;
        color: #4b5563;
        font-size: 0.875rem;
        transition: all 0.3s ease;

        &:hover {
          background: #f3f4f6;
          border-color: #d1d5db;
        }

        .el-icon {
          margin-left: 6px;
          transition: transform 0.3s ease;
        }
      }
    }

    /* 成员列表区域 */
    .department-members {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px solid #f3f4f6;

      .members-title {
        font-size: 0.875rem;
        font-weight: 600;
        color: #4b5563;
        margin-bottom: 12px;
      }

      .members-list {
        display: grid;
        gap: 12px;
      }

      .member-item {
        display: flex;
        align-items: center;
        padding: 12px;
        background: #f9fafb;
        border-radius: 8px;
        transition: all 0.2s ease;

        &:hover {
          background: #f3f4f6;
          transform: translateX(4px);
        }

        .member-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          margin-right: 12px;
          object-fit: cover;
          border: 2px solid white;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .member-info {
          .member-name {
            font-size: 0.875rem;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 2px;
          }

          .member-position {
            font-size: 0.75rem;
            color: #9ca3af;
          }
        }
      }
    }
  }

  /* 组织架构图样式 */
  .organization-chart {
    margin-top: 80px;
    background: white;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);

    .chart-title {
      font-size: 1.5rem;
      font-weight: 600;
      color: #1f2937;
      text-align: center;
      margin-bottom: 40px;
    }

    .chart-container {
      position: relative;
      height: 350px;
      min-width: 600px;
      margin: 0 auto;

      /* 组织节点 */
      .org-node {
        position: absolute;
        padding: 16px 24px;
        border-radius: 8px;
        text-align: center;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        cursor: pointer;
        z-index: 10;

        &:hover {
          transform: scale(1.05);
          box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }

        &.president {
          top: 0;
          left: 50%;
          transform: translateX(-50%);
          background: linear-gradient(135deg, #165dff 0%, #4080ff 100%);
          color: white;
          width: 180px;
        }

        &.director {
          top: 100px;
          left: 50%;
          transform: translateX(-50%);
          background: white;
          border: 2px solid #e5e7eb;
          width: 200px;
        }

        &.department {
          top: 200px;
          width: 180px;

          &.algorithm {
            left: calc(33.333% - 90px);
            background: rgba(22, 93, 255, 0.1);
            border: 1px solid rgba(22, 93, 255, 0.2);
          }

          &.competition {
            left: calc(50% - 90px);
            background: rgba(255, 125, 0, 0.1);
            border: 1px solid rgba(255, 125, 0, 0.2);
          }

          &.management {
            left: calc(66.666% - 90px);
            background: rgba(75, 85, 99, 0.1);
            border: 1px solid rgba(75, 85, 99, 0.2);
          }
        }

        .org-node-title {
          font-weight: 600;
          margin-bottom: 4px;
        }
      }

      /* 连接线 */
      .org-line {
        position: absolute;
        background: #d1d5db;

        &.vertical {
          width: 2px;
          height: 40px;
          left: 50%;
          transform: translateX(-50%);
        }

        &.horizontal {
          width: calc(100% - 200px);
          height: 2px;
          top: 200px;
          left: 100px;
        }
      }
    }

    .chart-note {
      text-align: center;
      color: #9ca3af;
      font-size: 0.75rem;
      margin-top: 20px;
    }
  }

  /* 响应式调整 */
  @media (max-width: 992px) {
    .departments-grid {
      grid-template-columns: 1fr 1fr;
    }

    .organization-chart {
      display: none;
    }
  }

  @media (max-width: 768px) {
    .departments-grid {
      grid-template-columns: 1fr;
    }

    .department-card {
      max-width: 400px;
      margin: 0 auto;
    }
  }
}

/* 活动风采部分样式 */
.activities-section {
  padding: 80px 0;
  background-color: white;

  .activities-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .activities-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 40px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .featured-activity {
    position: relative;
    height: 400px;
    border-radius: 12px;
    overflow: hidden;
    background-color: #0f172a;
    background-size: cover;
    background-position: center;

    .activity-overlay {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 24px;
      background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
      color: white;

      .activity-tag {
        margin-bottom: 12px;
        font-weight: bold;
      }

      .activity-title {
        font-size: 1.5rem;
        font-weight: bold;
        margin-bottom: 12px;
      }

      .activity-description {
        margin-bottom: 16px;
        font-size: 0.875rem;
        opacity: 0.9;
      }

      .activity-meta {
        display: flex;
        gap: 24px;

        .meta-item {
          display: flex;
          align-items: center;
          font-size: 0.875rem;
          opacity: 0.8;

          .el-icon {
            margin-right: 8px;
            font-size: 16px;
          }
        }
      }
    }
  }

  .activity-highlights {
    .highlights-title {
      font-size: 1.25rem;
      font-weight: bold;
      margin-bottom: 16px;
      color: #333;
    }

    .highlights-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
      margin-bottom: 16px;
    }

    .highlight-item {
      position: relative;
      height: 120px;
      border-radius: 8px;
      overflow: hidden;
      background-size: cover;
      background-position: center;
      cursor: pointer;
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.05);

        .highlight-overlay {
          opacity: 1;
        }
      }

      .highlight-overlay {
        position: absolute;
        inset: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-weight: 500;
        transition: opacity 0.3s ease;
      }
    }

    .activity-features {
      background-color: #f8fafc;
      border-radius: 12px;
      padding: 16px;
      margin-top: 24px;

      .feature-header {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .feature-icon {
          color: #165dff;
          font-size: 24px;
          margin-right: 12px;
        }

        .feature-title {
          font-weight: bold;
          color: #333;
        }
      }

      .feature-list {
        list-style: none;
        padding: 0;
        margin: 0;

        .feature-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 12px;

          .feature-check {
            color: #165dff;
            font-size: 16px;
            margin-right: 8px;
            margin-top: 2px;
          }

          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }

  .activity-videos {
    margin-top: 60px;

    .videos-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
      text-align: center;
      color: #333;
    }

    .videos-grid {
      display: grid;
      grid-template-columns: 1fr;
      gap: 24px;

      @media (min-width: 768px) {
        grid-template-columns: repeat(3, 1fr);
      }
    }

    .video-item {
      position: relative;
      border-radius: 12px;
      overflow: hidden;
      cursor: pointer;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-4px);

        .video-overlay {
          background-color: rgba(0, 0, 0, 0.6);

          .play-icon {
            transform: scale(1.2);
          }
        }
      }

      .video-overlay {
        position: absolute;
        inset: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(0, 0, 0, 0.4);
        transition: all 0.3s ease;

        .play-icon {
          color: white;
          font-size: 48px;
          transition: transform 0.3s ease;
        }
      }

      .video-caption {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 12px;
        background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
        color: white;
        font-size: 0.875rem;
      }
    }
  }
}

/* 项目展示部分样式 */
.projects-section {
  padding: 80px 0;
  background-color: #f8fafc;

  .projects-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .projects-grid {
    display: grid;
    grid-template-columns: 1fr;
    gap: 24px;

    @media (min-width: 768px) {
      grid-template-columns: repeat(2, 1fr);
    }

    @media (min-width: 992px) {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  .project-card {
    background-color: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
    }
  }

  .project-image-container {
    position: relative;
    height: 200px;
    background-size: cover;
    background-position: center;

    .project-tag {
      position: absolute;
      top: 16px;
      left: 16px;
    }
  }

  .project-content {
    padding: 20px;

    .project-title {
      font-size: 1.25rem;
      font-weight: bold;
      margin-bottom: 12px;
      color: #333;
    }

    .project-description {
      color: #666;
      margin-bottom: 16px;
      font-size: 0.875rem;
      line-height: 1.6;
    }

    .project-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 20px;

      .tech-tag {
        font-size: 0.75rem;
        background-color: #f8fafc;
        color: #666;
        border: none;
      }
    }

    .project-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 12px;
      border-top: 1px solid #eee;

      .project-author {
        display: flex;
        align-items: center;

        .author-avatar {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          margin-right: 8px;
          object-fit: cover;
        }

        .author-name {
          font-size: 0.875rem;
          color: #666;
        }
      }

      .project-link {
        font-size: 0.875rem;
        color: #165dff;
      }
    }
  }

  .more-projects {
    margin-top: 40px;
    text-align: center;

    .more-btn {
      padding: 12px 24px;
      background-color: white;
      color: #165dff;
      border: 1px solid #165dff;

      &:hover {
        background-color: rgba(22, 93, 255, 0.1);
      }
    }
  }
}

/* 加入我们部分样式 */
.join-section {
  padding: 80px 0;
  background: linear-gradient(to bottom right, #0f172a, #1e293b);
  color: white;

  .join-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  .section-title,
  .section-subtitle {
    color: white;
  }

  .section-divider {
    background: linear-gradient(to right, #165dff, #ff7d00);
  }

  .join-content {
    display: grid;
    grid-template-columns: 1fr;
    gap: 40px;
    margin-top: 40px;

    @media (min-width: 992px) {
      grid-template-columns: 1fr 1fr;
    }
  }

  .join-reasons {
    .reasons-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 24px;
    }

    .reasons-list {
      display: grid;
      gap: 24px;
    }

    .reason-item {
      display: flex;
      gap: 16px;

      .reason-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        background-color: rgba(22, 93, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        .el-icon {
          color: #165dff;
          font-size: 24px;
        }
      }

      .reason-content {
        .reason-title {
          font-size: 1.125rem;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .reason-description {
          color: #cbd5e1;
          font-size: 0.875rem;
          line-height: 1.6;
        }
      }
    }

    .join-notice {
      margin-top: 40px;
      padding: 20px;
      background-color: rgba(255, 255, 255, 0.05);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 12px;

      .notice-header {
        display: flex;
        align-items: center;
        margin-bottom: 16px;

        .notice-icon {
          color: #ff7d00;
          font-size: 24px;
          margin-right: 12px;
        }

        .notice-title {
          font-weight: bold;
          font-size: 1.125rem;
        }
      }

      .notice-list {
        list-style: none;
        padding: 0;
        margin: 0;

        .notice-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 12px;
          color: #cbd5e1;
          font-size: 0.875rem;

          .notice-check {
            color: #ff7d00;
            font-size: 16px;
            margin-right: 8px;
            margin-top: 2px;
          }

          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }

  .join-form-container {
    .join-form {
      background-color: white;
      padding: 32px;
      border-radius: 12px;
      box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);

      .form-title {
        font-size: 1.5rem;
        font-weight: bold;
        margin-bottom: 24px;
        color: #333;
        text-align: center;
      }

      .el-form-item {
        margin-bottom: 20px;
      }

      .el-input,
      .el-select,
      .el-textarea {
        width: 100%;
      }

      .submit-btn {
        width: 100%;
        padding: 16px;
        font-size: 1rem;
        background: linear-gradient(to right, #165dff, #ff7d00);
        border: none;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 5px 15px rgba(22, 93, 255, 0.3);
        }
      }
    }
  }
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0) translateX(-50%);
  }

  40% {
    transform: translateY(-20px) translateX(-50%);
  }

  60% {
    transform: translateY(-10px) translateX(-50%);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .home-banner .banner-content .action-buttons {
    flex-direction: column;
    align-items: center;
  }

  .navbar .container {
    padding: 12px 16px;
  }

  .departments-grid {
    grid-template-columns: 1fr !important;
  }

  .organization-chart {
    display: none;
  }
}
</style>
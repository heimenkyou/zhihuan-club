<script setup lang="ts">
  import { onMounted, ref, computed, onUnmounted, nextTick, watch } from 'vue'
  import { useMessageStore } from '../stores/messageStore'
  import { showSuccess } from '@/utils/notification'
  import { Refresh, Edit } from '@element-plus/icons-vue'
  import { useRouter } from 'vue-router'
  import { ElMessageBox } from 'element-plus'
  import CommonFooter from '../components/CommonFooter.vue'

  
  const messageStore = useMessageStore()
  const debugInfo = ref('')
  const router = useRouter() // 初始化router

  // 返回首页函数
  const goToHome = () => {
    router.push('/')
  }

  // 根元素引用
  const messageBoard = ref<null | any>(null)

  // 分页相关数据
  const currentPage = ref(1)
  const pageSize = ref(6)
  const totalCount = ref(0)
  const totalPages = ref(1)
  const jumpPage = ref('')

  // 新增留言数据
  const newMessage = ref({
    nickname: '',
    content: '',
  })

  // 下拉刷新相关
  const isRefreshing = ref(false)
  const startY = ref(0)
  const currentY = ref(0)
  const pullDistance = ref(0)
  const threshold = 80 // 下拉刷新阈值

  // 点赞按钮加载状态（防止重复点击，仅防重复请求，不是防刷）
  const likeLoading = ref<Record<number, boolean>>({})

  // 控制留言内容展开/收起状态
  const expandedMessages = ref<Record<number, boolean>>({})

  // 存储每个留言内容元素的引用
  const contentRefs = ref<Record<number, HTMLElement | null>>({})

  // 存储每个留言是否需要展开按钮的状态
  const showExpandButtons = ref<Record<number, boolean>>({})

  // 计算可见的页码
  const visiblePages = computed(() => {
    const pages = []
    const maxVisible = 7

    if (totalPages.value <= maxVisible) {
      for (let i = 1; i <= totalPages.value; i++) {
        pages.push(i)
      }
    } else {
      if (currentPage.value <= 4) {
        for (let i = 1; i <= 5; i++) {
          pages.push(i)
        }
        pages.push('...')
        pages.push(totalPages.value)
      } else if (currentPage.value >= totalPages.value - 3) {
        pages.push(1)
        pages.push('...')
        for (let i = totalPages.value - 4; i <= totalPages.value; i++) {
          pages.push(i)
        }
      } else {
        pages.push(1)
        pages.push('...')
        for (let i = currentPage.value - 1; i <= currentPage.value + 1; i++) {
          pages.push(i)
        }
        pages.push('...')
        pages.push(totalPages.value)
      }
    }

    return pages
  })

  // 下拉刷新处理 - 使用Vue 3响应式方式
  const pullDistanceStyle = ref('0px')

  const handleTouchStart = (e: any) => {
    if (window.scrollY === 0) {
      startY.value = e.touches[0].clientY
    }
  }

  const handleTouchMove = (e: any) => {
    // 下拉刷新处理 - 优化为仅在页面顶部时触发
    if (messageBoard.value && window.scrollY === 0 && startY.value > 0) {
      currentY.value = e.touches[0].clientY
      pullDistance.value = Math.max(0, currentY.value - startY.value)
      if (pullDistance.value > 0) {
        pullDistanceStyle.value = `${Math.min(pullDistance.value, threshold)}px`
      }
    }
  }

  const handleTouchEnd = async () => {
    if (pullDistance.value >= threshold && !isRefreshing.value) {
      isRefreshing.value = true
      await handleRefresh()
      isRefreshing.value = false
    }
    startY.value = 0
    currentY.value = 0
    pullDistance.value = 0
    pullDistanceStyle.value = '0px'
  }

  // 分页处理方法
  const handlePrevPage = async () => {
    if (currentPage.value > 1) {
      currentPage.value--
      await fetchMessages()
    }
  }

  const handleNextPage = async () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++
      await fetchMessages()
    }
  }

  const handlePageChange = async (page: number | string) => {
    if (typeof page === 'number') {
      currentPage.value = page
      await fetchMessages()
    }
  }

  const handleJumpPage = async () => {
    const page = parseInt(jumpPage.value)
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page
      jumpPage.value = ''
      await fetchMessages()
    }
  }

  // 获取留言数据的方法
  const fetchMessages = async () => {
    debugInfo.value = `正在获取第 ${currentPage.value} 页数据...`
    try {
      const response = await messageStore.fetchMessages({
        current: currentPage.value,
        size: pageSize.value,
      })
      if (response && response.current && response.total) {
        totalCount.value = response.total
        totalPages.value = response.pages
        currentPage.value = response.current
        debugInfo.value = `成功获取第 ${currentPage.value} 页，共 ${totalCount.value} 条留言`
      }
    } catch (error) {
      debugInfo.value = `获取留言失败: ${
        error instanceof Error ? error.message : '未知错误'
      }`
      console.error('获取留言失败:', error)
      throw error
    }
  }

  // 在组件挂载时获取消息数据
  onMounted(async () => {
    try {
      const savedNickname = localStorage.getItem('message_board_nickname')
      if (savedNickname) {
        newMessage.value.nickname = savedNickname
        console.log('👤 已加载保存的昵称:', savedNickname)
      }
      debugInfo.value = '开始获取留言数据...'
      await fetchMessages()
    } catch (error) {
      debugInfo.value = `初始化失败: ${
        error instanceof Error ? error.message : '未知错误'
      }`
      console.error('组件初始化失败:', error)
      throw error
    }
  })

  // 组件卸载时清理资源
  onUnmounted(() => {
    startY.value = 0
    currentY.value = 0
    pullDistance.value = 0
    pullDistanceStyle.value = '0px'
  })

  // 点赞/取消点赞
  const handleLike = async (messageId: number) => {
    // 防止重复点击（仅阻止短时间内重复请求，不是防刷）
    if (likeLoading.value[messageId]) return
    likeLoading.value[messageId] = true
    debugInfo.value = `正在处理留言 ${messageId} 的点赞操作...`

    try {
      // 直接调用store方法，不做额外状态验证（避免同步延迟导致的错误）
      const result = await messageStore.handleLike(messageId)

      // 基于store返回结果提示（不再查找组件内状态，减少同步问题）
      const actionText = result === 'LIKED' ? '点赞' : '取消点赞'
      const successText = result === 'LIKED' ? '已点赞' : '已取消点赞'

      debugInfo.value = `${actionText}成功: 留言 ${messageId}`
      console.log(`👍 ${actionText}成功:`, messageId)
      showSuccess(successText)
    } catch (error) {
      // 仅在store调用真失败时提示（如网络错误），消除状态同步延迟导致的误提示
      const errorMsg =
        error instanceof Error ? error.message : '网络异常，操作失败'
      // 简化错误提示，避免用户困惑
      if (errorMsg.includes('与预期不符')) {
        debugInfo.value = `点赞操作状态同步中，请稍候再试`
        console.warn('点赞状态同步延迟，忽略提示:', error)
        return // 不弹错误框
      }
      debugInfo.value = `点赞/取消点赞失败: ${errorMsg}`
      console.error('点赞操作异常:', error)
      // showError(`操作失败: ${errorMsg}`)
      throw new Error(`点赞/取消点赞失败: ${errorMsg}`)
    } finally {
      // 无论成功失败，都释放加载状态
      likeLoading.value[messageId] = false
    }
  }

  // 实现删除功能 - 使用Vue 3的方式处理确认对话框
  const handleDelete = async (messageId: number) => {
    try {
      await ElMessageBox.confirm(
        '确定要删除这条留言吗？此操作不可恢复。',
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )

      debugInfo.value = `正在删除留言 ${messageId}...`
      try {
        await messageStore.handleDeleteMessage(messageId)
        debugInfo.value = `留言 ${messageId} 删除成功`
        console.log(`🗑️ 留言删除成功:`, messageId)
        showSuccess('删除成功')
        await fetchMessages()
      } catch (error) {
        const errorMsg = error instanceof Error ? error.message : '删除失败'
        debugInfo.value = `删除留言失败: ${errorMsg}`
        console.error('删除留言异常:', error)
        // showError(`删除失败: ${errorMsg}`)
        throw new Error(`删除留言失败: ${errorMsg}`)
      }
    } catch (error) {
      // 用户点击取消
      if ((error as any).msg !== 'cancel') {
        console.error('删除确认框异常:', error)
      }
    }
  }

  // 新增留言
  const handleAddMessage = async () => {
    if (!newMessage.value.nickname.trim() || !newMessage.value.content.trim()) {
      debugInfo.value = '昵称或留言内容不能为空'
      // 只抛出错误，不直接显示通知，避免重复提示
      throw new Error('昵称或留言内容不能为空')
    }

    debugInfo.value = '正在发送留言...'
    try {
      await messageStore.handleCreateMessage({
        nickname: newMessage.value.nickname.trim(),
        content: newMessage.value.content.trim(),
      })
      localStorage.setItem(
        'message_board_nickname',
        newMessage.value.nickname.trim()
      )
      console.log('💾 已保存昵称到本地存储:', newMessage.value.nickname.trim())
      newMessage.value.content = ''
      debugInfo.value = '留言发布成功'
      console.log('📝 留言发布成功')
      showSuccess('新增留言成功')
      await fetchMessages()
    } catch (error) {
      const errorMsg = error instanceof Error ? error.message : '发布失败'
      debugInfo.value = `留言发布失败: ${errorMsg}`
      console.error('发布留言异常:', error)
      // showError(`操作失败: ${errorMsg}`)
      throw error
    }
  }

  // 刷新留言
  const handleRefresh = async () => {
    debugInfo.value = '正在刷新留言数据...'
    try {
      await fetchMessages()
      debugInfo.value = '留言数据刷新成功'
      showSuccess('刷新成功')
    } catch (error) {
      const errorMsg = error instanceof Error ? error.message : '刷新失败'
      debugInfo.value = `刷新留言失败: ${errorMsg}`
      // showError(`刷新失败: ${errorMsg}`)
      throw error
    }
  }

  // 格式化时间显示
  const formatTime = (timeString: string) => {
    try {
      const date = new Date(timeString)
      const now = new Date()
      const diff = now.getTime() - date.getTime()

      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
      if (diff < 172800000) return '昨天'
      if (diff < 31536000000) {
        return date
          .toLocaleDateString('zh-CN', { month: 'numeric', day: 'numeric' })
          .replace('/', '-')
      }
      return date
        .toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: 'numeric',
          day: 'numeric',
        })
        .replace(/\//g, '-')
    } catch (error) {
      return timeString
    }
  }

  // 检测留言是否被截断
  const checkTruncatedMessages = async () => {
    await nextTick()
    messageStore.messages.forEach((msg) => {
      const contentElement = contentRefs.value[msg.id]
      if (contentElement) {
        // 检查元素是否被截断（scrollHeight > clientHeight）
        const isTruncated = contentElement.scrollHeight > contentElement.clientHeight
        showExpandButtons.value[msg.id] = isTruncated
      }
    })
  }

  // 切换留言内容展开/收起状态
  const toggleMessageExpansion = (messageId: number) => {
    expandedMessages.value[messageId] = !expandedMessages.value[messageId]
  }

  // 在组件挂载时检测留言截断状态
  onMounted(async () => {
    try {
      const savedNickname = localStorage.getItem('message_board_nickname')
      if (savedNickname) {
        newMessage.value.nickname = savedNickname
        console.log('👤 已加载保存的昵称:', savedNickname)
      }
      debugInfo.value = '开始获取留言数据...'
      await fetchMessages()
      // 检测留言截断状态
      await checkTruncatedMessages()
    } catch (error) {
      debugInfo.value = `初始化失败: ${
        error instanceof Error ? error.message : '未知错误'
      }`
      console.error('组件初始化失败:', error)
      throw error
    }
  })

  // 监听消息变化，重新检测截断状态
  watch(
    () => messageStore.messages,
    async () => {
      await checkTruncatedMessages()
    },
    { deep: true }
  )

  // 监听expandedMessages变化，但不需要重新检测截断状态
  // 因为展开/收起状态不影响内容是否被截断的判断
  watch(
    expandedMessages,
    () => {
      // 不需要执行任何操作，只需保持响应式更新
    },
    { deep: true }
  )
</script>

<template>
  <div
    class="message-board"
    ref="messageBoard"
    @touchstart="handleTouchStart"
    @touchmove="handleTouchMove"
    @touchend="handleTouchEnd"
  >
    <!-- 下拉刷新指示器 - 使用Vue 3的响应式样式绑定 -->
    <div
      class="pull-indicator"
      :class="{ release: pullDistance >= threshold, refreshing: isRefreshing }"
      :style="{ height: pullDistanceStyle }"
    ></div>

    <!-- 头部区域 -->
    <header class="board-header">
      <div class="container">
        <!-- 返回首页按钮 -->
        <button class="back-home-btn" @click="goToHome" title="返回首页">
          <font-awesome-icon :icon="['fas', 'arrow-left']" class="fa-arrow-left" />
        </button>
        <h1 class="board-title">
          <span class="icon">💬</span>
          社团留言板
        </h1>
        <p class="board-subtitle">分享你的想法，为社团注入新活力</p>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="board-main">
      <div class="container">
        <!-- 新增留言区域 -->
        <div class="add-message-container">
          <div class="add-message-form">
            <div class="content-input-wrapper">
              <textarea
                class="content-input"
                placeholder="请输入留言内容..."
                v-model="newMessage.content"
                rows="4"
              ></textarea>
            </div>
            <div class="bottom-row">
              <div class="left-section">
                <input
                  type="text"
                  class="nickname-input"
                  placeholder="请输入您的昵称"
                  v-model="newMessage.nickname"
                />
              </div>
              <div class="button-group">
                <button class="submit-btn" @click="handleAddMessage">
                  <el-icon :size="20"><Edit /></el-icon>
                  <span class="submit-text">发布留言</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 悬浮刷新按钮 -->
        <div class="floating-refresh" :class="{ refreshing: isRefreshing }">
          <button
            class="refresh-btn"
            @click="handleRefresh"
            :disabled="isRefreshing"
          >
            <el-icon :size="24"><Refresh /></el-icon>
          </button>
        </div>

        <!-- 加载状态 -->
        <div v-if="messageStore.loading" class="loading-container">
          <div class="loading-spinner"></div>
          <p class="loading-text">正在加载留言...</p>
        </div>

        <!-- 错误状态 -->
        <div v-else-if="messageStore.error" class="error-container">
          <div class="error-icon">⚠️</div>
          <p class="error-text">{{ messageStore.error }}</p>
        </div>

        <!-- 空数据状态 -->
        <div
          v-else-if="messageStore.messages.length === 0"
          class="empty-container"
        >
          <div class="empty-icon">📝</div>
          <p class="empty-text">暂无留言数据</p>
          <p class="empty-subtext">成为第一个留言的人吧！</p>
        </div>

        <!-- 留言列表 -->
        <div v-else class="messages-container">
          <div class="messages-grid">
            <article
              v-for="msg in messageStore.messages"
              :key="msg.id"
              class="message-card"
              :class="{ 'message-liked': msg.liked }"
            >
              <!-- 留言头部 -->
              <div class="message-header">
                <div class="user-info">
                  <div class="avatar">
                    {{ msg.nickname.charAt(0).toUpperCase() }}
                  </div>
                  <div class="user-details">
                    <h3 class="nickname">{{ msg.nickname }}</h3>
                    <time class="timestamp">{{
                      formatTime(msg.createTime)
                    }}</time>
                  </div>
                </div>
                <div class="message-actions">
                  <button
                    v-if="msg.canDelete"
                    @click="handleDelete(msg.id)"
                    class="delete-btn"
                    title="删除留言"
                    :disabled="likeLoading[msg.id]"
                  >
                    <span class="delete-icon">🗑️</span>
                  </button>
                </div>
              </div>

              <!-- 留言内容 -->
              <div class="message-content">
                <p
                  class="content-text"
                  :class="{ expanded: expandedMessages[msg.id] }"
                  :ref="(el) => (contentRefs[msg.id] = el as HTMLElement)"
                >
                  {{ msg.content }}
                </p>
                <!-- 展开/收起按钮 -->
                <button
                  v-if="showExpandButtons[msg.id]"
                  @click="toggleMessageExpansion(msg.id)"
                  class="expand-toggle-btn"
                >
                  {{ expandedMessages[msg.id] ? '收起' : '展开' }}
                  <el-icon :size="12" class="expand-icon">
                    <i
                      :class="
                        expandedMessages[msg.id]
                          ? 'fa fa-angle-up'
                          : 'fa fa-angle-down'
                      "
                    ></i>
                  </el-icon>
                </button>
              </div>

              <!-- 留言底部 -->
              <div class="message-footer">
                <div class="interaction-buttons">
                  <button
                    @click="handleLike(msg.id)"
                    class="like-btn"
                    :class="{ liked: msg.liked, loading: likeLoading[msg.id] }"
                    :title="msg.liked ? '取消点赞' : '点赞'"
                    :disabled="likeLoading[msg.id]"
                  >
                    <span class="like-icon" :class="{ liked: msg.liked }">
                      {{ msg.liked ? '❤️' : '🤍' }}
                    </span>
                    <span class="like-count">{{ msg.likeCount }}</span>
                  </button>
                </div>
              </div>
            </article>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <div class="pagination-info">
              <span class="total-count">共 {{ totalCount }} 条留言</span>
              <span class="page-info"
                >第 {{ currentPage }} 页，共 {{ totalPages }} 页</span
              >
            </div>
            <div class="pagination-controls">
              <button
                class="page-btn prev-btn"
                :class="{ disabled: currentPage <= 1 }"
                :disabled="currentPage <= 1"
                @click="handlePrevPage"
              >
                <span class="arrow">←</span>
                <span class="btn-text">上一页</span>
              </button>
              <div class="page-numbers">
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  class="page-number"
                  :class="{
                    active: page === currentPage,
                    ellipsis: page === '...',
                  }"
                  @click="handlePageChange(page)"
                  :disabled="page === '...'"
                >
                  {{ page }}
                </button>
              </div>
              <button
                class="page-btn next-btn"
                :class="{ disabled: currentPage >= totalPages }"
                :disabled="currentPage >= totalPages"
                @click="handleNextPage"
              >
                <span class="btn-text">下一页</span>
                <span class="arrow">→</span>
              </button>
            </div>
            <div class="page-jump">
              <span class="jump-text">跳转到</span>
              <input
                type="number"
                class="jump-input"
                v-model="jumpPage"
                :min="1"
                :max="totalPages"
                placeholder="页码"
              />
              <span class="jump-text">页</span>
              <button class="jump-btn" @click="handleJumpPage">确定</button>
            </div>
          </div>
        </div>
      </div>
    </main>
    <CommonFooter />
  </div>
</template>

<style scoped>
  /* 基础样式 */
  * {
    box-sizing: border-box;
  }
  body {
    overflow-x: hidden;
    margin: 0;
  }

  .message-board {
    min-height: 100vh;
    background-image: url('@/assets/images/messageBg.webp');
    background-size: cover;
    background-position: center;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
  }

  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 16px;
    width: 100%;
  }

  /* 头部样式 */
  .board-header {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    padding: 1rem 0;
    position: relative; /* 添加相对定位 */
    text-align: center;
  }

  /* 返回首页按钮样式 */
  .back-home-btn {
    position: absolute;
    left: 20px;
    top: 50%;
    transform: translateY(-50%);
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #333;
    cursor: pointer;
    transition: all 0.3s ease;
    z-index: 10;
  }

  .back-home-btn:hover {
    background: rgba(255, 255, 255, 0.4);
    transform: translateY(-50%) scale(1.1);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .back-home-btn i {
    font-size: 16px;
  }

  /* 响应式调整 */
  @media (max-width: 768px) {
    .board-title {
      font-size: 1.5rem;
      margin-left: 40px; /* 为按钮留出空间 */
    }

    .back-home-btn {
      width: 36px;
      height: 36px;
      left: 15px;
    }
  }

  .board-title {
    font-size: 1.8rem;
    font-weight: 700;
    color: rgb(15, 4, 4);
    margin: 0 0 0.3rem 0;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
  }

  .board-title .icon {
    font-size: 1.5rem;
  }

  .board-subtitle {
    font-size: 0.9rem;
    color: rgba(6, 11, 34, 1);
    margin: 0;
    font-weight: 300;
  }

  /* 主要内容区域 */
  .board-main {
    padding: 1.5rem 0;
  }

  /* 新增留言区域 */
  .add-message-container {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 16px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
  }

  .add-message-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .content-input-wrapper {
    width: 100%;
  }

  .nickname-input,
  .content-input {
    padding: 0.8rem 1rem;
    border: 1px solid rgba(0, 0, 0, 0.3);
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.9);
    color: #333;
    font-size: 1rem;
    font-family: inherit;
    resize: none;
    line-height: 1.5;
  }

  .content-input {
    width: 100%;
    min-height: 100px;
  }

  .nickname-input {
    height: 48px;
    display: flex;
    align-items: center;
  }

  .nickname-input:focus,
  .content-input:focus {
    outline: none;
    border-color: #667eea;
    background: #fff;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }

  .bottom-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 1rem;
    flex-wrap: wrap;
  }

  .left-section {
    flex: 1;
    min-width: 200px;
  }

  .button-group {
    display: flex;
    gap: 1rem;
  }

  .submit-btn {
    background: linear-gradient(135deg, #acb5e0, #e6deee);
    color: rgb(202, 99, 233);
    border: none;
    border-radius: 12px;
    padding: 0.8rem 1.5rem;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    font-size: 1rem;
    font-weight: 600;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  }

  .submit-btn:hover {
    background: linear-gradient(135deg, #764ba2, #667eea);
    transform: translateY(-2px);
    color: white;
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  }

  /* 悬浮刷新按钮 */
  .floating-refresh {
    position: fixed;
    bottom: 24px;
    right: 24px;
    transform: translateY(0);
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #667eea, #764ba2);
    border-radius: 50%;
    box-shadow: 0 6px 15px rgba(102, 126, 234, 0.3);
    transition: all 0.3s ease;
    opacity: 0.95;
  }

  .floating-refresh:hover {
    transform: scale(1.1);
    box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
  }

  .floating-refresh.refreshing {
    background: linear-gradient(135deg, #42b983, #27ae60);
  }

  .refresh-btn {
    border: none;
    background: transparent;
    color: white;
    cursor: pointer;
    padding: 0;
    margin: 0;
  }

  /* 加载/错误/空数据状态 */
  .loading-container,
  .error-container,
  .empty-container {
    text-align: center;
    padding: 4rem 0;
    color: white;
  }

  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 3px solid rgba(255, 255, 255, 0.3);
    border-top: 3px solid white;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin: 0 auto 1rem;
  }

  .error-icon,
  .empty-icon {
    font-size: 3rem;
    margin-bottom: 1rem;
  }

  .error-text {
    color: #ff6b6b;
    font-size: 1.1rem;
    margin: 0;
  }

  .empty-text {
    font-size: 1.3rem;
    margin: 0 0 0.5rem 0;
    font-weight: 500;
  }

  .empty-subtext {
    color: rgba(255, 255, 255, 0.7);
    font-size: 1rem;
    margin: 0;
  }

  /* 留言列表：移动端2列 */
  .messages-grid {
    display: grid;
    gap: 1rem;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    overflow-x: hidden;
  }

  /* 留言卡片：统一大小 */
  .message-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 16px;
    padding: 1.2rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
    width: 100%;
    max-width: 100%;
    display: flex;
    flex-direction: column;
  }

  .message-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  }

  .message-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(90deg, #667eea, #764ba2);
    opacity: 0;
    transition: opacity 0.3s ease;
    border-radius: 16px 16px 0 0;
  }

  .message-card:hover::before {
    opacity: 0.8;
  }

  .message-liked::before {
    background: linear-gradient(90deg, #ff6b6b, #ee5a24);
    opacity: 0.8;
  }

  /* 留言头部：固定布局 */
  .message-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 0.8rem;
    flex-shrink: 0;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 0.6rem;
    flex: 1;
    overflow: hidden;
  }

  .avatar {
    width: 40px;
    height: 40px;
    background: linear-gradient(135deg, #8d9ad7, #ae7de2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: 600;
    font-size: 1.1rem;
    flex-shrink: 0;
  }

  .user-details {
    display: flex;
    flex-direction: column;
    gap: 0.2rem;
    overflow: hidden;
  }

  .nickname {
    font-size: 1rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
    line-height: 1.2;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .timestamp {
    font-size: 0.8rem;
    color: #7f8c8d;
    margin: 0;
  }

  .message-actions {
    display: flex;
    gap: 0.5rem;
    flex-shrink: 0;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .delete-btn {
    background: rgba(231, 76, 60, 0.1);
    border: 1px solid rgba(231, 76, 60, 0.3);
    padding: 0.4rem;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
    color: #e74c3c;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .delete-btn:hover {
    background: rgba(231, 76, 60, 0.2);
    border-color: rgba(231, 76, 60, 0.6);
    transform: scale(1.05);
    box-shadow: 0 2px 8px rgba(231, 76, 60, 0.3);
  }

  .delete-icon {
    font-size: 1rem;
  }

  /* 留言内容 */
  .message-content {
    margin-bottom: 1rem;
    flex: 1;
    min-height: 60px;
  }

  .content-text {
    font-size: 0.95rem;
    line-height: 1.6;
    color: #34495e;
    margin: 0;
    word-wrap: break-word;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .content-text.expanded {
    -webkit-line-clamp: unset;
    line-clamp: unset;
  }

  .expand-toggle-btn {
    background: transparent;
    border: none;
    color: #667eea;
    cursor: pointer;
    font-size: 0.9rem;
    padding: 5px 0;
    display: flex;
    align-items: center;
    gap: 5px;
    margin-top: 5px;
    outline: none;
  }

  .expand-toggle-btn:hover {
    color: #5a67d8;
    text-decoration: underline;
  }

  .expand-icon {
    transition: transform 0.3s ease;
  }

  /* 留言底部：点赞按钮 */
  .message-footer {
    display: flex;
    justify-content: flex-end;
    flex-shrink: 0;
  }

  .like-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(102, 126, 234, 0.1);
    border: 2px solid rgba(102, 126, 234, 0.3);
    border-radius: 25px;
    padding: 0.4rem 0.8rem;
    cursor: pointer;
    transition: all 0.3s ease;
    color: #3d59d7;
    font-weight: 500;
    font-size: 0.85rem;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .like-btn:hover:not(.loading) {
    background: rgba(102, 126, 234, 0.2);
    border-color: rgba(102, 126, 234, 0.5);
    transform: translateY(-1px);
  }

  .like-btn.liked {
    background: rgba(255, 107, 107, 0.15);
    border-color: rgba(255, 107, 107, 0.4);
    color: #ff6b6b;
  }

  .like-btn.loading {
    opacity: 0.7;
    cursor: not-allowed;
    background: rgba(102, 126, 234, 0.05);
    border-color: rgba(102, 126, 234, 0.2);
  }

  .like-icon {
    font-size: 1rem;
    transition: transform 0.2s ease;
  }

  .like-btn:hover .like-icon {
    transform: scale(1.1);
  }

  .like-count {
    font-weight: 600;
  }

  /* 分页组件 */
  .pagination-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.8rem;
    margin-top: 1.5rem;
    padding: 0.8rem;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(0, 0, 0, 0.2);
    width: 100%;
  }

  .pagination-info {
    display: flex;
    align-items: center;
    gap: 0.8rem;
    color: black;
    font-size: 0.8rem;
    opacity: 0.9;
    flex-wrap: wrap;
    justify-content: center;
  }

  .pagination-controls {
    display: flex;
    align-items: center;
    gap: 0.3rem;
    color: black;
    font-size: 0.8rem;
    opacity: 0.9;
    flex-wrap: wrap;
    justify-content: center;
  }

  .page-btn {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 6px;
    padding: 0.3rem 0.6rem;
    cursor: pointer;
    transition: all 0.2s ease;
    display: flex;
    align-items: center;
    gap: 0.2rem;
    color: black;
    font-weight: 500;
    font-size: 0.8rem;
    min-height: auto;
  }

  .page-btn:hover:not(.disabled) {
    background: rgba(255, 255, 255, 0.2);
    border-color: rgba(255, 255, 255, 0.5);
  }

  .page-btn.disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .page-numbers {
    display: flex;
    gap: 0.2rem;
  }

  .page-number {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 6px;
    padding: 0.3rem 0.6rem;
    cursor: pointer;
    transition: all 0.2s ease;
    color: black;
    font-weight: 500;
    font-size: 0.8rem;
    min-height: auto;
    min-width: auto;
  }

  .page-number:hover:not(.active):not(.ellipsis) {
    background: rgba(255, 255, 255, 0.2);
    border-color: rgba(255, 255, 255, 0.5);
  }

  .page-number.active {
    background: rgba(255, 255, 255, 0.3);
    border-color: rgba(255, 255, 255, 0.6);
    color: #667eea;
    font-weight: 600;
  }

  .page-number.ellipsis {
    cursor: default;
    color: rgba(255, 255, 255, 0.7);
  }

  .page-jump {
    display: flex;
    align-items: center;
    gap: 0.3rem;
    color: black;
    font-size: 0.8rem;
    opacity: 0.9;
  }

  .jump-input {
    width: 40px;
    padding: 0.3rem;
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 6px;
    background: rgba(255, 255, 255, 0.9);
    color: #333;
    text-align: center;
    font-size: 0.8rem;
  }

  .jump-input:focus {
    outline: none;
    border-color: #667eea;
    background: #fff;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
  }

  .jump-btn {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 6px;
    padding: 0.3rem 0.6rem;
    cursor: pointer;
    transition: all 0.2s ease;
    color: black;
    font-weight: 500;
    font-size: 0.8rem;
  }

  .jump-btn:hover {
    background: rgba(255, 255, 255, 0.2);
    border-color: rgba(255, 255, 255, 0.5);
  }

  /* 下拉刷新指示器 - 移除CSS变量依赖，使用Vue样式绑定 */
  .pull-indicator {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background: linear-gradient(135deg, #667eea, #764ba2);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 0.85rem;
    font-weight: 500;
    z-index: 1000;
    transition: height 0.2s ease;
    pointer-events: none;
    line-height: 1;
  }

  .pull-indicator.release::before {
    content: '释放刷新';
    opacity: 1;
  }

  .pull-indicator.refreshing {
    background: linear-gradient(135deg, #42b983, #27ae60);
  }

  .pull-indicator.refreshing::before {
    content: '正在刷新...';
    opacity: 1;
  }

  /* 响应式适配：移动端2列 */
  @media (max-width: 768px) {
    .messages-grid {
      grid-template-columns: repeat(2, 1fr);
      gap: 0.8rem;
      padding: 0 0.2rem;
    }

    .message-card {
      padding: 1rem;
    }

    .avatar {
      width: 36px;
      height: 36px;
      font-size: 1rem;
    }

    .nickname {
      font-size: 0.9rem;
    }

    .content-text {
      font-size: 0.9rem;
      -webkit-line-clamp: 3;
      line-clamp: 3;
    }

    .like-btn {
      padding: 0.3rem 0.6rem;
      font-size: 0.8rem;
    }

    .like-icon {
      font-size: 0.9rem;
    }
  }

  /* 超小屏适配 */
  @media (max-width: 480px) {
    .messages-grid {
      gap: 0.6rem;
    }

    .message-card {
      padding: 0.9rem;
    }

    .content-text {
      font-size: 0.85rem;
    }

    .floating-refresh {
      width: 48px;
      height: 48px;
      bottom: 16px;
      right: 16px;
    }

    .refresh-btn el-icon {
      font-size: 1.4rem;
    }
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
</style>

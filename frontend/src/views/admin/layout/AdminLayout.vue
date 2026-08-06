<template>
  <div class="admin-container">
    <el-aside
      class="admin-aside"
      :width="isMobile ? (isMenuCollapsed ? '0' : '200px') : '200px'"
      :class="{ collapsed: isMobile && isMenuCollapsed }"
    >
      <div class="sidebar-brand" @click="router.push('/')" aria-label="返回前台首页">
        <i-fa6-solid-code class="brand-icon" /><span>智环学创融合协会</span>
      </div>
      <el-menu
        :default-active="currentRoutePath"
        class="el-menu-vertical-demo"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/admin/dashboard">
          <template #title>
            <el-icon><i-ep-house /></el-icon>
            <span>后台首页</span>
          </template>
        </el-menu-item>
        <el-menu-item v-if="!isSubmitter" index="/admin/applications">
          <template #title>
            <el-icon><i-ep-user /></el-icon>
            <span>报名信息管理</span>
          </template>
        </el-menu-item>
        <el-menu-item v-if="!isSubmitter" index="/admin/members">
          <template #title>
            <el-icon><i-ep-user-filled /></el-icon>
            <span>成员管理</span>
          </template>
        </el-menu-item>
        <el-menu-item v-if="!isSubmitter" index="/admin/messages">
          <template #title>
            <el-icon><i-ep-message /></el-icon>
            <span>留言板管理</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/admin/awards">
          <template #title>
            <el-icon><i-ep-trophy /></el-icon>
            <span>奖项管理</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/admin/projects">
          <template #title>
            <el-icon><i-ep-box /></el-icon>
            <span>项目管理</span>
          </template>
        </el-menu-item>
        <el-menu-item v-if="!isSubmitter" index="/admin/attachments">
          <template #title>
            <el-icon><i-ep-picture /></el-icon>
            <span>附件库</span>
          </template>
        </el-menu-item>
        <el-menu-item v-if="adminStore.isSuperAdmin()" index="/admin/admins">
          <template #title>
            <el-icon><i-ep-key /></el-icon>
            <span>管理员管理</span>
          </template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <div class="admin-content">
      <el-header class="admin-header">
        <el-icon class="menu-toggle" @click="toggleMenu" v-if="isMobile">
          <i-ep-expand v-if="isMenuCollapsed" />
          <i-ep-fold v-else />
        </el-icon>
        <div class="header-right">
        <el-dropdown
          :trigger="isMobile ? 'click' : 'hover'"
          @command="handleCommand"
        >
          <div class="user-info">
            <el-icon class="user-icon"><i-ep-user /></el-icon>
            <span class="username" v-if="!isMobile || !isMenuCollapsed">{{
              userInfo?.username
            }}</span>
            <el-icon class="dropdown-icon"><i-ep-arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-if="!isSubmitter" command="profile">
                <el-icon><i-ep-user /></el-icon>
                个人资料
              </el-dropdown-item>
              <el-dropdown-item command="home">
                <el-icon><i-ep-house /></el-icon>
                返回前台
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><i-ep-switch-button /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        </div>
      </el-header>
      <el-main class="admin-main">
        <router-view></router-view>
      </el-main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useAdminStore } from "@/stores/adminStore";
import { ref, onMounted, computed, onBeforeUnmount } from "vue";
import { ElMessage } from "element-plus";
import { logout as logoutApi } from "@/services/adminService";

const router = useRouter();
const adminStore = useAdminStore();
const isMenuCollapsed = ref(false);
const isMobile = ref(false);
const userInfo = computed(() => adminStore.userInfo);
const isSubmitter = computed(() => adminStore.hasRole(["submitter"]));

/**
 * 根据屏幕宽度切换菜单折叠策略，避免移动端侧栏遮挡主体内容。
 */
const checkIsMobile = () => {
	isMobile.value = window.innerWidth <= 768;
	if (isMobile.value) {
		isMenuCollapsed.value = true;
	} else {
		isMenuCollapsed.value = false;
	}
};

/**
 * 切换移动端菜单展开状态。
 */
const toggleMenu = () => {
	isMenuCollapsed.value = !isMenuCollapsed.value;
};

/**
 * 响应窗口尺寸变化，保持菜单状态与当前设备形态一致。
 */
const handleResize = () => {
	checkIsMobile();
};

onMounted(() => {
	checkIsMobile();
	window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
	window.removeEventListener("resize", handleResize);
});

/**
 * 处理侧边栏导航，并在移动端点击后自动收起菜单。
 *
 * @param {string} key
 */
const handleMenuSelect = (key) => {
	router.push(key);
	if (isMobile.value) {
		isMenuCollapsed.value = true;
	}
};

/**
 * 处理右上角下拉菜单命令。
 *
 * @param {string} command
 */
const handleCommand = (command) => {
	switch (command) {
		case "profile":
			router.push("/admin/profile");
			break;
		case "home":
			router.push("/");
			break;
		case "logout":
			handleLogout();
			break;
	}
};

/**
 * 退出登录时先清理本地状态，再异步通知后端，避免页面停留在无效登录态。
 */
const handleLogout = () => {
	logoutApi().catch((error) => {
		console.warn("后端登出接口调用失败:", error);
	});

	adminStore.logout();
	ElMessage.success("退出登录成功");
	router.push("/admin/login");
};

const currentRoutePath = computed(() => router.currentRoute.value.path);
</script>

<style scoped>
   .admin-container {
     display: flex;
     height: 100vh;
     --el-color-primary: #3b82f6;
  }
  .admin-header {
    background: #fff;
    border-bottom: 1px solid #e5e7eb;
    color: #1e293b;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
     height: 60px;
     flex: none;
     position: sticky;
     top: 0;
     z-index: 20;
  }
  .menu-toggle {
    font-size: 24px;
    margin-right: 15px;
    cursor: pointer;
    display: none;
  }
   .sidebar-brand {
     display: flex;
     align-items: center;
     gap: 6px;
     height: 60px;
     padding: 0 14px;
     font-size: 17px;
     font-weight: 700;
     color: #fff;
    white-space: nowrap;
    cursor: pointer;
    transition: transform 0.2s ease;
  }
   .sidebar-brand:hover {
     transform: scale(1.05);
  }
   .brand-icon {
     flex: none;
     transition: all 0.3s ease;
  }
   .sidebar-brand:hover .brand-icon {
     transform: rotate(10deg);
     color: #93c5fd;
  }
  .header-right {
    display: flex;
    align-items: center;
  }
  .user-info {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    background: #fff;
    border: 1px solid #e5e7eb;
  }
  .user-info:hover {
    background: #f8fafc;
    border-color: #d1d5db;
    transform: translateY(-1px);
  }
  .user-icon {
    font-size: 18px;
    margin-right: 8px;
    color: #64748b;
  }
  .username {
    font-size: 14px;
    font-weight: 500;
    margin-right: 4px;
    color: #1e293b;
    white-space: nowrap;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .dropdown-icon {
    font-size: 12px;
    transition: transform 0.3s ease;
    color: #64748b;
  }
  .el-dropdown:hover .dropdown-icon {
    transform: rotate(180deg);
  }
   .admin-aside {
     flex: none;
     background-color: #304156;
    color: white;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
    transition: width 0.3s ease;
    overflow: hidden;
  }
  .admin-aside.collapsed {
    width: 0;
  }
   .el-menu-vertical-demo {
    background-color: #304156;
    color: white;
    border-right: none;
     height: calc(100% - 60px);
  }
  .el-menu-item {
    color: #bfcbd9;
    transition: all 0.3s ease;
    border-radius: 4px;
    margin: 2px 8px;
  }
  .el-menu-item:hover {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
   .el-menu-item.is-active {
     background: #1f2d3d;
     border-left: 3px solid #3b82f6;
     color: white;
     font-weight: 500;
  }
   .admin-main {
     flex: 1;
    padding: 24px;
    overflow-y: auto;
    background-color: #f5f7fa;
     min-height: 0;
   }
   .admin-content { display: flex; min-width: 0; flex: 1; flex-direction: column; }

  /* 移动端样式 */
  @media (max-width: 768px) {
    .menu-toggle {
      display: block;
    }

     .admin-header {
       justify-content: space-between;
      padding: 0 15px;
      height: 50px;
    }

    .user-icon {
      font-size: 16px;
      margin-right: 5px;
    }

    .username {
      font-size: 13px;
      max-width: 80px;
    }

    .user-info {
      padding: 6px 10px;
    }

    .admin-main {
      padding: 15px;
    }

     .admin-aside {
       position: fixed;
       top: 0;
       left: 0;
       z-index: 1000;
       height: 100vh;
      transition: width 0.3s ease;
    }
  }

  @media (max-width: 480px) {
    .admin-header {
      padding: 0 10px;
    }

    .username {
      max-width: 60px;
    }

    .admin-main {
      padding: 10px;
    }
  }
</style>

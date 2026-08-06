import { defineStore } from "pinia";
import { computed, ref } from "vue";
import api from "@/services/api";

export const useAdminStore = defineStore("admin", () => {
	const userInfo = ref(null);
	const token = ref(localStorage.getItem("adminToken"));
	const isLoading = ref(false);
	const initialized = ref(false);
	let initializePromise = null;

	// 本地凭证必须经过后端校验后才视为已登录。
	const isLoggedIn = computed(() => Boolean(token.value && userInfo.value));

	/**
	 * 写入当前登录用户与 token，保证刷新后仍可恢复登录态。
	 */
	const login = (userData, credential) => {
		userInfo.value = userData;
		if (credential) {
			localStorage.setItem("adminToken", credential);
			token.value = credential;
		}
		initialized.value = true;
	};

	/**
	 * 清除认证状态，统一处理本地登录信息回收。
	 */
	const clearAuthState = () => {
		userInfo.value = null;
		token.value = null;
		initialized.value = true;
		localStorage.removeItem("adminToken");
	};

	/**
	 * 退出登录，仅负责清理状态，路由跳转留给页面层处理。
	 */
	const logout = () => {
		clearAuthState();
	};

	/**
	 * 静默拉取当前用户信息，失败时回收无效登录态。
	 */
	const fetchUserInfo = async () => {
		try {
			const response = await api.get("/admin/auth/me");
			userInfo.value = response.data.data;
			return true;
		} catch (error) {
			clearAuthState();
			throw error;
		}
	};

	/**
	 * 主动校验当前登录状态，避免重复并发请求。
	 */
	const initialize = () => {
		if (initialized.value) return Promise.resolve(isLoggedIn.value);
		if (initializePromise) return initializePromise;
		if (!token.value) {
			initialized.value = true;
			return Promise.resolve(false);
		}

		isLoading.value = true;
		initializePromise = fetchUserInfo()
			.then(() => true)
			.catch(() => false)
			.finally(() => {
				isLoading.value = false;
				initialized.value = true;
				initializePromise = null;
			});
		return initializePromise;
	};

	/**
	 * 判断当前用户是否具备超级管理员权限。
	 */
	const isSuperAdmin = () => {
		return userInfo.value?.role === "super";
	};

	/**
	 * 判断当前用户是否具有指定角色之一。
	 */
	const hasRole = (roles) => {
		return roles.includes(userInfo.value?.role);
	};

	return {
		isLoggedIn,
		token,
		userInfo,
		isLoading,
		initialized,
		fetchUserInfo,
		login,
		logout,
		clearAuthState,
		initialize,
		isSuperAdmin,
		hasRole,
	};
});

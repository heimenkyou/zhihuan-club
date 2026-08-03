import { computed, onBeforeUnmount, onMounted, ref } from "vue";

/** 返回后台页面共用的移动端视口状态。 */
export const useAdminMobile = () => {
	const viewportWidth = ref(window.innerWidth);
	const updateViewportWidth = () => {
		viewportWidth.value = window.innerWidth;
	};

	onMounted(() => window.addEventListener("resize", updateViewportWidth));
	onBeforeUnmount(() =>
		window.removeEventListener("resize", updateViewportWidth),
	);

	return { isMobile: computed(() => viewportWidth.value <= 768) };
};

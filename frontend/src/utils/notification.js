import { ElNotification } from "element-plus";

/**
 * 统一封装通知弹窗，保证不同入口的提示风格一致。
 */
export const showNotification = (
	type,
	message,
	title,
	duration = type === "error" ? 3000 : 2000,
) => {
	ElNotification({
		title:
			title ||
			(type === "success"
				? "操作成功"
				: type === "error"
					? "错误"
					: type === "warning"
						? "提示"
						: "信息"),
		message,
		type,
		duration,
		position: "top-right",
		customClass: "app-notification",
	});
};

/**
 * 显示成功提示，保持常规成功反馈时长。
 */
export const showSuccess = (message) => {
	showNotification("success", message, "操作成功", 2000);
};

/**
 * 显示错误提示，统一错误提示文案出口。
 */
export const showError = (message) => {
	showNotification("error", message, "错误", 2000);
};

/**
 * 显示警告提示，用于不阻断流程的提醒场景。
 */
export const showWarning = (message) => {
	showNotification("warning", message, "提示", 2000);
};

/**
 * 显示信息提示，用于一般性通知。
 */
export const showInfo = (message) => {
	showNotification("info", message, "信息", 2000);
};

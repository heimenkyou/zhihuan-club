import * as qiniu from "qiniu-js";
import api from "@/services/api";

const getData = (response) => response.data.data;

/**
 * 上传图片到七牛云并确认附件记录。
 *
 * @param {File} file 图片文件
 * @param {(percent: number) => void} [onProgress] 上传进度回调
 * @returns {Promise<Object>} 已就绪附件
 */
export const uploadImage = async (file, onProgress) => {
	if (!file.type.startsWith("image/")) {
		throw new Error("只允许上传图片");
	}
	if (file.size > 10 * 1024 * 1024) {
		throw new Error("图片大小不能超过 10MB");
	}

	const credential = getData(
		await api.post("/admin/attachments/upload-token", {
			originalName: file.name,
			mimeType: file.type,
			size: file.size,
		}),
	);

	await new Promise((resolve, reject) => {
		qiniu
			.upload(
				file,
				credential.key,
				credential.token,
				{ fname: file.name },
				{ retryCount: 2 },
			)
			.subscribe({
				next: (result) => onProgress?.(Math.round(result.total.percent)),
				error: (error) => reject(new Error(error.message || "上传七牛云失败")),
				complete: resolve,
			});
	});

	return getData(
		await api.post(`/admin/attachments/${credential.id}/complete`),
	);
};

/**
 * 分页查询附件库。
 *
 * @param {{ current?: number, size?: number }} [params]
 * @returns {Promise<Object>}
 */
export const getAttachments = async (params) => {
	return getData(
		await api.get("/admin/attachments", {
			params: { current: params?.current, size: params?.size },
		}),
	);
};

/**
 * 删除图片附件。
 *
 * @param {number} id 附件 ID
 * @returns {Promise<void>}
 */
export const deleteAttachment = async (id) => {
	await api.delete(`/admin/attachments/${id}`);
};

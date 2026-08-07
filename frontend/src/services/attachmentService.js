import * as qiniu from "qiniu-js";
import api from "@/services/api";

const MAX_IMAGE_SIZE = 1024 * 1024;
const WEBP_QUALITY = 0.9;
const COMPRESSED_WEBP_QUALITY = 0.82;
const getData = (response) => response.data.data;

/**
 * 将图片加载到画布，统一后续格式转换与压缩流程。
 *
 * @param {File} file 原始图片
 * @returns {Promise<HTMLCanvasElement>}
 */
const createImageCanvas = async (file) => {
	const imageUrl = URL.createObjectURL(file);
	try {
		const image = await new Promise((resolve, reject) => {
			const element = new Image();
			element.onload = () => resolve(element);
			element.onerror = () => reject(new Error("图片无法读取"));
			element.src = imageUrl;
		});
		const canvas = document.createElement("canvas");
		canvas.width = image.naturalWidth;
		canvas.height = image.naturalHeight;
		const context = canvas.getContext("2d");
		if (!context) throw new Error("浏览器不支持图片处理");
		context.drawImage(image, 0, 0);
		return canvas;
	} finally {
		URL.revokeObjectURL(imageUrl);
	}
};

/**
 * 导出 WebP 图片。
 *
 * @param {HTMLCanvasElement} canvas 图片画布
 * @param {number} quality WebP 质量
 * @returns {Promise<Blob>}
 */
const exportWebp = (canvas, quality) =>
	new Promise((resolve, reject) => {
		canvas.toBlob(
			(blob) => {
				if (blob) resolve(blob);
				else reject(new Error("浏览器不支持图片转换"));
			},
			"image/webp",
			quality,
		);
	});

/**
 * 为转换后的 WebP 文件生成名称。
 *
 * @param {string} name 原文件名
 * @returns {string}
 */
const getWebpName = (name) => {
	const extensionIndex = name.lastIndexOf(".");
	const baseName = extensionIndex > 0 ? name.slice(0, extensionIndex) : name;
	return `${baseName || "image"}.webp`;
};

/**
 * 在申请上传凭证前完成 WebP 转换与大小校验。
 *
 * @param {File} file 原始图片
 * @returns {Promise<File>} 可上传图片
 */
export const prepareImage = async (file) => {
	if (!file.type.startsWith("image/")) {
		throw new Error("只允许上传图片");
	}

	const canvas = await createImageCanvas(file);
	const webpFile = new File(
		[await exportWebp(canvas, WEBP_QUALITY)],
		getWebpName(file.name),
		{ type: "image/webp" },
	);
	const candidate = webpFile.size < file.size ? webpFile : file;
	if (candidate.size <= MAX_IMAGE_SIZE) {
		return candidate;
	}

	const compressedFile = new File(
		[await exportWebp(canvas, COMPRESSED_WEBP_QUALITY)],
		getWebpName(file.name),
		{ type: "image/webp" },
	);
	if (compressedFile.size <= MAX_IMAGE_SIZE) {
		return compressedFile;
	}
	throw new Error("图片处理后仍超过 1MB，请选择更小的图片");
};

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
	if (file.size > MAX_IMAGE_SIZE) {
		throw new Error("图片大小不能超过 1MB");
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
				{ fname: file.name, mimeType: file.type },
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

/**
 * 修改附件名称。
 *
 * @param {number} id 附件 ID
 * @param {string} originalName 新名称
 * @returns {Promise<Object>} 更新后的附件
 */
export const renameAttachment = async (id, originalName) => {
	return getData(await api.put(`/admin/attachments/${id}`, { originalName }));
};

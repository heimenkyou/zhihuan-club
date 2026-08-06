import api from "@/services/api";

/**
 * 读取接口响应体。
 *
 * @param {import('axios').AxiosResponse<any>=} response
 * @returns {any}
 */
const getBody = (response) => response?.data;

/**
 * 读取标准响应体中的 data 字段。
 *
 * @param {any} body
 * @returns {any}
 */
const getResultData = (body) => {
	if (!body || typeof body !== "object") {
		return undefined;
	}
	return body.data;
};

/**
 * 获取前台首页高光列表。
 *
 * @returns {Promise<any[]>}
 */
export const getHomepageHighlights = async () => {
	const response = await api.get("/homepage/highlights");
	return getResultData(getBody(response)) ?? [];
};

/**
 * 获取后台首页高光列表。
 *
 * @returns {Promise<any[]>}
 */
export const getAdminHomepageHighlights = async () => {
	const response = await api.get("/admin/homepage-highlights");
	return getResultData(getBody(response)) ?? [];
};

/**
 * 添加项目高光。
 *
 * @param {{ projectId: number }} params
 * @returns {Promise<void>}
 */
export const addProjectHighlight = async (params) => {
	await api.post("/admin/homepage-highlights/project", params);
};

/**
 * 添加活动高光。
 *
 * @param {Object} params
 * @returns {Promise<void>}
 */
export const addActivityHighlight = async (params) => {
	await api.post("/admin/homepage-highlights/activity", params);
};

/**
 * 更新活动高光。
 *
 * @param {number} id
 * @param {Object} params
 * @returns {Promise<void>}
 */
export const updateActivityHighlight = async (id, params) => {
	await api.put(`/admin/homepage-highlights/activity/${id}`, params);
};

/**
 * 全量重排首页高光。
 *
 * @param {number[]} ids
 * @returns {Promise<void>}
 */
export const reorderHighlights = async (ids) => {
	await api.put("/admin/homepage-highlights/order", { ids });
};

/**
 * 删除首页高光。
 *
 * @param {number} id
 * @returns {Promise<void>}
 */
export const deleteHighlight = async (id) => {
	await api.delete(`/admin/homepage-highlights/${id}`);
};

package cn.luowb.clubrecruitment.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页返回数据模型
 *
 * @param <T> 前端需要的记录类型（通常是 RespDTO/VO）
 */
@Getter
public final class PageData<T> implements Serializable {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private final long current;
    /**
     * 每页大小
     */
    @Schema(description = "每页大小")
    private final long size;
    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private final long total;
    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private final long pages;
    /**
     * 当前页数据
     */
    @Schema(description = "当前页数据")
    private final List<T> records;

    private PageData(long current, long size, long total, long pages, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = pages;
        this.records = records;
    }

    /**
     * 从 MyBatis-Plus 的分页对象构造（已转换好的记录）
     */
    public static <T> PageData<T> of(IPage<?> page, List<T> records) {
        return new PageData<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                records == null ? Collections.emptyList() : records
        );
    }

    /**
     * 从 MyBatis-Plus 的分页对象构造（自动映射 Entity -> DTO）
     */
    public static <E, T> PageData<T> of(IPage<E> page, Function<E, T> mapper) {
        List<T> mappedRecords = page.getRecords() == null
                ? Collections.emptyList()
                : page.getRecords().stream().map(mapper).collect(Collectors.toList());

        return new PageData<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                mappedRecords
        );
    }

    /**
     * 创建一个空分页
     */
    public static <T> PageData<T> empty(long current, long size) {
        return new PageData<>(current, size, 0, 0, Collections.emptyList());
    }
}
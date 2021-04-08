/**
 * @file:  CommonPage.java
 * @author: liang_xiaojian
 * @date:   2020/9/11 20:49
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
package com.xuexiang.chh_healthy_android.core.http.entity;

import java.util.List;

/**
 * @class CommonPage
 * @classdesc 通用分页封装，和 CommonResponse 搭配用法：
 * <pre>
 *     // 下面这种是本地服务, 需要开启分页后才会生效
 *     @PostMapping("xxx")
 *     CommonResponse<CommonPage<xxxVO>> listXxx(@Valid @RequestBody CommonRequest<xxxQuery> request) {
 *         List<xxxDTO> dtoList = xxxService.listXxx(request.getBody);
 *         List<xxxVO> voList = BeanUtil.copyList(dtoList, xxxVo.class);
 *         CommonPage<xxxVO> result = CommonPage.restPage(voList);
 *         return CommonResponseUtils.success(result);
 *     }
 *     // 以下这种是远程服务, xxxService 为远程服务接口
 *     @PostMapping("xxx")
 *     CommonResponse<CommonPage<xxxVO>> listXxx(@Valid @RequestBody CommonRequest<xxxQuery> request) {
 *         CommonResponse<CommonPage<xxxDTO>> response = xxxService.listXxx(request);
 *         CommonPage<xxxDTO> commonPage = response.getBody();
 *         List<xxxDTO> dtoList = commonPage.getData();
 *         List<xxxVO> voList = BeanUtil.copyList(dtoList, xxxVo.class);
 *         CommonPage<xxxVO> result = CommonPage.restPage(commonPage);
 *         result.setData(voList);
 *         return CommonResponseUtils.success(result);
 *     }
 * </pre>
 * @author liang_xiaojian
 * @date 2020/9/11  20:49
 * @version 1.0.0
 * @see
 * @since
 */
public class CommonPage<T> {

    /**
     * 总记录数
     */
    private Long total;
    /**
     * 页号
     */
    private Integer pageNum;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 数据列表
     */
    private List<T> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public CommonPage() {
    }

    public CommonPage(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = list;
    }

    public CommonPage(CommonPage<T> commonPage) {
        this.total = commonPage.getTotal();
        this.pageNum = commonPage.getPageNum();
        this.pageSize = commonPage.getPageSize();
        this.data = null;
    }
}

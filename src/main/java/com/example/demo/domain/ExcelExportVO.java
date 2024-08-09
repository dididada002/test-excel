package com.example.demo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.util.List;

/**
 * @author: jingteng
 * @date: 2023/12/8 18:17
 */
@Data
public class ExcelExportVO {

    @Excel(name = "用户id", needMerge = true, width = 20,height = 8)
    private String userId;

    @Excel(name = "是否属于测试数据", needMerge = true, width = 20,height = 8)
    private String testData;

    @Excel(name = "支付时间", needMerge = true, width = 20,height = 8)
    private String createTime;

    @Excel(name = "端", needMerge = true, width = 20,height = 8)
    private String platform;

    @Excel(name = "订单号", needMerge = true, width = 20,height = 8)
    private String orderSn;

    @Excel(name = "金额（分）", needMerge = true, width = 20,height = 8)
    private Integer amount;

    @Excel(name = "充值获得小易豆", needMerge = true, width = 20,height = 8)
    private Integer count;

    @Excel(name = "已使用的小易豆数量", needMerge = true, width = 20,height = 8)
    private Integer useCount;

    @Excel(name = "支付渠道", needMerge = true, width = 20,height = 8)
    private String paymentWay;

    @ExcelCollection(name = "使用小易豆细则")
    private List<NumUseItemDTO> itemList;
}

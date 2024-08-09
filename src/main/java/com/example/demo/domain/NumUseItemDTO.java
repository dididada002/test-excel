package com.example.demo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: jingteng
 * @date: 2023/12/8 15:46
 */
@Data
public class NumUseItemDTO {


    @Excel(name = "使用时间", needMerge = true, width = 20,height = 8)
    private String useTime;

    @Excel(name = "使用数量", needMerge = true, width = 20,height = 8)
    private Integer count;

    private String id;
}

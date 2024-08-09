package com.example.demo.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatNumRecordOfPersonBuyBakEntity {

    /**
     * uuid(按照时间戳生成的uuid)
     */
    private String id;

    /**
     * 用户ID
     */
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 订单编号
     */
    @JSONField(name = "order_sn")
    private String orderSn;

    /**
     * 小易豆数量
     */
    @JSONField(name = "count")
    private Integer count;

    /**
     * 金额，单位是分
     */
    @JSONField(name = "amount")
    private Integer amount;

    /**
     * 客户端类型
     */
    @JSONField(name = "client_type")
    private String clientType;

    /**
     * 支付方式
     */
    @JSONField(name = "payment_way")
    private String paymentWay;

    /**
     * 支付时间
     */
    @JSONField(name = "pay_time")
    private String payTime;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private String createTime;

    private ChatNumRecordOfPersonUseBak chatNumRecordOfPersonUseBak;
}

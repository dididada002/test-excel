package com.example.demo.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: jingteng
 * @date: 2023/4/18 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatNumRecordOfPersonUseBak {

    /**
     * uuid(按照时间戳生成的uuid)
     */
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "num_type")
    private Integer numType;

    @JSONField(name = "task_type")
    private Integer taskType;

    @JSONField(name = "chat_used_count")
    private Integer chatUsedCount;

    /**
     * 端侧来源
     */
    @JSONField(name = "platform")
    private String platform;

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "create_time")
    private String createTime;

    private Integer useCount;

    private List<NumUseItemDTO> useHistory;

    @JSONField(name = "create_date")
    private String createDate;

    @JSONField(name = "update_time")
    private String updateTime;

    private boolean buyOrder;
}

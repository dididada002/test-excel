package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.ChatNumRecordOfPersonBuyBakEntity;
import com.example.demo.domain.ChatNumRecordOfPersonUseBak;
import com.example.demo.domain.ExcelExportVO;
import com.example.demo.domain.NumUseItemDTO;
import com.example.demo.util.MyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author: jingteng
 * @date: 2024/8/9 14:34
 */

@Slf4j
@RestController
@RequestMapping("/availableTimes")
public class DemoController {


    @PostMapping("/export/buy/records")
    public void exportBuyRecords(@RequestParam("file") MultipartFile file,
                                 @RequestParam("buyFile") MultipartFile buyFile,
                                 @RequestParam("testOrderFile") MultipartFile testOrderFile,
                                 @RequestParam String month, HttpServletResponse response) {
        Map<String, List<ChatNumRecordOfPersonUseBak>> userListMap = new HashMap<>();
        Set<String> uidSet = new TreeSet<>();
        Map<String, Integer> lastMonthCanUse = new HashMap<>();
        Set<String> orderTestSet = new TreeSet<>();
        try {
            BufferedReader reader0 = new BufferedReader(new InputStreamReader(testOrderFile.getInputStream()));
            String line0;
            while ((line0 = reader0.readLine()) != null) {
                String orderTest = line0;
                if (StringUtils.isNotBlank(orderTest)) {
                    orderTestSet.add(orderTest);
                }
            }
            List<ChatNumRecordOfPersonUseBak> personList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ChatNumRecordOfPersonUseBak gptVoByStr = JSON.parseObject(line, ChatNumRecordOfPersonUseBak.class);
                personList.add(gptVoByStr);
            }
            Collections.sort(personList, new Comparator<ChatNumRecordOfPersonUseBak>() {
                @Override
                public int compare(ChatNumRecordOfPersonUseBak p1, ChatNumRecordOfPersonUseBak p2) {
                    // 这里假设 createTime 是 ISO 8601 格式的日期字符串
                    return p1.getCreateTime().compareTo(p2.getCreateTime());
                }
            });
            personList.forEach(item -> {
                if (item.getTaskType() != null && item.getTaskType() == 8) {
                    uidSet.add(item.getUserId());
                }
                List<ChatNumRecordOfPersonUseBak> chatNumRecordOfPersonUseBaks;
                if (userListMap.containsKey(item.getUserId())) {
                    chatNumRecordOfPersonUseBaks = userListMap.get(item.getUserId());
                    chatNumRecordOfPersonUseBaks.add(item);
                }else {
                    chatNumRecordOfPersonUseBaks = new ArrayList<>();
                    chatNumRecordOfPersonUseBaks.add(item);
                    userListMap.put(item.getUserId(), chatNumRecordOfPersonUseBaks);
                }
            });

            //2 开始回溯
            uidSet.forEach(item -> {
                List<ChatNumRecordOfPersonUseBak> chatNumRecordOfPersonUseBaks = userListMap.get(item);
                for (int i = 0; i < chatNumRecordOfPersonUseBaks.size(); i++) {
                    ChatNumRecordOfPersonUseBak chatNumRecordOfPersonUseBak = chatNumRecordOfPersonUseBaks.get(i);
                    if (chatNumRecordOfPersonUseBak.getNumType() == 1) {
                        updateUseCount(chatNumRecordOfPersonUseBaks, i, chatNumRecordOfPersonUseBak.getChatUsedCount(), chatNumRecordOfPersonUseBak);
                    }
                }
                System.out.println(123);
            });
            uidSet.forEach(item -> {
                List<ChatNumRecordOfPersonUseBak> chatNumRecordOfPersonUseBaks = userListMap.get(item);
                log.info("chatNumRecordOfPersonUseBaks:{}", chatNumRecordOfPersonUseBaks);
            });


            List<ChatNumRecordOfPersonBuyBakEntity> buyList = new ArrayList<>();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(buyFile.getInputStream()));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                ChatNumRecordOfPersonBuyBakEntity buyBakEntity = JSON.parseObject(line1, ChatNumRecordOfPersonBuyBakEntity.class);
                buyList.add(buyBakEntity);
            }
            buyList.forEach(item -> {
                List<ChatNumRecordOfPersonUseBak> chatNumRecordOfPersonUseBaks = userListMap.get(item.getUserId());
                for (int i = 0; i < chatNumRecordOfPersonUseBaks.size(); i++) {
                    ChatNumRecordOfPersonUseBak chatNumRecordOfPersonUseBak = chatNumRecordOfPersonUseBaks.get(i);
                    if (chatNumRecordOfPersonUseBak.getTaskType() != null
                            && chatNumRecordOfPersonUseBak.getTaskType() == 8
                            && (chatNumRecordOfPersonUseBak.getCreateTime().equals(item.getCreateTime()) || chatNumRecordOfPersonUseBak.getCreateTime().equals(item.getPayTime()))) {
                        item.setChatNumRecordOfPersonUseBak(chatNumRecordOfPersonUseBak);
                        chatNumRecordOfPersonUseBak.setBuyOrder(true);
                        return;
                    }
                }
            });
            List<ExcelExportVO> result = new ArrayList<>();
            buyList.forEach(buyBakEntity -> {
                if (buyBakEntity.getChatNumRecordOfPersonUseBak() == null) {
                    log.info("getChatNumRecordOfPersonUseBak null item :{}", buyBakEntity);
                    return;
                }
                ExcelExportVO excelExportVO = new ExcelExportVO();
                excelExportVO.setUserId(buyBakEntity.getUserId());
                excelExportVO.setCreateTime(buyBakEntity.getPayTime());
                excelExportVO.setPlatform(buyBakEntity.getChatNumRecordOfPersonUseBak().getPlatform());
                excelExportVO.setOrderSn(buyBakEntity.getOrderSn());
                excelExportVO.setAmount(buyBakEntity.getAmount());
                excelExportVO.setCount(buyBakEntity.getCount());
                excelExportVO.setUseCount(buyBakEntity.getChatNumRecordOfPersonUseBak().getUseCount());
                excelExportVO.setPaymentWay(buyBakEntity.getPaymentWay());
                excelExportVO.setItemList(buyBakEntity.getChatNumRecordOfPersonUseBak().getUseHistory());
                if (orderTestSet.contains(buyBakEntity.getOrderSn())) {
                    excelExportVO.setTestData("是");
                }else {
                    excelExportVO.setTestData("否");
                }
                result.add(excelExportVO);
            });

            //1、这个用户这个月充值多少
            //2 这个用户这个月消耗多少
            System.out.println(123);
            MyExcelUtils.exportExcel(result,"小易豆充值和使用明细","sheet1",ExcelExportVO.class, "test.xls", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //1 找到所有待统计用户
        //2 按月回溯扣减豆子的记录、
        //3 用户获取和使用记录回溯
        //4 统计
    }


    private void updateUseCount(List<ChatNumRecordOfPersonUseBak> chatNumRecordOfPersonUseBaks, int maxIndex, Integer chatUsedCount, ChatNumRecordOfPersonUseBak useBak) {
        //从0开始扣减
        for (int i = 0; i < maxIndex; i++) {
            ChatNumRecordOfPersonUseBak chatNumRecordOfPersonUseBak = chatNumRecordOfPersonUseBaks.get(i);
            if (chatNumRecordOfPersonUseBak.getUseCount() == null) {
                chatNumRecordOfPersonUseBak.setUseCount(0);
            }
            if (chatNumRecordOfPersonUseBak.getNumType() == 0
                    && (chatNumRecordOfPersonUseBak.getUseCount() < chatNumRecordOfPersonUseBak.getChatUsedCount())) {
                Integer restCount = chatNumRecordOfPersonUseBak.getChatUsedCount() - chatNumRecordOfPersonUseBak.getUseCount();
                if (restCount >= chatUsedCount) {
                    chatNumRecordOfPersonUseBak.setUseCount(chatNumRecordOfPersonUseBak.getUseCount() + chatUsedCount);
                    NumUseItemDTO numUseItemDTO = new NumUseItemDTO();
                    numUseItemDTO.setUseTime(useBak.getCreateTime());
                    numUseItemDTO.setCount(chatUsedCount);
                    numUseItemDTO.setId(useBak.getId());

                    List<NumUseItemDTO> useHistory = chatNumRecordOfPersonUseBak.getUseHistory();
                    if (useHistory == null) {
                        useHistory = new ArrayList<>();
                        useHistory.add(numUseItemDTO);
                        chatNumRecordOfPersonUseBak.setUseHistory(useHistory);
                    }else {
                        useHistory.add(numUseItemDTO);
                        chatNumRecordOfPersonUseBak.setUseHistory(useHistory);
                    }
                    break;
                }else {
                    chatNumRecordOfPersonUseBak.setUseCount(chatNumRecordOfPersonUseBak.getUseCount() + restCount);
                    NumUseItemDTO numUseItemDTO = new NumUseItemDTO();
                    numUseItemDTO.setUseTime(useBak.getCreateTime());
                    numUseItemDTO.setCount(restCount);
                    numUseItemDTO.setId(useBak.getId());
                    List<NumUseItemDTO> useHistory = chatNumRecordOfPersonUseBak.getUseHistory();
                    if (useHistory == null) {
                        useHistory = new ArrayList<>();
                        useHistory.add(numUseItemDTO);
                        chatNumRecordOfPersonUseBak.setUseHistory(useHistory);
                    }else {
                        useHistory.add(numUseItemDTO);
                        chatNumRecordOfPersonUseBak.setUseHistory(useHistory);
                    }
                    chatUsedCount = chatUsedCount - restCount;
                }
            }
        }
        if (chatUsedCount > 0){
            log.info("updateUseCount error:{}, chatUsedCount:{}", useBak, chatUsedCount);
        }
    }

}

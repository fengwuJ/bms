package com.xzt.service.user;

import com.alibaba.fastjson.JSONObject;
import com.xzt.entity.TBookInfo;
import com.xzt.entity.TBookNumber;
import com.xzt.entity.TClassInfo;
import com.xzt.mapper.user.UserBookingManageMapper;
import com.xzt.pojo.PBookInfoNumber;
import com.xzt.util.ChineseToPinyin;
import com.xzt.util.RetResponse;
import com.xzt.util.RetResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserBookingManageService {

    @Resource
    private UserBookingManageMapper userBookingManageMapper;

    public RetResult searchSorts() {
        List<TClassInfo> list = userBookingManageMapper.getBookSorts();
        return RetResponse.makeOKRsp("1",list);

    }

    //分类查询
    public RetResult searchBookBySort(Map<String,Object> map) {
        Map<String,Object> resultMap = new HashMap<>();
        //查询书籍总数
        List list = (List) map.get("bookIds");
        int totalNum = 0;
        if (list != null){
            Map<String,Object> selectMap = new HashMap<>();
            selectMap.put("bookIdList",list);
            String name = (String) map.get("name");
            if (name != null){
                selectMap.put("name",name);
            }
             totalNum = userBookingManageMapper.searchBookNumBy(selectMap);
        }else {
            String name = (String) map.get("name");
            totalNum = userBookingManageMapper.searchBookNumByName(name);
        }
        resultMap.put("totalNum",totalNum);

        //查询书籍
        List<TBookInfo> bookInfoList = userBookingManageMapper.getBooksBy(map);
        List<PBookInfoNumber> resultList = new ArrayList<>();
        for (int i = 0; i < bookInfoList.size(); i++){
            PBookInfoNumber pBookInfoNumber = new PBookInfoNumber();
            TBookNumber tBookNumber = searchBookNumById(bookInfoList.get(i).getBookId());
            pBookInfoNumber.setBookInfo(bookInfoList.get(i));
            pBookInfoNumber.setBookNumber(tBookNumber);
            resultList.add(pBookInfoNumber);
        }
        if (resultList != null && resultList.size() > 0){
            resultMap.put("resultList",resultList);
            return RetResponse.makeOKRsp("1",resultMap);
        }else {
            return RetResponse.makeErrRsp("0");
        }

    }


    //首字母查询
    public RetResult searchBookByFirstChar(char firstChar) {

        Map<String,Object> map = new HashMap<>();
        List<TBookInfo> bookInfoList = userBookingManageMapper.getBooksBy(map);
        List<TBookInfo> tempList = new ArrayList<>();
        List<PBookInfoNumber> resultList = new ArrayList<>();
        for (int i = 0; i < bookInfoList.size(); i++){
            int isOk = ChineseToPinyin.getPinYinHeadChar(bookInfoList.get(i).getName(),firstChar);
            if (isOk == 1 || isOk == 0){
                tempList.add(bookInfoList.get(i));
            }
        }

        for (int i = 0; i < tempList.size(); i++){
            PBookInfoNumber pBookInfoNumber = new PBookInfoNumber();
            pBookInfoNumber.setBookInfo(tempList.get(i));
            TBookNumber tBookNumber = searchBookNumById(tempList.get(i).getBookId());
            pBookInfoNumber.setBookNumber(tBookNumber);
            resultList.add(pBookInfoNumber);
        }

        if (resultList.size() > 0){
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("resultMap",resultList);
            resultMap.put("totalNum",resultList.size());
            return RetResponse.makeOKRsp("1",resultMap);
        }else {
            return RetResponse.makeOKRsp("0");
        }
    }


    //查询bookids
    public List searchBookIds(List classIdList) {
        List list = userBookingManageMapper.searchBookIdsByClassId(classIdList);
        return list;
    }

    //查询书籍数量
    public TBookNumber searchBookNumById(long bookId){
        return userBookingManageMapper.findBookNumById(bookId);
    }

}

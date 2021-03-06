package com.xzt.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xzt.entity.TBookInfo;
import com.xzt.service.admin.BookManageService;
import com.xzt.util.RetResponse;
import com.xzt.util.RetResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@RestController()
@RequestMapping("/q/admin/book")
public class BookManageController {
    @Resource(name = "bmservice")
    private BookManageService bmservice;

    //插入书籍信息
    @RequestMapping("/addbook")
    public RetResult addBook(@RequestBody JSONObject jsonObject){
        JSONObject bookInfoJSon = jsonObject.getJSONObject("bookInfo");
        TBookInfo bookInfo = JSONObject.toJavaObject(bookInfoJSon,TBookInfo.class);
        int number = jsonObject.getJSONObject("bookNum").getInteger("number");
        JSONArray jsonArray = jsonObject.getJSONArray("classIds");
        List<Long> classIdList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++){
            classIdList.add(jsonArray.getLongValue(i));
        }
        RetResult retResult = bmservice.addBook(bookInfo,number,classIdList);
        return retResult;
    }

    //检测书籍是否存在
    @RequestMapping("/checkBookExsist")
    public RetResult checkBookExsist(@RequestBody JSONObject jsonObject){
        String bookName = jsonObject.getString("bookName");
        RetResult retResult = bmservice.checkBookExsist(bookName);
        return retResult;
    }

    //修改书籍信息
    @RequestMapping("/updateBookInfo")
    public RetResult updateBookInfo(@RequestBody JSONObject jsonObject){
        JSONObject bookInfoJSon = jsonObject.getJSONObject("bookInfo");
        TBookInfo bookInfo = JSONObject.toJavaObject(bookInfoJSon,TBookInfo.class);
        int number = jsonObject.getJSONObject("bookNum").getInteger("number");
        return bmservice.updateBookInfoById(bookInfo,number);
    }

    //删除书籍信息
    @RequestMapping("/deleteBookInfo")
    public RetResult deleteBookInfo(@RequestBody JSONObject jsonObject){

        if (jsonObject.getLong("bookId") != null){
            Long bookId = jsonObject.getLong("bookId");
            return bmservice.deleteBookInfoById(bookId);
        }else {
            return RetResponse.makeErrRsp("传入参数有误");
        }

    }

}

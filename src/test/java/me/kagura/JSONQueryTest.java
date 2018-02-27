package me.kagura;


import com.google.gson.reflect.TypeToken;
import me.kagura.exception.FieldNotExistException;
import me.kagura.exception.TypeNotMismatchException;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Unit test for JSONQuery.
 */
public class JSONQueryTest {

    @Test
    public void Test() throws TypeNotMismatchException, FieldNotExistException {
        String json = "" +
                "{\n" +
                "  \"errno\": 0,\n" +
                "  \"errmsg\": 成功,\n" +
                "  \"user\": \"{\\\"user_id\\\":643361255,\\\"user_name\\\":\\\"鹞之神乐\\\",\\\"user_sex\\\":1,\\\"user_status\\\":1}\",\n" +
                "  \"comment_info\": [\n" +
                "    {\n" +
                "      \"tid\": \"5504460056\",\n" +
                "      \"pid\": \"116776960983\",\n" +
                "      \"cid\": \"116857893053\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"tid\": \"5504460056\",\n" +
                "      \"pid\": \"116776960983\",\n" +
                "      \"cid\": \"116858057626\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"tid\": \"5504460056\",\n" +
                "      \"pid\": \"116776960983\",\n" +
                "      \"cid\": \"116880757453\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"data\": {\n" +
                "    \"comment_list\": {\n" +
                "      \"116776891765\": {\n" +
                "        \"comment_num\": 3,\n" +
                "        \"comment_list_num\": 4\n" +
                "      },\n" +
                "      \"116776960983\": {\n" +
                "        \"comment_num\": 4,\n" +
                "        \"comment_list_num\": 4\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        //获取根元素errno
        JsonResult jsonResult = JSONQuery.select(json, "errno");
        //获取根元素errno，并转换为int
        int errno = jsonResult.getAsInt();
        //获取根元素data中的comment_list
        jsonResult = JSONQuery.select(json, "data > comment_list");
        //正则过滤出属性数组，针对一部分拿对象当数组用的情况
        jsonResult = JSONQuery.select(json, "data > comment_list > [\\d+]");
        //获取数组指定位置的元素
        jsonResult = JSONQuery.select(json, "comment_info > [2]");
        //获取数组指定位置的元素 负数坐标
        jsonResult = JSONQuery.select(json, "comment_info > [-1]");
        //针对某个字符串属性的值又是个json字符串的情况
        jsonResult = JSONQuery.select(json, "user > user_name");
        //jsonResult作为参数替代json字符串
        JsonResult data = JSONQuery.select(json, "data");
        jsonResult = JSONQuery.select(data, "comment_list");
        //将json字符串转换为JsonResult
        jsonResult = JSONQuery.select(json, "");
        jsonResult = JSONQuery.select(json, null);

        // v0.2.4新增
        //将选择结果反序列化为普通对象
        Post post = JSONQuery.select(json, "comment_info > [2]", Post.class);
        //将选择结果反序列化为普通对象数组
        Post[] postArray = JSONQuery.select(json, "comment_info", Post[].class);
        //将选择结果反射为泛型类型List<Post>
        Type type = new TypeToken<List<Post>>() {}.getType();
        List<Post> postList = JSONQuery.select(json, "comment_info", type);

    }

}

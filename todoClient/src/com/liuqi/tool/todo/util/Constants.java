package com.liuqi.tool.todo.util;/**
 * Created by ctx334 on 2017/7/13.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类 <br>
 * 作　　者：刘奇<br>
 * 创建时间：2017/7/13<br>
 * 对象版本：V1.0<br>
 * －－－－－－－－－－修改记录－－－－－－－－－－<br>
 * 版　　本：V1.1<br>
 * 修改内容：<br>
 * 修改　人：刘奇<br>
 * 修改时间：<br>
 **/
public class Constants {
    /**
     * 公司列表
     */
    final static List<String> CORP_LIST = new ArrayList<String>(){
        {
            add("宇信");
            add("联创");
            add("联信");
            add("文思");
            add("王教授");
        }
    };

    /**
     * 项目列表　
     */
    final static List<String> PRJ_LIST = new ArrayList<String>() {
        {
            add("数据家园");
            add("PC报表");
            add("其它");
        }
    };

    /**
     * 部门列表
     */
    public final static List<String> DEPT_LIST = new ArrayList<String>() {{
        add("管理信息部");
        add("运营管理部");
        add("零售业务部");
        add("网络金融事业部");
        add("信息技术部");
        add("信用卡部");
        add("财务企划部");
        add("公司业务部");
        add("中小企业部");
        add("风险管理部");
        add("客户服务部");
        add("金融市场部");
        add("金融同业部");
        add("人力资源部");
        add("分支行");
    }};
}

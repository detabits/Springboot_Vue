package com.qingge.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.entity.Product;
import com.qingge.springboot.entity.User;

import com.qingge.springboot.entity.YNOrder;

import com.qingge.springboot.service.LogService;
import com.qingge.springboot.service.UserService;

import com.qingge.springboot.service.YNOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private LogService logService;
    @Resource
    private HttpServletRequest request;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        logService.log(userDTO.getUsername(), StrUtil.format("用户 {} 登录系统",userDTO.getUsername()));
        return Result.success(dto);
    }

    @Autowired
    private UserService userService;
    @Autowired
    private YNOrderService YNOrderService;


    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        userDTO.setRole("ROLE_CUSTOMER");
        userDTO.setCreateTime(DateUtil.now());
        userDTO.setAvatar("https://b.zol-img.com.cn/desk/bizhi/image/10/960x600/1598319721647.jpg");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }

        logService.log(userDTO.getUsername(), StrUtil.format("用户 {} 注册账号成功", userDTO.getUsername()));
        return Result.success(userService.register(userDTO));
    }


    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword("123456");
            // 新增或者更新
            logService.log(StrUtil.format("新增用户：{} ", user.getUsername()));
        }
       else{
            // 新增或者更新
            logService.log(StrUtil.format("更新用户：{} ", user.getUsername()));
        }
        return userService.saveUser(user);
    }



    public User getUser() {
        String token = request.getHeader("token");
        String username = JWT.decode(token).getAudience().get(0);
        username=userService.findusername(username);  //特事特办
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @GetMapping("/{id}")
    public  Result findById(@PathVariable Integer id) {
        return Result.success(userService.findById(id));
    }

    /**
     * 更新账户余额
     * @param money
     * @return
     */
    @PutMapping("/account/{money}")
    public Result recharge(@PathVariable BigDecimal money) {
        User user = getUser();
        user.setAccount(user.getAccount().add(money));
        userService.updateById(user);
        logService.log(StrUtil.format("更新用户账户：{} ", user.getUsername()));
        return Result.success(0);
    }




    // 查询所有数据
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }



    //个人信息根据用户名查询
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        User user = userService.getById(id);
        logService.log(StrUtil.format("删除用户 {} ", user.getUsername()));
        return userService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) { // [1,2,3]
        logService.log(StrUtil.format("批量删除用户"));
        return userService.removeByIds(ids);
    }

    // 分页查询
    //  接口路径：/user/page?pageNum=1&pageSize=10
    // @RequestParam接受
//    limit第一个参数 = (pageNum - 1) * pageSize
    // pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username) {
//        pageNum = (pageNum - 1) * pageSize;
//        username = "%" + username + "%";
//        List<User> data = userMapper.selectPage(pageNum, pageSize, username);
//        Integer total = userMapper.selectTotal(username);
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }

    // 分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum ,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String nickname,
                                @RequestParam(defaultValue = "") String nicknameYG,
                                @RequestParam(defaultValue = "") String phone,
                                @RequestParam(defaultValue = "") String nicknameKH,
                                @RequestParam(defaultValue = "") String role,
                                @RequestParam(defaultValue = "") String productclassification) {

        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.and(wrapper -> wrapper.like("username", username));
        }
        if (!"".equals(nickname)) {
            queryWrapper.and(wrapper -> wrapper.like("nickname", nickname));
        }
        //if (!"".equals(id)) {
           // queryWrapper.and(wrapper -> wrapper.like("id", id).like("role", "ROLE_USER").or().like("role", "ROLE_ADMIN"));
       // }
        if (!"".equals(nicknameYG)) {
            queryWrapper.and(wrapper -> wrapper.like("nickname", nicknameYG).like("role", "ROLE_USER").or().like("role", "ROLE_ADMIN"));
        }

        if (!"".equals(phone)) {
            queryWrapper.and(wrapper -> wrapper.like("phone", phone));
        }
        if (!"".equals(nicknameKH)) {
            queryWrapper.and(wrapper -> wrapper.like("nickname", nicknameKH)).like("role", "ROLE_CUSTOMER");
        }
        if (!"".equals(role)) {
            if("内部平台".equals(role)) {
                queryWrapper.and(wrapper -> wrapper.like("role", "ROLE_USER").or().like("role", "ROLE_ADMIN"));
            }
            if("外部平台".equals(role)) {
                queryWrapper.and(wrapper -> wrapper.like("role", "ROLE_CUSTOMER"));
            }
            if("ROLE_CUSTOMER".equals(role)) {
                queryWrapper.and(wrapper -> wrapper.like("role", "ROLE_CUSTOMER"));
            }
        }
        if (!"".equals(productclassification)) {
            queryWrapper.and(wrapper -> wrapper.like("tags", productclassification));
        }
        queryWrapper.orderByDesc("id");
        return userService.page(page, queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatar", "头像");
        writer.addHeaderAlias("role", "权限");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(1).toString());
            user.setPassword(row.get(2).toString());
            user.setNickname(row.get(3).toString());
            user.setEmail(row.get(4).toString());
            user.setPhone(row.get(5).toString());
            user.setAddress(row.get(6).toString());
            //user.setCreateTime(row.get(7).toString());
            user.setAvatar(row.get(8).toString()); //头像
            user.setRole(row.get(9).toString()); //
            users.add(user);
        }

        userService.saveBatch(users);
        return true;
    }

}

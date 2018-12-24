package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.UserService;
import com.baymin.restroomapi.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Validated
@Api(description = "用户操作接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "username", value = "用户登录号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "relName", value = "用户姓名",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码",defaultValue = "e10adc3949ba59abbe56e057f20f883e", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "department", value = "部门",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "员工技能等级",defaultValue = "2",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userStatus", value = "用户类型{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/user")
    public Object save(@RequestParam(value = "username") String username,
                       @RequestParam(value = "relName") String relName,
                       @RequestParam(value = "password",defaultValue = "e10adc3949ba59abbe56e057f20f883e") String password,
                       @RequestParam(value = "department",required = false) String department,
                       @RequestParam(value = "level",defaultValue = "2") Integer levelId,
                       @RequestParam(value = "userStatus",defaultValue = "1") Integer userStatus)throws MyException{
        String salt=UUID.randomUUID().toString();
        User u=new User();
        u.setUsername(username);
        u.setRelName(relName);
        u.setPassword(Utils.sha256(password,salt+"-"+username));
        u.setDepartment(department);
        u.setUserStatus(userStatus);
        u.setSalt(salt);
        u.setLevel(userService.findLevelById(levelId));
        u.setUserHeadUrl("https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png");
        return userService.save(u);
    }

    @ApiOperation(value="编辑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "username", value = "用户登录号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "relName", value = "用户姓名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "department", value = "部门", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "员工技能等级", dataType = "string", paramType = "query"),
            //@ApiImplicitParam(name = "userType", value = "用户类型{0：后台账户|1：app端}",defaultValue = "0",required = true, dataType = "string", paramType = "query"),
    })
    @PatchMapping("/user")
    public Object update(@RequestParam(value = "username") String username,
                       @RequestParam(value = "relName") String relName,
                       @RequestParam(value = "department") String department,
                       @RequestParam(value = "level") Integer levelId)throws MyException{
        return userService.updateByUsername(username,relName,department,levelId);
    }

    @ApiOperation(value="重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "username", value = "用户登录号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码（md5加密）", dataType = "string", paramType = "query"),
    })
    @PatchMapping(value = "password")
    public Object updatePassword(@RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password)throws MyException{
        return userService.updatePasswordByUsername(username,password);
    }

    @ApiOperation(value="删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "userId",value = "id", required = true, dataType = "string",paramType = "path"),
    })
    @DeleteMapping(value = "/user/{userId}")
    public Object updatePassword(@PathVariable("userId") Integer userId)throws MyException{
        return userService.delete(userId);
    }


    @ApiOperation(value="用户登录",httpMethod = "POST")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password",defaultValue = "e10adc3949ba59abbe56e057f20f883e", value = "密码（md5）",required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "用户类型",defaultValue = "0", required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping(value = "/user/login")
    public Object userLogin(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) throws MyException {
        return userService.userLogin(username,password);
    }

    /**
     * 获取个人列表分页
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取个人列表[分页]", response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "userType", defaultValue = "1", value = "用户类型", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "页数,不传默认0", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "每页数量,不传默认10", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "查询字段,不传表示不筛选", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型",defaultValue = "desc",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortField", value = "排序字段",defaultValue = "username",required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/user")
    public Object getUserListByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "userType", defaultValue = "1") Integer userType,
                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "值不能小于1") Integer size,
                                    @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                    @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
                                    @RequestParam(value = "sortField", defaultValue = "createTime") String sortField
                                    ) throws Exception {
        User userConditions = new User();
        userConditions.setUsername(keyword);
        //userConditions.setLevel(keyword);
        userConditions.setDepartment(keyword);
        userConditions.setRelName(keyword);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("userNumber", ExampleMatcher.GenericPropertyMatchers.startsWith())//模糊查询匹配开头，即{username}%
////                .withMatcher("relName" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
////                .withMatcher("level" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
////                .withMatcher("department" ,ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
//                .withIgnorePaths("password","userId","salt","userType","createTime");//忽略字段，即不管password是什么值都不加入查询条件
//        Example<User> example = Example.of(user ,matcher);
        //Sort.Direction dir="asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC;
        //Pageable pageable=PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField);
        //PageRequest.of(page, size, "asc".equals(sortType) ? Sort.Direction.ASC : Sort.Direction.DESC, sortField)
        return userService.findAll(userType,userConditions, PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField));
    }


}

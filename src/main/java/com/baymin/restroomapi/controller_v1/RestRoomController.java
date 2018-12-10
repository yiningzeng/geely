package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.RestRoomService;
import com.baymin.restroomapi.service.UserService;
import com.baymin.restroomapi.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Validated
@Api(description = "公厕操作接口")
public class RestRoomController {


    @Autowired
    private RestRoomService restRoomService;

    @ApiOperation(value="新增公厕")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "公厕名称", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "region", value = "所属行政区",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/restroom")
    public Object save(@RequestParam(value = "name") String name,
                       @RequestParam(value = "region") String region,
                       @RequestParam(value = "address") String address,
                       @RequestParam(value = "remark",required = false) String remark,
                       @RequestParam(value = "status",defaultValue = "1") Integer status)throws MyException{
        RestRoom restRoom=new RestRoom();
        restRoom.setRestRoomName(name);
        restRoom.setRegion(region);
        restRoom.setAddress(address);
        restRoom.setRemark(remark);
        restRoom.setStatus(status);
        return restRoomService.save(restRoom);
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
    @PatchMapping("/restroom")
    public Object update(@RequestParam(value = "username") String username,
                       @RequestParam(value = "relName") String relName,
                       @RequestParam(value = "department") String department,
                       @RequestParam(value = "level") Integer levelId)throws MyException{

        return restRoomService.updateByRestRoomId(username,relName,department,levelId);
    }


    @ApiOperation(value="删除公厕")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId",value = "restRoomId", required = true, dataType = "string",paramType = "path"),
    })
    @DeleteMapping(value = "/restroom/{restRoomId}")
    public Object deleteRestRoom(@PathVariable("restRoomId") Integer restRoomId)throws MyException{
        return restRoomService.deleteByRestRoomId(restRoomId);
    }

    /**
     * 获取个人列表分页
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取公厕列表[分页]", response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "页数,不传默认0", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "每页数量,不传默认10", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "查询字段,不传表示不筛选", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型",defaultValue = "desc",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortField", value = "排序字段",defaultValue = "createTime",required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/restroom")
    public Object getRestRoomListByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "status", required = false) Integer status,
                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "值不能小于1") Integer size,
                                    @RequestParam(value = "keyword",required = false) String keyword,
                                    @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
                                    @RequestParam(value = "sortField", defaultValue = "createTime") String sortField
                                    ) throws Exception {
        RestRoom restRoom=new RestRoom();
        Optional.ofNullable(status).ifPresent(v->restRoom.setStatus(v));
        Optional.ofNullable(keyword).ifPresent(v->{
            restRoom.setRemark(keyword);
            restRoom.setRegion(keyword);
            restRoom.setAddress(keyword);
            restRoom.setRestRoomName(keyword);
        });
        return restRoomService.findAll(restRoom, PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField));

    }


}

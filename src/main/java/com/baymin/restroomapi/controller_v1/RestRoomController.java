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
import org.springframework.data.domain.Pageable;
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
@Api(description = "公厕操作接口done")
public class RestRoomController {

    @Autowired
    private RestRoomService restRoomService;

    @ApiOperation(value="新增公厕")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "公厕名称", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "ip", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "region", value = "所属行政区",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cleaner", value = "责任保洁", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/restroom")
    public Object save(@RequestParam(value = "name") String name,
                       @RequestParam(value = "ip",required = false) String ip,
                       @RequestParam(value = "region") String region,
                       @RequestParam(value = "address") String address,
                       @RequestParam(value = "longitude") Float longitude,
                       @RequestParam(value = "latitude") Float latitude,
                       @RequestParam(value = "cleaner",required = false) String cleaner,
                       @RequestParam(value = "remark",required = false) String remark,
                       @RequestParam(value = "status",defaultValue = "1") Integer status)throws MyException{
        RestRoom restRoom=new RestRoom();
        restRoom.setIp(ip);
        restRoom.setLongitude(longitude);
        restRoom.setLatitude(latitude);
        restRoom.setRestRoomName(name);
        restRoom.setRegion(region);
        restRoom.setAddress(address);
        restRoom.setCleaner(cleaner);
        restRoom.setRemark(remark);
        restRoom.setStatus(status);
        return restRoomService.save(restRoom);
    }

    @ApiOperation(value="编辑厕所")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId", value = "公厕id", required = true,dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "公厕名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "ip", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "region", value = "所属行政区", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "详细地址",dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cleaner", value = "责任保洁", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}", dataType = "string", paramType = "query"),
    })
    @PatchMapping("/restroom/{restRoomId}")
    public Object update(@PathVariable(value = "restRoomId") Integer restRoomId,
                         @RequestParam(value = "name",required = false) String name,
                         @RequestParam(value = "ip",required = false) String ip,
                         @RequestParam(value = "region",required = false) String region,
                         @RequestParam(value = "address",required = false) String address,
                         @RequestParam(value = "longitude") Float longitude,
                         @RequestParam(value = "latitude") Float latitude,
                         @RequestParam(value = "cleaner",required = false) String cleaner,
                         @RequestParam(value = "remark",required = false) String remark,
                         @RequestParam(value = "status",required = false) Integer status)throws MyException{

        return restRoomService.updateByRestRoomId(restRoomId,Optional.ofNullable(name),Optional.ofNullable(ip),Optional.ofNullable(region),Optional.ofNullable(address),Optional.ofNullable(longitude),Optional.ofNullable(latitude),Optional.ofNullable(cleaner),Optional.ofNullable(remark),Optional.ofNullable(status));
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
    @ApiOperation(value = "获取公厕列表[分页]", response = RestRoom.class)
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
        Pageable pageable=PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField);
        if(Optional.ofNullable(keyword).isPresent()){
            RestRoom restRoom=new RestRoom();
            Optional.ofNullable(status).ifPresent(v->restRoom.setStatus(v));
            Optional.ofNullable(keyword).ifPresent(v->{
                restRoom.setRemark(keyword);
                restRoom.setRegion(keyword);
                restRoom.setAddress(keyword);
                restRoom.setRestRoomName(keyword);
            });
            return restRoomService.findAll(restRoom, pageable);
        }
        return restRoomService.findAll(null,pageable);


    }


}

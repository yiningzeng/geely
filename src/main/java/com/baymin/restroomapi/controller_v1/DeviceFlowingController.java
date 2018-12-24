package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.DeviceGas;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.DeviceGasService;
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

@RestController
@RequestMapping("/api/v1/device")
@Slf4j
@Validated
@Api(description = "客流progress")
public class DeviceFlowingController {

    @Autowired
    private DeviceGasService deviceGasService;

    @ApiOperation(value="新增设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId", value = "厕所编号",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "gasDeviceId", value = "硬件设备id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "南侧女厕，{0：男|1：女}",defaultValue = "0",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/flowing")
    public Object save(@RequestParam(value = "restRoomId") Integer restRoomId,
                       @RequestParam(value = "gasDeviceId") Integer gasDeviceId,
                       @RequestParam(value = "type") Integer type,
                       @RequestParam(value = "status",defaultValue = "1") Integer status)throws MyException{
        DeviceGas deviceGas=new DeviceGas();
        deviceGas.setGasDeviceId(gasDeviceId);
        deviceGas.setType(type);
        deviceGas.setStatus(status);
        return deviceGasService.save(restRoomId,deviceGas);
    }



    @ApiOperation(value="删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "gasDeviceId",value = "gasDeviceId", required = true, dataType = "string",paramType = "path"),
    })
    @DeleteMapping(value = "/flowing/{gasDeviceId}")
    public Object deleteRestRoom(@PathVariable("gasDeviceId") Integer gasDeviceId)throws MyException{
        return deviceGasService.deleteByDeviceGasId(gasDeviceId);
    }

    /**
     * 获取列表分页
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取列表[分页]", response = RestRoom.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "页数,不传默认0", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "每页数量,不传默认10", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型",defaultValue = "desc",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortField", value = "排序字段",defaultValue = "createTime",required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/flowing")
    public Object getRestRoomListByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "status", required = false) Integer status,
                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "值不能小于1") Integer size,
                                    @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
                                    @RequestParam(value = "sortField", defaultValue = "createTime") String sortField) throws Exception {
        return deviceGasService.findAll(1,Optional.ofNullable(status),PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField));
    }


}

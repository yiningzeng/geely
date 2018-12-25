package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.DeviceCameraService;
import com.baymin.restroomapi.service.RestRoomService;
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

@RestController
@RequestMapping("/api/v1/device")
@Slf4j
@Validated
@Api(description = "摄像头操作接口done")
public class DeviceCameraController {

    @Autowired
    private DeviceCameraService deviceCameraService;

    @ApiOperation(value="新增摄像头")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId", value = "厕所编号",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "公厕ip可带端口", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "设备的username", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "设备的password", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/camera")
    public Object save(@RequestParam(value = "restRoomId") Integer restRoomId,
                       @RequestParam(value = "ip",required = false) String ip,
                       @RequestParam(value = "username") String username,
                       @RequestParam(value = "password") String password,
                       @RequestParam(value = "remark",required = false) String remark,
                       @RequestParam(value = "status",defaultValue = "1") Integer status)throws MyException{
        DeviceCamera deviceCamera=new DeviceCamera();
        deviceCamera.setIp(ip);
        deviceCamera.setRtsp("rtsp://"+username+":"+password+"@"+ip+"/h264/ch1/main/av_stream");
        deviceCamera.setUsername(username);
        deviceCamera.setPassword(password);
        deviceCamera.setRemark(remark);
        deviceCamera.setStatus(status);
        return deviceCameraService.save(restRoomId,deviceCamera);
    }

    @ApiOperation(value="编辑摄像头")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "cameraId", value = "摄像头id",required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "restRoomId", value = "厕所编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "公厕ip可带端口", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "设备的username", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "设备的password", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}", dataType = "string", paramType = "query"),
    })
    @PatchMapping("/camera/{cameraId}")
    public Object update(@PathVariable(value = "cameraId") Integer cameraId,
                         @RequestParam(value = "restRoomId",required = false) Integer restRoomId,
                         @RequestParam(value = "ip",required = false) String ip,
                         @RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "remark",required = false) String remark,
                         @RequestParam(value = "status",required = false) Integer status)throws MyException{
        return deviceCameraService.updateByDeviceCameraId(cameraId,Optional.ofNullable(restRoomId),Optional.ofNullable(ip),Optional.ofNullable(username),Optional.ofNullable(password),Optional.ofNullable(remark),Optional.ofNullable(status));
    }


    @ApiOperation(value="删除摄像头")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "cameraId",value = "cameraId", required = true, dataType = "string",paramType = "path"),
    })
    @DeleteMapping(value = "/camera/{cameraId}")
    public Object deleteRestRoom(@PathVariable("cameraId") Integer cameraId)throws MyException{
        return deviceCameraService.deleteByDeviceCameraId(cameraId);
    }

    /**
     * 获取个人列表分页
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取摄像头列表[分页]", response = RestRoom.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId", value = "摄像头id",required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "页数,不传默认0", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "每页数量,不传默认10", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型",defaultValue = "desc",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortField", value = "排序字段",defaultValue = "createTime",required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/camera/{restRoomId}")
    public Object getRestRoomListByPage(
            @PathVariable(value = "restRoomId") Integer restRoomId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "值不能小于1") Integer size,
            @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
            @RequestParam(value = "sortField", defaultValue = "createTime") String sortField) throws Exception {
        return deviceCameraService.findAll(restRoomId,Optional.ofNullable(status),PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField));
    }


}

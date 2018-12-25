package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.DeviceGas;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.DeviceCameraService;
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
@RequestMapping("/api/v1/stream")
@Slf4j
@Validated
@Api(description = "摄像头转码推流接口done")
public class PushStreamController {

    @Autowired
    private DeviceCameraService deviceCameraService;

    @ApiOperation(value="转码推流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "cameraId", value = "摄像头编号",required = true, dataType = "string", paramType = "path"),
    })
    @GetMapping("/push/{cameraId}")
    public Object push(@PathVariable(value = "cameraId") Integer cameraId)throws MyException{
        return deviceCameraService.push(cameraId);
    }
    @ApiOperation(value="停止转码推流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "cameraId", value = "摄像头编号",required = true, dataType = "string", paramType = "path"),
    })
    @GetMapping("/stop/{cameraId}")
    public Object stop(@PathVariable(value = "cameraId") Integer cameraId)throws MyException{
        return deviceCameraService.stop(cameraId);
    }

}

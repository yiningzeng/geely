package com.baymin.restroomapi.controller_v1;

import com.baymin.restroomapi.entity.DeviceBoard;
import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.DeviceBoardService;
import com.baymin.restroomapi.service.DeviceCameraService;
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


import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/device")
@Slf4j
@Validated
@Api(description = "公告屏操作接口")
public class DeviceBoardController {

    @Autowired
    private DeviceBoardService deviceBoardService;

    @ApiOperation(value="新增公告屏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "restRoomId", value = "厕所编号",required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "公厕ip可带端口", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}",defaultValue = "1",required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping("/board")
    public Object save(@RequestParam(value = "restRoomId") Integer restRoomId,
                       @RequestParam(value = "ip") String ip,
                       @RequestParam(value = "status",defaultValue = "1") Integer status)throws MyException{
        DeviceBoard deviceBoard=new DeviceBoard();
        deviceBoard.setIp(ip);
        deviceBoard.setStatus(status);
        return deviceBoardService.save(restRoomId,deviceBoard);
    }

    @ApiOperation(value="编辑公告屏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "boardId", value = "摄像头id",required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "restRoomId", value = "厕所编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "公厕ip可带端口", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态{0：禁用|1：启用}", dataType = "string", paramType = "query"),
    })
    @PatchMapping("/board/{boardId}")
    public Object update(@PathVariable(value = "boardId") Integer boardId,
                         @RequestParam(value = "restRoomId",required = false) Integer restRoomId,
                         @RequestParam(value = "ip",required = false) String ip,
                         @RequestParam(value = "status",required = false) Integer status)throws MyException{
        return deviceBoardService.updateByDeviceBoardId(boardId,Optional.ofNullable(restRoomId),Optional.ofNullable(ip),Optional.ofNullable(status));
    }


    @ApiOperation(value="删除公告屏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "boardId",value = "boardId", required = true, dataType = "string",paramType = "path"),
    })
    @DeleteMapping(value = "/board/{boardId}")
    public Object deleteRestRoom(@PathVariable("boardId") Integer boardId)throws MyException{
        return deviceBoardService.deleteByDeviceBoardId(boardId);
    }

    /**
     * 获取个人列表分页
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取公告屏列表[分页]", response = RestRoom.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "页数,不传默认0", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "每页数量,不传默认10", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型",defaultValue = "desc",required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sortField", value = "排序字段",defaultValue = "createTime",required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/board")
    public Object getRestRoomListByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "status", required = false) Integer status,
                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1, message = "值不能小于1") Integer size,
                                    @RequestParam(value = "sortType", defaultValue = "desc") String sortType,
                                    @RequestParam(value = "sortField", defaultValue = "createTime") String sortField
                                    ) throws Exception {
        return deviceBoardService.findAll(1,Optional.ofNullable(status),PageRequest.of(page,size,"asc".equals(sortType)?Sort.Direction.ASC:Sort.Direction.DESC,sortField));
    }

    @ApiOperation(value="公告屏主动请求获取公厕的信息by it's ip")
    @GetMapping(value = "/hi-give-me-five")
    public Object getMeFive(HttpServletRequest request) throws MyException {
        String ip = Utils.getIpAddr(request);
        log.info("ip访问：{}",ip);
        return deviceBoardService.giveMeFive(ip);
    }

    @ApiOperation(value="测试-公告屏主动请求获取公厕的信息by it's ip")
    @GetMapping(value = "/hi-give-me-five-test")
    public Object getMeFiveTest(HttpServletRequest request) throws MyException {
        return deviceBoardService.giveMeFive("192.168.10.6");
    }

    @ApiOperation(value="只获取统计的的客流by it's ip")
    @GetMapping(value = "/get-fuck-flow")
    public Object getOnlyFuckFlow(HttpServletRequest request) throws MyException {
        String ip = Utils.getIpAddr(request);
        log.info("ip访问：{}",ip);
        return deviceBoardService.getOnlyFuckFlow(ip);
    }

    @ApiOperation(value="测试-只获取统计的的客流by it's ip")
    @GetMapping(value = "/get-fuck-flow-test")
    public Object getOnlyFuckFlowTest(HttpServletRequest request) throws MyException {
        return deviceBoardService.getOnlyFuckFlow("192.168.10.6");
    }

}

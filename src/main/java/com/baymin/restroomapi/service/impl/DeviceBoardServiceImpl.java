package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.okhttp3.MyOkHttpClient;
import com.baymin.restroomapi.dao.DeviceBoardDao;
import com.baymin.restroomapi.dao.DeviceCameraDao;
import com.baymin.restroomapi.dao.InfoPassengerFlowDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.entity.DeviceBoard;
import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.ret.model.GasInfo;
import com.baymin.restroomapi.ret.model.GiveMeFive;
import com.baymin.restroomapi.service.DeviceBoardService;
import com.baymin.restroomapi.service.DeviceCameraService;
import com.baymin.restroomapi.utils.DateUtils;
import com.baymin.restroomapi.utils.ShellKit;
import com.baymin.restroomapi.utils.StreamGobblerCallback;
import com.baymin.restroomapi.utils.Utils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Optional;

@Slf4j
@Service
public class DeviceBoardServiceImpl implements DeviceBoardService {
    @Autowired
    private DeviceBoardDao deviceBoardDao;
    @Autowired
    private RestRoomDao restRoomDao;

    @Autowired
    private InfoPassengerFlowDao infoPassengerFlowDao;

    @Override
    public Object updateByDeviceBoardId(Integer deviceBoardId, Optional<Integer> restRoomId, Optional<String> ip, Optional<Integer> status) throws MyException {
        return R.callBackRet(deviceBoardDao.findById(deviceBoardId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                DeviceBoard deviceBoard=(DeviceBoard)data;
                restRoomId.ifPresent(v->restRoomDao.findById(v).ifPresent(a->deviceBoard.setRestRoom(a)));
//                restRoomId.ifPresent(v->deviceCamera.setRestRoom(v));
                ip.ifPresent(v->deviceBoard.setIp(v));

//                rtsp.ifPresent(v->deviceCamera.setRtsp(v));
                status.ifPresent(v->deviceBoard.setStatus(v));
                if(deviceBoardDao.save(deviceBoard)!=null) return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }

            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object save(Integer restRoomId, DeviceBoard deviceBoard) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceBoard.setRestRoom((RestRoom)data);
                if(deviceBoardDao.save(deviceBoard)!=null)return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }

    @Override
    public Object deleteByDeviceBoardId(Integer deviceBoardId) throws MyException {
        return R.callBackRet(deviceBoardDao.findById(deviceBoardId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceBoardDao.deleteById(deviceBoardId);
                deviceBoardDao.flush();
                return R.success();
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object findAll(Integer restRoomId, Optional<Integer> status, Pageable pageable) throws MyException {
        return R.callBackRet(status, new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<DeviceBoard> retPage= deviceBoardDao.findAllByStatus((Integer)data,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
            @Override
            public Object onFalse() {
                Page<DeviceBoard> retPage= deviceBoardDao.findAllByRestRoom_RestRoomId(restRoomId,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });
    }

    @Override
    public Object giveMeFive(String ip) throws MyException {

        return R.callBackRet(deviceBoardDao.findFirstByIp(ip), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {

                DeviceBoard deviceBoard =(DeviceBoard)data;
                Integer restroomId = deviceBoard.getRestRoom().getRestRoomId();

                GiveMeFive giveMeFive =new GiveMeFive();
                giveMeFive.setRestRoomName(deviceBoard.getRestRoom().getRestRoomName());

                //region 获取人流信息
                GiveMeFive.FuckFlow fuckFlow=new GiveMeFive.FuckFlow();
                fuckFlow.setToday(infoPassengerFlowDao.findAllSumNumber(restroomId, DateUtils.getDayBegin().toString(), DateUtils.getDayEnd().toString()));
                fuckFlow.setMonth(infoPassengerFlowDao.findAllSumNumber(restroomId, DateUtils.getBeginDayOfMonth().toString(), DateUtils.getEndDayOfMonth().toString()));
                fuckFlow.setAll(infoPassengerFlowDao.findAllSumNumberNoTime(restroomId));
                fuckFlow.setTodayFlow(infoPassengerFlowDao.findAll(restroomId, DateUtils.getDayBegin().toString(), DateUtils.getDayEnd().toString()));

                giveMeFive.setFuckFlow(fuckFlow);

                String res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/termdata/getDevTermList?deviceId="+deviceBoard.getRestRoom().getDeviceGases().get(0).getGasDeviceParentId());

                Gson gson=new Gson();
                GiveMeFive.Gas gasInfo= gson.fromJson(res, GiveMeFive.Gas.class);

                giveMeFive.setGas(gasInfo);
                //endregion

                return R.success(giveMeFive);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object getOnlyFuckFlow(String ip) throws MyException {
        return R.callBackRet(deviceBoardDao.findFirstByIp(ip), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                DeviceBoard deviceBoard =(DeviceBoard)data;
                Integer restroomId = deviceBoard.getRestRoom().getRestRoomId();
                return R.success(infoPassengerFlowDao.findAllSumNumber(restroomId, DateUtils.getDayBegin().toString(), DateUtils.getDayEnd().toString()));
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

}

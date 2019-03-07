package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.okhttp3.MyOkHttpClient;
import com.baymin.restroomapi.dao.DeviceGasDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.entity.DeviceGas;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.ret.model.GasInfo;
import com.baymin.restroomapi.service.DeviceGasService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceGasServiceImpl implements DeviceGasService {

    @Autowired
    private DeviceGasDao deviceGasDao;
    @Autowired
    private RestRoomDao restRoomDao;

    @Override
    public Object save(Integer restRoomId, DeviceGas deviceGas) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceGas.setRestRoom((RestRoom)data);
                return R.callBackRet(deviceGasDao.findFirstByGasDeviceId(deviceGas.getGasDeviceId()), new R.OptionalResult() {
                    @Override
                    public Object onTrue(Object data) {
                        return R.error(ResultEnum.FAIL_ALLREADY_HAVE_DEVICE);
                    }
                    @Override
                    public Object onFalse() {
                        if(deviceGasDao.save(deviceGas)!=null)return R.success();
                        return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
                    }
                });
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }

    @Override
    public Object deleteByDeviceGasId(Integer deviceGasId) throws MyException {
        return R.callBackRet(deviceGasDao.findFirstByGasDeviceId(deviceGasId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceGasDao.deleteAllByGasDeviceId(deviceGasId);
                deviceGasDao.flush();
                return R.success();
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object findAll(Integer restRoomId,Optional<Integer> status,Pageable pageable) throws MyException {
        return R.callBackRet(status, new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<DeviceGas> retPage= deviceGasDao.findAllByStatus((Integer)data,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
            @Override
            public Object onFalse() {
                Page<DeviceGas> retPage= deviceGasDao.findAllByRestRoom_RestRoomId(restRoomId,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });
    }

    @Override
    public Object findAllGasList(Integer deviceId, Integer startTm, Integer endTm) throws MyException {
        String res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/termdata/getDevTermList?deviceId="+deviceId);

        Gson gson=new Gson();
        GasInfo gasInfo= gson.fromJson(res.replace("pickTm","x").replace("zq","y"), GasInfo.class);
        for (int i=0;i<gasInfo.getData().getItems().size();i++){
            String funcId=gasInfo.getData().getItems().get(i).getFuncId();
            res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/datapick/historyList?deviceId="+deviceId+"&funcId="+funcId+"&mode=asc&startTm="+startTm+"&endTm="+endTm);
            GasInfo temp=gson.fromJson(res.replace("pickTm","x").replace("zq","y"),GasInfo.class);

            List<GasInfo.Data.Items> result = null;
            result = temp.getData().getItems().stream()
                    .filter(
                            (GasInfo.Data.Items s) ->
                                     s.getY()!=null && s.getY()>0
                    )
                    .collect(Collectors.toList());

            gasInfo.getData().getItems().get(i).setHistroyList(result);
        }
        return R.success(gasInfo);
    }
}

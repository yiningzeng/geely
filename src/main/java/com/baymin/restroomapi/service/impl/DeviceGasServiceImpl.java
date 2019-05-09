package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.RedisUtils;
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
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
//@CacheConfig(cacheNames = "device_gas")
public class DeviceGasServiceImpl implements DeviceGasService {

    @Autowired
    private DeviceGasDao deviceGasDao;
    @Autowired
    private RestRoomDao restRoomDao;

    @Autowired
    private StringRedisTemplate strRedis;

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
//    @CacheEvict(value="deleteByDeviceGasId", key ="#p0")
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
//    @Cacheable(value="findAll", key ="#p0")
    public Object findAll(Integer restRoomId,Optional<Integer> status,Pageable pageable) throws MyException {
//        redisUtils.set(restRoomId+"",);
        return R.callBackRet(status, new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<DeviceGas> retPage= deviceGasDao.findAllByStatus((Integer)data,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
            @Override
            public Object onFalse() {
                Page<DeviceGas> retPage= deviceGasDao.findAllByRestRoom_RestRoomId(restRoomId,pageable);//userDao.findAll(example,pageable);

//                new GsonBuilder().registerTypeAdapter(DeviceGas.class, new JsonSerializer<DeviceGas>() {
//                    @Override
//                    public JsonElement serialize(DeviceGas src, Type typeOfSrc,  JsonSerializationContext context) {
//                        JsonObject o=new JsonObject();
//                        o.addProperty("pid",  src.getPid());
//                          //o.addProperty("name", src.getName());
//                        return o;
//                    }
//                }).create();
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });
    }

    @Override
//    @Cacheable(value="findAllGasList", key ="#p0")
    public Object findAllGasList(Integer restRoomId, Integer startTm, Integer endTm) throws MyException {

        Optional<DeviceGas> deviceGasOptional=deviceGasDao.findFirstByRestRoom_RestRoomId(restRoomId);
        if(deviceGasOptional.isPresent()){
            String res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/termdata/getDevTermList?deviceId="+deviceGasOptional.get().getGasDeviceParentId());

            Gson gson=new Gson();
            GasInfo gasInfo= gson.fromJson(res.replace("pickTm","x"), GasInfo.class);
//            for (int i=0;i<gasInfo.getData().getItems().size();i++){
//                Integer funcId=Integer.parseInt(gasInfo.getData().getItems().get(i).getFuncId());
//                Optional<DeviceGas> aa=deviceGasDao.findFirstByGasDeviceId(funcId);
//                if(aa.isPresent()){
//                    String rr=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/datapick/historyList?deviceId="+deviceGasOptional.get().getGasDeviceParentId()+"&funcId="+funcId+"&mode=desc&startTm="+startTm+"&endTm="+endTm);
//                    try{
//
//                        List<GasInfo.Data.Items> aaaaa=gson.fromJson(rr.replace("pickTm","x"), GasInfo.class).getData().getItems();
//
//
//                        gasInfo.getData().getItems().get(i).setHistroyList(aaaaa);
//                        if(aaaaa .size()>0)break;
//
////                        String date=gson.fromJson(rr.replace("pickTm","x"), GasInfo.class).getData().getItems().get(0).getX();
////                        if(date!=null){
////                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                            Optional<RestRoom> optionalRestRoom= restRoomDao.findById(restRoomId);
////                            if(optionalRestRoom.isPresent())optionalRestRoom.get().setUpdateTime(sdf.parse(date));
////                            restRoomDao.save(optionalRestRoom.get());
////                        }
////                        gasInfo.getData().getItems().get(i).setX(date);
//                    }
//                    catch (Exception e){
//
//                    }
//
//                    gasInfo.getData().getItems().get(i).setType(aa.get().getType());
//                }
//                else continue;
//
////            res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/datapick/historyList?deviceId="+deviceId+"&funcId="+funcId+"&mode=asc&startTm="+startTm+"&endTm="+endTm);
////            GasInfo temp=gson.fromJson(res.replace("pickTm","x").replace("zq","y"),GasInfo.class);
////
////            List<GasInfo.Data.Items> result = null;
////            result = temp.getData().getItems().stream()
////                    .filter(
////                            (GasInfo.Data.Items s) ->
////                                     s.getY()!=null && s.getY()>0
////                    )
////                    .collect(Collectors.toList());
//
////            gasInfo.getData().getItems().get(i).setHistroyList(result);
//            }
            return R.success(gasInfo);
        }
        else{
            return R.error(ResultEnum.NO_LIST);
        }



    }

    @Override
//    @Cacheable(value="findAllGasListHome", key ="#p0")
    public Object findAllGasListHome(Integer restRoomId, Integer startTm, Integer endTm) throws MyException {

        Optional<DeviceGas> deviceGasOptional=deviceGasDao.findFirstByRestRoom_RestRoomId(restRoomId);
        if(deviceGasOptional.isPresent()){
            String res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/termdata/getDevTermList?deviceId="+deviceGasOptional.get().getGasDeviceParentId());

            Gson gson=new Gson();
            GasInfo gasInfo= gson.fromJson(res.replace("pickTm","x"), GasInfo.class);
            for (int i=0;i<gasInfo.getData().getItems().size();i++){
                Integer deviceParentId=Integer.parseInt(gasInfo.getData().getItems().get(i).getFuncId());
                Optional<DeviceGas> aa=deviceGasDao.findFirstByGasDeviceId(deviceParentId);

                if(aa.isPresent()){
                    String rr=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/datapick/historyList?deviceId="+deviceGasOptional.get().getGasDeviceParentId()+"&funcId="+aa.get().getGasDeviceId()+"&mode=desc&startTm="+startTm+"&endTm="+endTm);
                    try{
                        List<GasInfo.Data.Items> aaaaa=gson.fromJson(rr.replace("pickTm","x"), GasInfo.class).getData().getItems();

                        List<GasInfo.Data.Items> retTemp=new ArrayList<>();

                        GasInfo.Data.Items t =null;
                        for (int n = 0 ;n<aaaaa.size();n++){
                            t= aaaaa.get(n);
                            if(t.getDf()==null) continue;
                            t.set男厕(t.getDf()+(int)(1+Math.random()*(6-1+1)));
                            t.set女厕(t.getDf()+(int)(1+Math.random()*(5-1+1)));
                            t.set大厅(t.getDf()+(int)(1+Math.random()*(3-1+1)));
                            t.set无障碍(t.getDf()+(int)(1+Math.random()*(4-1+1)));
                            retTemp.add(t);
                        }
                        gasInfo.getData().getItems().get(i).setHistroyList(retTemp);
                        if(aaaaa .size()>0)break;
                    }
                    catch (Exception e){

                    }

                    gasInfo.getData().getItems().get(i).setType(aa.get().getType());
                }
                else continue;

//            res=MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/datapick/historyList?deviceId="+deviceId+"&funcId="+funcId+"&mode=asc&startTm="+startTm+"&endTm="+endTm);
//            GasInfo temp=gson.fromJson(res.replace("pickTm","x").replace("zq","y"),GasInfo.class);
//
//            List<GasInfo.Data.Items> result = null;
//            result = temp.getData().getItems().stream()
//                    .filter(
//                            (GasInfo.Data.Items s) ->
//                                     s.getY()!=null && s.getY()>0
//                    )
//                    .collect(Collectors.toList());

//            gasInfo.getData().getItems().get(i).setHistroyList(result);
            }
            return R.success(gasInfo);
        }
        else{
            return R.error(ResultEnum.NO_LIST);
        }



    }
}

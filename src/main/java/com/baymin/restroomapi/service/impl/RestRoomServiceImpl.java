package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.aspect.jwt.TokenUtils;
import com.baymin.restroomapi.dao.DeviceGasDao;
import com.baymin.restroomapi.dao.InfoGasDailyStatisticsDao;
import com.baymin.restroomapi.dao.InfoPassengerFlowDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.dao.specs.RestRoomSpecs;
import com.baymin.restroomapi.entity.*;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.ret.model.RetFuckFlowContrast;
import com.baymin.restroomapi.ret.model.RetOnlyFuckFlow;
import com.baymin.restroomapi.service.RestRoomService;
import com.baymin.restroomapi.utils.DateUtils;
import com.baymin.restroomapi.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RestRoomServiceImpl implements RestRoomService {
    @Autowired
    private RestRoomSpecs restRoomSpecs;
    @Autowired
    private RestRoomDao restRoomDao;
    @Autowired
    private DeviceGasDao deviceGasDao;
    @Autowired
    private InfoGasDailyStatisticsDao infoGasDailyStatisticsDao;
    @Autowired
    private InfoPassengerFlowDao iPFlowDao;
    @Value("${restroom.fuck-flow-save-interval}")
    private boolean isSaveInterval;

    @Override
    public Object updateByRestRoomId(Integer restRoomId, Optional<String> name,Optional<String> ip, Optional<String> region, Optional<String> address,Optional<Float> longitude,Optional<Float> latitude,Optional<String> cleaner, Optional<String> remark, Optional<Integer> status) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                RestRoom restRoom=(RestRoom)data;
                name.ifPresent(v->restRoom.setRestRoomName(v));
                ip.ifPresent(v->restRoom.setIp(v));
                region.ifPresent(v->restRoom.setRegion(v));
                address.ifPresent(v->restRoom.setAddress(v));
                longitude.ifPresent(v->restRoom.setLongitude(v));
                latitude.ifPresent(v->restRoom.setLatitude(v));
                cleaner.ifPresent(v->restRoom.setCleaner(v));
                remark.ifPresent(v->restRoom.setRemark(v));
                status.ifPresent(v->restRoom.setStatus(v));
                if(restRoomDao.save(restRoom)!=null) return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }

            @Override
            public Object onFalse() {
               return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }

    @Override
    public Object save(RestRoom restRoom) throws MyException {
        return R.callBackRet(restRoomDao.findFirstByRestRoomNameAndRegion(restRoom.getRestRoomName(),restRoom.getRegion()), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.error(ResultEnum.FAIL_ADD_RESTROOM_ALLREADY_EXIT);
            }

            @Override
            public Object onFalse() {
                if(restRoomDao.save(restRoom)!=null) return R.success();
                else return R.error(ResultEnum.FAIL_ADD_RESTROOM);
            }
        });
    }

    @Override
    public Object deleteByRestRoomId(Integer restRoomId) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                restRoomDao.deleteById(restRoomId);
                restRoomDao.flush();
                return R.success();
            }

            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });

    }

    @Override
    public Object findAll(RestRoom specs, Pageable pageable) throws MyException {
        return R.callBackRet(Optional.ofNullable(specs), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<RestRoom> retPage= restRoomDao.findAll(restRoomSpecs.listSpecsIni(specs),pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }

            @Override
            public Object onFalse() {
                Page<RestRoom> retPage= restRoomDao.findAll(pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });

    }

    @Override
    public Object fuckFlowByAll(FuckFlow fuckFlow) throws MyException {
        return R.callBackRet(restRoomDao.findFirstByIp(fuckFlow.getIpAddress()), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                RestRoom restRoom = (RestRoom)data;
                Integer oldNum = restRoom.getPeopleNum();
                restRoom.setPeopleNum(fuckFlow.getPeopleCounting().getEnter());
                if(Utils.isYeaterday(restRoom.getUpdateTime(),null) == -1){ //表示是同一天
                    Integer num= restRoom.getPeopleNum()-oldNum;
                    if(num>0) { //只保留有客流的数据
                        //region 更新半小时内的数据
                        InfoPassengerFlow infoPassengerFlow = isSaveInterval
                                ?
                                iPFlowDao.findFirstByRestRoom_RestRoomIdOrderByUpdateTimeDesc(restRoom.getRestRoomId()).orElse(new InfoPassengerFlow())
                                :
                                new InfoPassengerFlow();

                        long diff = new Date().getHours() - infoPassengerFlow.getUpdateTime().getHours();
                        if (diff==0) {
                            infoPassengerFlow.setNumber(infoPassengerFlow.getNumber()+num);
                        }
                        else {
                            infoPassengerFlow = new InfoPassengerFlow();
                            infoPassengerFlow.setNumber(num);
                        }
                        infoPassengerFlow.setIp(fuckFlow.getIpAddress());
                        infoPassengerFlow.setRestRoom(restRoom);
                        infoPassengerFlow.setUpdateTime(new Date());
                        //endregion
                        iPFlowDao.save(infoPassengerFlow);
                        restRoom.setPeopleNum(oldNum+num);
                    }
                }
                else {
                    InfoPassengerFlow i =new InfoPassengerFlow();
                    i.setNumber(restRoom.getPeopleNum());
                    i.setRestRoom(restRoom);
                    i.setIp(fuckFlow.getIpAddress());
                    iPFlowDao.save(i);
                }
                restRoom.setUpdateTime(new Date());
                restRoomDao.save(restRoom);
                return R.success();
            }

            @Override
            public Object onFalse() {
              return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }

    @Override
    public Object fuckFlowByOnce(FuckFlow fuckFlow) throws MyException {
        return R.callBackRet(restRoomDao.findFirstByIp(fuckFlow.getIpAddress()), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                RestRoom restRoom = (RestRoom)data;
                Integer onceFlow = fuckFlow.getPeopleCounting().getEnter();
                if(Utils.isYeaterday(restRoom.getUpdateTime(),null) == -1){ //表示是同一天

                    if(onceFlow>0) { //只保留有客流的数据
                        //region 更新半小时内的数据
                        InfoPassengerFlow infoPassengerFlow = isSaveInterval
                                ?
                                iPFlowDao.findFirstByRestRoom_RestRoomIdOrderByUpdateTimeDesc(restRoom.getRestRoomId()).orElse(new InfoPassengerFlow())
                                :
                                new InfoPassengerFlow();

                        long diff = new Date().getHours() - infoPassengerFlow.getUpdateTime().getHours();
                        if (diff==0) {
                            infoPassengerFlow.setNumber(infoPassengerFlow.getNumber()+onceFlow);
                        }
                        else {
                            infoPassengerFlow = new InfoPassengerFlow();
                            infoPassengerFlow.setNumber(onceFlow);
                        }
                        infoPassengerFlow.setIp(fuckFlow.getIpAddress());
                        infoPassengerFlow.setRestRoom(restRoom);
                        infoPassengerFlow.setUpdateTime(new Date());
                        //endregion
                        iPFlowDao.save(infoPassengerFlow);
                        restRoom.setPeopleNum(restRoom.getPeopleNum() + onceFlow);
                    }
                }
                else {
                    restRoom.setPeopleNum(onceFlow);

                    InfoPassengerFlow i =new InfoPassengerFlow();
                    i.setNumber(onceFlow);
                    i.setRestRoom(restRoom);
                    i.setIp(fuckFlow.getIpAddress());
                    iPFlowDao.save(i);
                }
                restRoom.setUpdateTime(new Date());
                restRoomDao.save(restRoom);
                return R.success();
            }

            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }

    @Override
    public Object getFuckFlow(Integer restRoomId, Integer type, String startTime, String endTime) throws MyException {
        if(type==0) return R.success(iPFlowDao.findAll(restRoomId,startTime,endTime),iPFlowDao.findAllSumNumber(restRoomId,startTime,endTime));
        else if(type==1) return R.success(iPFlowDao.findAllOnlyShowDays(restRoomId,startTime,endTime),iPFlowDao.findAllSumNumber(restRoomId,startTime,endTime));
        return R.success(iPFlowDao.findAllOnlyShowDays(restRoomId,startTime,endTime),iPFlowDao.findAllSumNumber(restRoomId,startTime,endTime));
    }

    @Override
    public Object getOnlyFuckFlow(Integer restRoomId) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                RetOnlyFuckFlow ret =new RetOnlyFuckFlow();
                ret.setToday(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getDayBegin().toString(), DateUtils.getDayEnd().toString()));
                ret.setMonth(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getBeginDayOfMonth().toString(), DateUtils.getEndDayOfMonth().toString()));
                ret.setAll(iPFlowDao.findAllSumNumberNoTime(restRoomId));
                return R.success(ret);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object getOnlyGasDeviceInfo(Integer restRoomId) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.success(deviceGasDao.findAllByRestRoomIdWithQuery(restRoomId));
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object getGasStatistic(Integer restRoomId, String startTime, String endTime) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.success(infoGasDailyStatisticsDao.findGasStatusOfDayCount(restRoomId, startTime, endTime));
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object getGasStatisticWithDay(Integer restRoomId, String startTime, String endTime) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.success(infoGasDailyStatisticsDao.findAllByRestRoom_RestRoomIdAndCreateTimeBetween(restRoomId, Utils.StrToDate(startTime),  Utils.StrToDate(endTime)));
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }



    List<Map<String, Object>> replace(List<Map<String, Object>> rest, Integer i, String key, Object object){
        Map<String, Object> one = rest.get(i);
        HashMap<String, Object> newOne = new HashMap<>();
        newOne.putAll(one);
        newOne.put(key,object);
        rest.remove(one);
        rest.add(i, newOne);
        return rest;
    }

    // region 更改周的通用数据
    List<Map<String, Object>> setWeek(List<Map<String, Object>> rest){
        for (int i =0;i< 7; i++) {
            if(i>=rest.size())break;
            switch (i){
                case 0:
                    rest = replace(rest, i, "day_of_week","周一");
                    break;
                case 1:
                    rest = replace(rest, i, "day_of_week","周二");
                    break;
                case 2:
                    rest = replace(rest, i, "day_of_week","周三");
                    break;
                case 3:
                    rest = replace(rest, i, "day_of_week","周四");
                    break;
                case 4:
                    rest = replace(rest, i, "day_of_week","周五");
                    break;
                case 5:
                    rest = replace(rest, i, "day_of_week","周六");
                    break;
                case 6:
                    rest = replace(rest, i, "day_of_week","周日");
                    break;
            }
        }
        return rest;
    }
    // endregion

    // region 更改月的通用数据
    List<Map<String, Object>> setMonth(List<Map<String, Object>> rest){
        for (int i =0;i< 31; i++) {
            if(i>=rest.size())break;
            rest = replace(rest, i, "day_of_month", rest.get(i).get("show_time").toString().split("-")[2]);
        }
        return rest;
    }
    // endregion

    @Override
    public Object getOnlyFuckFlowWithTypeAndWeek(Integer restRoomId) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                List<Map<String, Object>> rest = iPFlowDao.findAllOnlyShowDaysWithTitle("本周", restRoomId, DateUtils.getBeginDayOfWeek().toString(), DateUtils.getEndDayOfWeek().toString());
                rest = setWeek(rest);
                rest.addAll(0, iPFlowDao.findAllOnlyShowDaysWithTitle("上周", restRoomId, DateUtils.getBeginDayOfLastWeek().toString(), DateUtils.getEndDayOfLastWeek().toString()));
                RetFuckFlowContrast f = new RetFuckFlowContrast();
                f.setList(setWeek(rest));
                f.setThisWeek(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getBeginDayOfWeek().toString(), DateUtils.getEndDayOfWeek().toString()));
                f.setLastWeek(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getBeginDayOfLastWeek().toString(), DateUtils.getEndDayOfLastWeek().toString()));
                return R.success(f);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object getOnlyFuckFlowWithTypeAndMonth(Integer restRoomId) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                List<Map<String, Object>> rest = iPFlowDao.findAllOnlyShowDaysWithTitle("本月", restRoomId, DateUtils.getBeginDayOfMonth().toString(), DateUtils.getEndDayOfMonth().toString());
                rest = setMonth(rest);
                rest.addAll(0, iPFlowDao.findAllOnlyShowDaysWithTitle("上月", restRoomId, DateUtils.getBeginDayOfLastMonth().toString(), DateUtils.getEndDayOfLastMonth().toString()));
                RetFuckFlowContrast f = new RetFuckFlowContrast();
                f.setList(setMonth(rest));
                f.setThisMonth(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getBeginDayOfMonth().toString(), DateUtils.getEndDayOfMonth().toString()));
                f.setLastMonth(iPFlowDao.findAllSumNumber(restRoomId, DateUtils.getBeginDayOfLastMonth().toString(), DateUtils.getEndDayOfLastMonth().toString()));
                return R.success(f);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }


}

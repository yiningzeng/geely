package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.aspect.jwt.TokenUtils;
import com.baymin.restroomapi.dao.DeviceGasDao;
import com.baymin.restroomapi.dao.InfoPassengerFlowDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.dao.specs.RestRoomSpecs;
import com.baymin.restroomapi.entity.*;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
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

import java.util.Date;
import java.util.Optional;

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


}

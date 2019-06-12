package com.baymin.restroomapi.config.task;

import com.baymin.restroomapi.config.okhttp3.MyOkHttpClient;
import com.baymin.restroomapi.dao.*;
import com.baymin.restroomapi.entity.*;
import com.baymin.restroomapi.ret.model.GasInfo;
import com.baymin.restroomapi.utils.DateUtils;
import com.baymin.restroomapi.utils.ShellKit;
import com.baymin.restroomapi.utils.StreamGobblerCallback;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ScheduledTaskService {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RestRoomDao restRoomDao;
    @Autowired
    private DeviceCameraDao deviceCameraDao;
    @Autowired
    private DeviceBoardDao deviceBoardDao;
    @Autowired
    private DeviceGasDao deviceGasDao;
    @Autowired
    private InfoGasDao infoGasDao;
    @Autowired
    private InfoGasDailyStatisticsDao infoGasDailyStatisticsDao;
//    @Scheduled(fixedRate = 5000)//1
//    public void reportCurrentTime(){
////        System.out.println("每隔五秒执行一次 "+DATE_FORMAT.format(new Date()));
////        StreamGobblerCallback.Work work = new StreamGobblerCallback.Work();
////        try {
////            ShellKit.runShell("ping -w 1 192.168.31.1", work);
//////            long now = System.currentTimeMillis();
////            while (work.isDoing()){
////                Thread.sleep(100);
////            }
////            log.info("结束"+work.getRes());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }

    Integer checkIsOnline(String ip){
        StreamGobblerCallback.Work work = new StreamGobblerCallback.Work();
        try {
            ShellKit.runShell("ping -w 5 "+ip, work);
//            long now = System.currentTimeMillis();
            while (work.isDoing()){
                Thread.sleep(100);
            }
            log.info("结束"+work.getRes());
            if(work.getRes().contains("icmp_seq=") && work.getRes().contains("ttl=")){
                return 1;
            }
            else return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //@Scheduled(cron = "0 0/30 * * * ? ")//

    /**
     * 检查设备在线状态
     */
    @Scheduled(cron = "0 0/30 * * * ? ")//
    public void fixTimeExecution(){
        log.info("在指定时间 "+DATE_FORMAT.format(new Date())+"执行");
        List<RestRoom> restRoomList= restRoomDao.findAll();
        for (RestRoom r:restRoomList) {
            //region 检查门口相机在线状态
            for (DeviceCamera d:deviceCameraDao.findAllByRestRoom_RestRoomId(r.getRestRoomId())) {
                d.setOnline(checkIsOnline(d.getIp()));
                deviceCameraDao.save(d);
            }
            //endregion

            //region 检查公告屏在线状态
            for (DeviceBoard d:deviceBoardDao.findAllByRestRoom_RestRoomId(r.getRestRoomId())) {
                d.setOnline(checkIsOnline(d.getIp()));
                deviceBoardDao.save(d);
            }
            //endregion
        }
    }

    /**
     * 收集气体数据
     * 暂定5分钟刷新一次
     */
    @Scheduled(cron = "0 0/30 * * * ? ")//
    public void reFreshGasData(){
        Date newDate=new Date();
        log.info("在指定时间 "+DATE_FORMAT.format(newDate)+" 收集气体数据");
        List<RestRoom> restRoomList= restRoomDao.findAll();
        for (RestRoom r:restRoomList) {
            for (DeviceGas d:deviceGasDao.findAllByRestRoom_RestRoomId(r.getRestRoomId())) {
                GasInfo gasInfo= new Gson().fromJson(
                        MyOkHttpClient.getInstance().get("http://servers.aqsystems.net/aks/termdata/getTermData?funcId="+d.getGasDeviceId()),
                        GasInfo.class);
                if(gasInfo.getData().getItems().size()==0) continue;
                InfoGas infoGas = new InfoGas();
                infoGas.setDeviceGas(d);
                infoGas.setFuncId(d.getGasDeviceId());
                infoGas.setRestRoom(r);
                infoGas.setType(d.getType());
                infoGas.setScore(gasInfo.getData().getItems().get(0).getZq());
                infoGas.setUpdateTime(newDate);
                infoGasDao.save(infoGas);
                d.setScore(infoGas.getScore());
                d.setTemperature(gasInfo.getData().getItems().get(0).getEa());
                deviceGasDao.save(d);
            }
        }
    }

    /**
     * 统计气体数据
     * 每日23点45分开始统计
     */
    @Scheduled(cron = "0 45 23 * * ? ")//
    public void gasStatistics(){
        Date newDate=new Date();
        log.info("统计气体数据->每日23点45分开始统计 "+DATE_FORMAT.format(newDate)+" 收集气体数据");
        List<RestRoom> restRoomList= restRoomDao.findAll();
        for (RestRoom r:restRoomList) {
            Float avg = infoGasDao.getAvgByRestRoomId(r.getRestRoomId(), DateUtils.getDayBegin().toString(), DateUtils.getDayEnd().toString());
            InfoGasDailyStatistics aa=new InfoGasDailyStatistics();
            aa.setRestRoom(r);
            aa.setScore(avg);
            if(0<avg && avg<=3)aa.setRemark("优秀");
            else if(3<avg && avg<=5)aa.setRemark("良好");
            else if(5<avg && avg<=7)aa.setRemark("一般");
            else if(7<avg && avg<=9)aa.setRemark("很差");
            else if(9<avg)aa.setRemark("极差");
            infoGasDailyStatisticsDao.save(aa);
        }
    }

    @Scheduled(cron = "0 0/30 * * * ? ")//
    public void mysqlBackup(){
        log.info("数据库备份 "+new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date())+"执行");
        StreamGobblerCallback.Work work = new StreamGobblerCallback.Work();
        try {
            ShellKit.runShell("/opt/lampp/bin/mysqldump -u root restroom --result-file=\"/baymin/mysql-restroom-bak/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql\" ", work);
//            long now = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

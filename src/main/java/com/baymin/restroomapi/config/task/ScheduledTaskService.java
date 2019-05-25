package com.baymin.restroomapi.config.task;

import com.baymin.restroomapi.dao.DeviceBoardDao;
import com.baymin.restroomapi.dao.DeviceBoardServiceDao;
import com.baymin.restroomapi.dao.DeviceCameraDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.entity.DeviceBoard;
import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.utils.ShellKit;
import com.baymin.restroomapi.utils.StreamGobblerCallback;
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
            ShellKit.runShell("ping -w 1 "+ip, work);
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

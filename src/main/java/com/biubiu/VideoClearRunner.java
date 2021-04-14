package com.biubiu;

import com.biubiu.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/14 上午9:26
 * @description：清理超过3天的文件
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
@Component
public class VideoClearRunner {

    private static final Logger logger = LoggerFactory.getLogger(VideoClearRunner.class);

    @Value("${ffmpeg.video.folder}")
    private String videoFolder;

    @Scheduled(cron = "0/1 * * * * *")
    public void clean() {
        logger.info("开始删除文件...");
        long startTime = System.currentTimeMillis();
        // 删除文件
        File deleteFile = new File(videoFolder);
        File[] deleteFiles = deleteFile.listFiles();
        for (File file : deleteFiles) {
            Date createDate = new Date(Long.parseLong(file.getName()));
            Instant instant = createDate.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            // 3 day ago
            LocalDateTime threeDayAgo = LocalDateTime.now().minusDays(3);
            // 删除3天前的文件
            if(localDateTime.isBefore(threeDayAgo)) {
                FileUtil.deleteFile(file);
            }
        }
        logger.info("删除文件结束,总耗时：[{}]毫秒", System.currentTimeMillis() - startTime);
    }

}
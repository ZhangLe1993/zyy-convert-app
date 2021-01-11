package com.biubiu.utils;

import com.biubiu.core.DeLogo;
import com.biubiu.exception.FFmpegException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FfmpegUtils {


    /* public static Boolean ffmpeg(String inputBase64, String outputBase64) throws FFmpegException {

        if (!checkFile(inputBase64)) {
            throw new FFmpegException("文件格式不合法");
        }
        List<String> command = getFfmpegCommand(type, ffmpegPath, inputPath, outputPath);
        if (null != command && command.size() > 0) {
            return process(command);
        }
        return false;
    }*/

    /*private static boolean checkFile(String base64) {
        return true;
    }*/

    private static boolean process(List<String> command) throws FFmpegException {
        try {
            if (null == command || command.size() == 0) {
                return false;
            }
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            new PrintStream(process.getErrorStream()).start();
            new PrintStream(process.getInputStream()).start();
            int exitCode = process.waitFor();
            if (exitCode == 1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new FFmpegException("文件处理异常", e);
        }

    }

    /**
     * 去除图片水印
     *
     * @param inputBase64
     * @param type
     * @param list
     * @return
     * @throws IOException
     */
    public static Map<String, Object> deLogoPictureWater(String inputBase64, String type, String targetFolder, List<DeLogo> list) throws IOException, FFmpegException {
        // delogo=x=300:y=250:w=56:h=18:show=0
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        File tempFile = FileUtil.createTempFile(type);
        FileOutputStream fos = new FileOutputStream(tempFile);
        boolean image = FileUtil.writeBase64ToFile(inputBase64, fos);
        if (!image) {
            return ImmutableMap.of("success", false, "base64", "");
        }
        // 文件路径
        command.add(tempFile.getCanonicalPath());
        command.add("-strict");
        command.add("-2");
        command.add("-vf");
        String deLogo = buildDeLogo(list);
        command.add(deLogo);
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String fileName = uuid + "." + type;
        command.add(targetFolder + fileName);

        boolean mark = true;
        if (command.size() > 0) {
            mark = process(command);
        }
        // 删除临时文件
        tempFile.deleteOnExit();
        String base64 = FileUtil.file2Base64(new File(targetFolder + fileName));
        return ImmutableMap.of("success", mark, "base64", base64);
    }

    private static String buildDeLogo(List<DeLogo> list) {
        List<String> deLogos = new LinkedList<>();
        for (DeLogo delogo : list) {
            StringBuffer sb = new StringBuffer("delogo=");
            sb.append("x=")
                    .append(delogo.getX().toString())
                    .append(":")
                    .append("y=")
                    .append(delogo.getY().toString())
                    .append(":")
                    .append("w=")
                    .append(delogo.getWidth().toString())
                    .append(":")
                    .append("h=")
                    .append(delogo.getHeight().toString())
                    .append(":show=0")
            ;
            deLogos.add(sb.toString());
        }
        return StringUtils.join(deLogos, ",");
    }

    static class PrintStream extends Thread {
        java.io.InputStream __is = null;

        public PrintStream(java.io.InputStream is) {
            __is = is;
        }

        public void run() {
            try {
                while (this != null) {
                    int _ch = __is.read();
                    if (_ch == -1) {
                        break;
                    } else {
                        System.out.print((char) _ch);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

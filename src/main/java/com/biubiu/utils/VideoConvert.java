package com.biubiu.utils;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_H264;

/**
 * @author ：张音乐
 * @date ：Created in 2021/4/12 上午11:04
 * @description：网络视频流转换成本地文件
 * @email: zhangyule1993@sina.com
 * @version: 1.0
 */
public class VideoConvert {

    /**
     *
     */
    public final static String NORMAL_VIDEO_TYPE = "mp4";

    /**
     * 转存视频流
     * @param input
     * @param outFile
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public static void record(String input, String outFile) throws FrameGrabber.Exception, FrameRecorder.Exception {
        if(StringUtils.isBlank(input) || StringUtils.isBlank(outFile)) {
            System.out.println("input or output is null");
            return;
        }
        FrameGrabber grabber = new FFmpegFrameGrabber(input);
        grabber.start();
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        Frame frame = grabber.grab();
        opencv_core.IplImage image = null;
        if(frame == null) {
            System.out.println("第一帧为空,请检查视频源");
            return;
        }
        image = converter.convert(frame);
        FrameRecorder recorder = FrameRecorder.createDefault(outFile, frame.imageWidth, frame.imageHeight);
        recorder.setVideoCodec(AV_CODEC_ID_H264);
        recorder.setFormat(NORMAL_VIDEO_TYPE);
        recorder.setFrameRate(25);
        recorder.setGopSize(25);
        // start中其实做了很多事情：一堆初始化操作、打开网络流、查找编码器、把format字符转换成对应的videoCodec和videoFormat等等。
        recorder.start();
        Frame saveFrame;
        while((frame = grabber.grab()) != null) {
            saveFrame = converter.convert(image);
            // 获取类型, 视频或者音频
            // EnumSet<Frame.Type> videoOrAudio = saveFrame.getTypes();
            // 录制视频
            // 编码方式录制，把已经解码的图像像素编码成对应的编码和格式推流出去
            recorder.record(saveFrame);
        }
        recorder.close();
    }
}
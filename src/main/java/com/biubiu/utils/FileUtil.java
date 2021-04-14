package com.biubiu.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 文件byte[]类型转File
     *
     * @param fileBytes
     * @param filePath
     * @param fileName
     * @return
     */
    public static File byte2File(byte[] fileBytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) { //判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 文件File类型转byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] file2Byte(String filePath) {
        byte[] fileBytes = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * 文件File类型转byte[]
     *
     * @param file
     * @return
     */
    public static byte[] file2Byte(File file) {
        byte[] fileBytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * 文件File类型转BASE64
     *
     * @param file
     * @return
     */
    public static String file2Base64(File file) {
        return Base64.getEncoder().encodeToString(file2Byte(file));
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println(file2Base64(new File("/home/yinyue/桌面/经销商服务费冻结.xlsx")));
    }

    /**
     * base64转化成 inputStream
     *
     * @param base64
     * @return
     */
    public static InputStream base64ToInputStream(String base64) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * inputStream 转化成 base64
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String inputStreamToBase64(InputStream inputStream) throws IOException {
        return Base64.getEncoder().encodeToString(inputStreamToBytes(inputStream));
    }

    public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int index = 0;
        while((index = inputStream.read(buff, 0, 4096)) > 0) {
            baos.write(buff, 0, index);
        }
        return baos.toByteArray();
    }

    /**
     * 计算文件hash值
     */
    public static String hashFile(File file) throws Exception {
        FileInputStream fis = null;
        String sha256 = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte buffer[] = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] digest = md.digest();
            sha256 = byte2hexLower(digest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("计算文件hash值错误");
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sha256;
    }

    private static String byte2hexLower(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0XFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    public static void zipDir(String sourcePath, String zipPath) throws Exception {
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipPath));
            File sourceDir = new File(sourcePath);
            File[] listFiles = sourceDir.listFiles();
            for (File file : listFiles) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(sourceDir.getName() + File.separator + file.getName()));
                // copy文件到zip输出流中
                int len;
                fis = new FileInputStream(file);
                byte[] buffer = new byte[2048];
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                    zos.flush();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (Exception e) {
            throw new Exception("创建ZIP文件失败");
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
            file.delete();
        }
    }

    public static File createTempFile(String fileType) throws IOException {
        String tempFileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return File.createTempFile(tempFileName, "." + fileType);
    }

    public static boolean writeBase64ToFile(String base64, FileOutputStream fos) {
        try{
            byte[] bytes = Base64.getDecoder().decode(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean generateImage(String base64, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null) // 图像数据为空
            return false;
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递归删除文件夹下所有文件
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            //递归删除文件夹下所有文件
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            //删除文件夹自己
            if (file.listFiles().length == 0) {
                logger.info("删除文件夹：[{}]", file);
                file.delete();
            }
        } else {
            // 如果是文件,就直接删除自己
            logger.info("删除文件：[{}]", file);
            file.delete();
        }
    }


}

package jdjt.com.homepager.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by xxd on 2017/9/7.
 * 操作文件的工具类
 */

public class FileUtil {


    /**
     * 基本文件操作，默认编码方式
     */
    public final static String FILE_READING_ENCODING = "UTF-8";
    public final static String FILE_WRITING_ENCODING = "UTF-8";

    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "".equals(str) || "null".equals(str);
    }

    public static File getFileByPath(String filePath) {
        return isNullString(filePath) ? null : new File(filePath);
    }

    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(File file) {
        boolean flag;
        label1:
        {
            if (file != null) {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        break label1;
                    }
                } else if (file.mkdirs()) {
                    break label1;
                }
            }

            flag = false;
            return flag;
        }

        flag = true;
        return flag;
    }

    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        } else if (file.exists()) {
            return file.isFile();
        } else if (!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    public static boolean createFileByDeleteOldFile(File file) {
        if(file == null) {
            return false;
        } else if(file.exists() && file.isFile() && !file.delete()) {
            return false;
        } else if(!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }



    // 写文件
    public static File writeFile(String path, String content, String encoding, boolean append) throws Exception {
        if (TextUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return writeFile(is, path, append);
    }

    /**
     * 写is到某个文件中
     *
     * @param is
     * @param path
     * @param append 如果文件已存在，是否在最后添加
     * @return
     * @throws Exception
     */
    public static File writeFile(InputStream is, String path, boolean append) throws Exception {

        File file = new File(path);
        boolean orExistsFile = createOrExistsFile(file);
        if(!orExistsFile){
            throw new RuntimeException("创建文件失败");
        }

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, append);
            int byteCount;
            byte[] bytes = new byte[1024];

            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.flush();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("写文件错误", e);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件，及其所有子目录文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return deleteFile(file);
    }

    /**
     * 删除文件，及其所有子目录文件
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) { // 是文件夹，遍历删除
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        if (!deleteFile(files[i])) {
                            return false;
                        }
                    } else {
                        if (!files[i].delete()) {
                            return false;
                        }
                    }
                }
            }
            if (!file.delete()) {
                return false;
            }
        } else {  // 是文件，直接删除
            return file.delete();
        }
        return true;
    }

    /**
     * 解压zip文件到指定文件夹，不支持中文和密码
     *
     * @param zipFileString
     * @param outPathString
     * @throws Exception
     */
    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                boolean orExistsDir = createOrExistsDir(folder);
                if(!orExistsDir){
                    throw new RuntimeException("创建文件夹失败");
                }
            } else {

                File file = new File(outPathString + File.separator + szName);
                boolean orExistsFile = createOrExistsFile(file);
                if(!orExistsFile){
                    throw new RuntimeException("创建文件失败");
                }
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static void unZipFolder(InputStream inputStream, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                boolean orExistsDir = createOrExistsDir(folder);
                if(!orExistsDir){
                    throw new RuntimeException("创建文件夹失败");
                }
            } else {

                File file = new File(outPathString + File.separator + szName);
                boolean orExistsFile = createOrExistsFile(file);
                if(!orExistsFile){
                    throw new RuntimeException("创建文件失败");
                }
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    /**
     * 获取路径下的第一个文件名称
     *
     * @param path
     * @return
     */
    public static String getFirstFileName(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory())
            return null;
        String[] list = file.list();
        if (list == null || list.length == 0)
            return null;
        return list[0];
    }

    public static String getFirstEndWith(String path, String end) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory())
            return null;
        String[] list = file.list();
        if (list == null || list.length == 0)
            return null;
        for (String s : list) {
            String[] strs = s.split("\\.");
            if (end.equalsIgnoreCase(strs[strs.length - 1])) {
                return s;
            }
        }
        return null;
    }


}

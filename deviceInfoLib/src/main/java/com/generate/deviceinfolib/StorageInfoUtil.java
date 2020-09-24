package com.generate.deviceinfolib;

import android.Manifest;
import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.generate.StringUtil.isEmptyText;
import static com.generate.deviceinfolib.Common.checkPermission;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class StorageInfoUtil {

    private Context context;

    public StorageInfoUtil(Context context) {
        this.context = context;
    }

    public JSONObject getCommStorageInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ram_total_size", isEmptyText(getRamTotalSize()));
        jsonObject.put("ram_usable_size", isEmptyText(getRamAvailSize()));
        jsonObject.put("external_storage", isEmptyText(getExternalStorageDirectory()));
        jsonObject.put("main_storage", isEmptyText(getRootDirectory()));
        jsonObject.put("memory_card_size", isEmptyText(getSDInfo().get("totalSize").toString()));
        jsonObject.put("memory_card_size_use", isEmptyText(getSDInfo().get("useSize").toString()));
        jsonObject.put("internal_storage_total", isEmptyText(getTotalInternalMemorySize() + ""));
        jsonObject.put("internal_storage_usable", isEmptyText(getAvailableInternalMemorySize() + ""));
        jsonObject.put("storageCCTotalSize", isEmptyText(getStorageTotalSize()));
        return jsonObject;
    }


    public JSONArray getCommAudioExternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String[] COLUMNS = new String[]{MediaStore.Audio.AudioColumns.DATE_ADDED, MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                        MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.MIME_TYPE,
                        MediaStore.Audio.AudioColumns.IS_MUSIC, MediaStore.Audio.AudioColumns.YEAR,
                        MediaStore.Audio.AudioColumns.IS_NOTIFICATION, MediaStore.Audio.AudioColumns.IS_RINGTONE,
                        MediaStore.Audio.AudioColumns.IS_ALARM};
                Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED));
                    String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                    String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.MIME_TYPE));
                    String is_music = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
                    String year = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.YEAR));
                    String is_notification = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_NOTIFICATION));
                    String is_ringtone = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_RINGTONE));
                    String is_alarm = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_ALARM));
                    jsonObject.put("is_music", isEmptyText(is_music));
                    jsonObject.put("date_added", isEmptyText(date_added));
                    jsonObject.put("date_modified", isEmptyText(date_modified));
                    jsonObject.put("duration", isEmptyText(duration));
                    jsonObject.put("mime_type", isEmptyText(mime_type));
                    jsonObject.put("year", isEmptyText(year));
                    jsonObject.put("is_notification", isEmptyText(is_notification));
                    jsonObject.put("is_ringtone", isEmptyText(is_ringtone));
                    jsonObject.put("is_alarm", isEmptyText(is_alarm));
                    jsonArray.add(jsonObject);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommAudioInternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            String[] COLUMNS = new String[]{MediaStore.Audio.AudioColumns.DATE_ADDED, MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                    MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.AudioColumns.MIME_TYPE,
                    MediaStore.Audio.AudioColumns.IS_MUSIC, MediaStore.Audio.AudioColumns.YEAR,
                    MediaStore.Audio.AudioColumns.IS_NOTIFICATION, MediaStore.Audio.AudioColumns.IS_RINGTONE,
                    MediaStore.Audio.AudioColumns.IS_ALARM};
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, COLUMNS, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            while (cursor != null && cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED));
                String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.MIME_TYPE));
                String is_music = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
                String year = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.YEAR));
                String is_notification = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_NOTIFICATION));
                String is_ringtone = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_RINGTONE));
                String is_alarm = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_ALARM));
                jsonObject.put("is_music", isEmptyText(is_music));
                jsonObject.put("date_added", isEmptyText(date_added));
                jsonObject.put("date_modified", isEmptyText(date_modified));
                jsonObject.put("duration", isEmptyText(duration));
                jsonObject.put("mime_type", isEmptyText(mime_type));
                jsonObject.put("year", isEmptyText(year));
                jsonObject.put("is_notification", isEmptyText(is_notification));
                jsonObject.put("is_ringtone", isEmptyText(is_ringtone));
                jsonObject.put("is_alarm", isEmptyText(is_alarm));
                jsonArray.add(jsonObject);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommImagesInternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            String[] COLUMNS = new String[]{MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATE_ADDED,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.ImageColumns.HEIGHT,
                    MediaStore.Images.ImageColumns.WIDTH, MediaStore.Images.ImageColumns.LATITUDE,
                    MediaStore.Images.ImageColumns.LONGITUDE, MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.TITLE, MediaStore.Images.ImageColumns.SIZE};
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, COLUMNS, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                String date_taken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
                String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
                String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                String height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                String width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                String latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
                String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
                jsonObject.put("datetaken", isEmptyText(date_taken));
                jsonObject.put("date_added", isEmptyText(date_added));
                jsonObject.put("date_modified", isEmptyText(date_modified));
                jsonObject.put("height", isEmptyText(height));
                jsonObject.put("width", isEmptyText(width));
                jsonObject.put("latitude", isEmptyText(latitude));
                jsonObject.put("longitude", isEmptyText(longitude));
                jsonObject.put("mime_type", isEmptyText(mime_type));
                jsonObject.put("title", isEmptyText(title));
                jsonObject.put("size", isEmptyText(size));
                jsonArray.add(jsonObject);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommImagesExternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String[] COLUMNS = new String[]{MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATE_ADDED,
                        MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.ImageColumns.HEIGHT,
                        MediaStore.Images.ImageColumns.WIDTH, MediaStore.Images.ImageColumns.LATITUDE,
                        MediaStore.Images.ImageColumns.LONGITUDE, MediaStore.Images.ImageColumns.MIME_TYPE,
                        MediaStore.Images.ImageColumns.TITLE, MediaStore.Images.ImageColumns.SIZE};
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String date_taken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
                    String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
                    String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));
                    String height = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                    String width = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                    String latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
                    String longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
                    String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
                    jsonObject.put("datetaken", isEmptyText(date_taken));
                    jsonObject.put("date_added", isEmptyText(date_added));
                    jsonObject.put("date_modified", isEmptyText(date_modified));
                    jsonObject.put("height", isEmptyText(height));
                    jsonObject.put("width", isEmptyText(width));
                    jsonObject.put("latitude", isEmptyText(latitude));
                    jsonObject.put("longitude", isEmptyText(longitude));
                    jsonObject.put("mime_type", isEmptyText(mime_type));
                    jsonObject.put("title", isEmptyText(title));
                    jsonObject.put("_size", isEmptyText(size));
                    jsonArray.add(jsonObject);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommVideoInternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            String[] COLUMNS = new String[]{MediaStore.Video.VideoColumns.DATE_ADDED, MediaStore.Video.VideoColumns.DATE_MODIFIED,
                    MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns.DESCRIPTION,
                    MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns.IS_PRIVATE,
                    MediaStore.Video.VideoColumns.LANGUAGE, MediaStore.Video.VideoColumns.MIME_TYPE,
                    MediaStore.Video.VideoColumns.RESOLUTION, MediaStore.Video.VideoColumns.SIZE,
                    MediaStore.Video.VideoColumns.TAGS, MediaStore.Video.VideoColumns.LATITUDE,
                    MediaStore.Video.VideoColumns.LONGITUDE, MediaStore.Video.VideoColumns.TITLE};
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, COLUMNS, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));
                String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
                String date_taken = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN));
                String description = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DESCRIPTION));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                String is_private = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.IS_PRIVATE));
                String language = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LANGUAGE));
                String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE));
                String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE));
                String tags = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TAGS));
                String latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LONGITUDE));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
                jsonObject.put("date_added", isEmptyText(date_added));
                jsonObject.put("date_modified", isEmptyText(date_modified));
                jsonObject.put("datetaken", isEmptyText(date_taken));
                jsonObject.put("description", isEmptyText(description));
                jsonObject.put("duration", isEmptyText(duration));
                jsonObject.put("is_private", isEmptyText(is_private));
                jsonObject.put("language", isEmptyText(language));
                jsonObject.put("mime_type", isEmptyText(mime_type));
                jsonObject.put("resolution", isEmptyText(resolution));
                jsonObject.put("_size", isEmptyText(size));
                jsonObject.put("tags", isEmptyText(tags));
                jsonObject.put("latitude", isEmptyText(latitude));
                jsonObject.put("longitude", isEmptyText(longitude));
                jsonObject.put("title", isEmptyText(title));
                jsonArray.add(jsonObject);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommVideoExternal() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String[] COLUMNS = new String[]{MediaStore.Video.VideoColumns.DATE_ADDED, MediaStore.Video.VideoColumns.DATE_MODIFIED,
                        MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns.DESCRIPTION,
                        MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns.IS_PRIVATE,
                        MediaStore.Video.VideoColumns.LANGUAGE, MediaStore.Video.VideoColumns.MIME_TYPE,
                        MediaStore.Video.VideoColumns.RESOLUTION, MediaStore.Video.VideoColumns.SIZE,
                        MediaStore.Video.VideoColumns.TAGS, MediaStore.Video.VideoColumns.LATITUDE,
                        MediaStore.Video.VideoColumns.LONGITUDE, MediaStore.Video.VideoColumns.TITLE};
                Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String date_added = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));
                    String date_modified = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
                    String date_taken = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN));
                    String description = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DESCRIPTION));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                    String is_private = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.IS_PRIVATE));
                    String language = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LANGUAGE));
                    String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE));
                    String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE));
                    String tags = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TAGS));
                    String latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LATITUDE));
                    String longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.LONGITUDE));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
                    jsonObject.put("date_added", isEmptyText(date_added));
                    jsonObject.put("date_modified", isEmptyText(date_modified));
                    jsonObject.put("datetaken", isEmptyText(date_taken));
                    jsonObject.put("description", isEmptyText(description));
                    jsonObject.put("duration", isEmptyText(duration));
                    jsonObject.put("is_private", isEmptyText(is_private));
                    jsonObject.put("language", isEmptyText(language));
                    jsonObject.put("mime_type", isEmptyText(mime_type));
                    jsonObject.put("resolution", isEmptyText(resolution));
                    jsonObject.put("_size", isEmptyText(size));
                    jsonObject.put("tags", isEmptyText(tags));
                    jsonObject.put("latitude", isEmptyText(latitude));
                    jsonObject.put("longitude", isEmptyText(longitude));
                    jsonObject.put("title", isEmptyText(title));
                    jsonArray.add(jsonObject);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommDownloadFile() {
        JSONArray jsonArray = new JSONArray();
        try {
            File fileDonwload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File[] files = fileDonwload.listFiles();
            List<JSONObject> list = getFileName(files);
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject obj = list.get(i);
                    jsonArray.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private static List<JSONObject> getFileName(File[] files) {
        List<JSONObject> list = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    List<JSONObject> list2 = getFileName(file.listFiles());
                    list.addAll(list2);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("file_name", file.getName());
                    jsonObject.put("file_type", MimeTypeMap.getFileExtensionFromUrl(file.getName()));
                    jsonObject.put("length", file.length());
                    jsonObject.put("last_modified", file.lastModified());
                    list.add(jsonObject);
                }
            }
        }
        return list;
    }

    private String getRamTotalSize() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem + "";
    }

    private String getRamAvailSize() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem + "";
    }

    private String getExternalStorageDirectory() {
        return System.getenv("SECONDARY_STORAGE");
    }

    private String getRootDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private JSONObject getSDInfo() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            JSONObject jsonObject = new JSONObject();
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            long blockSize = sf.getBlockSize();
            long totalBlock = sf.getBlockCount();
            long availableBlock = sf.getAvailableBlocks();
            long totalLong = totalBlock * blockSize;
            long freeLong = availableBlock * blockSize;
            long useLong = totalLong - freeLong;
            jsonObject.put("totalSize", totalLong);
            jsonObject.put("freeSize", freeLong);
            jsonObject.put("useSize", useLong);
            return jsonObject;
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
            return null;
        }
        return null;
    }

    private long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private String getStorageTotalSize() {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        int version = Build.VERSION.SDK_INT;
        if (version < Build.VERSION_CODES.M) {
            try {
                Method getVolumeList = StorageManager.class.getDeclaredMethod("getVolumeList");
                StorageVolume[] volumeList = (StorageVolume[]) getVolumeList.invoke(storageManager);
                long totalSize = 0;
                if (volumeList != null) {
                    Method getPathFile = null;
                    for (StorageVolume volume : volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.getClass().getDeclaredMethod("getPathFile");
                        }
                        File file = (File) getPathFile.invoke(volume);
                        totalSize += file.getTotalSpace();
                    }
                }
                return totalSize + "";
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        if (version >= Build.VERSION_CODES.M) {
            try {
                Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes");
                List<Object> getVolumeInfo = (List<Object>) getVolumes.invoke(storageManager);
                long total = 0L;
                for (Object obj : getVolumeInfo) {
                    Field getType = obj.getClass().getField("type");
                    int type = getType.getInt(obj);
                    if (type == 1) {
                        long totalSize = 0L;
                        if (version >= Build.VERSION_CODES.O) {
                            Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid");
                            String fsUuid = (String) getFsUuid.invoke(obj);
                            totalSize = getTotalSize(context, fsUuid);
                        } else if (version >= Build.VERSION_CODES.N_MR1) {
                            Method getPrimaryStorageSize = StorageManager.class.getMethod("getPrimaryStorageSize");
                            totalSize = (long) getPrimaryStorageSize.invoke(storageManager);
                        }

                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);

                            if (totalSize == 0) {
                                totalSize = f.getTotalSpace();
                            }
                            total += totalSize;
                        }
                    } else if (type == 0) {
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);
                            total += f.getTotalSpace();
                        }
                    } else if (type == 2) {
                    }
                }
                return total + "";
            } catch (SecurityException e) {

            } catch (Exception e) {
                e.printStackTrace();
                return queryWithStatFs();
            }
        }
        return "";
    }

    private String queryWithStatFs() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockCount = statFs.getBlockCount();
        long blockSize = statFs.getBlockSize();
        return (blockSize * blockCount) + "";
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private long getTotalSize(Context context, String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

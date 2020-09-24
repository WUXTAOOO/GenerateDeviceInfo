package com.generate.deviceinfolib;

import android.content.Context;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class DeviceInfoLib {
    private static final String TAG = "DeviceInfoLib";

    private Context mContext;

    private static volatile DeviceInfoLib singleton;

    private String mGaid = "";

    private DeviceInfoLib() {

    }

    public static DeviceInfoLib getInstance() {
        if (singleton == null) {
            synchronized (DeviceInfoLib.class) {
                if (singleton == null) {
                    singleton = new DeviceInfoLib();
                }
            }
        }
        return singleton;
    }

    public void init(Context context, String gaid) {
        if (null == context) {
            throw new NullPointerException("context must be not empty");
        }
        mContext = context;
        mGaid = gaid;
    }


    public Map<String, Object> getCommonDeviceInfo() {
        if (null == mContext) {
            throw new NullPointerException("context must be not empty");
        }
        DeviceInfoUtil deviceInfo = new DeviceInfoUtil(mContext, mGaid);
        StorageInfoUtil storageInfo = new StorageInfoUtil(mContext);
        ContactInfoUtil contactInfo = new ContactInfoUtil(mContext);
        CalendarsInfoUtil calendarsInfo = new CalendarsInfoUtil(mContext);

        Map<String, Object> params = new HashMap<>();
        params.put("hardware", deviceInfo.getCommHardwareInfo());
        params.put("storage", storageInfo.getCommStorageInfo());
        params.put("general_data", deviceInfo.getCommGeneralDataInfo());
        params.put("sim_card", deviceInfo.getCommSimCardInfo());
        params.put("contact", contactInfo.getCommContact());
        params.put("location", deviceInfo.getCommLocation());
        params.put("battery_status", deviceInfo.getCommBatteryStatus());
        params.put("other_data", deviceInfo.getCommOtherDataInfo());
        params.put("application", deviceInfo.getCommApplication());
        params.put("contact_address", contactInfo.getCommContactAddress());
        params.put("contact_email", contactInfo.getCommContactEmail());
        params.put("contact_phone", contactInfo.getCommContactPhone());
        params.put("contact_group", contactInfo.getCommContactsGroup());
        params.put("calendars", calendarsInfo.getCommCalendars());
        params.put("calendar_events", calendarsInfo.getCommCalendarsEvent());
        params.put("calendar_attendees", calendarsInfo.getCommCalendarAttendees());
        params.put("calendar_reminders", calendarsInfo.getCommCalendarReminders());
        params.put("browser_android", deviceInfo.getCommBrowserAndroid());
        params.put("browser_chrome", deviceInfo.getCommBrowserChrome());
        params.put("audio_external", storageInfo.getCommAudioExternal());
        params.put("audio_internal", storageInfo.getCommAudioInternal());
        params.put("images_internal", storageInfo.getCommImagesInternal());
        params.put("images_external", storageInfo.getCommImagesExternal());
        params.put("video_internal", storageInfo.getCommVideoInternal());
        params.put("video_external", storageInfo.getCommVideoExternal());
        params.put("download_files", storageInfo.getCommDownloadFile());
        params.put("registered_accounts", deviceInfo.getCommRegisteredAccounts());
        params.put("voice", deviceInfo.getCommVoice());
        params.put("network", deviceInfo.getCommNetwork());
        params.put("bluetooth", deviceInfo.getCommBluetooth());
        params.put("sensor", deviceInfo.getCommSensor());
        params.put("appinfo", deviceInfo.getCommAppInfo());
        params.put("sms", deviceInfo.getCommSms());
        params.put("call", deviceInfo.getCommCalls());
        return params;
    }


    public void test() {
        Map<String, Object> map = getCommonDeviceInfo();
        String result = new JSONObject(map).toJSONString();
        try {
            //mode参数注意下,这里使用的Context.MODE_PRIVATE
            FileOutputStream outputStream = mContext.openFileOutput("testfile.txt", Context.MODE_PRIVATE);
            outputStream.write(result.getBytes());
            outputStream.close();
            Log.e("保存", "保存成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("保存", "保存失败");
        }
    }
}

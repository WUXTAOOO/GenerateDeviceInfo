package com.generate.deviceinfolib;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import static com.generate.StringUtil.isEmptyText;
import static com.generate.deviceinfolib.Common.SHA1;
import static com.generate.deviceinfolib.Common.checkDebug;
import static com.generate.deviceinfolib.Common.checkPermission;
import static com.generate.deviceinfolib.Common.checkProxy;
import static com.generate.deviceinfolib.Common.checkVPN;
import static com.generate.deviceinfolib.Common.getElapsedRealtime;
import static com.generate.deviceinfolib.Common.getTelephonyManager;
import static com.generate.deviceinfolib.Common.intToIpAddress;
import static com.generate.deviceinfolib.Common.isEmulator;
import static com.generate.deviceinfolib.Common.isRooted;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class DeviceInfoUtil {

    private Context context;

    private String gaid = "";

    public DeviceInfoUtil(Context context, String gaid) {
        this.context = context;
        this.gaid = gaid;
    }

    public JSONObject getCommHardwareInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", isEmptyText(getDriverModel()));
        jsonObject.put("brand", isEmptyText(getDriverBrand()));
        jsonObject.put("device_name", isEmptyText(getDriverBrand()));
        jsonObject.put("product", isEmptyText(getDriverProduct()));
        jsonObject.put("release", isEmptyText(getDriverOsVersion()));
        jsonObject.put("sdk_version", isEmptyText(getDriverSDKVersion()));
        jsonObject.put("physical_size", isEmptyText(getPhysicalSize()));
        jsonObject.put("serial_number", isEmptyText(getSerialNumber()));
        jsonObject.put("cpu_type", isEmptyText(getCpuName()));

        // 新增
        jsonObject.put("modelName", isEmptyText(getDriverModel()));
        jsonObject.put("handSetMakers", isEmptyText(getDriverProduct()));
        jsonObject.put("manufacturerName", isEmptyText(getDriverBrand()));
        jsonObject.put("board", isEmptyText(getDriverBoard()));
        jsonObject.put("serial", isEmptyText(getDriverSerial()));
        jsonObject.put("display", isEmptyText(getDriverDisplay()));
        jsonObject.put("id", isEmptyText(getDriverID()));
        jsonObject.put("bootloader", isEmptyText(getDriverBootloader()));
        jsonObject.put("fingerPrint", isEmptyText(getDriverFingerprint()));
        jsonObject.put("host", isEmptyText(getDriverHost()));
        jsonObject.put("hardWare", isEmptyText(getDriverHardWare()));
        jsonObject.put("device", isEmptyText(getDriverDevice()));
        jsonObject.put("user", isEmptyText(getDriverUser()));
        jsonObject.put("radioVersion", isEmptyText(getRadioVersion()));
        jsonObject.put("tags", isEmptyText(getDriverTags()));
        jsonObject.put("time", isEmptyText(getDriverTime()));
        jsonObject.put("type", isEmptyText(getDriverType()));
        return jsonObject;
    }


    public JSONObject getCommGeneralDataInfo() {
        JSONObject jsonObject = new JSONObject();
        NetworkInfoUtil networkInfo = new NetworkInfoUtil(context);
        MacInfoUtil macInfo = new MacInfoUtil(context);

        jsonObject.put("deviceid", isEmptyText(getDeviceId()));
        jsonObject.put("and_id", isEmptyText(getAndroidID()));
        jsonObject.put("gaid", isEmptyText(gaid));
        jsonObject.put("network_operator_name", isEmptyText(getNetworkOperatorName()));
        jsonObject.put("network_operator", isEmptyText(getNetworkOperator()));
        jsonObject.put("network_type", isEmptyText(networkInfo.getNetWorkModel()));
        jsonObject.put("phone_type", isEmptyText(getPhoneType()));
        jsonObject.put("phone_number", isEmptyText(getLine1Number()));
        jsonObject.put("mcc", isEmptyText(getMCC()));
        jsonObject.put("mnc", isEmptyText(getMNC()));
        jsonObject.put("locale_iso_3_language", isEmptyText(getISO3Language()));
        jsonObject.put("locale_iso_3_country", isEmptyText(getISO3Country()));
        jsonObject.put("time_zone_id", isEmptyText(getTimeZoneId()));
        jsonObject.put("locale_display_language", isEmptyText(getLocaleDisplayLanguage()));
        jsonObject.put("imsi", isEmptyText(getImsi()));
        jsonObject.put("cid", isEmptyText(String.valueOf(getCellInfo().get("cid"))));
        jsonObject.put("dns", isEmptyText(getLocalDNS()));
        jsonObject.put("uuid", isEmptyText(getDriverUUID()));
        jsonObject.put("imei", isEmptyText(getDriverIMIE()));
        jsonObject.put("mac", isEmptyText(macInfo.getMac()));
        jsonObject.put("language", isEmptyText(getOsLanguage()));
        jsonObject.put("is_using_proxy_port", checkProxy());
        jsonObject.put("is_using_vpn", checkVPN());
        jsonObject.put("is_usb_debug", checkDebug(context));
        jsonObject.put("elapsed_realtime", getElapsedRealtime());
        return jsonObject;
    }


    public JSONObject getCommSimCardInfo() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("sim_country_iso", isEmptyText(getSimCountryIso()));
            jsonObj.put("sim_serial_number", isEmptyText(getSimSerialNumber()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }


    public JSONObject getCommLocation() {
        JSONObject jsonObject = new JSONObject();
        if (checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);

                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);
                double latitude = -1;
                double longitude = -1;
                try {
                    if (null != location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        JSONObject gpsObj = new JSONObject();
                        gpsObj.put("latitude", latitude);
                        gpsObj.put("longitude", longitude);
                        jsonObject.put("gps", gpsObj);
                    } else {
                        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        if (null != location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            JSONObject netObj = new JSONObject();
                            netObj.put("latitude", latitude);
                            netObj.put("longitude", longitude);
                            jsonObject.put("network", netObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String country = address.getCountryName();
                        String province = address.getAdminArea();
                        String city = address.getSubAdminArea();
                        String bigDirect = address.getLocality();
                        String smallDirect = address.getThoroughfare();
                        String detailed = address.getAddressLine(0);
                        jsonObject.put("gps_address_province", isEmptyText(province));
                        jsonObject.put("gps_address_city", isEmptyText(city));
                        jsonObject.put("gps_address_large_district", isEmptyText(bigDirect));
                        jsonObject.put("gps_address_small_district", isEmptyText(smallDirect));
                        jsonObject.put("gps_address_street", isEmptyText(detailed));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Criteria criteria = new Criteria();
                criteria.setCostAllowed(false);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String providerName = locationManager.getBestProvider(criteria, true);
                if (providerName != null) {
                    Location location = locationManager.getLastKnownLocation(providerName);
                    if (location != null) {
                        try {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            JSONObject locaObj = new JSONObject();
                            locaObj.put("latitude", latitude);
                            locaObj.put("longitude", longitude);
                            jsonObject.put("network", locaObj);
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses.size() > 0) {
                                Address address = addresses.get(0);
                                String country = address.getCountryName();
                                String province = address.getAdminArea();
                                String city = address.getSubAdminArea();
                                String bigDirect = address.getLocality();
                                String smallDirect = address.getThoroughfare();
                                String detailed = address.getAddressLine(0);

                                jsonObject.put("gps_address_province", isEmptyText(province));
                                jsonObject.put("gps_address_city", isEmptyText(city));
                                jsonObject.put("gps_address_large_district", isEmptyText(bigDirect));
                                jsonObject.put("gps_address_small_district", isEmptyText(smallDirect));
                                jsonObject.put("gps_address_street", isEmptyText(detailed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return jsonObject;
    }

    public JSONObject getCommBatteryStatus() {
        Intent batteryInfoIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int plugged = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int status = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int health = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        boolean present = batteryInfoIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        int voltage = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        int temperature = batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        String technology = batteryInfoIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        float batteryPct = level / (float) scale;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("battery_pct", Math.round(batteryPct * 100) / 100.0);
            jsonObject.put("health", health);
            jsonObject.put("temperature", temperature);
            jsonObject.put("present", present);
            jsonObject.put("status", status);
            jsonObject.put("technology", technology);
            jsonObject.put("plugged", plugged);
            jsonObject.put("scale", scale);
            jsonObject.put("level", level);
            jsonObject.put("voltage", voltage);
            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_AC: {
                    jsonObject.put("is_usb_charge", 0);
                    jsonObject.put("is_ac_charge", 1);
                    jsonObject.put("is_charging", 1);
                    break;
                }
                case BatteryManager.BATTERY_PLUGGED_USB: {
                    jsonObject.put("is_usb_charge", 1);
                    jsonObject.put("is_ac_charge", 0);
                    jsonObject.put("is_charging", 1);
                    break;
                }
                default: {
                    jsonObject.put("is_usb_charge", 0);
                    jsonObject.put("is_ac_charge", 0);
                    jsonObject.put("is_charging", 0);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCommOtherDataInfo() {
        JSONObject jsonObject = new JSONObject();
        List<String> sysPhotos = getSystemPhotoList();
        boolean isRoot = isRooted();
        boolean isEmulator = isEmulator();
        jsonObject.put("image_num", null == sysPhotos ? 0 : sysPhotos.size());
        jsonObject.put("root_jailbreak", isRoot ? 1 : 0);
        jsonObject.put("simulator", isEmulator ? 1 : 0);
        jsonObject.put("keyboard", isEmptyText(getKeyboard()));
        jsonObject.put("dbm", isEmptyText(String.valueOf(getCellInfo().get("dbm"))));
        jsonObject.put("last_boot_time", getLastBootTime());
        return jsonObject;
    }

    public JSONArray getCommApplication() {
        JSONArray jsonArray = new JSONArray();
        if (null == context) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (int i = 0; i < list.size(); i++) {
            PackageInfo packageInfo = list.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("app_name", isEmptyText(pm.getApplicationLabel(packageInfo.applicationInfo).toString()));
            jsonObject.put("package", isEmptyText(packageInfo.packageName));
            jsonObject.put("in_time", packageInfo.firstInstallTime);
            jsonObject.put("up_time", packageInfo.lastUpdateTime);
            jsonObject.put("version_name", isEmptyText(packageInfo.versionName));
            jsonObject.put("version_code", packageInfo.versionCode);
            jsonObject.put("flags", packageInfo.applicationInfo.flags);
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                jsonObject.put("app_type", 1);
            } else {
                jsonObject.put("app_type", 0);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray getCommBrowserChrome() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, "com.android.browser.permission.READ_HISTORY_BOOKMARKS")) {
            try {
                String[] COLUMNS = new String[]{"title", "url", "date", "bookmark", "visits"};
                Cursor cursor = context.getContentResolver().query(Uri.parse("content://browser/bookmarks"),
                        COLUMNS, null, null, "date desc");
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String bookmark = cursor.getString(cursor.getColumnIndex("bookmark"));
                    String visits = cursor.getString(cursor.getColumnIndex("visits"));
                    jsonObject.put("title", isEmptyText(title));
                    jsonObject.put("url", isEmptyText(url));
                    jsonObject.put("date", isEmptyText(date));
                    jsonObject.put("bookmark", isEmptyText(bookmark));
                    jsonObject.put("visits", isEmptyText(visits));
                    jsonArray.add(jsonObject);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public JSONArray getCommBrowserAndroid() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, "com.android.browser.permission.READ_HISTORY_BOOKMARKS")) {
            try {
                String[] COLUMNS = new String[]{"title", "url", "date", "bookmark", "visits"};
                Cursor cursor = context.getContentResolver().query(Uri.parse("content://browser/bookmarks"),
                        COLUMNS, null, null, "date desc");
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String bookmark = cursor.getString(cursor.getColumnIndex("bookmark"));
                    String visits = cursor.getString(cursor.getColumnIndex("visits"));
                    jsonObject.put("title", isEmptyText(title));
                    jsonObject.put("url", isEmptyText(url));
                    jsonObject.put("date", isEmptyText(date));
                    jsonObject.put("bookmark", isEmptyText(bookmark));
                    jsonObject.put("visits", isEmptyText(visits));
                    jsonArray.add(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public JSONArray getCommRegisteredAccounts() {
        JSONArray jsonArray = new JSONArray();
        try {
            AccountManager accountManager = AccountManager.get(context);
            Account[] accounts = accountManager.getAccounts();
            for (Account account : accounts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("account", account.name);
                jsonObject.put("type", account.type);
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONObject getCommVoice() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject callObj = new JSONObject();
            callObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
            callObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
            JSONObject systemObj = new JSONObject();
            systemObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
            systemObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
            JSONObject ringObj = new JSONObject();
            ringObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
            ringObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_RING));
            JSONObject musicObj = new JSONObject();
            musicObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            musicObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            JSONObject alertObj = new JSONObject();
            alertObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
            alertObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("max", audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
            notificationObj.put("current", audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));

            jsonObject.put("call", callObj);
            jsonObject.put("system", systemObj);
            jsonObject.put("ring", ringObj);
            jsonObject.put("music", musicObj);
            jsonObject.put("alert", alertObj);
            jsonObject.put("notification", notificationObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCommNetwork() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject current_wifi = new JSONObject();
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (null != wifiInfo) {
                jsonObject.put("IP", intToIpAddress(wifiInfo.getIpAddress()));
                current_wifi.put("bssid", isEmptyText(wifiInfo.getBSSID()));
                current_wifi.put("ssid", isEmptyText(wifiInfo.getSSID()));
                current_wifi.put("name", isEmptyText(wifiInfo.getSSID()));
                current_wifi.put("mac", isEmptyText(wifiInfo.getMacAddress()));
            }
            jsonObject.put("current_wifi", current_wifi);

            JSONArray configured_wifi = new JSONArray();
            List<ScanResult> scanResults = wifiManager.getScanResults();
            if (scanResults != null) {
                for (int i = 0; i < scanResults.size(); i++) {
                    ScanResult scanResult = scanResults.get(i);
                    JSONObject scanObj = new JSONObject();
                    scanObj.put("bssid", isEmptyText(scanResult.SSID));
                    scanObj.put("ssid", isEmptyText(scanResult.BSSID));
                    scanObj.put("name", isEmptyText(scanResult.SSID));
                    scanObj.put("mac", isEmptyText(scanResult.BSSID));
                    configured_wifi.add(scanObj);
                }
            }
            jsonObject.put("configured_wifi", configured_wifi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getCommBluetooth() {
        JSONObject jsonObject = new JSONObject();
        if (checkPermission(context, Manifest.permission.BLUETOOTH)) {
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String name = bluetoothAdapter.getName();
                int state = bluetoothAdapter.getState();
                String macAddress = getBlueToothAddress(bluetoothAdapter);
                jsonObject.put("name", isEmptyText(name));
                jsonObject.put("state", state);
                jsonObject.put("mac_address", isEmptyText(macAddress));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private String getBlueToothAddress(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter.isEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Field field;
            try {
                field = BluetoothAdapter.class.getDeclaredField("mService");
                field.setAccessible(true);
                Object bluetoothManagerService = field.get(bluetoothAdapter);
                if (bluetoothManagerService == null) {
                    return null;
                }
                Method method = bluetoothManagerService.getClass().getMethod("getAddress");
                if (method != null) {
                    Object obj = method.invoke(bluetoothManagerService);
                    if (obj != null) {
                        return obj.toString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public JSONArray getCommSensor() {
        JSONArray jsonArray = new JSONArray();
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);  //获取系统的传感器服务并创建实例

        List<Sensor> list = sm.getSensorList(Sensor.TYPE_ALL);  //获取传感器的集合
        for (Sensor sensor : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", isEmptyText(sensor.getType() + ""));
            jsonObject.put("name", isEmptyText(sensor.getName()));
            jsonObject.put("version", isEmptyText(sensor.getVersion() + ""));
            jsonObject.put("vendor", isEmptyText(sensor.getVendor()));
            jsonObject.put("maxRange", isEmptyText(sensor.getMaximumRange() + ""));
            jsonObject.put("minDelay", isEmptyText(sensor.getMinDelay() + ""));
            jsonObject.put("power", isEmptyText(sensor.getPower() + ""));
            jsonObject.put("resolution", isEmptyText(sensor.getResolution() + ""));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public JSONObject getCommAppInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            //获取app名称
            String label = context.getResources().getString(labelRes);
            //获取app versionName
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            String packageName = context.getPackageName();
            jsonObject.put("versionName", isEmptyText(versionName));
            jsonObject.put("versionCode", versionCode);
            jsonObject.put("label", isEmptyText(label));
            jsonObject.put("packageName", isEmptyText(packageName));
            jsonObject.put("sign", isEmptyText(getSingInfo(context.getApplicationContext(), packageName, SHA1)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getCommSms() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, Manifest.permission.READ_SMS)) {
            final String SMS_URI_ALL = "content://sms/";
            try {
                Uri uri = Uri.parse(SMS_URI_ALL);
                String[] projection = new String[]{"_id", "date_sent", "type", "seen", "read", "subject", "status", "address", "person", "body", "date"};
                Cursor cur = context.getContentResolver().query(uri, projection, null, null, "date desc"); // 获取手机内部短信
                if (null == cur) {
                    return null;
                }
                if (cur != null && cur.moveToFirst()) {
                    do {
                        JSONObject jsonObject = new JSONObject();
                        String _id = cur.getString(cur.getColumnIndex("_id"));
                        String date_sent = cur.getString(cur.getColumnIndex("date_sent"));
                        String type = cur.getString(cur.getColumnIndex("type"));
                        String seen = cur.getString(cur.getColumnIndex("seen"));
                        String read = cur.getString(cur.getColumnIndex("read"));
                        String subject = cur.getString(cur.getColumnIndex("subject"));
                        String status = cur.getString(cur.getColumnIndex("status"));
                        String address = cur.getString(cur.getColumnIndex("address"));
                        int person = cur.getInt(cur.getColumnIndex("person"));
                        String body = cur.getString(cur.getColumnIndex("body"));
                        String date = cur.getString(cur.getColumnIndex("date"));

                        jsonObject.put("_id", isEmptyText(_id));
                        jsonObject.put("type", isEmptyText(type));
                        jsonObject.put("seen", isEmptyText(seen));
                        jsonObject.put("read", isEmptyText(read));
                        jsonObject.put("subject", isEmptyText(subject));
                        jsonObject.put("body", isEmptyText(jsonString(body)));
                        jsonObject.put("status", isEmptyText(status));
                        jsonObject.put("address", isEmptyText(address));
                        jsonObject.put("date", isEmptyText(date));
                        jsonObject.put("date_sent", isEmptyText(date_sent));
                        jsonObject.put("person", person);
                        jsonArray.add(jsonObject);
                    } while (cur.moveToNext());
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                } else {
                    return null;
                } // end if
            } catch (SQLiteException ex) {
            }
        }
        return jsonArray;
    }

    public JSONArray getCommCalls() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, Manifest.permission.READ_CALL_LOG)) {
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    new String[]{CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE,
                            CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.IS_READ}, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            boolean hasRecord = null != cursor && cursor.moveToFirst();
            while (hasRecord) {
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String is_read = cursor.getString(cursor.getColumnIndex(CallLog.Calls.IS_READ));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("number", isEmptyText(number));
                jsonObject.put("type", type);
                jsonObject.put("time", date);
                jsonObject.put("duration", duration);
                jsonObject.put("is_read", is_read);
                jsonObject.put("name", isEmptyText(name));
                jsonArray.add(jsonObject);

                hasRecord = cursor.moveToNext();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }
        return jsonArray;
    }

    /**
     * 返回一个签名的对应类型的字符串
     *
     * @param context
     * @param packageName
     * @param type
     *
     * @return
     */
    public static String getSingInfo(Context context, String packageName, String type) {
        String tmp = null;
        Signature[] signs = getSignatures(context, packageName);
        for (Signature sig : signs) {
            if (SHA1.equals(type)) {
                tmp = getSignatureString(sig, SHA1);
                break;
            }
        }
        return tmp;
    }

    /**
     * 返回对应包的签名信息
     *
     * @param context
     * @param packageName
     *
     * @return
     */
    public static Signature[] getSignatures(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String jsonString(String s) {
        s.replaceAll("\"", "'").replaceAll("\"", "'");
        s.replaceAll("“", "'").replaceAll("”", "'");
        return s;
    }
    /**
     * 获取相应的类型的字符串（把签名的byte[]信息转换成16进制）
     *
     * @param sig
     * @param type
     *
     * @return
     */
    public static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        String fingerprint = "error!";
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                byte[] digestBytes = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprint = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return fingerprint;
    }
    
    private String getDriverModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverBrand() {
        try {
            return Build.BRAND;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverProduct() {
        try {
            return Build.PRODUCT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverOsVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverSDKVersion() {
        try {
            return Build.VERSION.SDK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPhysicalSize() {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            return Double.toString(Math.sqrt(Math.pow(((float) displayMetrics.heightPixels) / displayMetrics.ydpi, 2.0d) + Math.pow(((float) displayMetrics.widthPixels) / displayMetrics.xdpi, 2.0d)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getSerialNumber() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            return (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverBoard() {
        try {
            return Build.BOARD;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverSerial() {
        try {
            return Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverDisplay() {
        try {
            return Build.DISPLAY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private String getDriverID() {
        try {
            return Build.ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverBootloader() {
        try {
            return Build.BOOTLOADER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverFingerprint() {
        try {
            return Build.FINGERPRINT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverHost() {
        try {
            return Build.HOST;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverHardWare() {
        try {
            return Build.HARDWARE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverDevice() {
        try {
            return Build.DEVICE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverUser() {
        try {
            return Build.USER;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getRadioVersion() {
        try {
            return Build.getRadioVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverTags() {
        try {
            return Build.TAGS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverTime() {
        try {
            long time = Build.TIME;
            return time + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverType() {
        try {
            return Build.TYPE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getDeviceId() {
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE))
            return "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return getTelephonyManager(context).getImei();
            } else {
                return getTelephonyManager(context).getDeviceId();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getSimSerialNumber() {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = getTelephonyManager(context);
                return tm.getSimSerialNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getAndroidID() {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }
        return "";
    }

    private String getNetworkOperatorName() {
        return getTelephonyManager(context).getNetworkOperatorName();
    }

    private String getNetworkOperator() {
        return getTelephonyManager(context).getNetworkOperator();
    }

    private String getSimCountryIso() {
        return getTelephonyManager(context).getSimCountryIso();
    }

    private String getPhoneType() {
        try {
            TelephonyManager tm = getTelephonyManager(context);
            String phoneTypeStr = "";
            int phoneType = tm.getPhoneType();
            switch (phoneType) {
                case TelephonyManager.PHONE_TYPE_CDMA:
                    phoneTypeStr = "CDMA";
                    break;
                case TelephonyManager.PHONE_TYPE_GSM:
                    phoneTypeStr = "GSM";
                    break;
                case TelephonyManager.PHONE_TYPE_SIP:
                    phoneTypeStr = "SIP";
                    break;
                case TelephonyManager.PHONE_TYPE_NONE:
                    phoneTypeStr = "None";
                    break;
            }
            return phoneTypeStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getLine1Number() {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getLine1Number();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getMCC() {
        try {
            Configuration cfg = context.getResources().getConfiguration();
            return cfg.mcc + "";
        } catch (Exception e) {
        }
        return "";
    }

    private String getMNC() {
        try {
            Configuration cfg = context.getResources().getConfiguration();
            return cfg.mnc + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getOsLanguage() {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getISO3Language() {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getISO3Language();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getISO3Country() {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getISO3Country();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getTimeZoneId() {
        try {
            return TimeZone.getDefault().getID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getLocaleDisplayLanguage() {
        try {
            return Locale.getDefault().getDisplayLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getImsi() {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getSubscriberId();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    private JSONObject getCellInfo() {
        JSONObject jsonObject = new JSONObject();
        if (checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            int dbm = -1;
            int cid = -1;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            List<CellInfo> cellInfoList;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cellInfoList = tm.getAllCellInfo();
                if (null != cellInfoList) {
                    for (CellInfo cellInfo : cellInfoList) {
                        if (cellInfo instanceof CellInfoGsm) {
                            CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthGsm.getDbm();
                            cid = ((CellInfoGsm) cellInfo).getCellIdentity().getCid();
                        } else if (cellInfo instanceof CellInfoCdma) {
                            CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthCdma.getDbm();
                            cid = ((CellInfoCdma) cellInfo).getCellIdentity().getBasestationId();
                        } else if (cellInfo instanceof CellInfoWcdma) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                                dbm = cellSignalStrengthWcdma.getDbm();
                                cid = ((CellInfoWcdma) cellInfo).getCellIdentity().getCid();
                            }
                        } else if (cellInfo instanceof CellInfoLte) {
                            CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthLte.getDbm();
                            cid = ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                        }
                    }
                }
            }
            jsonObject.put("dbm", dbm);
            jsonObject.put("cid", cid);
        }
        return jsonObject;
    }


    private String getKeyboard() {
        try {
            Configuration cfg = context.getResources().getConfiguration();
            return cfg.keyboard + "";
        }catch (Exception e){
            return "";
        }
    }

    private long getLastBootTime() {
        try {
            return System.currentTimeMillis() - SystemClock.elapsedRealtime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    private String getLocalDNS() {
        Process cmdProcess = null;
        BufferedReader reader = null;
        String dnsIP = "";
        try {
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
            dnsIP = reader.readLine();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
            cmdProcess.destroy();
        }
    }

    private String getDriverUUID() {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                final String tmDevice, tmSerial, tmPhone, androidId;
                tmDevice = "" + getDeviceId();
                tmSerial = "" + getSimSerialNumber();
                androidId = "" + getAndroidID();
                UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                return deviceUuid.toString(); // uniqueId
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDriverIMIE() {
        try {
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return getTelephonyManager(context).getDeviceId();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<String> getSystemPhotoList() {
        List<String> result = new ArrayList<String>();
        if (checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor == null || cursor.getCount() <= 0) {
                return null;
            }
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(index);
                File file = new File(path);
                if (file.exists()) {
                    result.add(path);
                }
            }
        }
        return result;
    }
}

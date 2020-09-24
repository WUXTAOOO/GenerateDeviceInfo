package com.generate.deviceinfolib;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.generate.StringUtil.isEmptyText;
import static com.generate.deviceinfolib.Common.checkPermission;
import static com.generate.deviceinfolib.Common.closeSilently;
import static com.generate.deviceinfolib.Common.removeDuplicate;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class ContactInfoUtil {

    private Context context;

    public ContactInfoUtil(Context context) {
        this.context = context;
    }

    public JSONArray getCommContact() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, Manifest.permission.READ_CONTACTS)) {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String custom_ringtone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CUSTOM_RINGTONE));
                    String last_time_contacted = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
                    String send_to_voicemail = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.SEND_TO_VOICEMAIL));
                    String starred = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.STARRED));
                    String times_contacted = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));
                    String has_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String in_visible_group = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP));
                    String is_user_profile = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.IS_USER_PROFILE));
                    String photo_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                    String contact_status = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_STATUS));
                    String contact_status_timestamp = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_STATUS_TIMESTAMP));
                    String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String updated_timestamp = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));

                    StringBuffer sb = new StringBuffer();
                    Cursor phone_number = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    List<String> phoneList = new ArrayList<>();
                    while (null != phone_number && phone_number.moveToNext()) {
                        String strPhoneNumber = phone_number.getString(phone_number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneList.add(strPhoneNumber);
                    }

                    if (phoneList.size() > 0) {
                        phoneList = removeDuplicate(phoneList);
                        for (int i = 0; i < phoneList.size(); i++) {
                            sb.append(phoneList.get(i)).append(",");
                        }
                    }
                    String phones = sb.toString();
                    if (phones.indexOf(",") != -1) {
                        phones = sb.deleteCharAt(sb.length() - 1).toString();
                    }
                    closeSilently(phone_number);
                    jsonObject.put("_id", isEmptyText(contactId));
                    jsonObject.put("number", isEmptyText(phones));
                    jsonObject.put("up_time", isEmptyText(updated_timestamp));
                    jsonObject.put("custom_ringtone", isEmptyText(custom_ringtone));
                    jsonObject.put("last_time_contacted", isEmptyText(last_time_contacted));
                    jsonObject.put("send_to_voicemail", isEmptyText(send_to_voicemail));
                    jsonObject.put("starred", isEmptyText(starred));
                    jsonObject.put("times_contacted", isEmptyText(times_contacted));
                    jsonObject.put("has_phone_number", isEmptyText(has_phone_number));
                    jsonObject.put("in_visible_group", isEmptyText(in_visible_group));
                    jsonObject.put("is_user_profile", isEmptyText(is_user_profile));
                    jsonObject.put("photo_id", isEmptyText(photo_id));
                    jsonObject.put("contact_status", isEmptyText(contact_status));
                    jsonObject.put("contact_status_ts", isEmptyText(contact_status_timestamp));
                    jsonObject.put("contact_display_name", isEmptyText(display_name));
                    jsonArray.add(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeSilently(cursor);
            }
        }
        return jsonArray;
    }

    public JSONArray getCommContactAddress() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CONTACTS)) {
                Uri uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                String[] COLUMNS = new String[]{
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID, ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                        ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME, ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                        ContactsContract.CommonDataKinds.StructuredPostal.REGION, ContactsContract.CommonDataKinds.StructuredPostal.STARRED,
                        ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS};
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID));
                    String country = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                    String address_display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
                    String city = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    String region = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    String address_starred = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STARRED));
                    String formatted_address = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                    jsonObject.put("contact_id", isEmptyText(contactId));
                    jsonObject.put("country", isEmptyText(country));
                    jsonObject.put("display_name", isEmptyText(address_display_name));
                    jsonObject.put("city", isEmptyText(city));
                    jsonObject.put("region", isEmptyText(region));
                    jsonObject.put("starred", isEmptyText(address_starred));
                    jsonObject.put("address", isEmptyText(formatted_address));
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

    public JSONArray getCommContactEmail() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CONTACTS)) {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] COLUMNS = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    jsonObject.put("contact_id", isEmptyText(contact_id));
                    jsonObject.put("display_name", isEmptyText(display_name));
                    jsonObject.put("number", isEmptyText(number));
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


    public JSONArray getCommContactPhone() {
        JSONArray jsonArray = new JSONArray();
        if (checkPermission(context, Manifest.permission.READ_CONTACTS)) {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] COLUMNS = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, COLUMNS, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("contact_id", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                jsonObject.put("display_name", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                jsonObject.put("number", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                jsonArray.add(jsonObject);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        }
        return jsonArray;
    }

    public JSONArray getCommContactsGroup() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CONTACTS)) {
                Uri uri = ContactsContract.Groups.CONTENT_URI;
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String groupId = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
                    Cursor groupCursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, null, ContactsContract.Groups._ID + " = " + groupId, null, null);
                    while (groupCursor.moveToNext()) {
                        String account_name = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME));
                        String auto_add = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.AUTO_ADD));
                        String deleted = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.DELETED));
                        String favorites = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.FAVORITES));
                        String group_visible = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.GROUP_VISIBLE));
                        String group_is_read_only = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.GROUP_IS_READ_ONLY));
                        String notes = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.NOTES));
                        String should_sync = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.SHOULD_SYNC));
                        String account_type = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE));
                        String title = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.TITLE));
                        jsonObject.put("_id", isEmptyText(groupId));
                        jsonObject.put("auto_add", isEmptyText(auto_add));
                        jsonObject.put("deleted", isEmptyText(deleted));
                        jsonObject.put("favorites", isEmptyText(favorites));
                        jsonObject.put("group_is_read_only", isEmptyText(group_is_read_only));
                        jsonObject.put("group_visible", isEmptyText(group_visible));
                        jsonObject.put("notes", isEmptyText(notes));
                        jsonObject.put("should_sync", isEmptyText(should_sync));
                        jsonObject.put("account_type", isEmptyText(account_type));
                        jsonObject.put("account_name", isEmptyText(account_name));
                        jsonObject.put("title", isEmptyText(title));
                        jsonArray.add(jsonObject);
                    }
                    if (groupCursor != null && !groupCursor.isClosed()) {
                        groupCursor.close();
                        groupCursor = null;
                    }
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}

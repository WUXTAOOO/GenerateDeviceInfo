package com.generate.deviceinfolib;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static com.generate.StringUtil.isEmptyText;
import static com.generate.deviceinfolib.Common.checkPermission;

/**
 * @author TAO
 * @desc
 * @since 2020/9/23
 */
public class CalendarsInfoUtil {
    
    private Context context;

    public CalendarsInfoUtil(Context context) {
        this.context = context;
    }

    public JSONArray getCommCalendars() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CALENDAR)) {
                ContentResolver cr = context.getContentResolver();
                Uri uri = CalendarContract.Calendars.CONTENT_URI;
                Cursor cursor = cr.query(uri, null, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String calendars_id = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                    String allowed_attendee_types = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES));
                    String allowed_availability = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ALLOWED_AVAILABILITY));
                    String allowed_reminders = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ALLOWED_REMINDERS));
                    String calendar_access_level = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL));
                    String calendar_time_zone = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE));
                    String visible = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.VISIBLE));
                    String account_type = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                    jsonObject.put("allowed_attendee_types", isEmptyText(allowed_attendee_types));
                    jsonObject.put("allowed_availability", isEmptyText(allowed_availability));
                    jsonObject.put("allowed_reminders", isEmptyText(allowed_reminders));
                    jsonObject.put("calendar_access_level", isEmptyText(calendar_access_level));
                    jsonObject.put("calendar_timezone", isEmptyText(calendar_time_zone));
                    jsonObject.put("visible", isEmptyText(visible));
                    jsonObject.put("account_type", isEmptyText(account_type));
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

    /**
     * 获取设备日历事件
     *
     * @return
     */
    public JSONArray getCommCalendarsEvent() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CALENDAR)) {
                String[] COLUMNS = new String[]{CalendarContract.Events._ID, CalendarContract.Events.ACCESS_LEVEL,
                        CalendarContract.Events.ALL_DAY, CalendarContract.Events.AVAILABILITY, CalendarContract.Events.CALENDAR_ID,
                        CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.DURATION,
                        CalendarContract.Events.EVENT_COLOR, CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Events.EXDATE,
                        CalendarContract.Events.EXRULE, CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, CalendarContract.Events.GUESTS_CAN_MODIFY,
                        CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, CalendarContract.Events.HAS_ALARM, CalendarContract.Events.HAS_ATTENDEE_DATA,
                        CalendarContract.Events.HAS_EXTENDED_PROPERTIES, CalendarContract.Events.LAST_DATE, CalendarContract.Events.RDATE,
                        CalendarContract.Events.RRULE, CalendarContract.Events.STATUS};
                Cursor cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
//                String event_id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID));
                    String access_level = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ACCESS_LEVEL));
                    String all_day = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ALL_DAY));
                    String availability = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.AVAILABILITY));
                    String calendar_id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
                    String dtstart = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                    String dtend = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND));
                    long duration = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DURATION));
                    String event_color = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_COLOR));
                    String event_timezone = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE));
                    String exdate = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EXDATE));
                    String exrule = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EXRULE));
                    String guests_can_invite_others = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS));
                    String guests_can_modify = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.GUESTS_CAN_MODIFY));
                    String guests_can_see_guests = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS));
                    String has_alarm = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.HAS_ALARM));
                    String has_attendee_data = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.HAS_ATTENDEE_DATA));
                    String has_extended_properties = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.HAS_EXTENDED_PROPERTIES));
                    String last_date = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.LAST_DATE));
                    String rdate = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RDATE));
                    String rrule = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RRULE));
                    String status = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.STATUS));

                    jsonObject.put("access_level", isEmptyText(access_level));
                    jsonObject.put("all_day", isEmptyText(all_day));
                    jsonObject.put("availability", isEmptyText(availability));
                    jsonObject.put("calendar_id", isEmptyText(calendar_id));
                    jsonObject.put("dtstart", isEmptyText(dtstart));
                    jsonObject.put("dtend", isEmptyText(dtend));
                    jsonObject.put("duration", duration);
                    jsonObject.put("event_color", isEmptyText(event_color));
                    jsonObject.put("event_timezone", isEmptyText(event_timezone));
                    jsonObject.put("exdate", isEmptyText(exdate));
                    jsonObject.put("exrule", isEmptyText(exrule));
                    jsonObject.put("guests_canInvite_others", isEmptyText(guests_can_invite_others));
                    jsonObject.put("guests_can_modify", isEmptyText(guests_can_modify));
                    jsonObject.put("guests_can_see_guests", isEmptyText(guests_can_see_guests));
                    jsonObject.put("has_alarm", isEmptyText(has_alarm));
                    jsonObject.put("has_attendee_data", isEmptyText(has_attendee_data));
                    jsonObject.put("has_extended_properties", isEmptyText(has_extended_properties));
                    jsonObject.put("last_date", isEmptyText(last_date));
                    jsonObject.put("rdate", isEmptyText(rdate));
                    jsonObject.put("rrule", isEmptyText(rrule));
                    jsonObject.put("event_status", isEmptyText(status));
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

    public JSONArray getCommCalendarAttendees() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CALENDAR)) {
                String[] COLUMNS = new String[]{CalendarContract.Attendees.EVENT_ID, CalendarContract.Attendees.ATTENDEE_RELATIONSHIP,
                        CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.ATTENDEE_STATUS};
                Cursor cursor = context.getContentResolver().query(CalendarContract.Attendees.CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String attendee_relationship = cursor.getString(cursor.getColumnIndex(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP));
                    String attendee_type = cursor.getString(cursor.getColumnIndex(CalendarContract.Attendees.ATTENDEE_TYPE));
                    String attendee_status = cursor.getString(cursor.getColumnIndex(CalendarContract.Attendees.ATTENDEE_STATUS));
                    jsonObject.put("attendee_relationship", isEmptyText(attendee_relationship));
                    jsonObject.put("attendee_type", isEmptyText(attendee_type));
                    jsonObject.put("attendee_status", isEmptyText(attendee_status));
                    jsonArray.add(jsonObject);
                }
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getCommCalendarReminders() {
        JSONArray jsonArray = new JSONArray();
        try {
            if (checkPermission(context, Manifest.permission.READ_CALENDAR)) {
                String[] COLUMNS = new String[]{CalendarContract.Reminders.EVENT_ID, CalendarContract.Reminders.METHOD, CalendarContract.Reminders.MINUTES};
                Cursor cursor = context.getContentResolver().query(CalendarContract.Reminders.CONTENT_URI, COLUMNS, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    String method = cursor.getString(cursor.getColumnIndex(CalendarContract.Reminders.MINUTES));
                    String minutes = cursor.getString(cursor.getColumnIndex(CalendarContract.Reminders.METHOD));
                    jsonObject.put("method", isEmptyText(method));
                    jsonObject.put("minutes", isEmptyText(minutes));
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

}

package com.eveningoutpost.dexdrip.utilitymodels;

import android.app.Notification;
import androidx.core.app.NotificationCompat;

import com.eveningoutpost.dexdrip.models.UserError;

/**
 * Created by jamorham on 18/10/2017.
 */

public class XdripNotificationCompat extends NotificationCompat {

    private final static String TAG = XdripNotificationCompat.class.getSimpleName();

    public static Notification build(NotificationCompat.Builder builder) {
        Notification n = builder.build();
        String id = n.getChannelId();
        if (id == null || (!id.equals(NotificationChannels.BG_ALERT_CHANNEL) && !id.equals(NotificationChannels.OTHER_ALERTS_CHANNEL))) {
            try { id = NotificationChannels.getChan(builder).getId(); }
            catch (Exception e) { id = NotificationChannels.GENERAL_CHANNEL; }
            builder.setChannelId(id);
            n = builder.build();
        }
        builder.setGroup(null);
        builder.setGroupSummary(false);

        UserError.Log.d(TAG, "NotifCompat: chan=" + id +
                " group=" + NotificationCompat.getGroup(n) +
                " summary=" + ((n.flags & Notification.FLAG_GROUP_SUMMARY) != 0));

        return n;
    }
}

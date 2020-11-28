package hiof.prosjekt.minigamebonanza.ui.main.utility;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import hiof.prosjekt.minigamebonanza.R;

public class NotificationBuilder extends AppCompatActivity {

    public static NotificationCompat.Builder createNotificationBuilder(Context context) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "minigameBonanza")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle(context.getResources().getString(R.string.notification_title))
                        .setContentText(context.getResources().getString(R.string.notification_short_description))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.notification_long_description)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(0, notificationBuilder.build());

        return notificationBuilder;
    }
}

package com.nafas.app
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
class WaterReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
 override fun doWork(): Result {
  val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  val channel = "nafas_water"
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) manager.createNotificationChannel(NotificationChannel(channel, "تذكيرات شرب الماء", NotificationManager.IMPORTANCE_DEFAULT))
  val intent = Intent(applicationContext, MainActivity::class.java)
  val pending = PendingIntent.getActivity(applicationContext, 21, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
  val notification = NotificationCompat.Builder(applicationContext, channel)
   .setSmallIcon(R.drawable.ic_stat_drop).setContentTitle("NAFAS | تذكير الماء")
   .setContentText("خذ استراحة قصيرة واشرب كمية مناسبة من الماء.")
   .setContentIntent(pending).setAutoCancel(true).build()
  manager.notify(2101, notification)
  return Result.success()
 }
}

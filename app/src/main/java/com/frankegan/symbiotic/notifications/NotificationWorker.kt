package com.frankegan.symbiotic.notifications

import android.content.Context
import androidx.work.*
import com.frankegan.symbiotic.SymbioticApp
import com.frankegan.symbiotic.data.Fermentation
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

private typealias Success<T> = com.frankegan.symbiotic.data.Result.Success<T>
private typealias Error = com.frankegan.symbiotic.data.Result.Error

private const val FERMENTATION_ID = "FERMENTATION_ID"
private const val REMINDER_TYPE = "REMINDER_TYPE"

/**
 * A [Worker] that displays a notification alerting the user that their fermentation time has elapsed. Use
 * the [NotificationWorker.enqueueWork] function to schedule these notifications.
 */
class NotificationWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val fermentationId = params.inputData.getString(FERMENTATION_ID)!!
    private val reminderType = params.inputData.getString(REMINDER_TYPE)!!
    private val symbioticRepository by lazy { (applicationContext as SymbioticApp).component.symbioticRepository() }

    override suspend fun doWork(): Result {
        val fermentation = when (val result = symbioticRepository.getFermentation(fermentationId)) {
            is Success -> result.data
            is Error -> null
        } ?: return Result.failure()

        showReminderNotification(context, fermentation, ReminderType.valueOf(reminderType))
        return Result.success()
    }

    companion object {
        /**
         * A convenience function for scheduling the first fermentation notification and the
         * second fermentation notification.
         *
         * @param fermentation The fermentation we will be scheduling reminders for.
         * @return The [Operation] for enqueueing the two [NotificationWorker] requests.
         */
        @JvmStatic
        fun enqueueWork(fermentation: Fermentation): Operation {
            //Calculate delay in minute until notification should show
            val firstDelay = LocalDateTime.now().until(fermentation.firstEndDate, ChronoUnit.MINUTES)
            val secondDelay = LocalDateTime.now().until(fermentation.secondEndDate, ChronoUnit.MINUTES)

            val firstReminder = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(firstDelay, TimeUnit.MINUTES)
                .addTag(fermentation.id)
                .setInputData(
                    workDataOf(
                        FERMENTATION_ID to fermentation.id,
                        REMINDER_TYPE to ReminderType.First.label
                    )
                )
                .build()

            val secondReminder = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(secondDelay, TimeUnit.MINUTES)
                .addTag(fermentation.id)
                .setInputData(
                    workDataOf(
                        FERMENTATION_ID to fermentation.id,
                        REMINDER_TYPE to ReminderType.Second.label
                    )
                )
                .build()

            return WorkManager.getInstance().enqueue(listOf(firstReminder, secondReminder))
        }

        /**
         * Cancels work for the specified fermentation.
         */
        fun cancelWork(fermentation: Fermentation) = WorkManager.getInstance().cancelAllWorkByTag(fermentation.id)
    }
}
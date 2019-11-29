package com.frankegan.symbiotic.notifications

import android.content.Context
import androidx.work.*
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.data.SymbioticRepository
import com.frankegan.symbiotic.di.injector
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private typealias Success<T> = com.frankegan.symbiotic.data.Result.Success<T>
private typealias Error = com.frankegan.symbiotic.data.Result.Error

private const val FERMENTATION_ID = "FERMENTATION_ID"
private const val REMINDER_TYPE = "REMINDER_TYPE"

/**
 * A [Worker] that displays a notification alerting the user that their fermentation time has elapsed. Use
 * the [NotificationWorker.enqueueWork] function to schedule these notifications.
 */
class NotificationWorker(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    init {
        injector.inject(this)
    }

    @Inject
    lateinit var symbioticRepository: SymbioticRepository
    private val fermentationId = params.inputData.getString(FERMENTATION_ID)
        ?: throw IllegalArgumentException("Missing fermentationId")
    private val reminderType = params.inputData.getString(REMINDER_TYPE)
        ?: throw IllegalArgumentException("Missing reminderType")

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
        fun enqueueWork(context: Context, fermentation: Fermentation): Operation {
            //Calculate delay in minute until notification should show
            val firstDelay =
                LocalDateTime.now().until(fermentation.firstEndDate, ChronoUnit.MINUTES)
            val secondDelay =
                LocalDateTime.now().until(fermentation.secondEndDate, ChronoUnit.MINUTES)

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

            return WorkManager.getInstance(context).enqueue(listOf(firstReminder, secondReminder))
        }

        /**
         * Cancels work for the specified fermentation.
         */
        @JvmStatic
        fun cancelWork(context: Context, fermentation: Fermentation) =
            WorkManager.getInstance(context).cancelAllWorkByTag(fermentation.id)
    }
}
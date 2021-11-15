package dev.astler.unlib_test.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.astler.unlib.utils.toast

class PeriodicTestWorker(pAppContext: Context, workerParams: WorkerParameters) :
    Worker(pAppContext, workerParams) {

    private val mInnerContext = pAppContext

    override fun doWork(): Result {
        mInnerContext.toast("Periodic Toast!")
        return Result.success()
    }
}

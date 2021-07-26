package dev.astler.unlib_test.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.astler.unlib.utils.makeToast

class PeriodicTest2Worker(pAppContext: Context, workerParams: WorkerParameters) :
    Worker(pAppContext, workerParams) {

    private val mInnerContext = pAppContext

    override fun doWork(): Result {
        mInnerContext.makeToast("Periodic Toast 2!")
        return Result.success()
    }
}
package dev.astler.unlib_test.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dev.astler.catlib.extensions.toast

class PeriodicTest2Worker(pAppContext: Context, workerParams: WorkerParameters) :
    Worker(pAppContext, workerParams) {

    private val mInnerContext = pAppContext

    override fun doWork(): Result {
        mInnerContext.toast("Periodic Toast 2!")
        return Result.success()
    }
}

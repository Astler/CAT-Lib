package dev.astler.unli_text

import android.os.Bundle
import dev.astler.unli.ui.activity.BaseUnLiActivity
import dev.astler.unli.view.ShortCodeTextView

class MainActivity : BaseUnLiActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ShortCodeTextView>(R.id.test).text = """
            Okay. Let's try that! [img src=file/]
            New I will add few similar files [img src=file tint=#FF0000/][img src=file tint=#00FF00/][img src=file tint=#0000FF/]
            And now - error flag = [img src=faile/] and error flag with color [img src=faile tint=#00FFFF/]

        """.trimIndent()
    }

    override fun backPressed(endAction: () -> Unit) {}

    override fun setToolbarTitle(title: String) {}
}
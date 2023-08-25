package dev.astler.unlib_test.fragments.views

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.cat_ui.utils.getDimensionFromAttr
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.FragmentShortCodePreviewBinding

@AndroidEntryPoint
class ShortCodePreviewFragment : CatFragment<FragmentShortCodePreviewBinding>(FragmentShortCodePreviewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testText =
            getString(R.string.strange_string) + safeContext.getDimensionFromAttr(androidx.appcompat.R.attr.actionBarSize) + "\n" + binding.toolbar.height

        binding.test.text = testText
    }
}
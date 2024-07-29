package dev.astler.unlib_test.fragments.views

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.fragments.CatFragment
import com.ao.subscribeme.R
import com.ao.subscribeme.databinding.FragmentShortCodePreviewBinding

@AndroidEntryPoint
class ShortCodePreviewFragment : CatFragment<FragmentShortCodePreviewBinding>(FragmentShortCodePreviewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.test.text = getString(R.string.strange_string)
    }
}
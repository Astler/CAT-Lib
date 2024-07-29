package dev.astler.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

abstract class BindingCatActivity<T : ViewBinding>(private val bindingInflater: (LayoutInflater) -> T) : CatActivity() {

    protected lateinit var binding: T
        private set

    protected inline fun <reified T : ViewBinding> inflateBinding(): T {
        return T::class.java.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
    }
}

package dev.astler.cat_ui.activities

import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

abstract class CatHiltActivity<VB : ViewBinding>(@LayoutRes layoutId: Int) : CatActivity<VB>(layoutId)

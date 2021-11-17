package com.mmunoz.todo.presentation.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import com.mmunoz.todo.databinding.PhotoItemLayoutBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PhotoImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var data: Uri

    private val binding = PhotoItemLayoutBinding.inflate(LayoutInflater.from(context), this)

    @ModelProp
    fun setData(data: Uri) {
        this.data = data
    }

    @AfterPropsSet
    fun bindData() {
        Glide.with(this)
            .load(data)
            .into(binding.imageView)
    }
}
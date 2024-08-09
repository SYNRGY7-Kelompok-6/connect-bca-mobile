package com.team6.connectbca.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentQrisBinding
import com.team6.connectbca.databinding.FragmentShowQrBinding
import com.team6.connectbca.ui.viewmodel.QrisViewModel
import com.team6.connectbca.ui.viewmodel.ShowQrViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowQrFragment : Fragment() {
    private lateinit var binding: FragmentShowQrBinding

    private val viewModel by viewModel<ShowQrViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowQrBinding.inflate(inflater, container, false)
        var isDarkTheme = checkTheme()
        getQrImage(isDarkTheme)
        viewModel.qrImage.observe(viewLifecycleOwner) {
            loadImage(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateToolbar()

    }

    private fun loadImage(image: String) {
        Glide.with(this)
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivQrCode)
    }

    private fun checkTheme(): Boolean {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            androidx.appcompat.R.attr.isLightTheme,
            typedValue,
            true
        )
        val isLightTheme = typedValue.data == -1
        var isDarkTheme = !isLightTheme
        return isDarkTheme
    }

    private fun initiateToolbar() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Tampilkan QR"
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }

    private fun getQrImage(isDarkTheme: Boolean) {
        viewModel.showQr(isDarkTheme)
    }

}
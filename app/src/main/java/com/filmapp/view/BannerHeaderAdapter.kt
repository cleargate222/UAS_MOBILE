package com.filmapp.view

import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.filmapp.databinding.LayoutBannerHeaderBinding
import com.filmapp.model.Film
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
class BannerHeaderAdapter constructor(
    private val onBannerClick: (Film) -> Unit
) : RecyclerView.Adapter<BannerHeaderAdapter.HeaderViewHolder>() {

    private var films: List<Film> = emptyList()
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private var viewPager: ViewPager2? = null

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val vp = viewPager ?: return
            if (films.isEmpty()) return
            currentPage = (currentPage + 1) % films.size
            vp.setCurrentItem(currentPage, true)
            handler.postDelayed(this, AUTO_SCROLL_DELAY)
        }
    }

    inner class HeaderViewHolder(val binding: LayoutBannerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = LayoutBannerHeaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        val bannerAdapter = BannerAdapter(films, onBannerClick)
        holder.binding.bannerPager.adapter = bannerAdapter
        viewPager = holder.binding.bannerPager

        setupDots(holder)

        holder.binding.bannerPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(pos: Int) {
                currentPage = pos
                updateDots(holder, pos)
            }
        })

        startAutoScroll()
    }

    override fun getItemCount() = if (films.isEmpty()) 0 else 1

    fun updateFilms(newFilms: List<Film>) {
        films = newFilms.take(5) // max 5 banner
        currentPage = 0
        notifyDataSetChanged()
    }

    private fun setupDots(holder: HeaderViewHolder) {
        holder.binding.dotsContainer.removeAllViews()
        val context = holder.binding.root.context
        val dp3 = (3 * context.resources.displayMetrics.density).toInt()

        // Ambil colorOnSurface dari theme untuk dot inactive
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
        val colorOnSurface = typedValue.data

        films.forEachIndexed { index, _ ->
            val dot = ImageView(context).apply {
                if (index == 0) {
                    setImageResource(com.filmapp.R.drawable.dot_active_pill)
                } else {
                    setImageResource(com.filmapp.R.drawable.dot_inactive_pill)
                    setColorFilter(colorOnSurface)
                    alpha = 0.3f
                }
                val params = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(dp3, 0, dp3, 0)
                layoutParams = params
            }
            holder.binding.dotsContainer.addView(dot)
        }
    }

    private fun updateDots(holder: HeaderViewHolder, activeIndex: Int) {
        val context = holder.binding.root.context
        val typedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
        val colorOnSurface = typedValue.data

        val container = holder.binding.dotsContainer
        for (i in 0 until container.childCount) {
            val dot = container.getChildAt(i) as? ImageView ?: continue
            if (i == activeIndex) {
                dot.setImageResource(com.filmapp.R.drawable.dot_active_pill)
                dot.clearColorFilter()
                dot.alpha = 1f
            } else {
                dot.setImageResource(com.filmapp.R.drawable.dot_inactive_pill)
                dot.setColorFilter(colorOnSurface)
                dot.alpha = 0.3f
            }
        }
    }

    fun startAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
        if (films.size > 1) handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
    }

    fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 3000L
    }
}

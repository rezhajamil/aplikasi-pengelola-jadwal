package com.example.pengingatjadwal.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.pengingatjadwal.Activity.BantuanActivity
import com.example.pengingatjadwal.Model.ScreenItem
import com.example.pengingatjadwal.R

class BantuanViewPagerAdapter(val context: Context, var listScreen: List<ScreenItem>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val layoutScreenView: View = inflater.inflate(R.layout.layout_screen_bantuan, null)

        val ivImageSlide = layoutScreenView.findViewById<ImageView>(R.id.iv_intro)
        val tvTitle = layoutScreenView.findViewById<TextView>(R.id.tv_intro_title)
        val tvDesc = layoutScreenView.findViewById<TextView>(R.id.tv_intro_description)
        val tvSubDesc = layoutScreenView.findViewById<TextView>(R.id.tv_intro_subdesc)

        tvTitle.text = listScreen[position].title
        tvDesc.text = listScreen[position].desc
        tvSubDesc.text = listScreen[position].subdesc
        ivImageSlide.setImageResource(listScreen[position].screenImage)

        container.addView(layoutScreenView)

        return layoutScreenView
    }

    override fun getCount(): Int {
        return listScreen.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
package com.muiezarif.kidsfitness.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.halilibo.bvpkotlin.BetterVideoPlayer
import com.halilibo.bvpkotlin.VideoCallback
import com.halilibo.bvpkotlin.VideoProgressCallback
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.LessonPartVideo
import com.muiezarif.kidsfitness.utils.toast
import kotlinx.android.synthetic.main.lesson_part_vp_pager.view.*

class LessonPartsPagerAdapter(var lessonParts:ArrayList<LessonPartVideo>,var context: Context, var genericAdapterCallback: GenericAdapterCallback): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return lessonParts.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewItems: View =
            inflater.inflate(R.layout.lesson_part_vp_pager, container, false)
        var model = lessonParts[position]
        viewItems.bvpLessonPartPlayer.setSource(Uri.parse(model.lessonVid))
//        viewItems.bvpLessonPartPlayer.setAutoPlay(true)
        viewItems.bvpLessonPartPlayer.setCallback(object:VideoCallback{
            override fun onBuffering(percent: Int) {

            }

            override fun onCompletion(player: BetterVideoPlayer) {
                genericAdapterCallback.getClickedObject(model,position,"LessonPartVideoCompleted")
            }

            override fun onError(player: BetterVideoPlayer, e: Exception) {

            }

            override fun onPaused(player: BetterVideoPlayer) {

            }

            override fun onPrepared(player: BetterVideoPlayer) {
                player.start()
            }

            override fun onPreparing(player: BetterVideoPlayer) {

            }

            override fun onStarted(player: BetterVideoPlayer) {

            }

            override fun onToggleControls(player: BetterVideoPlayer, isShowing: Boolean) {

            }

        })
        viewItems.bvpLessonPartPlayer.setProgressCallback(object:VideoProgressCallback{
            override fun onProgressUpdate(position: Int, duration: Int) {
            }
        })
//        val img_Poems: ImageView = viewItems
//            .findViewById<View>(R.id.poem_items) as ImageView
//        img_Poems.setBackgroundResource(poemName.get(position).getImg())
        container.addView(viewItems)
        return viewItems
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as ConstraintLayout)
    }

}
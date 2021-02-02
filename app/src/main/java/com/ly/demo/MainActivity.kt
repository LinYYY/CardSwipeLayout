package com.ly.demo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ly.cardswipelayout.cardswipe.*
import com.ly.cardswipelayout.cardswipe.adapter.BaseAdapter
import com.ly.cardswipelayout.cardswipe.adapter.CommonViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var cardItemTouchCallback: CardItemTouchCallback<String>

    private val adapter by lazy {
        TestAdapter(this).apply {
            addAll(
                arrayListOf(
                    "https://hbimg.huabanimg.com/30d98122036b0355dd26618dbd4cfab5798dcf86284cf-eSXJaN_fw658",
                    "https://images.jiulou.men/uploads/18257/1b530ea617775e60884023345e8b0119.jpg",
                    "https://i.pinimg.com/originals/6a/17/75/6a17751aea3c0312cd1b7e6ef644bb31.jpg",
                    "https://lh3.googleusercontent.com/proxy/PrgKgNgrAi7Cd4unLZYwpVV_FWPrXTgoFiunOQYwBniKuCkWgctO4qSHPYTpJ3exPWqjsHXbABNwGynZQ5NCf3lT-QNKbIp2_l7HJWQVXA0IiL8DEZpkut9Qxgk",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBmH3k-HtcGtTh7S3UFb2t-b54U1KN-SuWOw&usqp=CAU",
                    "https://hbimg.huabanimg.com/30d98122036b0355dd26618dbd4cfab5798dcf86284cf-eSXJaN_fw658",
                    "https://images.jiulou.men/uploads/18257/1b530ea617775e60884023345e8b0119.jpg",
                    "https://i.pinimg.com/originals/6a/17/75/6a17751aea3c0312cd1b7e6ef644bb31.jpg",
                    "https://lh3.googleusercontent.com/proxy/PrgKgNgrAi7Cd4unLZYwpVV_FWPrXTgoFiunOQYwBniKuCkWgctO4qSHPYTpJ3exPWqjsHXbABNwGynZQ5NCf3lT-QNKbIp2_l7HJWQVXA0IiL8DEZpkut9Qxgk",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBmH3k-HtcGtTh7S3UFb2t-b54U1KN-SuWOw&usqp=CAU"
                ),
                false
            )
        }
    }

    private val itemTouchHelper by lazy {
        cardItemTouchCallback = CardItemTouchCallback<String>(
            adapter,
            recycler_view
        )
        ItemTouchHelper(cardItemTouchCallback)
    }

    private val revokeList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemTouchHelper.attachToRecyclerView(recycler_view)
        recycler_view.itemAnimator = CardItemAnimator()
        recycler_view.layoutManager = CardLayoutManager(recycler_view, itemTouchHelper)
        recycler_view.adapter = adapter

        cardItemTouchCallback.listener = object : OnSwipeListener<String> {

            override fun canSwipeAway(
                viewHolder: RecyclerView.ViewHolder,
                t: String,
                direction: Int,
                extraFlag: Int
            ): Boolean {
                //判断当前的数据是否可以划走
                return true
            }

            override fun onSwiping(
                viewHolder: RecyclerView.ViewHolder,
                ratio: Float,
                direction: Int
            ) {
                //正在滑动手势回调 可以处理动画

            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                t: String,
                direction: Int,
                restCount: Int,
                extraFlag: Int
            ) {
                // 划走之后的处理
                revokeList.add(t)
            }

            override fun onSwipedClear() {
                //空数据的时候处理
            }

            override fun onSwipeBlock(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //滑动被拦截之后可以处理一些操作
            }

        }

        btn_left.setOnClickListener {
            if (this::cardItemTouchCallback.isInitialized) {
                cardItemTouchCallback.handleCardSwipe(
                    CardConfig.SWIPING_LEFT,
                    500,
                    0
                )
            }
        }

        btn_right.setOnClickListener {
            if (this::cardItemTouchCallback.isInitialized) {
                cardItemTouchCallback.handleCardSwipe(
                    CardConfig.SWIPING_RIGHT,
                    500,
                    0
                )
            }
        }

        btn_revoke.setOnClickListener {
            if (revokeList.isNotEmpty()) {
                val bean = revokeList.removeAt(0)
                adapter.add(0, bean)
            }
        }

    }

    private class TestAdapter(context: Context) :
        BaseAdapter<String>(context, R.layout.item_layout) {

        override fun convert(holder: CommonViewHolder, bean: String, position: Int) {
            Glide.with(context).load(bean).centerCrop().into(holder.getView<ImageView>(R.id.iv_image))
        }
    }
}

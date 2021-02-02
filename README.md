## CardSwipeLayout

仿探探左右滑卡片布局 支持回撤 支持阻止滑动操作
```kotlin

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
```
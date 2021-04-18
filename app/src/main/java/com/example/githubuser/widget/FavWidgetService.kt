package com.example.githubuser.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.db.GitHubUserDatabase
import com.example.githubuser.data.model.User

class FavWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        FavWidgetItemFactory(applicationContext, intent as Intent)

    class FavWidgetItemFactory(
        private val mContext: Context,
        intent: Intent
    ) :
        RemoteViewsFactory {

        private val mUsers = mutableListOf<User>()

        private lateinit var db: GitHubUserDatabase

        override fun onCreate() {
            db = GitHubUserDatabase.getDatabase(mContext)
        }

        override fun onDataSetChanged() {
            mUsers.addAll(db.getUserDao().getAllUsersWidget())
        }

        override fun onDestroy() {
            db.close()
        }

        override fun getCount(): Int = mUsers.size

        override fun getViewAt(position: Int): RemoteViews {
            val user = mUsers[position]
            val views = RemoteViews(mContext.packageName, R.layout.widget_item)
            views.setTextViewText(R.id.tv_user_login_widget, user.login)
            views.setImageViewBitmap(R.id.image_view_widget, loadUserImage(user.avatar_url))

            return views
        }

        private fun loadUserImage(imgUrl: String): Bitmap {
            return Glide.with(mContext.applicationContext).asBitmap()
                .load(imgUrl).circleCrop().submit(110, 110).get()
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = true
    }
}
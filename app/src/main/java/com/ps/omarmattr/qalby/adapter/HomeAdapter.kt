package com.ps.omarmattr.qalby.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.databinding.ItemHomeBinding
import com.ps.omarmattr.qalby.model.home.HomeItem
import com.ps.omarmattr.qalby.model.home.social.SocialItem
import kotlinx.android.synthetic.main.item_home.view.*


class HomeAdapter(var arrayList: ArrayList<HomeItem>, private val click: OnClickHome) :
    RecyclerView.Adapter<HomeAdapter.HomeAdapterVH>() {
    inner class HomeAdapterVH(private val mItemView: ItemHomeBinding) :
        RecyclerView.ViewHolder(mItemView.root) {

        fun bind(itemHome: HomeItem) {
            mItemView.itemHome = itemHome
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterVH {
        return HomeAdapterVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_home, parent, false
            )
        )
    }

    interface OnClickHome {
        fun onClick(itemHome: HomeItem)
    }

    override fun onBindViewHolder(holder: HomeAdapterVH, position: Int) {
        holder.bind(arrayList[position])
        holder.itemView.apply {
            rcInnerHome.apply {
                val mAdapter = GenericAdapter(
                    R.layout.inner_item_home,
                    BR.social,
                    object : GenericAdapter.OnListItemViewClickListener<SocialItem> {
                        override fun onClickItem(
                            itemViewModel: SocialItem,
                            type: Int,
                            position: Int
                        ) {
                            click.onClick(arrayList[position])
                        }

                    })
//                if (position == 1) layoutManager =
//                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                mAdapter.data = arrayList[position].social
                adapter = mAdapter
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}
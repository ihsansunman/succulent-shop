package school.cactus.succulentshop.infra

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import school.cactus.succulentshop.product.ProductItem

abstract class BaseAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseViewHolder<T>>(diffCallback) {

    open var itemClickListener: (ProductItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) =
        holder.bind(getItem(position))
}
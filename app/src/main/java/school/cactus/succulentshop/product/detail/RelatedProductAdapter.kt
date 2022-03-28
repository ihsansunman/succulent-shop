package school.cactus.succulentshop.product.detail

import androidx.recyclerview.widget.DiffUtil
import school.cactus.succulentshop.R
import school.cactus.succulentshop.infra.BaseAdapter
import school.cactus.succulentshop.infra.BaseViewHolder
import school.cactus.succulentshop.product.ProductItem

class RelatedProductAdapter : BaseAdapter<ProductItem>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem) =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int) = R.layout.item_related_product

    override fun onBindViewHolder(holder: BaseViewHolder<ProductItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            itemClickListener(getItem(position))
        }
    }
}
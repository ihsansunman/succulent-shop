package school.cactus.succulentshop

import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.infra.BaseAdapter
import school.cactus.succulentshop.product.ProductItem

@BindingAdapter("app:error")
fun TextInputLayout.error(@StringRes errorMessage: Int?) {
    error = errorMessage?.resolveAsString(context)
    isErrorEnabled = errorMessage != null
}

@BindingAdapter("app:adapter", "app:products", "app:itemClickListener", "app:decoration")
fun RecyclerView.setProductData(
    productAdapter: BaseAdapter<ProductItem>,
    products: List<ProductItem>?,
    itemClickListener: (ProductItem) -> Unit,
    itemDecoration: RecyclerView.ItemDecoration
) {
    if (adapter != productAdapter) {
        adapter = productAdapter
    }

    if (itemDecorationCount == 0) {
        addItemDecoration(itemDecoration)
    }

    productAdapter.itemClickListener = itemClickListener
    productAdapter.submitList(products.orEmpty())
}

@BindingAdapter("app:imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
            .override(512)
            .into(this)
    }
}